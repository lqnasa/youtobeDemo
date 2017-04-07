package com.onmet.crawler.main;

public class RegTest {
	public static void main(String[] args) {
		  String str2="aa<a href=\"http://www.islam.ms/ar/%D8%B5%D9%81%D8%A7%D8%AA-%D8%B9%D8%B5%D9%85%D8%A9-%D8%A7%D9%84%D8%A3%D9%86%D8%A8%D9%8A%D8%A7%D8%A1-%D8%A7%D9%84%D8%B1%D8%B3%D9%84/\">صِفات وعِصْمَة الأَنبِيَاءِ والرُّسُل</a>";
		    String str1="أنظر :<a href=\"http://www.islam.ms/ar/%D8%A7%D8%A8%D8%AA%D9%84%D8%A7%D8%A1-%D9%88%D8%B5%D8%A8%D8%B1-%D9%86%D8%A8%D9%8A-%D8%A7%D9%84%D9%84%D9%87-%D8%A3%D9%8A%D9%88%D8%A8/\">ابتلاء وصبر سيدنا أيوب عليه السلام</a>";
		    str1=str1.replaceAll("أنظر :", "");
		    System.out.println(str2.replaceAll("<a.*?href=\"(?!http://www.islam.ms).*?\"[^>]*>[^<]*</a>", "222"));
	}
}
