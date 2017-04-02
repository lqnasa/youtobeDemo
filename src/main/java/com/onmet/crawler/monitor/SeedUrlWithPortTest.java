package com.onmet.crawler.monitor;

import javax.management.JMException;

import org.junit.Test;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.monitor.SpiderMonitor;
import us.codecraft.webmagic.processor.PageProcessor;

public class SeedUrlWithPortTest {

    @Test
    public void testSeedUrlWithPort() throws JMException {
        Spider spider = Spider.create(new TempProcessor()).addUrl("http://www.hndpf.org:8889/");
        SpiderMonitor.instance().register(spider);
        spider.run();
    }
}

class TempProcessor implements PageProcessor {

    @Override
    public void process(Page page) {

    }

    @Override
    public Site getSite() {
        return Site.me();
    }
}