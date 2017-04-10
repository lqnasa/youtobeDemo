package com.onemt.crawler.main;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.onemt.crawler.pojo.PageArticle;
import com.onemt.crawler.pojo.PageImage;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.JsonPathSelector;

public class MyProcessor implements PageProcessor {

	private final static String SEARCH_URL = "https://www.googleapis.com/youtube/v3/search";
	private final static String GET_VIDEO_INFO_URL = "https://www.youtube.com/get_video_info?video_id=";
	private final static String VIDEO_URL = "https://www.googleapis.com/youtube/v3/videos?part=snippet,statistics";

	private static String apiKey = ProjectConfigUtil.get("youtube.apiKey");

	private Site site = Site.me().setDomain("www.googleapis.com")
			.setUserAgent("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36")
			.setTimeOut(30000).setCycleRetryTimes(3);

	@Override
	public Site getSite() {
		return site;
	}

	@Override
	public void process(Page page) {
		String url = page.getRequest().getUrl();
		Map<String, Object> extras = getExtras(page);

		// 1. 获取youtube 返回的视频列表
		if (url.contains(SEARCH_URL)) {
			// 抽取视频id
			List<String> items = page.getJson().jsonPath("$.items[*]").all();
			for (String item : items) {
				String videoId = new JsonPathSelector("$.id.videoId").select(item);
				Request request = new Request(GET_VIDEO_INFO_URL + videoId);
				request.setExtras(extras);
				page.addTargetRequest(request);
			}

			// 翻页处理
			if (page.getJson().get().contains("nextPageToken")) {
				String nextPageToken = new JsonPathSelector("$.nextPageToken").select(page.getJson().get());
				String nextUrl = page.getRequest().getUrl();
				Request request = new Request(nextUrl.replaceAll("&pageToken=\\S+", "") + "&pageToken=" + nextPageToken);
				request.setExtras(extras);
				page.addTargetRequest(request);
			}
			page.setNeedCycleRetry(true);
			// skip==true不会进入MyPipeline进行数据保存
			page.setSkip(true);

		}

		// 2.判定视频是否可用
		if (url.contains(GET_VIDEO_INFO_URL)) {
			String str = page.getRawText();
			page.setSkip(true);
			// 存在errorcode,表示该视频无法播放
			if (str.contains("errorcode")) {
				return;
			}

			Map<String, String> paramMap = getParamMap(str);
			// 作者
			String author = paramMap.get("author");
			// 视频时长(s)
			String lengthSeconds = paramMap.get("length_seconds");
			String videoId = paramMap.get("video_id");
			// part参数 :
			// https://developers.google.com/youtube/v3/getting-started#partial
			// https://www.googleapis.com/youtube/v3/videos?id=scWpXEYZEGk&part=id,contentDetails,snippet,statistics&key=AIzaSyAPjmHyYPp9iy4mNhWwhHtIYX-ZfcdG2fE
			StringBuilder sb = new StringBuilder(VIDEO_URL);
			sb.append("&id=" + videoId).append("&key=" + apiKey);

			Request request = new Request(sb.toString());
			extras.put("author", author);
			extras.put("videoTime", lengthSeconds);
			extras.put("videoId", videoId);
			request.setExtras(extras);
			page.setNeedCycleRetry(true);
			page.addTargetRequest(request);

		}

		// 该接口可以不用 上面请求获取的信息包含所有内容
		// 3.获取视频的详细信息封装到pageArticle对象中
		// https://developers.google.com/youtube/v3/docs/videos
		// https://developers.google.com/youtube/v3/docs/videos#resource
		if (url.contains(VIDEO_URL)) {
			processDetail(page);
		}

	}
	
	
	public Map<String, String> getParamMap(String rawText) {
		Map<String, String> paramMap = new HashMap<>();
		/*
		 * try { rawText = URLDecoder.decode(rawText, "UTF-8"); } catch
		 * (UnsupportedEncodingException e) { e.printStackTrace(); }
		 */

		String[] params = rawText.split("&");
		for (String param : params) {
			String[] p = param.split("=");
			if (p.length > 1) {
				paramMap.put(p[0], p[1]);
			}
		}

		return paramMap;
	}

