package com.example.zhouqian.myapplication;

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
	CommandReceiver cmdReceiver;// 继承自BroadcastReceiver对象，用于得到Activity发送过来的命令
	//	String PC_IP = "101.5.243.196"; // 服务器地址
	String PC_IP = "101.5.132.119"; // 服务器地址
	int port_number = 10086; // 端口号
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
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		cmdReceiver = new CommandReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction("android.intent.action.cmd");
		// filter.addCategory("android.intent.category.LAUNCHER");
		registerReceiver(cmdReceiver, filter);
		connect();
		System.out.println("netservice started");
		return super.onStartCommand(intent, flags, startId);
	}

	/*
	 * 连接服务器请求，仅连接，不涉及用户名密码的校验。 不需要服务器返回值。
	 */
	public void connect() {
		Thread th = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					clientSocket = new Socket(PC_IP, port_number);
					if (clientSocket.isConnected()) {
						DatatoActivity("str","AReleaseTask");
						showToast("连接服务器成功！");
						System.out.println("连接服务器成功");
					} else {
						showToast("连接服务器失败，请稍后再试^-^");
					}
					output = new
							BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream(), "UTF-8"));
					/* 客户端接收服务器数据  */
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
							DatatoActivity("str", "AReleaseTask");
							// socket.close();
						} catch (IOException e) {
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

	public void sent(final String hehe) { // 向网络发送数据
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

	// 接收Activity传送过来的命令
	private class CommandReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals("android.intent.action.cmd")) {
				Bundle bundle = intent.getExtras();
				String str = bundle.getString("value");
				showToast("sent"+new String(str));
				System.out.println("message received");
				sent(str);
			}
		}
	}
	//向activity传输数据，str是数据，actname是目标activity的名字
	public void DatatoActivity(String str,String actname){
		Intent intent = new Intent();
		intent.putExtra("str", str);
		intent.setAction("android.intent.action."+actname);
		sendBroadcast(intent);
	}

	// 在当前页面上显示提示信息
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
