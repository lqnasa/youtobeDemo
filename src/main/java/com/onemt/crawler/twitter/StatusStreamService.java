package com.onemt.crawler.twitter;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.stereotype.Service;

import twitter4j.DirectMessage;
import twitter4j.FilterQuery;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
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

	
	@Autowired
	private RedisTemplate<String,String> jedisTemplate;
	
	@PostConstruct
	public void getUserStatus(){
		 TwitterStream twitterStream = new TwitterStreamFactory().getInstance();
		 twitterStream.addListener(new StatusListener() {
				
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
					  /*//获取快讯id,构造redis的key
                    String statusKey = String.valueOf(status.getId());
                    //判定该快讯是否存在,存在则不存入
                    Boolean hasKey = jedisTemplate.hasKey(statusKey);
                    if(hasKey)
                        return;*/
                    
                    //入库操作
                    System.out.println(new GsonBuilder().create().toJson(status));  
                    
                    //保存key值到数据库
                    /*SetOperations<String, String> opsForSet = jedisTemplate.opsForSet();
                    opsForSet.add(TWITTER_STATUSID_PREFIX, statusKey);*/
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
			//twitterStream.filter("coder_Lee23");
			
		 //
		twitterStream.filter(new FilterQuery().follow(new long[]{851019377235206144L}).filterLevel("medium"));
	}
}
