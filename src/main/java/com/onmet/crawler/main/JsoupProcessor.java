package com.onmet.crawler.main;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.onmet.crawler.pojo.PageArticle;
import com.onmet.crawler.pojo.PageImage;

public class JsoupProcessor {

	
	
	public static void main(String[] args) {
		String pageUrl="http://ly.qq.com/a/20170309/030435.htm";
		try {
			Document doc=Jsoup.connect(pageUrl)
					.userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36")
					.timeout(30000).get();
		
			String lang = doc.select("html").attr("lang");
			
			String title="";
			Element titleEl = doc.select("#C-Main-Article-QQ h1").first();
			String titleAttr="";
			if(StringUtils.isNotBlank(titleAttr)){
				title=titleEl.attr(titleAttr);
			}else{
				title=titleEl.text();
			}
			System.out.println("标题："+title);
			
			
			
			String author="";
			Element authorEl = doc.select(".color-a-1").first();
			String authorAttr="";
			if(StringUtils.isNotBlank(authorAttr)){
				author=authorEl.attr(authorAttr);
			}else{
				author=authorEl.text();
			}
			System.out.println("作者："+author);
			
			
			
			String date="";
			Element dateEl = doc.select(".article-time").first();
			String dateAttr="";
			if(StringUtils.isNotBlank(dateAttr)){
				date=dateEl.attr(dateAttr);
			}else{
				date=dateEl.text();
			}
			System.out.println("发布时间："+date);
			
			
			
			Elements elements= doc.select("#Cnt-Main-Article-QQ");
			elements.select(".mbArticleShareBtn").remove();
			elements.select("img").wrap("<figure></figure>");
			
			Elements imgs = doc.select("#Cnt-Main-Article-QQ img");
			List<PageImage> pageImages=new ArrayList<PageImage>();
			for (Element element : imgs) {
				PageImage pageImage=new PageImage();
				pageImage.setPrimary(true);
				
				Element img = element.select("img").first();
				
				String src = img.attr("src");
				pageImage.setUrl(src);
				
				String alt = img.attr("alt");
				pageImage.setTitle(alt);
				
				elements.select("img[src="+src+"]").append("<figcaption>"+alt+"</figcaption>");
				
				pageImages.add(pageImage);
			}
			
			
			elements.select("div").tagName("p");
			elements.select("p").removeAttr("id");
			elements.select("p").removeAttr("class");
			elements.select("p").removeAttr("style");
			
			System.out.println(elements.html());
			
			PageArticle pageArticle=new PageArticle();
			pageArticle.setAuthor(author);
			pageArticle.setDate(date);
			pageArticle.setHumanLanguage(lang);
			pageArticle.setType("article");
			pageArticle.setPageUrl(pageUrl);
			
			pageArticle.setText(elements.text());
			pageArticle.setHtml(elements.html());
			pageArticle.setTags(null);
			pageArticle.setImages(pageImages);
			pageArticle.setVideos(null);
			
			Gson gson=new GsonBuilder().disableHtmlEscaping().create();
			String json = gson.toJson(pageArticle);
			System.out.println(json);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}
}
