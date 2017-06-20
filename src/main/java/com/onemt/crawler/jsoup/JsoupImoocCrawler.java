package com.onemt.crawler.jsoup;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.jayway.jsonpath.JsonPath;

public class JsoupImoocCrawler {
	@SuppressWarnings("deprecation")
	private static boolean mockLogin() {  
	    boolean result = false;  
	    
	    List<NameValuePair> nvps = new ArrayList<NameValuePair>();  
	    nvps.add(new BasicNameValuePair("username", "lq1986414@163.com"));  
	    nvps.add(new BasicNameValuePair("password", "nasa23"));  
	    nvps.add(new BasicNameValuePair("referer", "http://m.imooc.com"));  
	    
	    HttpPost httpRequst = new HttpPost("http://m.imooc.com/passport/user/login");//创建HttpPost对象  
	    
	    try {  
            httpRequst.setEntity(new UrlEncodedFormEntity(nvps));  
            CloseableHttpResponse execute = new DefaultHttpClient().execute(httpRequst);  
            if(execute.getStatusLine().getStatusCode() == 200)  
            {  
            	HttpEntity entity = execute.getEntity();
            	/*InputStream in = entity.getContent();
            	BufferedReader br=new BufferedReader(new InputStreamReader(in));
            	String len=null;
            	while((len=br.readLine())!=null){
            		System.out.println(len);
            	}*/
            	
            }  
        } catch (UnsupportedEncodingException e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        }  
        catch (ClientProtocolException e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        }  
        catch (IOException e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        }  
	    return result;  
	}  
	
	public static void main(String[] args) throws Exception {
		//mockLogin();
		//System.out.println("\u53c2\u6570\u975e\u6cd5");
		Map<String, String> map = new HashMap<String, String>();  
		map.put("username", "lq1986414@163.com");  
		map.put("password", "nasa23");  
		map.put("referer", "http://m.imooc.com");
		String url="http://m.imooc.com/video/13590";
		Response execute = Jsoup.connect("http://m.imooc.com/passport/user/login")
				.data(map)
				.userAgent("Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.81 Mobile Safari/537.36")
				.execute();
		//System.out.println(execute.body());
		
		List<String> url2 = JsonPath.read(execute.body(), "$.data.url[*][*]");
		for (String string : url2) {
			System.out.println(string);
		}
		
		
		
	}
	
}
