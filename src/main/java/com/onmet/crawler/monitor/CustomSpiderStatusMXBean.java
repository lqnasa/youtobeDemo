package com.onmet.crawler.monitor;

import us.codecraft.webmagic.monitor.SpiderStatusMXBean;

public interface CustomSpiderStatusMXBean extends SpiderStatusMXBean {

    public String getSchedulerName();

}