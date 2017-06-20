package com.onemt.crawler.jsoup;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.FormElement;

public class JsoupCrawler {

	public static void main(String[] args) throws IOException {
		/*
		 * gads=ID=40325e3dd84d055a:T=1488445290:S=ALNI_MYn4WliGBQsk8GpmHntt-
		 * q90n8KZA; _ym_uid=1488445294262646469;
		 * __qca=P0-1436669615-1493473450690;
		 * userid=057B3123-6BF9-4A9D-C6D3-E4C9393002A5; _ym_isad=2; _tb_sess_r=;
		 * _gat=1; _tb_t_article=article01_opt; _gat__pm_ga=1;
		 * _tb_t_ppg=https%3A
		 * //arabic.rt.com/technology/876019-%25D8%25A7%25D9%2584
		 * %25D8%25B9%25D8%
		 * 25AB%25D9%2588%25D8%25B1-%25D8%25B9%25D9%2584%25D9%2589
		 * -%25D8%25A3%25D
		 * 9%2582%25D8%25AF%25D9%2585-%25D8%25A2%25D8%25AB%25D8%25
		 * A7%25D8%25B1-%25D
		 * 9%2584%25D9%2584%25D8%25A8%25D8%25B4%25D8%25B1-%25D9%
		 * 2581%25D9%258A-%25D
		 * 8%25A3%25D9%2585%25D8%25B1%25D9%258A%25D9%2583%25D8%25A7/;
		 * position=27; GED_PLAYLIST_ACTIVITY=
		 * W3sidSI6Im90UkEiLCJ0c2wiOjE0OTM2OTY2MjQsIm52IjoxLCJ1cHQiOjE0OTM2OTY1OTksImx0IjoxNDkzNjk2NjIzfV0
		 * .; _ga=GA1.2.1639415629.1488445275; _gid=GA1.2.1875009351.1493696625;
		 * _em_t=true; __atuvc=0%7C14%2C1%7C15%2C5%7C16%2C1%7C17%2C3%7C18;
		 * __atuvs=5907ffc90ec3bc6e002;
		 * _em_vt=36ba4887058932ee80a2dd70a46358a3acad8f4aa5-945848695908007f; _
		 * em_v=2b248337514976e0d1b4e385c6ad5907ffd70539f9-416522945908007f;
		 * _ym_visorc_23121313=b
		 */

		String url = "http://www.noonews.com";
		String sendHttpsGet = sendHttpsGet(url);
		 System.out.println(sendHttpsGet);

		Document parse = Jsoup.parse(sendHttpsGet);

	/*	List<FormElement> forms = parse.select("#challenge-form").forms();// 获取form表单，可以通过查看页面源码代码得知
		// 获取，cooking和表单属性，下面map存放post时的数据
		Map<String, String> datas = new HashMap<>();
		FormElement formElement = forms.get(0);
		System.out.println("====="+formElement);
		formElement.setBaseUri("http://www.noonews.com");
		Connection submit =formElement.submit();
		Document document = submit.get();
		System.out.println(document.html());*/
		
		Element pass = parse.select("#challenge-form input[name=pass]").first();
		String passattr = pass.attr("value");
		Element jschl_vc = parse.select("#challenge-form input[name=jschl_vc]").first();
		String jschl_vcattr = jschl_vc.attr("value");
		Element jschl_answer = parse.select("#challenge-form input[name=jschl-answer]").first();
		String jschl_answerattr = jschl_vc.attr("value");
		System.out.println(url+"/cdn-cgi/l/chk_jschl?pass="+passattr+"&jschl_vc="+jschl_vcattr+"&jschl_answer="+2105);
		//ttp://www.noonews.com/cdn-cgi/l/chk_jschl?jschl_vc=dada869a005e32bdd2b012b93074ec39&pass=1493725506.752-XXavUIqkKa&jschl_answer=57840
		Document doc = Jsoup.connect(url+"/cdn-cgi/l/chk_jschl?pass="+passattr+"&jschl_vc="+jschl_vcattr+"&jschl_answer="+2105).userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.81 Safari/537.36").get();
		System.out.println(doc.html());

		/**
		 * 第二次请求，post表单数据，以及cookie信息
		 * 
		 * **/
		/*
		 * Connection con2=Jsoup.connect("http://www.iteye.com/login");
		 * con2.header("User-Agent",
		 * "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:29.0) Gecko/20100101 Firefox/29.0"
		 * ); //设置cookie和post上面的map数据 Response
		 * login=con2.ignoreContentType(true)
		 * .method(Method.POST).data(datas).cookies(rs.cookies()).execute();
		 * //打印，登陆成功后的信息 System.out.println(login.body());
		 * 
		 * //登陆成功后的cookie信息，可以保存到本地，以后登陆时，只需一次登陆即可 Map<String, String>
		 * map=login.cookies(); for(String s:map.keySet()){
		 * System.out.println(s+"      "+map.get(s)); }
		 */

		/*
		 * Response res = Jsoup .connect(url) .timeout(50000)
		 * //.cookie("userid",
		 * "057B3123-6BF9-4A9D-C6D3-E4C9393002A5")//057B3123-
		 * 6BF9-4A9D-C6D3-E4C9393002A5 .userAgent(
		 * "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.81 Safari/537.36"
		 * ) .execute(); int statusCode = res.statusCode();
		 * System.out.println(statusCode);
		 */

	}

	/**
	 * 发送 get请求Https
	 * 
	 * @param httpUrl
	 */
	public static String sendHttpsGet(String httpUrl) {
		HttpGet httpGet = new HttpGet(httpUrl);// 创建get请求
		return sendHttpGet(httpGet);
	}

	private static RequestConfig requestConfig = RequestConfig.custom()
			.setSocketTimeout(15000).setConnectTimeout(15000)
			.setConnectionRequestTimeout(15000).build();

	/**
	 * 发送Get请求
	 * 
	 * @param httpPost
	 * @return
	 */
	private static String sendHttpGet(HttpGet httpGet) {
		CloseableHttpClient httpClient = null;
		CloseableHttpResponse response = null;
		HttpEntity entity = null;
		String responseContent = null;
		try {
			// 创建默认的httpClient实例.
			httpClient = HttpClients.createDefault();
			httpGet.setConfig(requestConfig);
			// 执行请求
			response = httpClient.execute(httpGet);
			entity = response.getEntity();
			responseContent = EntityUtils.toString(entity, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				// 关闭连接,释放资源
				if (response != null) {
					response.close();
				}
				if (httpClient != null) {
					httpClient.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return responseContent;
	}
}
