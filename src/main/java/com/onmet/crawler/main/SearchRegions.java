package com.onmet.crawler.main;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.I18nRegion;
import com.google.api.services.youtube.model.I18nRegionListResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.onmet.crawler.cmdline.Auth;

public class SearchRegions {
	private static final String PROPERTIES_FILENAME = "youtube.properties";
	private static final long NUMBER_OF_VIDEOS_RETURNED = 25;
	private static YouTube youtube;

	public static void main(String[] args) {
		Properties properties = new Properties();
		try {
			InputStream in = Search.class.getResourceAsStream("/"+ PROPERTIES_FILENAME);
			properties.load(in);

		} catch (IOException e) {
			System.err.println("There was an error reading "
					+ PROPERTIES_FILENAME + ": " + e.getCause() + " : "
					+ e.getMessage());
			System.exit(1);
		}

		try {
			youtube = new YouTube.Builder(Auth.HTTP_TRANSPORT,Auth.JSON_FACTORY, new HttpRequestInitializer() {
						public void initialize(HttpRequest request)throws IOException {
						}
					}).setApplicationName("youtube-crawler").build();

			YouTube.I18nRegions.List searchRegions = youtube.i18nRegions().list("snippet");
			// {{ https://cloud.google.com/console }}
			String apiKey = properties.getProperty("youtube.apikey");
			searchRegions.setKey(apiKey);
			//可选参数 该hl参数指定应在API响应中用于文本值的语言。默认值为en_US。
			//searchRegions.setHl("zh_CN");

			I18nRegionListResponse searchResponse = searchRegions.execute();
			List<I18nRegion> items = searchResponse.getItems();
			System.out.println(items.size());
			for (I18nRegion i18nRegion : items) {
				Gson gson=new GsonBuilder().create();
				String json = gson.toJson(i18nRegion);
				System.out.println(json);
			}
			
			
		} catch (GoogleJsonResponseException e) {
			System.err.println("There was a service error: "
					+ e.getDetails().getCode() + " : "
					+ e.getDetails().getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("There was an IO error: " + e.getCause() + " : "
					+ e.getMessage());
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}
}
