package com.onemt.crawler.twitter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.stereotype.Service;

import twitter4j.DirectMessage;
import twitter4j.FilterQuery;
import twitter4j.OEmbed;
import twitter4j.OEmbedRequest;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusAdapter;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.User;
import twitter4j.UserList;
import twitter4j.UserStreamListener;
import us.codecraft.webmagic.Task;

import com.google.gson.GsonBuilder;

@Service
public class StatusStreamService {
	
	private static final String TWITTER_STATUSID_PREFIX="twitter_statusid";
	private final static Twitter twitter= new TwitterFactory().getInstance();
	
	
	@Autowired
	private RedisTemplate<String,String> jedisTemplate;
	
	@PostConstruct
	public void getUserStatus(){
		final List<String> screenNames=new ArrayList<>();
		//screenNames.add("SaudiNews50");
		screenNames.add("AlArabiya_Brk");
		
		
		 TwitterStream twitterStream = new TwitterStreamFactory().getInstance();
		 /*twitterStream.addListener(new StatusListener() {
				
				@Override
				public void onException(Exception ex) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onStatus(Status status) {
					// TODO Auto-generated method stub
					  //获取快讯id,构造redis的key
                    String statusKey = String.valueOf(status.getId());
                    //判定该快讯是否存在,存在则不存入
                    Boolean hasKey = jedisTemplate.hasKey(statusKey);
                    if(hasKey)
                        return;
                    
                    //入库操作
                    System.out.println(new GsonBuilder().create().toJson(status));  
                    
                    //保存key值到数据库
                    SetOperations<String, String> opsForSet = jedisTemplate.opsForSet();
                    opsForSet.add(TWITTER_STATUSID_PREFIX, statusKey);
				}
				
				@Override
				public void onStallWarning(StallWarning warning) {
					// TODO Auto-generated method stub
				}
				
				@Override
				public void onScrubGeo(long userId, long upToStatusId) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
					// TODO Auto-generated method stub
					
				}
			});
			//@SaudiNews50 474886468  coder_Lee23 851019377235206144L
			//twitterStream.filter("coder_Lee23"); medium
			
		 //
		twitterStream.filter(new FilterQuery().follow(new long[]{474886468}).filterLevel("low"));*/
		 
		 
		 twitterStream.addListener(new StatusAdapter() {
	            @Override
	            public void onStatus(Status status) {
	            	String screenName = status.getUser().getScreenName();
	            	//不包含他人转推
	            	if(!screenNames.contains(screenName))
	            		return;
	            	 System.out.println("===============================================");
	                System.out.println(String.format("@%s \n text ===================\n %s", status.getUser().getScreenName(), status.getText()));
	                
	                try {
						BufferedWriter bw=new BufferedWriter(new FileWriter(new File("E:\\twitter.txt"),true));
						bw.newLine();
						bw.write(new GsonBuilder().create().toJson(status));
						bw.flush();
						
						/*OEmbedRequest oEmbedRequest = new OEmbedRequest(status.getId(), "");
						oEmbedRequest.setHideMedia(false);
						oEmbedRequest.setHideThread(false);
						
						try {
							OEmbed oEmbed = twitter.getOEmbed(oEmbedRequest);
							String html = oEmbed.getHtml();
							bw.newLine();
							bw.write(html);
							bw.flush();
						} catch (TwitterException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}*/
						
						
						
						bw.close();
						
						//https://publish.twitter.com/oembed?url=https%3A%2F%2Ftwitter.com%2FInterior%2Fstatus%2F507185938620219395
						
						
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	            
	            
	            }

	            @Override
	            public void onException(Exception ex) {
	                ex.printStackTrace();
	            }
	        });
		 twitterStream.filter(new FilterQuery(screenNames.toArray(new String[screenNames.size()])));
	}
	
	public static void main(String[] args) {
		OEmbedRequest oEmbedRequest = new OEmbedRequest(851586816188854272L, "");
		oEmbedRequest.setHideMedia(false);
		//oEmbedRequest.setHideThread(false);
		
		try {
			OEmbed oEmbed = twitter.getOEmbed(oEmbedRequest);
			String html = oEmbed.getHtml();
			System.out.println(new GsonBuilder().create().toJson(oEmbed));
			Status showStatus = twitter.showStatus(852198778522279942L);
			System.out.println(new GsonBuilder().create().toJson(showStatus));
		} catch (TwitterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	/*	try {
			Document jsoup=Jsoup.connect("https://twitter.com/SaudiNews50").userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/57.0.2987.133 Safari/537.36").get();
			Elements elements = jsoup.select("#timeline .stream li .js-stream-tweet");
			
			for (Element element : elements) {
				//TweetTextSize
				if(!element.select(".TweetTextSize").isEmpty()){
					Element text = element.select(".TweetTextSize").first();
					String html2 = text.html();
					System.out.println(html2);
				}
				//AdaptiveMedia-singlePhoto
				if(!element.select(".AdaptiveMedia-singlePhoto").isEmpty()){
					System.out.println("==========AdaptiveMedia-singlePhoto============");
					Element photo = element.select(".AdaptiveMedia-singlePhoto").first();
					System.out.println(photo.html());
				}
				
				//AdaptiveMedia-videoContainer
				if(!element.select(".AdaptiveMedia-videoContainer").isEmpty()){
					System.out.println("==========AdaptiveMedia-videoContainer============");
					Element photo = element.select(".AdaptiveMedia-videoContainer").first();
					System.out.println(photo.html());
				}
				
				//js-media-container
				//data-card2-name="poll2choice_text_only" 投票
				
				
				System.out.println("=================================================================");
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		
		
	}
	
}
