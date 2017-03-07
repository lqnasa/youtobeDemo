package com.onmet.crawler.main;

import java.util.ArrayList;
import java.util.List;

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

	@Override
	public void process(Page page) {
		String url = page.getRequest().getUrl();
		
		//1. 获取youtube 返回的视频列表
		if(url.contains("https://www.googleapis.com/youtube/v3/search")){
			//抽取视频id
			List<String> items = page.getJson().jsonPath("$.items[*]").all();
			String apiKey = (String) page.getRequest().getExtra("apiKey");
			for (String item : items) {
				String videoId = new JsonPathSelector("$.id.videoId").select(item);
				Request request=new Request();
				request.setUrl("https://www.youtube.com/get_video_info?video_id="+videoId);
				request.putExtra("apiKey", apiKey);
				request.putExtra("videoId", videoId);
				page.addTargetRequest(request);
				System.out.println(item);
			}
			//skip不会进入MyPipeline进行数据保存
			page.setSkip(true); 
		}
		
		//2.判定视频是否可用
		if(url.contains("https://www.youtube.com/get_video_info")){
			String str = page.getRawText();
			page.setSkip(true);
			//存在errorcode,表示该视频无法播放
			if(str.contains("errorcode"))
				return;
			
				
			
			String apiKey = (String) page.getRequest().getExtra("apiKey");
			String videoId = (String) page.getRequest().getExtra("videoId");
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
		}
		
		//3.获取视频的详细信息封装到pageArticle对象中
		//https://developers.google.com/youtube/v3/docs/videos
		if(url.contains("https://www.googleapis.com/youtube/v3/videos")){
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
			
		}
		
		
			
		
	}
	
	
	public static void main(String[] args) {
		
		//构造url
		StringBuilder sb=new StringBuilder();
		sb.append("https://www.googleapis.com/youtube/v3/search?key=AIzaSyAPjmHyYPp9iy4mNhWwhHtIYX-ZfcdG2fE")
		.append("&relevanceLanguage=ar")
		.append("&topicId=/m/02vx4")
		.append("&type=video")
		.append("&maxResults=5")
		.append("&part=id")
		.append("&videoDefinition=high")
		.append("&order=viewCount")
		.append("&safeSearch=strict")
		.append("&videoLicense=youtube")
		.append("&videoSyndicated=true")
		.append("&fields=*");
		
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
