package com.onemt.crawler.pojo;

import java.util.List;

import com.google.gson.annotations.Expose;

/**
 * 
* @ClassName: PageArticle
* @Description: 源文章属性
* @author lennon dai
* @date 2016-11-4 上午11:35:04
*
 */
public class PageArticle {
	@Expose
	public String type;//类型
	@Expose
	public String title;//标题
	@Expose
	public String text;//纯文本内容
	@Expose
	public String html;//html内容
	@Expose
	public String date;//
	@Expose
	public String author;//作者
	@Expose
	public String authorUrl;//作者URL
	//TODO 2017/2/4 不注解@Expose 原因diffbot获取的为对象 gson转换异常 (如必须可以尝试通过 TypeAdapter处理)
	public String discussion;//描述
	@Expose
	public String humanLanguage;//语言
	@Expose
	public String siteName;//站点名称
	@Expose
	public String publisherRegion;//发布区域
	@Expose
	public String publisherCountry;//发布国家
	@Expose
	public String pageUrl;//页面链接
	@Expose
	public List<PageImage> images;//图片
	@Expose
	public List<PageVideo> videos;//视频
	@Expose
	public List<String> tags;//标签
	
	//v1.4 采集视频
	public Integer readCount; //源网站阅读量
	public Integer commentCount; //源网站评论量
	public Integer likeCount; //源网站点赞量
	public String videoId; //视频id
	public Integer videoTime; //视频时间，单位秒
	
	public PageArticle() {}
	
	public PageArticle(String type,String date,String title,String html,String text,List<PageImage> images,String pageUrl) {
		this.type = type;
		this.date = date;
		this.title = title;
		this.html = html;
		this.text = text;
		this.images  =images;
		this.pageUrl = pageUrl;
	}
	
	public List<String> getTags() {
		return tags;
	}
	public void setTags(List<String> tags) {
		this.tags = tags;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getHtml() {
		return html;
	}
	public void setHtml(String html) {
		this.html = html;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getAuthorUrl() {
		return authorUrl;
	}
	public void setAuthorUrl(String authorUrl) {
		this.authorUrl = authorUrl;
	}
	public String getDiscussion() {
		return discussion;
	}
	public void setDiscussion(String discussion) {
		this.discussion = discussion;
	}
	public String getHumanLanguage() {
		return humanLanguage;
	}
	public void setHumanLanguage(String humanLanguage) {
		this.humanLanguage = humanLanguage;
	}
	public String getSiteName() {
		return siteName;
	}
	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}
	public String getPublisherRegion() {
		return publisherRegion;
	}
	public void setPublisherRegion(String publisherRegion) {
		this.publisherRegion = publisherRegion;
	}
	public String getPublisherCountry() {
		return publisherCountry;
	}
	public void setPublisherCountry(String publisherCountry) {
		this.publisherCountry = publisherCountry;
	}
	public String getPageUrl() {
		return pageUrl;
	}
	public void setPageUrl(String pageUrl) {
		this.pageUrl = pageUrl;
	}
	public List<PageImage> getImages() {
		return images;
	}
	public void setImages(List<PageImage> images) {
		this.images = images;
	}
	public List<PageVideo> getVideos() {
		return videos;
	}
	public void setVideos(List<PageVideo> videos) {
		this.videos = videos;
	}

	public Integer getReadCount() {
		return readCount;
	}

	public void setReadCount(Integer readCount) {
		this.readCount = readCount;
	}

	public Integer getCommentCount() {
		return commentCount;
	}

	public void setCommentCount(Integer commentCount) {
		this.commentCount = commentCount;
	}

	public Integer getLikeCount() {
		return likeCount;
	}

	public void setLikeCount(Integer likeCount) {
		this.likeCount = likeCount;
	}

	public String getVideoId() {
		return videoId;
	}

	public void setVideoId(String videoId) {
		this.videoId = videoId;
	}

	public Integer getVideoTime() {
		return videoTime;
	}

	public void setVideoTime(Integer videoTime) {
		this.videoTime = videoTime;
	}
	
	
}
