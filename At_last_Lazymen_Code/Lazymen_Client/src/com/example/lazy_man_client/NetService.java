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
	String PC_IP = "183.173.51.249"; // 服务器地址
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
		cmdReceiver = new CommandReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction("android.intent.action.cmd");
		registerReceiver(cmdReceiver, filter);
		connect();
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
						clientSocket.setKeepAlive(true);
					} catch (SocketException e) {
						e.printStackTrace();
					}

					if (clientSocket.isConnected()) {
						DatatoActivity("&01&COK"); // 给登录页面发送连接成功的消息
						output = new BufferedWriter(new OutputStreamWriter(
								clientSocket.getOutputStream(), "UTF-8"));
						/* 客户端接收服务器数据 */
						input = new BufferedReader(new InputStreamReader(
								clientSocket.getInputStream(), "UTF-8"));
						while (true) {
							try {
								String buf = "";
								if (input != null) {
									buf = input.readLine();
									DatatoActivity(buf);
								} else {
									break; // 如果input为null，关闭当前线程
								}
							} catch (IOException e) {
								break;
							}
						}
					} else {
						showToast("网络异常，请稍后再试^_^");
						DatatoActivity("&01&NOK"); // 给登录页面发送连接不成功的消息
					}

				} catch (IOException e) {
					showToast("网络异常，请稍后再试^_^");
					DatatoActivity("&01&NOK"); // 给登录页面发送连接不成功的消息
				}
			}
		});
		th.start();
	}

	public void reconnect() {
		try {
			showToast("尝试重新连接中……");
			if( output != null ) {
				output.close(); // 关闭输出流
				output = null;
			}
			if( input != null ) {
				input.close(); // 关闭输入流
				input = null;
			}
			if( clientSocket != null ) {
				clientSocket.close(); // 关闭连接
				clientSocket = null;
			}			
			connect(); // 重连
			showToast("网络恢复，请重新提交信息^_^");
			
		} catch (IOException e) {
			showToast("网络异常，请稍后再试^_^");
			DatatoActivity("&01&NOK"); // 给登录页面发送连接不成功的消息
		}
	}

	public void sent(final String hehe) { // 向网络发送数据
		new Thread() {
			public void run() {
				try {
					if (output != null) {
						output.write(hehe + "\n");
						output.flush();
					} else {
						reconnect(); //发送数据时发现断线则重新连接
					}
				} catch (IOException e) {
					reconnect(); //发送数据时发现断线则重新连接
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
		else if (act.equals("&03") || act.equals("&54") || act.equals("&55") || act.equals("&56"))
			actname = "Mine_MissionActivity";
		else if (act.equals("&02") || act.equals("&06"))
			actname = "InformationActivity";
		else if (act.equals("&04") || act.equals("&07"))
			actname = "PasswordActivity";
		else if (act.equals("&05") || act.equals("&08"))
			actname = "AddressActivity";
		else if (act.equals("&09") || act.equals("&50"))
			actname = "AReleaseTask";
		else if (act.equals("&10") || act.equals("&51") || act.equals("&52") || act.equals("&62"))
			actname = "ATaskModify";
		else if (act.equals("&63"))
			actname = "ReleaseTaskActivity";
		else if (act.equals("&64"))
			actname = "AdoptTaskActivity";
		else if (act.equals("&53") || act.equals("&58") || act.equals("&60"))
			actname = "ReDetailActivity";
		else if (act.equals("&57") || act.equals("&61"))
			actname = "AdDetailActivity";

		Intent intent = new Intent();
		intent.putExtra("str", str);
		intent.setAction("android.intent.action." + actname);
		sendBroadcast(intent);
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
