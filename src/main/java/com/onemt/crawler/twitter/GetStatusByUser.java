package com.onemt.crawler.twitter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.List;

import twitter4j.Paging;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;

public class GetStatusByUser {

	public static void main(String[] args) throws Exception {
		//@AlArabiya_Brk
		Twitter twitter = new TwitterFactory().getInstance();
		String[] screenNames =new String[]{ "AlArabiya_Brk","SaudiNews50"};
		for (String screenName : screenNames) {
			BufferedWriter bw = new BufferedWriter(new FileWriter(new File("E:\\"+ screenName + ".txt")));
			for (int i = 1; i <= 10; i++) {
				List<Status> statusList = twitter.getUserTimeline(screenName,new Paging(i));
				for (Status status : statusList) {
					String text = status.getText();
					bw.write(text.replaceAll("\\s+", " "));
					bw.newLine();
				}
			}
			bw.flush();
			bw.close();
			
		}
		
	}

}
