package com.onemt.crawler.pojo;

import com.google.gson.annotations.Expose;

/**
 * 
* @ClassName: PageImage
* @Description: 
* @author lennon dai
* @date 2016-11-4 上午11:33:13
*
 */
public class PageImage {
	
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
	
	public PageImage() {
	}
	
	public PageImage(String url, String title, Integer naturalHeight,
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
	
	
	
}
