package com.onmet.crawler.jsoup;

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

		String image="#endText img";
		String image_attr="";
		String image_ignore="";

		String image_url="img";
		String image_url_attr="src";
		String image_url_ignore="";

		String image_title="img";
		String image_title_attr="alt";




		
		
		String pageUrl="http://renjian.163.com/17/0314/18/CFGPGB1A000181RV.html";//"http://ly.qq.com/a/20170309/030435.htm";
		try {
			Document doc=Jsoup.connect(pageUrl)
					.userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36")
					.timeout(30000).get();
		
			//String lang = doc.select("html").attr("lang");
			//Document cloneDoc = doc.clone();
			
			
			//实现文章title抽取
			String titleName=doc.title();
			if(StringUtils.isNotBlank(title)){
				Elements titleEls = doc.select(title);
				if(!titleEls.isEmpty()){
					Element titleEl = titleEls.first();
					titleName=StringUtils.isNotBlank(title_attr)?titleEl.attr(title_attr):titleEl.text();
				}
			}
			System.out.println("标题："+titleName);
			
			
			//抽取作者
			String authorName="";
			if(StringUtils.isNotBlank(author)){
				Elements authorEls = doc.select(author);
				if(!authorEls.isEmpty()){
					Element authorEl=authorEls.first();
					authorName=StringUtils.isNotBlank(author_attr)?authorEl.attr(author_attr):authorEl.text();
				}
			}
			System.out.println("作者："+authorName);
			
			
			//抽取发布时间
			String dateVal="";
			if(StringUtils.isNotBlank(date)){
				Elements dateEls = doc.select(date);
				if(!dateEls.isEmpty()){
					Element dateEl = dateEls.first();
					dateVal=StringUtils.isNotBlank(date_attr)?dateEl.attr(date_attr):dateEl.text();
				}
			}
			System.out.println("发布时间："+dateVal);
			
			//抽取图片和备注
			List<PageImage> pageImages=new ArrayList<PageImage>();
			if(StringUtils.isNotBlank(image)){
				Elements imgs = doc.select(image);
				if(!imgs.isEmpty()){
					if(StringUtils.isNotBlank(image_ignore)){
						imgs.select(image_ignore).remove();
					}
					for (Element img : imgs) {
						PageImage pageImage=new PageImage();
						Element imgEl=StringUtils.isNotBlank(image_url)?img.select(image_url).first():img.select("img").first();
						String url=StringUtils.isNotBlank(image_url)?imgEl.attr(image_url_attr):img.attr("src");
						pageImage.setPrimary(true);
						pageImage.setUrl(url);
						
						if(StringUtils.isNotBlank(image_title)){
							Elements imgTitleEls=img.select(image_title);
							if(!imgTitleEls.isEmpty()){
								String imgTitle=StringUtils.isNotBlank(image_title_attr)?imgTitleEls.first().attr(image_title_attr):imgTitleEls.first().text();
								pageImage.setTitle(imgTitle);
							}
						}
						
						pageImages.add(pageImage);
					}
				}
			}
			
			
			
			//抽取文本
			String htmlText="";
			String html="";
			if(StringUtils.isNoneBlank(text)){
				Elements htmlEls= doc.select(text);
				if(!htmlEls.isEmpty()){
					//过滤不需要的元素
					if(StringUtils.isNotBlank(text_ignore))
						htmlEls.select(text_ignore).remove();
					htmlEls.select("img").wrap("<figure></figure>");
					for (PageImage pageImage : pageImages) {
						if(StringUtils.isNotBlank(pageImage.getTitle())){
							htmlEls.select("img[src="+pageImage.getUrl()+"]").append("<figcaption>"+pageImage.getTitle()+"</figcaption>");
						}
					}
				}
				
				htmlEls.select("div").tagName("p");
				htmlEls.select("p").removeAttr("id");
				htmlEls.select("p").removeAttr("class");
				htmlEls.select("p").removeAttr("style");
				
				html=htmlEls.html();
				htmlText=htmlEls.text();
			}

		   System.out.println(html);
			
			
			PageArticle pageArticle=new PageArticle();
			pageArticle.setAuthor(authorName);
			pageArticle.setDate(dateVal);
			pageArticle.setTitle(titleName);
			//pageArticle.setHumanLanguage(lang);
			pageArticle.setType("article");
			pageArticle.setPageUrl(pageUrl);
			
			pageArticle.setText(htmlText);
			pageArticle.setHtml(html);
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
