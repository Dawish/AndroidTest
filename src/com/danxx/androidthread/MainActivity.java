package com.danxx.androidthread;


import com.danxx.model.MyEvent;
import com.danxx.model.MyEventTwo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import de.greenrobot.event.EventBus;
/**
 * Android消息机制
 * 	Message：消息，其中包含了消息ID，消息处理对象以及处理的数据等，由MessageQueue统一列队，终由Handler处理。
 *
 * 	Handler：处理者，负责Message的发送及处理。使用Handler时，需要实现handleMessage(Message msg)方法来对特定的Message进行处理，例如更新UI等。
 *
 *	MessageQueue：消息队列，用来存放Handler发送过来的消息，并按照FIFO规则执行。当然，存放Message并非实际意义的保存，而是将Message以链表的方式串联起来的，等待Looper的抽取。
 *
 *	Looper：消息泵，不断地从MessageQueue中抽取Message执行。因此，一个MessageQueue需要一个Looper。
 *
 *	Thread：线程，负责调度整个消息循环，即消息循环的执行场所。
 *	
 ************************总结********************************
 *
 *	 Handler的处理过程运行在创建Handler的线程里
 *
 *   一个Looper对应一个MessageQueue
 *
 *   一个线程对应一个Looper
 *
 *   一个Looper可以对应多个Handler
 *
 *   不确定当前线程时，更新UI时尽量调用post方法
 * 
 * 请参考： 
 * http://www.cnblogs.com/codingmyworld/archive/2011/09/14/2174255.html#!comments
 * http://www.cnblogs.com/xirihanlin/archive/2011/04/11/2012746.html
 *
 * @author Danxx
 *
 */
public class MainActivity extends Activity {

	private Handler handler01;
	private TextView textview,tv;
	private Button btn1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		/**注册**/
		EventBus.getDefault().register(this);
		
		textview  = (TextView) findViewById(R.id.textview);
		tv = (TextView) findViewById(R.id.tv);
		btn1 = (Button) findViewById(R.id.btn1);
		btn1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this ,EventBusTestActivity.class);
				startActivity(intent);
			}
		});
		
		Thread workThread = new Thread(new SampleTask(new MyHandler()));
		
		workThread.start();
		
	}
	
	/**
	 * 订阅函数
	 * 运行在UI线程
	 * @param event
	 */
	public void onEventMainThread(MyEvent event){
		tv.setText(event.getMessage());
		Toast.makeText(this, event.getMessage(), Toast.LENGTH_SHORT).show();
	}
	
	/**
	 * 订阅函数
	 * 运行在子线程，要与UI线程通讯就需要使用handler
	 * @param event
	 */
	public void onEventBackgroundThread(MyEventTwo event){
		handler.sendEmptyMessage(2);
	}
	
	private Handler handler = new Handler(new Callback() {
		
		@Override
		public boolean handleMessage(Message msg) {
			// TODO Auto-generated method stub
			tv.setText("发送消息02");
			Toast.makeText(MainActivity.this, "发送消息02", Toast.LENGTH_SHORT).show();
			return false;
		}
	});
	
	public void appendText(String msg) {
        textview.setText(textview.getText() + "\n" + msg);
    }
	
	/**自定义handler**/
	class MyHandler extends Handler{
		@Override
		public void handleMessage(Message msg) {
			
			String result = msg.getData().getString("message");
		    // 更新UI
            appendText(result);
		}
	}
	
	/**
	 * 自己写的线程与MainThread线程通讯
	 * @author Danxx
	 *
	 */
	class SampleTask implements Runnable {
	    private String TAG = "danxx";
	    Handler handler;
	    
	    /**
	     * 传入handler，不然不知道要把message发送到那个线程的MessageQueue
	     * 这里是传入的MainThread线程中handler01的引用
	     * @param handler 为handler的引用
	     */
	    public SampleTask(Handler handler) {
	        super();
	        this.handler = handler;
	    }

	    @Override
	    public void run() {
	        try {  // 模拟执行某项任务，下载等
	            Thread.sleep(5000);
	            // 任务完成后通知activity更新UI
	            Message msg = prepareMessage("task completed!");
	            // message将被添加到主线程的MQ中
	            handler.sendMessage(msg);
	        } catch (InterruptedException e) {
	            Log.d(TAG, "interrupted!");
	        }

	    }
        /**组装放入的msg**/
	    private Message prepareMessage(String str) {
	    	/**从传入的handler获取Messahe对象，从而可以直接向该handler对象发送消息**/
	        Message result = handler.obtainMessage();
	        Bundle data = new Bundle();
	        data.putString("message", str);
	        result.setData(data);
	        return result;
	    }

	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		/**取消注册**/
		EventBus.getDefault().unregister(this);
	}
}
