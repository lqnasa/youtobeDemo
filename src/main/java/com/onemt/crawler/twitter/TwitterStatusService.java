package com.onemt.crawler.twitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import twitter4j.FilterQuery;
import twitter4j.Status;
import twitter4j.StatusAdapter;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.URLEntity;
import twitter4j.User;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jayway.jsonpath.JsonPath;
import com.onemt.crawler.twitter.domain.TwitterBn;
import com.onemt.crawler.twitter.domain.TwitterImage;

@Service
public class TwitterStatusService {

	@Autowired
	private RedisTemplate<String, String> jedisTemplate;
	
	private static final String TWITTER_STATUSID_PREFIX="twitter_statusid";
	
	private static final String VIDEO_URL="https://www.googleapis.com/youtube/v3/videos?part=snippet,contentDetails&fields=items(snippet/thumbnails,contentDetails/duration)";
	
	private static final Gson gson= new GsonBuilder().create();
	
	//private static String apiKey=ProjectConfigUtil.get("youtube.apiKey");
	
	private static String apiKey="AIzaSyAPjmHyYPp9iy4mNhWwhHtIYX-ZfcdG2fE";

	public static void main(String[] args) {
		getUserStatus();
		/*String str="<meta property=\"og:image\" content=\"https://pbs.twimg.com/media/C9E9VfTXoAEmL73.jpg:large\">\n<meta property=\"og:image:user_generated\" content=\"true\"><meta property=\"og:image\" content=\"https://pbs.twimg.com/media/C9E9VfTXoAEmL73.jpg:large\">\n<meta property=\"og:image:user_generated\" content=\"true\">";
		Pattern p=Pattern.compile("<meta.*?property=\"og:image\".*?content=\"(.*?)\">\\s*(?=<meta property=\"og:image:user_generated\" content=\"true\">)");
		Matcher matcher = p.matcher(str);
		while(matcher.find()){
			String group = matcher.group(1);
			System.out.println(group);
		}*/
		
		//P#DT#H#M#S
		//PT15M33S
		/*String str="PT15M33S";
		//str="P1DT20H1M2S";
		String[] split = str.replaceAll("\\D+", " ").trim().split(" ");
		int time=0;
		for (int i = split.length-1,j=0; i >=0; i--,j++) {
			switch (j) {
			case 0: //秒
				time+=Integer.valueOf(split[i]);
				break;
			case 1: //分
				time+=Integer.valueOf(split[i])*60;
				break;
			case 2: //时
				time+=Integer.valueOf(split[i])*3600;
				break;
			case 3: //天
				time+=Integer.valueOf(split[i])*3600*24;
				break;
			default:
				break;
			}
		}
		
		System.out.println(time);
	*/
	}
	
	@Test
	@PostConstruct
	public static void getUserStatus() {
		final List<String> screenNames = new ArrayList<>();
		 //screenNames.add("SaudiNews50");
		//screenNames.add("AlArabiya_Brk");
		screenNames.add("coder_Lee23");
		TwitterStream twitterStream = new TwitterStreamFactory().getInstance();
		twitterStream.addListener(new StatusAdapter() {

			@Override
			public void onStatus(Status status) {
				String screenName = status.getUser().getScreenName();
				System.out.println("screenName:@"+screenName);
				// 非用户推送的消息不抓取
				if (!screenNames.contains(screenName))
					return;
				// 判定是否已抓取过
				//String statusKey = String.valueOf(status.getId());
				// 判定该快讯是否存在,存在则不存入
				/*Boolean hasKey = jedisTemplate.hasKey(statusKey);
				if (hasKey)
					return;*/

				// 入库操作
				System.out.println(gson.toJson(status));
				doProcess(status);

				// 保存key值到数据库
				/*SetOperations<String, String> opsForSet = jedisTemplate.opsForSet();
				opsForSet.add(TWITTER_STATUSID_PREFIX, statusKey);*/

			}

			
		});
		//coder_Lee23
		FilterQuery filterQuery=new FilterQuery();
		filterQuery.follow(851019377235206144L);
		twitterStream.filter(filterQuery);
	}
	
