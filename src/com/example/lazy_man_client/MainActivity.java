//登录界面（初始界面）
package com.example.lazy_man_client;

import com.example.mysocketdemo.R;

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
	private Button button_login,button_regist;
	private EditText edit_username,edit_key;  //用户输入控件
	private Handler mHandler;
	private int stateflag = 0;  // 0:wait   1:OK   -1:fail
	MyReceiver receiver;
//	boolean stop = true;
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();       //控件初始化
		receiver = new MyReceiver(); // 注册广播
		IntentFilter filter = new IntentFilter();
		filter.addAction("android.intent.action.MainActivity");
		registerReceiver(receiver, filter);
		/*
		 * 登录,若成功则进入任务页面 发送用户名和密码 
		 * 示例：用户“李四”登录，密码20482048；发送字符     &01&李四&20482048
		 * 需要服务器反馈：用户是否存在/密码是否正确
		 */
        button_login.setOnClickListener(new View.OnClickListener() {            
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                //当点击按钮时,会获取编辑框中的数据,然后提交给线程
            	byte[]  msgBuffer = null;
            	String sendstr = "&01&"+edit_username.getText().toString()+"&"+edit_key.getText();
            	msgBuffer = sendstr.getBytes();
                sent(msgBuffer);
//				waiting();
//                stateflag=1;
				if (1 == stateflag) {
					Toast.makeText(getApplicationContext(), "登录成功",
							Toast.LENGTH_SHORT).show();
					Intent intent = new Intent(MainActivity.this,
							Mine_MissionActivity.class);
					startActivity(intent);
				}
            }
        });
//进入注册页面
        button_regist.setOnClickListener(new View.OnClickListener() {            
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
    			Intent intent =new Intent(MainActivity.this,RegistActivity.class);
    			startActivity(intent);	
            }
        });
        //在连接和发送数据之后，接下来就是处理了,发送的数据会通过message的方式传递到消息队列,再由handl进行获取
        mHandler = new Handler(){
            public void handleMessage(android.os.Message msg) {
            	String str=msg.obj.toString();
                Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT).show();
            };
        };
    }
	
	public void init() {
		edit_username = (EditText) findViewById(R.id.username);		
		edit_key = (EditText) findViewById(R.id.key);
		button_login = (Button) findViewById(R.id.button_login);
		button_regist = (Button) findViewById(R.id.button_regist);
		connect();
		
	}

	public void connect() {   //连接服务器，启动Service
		Intent intent = new Intent(MainActivity.this,NetService.class); 
		startService(intent); 
	}
	
	public void sent(byte[] bs){  //通过Service发送数据
		Intent intent = new Intent();//创建Intent对象  
		intent.setAction("android.intent.action.cmd");  
		intent.putExtra("value", bs);  
		sendBroadcast(intent);//发送广播      
	} 
	
	private void waiting(){
		stateflag = 0;
		while(0==stateflag){}		
	}
	
	private class MyReceiver extends BroadcastReceiver { // 接收service传来的信息
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			if (intent.getAction().equals("android.intent.action.MainActivity")) {
				Bundle bundle = intent.getExtras();
				String str = bundle.getString("str");
				if (str.equals("00")) {
					stateflag = 1   ;
				}
//				else if (cmd == CMD_RECEIVE_DATA) {
//					String str = bundle.getString("str");
//					Message msg = new Message();
//					msg.obj = str;
//					mHandler.sendMessage(msg);
//				}
				
			}
		}
	}
	

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
}
