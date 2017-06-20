package com.onemt.crawler.thread;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

	public static void main(String[] args) {
		List<String> list=new ArrayList<>();
		for (int i = 0; i < 20; i++) {
			list.add("test"+i);
		}
		ConcurrentLinkedQueue<String> queue=new ConcurrentLinkedQueue<String>();
		  
		ExecutorService executorService = Executors.newFixedThreadPool(3);
		
		for (String str : list) {
			MyRunable myRunable=new MyRunable(str,queue);
			executorService.execute(myRunable);
		}
		
		executorService.shutdown();
		
		new MyThread(executorService,queue).start();
		
		
		
	}
}

class MyThread extends Thread{
	
	private ConcurrentLinkedQueue<String> queue;
	
	private ExecutorService executorService;

	public MyThread(ExecutorService executorService,ConcurrentLinkedQueue<String> queue) {
		this.executorService=executorService;
		this.queue = queue;
	}


	@Override
	public void run() {
		while(!executorService.isTerminated() ){
			if(!queue.isEmpty())
				System.out.println(queue.poll());
		}
	}
	
	
}
