package com.onemt.crawler.twitter;

import java.util.List;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;

import com.google.gson.GsonBuilder;

public class StatusTest {

	public static void main(String[] args) {
		//String screenName = "AlArabiya_Brk"; 
		String screenName = "SaudiNews50";
		try {
			// gets Twitter instance with default credentials
			Twitter twitter = new TwitterFactory().getInstance();
			User user = twitter.verifyCredentials();
			List<Status> statuses = twitter.getUserTimeline(screenName);
			System.out.println("Showing @" + user.getScreenName()+ "'s home timeline.");
			for (Status status : statuses) {
				System.out.println(new GsonBuilder().create().toJson(status));
				System.out.println("@" + status.getUser().getScreenName()+ " - " + status.getText());
			}
		} catch (TwitterException te) {
			te.printStackTrace();
			System.out.println("Failed to get timeline: " + te.getMessage());
			System.exit(-1);
		}
	}
}
