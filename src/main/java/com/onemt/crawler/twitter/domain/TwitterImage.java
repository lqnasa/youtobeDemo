package com.onemt.crawler.twitter.domain;

import com.google.gson.annotations.Expose;

/**
 * 
 * 项目名称：youtubeCrawler   
 *
 * 类描述：
 * 类名称：com.onemt.crawler.twitter.domain.TwitterImage     
 * 创建人：liqiao 
 * 创建时间：2017-4-13 上午11:29:27   
 * 修改人：
 * 修改时间：2017-4-13 上午11:29:27   
 * 修改备注：   
 * @version   V1.0
 */
public class TwitterImage {
	
	@Expose
	public String url;// 地址
	@Expose
	public String title;// 图片描述
	@Expose
	public Integer naturalHeight;//图片高度
	@Expose
	public Integer naturalWidth;//图片宽度
	@Expose
	public boolean primary;//是否主图
	
	public TwitterImage() {
	}
	
	public TwitterImage(String url, String title, Integer naturalHeight,
			Integer naturalWidth, boolean primary) {
		this.url = url;
		this.title = title;
		this.naturalHeight = naturalHeight;
		this.naturalWidth = naturalWidth;
		this.primary = primary;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getNaturalHeight() {
		return naturalHeight;
	}

	public void setNaturalHeight(Integer naturalHeight) {
		this.naturalHeight = naturalHeight;
	}

	public Integer getNaturalWidth() {
		return naturalWidth;
	}

	public void setNaturalWidth(Integer naturalWidth) {
		this.naturalWidth = naturalWidth;
	}

	public boolean isPrimary() {
		return primary;
	}

	public void setPrimary(boolean primary) {
		this.primary = primary;
	}

	@Override
	public String toString() {
		return "TwitterImage [url=" + url + ", title=" + title
				+ ", naturalHeight=" + naturalHeight + ", naturalWidth="
				+ naturalWidth + ", primary=" + primary + "]";
	}
	
	
	
}