	//处理保存入库
	public static void doProcess(Status status) {
		long id = status.getId();
		Date createdAt = status.getCreatedAt();
		int favoriteCount = status.getFavoriteCount();
		String lang = status.getLang();
		int retweetCount = status.getRetweetCount();
		String text = status.getText();
		User user = status.getUser();
		URLEntity[] urlEntities = status.getURLEntities();
		String videoId="";
		for (URLEntity urlEntity : urlEntities) {
			//https://youtu.be/ceosOod0Ipc
			if(urlEntity.getExpandedURL().contains("https://youtu.be/")){
				videoId=urlEntity.getExpandedURL().replaceAll("https://youtu.be/", "");
				break;
			}
		}
		
		TwitterBn twitterBn=new TwitterBn();
		twitterBn.setFetchTime(new Date().getTime());
		twitterBn.setPublishTime(createdAt.getTime());
		twitterBn.setLikeCount(favoriteCount);
		twitterBn.setLang(lang);
		twitterBn.setRetweetCount(retweetCount);
		twitterBn.setTextContent(text);
		twitterBn.setHtmlContent(text);
		twitterBn.setContent(text);
		
	
		//判定是否含有youtube视频
		if(StringUtils.isNotBlank(videoId)){
			//https://www.googleapis.com/youtube/v3/videos?id=scWpXEYZEGk&part=snippet,contentDetails&key=AIzaSyAPjmHyYPp9iy4mNhWwhHtIYX-ZfcdG2fE&fields=items(snippet/thumbnails,contentDetails/duration)
			StringBuilder sb=new StringBuilder(VIDEO_URL);
			sb.append("&id="+videoId)
			.append("&key="+apiKey);
			
			try {
				Document twDoc = Jsoup.connect(sb.toString()).ignoreContentType(true).userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/57.0.2987.133 Safari/537.36").timeout(80000).get();
				String json = twDoc.body().text();
				String imageUrl = JsonPath.read(json, "$.items[0].snippet.thumbnails.standard.url");
				String duration = JsonPath.read(json, "$.items[0].contentDetails.duration");
				int videoTime=getYoutubeDurationTime(duration);
				List<TwitterImage> twitterImages=new ArrayList<>();
				TwitterImage twitterImage=new TwitterImage();
				twitterImage.setPrimary(true);
				twitterImage.setUrl(imageUrl);
				twitterImages.add(twitterImage);
				
				twitterBn.setVideoId(videoId); 
				twitterBn.setVideoSource("youtube");
				twitterBn.setVideoTime(videoTime);
				twitterBn.setImages(twitterImages);
				twitterBn.setType("video");
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else{
			//构造推文地址
			//https://twitter.com/SaudiNews50/status/851529252868587523
			//https://twitter.com/i/web/status/851850163287097348
			String tweetUrl="https://twitter.com/i/web/status/"+id;
			twitterBn.setSourceUrl(tweetUrl);
			
			//开启jsoup抓取信息
			try {
				Document twDoc = Jsoup.connect(tweetUrl).userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/57.0.2987.133 Safari/537.36").timeout(80000).get();
				Element head = twDoc.head();
				//<meta property="og:type" content="video">
				Element type = head.select("meta[property=og:type]").first();
				//type为video 则为twitter发布的视频
				if(type!=null && "video".equals(type.attr("content"))){
					Element videoUrlEle = head.select("meta[property=og:video:url]").first();
					String videoUrl = videoUrlEle.attr("content");
					
					twitterBn.setVideoId(videoUrl);
					twitterBn.setVideoSource("twitter");
					Document videoDoc = Jsoup.connect(videoUrl).userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/57.0.2987.133 Safari/537.36").timeout(80000).get();
					Element videoContainer = videoDoc.select("#playerContainer").first();
					String videoJson = videoContainer.attr("data-config");
					Integer duration = JsonPath.read(videoJson, "$.duration");
					String video_url = JsonPath.read(videoJson, "$.video_url");
					String image_src = JsonPath.read(videoJson, "$.image_src");
					List<TwitterImage> twitterImages=new ArrayList<>();
					TwitterImage twitterImage=new TwitterImage();
					twitterImage.setPrimary(true);
					twitterImage.setUrl(image_src);
					twitterImages.add(twitterImage);
					
					twitterBn.setVideoId(video_url);
					twitterBn.setVideoSource("youtube");
					twitterBn.setVideoTime(Integer.valueOf(duration));
					twitterBn.setImages(twitterImages);
					
					twitterBn.setType("video");
					
				}else{ //type为  article  
					String html = head.html();
					StringBuilder sb=new StringBuilder(twitterBn.getTextContent()+"\n");
					Pattern p=Pattern.compile("<meta.*?property=\"og:image\".*?content=\"(.*?)\">\\s*(?=<meta property=\"og:image:user_generated\" content=\"true\">)");
					//String str="<meta  property=\"og:image\" content=\"(.*?)\">\\s*?(?=<meta  property=\"og:image:user_generated\" content=\"true\">)";
					Matcher matcher = p.matcher(html);
					List<TwitterImage> twitterImages=new ArrayList<>();
					while(matcher.find()){
						String imageUrl = matcher.group(1);
						TwitterImage twitterImage=new TwitterImage();
						twitterImage.setPrimary(true);
						twitterImage.setUrl(matcher.group(1));
						twitterImages.add(twitterImage);
						sb.append("<figure><img src=\""+imageUrl+"\"></img></figure>\n");
					}
					twitterBn.setHtmlContent(sb.toString());
					twitterBn.setContent(sb.toString());
					twitterBn.setImages(twitterImages);
					twitterBn.setType("text");
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		System.out.println(gson.toJson(twitterBn));
		
	}

	
	//获取youtube视频时长
	public static int getYoutubeDurationTime(String duration) {
		int time=0;
		try {
			String[] split = duration.replaceAll("\\D+", " ").trim().split(" ");
			for (int i = split.length-1,j=0; i >=0; i--,j++) {
				switch (j) {
				case 0: //秒
					time+=Integer.valueOf(split[i]);
					break;
				case 1: //分
					time+=Integer.valueOf(split[i])*60;
					break;
				case 2: //时
					time+=Integer.valueOf(split[i])*3600;
					break;
				case 3: //天
					time+=Integer.valueOf(split[i])*3600*24;
					break;
				default:
					break;
				}
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return time;
	}
	
	
}
