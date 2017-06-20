package com.onemt.crawler.test;

public class BMW implements Car {
	
	public BMW(){
		System.out.println("bmw");
	}

	@Override
	public Car createCar() {
		// TODO Auto-generated method stub
		return new BMW();
	}

}
