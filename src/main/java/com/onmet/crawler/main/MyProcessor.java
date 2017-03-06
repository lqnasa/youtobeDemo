package com.onmet.crawler.main;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.JsonPathSelector;

public class MyProcessor implements PageProcessor{

	private Site site=Site.me()
			//.setDomain("www.youtube.com")
			.setDomain("news.baidu.com")
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
		if(url.matches("^https://www.googleapis.com/youtube/v3/search?.*")){
			List<String> items = page.getJson().jsonPath("$.items[*]").all();
			for (String item : items) {
				String videoId = new JsonPathSelector("$.id.videoId").select(item);
				Request request=new Request();
				request.setUrl("https://www.youtube.com/get_video_info?video_id="+videoId);
				page.addTargetRequest(request);
				System.out.println(item);
			}
			page.setSkip(true);
		}
		
		if(url.matches("^https://www.youtube.com/get_video_info?.*")){
			String str = page.getRawText();
			if(str.contains("errorcode")){
				page.setSkip(true);
			}
		}
		
		/*String rawText = page.getRawText();
		Pattern p=Pattern.compile("cpOptions_1.data.push\\(\\{[^\\{]*\"imgUrl\": \"(.*?)\"[^\\}]*\\}\\);");
		Matcher matcher = p.matcher(rawText);
		while(matcher.find()){
			System.out.println(matcher.group(1));
		}*/
			
		
	}
	
	
	public static void main(String[] args) {
		StringBuilder sb=new StringBuilder();
		sb.append("https://www.googleapis.com/youtube/v3/search?key=AIzaSyAPjmHyYPp9iy4mNhWwhHtIYX-ZfcdG2fE")
		.append("&relevanceLanguage=ar")
		.append("&topicId=/m/02vx4")
		.append("&type=video")
		.append("&maxResults=5")
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
		//Spider.create(new MyProcessor()).addUrl("http://news.baidu.com").thread(1).runAsync();
		
	}
}
