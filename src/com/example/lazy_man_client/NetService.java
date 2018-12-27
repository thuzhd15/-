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
	String PC_IP = "183.173.89.38"; // ��������ַ
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
							DatatoActivity(buf,"MainActivity");
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
	 public void DatatoActivity(String str,String actname){
	 Intent intent = new Intent();
	 intent.putExtra("str", str);
	 intent.setAction("android.intent.action."+actname);
	 sendBroadcast(intent);
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
