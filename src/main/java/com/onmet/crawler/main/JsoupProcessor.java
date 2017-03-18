package com.onmet.crawler.main;

import java.io.IOException;
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
		String title=".bannertext h1";
		String title_attr="";

		String author=".author_txt .name";
		String author_attr="";

		String date=".pub_time";
		String date_attr="";

		String text="#endText";
		String text_attr="";
		String text_ignore="script,end,.tu span";

		String image="#endText img,.tu img ~ span";
		String image_attr="";
		String image_ignore="";

		String image_url="img";
		String image_url_attr="src";
		String image_url_ignore="";

		String image_title=".tu img ~ span";
		String image_title_attr="";




		
		
		String pageUrl="http://renjian.163.com/17/0314/18/CFGPGB1A000181RV.html";//"http://ly.qq.com/a/20170309/030435.htm";
		try {
			Document doc=Jsoup.connect(pageUrl)
					.userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36")
					.timeout(30000).get();
		
			//String lang = doc.select("html").attr("lang");
			Document cloneDoc = doc.clone();
			
			String titleName="";
			Elements titleEl = doc.select(title);
			if(!titleEl.isEmpty()){
				Element titleEle = titleEl.first();
				if(StringUtils.isNotBlank(title_attr)){
					titleName=titleEle.attr(title_attr);
				}else{
					titleName=titleEle.text();
				}
			}
			System.out.println("标题："+titleName);
			
			
			
			String authorName="";
			Elements authorEl = doc.select(author);
			if(!authorEl.isEmpty()){
				Element authorEle=authorEl.first();
				if(StringUtils.isNotBlank(author_attr)){
					authorName=authorEle.attr(author_attr);
				}else{
					authorName=authorEle.text();
				}
			}
			System.out.println("作者："+authorName);
			
			
			
			String dateVal="";
			Elements dateEl = doc.select(date);
			if(!dateEl.isEmpty()){
				Element dateEle = dateEl.first();
				if(StringUtils.isNotBlank(date_attr)){
					dateVal=dateEle.attr(date_attr);
				}else{
					dateVal=dateEle.text();
				}
			}
			System.out.println("发布时间："+dateVal);
			
			
			Elements elements= doc.select(text);
			List<PageImage> pageImages=new ArrayList<PageImage>();
			if(!elements.isEmpty()){
				elements.select(text_ignore).remove();
				elements.select("img").wrap("<figure></figure>");
				System.out.println(elements);
				Elements imgs = cloneDoc.select(image); 
				for (Element element : imgs) {
					PageImage pageImage=new PageImage();
					pageImage.setPrimary(true);
					
					String src="";
					Elements imgEl = element.select(image_url);
					if(!imgEl.isEmpty()){
						Element img = imgEl.first();
						src = img.attr(image_url_attr);
						pageImage.setUrl(src);
					}
					
					String imgtitle="";
					if(StringUtils.isNotBlank(image_title)){
						Elements select = cloneDoc.select(image).select(image_title);
						if(!select.isEmpty()){
							Element first = select.first();
							if(StringUtils.isNotBlank(image_title_attr)){
								imgtitle = first.attr(image_title_attr);
							}else{
								imgtitle=first.text();
							}
							pageImage.setTitle(imgtitle);
							elements.select("img[src="+src+"]").append("<figcaption>"+imgtitle+"</figcaption>");
						}
					}
					
					
					pageImages.add(pageImage);
				}
				
				
				elements.select("div").tagName("p");
				elements.select("p").removeAttr("id");
				elements.select("p").removeAttr("class");
				elements.select("p").removeAttr("style");
				
				System.out.println(elements.html());
			}
			
			PageArticle pageArticle=new PageArticle();
			pageArticle.setAuthor(author);
			pageArticle.setDate(date);
			//pageArticle.setHumanLanguage(lang);
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
