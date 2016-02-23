package com.danxx.model;
/**
 * 自定义事件
 * @author Danxx
 *
 */
public class MyEventTwo {

	private String msg;

	public MyEventTwo(String msg) {
		this.msg = msg;
	}
	
	public String getMessage(){
		return msg;
	}
	
}
