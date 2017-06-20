package com.onemt.crawler.main;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;




public class RegTest {
	public static void main(String[] args) throws IOException {
		 /* String str2="aa<a href=\"http://www.islam.ms/ar/%D8%B5%D9%81%D8%A7%D8%AA-%D8%B9%D8%B5%D9%85%D8%A9-%D8%A7%D9%84%D8%A3%D9%86%D8%A8%D9%8A%D8%A7%D8%A1-%D8%A7%D9%84%D8%B1%D8%B3%D9%84/\">صِفات وعِصْمَة الأَنبِيَاءِ والرُّسُل</a>";
		    String str1="أنظر :<a href=\"http://www.islam.ms/ar/%D8%A7%D8%A8%D8%AA%D9%84%D8%A7%D8%A1-%D9%88%D8%B5%D8%A8%D8%B1-%D9%86%D8%A8%D9%8A-%D8%A7%D9%84%D9%84%D9%87-%D8%A3%D9%8A%D9%88%D8%A8/\">ابتلاء وصبر سيدنا أيوب عليه السلام</a>";
		    str1=str1.replaceAll("أنظر :", "");
		    System.out.println(str2.replaceAll("<a.*?href=\"(?!http://www.islam.ms).*?\"[^>]*>[^<]*</a>", "222"));*/
		
		/*Document document = Jsoup.connect("https://arabic.rt.com/sport/874558-%D9%87%D9%88%D9%81%D9%86%D9%87%D8%A7%D9%8A%D9%85-%D9%8A%D9%86%D8%AC%D9%88-%D9%85%D9%86-%D8%A7%D9%84%D9%87%D8%B2%D9%8A%D9%85%D8%A9-%D8%A3%D9%85%D8%A7%D9%85-%D9%83%D9%88%D9%84%D9%86/").get();
		System.out.println(document);*/
		
		/*int m=(int) ((267800-200000)*0.015+10000*0.02+90000*0.025+50);
		System.out.println(m);*/
		String cookiesStr="imooc_uuid=8e611296-bd03-4b90-a94e-1d7e9143bb31; imooc_isnew=1; imooc_isnew_ct=1494051371; loginstate=1; apsid=IxOThmNDVjYzgwZjJjZWVjNTM0MjBjZDQ1YTRkNGIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAMTI0NDE5OQAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAABscTE5ODY0MTRAMTYzLmNvbQAAAAAAAAAAAAAAAAAAAGMzNDc1MjZkNzMwODhkMTRlMDNjOGIwNjc1MGEwNzJlMGoNWTBqDVk%3DZm; last_login_username=lq1986414%40163.com; IMCDNS=0; Hm_lvt_f0cfcccd7b1393990c78efdeebff3968=1494051356; Hm_lpvt_f0cfcccd7b1393990c78efdeebff3968=1494051615; PHPSESSID=gddne35ufpunhv5rco6jqr2nm1; Hm_lvt_c92536284537e1806a07ef3e6873f2b3=1494051676; Hm_lpvt_c92536284537e1806a07ef3e6873f2b3=1494051676; cvde=590d6a2b01b10-18";
		String[] cookies = cookiesStr.split(";");
		Map<String,String> map=new HashMap<>();
		for (String cookie : cookies) {
			String[] cookieArr = cookie.split("=");
			System.out.println(cookieArr[0]+" "+ cookieArr[1]);
			map.put(cookieArr[0], cookieArr[1]);	
				
		}
		String url="http://www.imooc.com/learn/767";
		Document doc = Jsoup.connect(url)
		.cookies(map)
		.userAgent("Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/57.0.2987.133 Mobile Safari/537.36")
		.get();
		
		System.out.println(doc.html());
		String title = doc.select("title").text();
		File file = new File("G:\\imooc\\"+title);
		if (!file.exists()) {
			file.mkdirs();
		}
		
		
		Elements videos = doc.select(".course-sections a");
		for (Element element : videos) {
			String absUrl = element.absUrl("href");
			String text = element.select("a").text();
			/*downUrlMap.put(absUrl, text);
			System.out.println(absUrl+" "+text);*/
			
			Document downdoc = Jsoup.connect(absUrl)
					.cookies(map)
					.userAgent("Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/57.0.2987.133 Mobile Safari/537.36")
					.get();
			
			//System.out.println(downdoc.html());
			
			
			String html = downdoc.html();
			Pattern p=Pattern.compile("course.videoUrl=\"(.*?)\";");
			Matcher matcher = p.matcher(html);
			while (matcher.find()) {
				String videoUrl = matcher.group(1);
				videoUrl = videoUrl.replace("/L", "/H");
				String str = videoUrl.substring(videoUrl.lastIndexOf("."));
				
	            
				 //获取下载地址  
	            URL url2 = new URL(videoUrl);  
	            //链接网络地址  
	            HttpURLConnection connection = (HttpURLConnection)url2.openConnection();
	            connection.setConnectTimeout(50000);
	            connection.setReadTimeout(50000);
				BufferedInputStream bis = new BufferedInputStream(connection.getInputStream());
				BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(new File("G:\\imooc\\"+title+"\\" + text + str)));

				byte[] b = new byte[1024 * 1024*10];
				int len = 0;
				while ((len = bis.read(b)) != -1) {
					bos.write(b, 0, len);
				}
				bos.flush();
				bos.close();
				bis.close();
				
				
				
			}			
			
			
			
			
		}
		
		
		
		
		
		
		
	}
}
