package com.onemt.crawler.twitter;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;

public class Tw {
	 
	  static String AccessToken = "851019377235206144-X7ruuIPjWEQmpR3fXKrAvEF8S8PPYkJ";
	  static String AccessSecret = "YhgIaH1WgYg0IgBxxoLpb4auuVN5kqCBASFTrut5i67Pt";
	  static String ConsumerKey = "dIc2GwN7KHbpL4qglOfGdkqir";
	  static String ConsumerSecret = "jAqWTwmpO31FiU4jUtQ7sLWopgeG7E4O3DBQ3CGyl1qeUfDp4G";

	  /*oauth.consumerKey=dIc2GwN7KHbpL4qglOfGdkqir
			  oauth.consumerSecret=jAqWTwmpO31FiU4jUtQ7sLWopgeG7E4O3DBQ3CGyl1qeUfDp4G
			  oauth.accessToken=851019377235206144-X7ruuIPjWEQmpR3fXKrAvEF8S8PPYkJ
			  oauth.accessTokenSecret=YhgIaH1WgYg0IgBxxoLpb4auuVN5kqCBASFTrut5i67Pt*/
	  
	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
	  OAuthConsumer consumer = new CommonsHttpOAuthConsumer(ConsumerKey,ConsumerSecret);
      consumer.setTokenWithSecret(AccessToken, AccessSecret);
      HttpGet request = new HttpGet("http://api.twitter.com/1.1/followers/ids.json?cursor=-1&screen_name=josdirksen");
      consumer.sign(request);

      CloseableHttpClient httpclient = HttpClients.createDefault();
      HttpResponse response = httpclient.execute(request);

      int statusCode = response.getStatusLine().getStatusCode();
      System.out.println(statusCode + ":" + response.getStatusLine().getReasonPhrase());
      System.out.println(IOUtils.toString(response.getEntity().getContent()));
	}
}