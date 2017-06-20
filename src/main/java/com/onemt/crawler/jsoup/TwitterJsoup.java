package com.onemt.crawler.jsoup;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class TwitterJsoup {

	public static void main(String[] args) {


		try {
			Document videoDoc = Jsoup
					.connect("https://twitter.com/i/web/status/857034482213425157").header("Connection", "close")
					.userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/57.0.2987.133 Safari/537.36")
					.timeout(80000).get();

			Element first = videoDoc.select("meta[property=\"og:description\"]").first();
			String attr = first.attr("content");

			//twitter视频中去掉https
			String replaceAll = attr.replaceAll("https[^\\s]*", "");
			System.out.println(replaceAll);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
