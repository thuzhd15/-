package com.example.lazy_man_client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.*;
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
	// String PC_IP = "localhost"; // ��������ַ
	String PC_IP = "101.5.90.82"; // ��������ַ
	int port_number = 20000; // �˿ں�
	private BufferedReader input; // ������
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
		//heart_beat();
		return super.onStartCommand(intent, flags, startId);
	}

	/*
	 * ���ӷ��������󣬽����ӣ����漰�û��������У�顣 ����Ҫ����������ֵ��
	 */
	public void connect() {
		Thread th = new Thread(new Runnable() {
			@Override
			public void run() {
				clientSocket = null;
				output = null;
				input = null;
				try {
					clientSocket = new Socket(PC_IP, port_number);
					try {
						clientSocket.setKeepAlive(true); // Ϊ��ʹ����������
					} catch (SocketException e) {
						e.printStackTrace();
					}

					if (clientSocket.isConnected()) {
						// DatatoActivity("000","MainActivity");
						// showToast("���ӷ������ɹ���");
					} else {
						showToast("���ӷ�����ʧ�ܣ����Ժ�����^-^");
					}
					output = new BufferedWriter(new OutputStreamWriter(
							clientSocket.getOutputStream(), "UTF-8"));
					/* �ͻ��˽��շ��������� */
					BufferedReader input = new BufferedReader( new InputStreamReader(
									clientSocket.getInputStream(), "UTF-8"));
					// byte[] buf = new byte[1024];
					while (true) {
						try {
							String buf = "";
							if (input != null) {
								buf = input.readLine();
								// System.out.println("Receive:"+new
								// String(buf));
								showToast("Receive:" + buf + "\r\n");
								DatatoActivity(buf);
							} else {
								break; // ���inputΪnull��˵���Ѿ��ؿ��߳�
							}
						} catch (IOException e) {							
							e.printStackTrace();
							reconnect(); // �Ͽ�����
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

	public void reconnect() {
		try {
			showToast("��������������������С���");
			output.close(); // �ر������
			input.close(); // �ر�������
			clientSocket.close(); // �ر�����
			output = null;
			input = null;
			clientSocket = null;
			connect(); // ����
		} catch (IOException e) {
			showToast("�������ʧȥ���ӣ�");
			e.printStackTrace();
		}
	}
	
	public void heart_beat() { // �����̣߳�����������
		Thread th_heart = new Thread(new Runnable() { // �������������߳�
					@Override
					public void run() {
						while (true) {
							try {
								clientSocket.sendUrgentData(0xFF);
							} catch (IOException e) { // �������
								try {
									showToast("��������������������С���");
									output.close(); // �ر������
									input.close(); // �ر�������
									clientSocket.close(); // �ر�����
									connect(); // ����
								} catch (IOException e2) {
									e2.printStackTrace();
								}
								e.printStackTrace();
							}
							try {
								Thread.sleep(5 * 1000);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					}
				});
		th_heart.start();
	}

	public void sent(final String hehe) { // �����緢������
		new Thread() {
			public void run() {
				try {
					if (output != null) {
						output.write(hehe + "\n");
						output.flush();
					} else {
						reconnect();
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					reconnect();
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
				// showToast("sent"+new String(str));
				sent(str);
			}
		}
	}

	// ��activity�������ݣ�str�����ݣ�actname��Ŀ��activity������
	public void DatatoActivity(String str) {
		String act = str.substring(0, 3);
		String actname = "";
		if (act.equals("&01"))
			actname = "MainActivity";
		else if (act.equals("&00"))
			actname = "RegistActivity";
		else if (act.equals("&02"))
			actname = "InformationActivity";
		// else if(act.equals("&00")) actname = "RegistActivity";
		else if (act.equals("&04"))
			actname = "PasswordActivity";
		else if (act.equals("&05"))
			actname = "AddressActivity";
		else if (act.equals("&50"))
			actname = "AReleaseTask";
		else if (act.equals("&51") || act.equals("&52"))
			actname = "ATaskModify";
		else if (act.equals("&59"))
			showToast("�޸�����ɹ�");
		else if (act.equals("&53"))
			actname = "ReDetailActivity";
		else if (act.equals("&55"))
			actname = "Mine_MissionActivity";
		else if (act.equals("&57"))
			actname = "AdDetailActivity";
		else if (act.equals("&58"))
			actname = "ReDetailActivity";

		// if(act.equals("&56"))actname = "AdDetailActivity";

		Intent intent = new Intent();
		intent.putExtra("str", str);
		intent.setAction("android.intent.action." + actname);
		sendBroadcast(intent);
		if (act.equals("&03")) {
			intent.setAction("android.intent.action." + "Mine_MissionActivity");
			sendBroadcast(intent);
			intent.setAction("android.intent.action." + "AReleaseTask");
			sendBroadcast(intent);
			intent.setAction("android.intent.action." + "ATaskModify");
			sendBroadcast(intent);
		}
		if (act.equals("&56")) {
			intent.setAction("android.intent.action." + "AdDetailActivity");
			sendBroadcast(intent);
			// intent.putExtra("str", str);
			intent.setAction("android.intent.action." + "Mine_MissionActivity");
			sendBroadcast(intent);
			intent.setAction("android.intent.action." + "ReDetailActivity");
			sendBroadcast(intent);
			intent.setAction("android.intent.action." + "AdDetailActivity");
			sendBroadcast(intent);
			intent.setAction("android.intent.action." + "ATaskModify");
			sendBroadcast(intent);
		}
		if (act.equals("&54")) {
			// intent.putExtra("str", str);
			intent.setAction("android.intent.action." + "Mine_MissionActivity");
			sendBroadcast(intent);
			intent.setAction("android.intent.action." + "ReleaseTaskActivity");
			sendBroadcast(intent);
			intent.setAction("android.intent.action." + "AdoptTaskActivity");
			sendBroadcast(intent);
		}
	}

	// �ڵ�ǰҳ������ʾ��ʾ��Ϣ
	public void showToast(final String str) {
		super.onCreate();
		handler = new Handler(Looper.getMainLooper());
		handler.post(new Runnable() {
			public void run() {
				Toast.makeText(getApplicationContext(), str, Toast.LENGTH_LONG)
						.show();
			}
		});
	}
}
