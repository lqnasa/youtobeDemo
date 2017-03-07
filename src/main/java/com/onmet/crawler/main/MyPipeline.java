package com.onmet.crawler.main;

import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.onmet.crawler.pojo.PageArticle;

/**
 * 
 * 项目名称：youtubeCrawler   
 *
 * 类描述：保存数据操作
 * 类名称：com.onmet.crawler.main.MyPipeline     
 * 创建人：liqiao 
 * 创建时间：2017-3-7 上午9:21:21   
 * 修改人：
 * 修改时间：2017-3-7 上午9:21:21   
 * 修改备注：   
 * @version   V1.0
 */
public class MyPipeline implements Pipeline {

	private final Gson gson=new GsonBuilder().create();
	@Override
	public void process(ResultItems resultItems, Task task) {
		PageArticle pageArticle = (PageArticle) resultItems.get("pageArticle");
		String json = gson.toJson(pageArticle, PageArticle.class);
		System.out.println(json);
	}
	
}
