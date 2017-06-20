package com.onemt.crawler.test;

public class Byd implements Car {
	
	
	public Byd(){
		System.out.println("Byd");
	}


	@Override
	public Car createCar() {
		return new Byd();
	}

}
