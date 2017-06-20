package com.onemt.crawler.jsoup;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class JsoupImages {

	
	public static void main(String[] args) throws IOException {
		
		File f=new File("D:\\img");
		if(!f.exists()){
			f.mkdirs();
		}
		
		Document document = Jsoup.connect("http://www.sohu.com/a/139064340_792304?_f=v2-index-feeds&qq-pf-to=pcqq.c2c").get();
		
		Elements select = document.select(".article img");
		
		
		int i=0;
		for (Element element : select) {
			
			System.out.println("http:"+element.attr("src"));
			
			URL url=new URL("http:"+element.attr("src"));
			
			System.out.println(url.getFile().substring(url.getFile().lastIndexOf("/")+1));
			
			BufferedInputStream bis=new BufferedInputStream(url.openStream());
			
			BufferedOutputStream bos=new BufferedOutputStream(new FileOutputStream(new File("D:\\img\\"+i+"_"+url.getFile().substring(url.getFile().lastIndexOf("/")+1))));
			
			byte[] b=new byte[1024*10];
			int len=0;
			while((len=bis.read(b))>0){
				bos.write(b, 0, len);
			}
			bos.flush();
			bos.close();
			bis.close();
			
			i++;
			
			
		}
		
		
		
	}
	
}
