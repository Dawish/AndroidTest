package com.danxx.model;
/**
 * 定义事件
 * @author Danxx
 *
 */
public class MyEvent {
	
	private String msg;

	public MyEvent(String msg) {
		this.msg = msg;
	}
	
	public String getMessage(){
		return msg;
	}
}
