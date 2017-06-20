package com.onemt.crawler.thread;

import java.util.concurrent.ConcurrentLinkedQueue;

public class MyRunable implements Runnable {

	
	private String str;
	
	private ConcurrentLinkedQueue<String> queue;
	
	public MyRunable(String str, ConcurrentLinkedQueue<String> queue) {
		super();
		this.str = str;
		this.queue = queue;
	}


	@Override
	public void run() {
		
			System.out.println(Thread.currentThread().getName()+" "+str);
			
			if(str.matches(".*0$"))
				queue.add(str);
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}

}
