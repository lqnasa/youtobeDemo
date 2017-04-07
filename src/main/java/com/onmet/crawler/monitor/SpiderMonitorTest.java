package com.onmet.crawler.monitor;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.monitor.SpiderMonitor;
import us.codecraft.webmagic.monitor.SpiderStatusMXBean;
import us.codecraft.webmagic.processor.example.GithubRepoPageProcessor;
import us.codecraft.webmagic.processor.example.ZhihuPageProcessor;
import us.codecraft.webmagic.scheduler.RedisScheduler;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:spring/redis.xml"})
public class SpiderMonitorTest {

	@Autowired
	private RedisTemplate<String,String> jedisTemplate;
	
	@Value("${redis.domain}")
	private String host;
	
    @Test
    public void testInherit() throws Exception {
    	System.out.println(host);
        SpiderMonitor spiderMonitor = new SpiderMonitor(){
            @Override
            protected SpiderStatusMXBean getSpiderStatusMBean(Spider spider, MonitorSpiderListener monitorSpiderListener) {
                return new CustomSpiderStatus(spider, monitorSpiderListener);
            }
        };

        Spider zhihuSpider = Spider.create(new ZhihuPageProcessor()).setScheduler(new RedisScheduler(host))
                .addUrl("http://my.oschina.net/flashsword/blog").thread(2);
        Spider githubSpider = Spider.create(new GithubRepoPageProcessor()).setScheduler(new RedisScheduler(host))
                .addUrl("https://github.com/code4craft");

        spiderMonitor.register(zhihuSpider, githubSpider);
        
        zhihuSpider.runAsync();
        githubSpider.runAsync();
        
        System.in.read();

    }
}