	private void processDetail(Page page) {

		String rawText = page.getRawText();

		// 封装个pageArticle对象
		String title = new JsonPathSelector("$.items[0].snippet.title").select(rawText);
		String publishedAt = new JsonPathSelector("$.items[0].snippet.publishedAt").select(rawText);
		String description = new JsonPathSelector("$.items[0].snippet.description").select(rawText);
		// 获取默认图片
		String imageUrl = new JsonPathSelector("$.items[0].snippet.thumbnails.default.url").select(rawText);
		// 获取640x480
		if (rawText.contains("standard")) {
			imageUrl = new JsonPathSelector("$.items[0].snippet.thumbnails.standard.url").select(rawText);
		}
		// 获取1280x720
		/*
		 * if (rawText.contains("maxres")) { imageUrl = new
		 * JsonPathSelector("$.items[0].snippet.thumbnails.maxres.url").select(
		 * rawText); }
		 */
		String readCount = new JsonPathSelector("$.items[0].statistics.viewCount").select(rawText);
		String likeCount = new JsonPathSelector("$.items[0].statistics.likeCount").select(rawText);
		String commentCount = new JsonPathSelector("$.items[0].statistics.commentCount").select(rawText);

		String videoId = (String) page.getRequest().getExtra("videoId");
		String author = (String) page.getRequest().getExtra("author");
		String videoTime = (String) page.getRequest().getExtra("videoTime");

		PageArticle pageArticle = new PageArticle();
		// pageArticle.setDate(DateUtils.getTimeFromStr(publishedAt,"yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")+"");
		pageArticle.setHumanLanguage("ar");
		pageArticle.setTitle(title);
		// pageArticle.setType(CrawlerArticle.TYPE_VIDEO);

		try {
			pageArticle.setAuthor(URLDecoder.decode(author, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		pageArticle.setVideoId(videoId);
		pageArticle.setVideoTime(Integer.valueOf(videoTime));
		pageArticle.setDiscussion(description);
		pageArticle.setReadCount(Integer.valueOf(readCount));
		pageArticle.setLikeCount(Integer.valueOf(likeCount));
		pageArticle.setCommentCount(Integer.valueOf(commentCount));
		pageArticle.setPageUrl("https://www.youtube.com/embed/" + videoId);

		// 保存视频图片
		List<PageImage> pageImages = new ArrayList<>();
		PageImage pageImage = new PageImage();
		pageImage.setPrimary(true);
		pageImage.setUrl(imageUrl);
		pageImages.add(pageImage);

		pageArticle.setImages(pageImages);

		// 标签
		List<String> tags = new JsonPathSelector("$.items[0].snippet.tags[*]").selectList(rawText);
		detailFetch(page, pageArticle, tags);
	}


	protected Map<String, Object> getExtras(Page page) {
		Map<String, Object> extras = new HashMap<String, Object>();
		extras.put("mediaId", page.getRequest().getExtra("mediaId"));
		extras.put("mediaName", page.getRequest().getExtra("mediaName"));
		extras.put("mediaIcon", page.getRequest().getExtra("mediaIcon"));
		extras.put("mediaCategoryId", page.getRequest().getExtra("mediaCategoryId"));
		extras.put("newsCategoryId", page.getRequest().getExtra("newsCategoryId"));
		extras.put("newsRegionId", page.getRequest().getExtra("newsRegionId"));
		extras.put("sourceCategory", page.getRequest().getExtra("sourceCategory"));
		extras.put("categoryId", page.getRequest().getExtra("categoryId"));
		extras.put("subcategoryId", page.getRequest().getExtra("subcategoryId"));
		extras.put("region", page.getRequest().getExtra("region"));
		extras.put("type", page.getRequest().getExtra("type"));
		return extras;
	}

	/**
	 * 
	 * @Title: detailFetch @Description: 组装文章页面详情,放入page @param @param
	 *         page @param @param pageArticle @param @param tags @return
	 *         void @throws
	 */
	protected void detailFetch(Page page, PageArticle pageArticle, List<String> tags) {
		if (null != pageArticle) {
			pageArticle.setTags(tags);
			page.putField("pageArticle", pageArticle);
			page.putField("mediaId", page.getRequest().getExtra("mediaId"));
			page.putField("mediaName", page.getRequest().getExtra("mediaName"));
			page.putField("mediaIcon", page.getRequest().getExtra("mediaIcon"));
			page.putField("mediaCategoryId", page.getRequest().getExtra("mediaCategoryId"));
			page.putField("newsCategoryId", page.getRequest().getExtra("newsCategoryId"));
			page.putField("newsRegionId", page.getRequest().getExtra("newsRegionId"));
			page.putField("sourceCategory", page.getRequest().getExtra("sourceCategory"));
			page.putField("categoryId", page.getRequest().getExtra("categoryId"));
			page.putField("subcategoryId", page.getRequest().getExtra("subcategoryId"));
			page.putField("region", page.getRequest().getExtra("region"));
			page.putField("type", page.getRequest().getExtra("type"));
		}
	}

	public static void main(String[] args) {

		Calendar date = Calendar.getInstance();
		date.setTime(new Date());
		date.add(Calendar.MONTH, -4);
		SimpleDateFormat rfcSdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		String publishedAfterDate = rfcSdf.format(date.getTime());

		//构造url
		StringBuilder sb=new StringBuilder();
		sb.append("https://www.googleapis.com/youtube/v3/search?type=video&part=id&maxResults=50&videoDefinition=high")
		//.append("&order=viewCount")
		//.append("&relevanceLanguage=ar")
		.append("&safeSearch=moderate")
		//.append("&videoLicense=youtube")
		//.append("&videoSyndicated=true")
		//该publishedAfter参数指示API响应应仅包含在指定时间或之后创建的资源。
		//yyyy-MM-dd'T'HH:mm:ss'Z'
		.append("&publishedAfter="+publishedAfterDate)
		.append("&key="+apiKey)
		//.append(url)
		;
		/*
		//该videoCategoryId参数根据其类别过滤视频搜索结果。
		//videoCategoryId 如果为此参数指定值，那么还必须将type参数的值设置为video。
		.append("videoCategoryId=10")
	    
		//用于搜索查询的区域代码。
		//伊朗Iran IR 伊拉克Iraq IQ  科威特Kuwait; KW
		//沙特阿拉伯Saudi Arabia SA 阿拉伯联合酋长国The United Arab Emirates  AE
		//阿曼Oman  OM 卡塔尔Qatar  QA 巴林Bahrain	BH  埃及 EG
		//.append("&regionCode=AE") 
		
		//该topicId参数指示API响应应仅包含与指定主题相关联的资源。该值标识一个Freebase主题ID。
		//https://developers.google.com/youtube/v3/docs/search/list
		//relevantTopicIds
		.append("&topicIds=/m/04rlf")
		
		//http://vidstatsx.com/ youtube频道排行板
		//https://www.zhihu.com/question/19609089?sort=created
		//https://www.googleapis.com/youtube/v3/guideCategories?key=AIzaSyAPjmHyYPp9iy4mNhWwhHtIYX-ZfcdG2fE&part=snippet&regionCode=ar
		.append("&channelId=UCiSkZCmdoaMb_FbhTPAJ-rg");*/
		
		//传参
		Request request = new Request(sb.toString())
//				.putExtra(Request.CYCLE_TRIED_TIMES, 1000)
//				.putExtra("name", "")
//				.putExtra("regex", "")
//				.putExtra("mediaCategoryId", "")
//				.putExtra("mediaId", "")
//				.putExtra("mediaName", "")
//				.putExtra("mediaIcon", "")
//				.putExtra("newsCategoryId", "")
//				.putExtra("newsRegionId", "")
//				.putExtra("sourceCategory", "")
//				.putExtra("extend", "")
//				.putExtra("categoryId", "")
//				.putExtra("subcategoryId", "")
//				.putExtra("region", "")
//				.putExtra("type", "")
				;
		
		Spider.create(new MyProcessor()).addRequest(request).addPipeline(new MyPipeline()).thread(10).run();

	}

}
