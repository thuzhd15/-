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

public class InformationActivity extends Activity{
	private String user_id;
	private String username, realname, tele, email, school;
	private TextView text_username, text_realname, text_tele, text_email, text_school;
	private EditText edit_username, edit_realname, edit_tele, edit_email, edit_school;
	private Button button_submit, button_backwards;
	private MyReceiver receiver;
	private Handler mHandler;
	
	protected void onCreate(Bundle savedIntanceState){
		super.onCreate(savedIntanceState);
		setContentView(R.layout.activity_information);
		username = realname = tele = email = school = null;
		user_id = Data_all.User_ID;
		receiver = new MyReceiver(); // 注册广播 
		IntentFilter filter = new IntentFilter(); 
		filter.addAction("android.intent.action.InformationActivity"); 
		registerReceiver(receiver, filter);
		
		mHandler = new Handler() {
			public void handleMessage(android.os.Message msg) {
				String str = msg.obj.toString();
				String substr = str.substring(0, 3);
				
				if (substr.equals("&06")) { // 显示用户信息										
					showinfo(str);									
				} else if(substr.equals("&02")) {  // 修改信息是否成功					
					after_change(str);				
				}
			}
		};
		
		init();
		
		// 信息初始化：获取用户信息
		String sendstr = "&06&06" + user_id;
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
				username = edit_username.getText().toString();
				realname = edit_realname.getText().toString();
				tele = edit_tele.getText().toString();
				email = edit_email.getText().toString();
				school = edit_school.getText().toString();
				if(username == null || realname == null || tele == null || email == null || school == null ){
					Toast.makeText(getApplicationContext(),"信息不能为空",Toast.LENGTH_SHORT).show();
				}
				else{
					if(test_str(username)&&test_str(realname)&&test_str(email)&&test_str(school)){
						if(test_phone(tele)){
							String sendstr = "&02"+"&06"+user_id+"&00"+username+"&02"+tele+"&03"+email+"&05"+realname+"&07"+school;
							sent(sendstr);
						}
						else
							showToast("手机号输入格式不正确");
					}
					else
						showToast("请勿输入带有&符号！");
				}
			}
		});
	}
	
	// 控件初始化
	public void init(){
		text_username = (TextView) findViewById(R.id.text_username);
		text_realname = (TextView) findViewById(R.id.text_realname);
		text_tele = (TextView) findViewById(R.id.text_tele);
		text_school = (TextView) findViewById(R.id.text_school);
		text_email = (TextView) findViewById(R.id.text_email);
		edit_username = (EditText) findViewById(R.id.edit_username);
		edit_realname = (EditText) findViewById(R.id.edit_realname);
		edit_tele = (EditText) findViewById(R.id.edit_tele);
		edit_email = (EditText) findViewById(R.id.edit_email);
		edit_school = (EditText) findViewById(R.id.edit_school);
		
		button_backwards = (Button) findViewById(R.id.button_backwards);
		button_submit = (Button) findViewById(R.id.button_submit);
	}
	
	// 显示用户信息
	public void showinfo(String str) {
		Usr user = new Usr();
		user.Initial(str);
				
		int sign = user.GetErrorType();
		switch (sign) {
		case 0:
			username = user.GetUsrName();
			realname = user.GetRealName();
			tele = user.GetTeleNumber();
			email = user.GetEmail();
			school = user.GetSchool();
			
			edit_username.setText(username);
			edit_realname.setText(realname);
			edit_tele.setText(tele);
			edit_email.setText(email);
			edit_school.setText(school);
			break;
		case 2:
			showToast("连接错误！");
			break;
		case 3:
			showToast("获取用户信息失败！");
			break;
		case 5:
			showToast("用户不存在！");
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
			showToast("修改用户信息成功！");
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
	
	public boolean test_str(String str){
		String[] s = str.split("&");
		if(s.length <= 1)
			return true;
		else
			return false;
	}
	
	public boolean test_phone(String str){
		if(str.length() == 11)
			return true;
		else
			return false;
	}
	
	public void sent(String bs) { // 通过Service发送数据
		Intent intent = new Intent();// 创建Intent对象
		intent.setAction("android.intent.action.cmd");
		intent.putExtra("value", bs);
		sendBroadcast(intent);// 发送广播
	}
	
	private class MyReceiver extends BroadcastReceiver{
		@Override
		public void onReceive(Context context, Intent intent){
			if (intent.getAction().equals("android.intent.action.InformationActivity")) {
				Bundle bundle = intent.getExtras();
				String str = bundle.getString("str");
				Message msg = new Message();
				msg.obj = str;
				mHandler.sendMessage(msg);
			}
		}
	}
	
	public void showToast(String str) {// 显示提示信息
		Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT).show();
	}
}