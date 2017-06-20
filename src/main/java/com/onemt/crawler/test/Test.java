package com.onemt.crawler.test;

public class Test {

	public static void main(String[] args) {
		BMW bmw=(BMW) CarFactory.createCar(BMW.class);
		Byd byd=(Byd) CarFactory.createCar(Byd.class);
		
	}
}
