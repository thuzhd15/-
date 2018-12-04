//��¼���棨��ʼ���棩
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
	private EditText edit_username,edit_key;  //�û�����ؼ�
	private Handler mHandler;
	private int stateflag = 0;  // 0:wait   1:OK   -1:fail
	MyReceiver receiver;
//	boolean stop = true;
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();       //�ؼ���ʼ��
		receiver = new MyReceiver(); // ע��㲥
		IntentFilter filter = new IntentFilter();
		filter.addAction("android.intent.action.MainActivity");
		registerReceiver(receiver, filter);
		/*
		 * ��¼,���ɹ����������ҳ�� �����û��������� 
		 * ʾ�����û������ġ���¼������20482048�������ַ�     &01&����&20482048
		 * ��Ҫ�������������û��Ƿ����/�����Ƿ���ȷ
		 */
        button_login.setOnClickListener(new View.OnClickListener() {            
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                //�������ťʱ,���ȡ�༭���е�����,Ȼ���ύ���߳�
            	byte[]  msgBuffer = null;
            	String sendstr = "&01&"+edit_username.getText().toString()+"&"+edit_key.getText();
            	msgBuffer = sendstr.getBytes();
                sent(msgBuffer);
//				waiting();
//                stateflag=1;
				if (1 == stateflag) {
					Toast.makeText(getApplicationContext(), "��¼�ɹ�",
							Toast.LENGTH_SHORT).show();
					Intent intent = new Intent(MainActivity.this,
							Mine_MissionActivity.class);
					startActivity(intent);
				}
            }
        });
//����ע��ҳ��
        button_regist.setOnClickListener(new View.OnClickListener() {            
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
    			Intent intent =new Intent(MainActivity.this,RegistActivity.class);
    			startActivity(intent);	
            }
        });
        //�����Ӻͷ�������֮�󣬽��������Ǵ�����,���͵����ݻ�ͨ��message�ķ�ʽ���ݵ���Ϣ����,����handl���л�ȡ
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

	public void connect() {   //���ӷ�����������Service
		Intent intent = new Intent(MainActivity.this,NetService.class); 
		startService(intent); 
	}
	
	public void sent(byte[] bs){  //ͨ��Service��������
		Intent intent = new Intent();//����Intent����  
		intent.setAction("android.intent.action.cmd");  
		intent.putExtra("value", bs);  
		sendBroadcast(intent);//���͹㲥      
	} 
	
	private void waiting(){
		stateflag = 0;
		while(0==stateflag){}		
	}
	
	private class MyReceiver extends BroadcastReceiver { // ����service��������Ϣ
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
