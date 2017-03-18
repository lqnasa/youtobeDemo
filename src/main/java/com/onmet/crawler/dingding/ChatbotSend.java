package com.onmet.crawler.dingding;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class ChatbotSend {
	
    public static String WEBHOOK_TOKEN = "https://oapi.dingtalk.com/robot/send?access_token=bde2a8f7faa6f8babd07f26c98b81b78531219d1c7d86a09c9a72996fc682151";
 
    private static Gson gson=new GsonBuilder().create();
    
    public static void main(String args[]) throws Exception{
 
        HttpClient httpclient = HttpClients.createDefault();
        HttpPost httppost = new HttpPost(WEBHOOK_TOKEN);
        httppost.addHeader("Content-Type", "application/json; charset=utf-8");
 
        DingDingMessage dingDingMessage=new DingDingMessage();
        dingDingMessage.setMsgtype(DingDingMessage.MSGTYPE_LINK);
        Link link=new Link();
        link.setTitle("this is link msg");
        link.setText("hello world");
        link.setMessageUrl("http://renjian.163.com/17/0314/18/CFGPGB1A000181RV.html");
        dingDingMessage.setLink(link);
        dingDingMessage.setIsAtAll(true);
        String textMsg = gson.toJson(dingDingMessage);
        StringEntity se = new StringEntity(textMsg, "utf-8");
        httppost.setEntity(se);
 
        HttpResponse response = httpclient.execute(httppost);
        if (response.getStatusLine().getStatusCode()== HttpStatus.SC_OK){
            String result= EntityUtils.toString(response.getEntity());
            System.out.println(result);
        }
    }
}

class DingDingMessage{

	public static final String MSGTYPE_TEXT="text";
	public static final String MSGTYPE_LINK="link";
	public static final String MSGTYPE_MARKDOWN="markdown";
	
	private String msgtype;
	private Text text;
	private Link link;
	private Markdown markdown;
	private At at;
	private Boolean isAtAll;
	
	public DingDingMessage() {
	}
	public DingDingMessage(String msgtype, Text text, Link link, Markdown markdown, At at, Boolean isAtAll) {
		this.msgtype = msgtype;
		this.text = text;
		this.link = link;
		this.markdown = markdown;
		this.at = at;
		this.isAtAll = isAtAll;
	}
	public String getMsgtype() {
		return msgtype;
	}
	public void setMsgtype(String msgtype) {
		this.msgtype = msgtype;
	}
	public Text getText() {
		return text;
	}
	public void setText(Text text) {
		this.text = text;
	}
	public Link getLink() {
		return link;
	}
	public void setLink(Link link) {
		this.link = link;
	}
	public Markdown getMarkdown() {
		return markdown;
	}
	public void setMarkdown(Markdown markdown) {
		this.markdown = markdown;
	}
	public At getAt() {
		return at;
	}
	public void setAt(At at) {
		this.at = at;
	}
	public Boolean getIsAtAll() {
		return isAtAll;
	}
	public void setIsAtAll(Boolean isAtAll) {
		this.isAtAll = isAtAll;
	}
	
	
}

class Text{
	private String content;

	public Text(){}
	public Text(String content) {
		super();
		this.content = content;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
}

class Link{
	private String title;
	private String text;
	private String picUrl;
	private String messageUrl;
	
	public Link() {
		super();
	}

	public Link(String title, String text, String picUrl, String messageUrl) {
		super();
		this.title = title;
		this.text = text;
		this.picUrl = picUrl;
		this.messageUrl = messageUrl;
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

	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}

	public String getMessageUrl() {
		return messageUrl;
	}

	public void setMessageUrl(String messageUrl) {
		this.messageUrl = messageUrl;
	}
	
	
}
class Markdown{
	private String title;
	private String text;
	public Markdown(){}
	public Markdown(String title, String text) {
		super();
		this.title = title;
		this.text = text;
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
}

class At{
	private String atMobiles;
	public At(){}

	public String getAtMobiles() {
		return atMobiles;
	}

	public void setAtMobiles(String atMobiles) {
		this.atMobiles = atMobiles;
	}
	
}
