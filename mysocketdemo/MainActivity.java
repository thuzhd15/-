package com.example.mysocketdemo;

import android.app.Activity;
import android.app.PendingIntent;
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
import android.app.NotificationManager;
import android.support.v4.app.NotificationCompat;

public class MainActivity extends Activity {
    static final int CMD_STOP_SERVICE = 0x01;  
    static final int CMD_SEND_DATA = 0x02;  
    static final int CMD_RECEIVE_DATA = 0x03;  
    static final int CMD_SHOW_TOAST =0x04; //Ԥ���������
	private TextView tv;
	private Button button_send, button_connect;
	private EditText edit_send,edit_IP;  //�û�����ؼ�
	private Handler mHandler;
//	boolean stop = true;
	String PCservice_IP="0000";  //������IP
	EditText edit_port;
	int port_number=0;
	MyReceiver receiver; 

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();       //�ؼ���ʼ��
        receiver = new MyReceiver();   //ע��㲥
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
                //����UI���󲿷ֵ����ݹ����Ѿ�������mythread����
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
                //�������ťʱ,���ȡ�༭���е�����,Ȼ���ύ���߳�
            	byte[]  msgBuffer = null;
            	msgBuffer = edit_send.getText().toString().getBytes();
                sent(msgBuffer);
                Toast.makeText(getApplicationContext(), "���ͳɹ�", Toast.LENGTH_SHORT).show();
//                ed_message.setText("");
                sendNotification();
            }
        });
         
        //�����Ӻͷ�������֮�󣬽��������Ǵ�����,���͵����ݻ�ͨ��message�ķ�ʽ���ݵ���Ϣ����,����handl���л�ȡ
        mHandler = new Handler(){
            public void handleMessage(android.os.Message msg) {
            	String str=msg.obj.toString();
                tv.append(str);
                Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT).show();
            };
        };
    }
	
    private void sendNotification() {
 	   //��ȡNotificationManagerʵ��
 	   NotificationManager notifyManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
 	   //ʵ����NotificationCompat.Builde�������������
 	   NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
 	           //����Сͼ��
 	           .setSmallIcon(R.drawable.ic_launcher)
 	           //����֪ͨ����
 	           .setContentTitle("��򵥵�Notification")
 	           //����֪ͨ����
 	           .setContentText("ֻ��Сͼ�ꡢ���⡢����");
 	           //����֪ͨʱ�䣬Ĭ��Ϊϵͳ����֪ͨ��ʱ�䣬ͨ����������
 	           //.setWhen(System.currentTimeMillis());
 	   //ͨ��builder.build()��������Notification����,������֪ͨ,id=1
 	   notifyManager.notify(1, builder.build());
 	}


	public void connect() {   //���ӷ�����������Service
		Intent intent = new Intent(MainActivity.this,NetService.class); 
		intent.putExtra("PC_IP", PCservice_IP);
		intent.putExtra("Portnumber", edit_port.getText().toString());
		startService(intent); 
	}
	
	public void sent(byte[] bs){  //ͨ��Service��������
		Intent intent = new Intent();//����Intent����  
		intent.setAction("android.intent.action.cmd");  
		intent.putExtra("cmd", CMD_SEND_DATA);  
		intent.putExtra("value", bs);  
		sendBroadcast(intent);//���͹㲥      
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
	}

	public class MyReceiver extends BroadcastReceiver { //����service��������Ϣ
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
	
	public void showToast(String str){//��ʾ��ʾ��Ϣ  
		Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT).show();      
	}  
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
}
