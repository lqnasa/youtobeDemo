package com.onemt.crawler.test;

public class CarFactory {

	public static Car createCar(Class<? extends Car> clazz){
		try {
			return clazz.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}
}
