package com.onmet.crawler.main;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.JsonPathSelector;

import com.onmet.crawler.pojo.PageArticle;
import com.onmet.crawler.pojo.PageImage;
import com.onmet.crawler.pojo.PageVideo;

public class MyProcessor implements PageProcessor{

	private Site site=Site.me()
			//.setDomain("www.youtube.com")
			.setUserAgent("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36")
			.setTimeOut(30000)
			.setCycleRetryTimes(3);

	@Override
	public Site getSite() {
		return site;
	}

	public Map<String,String> getParamMap(String rawText){
		Map<String,String> paramMap=new HashMap<>();
		try {
			rawText = URLDecoder.decode(rawText, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		String[] params = rawText.split("&");
		for (String param : params) {
			String[] p = param.split("=");
			if(p.length>1){
				paramMap.put(p[0], p[1]);	
			}
		}
		
		return paramMap;
	}
	
	
	@Override
	public void process(Page page) {
		String url = page.getRequest().getUrl();
		
		//1. 获取youtube 返回的视频列表
		if(url.contains("https://www.googleapis.com/youtube/v3/search")){
			//抽取视频id
			List<String> items = page.getJson().jsonPath("$.items[*]").all();
			String apiKey = (String) page.getRequest().getExtra("apiKey");
			for (String item : items) {
				System.out.println(item);
				String videoId = new JsonPathSelector("$.id.videoId").select(item);
				Request request=new Request();
				request.setUrl("https://www.youtube.com/get_video_info?video_id="+videoId);
				request.putExtra("videoId", videoId);
				page.addTargetRequest(request);
			}
			//skip不会进入MyPipeline进行数据保存
			page.setSkip(true); 
		}
		
		//2.判定视频是否可用
		/*if(url.contains("https://www.youtube.com/get_video_info")){
			String str = page.getRawText();
			page.setSkip(true);
			//存在errorcode,表示该视频无法播放
			if(str.contains("errorcode"))
				return;
			
			String apiKey = (String) page.getRequest().getExtra("apiKey");
			String videoId = (String) page.getRequest().getExtra("videoId");
			//part参数 : https://developers.google.com/youtube/v3/getting-started#partial
			//https://www.googleapis.com/youtube/v3/videos?id=scWpXEYZEGk&part=id,contentDetails,snippet,statistics&key=AIzaSyAPjmHyYPp9iy4mNhWwhHtIYX-ZfcdG2fE
			StringBuilder sb=new StringBuilder("https://www.googleapis.com/youtube/v3/videos?");
			sb.append("id="+videoId)
			.append("&key="+apiKey)
			.append("&part=snippet,statistics");
			//.append("&fields=items(snippet(title,publishedAt,description,thumbnails(maxres(url,width,height))),statistics,fileDetails(durationMs,creationTime))");
			Request request=new Request();
			request.setUrl(sb.toString());
			request.putExtra("apiKey", apiKey);
			request.putExtra("videoId", videoId);
			request.setMethod("GET");
			page.addTargetRequest(request);
		}*/
		if(url.contains("https://www.youtube.com/get_video_info")){
			String str = page.getRawText();
			page.setSkip(true);
			//存在errorcode,表示该视频无法播放
			if(str.contains("errorcode"))
				return;

		/*			keywords=Arsenal F.C.,Arsenal Fans,fc bayern munich (football team),champions league,bayern munich,robert lewandowski,aftv,afc,Fans,Football,Soccer,Goal,Kieran Gibbs,Aaron Ramsey,Highlights,Ozil,Mesut Ozil,epl,Goals,A.F.C.,Alexis Sanchez,Arsene Wenger,Theo Walcott,Arsenal,The Gunners,Gooners,Olivier Giroud,Hector Bellerin,Nacho Monreal,premier league,mohamed elneny,Shkodran Mustafi,laurent koscielny,alex oxlade-chamberlain,Granit Xhaka
					expire=1489004579
					video_id=4CgbTHJ5ksc
					author=ArsenalFanTV
					length_seconds=646
					iurl=https://i.ytimg.com/vi/4CgbTHJ5ksc/hqdefault.jpg
					cver=1.20170307
					iurlhq720=https://i.ytimg.com/vi/4CgbTHJ5ksc/hq720.jpg
					view_count=329267
					iurlhq=https://i.ytimg.com/vi/4CgbTHJ5ksc/hqdefault.jpg
					fps=25
					lmaxres=https://i.ytimg.com/vi/4CgbTHJ5ksc/maxresdefault.jpg
					cl=149455302
					timestamp=1488979379
					iurlsd=https://i.ytimg.com/vi/4CgbTHJ5ksc/sddefault.jpg
					title=Arsenal 1 Bayern Munich 5 | Walk Away Arsene! (DT Rant)
					thumbnail_url=https://i.ytimg.com/vi/4CgbTHJ5ksc/default.jpg
*/
			Map<String, String> paramMap = getParamMap(str);
			//关键字
			String keywords = paramMap.get("keywords");
			List<String> tags = Arrays.asList(keywords.split(","));
			//作者
			String author = paramMap.get("author");
			//视频时长
			String lengthSeconds = paramMap.get("length_seconds");
			//图片720
			String iurlhq720 = paramMap.get("iurlhq720");
			//视频观看数
			String view_count = paramMap.get("view_count");
			//视频标题
			String title = paramMap.get("title");
			//上传时间
			String publishedAt = paramMap.get("publishedAt");
			publishedAt=publishedAt.substring(publishedAt.indexOf("."));
			
			
			String videoId = (String) page.getRequest().getExtra("videoId");
			
			PageArticle pageArticle=new PageArticle();
			//pageArticle.setDate(DateUtils.getUTCTime(publishedAt, "EEE, d MMM yyyy HH:mm:ss 'GMT'"));
			pageArticle.setHumanLanguage("ar");
			pageArticle.setTitle(title);
			pageArticle.setType("video");
			pageArticle.setTags(tags);
			pageArticle.setAuthor(author);
			
			List<PageImage> pageImages=new ArrayList<>();
			PageImage pageImage=new PageImage();
			pageImage.setPrimary(true);
			pageImage.setUrl(iurlhq720);
			pageImages.add(pageImage);
			
			List<PageVideo> pageVideos=new ArrayList<>();
			PageVideo pageVideo=new PageVideo();
			pageVideo.setPrimary(true);
			pageVideo.setUrl("https://www.youtube.com/embed/"+videoId);
			pageVideos.add(pageVideo);
			
			pageArticle.setVideos(pageVideos);
			pageArticle.setImages(pageImages);
			
			page.putField("pageArticle", pageArticle);
			
			
		}
		
		
		
		//该接口可以不用 上面请求获取的信息包含所有内容
		//3.获取视频的详细信息封装到pageArticle对象中
		//https://developers.google.com/youtube/v3/docs/videos
		//https://developers.google.com/youtube/v3/docs/videos#resource
		/*if(url.contains("https://www.googleapis.com/youtube/v3/videos")){
			//封装个pageArticle对象
			String videoId = (String) page.getRequest().getExtra("videoId");
			String rawText = page.getRawText();
			
			String title = new JsonPathSelector("$.items[0].snippet.title").select(rawText);
			String publishedAt = new JsonPathSelector("$.items[0].snippet.publishedAt").select(rawText);
			String description = new JsonPathSelector("$.items[0].snippet.description").select(rawText);
			String imageUrl =new JsonPathSelector("$.items[0].snippet.thumbnails.default.url").select(rawText);
			if(rawText.contains("standard")){
				imageUrl= new JsonPathSelector("$.items[0].snippet.thumbnails.standard.url").select(rawText);
			}
			if(rawText.contains("maxres")){
				imageUrl = new JsonPathSelector("$.items[0].snippet.thumbnails.maxres.url").select(rawText);
			}
			//String durationMs = new JsonPathSelector("$.items[0].fileDetails.durationMs").select(rawText);
			PageArticle pageArticle=new PageArticle();
			pageArticle.setDate(publishedAt);
			pageArticle.setDiscussion(description);
			pageArticle.setHumanLanguage("ar");
			pageArticle.setText(description);
			pageArticle.setTitle(title);
			pageArticle.setType("video");
			
			List<PageImage> pageImages=new ArrayList<>();
			PageImage pageImage=new PageImage();
			pageImage.setPrimary(true);
			pageImage.setUrl(imageUrl);
			pageImages.add(pageImage);
			
			List<PageVideo> pageVideos=new ArrayList<>();
			PageVideo pageVideo=new PageVideo();
			pageVideo.setPrimary(true);
			pageVideo.setUrl("https://www.youtube.com/embed/"+videoId);
			pageVideos.add(pageVideo);
			
			pageArticle.setVideos(pageVideos);
			pageArticle.setImages(pageImages);
			
			page.putField("pageArticle", pageArticle);
			
		}*/
		
	}
	
	
	public static void main(String[] args) {
		
		Calendar date = Calendar.getInstance();
		date.setTime(new Date());
		//date.set(Calendar.HOUR, - 1);
		date.set(Calendar.HOUR, - 24);
		SimpleDateFormat rfcSdf=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		String publishedAfterDate = rfcSdf.format(date.getTime());
		
		//构造url
		StringBuilder sb=new StringBuilder();
		sb.append("https://www.googleapis.com/youtube/v3/search?key=AIzaSyAPjmHyYPp9iy4mNhWwhHtIYX-ZfcdG2fE")
		.append("&relevanceLanguage=ar")
		.append("&type=video")
		.append("&maxResults=2")
		.append("&part=id")
		.append("&videoDefinition=high")
		.append("&order=viewCount")
		.append("&safeSearch=strict")
		.append("&videoLicense=youtube")
		.append("&videoSyndicated=true")
		.append("&fields=*")
		
		//该topicId参数指示API响应应仅包含与指定主题相关联的资源。该值标识一个Freebase主题ID。
		//https://developers.google.com/youtube/v3/docs/search/list
		.append("&topicId=/m/02vx4")
		
		//该videoCategoryId参数根据其类别过滤视频搜索结果。
		//videoCategoryId 如果为此参数指定值，那么还必须将type参数的值设置为video。
		//https://www.googleapis.com/youtube/v3/videoCategories?key=AIzaSyAPjmHyYPp9iy4mNhWwhHtIYX-ZfcdG2fE&part=snippet&regionCode=SA
		//.append("videoCategoryId=2")
	    
		//用于搜索查询的区域代码。
		//伊朗Iran IR 伊拉克Iraq IQ  科威特Kuwait; KW
		//沙特阿拉伯Saudi Arabia SA 阿拉伯联合酋长国The United Arab Emirates  AE
		//阿曼Oman  OM 卡塔尔Qatar  QA 巴林Bahrain	BH
		//.append("&regionCode=AE") 
		
		//http://vidstatsx.com/ youtube频道排行板
		//https://www.zhihu.com/question/19609089?sort=created
		//https://www.googleapis.com/youtube/v3/guideCategories?key=AIzaSyAPjmHyYPp9iy4mNhWwhHtIYX-ZfcdG2fE&part=snippet&regionCode=ar
		.append("&channelId=UCBTy8j2cPy6zw68godcE7MQ") 
		
		//该publishedAfter参数指示API响应应仅包含在指定时间或之后创建的资源。
		//publishedAfter
		//yyyy-MM-dd'T'HH:mm:ss'Z'
		.append("&publishedAfter="+publishedAfterDate)
		;
		
	
		Request request=new Request();
		request.setUrl(sb.toString());
		//模拟带参数
		request.putExtra("apiKey", "AIzaSyAPjmHyYPp9iy4mNhWwhHtIYX-ZfcdG2fE");
		Spider.create(new MyProcessor())
		.addRequest(request)
		.addPipeline(new MyPipeline())
		.thread(10)
		.run();
		
	}

}
