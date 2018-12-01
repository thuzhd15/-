//登录界面（初始界面）
package com.example.mysocketdemo;

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
    static final int CMD_STOP_SERVICE = 0x01;  
    static final int CMD_SEND_DATA = 0x02;  
    static final int CMD_RECEIVE_DATA = 0x03;  
    static final int CMD_SHOW_TOAST =0x04; //预定义的命令
	private TextView tv;
	private Button button_send, button_connect,button_login,button_regist;
	private EditText edit_send,edit_IP;  //用户输入控件
	private Handler mHandler;
//	boolean stop = true;
	String PCservice_IP="0000";  //服务器IP
	EditText edit_port;
	int port_number=0;
	MyReceiver receiver; 

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();       //控件初始化
        receiver = new MyReceiver();   //注册广播
        IntentFilter filter=new IntentFilter();  
        filter.addAction("android.intent.action.lxx");
        registerReceiver(receiver,filter); 
        button_connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	PCservice_IP=edit_IP.getText().toString();
            	connect();
                Toast.makeText(getApplicationContext(), "connect ok", Toast.LENGTH_SHORT).show();
//                 
                //更新UI，大部分的数据工作已经交给了mythread对象
                button_send.setEnabled(true);
            	
//                btnconn.setEnabled(false);
//                stop = false;
            }
        });
        //sent Message
        button_send.setOnClickListener(new View.OnClickListener() {            
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                //当点击按钮时,会获取编辑框中的数据,然后提交给线程
            	byte[]  msgBuffer = null;
            	msgBuffer = edit_send.getText().toString().getBytes();
                sent(msgBuffer);
                Toast.makeText(getApplicationContext(), "发送成功", Toast.LENGTH_SHORT).show();
//                ed_message.setText("");
//                sendNotification();
            }
        });
        //登录,若成功则进入任务页面
        button_login.setOnClickListener(new View.OnClickListener() {            
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                //当点击按钮时,会获取编辑框中的数据,然后提交给线程
            	byte[]  msgBuffer = null;
            	msgBuffer = edit_send.getText().toString().getBytes();
                sent(msgBuffer);
                Toast.makeText(getApplicationContext(), "登录", Toast.LENGTH_SHORT).show();
    			Intent intent =new Intent(MainActivity.this,SearchActivity.class);
    			startActivity(intent);	
            }
        });
      //注册
        button_regist.setOnClickListener(new View.OnClickListener() {            
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                //当点击按钮时,会获取编辑框中的数据,然后提交给线程
    			Intent intent =new Intent(MainActivity.this,RegistActivity.class);
    			startActivity(intent);	
            }
        });
        //在连接和发送数据之后，接下来就是处理了,发送的数据会通过message的方式传递到消息队列,再由handl进行获取
        mHandler = new Handler(){
            public void handleMessage(android.os.Message msg) {
            	String str=msg.obj.toString();
                tv.append(str);
                Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT).show();
            };
        };
    }
	

	public void connect() {   //连接服务器，启动Service
		Intent intent = new Intent(MainActivity.this,NetService.class); 
		intent.putExtra("PC_IP", PCservice_IP);
		intent.putExtra("Portnumber", edit_port.getText().toString());
		startService(intent); 
	}
	
	public void sent(byte[] bs){  //通过Service发送数据
		Intent intent = new Intent();//创建Intent对象  
		intent.setAction("android.intent.action.cmd");  
		intent.putExtra("cmd", CMD_SEND_DATA);  
		intent.putExtra("value", bs);  
		sendBroadcast(intent);//发送广播      
	}  
	
	public void init() {
		tv = (TextView) findViewById(R.id.tv);
		button_send = (Button) findViewById(R.id.btn_sent);
		edit_send = (EditText) findViewById(R.id.et_message);		
		edit_IP = (EditText) findViewById(R.id.IP);
		edit_port = (EditText) findViewById(R.id.my_number);
		edit_port.setText("20000");
		button_connect = (Button) findViewById(R.id.btn_conn);
		button_connect.setEnabled(true);
		button_send.setEnabled(false);
		button_login = (Button) findViewById(R.id.button_login);
		button_regist = (Button) findViewById(R.id.button_regist);
	}

	public class MyReceiver extends BroadcastReceiver { //接收service传来的信息
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			if (intent.getAction().equals("android.intent.action.lxx")) {
				Bundle bundle = intent.getExtras();
				int cmd = bundle.getInt("cmd");

				if (cmd == CMD_SHOW_TOAST) {
					String str = bundle.getString("str");
					showToast(str);
				}
				else if (cmd == CMD_RECEIVE_DATA) {
					String str = bundle.getString("str");
					Message msg = new Message();
					msg.obj = str;
					mHandler.sendMessage(msg);					
				}
			}
		}
	}
	
	public void showToast(String str){//显示提示信息  
		Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT).show();      
	}  
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
}
