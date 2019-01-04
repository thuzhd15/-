package com.example.lazy_man_client;

import android.app.Activity; 
import android.content.BroadcastReceiver; 
import android.content.Context; 
import android.content.Intent; 
import android.content.IntentFilter; 
import android.os.Bundle; 
import android.os.Handler; 
import android.os.Message; 
import android.view.View; 
import android.widget.Button; 
import android.widget.EditText; 
import android.widget.TextView; 
import android.widget.Toast; 

public class PasswordActivity extends Activity{
	private Button button_backwards, button_submit;
	private TextView text_oldpass, text_newpass_1, text_newpass_2;
	private EditText edit_oldpass, edit_newpass_1, edit_newpass_2;
	private String user_id, oldpass, newpass_1, newpass_2;
	private String old_pass;
	private MyReceiver receiver;
	private Handler mHandler;
	
	protected void onCreate(Bundle savedIntanceState){
		super.onCreate(savedIntanceState);
		setContentView(R.layout.activity_password);
		oldpass = newpass_1 = newpass_2 = null;
		user_id = Data_all.User_ID;
		
		receiver = new MyReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction("android.intent.action.PasswordActivity");
		registerReceiver(receiver, filter);
		
		mHandler = new Handler() {
			public void handleMessage(android.os.Message msg) {
				String str = msg.obj.toString();
				String substr = str.substring(0, 3);
								
				if (substr.equals("&07")) { // 获取旧密码									
					oldpw_get(str);
				} else if(substr.equals("&04")) { // 修改信息是否成功
					after_change(str);
				}
			}
		};
		
		init();
		
		// 信息初始化：获取用户信息
		String sendstr = "&07&06" + user_id;
		sent(sendstr);
		
		button_backwards.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
				finish();
			}
		});
	
		button_submit.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
				oldpass = edit_oldpass.getText().toString();
				if(old_pass == oldpass){
					newpass_1 = edit_newpass_1.getText().toString();
					newpass_2 = edit_newpass_2.getText().toString();
					if(newpass_1 == null)
						Toast.makeText(getApplicationContext(),"新密码不能为空",Toast.LENGTH_SHORT).show();
					else{
						if(test_str(newpass_1)){
							if(newpass_1 != newpass_2)
								Toast.makeText(getApplicationContext(),"两次新密码不同",Toast.LENGTH_SHORT).show();
							else{
								String sendstr = "&04"+"&06"+user_id+"&01"+newpass_1;
								sent(sendstr);
							}
						}
						else
							showToast("密码中不能带有&符号");
					}
				}
			else
				showToast("旧密码不正确");
			}
		});
	}
	
	// 控件初始化
	public void init(){
		text_oldpass = (TextView) findViewById(R.id.text_oldpass);
		text_newpass_1 = (TextView) findViewById(R.id.text_newpass_1);
		text_newpass_2 = (TextView) findViewById(R.id.text_newpass_2);
		edit_oldpass = (EditText) findViewById(R.id.edit_oldpass);
		edit_newpass_1 = (EditText) findViewById(R.id.edit_newpass_1);
		edit_newpass_2 = (EditText) findViewById(R.id.edit_newpass_2);
		button_backwards = (Button) findViewById(R.id.button_backwards);
		button_submit = (Button) findViewById(R.id.button_submit);
	}
	
	// 获得旧密码
	public void oldpw_get(String str) {
		Usr user = new Usr();
		user.Initial(str);		
		int sign = user.GetErrorType();
		switch (sign) {
		case 0:
			oldpass = user.GetPassword();
			break;
		case 2:
			showToast("连接错误！");
			finish();
			break;
		case 3:
			showToast("获取用户密码失败！");
			finish();
			break;
		case 5:
			showToast("用户不存在！");
			finish();
			break;
		}
	}
		
	// 处理修改后的反馈信息
	public void after_change(String str) {
		Usr user = new Usr();
		user.Initial(str);
		int sign = user.GetErrorType();
		switch (sign) {
		case 0:
			showToast("修改密码成功！");
			break;
		case 2:
			showToast("连接错误！");
			break;
		case 3:
			showToast("修改用户信息失败！");
			break;
		case 4:
			showToast("信息不规范！");
			break;
		}
	}
	
	public void sent(String bs) { // 通过Service发送数据
		Intent intent = new Intent();// 创建Intent对象
		intent.setAction("android.intent.action.cmd");
		intent.putExtra("value", bs);
		sendBroadcast(intent);// 发送广播
	}
	
	public boolean test_str(String str){
		String[] s = str.split("&");
		if(s.length <= 1)
			return true;
		else
			return false;
	}
	
	private class MyReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent){
			if(intent.getAction().equals("android.intent.action.PasswordActivity")) {
				Bundle bundle = intent.getExtras();
				String str = bundle.getString("str");
				Message msg = new Message();
				msg.obj = str;
				mHandler.sendMessage(msg);
			}
		}
	}
	
	// 显示提示信息
	public void showToast(String str) {
		Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT).show();
	}
	
}
