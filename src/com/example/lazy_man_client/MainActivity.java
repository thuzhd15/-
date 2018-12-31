//登录界面（初始界面）
package com.example.lazy_man_client;

import android.app.Activity;
//import android.app.PendingIntent;
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
//import android.app.Notification;
//import android.app.NotificationManager;
//import android.support.v4.app.NotificationCompat;

public class MainActivity extends Activity {
	private Button button_login, button_regist;
	private EditText edit_username, edit_key; // 用户输入控件
	private Handler mHandler;
	// private int stateflag = 0; // 0:wait 1:OK -1:fail
	MyReceiver receiver;
	Usr curuser;

	// boolean stop = true;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		init(); // 控件初始化
		curuser = new Usr();
		receiver = new MyReceiver(); // 注册广播
		IntentFilter filter = new IntentFilter();
		filter.addAction("android.intent.action.MainActivity"); //此处改成自己activity的名字
		registerReceiver(receiver, filter);
		/*
		 * 登录,若成功则进入任务页面 发送用户名和密码 示例：用户“李四”登录，密码20482048；发送字符 &01&李四&20482048
		 * 需要服务器反馈：用户是否存在/密码是否正确
		 */
		button_login.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// 当点击按钮时,会获取编辑框中的数据,然后提交给线程
				String sendstr = "&01&00" + edit_username.getText().toString()
						+ "&01" + edit_key.getText();
				setAll(false);
				sent(sendstr);
			}
		});
		// 进入注册页面
		button_regist.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MainActivity.this,
						RegistActivity.class);
				startActivity(intent);
			}
		});
		// 在连接和发送数据之后，接下来就是处理了,发送的数据会通过message的方式传递到消息队列,再由handl进行获取
		mHandler = new Handler() {
			public void handleMessage(android.os.Message msg) {
				String strall = msg.obj.toString();
				String str = strall.substring(3, 7);
//				Toast.makeText(getApplicationContext(), "handle"+str, Toast.LENGTH_SHORT)
//				.show();	
				if (str.equals("&000")) { // 登陆成功
					setAll(true);
					LoginOK(strall);
				}
				else if (str.equals("&001")) { 
					setAll(true);
					Toast.makeText(getApplicationContext(), "指令错误", Toast.LENGTH_SHORT)
					.show();		
				}
				else if (str.equals("&002")) { 
					setAll(true);
					Toast.makeText(getApplicationContext(), "连接数据库失败", Toast.LENGTH_SHORT)
					.show();		
				}
				else if (str.equals("&003")) { 
					setAll(true);
					Toast.makeText(getApplicationContext(), "获取或更改数据失败", Toast.LENGTH_SHORT)
					.show();		
				}
				else if (str.equals("&004")) { 
					setAll(true);
					Toast.makeText(getApplicationContext(), "客户端提供数据不足或不符合规范", Toast.LENGTH_SHORT)
					.show();		
				}
				else if (str.equals("&005")) { 
					setAll(true);
					Toast.makeText(getApplicationContext(), "用户名或密码错误", Toast.LENGTH_SHORT)
					.show();		
				}
			};
		};
	}


	public void LoginOK(String str) {
		curuser.Initial(str);
//		Toast.makeText(getApplicationContext(), "登录成功", Toast.LENGTH_SHORT)
//		.show();
//		this.onDestroy();
		Data_all.User_ID = curuser.GetUsrID();
		Intent intent = new Intent(MainActivity.this,
				Mine_MissionActivity.class);
		startActivity(intent);
	}

	public void init() {
		edit_username = (EditText) findViewById(R.id.username);
		edit_key = (EditText) findViewById(R.id.key);
		button_login = (Button) findViewById(R.id.button_login);
		button_regist = (Button) findViewById(R.id.button_regist);
//		setAll(false);
		connect();
	}


	public void connect() { // 连接服务器，启动Service
		Intent intent = new Intent(MainActivity.this, NetService.class);
		startService(intent);
	}

	//****************两个与netservice沟通的接口，收发数据，，字符串格式	
	public void sent(String bs) { // 通过Service发送数据
		Intent intent = new Intent();// 创建Intent对象
		intent.setAction("android.intent.action.cmd");
		intent.putExtra("value", bs);
		sendBroadcast(intent);// 发送广播
	}

	private class MyReceiver extends BroadcastReceiver { // 接收service传来的信息
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			if (intent.getAction().equals("android.intent.action.MainActivity")) {
				Bundle bundle = intent.getExtras();
				String str = bundle.getString("str");
				Message msg = new Message();
				msg.obj = str;
				mHandler.sendMessage(msg);
			}
		}
	}

	public void setAll(boolean state){
		edit_username.setEnabled(state);
		edit_key.setEnabled(state);
		button_login.setEnabled(state);
		button_regist.setEnabled(state);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
}
