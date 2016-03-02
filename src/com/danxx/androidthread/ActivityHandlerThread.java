package com.danxx.androidthread;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;
/**
 * 使用HandlerThread创建线程
 * @author Danxx
 *
 */
public class ActivityHandlerThread extends Activity {
	private TextView tv,tv2;
	private Handler mainHandler = new Handler(new Callback() {
		
		@Override
		public boolean handleMessage(Message msg) {
			Log.d("danxx", "UI线程接受到HandlerThread线程发送过来的消息");
			tv2.setText("UI线程接受到HandlerThread线程发送过来的消息");
			return false;
		}
	});
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_activity_handler_thread);
		
		tv = (TextView) findViewById(R.id.tv);
		tv2 = (TextView) findViewById(R.id.tv2);
		
		/*使用HandlerThread开一个新线程*/
		HandlerThread handlerThread = new HandlerThread("handlerThread");
		/*启动线程*/
		handlerThread.start();
		/*为HandlerThread线程创建一个handler*/
		MyHandler handler = new MyHandler(handlerThread.getLooper());
		/*为handlerThread线程的handler创建一个Message*/
		Message msg = handler.obtainMessage();
		/*发送msg到handlerThread线程的MQ*/
		msg.sendToTarget();
		
		/**在UI线程为HandlerThread线程添加任务**/
		handler.post(new MyTask(mainHandler));
		
	}
	/**
	 *handlerThread的handler
	 */
	class MyHandler extends Handler{
		
		public MyHandler(){
			
		}
		
		public MyHandler(Looper looper){
			super(looper);
		}

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			Log.d("danxx", "handlerThread线程接受到UI线程发送来的消息");
		}
	}
	/*
	 * 通过handlerThread的handler的post方法可以把MyTask放在handlerThread中执行
	 */
	class MyTask implements Runnable{
		Handler handler;
		/**
		 * 传入UI线程的handler
		 * @param handler
		 */
		public MyTask(Handler handler) {
			this.handler = handler;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				/*模拟耗时操作*/
				Thread.sleep(1000*6);
				Message msg = handler.obtainMessage();
				msg.sendToTarget();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
}
