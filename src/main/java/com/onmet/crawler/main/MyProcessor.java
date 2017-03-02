package com.onmet.crawler.main;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Json;

public class MyProcessor implements PageProcessor{

	private Site site=Site.me()
			.setDomain("www.youtube.com")
			.setUserAgent("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36")
			.setTimeOut(30000)
			.setCycleRetryTimes(3);

	@Override
	public Site getSite() {
		return site;
	}

	@Override
	public void process(Page page) {
		Json json = page.getJson();
		System.out.println(json);
		
		
	}
	
	
	public static void main(String[] args) {
		StringBuilder sb=new StringBuilder();
		sb.append("https://www.googleapis.com/youtube/v3/search?key=AIzaSyAPjmHyYPp9iy4mNhWwhHtIYX-ZfcdG2fE")
		.append("&relevanceLanguage=ar")
		.append("&topicId=/m/02vx4")
		.append("&type=video")
		.append("&maxResults=50")
		.append("&part=id,snippet")
		.append("&videoDefinition=high")
		.append("&order=viewCount")
		.append("&safeSearch=strict")
		.append("&fields=*");
		Spider.create(new MyProcessor())
		//.addPipeline(pipeline)
		.addUrl(sb.toString())
		.thread(5)
		.runAsync();
	}
}
