package com.onemt.crawler.twitter;

import com.google.gson.GsonBuilder;

import twitter4j.DirectMessage;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.User;
import twitter4j.UserList;
import twitter4j.UserStreamListener;

public class StatusStreamTest {
	public static void main(String[] args) {
		TwitterStream twitterStream = new TwitterStreamFactory().getInstance();
		twitterStream.addListener(new UserStreamListener() {
			
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
				System.out.println(new GsonBuilder().create().toJson(status));
				
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
			
			@Override
			public void onUserSuspension(long suspendedUser) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onUserProfileUpdate(User updatedUser) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onUserListUpdate(User listOwner, UserList list) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onUserListUnsubscription(User subscriber, User listOwner,
					UserList list) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onUserListSubscription(User subscriber, User listOwner,
					UserList list) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onUserListMemberDeletion(User deletedMember, User listOwner,
					UserList list) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onUserListMemberAddition(User addedMember, User listOwner,
					UserList list) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onUserListDeletion(User listOwner, UserList list) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onUserListCreation(User listOwner, UserList list) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onUserDeletion(long deletedUser) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onUnfollow(User source, User unfollowedUser) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onUnfavorite(User source, User target, Status unfavoritedStatus) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onUnblock(User source, User unblockedUser) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onRetweetedRetweet(User source, User target,
					Status retweetedStatus) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onQuotedTweet(User source, User target, Status quotingTweet) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onFriendList(long[] friendIds) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onFollow(User source, User followedUser) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onFavoritedRetweet(User source, User target,
					Status favoritedRetweeet) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onFavorite(User source, User target, Status favoritedStatus) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onDirectMessage(DirectMessage directMessage) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onDeletionNotice(long directMessageId, long userId) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onBlock(User source, User blockedUser) {
				// TODO Auto-generated method stub
				
			}
		});
		
		
		//FilterQuery query = new FilterQuery();
		//query.follow(new long[]{851019377235206144L});// userId 是在Twitter App页面中的用户ID
		//query.track("coder_Lee23"); 
		//twitterStream.filter("coder_Lee23");
		twitterStream.user("coder_Lee23");
	}
}
