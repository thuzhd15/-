package com.example.lazy_man_client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.widget.Toast;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;

public class NetService extends Service {
	CommandReceiver cmdReceiver;// �̳���BroadcastReceiver�������ڵõ�Activity���͹���������
	//	String PC_IP = "101.5.243.196"; // ��������ַ
	String PC_IP = "101.5.170.45"; // ��������ַ
	int port_number = 20000; // �˿ں�
	private BufferedWriter output; // ���ͣ��������
	private Socket clientSocket; // �׽���
	private Handler handler;
	boolean stop = true;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
	}

	// ǰ̨Activity����startServiceʱ���÷����Զ�ִ��
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		cmdReceiver = new CommandReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction("android.intent.action.cmd");
		// filter.addCategory("android.intent.category.LAUNCHER");
		registerReceiver(cmdReceiver, filter);
		connect();
		return super.onStartCommand(intent, flags, startId);
	}

	/*
	 * ���ӷ��������󣬽����ӣ����漰�û��������У�顣 ����Ҫ����������ֵ��
	 */
	public void connect() {
		Thread th = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					clientSocket = new Socket(PC_IP, port_number);
					if (clientSocket.isConnected()) {
						//						DatatoActivity("000","MainActivity");
						//						showToast("���ӷ������ɹ���");
					} else {
						showToast("���ӷ�����ʧ�ܣ����Ժ�����^-^");
					}
					output = new 
							BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream(), "UTF-8"));
					/* �ͻ��˽��շ���������  */
					//					InputStream input = clientSocket.getInputStream();
					BufferedReader input = new 
							BufferedReader(new InputStreamReader(clientSocket.getInputStream(), "UTF-8"));
					//					byte[] buf = new byte[1024];
					while (true) {
						try {
							String buf = "";
							buf = input.readLine();	
							//			    			System.out.println("Receive:"+new String(buf));
							showToast("Receive:" + buf + "\r\n");
							//			    			socket.close();
							DatatoActivity(buf);
							// socket.close();
						} catch (IOException e) {
							break;
						}
					}
				} catch (IOException e) {
					showToast("�������ʧȥ���ӣ�");
					e.printStackTrace();
				}
			}
		});
		th.start();
	}

	public void sent(final String hehe) { // �����緢������
		new Thread() {
			public void run() {
				try {
					output.write(hehe+"\n");
					output.flush();
				} catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					showToast("Fail!!!");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					showToast("Fail!!!");
				}
			}
		}.start();
	}

	// ����Activity���͹���������
	private class CommandReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals("android.intent.action.cmd")) {
				Bundle bundle = intent.getExtras();
				String str = bundle.getString("value");
				showToast("sent"+new String(str));
				sent(str);
			}
		}
	}
	//��activity�������ݣ�str�����ݣ�actname��Ŀ��activity������
	public void DatatoActivity(String str){
		String act = str.substring(0, 3);
		String actname = "";
		if(act.equals("&01")) actname = "MainActivity";
		else if(act.equals("&00")) actname = "RegistActivity";
		else if(act.equals("&02")) actname = "InformationActivity";
		//			else if(act.equals("&00")) actname = "RegistActivity";
		else if(act.equals("&03")) actname = "Mine_MissionActivity";
		else if(act.equals("&04")) actname = "PasswordActivity";
		else if(act.equals("&05")) actname = "AddressActivity";	
		else if(act.equals("&50")) actname = "AReleaseTask";	
		else if(act.equals("&51")||act.equals("&52")) actname = "ATaskModify";
		else if(act.equals("&59")) showToast("�޸�����ɹ�");
		else if(act.equals("&53")) actname = "ReDetailActivity";
		else if(act.equals("&55")) actname = "Mine_MissionActivity";
		else if(act.equals("&57")) actname = "AdDetailActivity";
		else if(act.equals("&58")) actname = "ReDetailActivity";

//		if(act.equals("&56"))actname = "AdDetailActivity";
		
		Intent intent = new Intent();
		intent.putExtra("str", str);
		intent.setAction("android.intent.action."+actname);
		sendBroadcast(intent);	 
		if(act.equals("&56")){ 
			intent.setAction("android.intent.action."+"AdDetailActivity");
			sendBroadcast(intent);
//			intent.putExtra("str", str);
			intent.setAction("android.intent.action."+"Mine_MissionActivity");
			sendBroadcast(intent);
			intent.setAction("android.intent.action."+"ReDetailActivity");
			sendBroadcast(intent);
			intent.setAction("android.intent.action."+"AdDetailActivity");
			sendBroadcast(intent);
			intent.setAction("android.intent.action."+"ATaskModify");
			sendBroadcast(intent);
		}
		if(act.equals("&54")){ 
//			intent.putExtra("str", str);
			intent.setAction("android.intent.action."+"Mine_MissionActivity");
			sendBroadcast(intent);
			intent.setAction("android.intent.action."+"ReleaseTaskActivity");
			sendBroadcast(intent);
			intent.setAction("android.intent.action."+"AdoptTaskActivity");
			sendBroadcast(intent);
		}
	}

	// �ڵ�ǰҳ������ʾ��ʾ��Ϣ
	public void showToast(final String str) {
		super.onCreate();
		handler = new Handler(Looper.getMainLooper());
		handler.post(new Runnable() {
			public void run() {
				Toast.makeText(getApplicationContext(), str,
						Toast.LENGTH_LONG).show();
			}
		});
	}
}
