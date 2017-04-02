package com.onmet.crawler.monitor;

import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.monitor.SpiderMonitor;
import us.codecraft.webmagic.monitor.SpiderStatus;

public class CustomSpiderStatus extends SpiderStatus implements CustomSpiderStatusMXBean {

    public CustomSpiderStatus(Spider spider, SpiderMonitor.MonitorSpiderListener monitorSpiderListener) {
        super(spider, monitorSpiderListener);
    }


    @Override
    public String getSchedulerName() {
        return spider.getScheduler().getClass().getName();
    }
}