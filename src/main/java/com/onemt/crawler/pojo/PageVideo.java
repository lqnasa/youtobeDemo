package com.onemt.crawler.pojo;

import com.google.gson.annotations.Expose;

/**
 * 
* @ClassName: PageVideo
* @Description: 来源网站视频信息对象 
* @author lennon dai
* @date 2016-11-4 上午11:31:27
*
 */
public class PageVideo {
	@Expose
	public String url;//视频地址
	@Expose
	public Integer naturalHeigh;//原始高度
	@Expose
	public Integer naturalWidth;//原始长度
	@Expose
	public boolean primary;//是否主视频
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public Integer getNaturalHeigh() {
		return naturalHeigh;
	}
	public void setNaturalHeigh(Integer naturalHeigh) {
		this.naturalHeigh = naturalHeigh;
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
