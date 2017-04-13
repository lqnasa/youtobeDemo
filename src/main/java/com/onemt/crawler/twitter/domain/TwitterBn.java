package com.onemt.crawler.twitter.domain;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * 
 * 项目名称：youtubeCrawler   
 *
 * 类描述：
 * 类名称：com.onemt.crawler.twitter.domain.TwitterBn     
 * 创建人：liqiao 
 * 创建时间：2017-4-13 上午11:16:27   
 * 修改人：
 * 修改时间：2017-4-13 上午11:16:27   
 * 修改备注：   
 * @version   V1.0
 */
public class TwitterBn {

	//新闻源分类编号
	public Integer mediaCategoryId;
	//新闻源ID
	public Integer mediaId;
	//新闻源名称
	public String mediaName;
	//新闻源icon
	public String mediaIcon;
	//快讯地址
	public String sourceUrl;
	//快讯地址MD5
	public String sourceUrlMd5;
	//快讯抓取时间
	public Long fetchTime;
	//快讯发布时间
	public Long publishTime;
	//快讯转发数量
	public Integer retweetCount;
	//快讯评论数量
	public Integer commentCount;
	//快讯点赞数量
	public Integer likeCount;
	//快讯类型
	public String type;
	//快讯作者
	public String author;
	//快讯语言
	public String lang;
	//快讯cdn存储路径
	public String contentUrl;
	//快讯来源分类
	public String sourceCategory;
	//图片
	public List<TwitterImage> images;
	//视频源
	public String videoSource;
	//视频id
	public String videoId; 
	//视频时间，单位秒
	public Integer videoTime; 
	//用于前端展示的内容
	public String content;
	//纯文本正文
	public String textContent;
	//源网站网页正文
	public String htmlContent;

	public TwitterBn() {}

	public TwitterBn(Integer mediaCategoryId, Integer mediaId,
			String mediaName, String mediaIcon, String sourceUrl,
			String sourceUrlMd5, Long fetchTime, Long publishTime,
			Integer retweetCount, Integer commentCount, Integer likeCount,
			String type, String author, String lang, String contentUrl,
			String sourceCategory, List<TwitterImage> images,
			String videoSource, String videoId, Integer videoTime,
			String content, String textContent, String htmlContent) {
		this.mediaCategoryId = mediaCategoryId;
		this.mediaId = mediaId;
		this.mediaName = mediaName;
		this.mediaIcon = mediaIcon;
		this.sourceUrl = sourceUrl;
		this.sourceUrlMd5 = sourceUrlMd5;
		this.fetchTime = fetchTime;
		this.publishTime = publishTime;
		this.retweetCount = retweetCount;
		this.commentCount = commentCount;
		this.likeCount = likeCount;
		this.type = type;
		this.author = author;
		this.lang = lang;
		this.contentUrl = contentUrl;
		this.sourceCategory = sourceCategory;
		this.images = images;
		this.videoSource = videoSource;
		this.videoId = videoId;
		this.videoTime = videoTime;
		this.content = content;
		this.textContent = textContent;
		this.htmlContent = htmlContent;
	}

	public Integer getMediaCategoryId() {
		return mediaCategoryId;
	}

	public void setMediaCategoryId(Integer mediaCategoryId) {
		this.mediaCategoryId = mediaCategoryId;
	}

	public Integer getMediaId() {
		return mediaId;
	}

	public void setMediaId(Integer mediaId) {
		this.mediaId = mediaId;
	}

	public String getMediaName() {
		return mediaName;
	}

	public void setMediaName(String mediaName) {
		this.mediaName = mediaName;
	}

	public String getMediaIcon() {
		return mediaIcon;
	}

	public void setMediaIcon(String mediaIcon) {
		this.mediaIcon = mediaIcon;
	}

	public String getSourceUrl() {
		return sourceUrl;
	}

	public void setSourceUrl(String sourceUrl) {
		this.sourceUrl = sourceUrl;
	}

	public String getSourceUrlMd5() {
		return sourceUrlMd5;
	}

	public void setSourceUrlMd5(String sourceUrlMd5) {
		this.sourceUrlMd5 = sourceUrlMd5;
	}

	public Long getFetchTime() {
		return fetchTime;
	}

	public void setFetchTime(Long fetchTime) {
		this.fetchTime = fetchTime;
	}

	public Long getPublishTime() {
		return publishTime;
	}

	public void setPublishTime(Long publishTime) {
		this.publishTime = publishTime;
	}

	public Integer getRetweetCount() {
		return retweetCount;
	}

	public void setRetweetCount(Integer retweetCount) {
		this.retweetCount = retweetCount;
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}

	public String getContentUrl() {
		return contentUrl;
	}

	public void setContentUrl(String contentUrl) {
		this.contentUrl = contentUrl;
	}

	public String getSourceCategory() {
		return sourceCategory;
	}

	public void setSourceCategory(String sourceCategory) {
		this.sourceCategory = sourceCategory;
	}

	public List<TwitterImage> getImages() {
		return images;
	}

	public void setImages(List<TwitterImage> images) {
		this.images = images;
	}

	public String getVideoSource() {
		return videoSource;
	}

	public void setVideoSource(String videoSource) {
		this.videoSource = videoSource;
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

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getTextContent() {
		return textContent;
	}

	public void setTextContent(String textContent) {
		this.textContent = textContent;
	}

	public String getHtmlContent() {
		return htmlContent;
	}

	public void setHtmlContent(String htmlContent) {
		this.htmlContent = htmlContent;
	}
	
	
}
