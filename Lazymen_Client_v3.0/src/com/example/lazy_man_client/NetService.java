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
	CommandReceiver cmdReceiver;// 继承自BroadcastReceiver对象，用于得到Activity发送过来的命令
	// String PC_IP = "localhost"; // 服务器地址
	String PC_IP = "101.5.90.82"; // 服务器地址
	int port_number = 20000; // 端口号
	private BufferedReader input; // 输入流
	private BufferedWriter output; // 发送（输出）流
	private Socket clientSocket; // 套接字
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

	// 前台Activity调用startService时，该方法自动执行
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
	 * 连接服务器请求，仅连接，不涉及用户名密码的校验。 不需要服务器返回值。
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
						clientSocket.setKeepAlive(true); // 为了使用心跳功能
					} catch (SocketException e) {
						e.printStackTrace();
					}

					if (clientSocket.isConnected()) {
						// DatatoActivity("000","MainActivity");
						// showToast("连接服务器成功！");
					} else {
						showToast("连接服务器失败，请稍后再试^-^");
					}
					output = new BufferedWriter(new OutputStreamWriter(
							clientSocket.getOutputStream(), "UTF-8"));
					/* 客户端接收服务器数据 */
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
								break; // 如果input为null，说明已经重开线程
							}
						} catch (IOException e) {							
							e.printStackTrace();
							reconnect(); // 断开重连
							break;
						}
					}
				} catch (IOException e) {
					showToast("与服务器失去连接！");
					e.printStackTrace();
				}
			}
		});
		th.start();
	}

	public void reconnect() {
		try {
			showToast("尝试与服务器重新连接中……");
			output.close(); // 关闭输出流
			input.close(); // 关闭输入流
			clientSocket.close(); // 关闭连接
			output = null;
			input = null;
			clientSocket = null;
			connect(); // 重连
		} catch (IOException e) {
			showToast("与服务器失去连接！");
			e.printStackTrace();
		}
	}
	
	public void heart_beat() { // 心跳线程（断线重连）
		Thread th_heart = new Thread(new Runnable() { // 发送心跳包的线程
					@Override
					public void run() {
						while (true) {
							try {
								clientSocket.sendUrgentData(0xFF);
							} catch (IOException e) { // 如果掉线
								try {
									showToast("尝试与服务器重新连接中……");
									output.close(); // 关闭输出流
									input.close(); // 关闭输入流
									clientSocket.close(); // 关闭连接
									connect(); // 重连
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

	public void sent(final String hehe) { // 向网络发送数据
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

	// 接收Activity传送过来的命令
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

	// 向activity传输数据，str是数据，actname是目标activity的名字
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
			showToast("修改任务成功");
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

	// 在当前页面上显示提示信息
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
