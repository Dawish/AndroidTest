package com.danxx.androidthread;

import com.danxx.model.MyEvent;
import com.danxx.model.MyEventTwo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import de.greenrobot.event.EventBus;
/**
 * EventBus发送消息的activity
 * @author Danxx
 *
 */
public class EventBusTestActivity extends Activity {

	private Button btn1,btn2;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event_bus_test);
		
		btn1 = (Button) findViewById(R.id.btn1);
		btn2 = (Button) findViewById(R.id.btn2);
		
		btn1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				EventBus.getDefault().post(new MyEvent("发送消息01"));
			}
		});
		
		btn2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				EventBus.getDefault().post(new MyEventTwo("发送消息02"));
			}
		});
	}
}
