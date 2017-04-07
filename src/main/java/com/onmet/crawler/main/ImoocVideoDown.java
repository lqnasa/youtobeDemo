package com.onmet.crawler.main;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.jayway.jsonpath.JsonPath;

public class ImoocVideoDown {

	public static void main(String[] args) {
		String learnUrl="http://www.imooc.com/learn/813";
		try {
			//模拟成手机端访问
			Document doc = Jsoup.connect(learnUrl).get();
			String videoTitle = doc.title();
			File file=new File("F:\\down\\imooc\\"+videoTitle);
			if(!file.exists())
				file.mkdirs();
			
			
			Elements elements = doc.select(".course-sections a");
			for (Element el : elements) {
				String videoId = el.attr("data-id");
				el.select("button").remove();
				String title = el.text().replaceAll("\\(.*?\\)", "").trim();
				
				 Document videoDoc = Jsoup.connect("http://www.imooc.com/course/ajaxmediainfo/?mid="+videoId+"&mode=flash").get();
				 System.out.println(videoDoc.text());
				 String videoUrl = JsonPath.read(videoDoc.text(),"$.data.result.mpath[2]");
				 System.out.println(videoUrl);
				URL url = new URL(videoUrl);
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setConnectTimeout(3000);
				conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
				    
				BufferedInputStream bis=new BufferedInputStream(conn.getInputStream());
				System.out.println(file.getPath()+File.separator+title+".mp4");
				BufferedOutputStream bos=new BufferedOutputStream(new FileOutputStream(new File(file.getPath()+File.separator+title+".mp4")));
				byte[] buf=new byte[1024*1024];
				int len=0;
				while((len=bis.read(buf))!=-1){
					bos.write(buf, 0, len);
				}
				bos.flush();
				bos.close();
				bis.close();
				
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}
	
}
