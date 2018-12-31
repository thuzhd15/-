//��¼���棨��ʼ���棩
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
	private EditText edit_username, edit_key; // �û�����ؼ�
	private Handler mHandler;
	// private int stateflag = 0; // 0:wait 1:OK -1:fail
	MyReceiver receiver;
	Usr curuser;

	// boolean stop = true;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		init(); // �ؼ���ʼ��
		curuser = new Usr();
		receiver = new MyReceiver(); // ע��㲥
		IntentFilter filter = new IntentFilter();
		filter.addAction("android.intent.action.MainActivity"); //�˴��ĳ��Լ�activity������
		registerReceiver(receiver, filter);
		/*
		 * ��¼,���ɹ����������ҳ�� �����û��������� ʾ�����û������ġ���¼������20482048�������ַ� &01&����&20482048
		 * ��Ҫ�������������û��Ƿ����/�����Ƿ���ȷ
		 */
		button_login.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// �������ťʱ,���ȡ�༭���е�����,Ȼ���ύ���߳�
				String sendstr = "&01&00" + edit_username.getText().toString()
						+ "&01" + edit_key.getText();
				setAll(false);
				sent(sendstr);
			}
		});
		// ����ע��ҳ��
		button_regist.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MainActivity.this,
						RegistActivity.class);
				startActivity(intent);
			}
		});
		// �����Ӻͷ�������֮�󣬽��������Ǵ�����,���͵����ݻ�ͨ��message�ķ�ʽ���ݵ���Ϣ����,����handl���л�ȡ
		mHandler = new Handler() {
			public void handleMessage(android.os.Message msg) {
				String strall = msg.obj.toString();
				String str = strall.substring(3, 7);
//				Toast.makeText(getApplicationContext(), "handle"+str, Toast.LENGTH_SHORT)
//				.show();	
				if (str.equals("&000")) { // ��½�ɹ�
					setAll(true);
					LoginOK(strall);
				}
				else if (str.equals("&001")) { 
					setAll(true);
					Toast.makeText(getApplicationContext(), "ָ�����", Toast.LENGTH_SHORT)
					.show();		
				}
				else if (str.equals("&002")) { 
					setAll(true);
					Toast.makeText(getApplicationContext(), "�������ݿ�ʧ��", Toast.LENGTH_SHORT)
					.show();		
				}
				else if (str.equals("&003")) { 
					setAll(true);
					Toast.makeText(getApplicationContext(), "��ȡ���������ʧ��", Toast.LENGTH_SHORT)
					.show();		
				}
				else if (str.equals("&004")) { 
					setAll(true);
					Toast.makeText(getApplicationContext(), "�ͻ����ṩ���ݲ���򲻷��Ϲ淶", Toast.LENGTH_SHORT)
					.show();		
				}
				else if (str.equals("&005")) { 
					setAll(true);
					Toast.makeText(getApplicationContext(), "�û������������", Toast.LENGTH_SHORT)
					.show();		
				}
			};
		};
	}


	public void LoginOK(String str) {
		curuser.Initial(str);
//		Toast.makeText(getApplicationContext(), "��¼�ɹ�", Toast.LENGTH_SHORT)
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


	public void connect() { // ���ӷ�����������Service
		Intent intent = new Intent(MainActivity.this, NetService.class);
		startService(intent);
	}

	//****************������netservice��ͨ�Ľӿڣ��շ����ݣ����ַ�����ʽ	
	public void sent(String bs) { // ͨ��Service��������
		Intent intent = new Intent();// ����Intent����
		intent.setAction("android.intent.action.cmd");
		intent.putExtra("value", bs);
		sendBroadcast(intent);// ���͹㲥
	}

	private class MyReceiver extends BroadcastReceiver { // ����service��������Ϣ
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
