package com.example.mysocketdemo;
import java.io.IOException;  
import java.io.InputStream;  
import java.io.OutputStream;    
import java.net.Socket;
import java.net.UnknownHostException;

import android.app.Service;  
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;  
import android.content.BroadcastReceiver;  
import android.content.Context;   
import android.content.IntentFilter;   

public class NetService extends Service{

    static final int CMD_STOP_SERVICE = 0x01;  
    static final int CMD_SEND_DATA = 0x02;  
    static final int CMD_RECEIVE_DATA = 0x03;  
    static final int CMD_SHOW_TOAST =0x04; //预定义的命令
	CommandReceiver cmdReceiver;//继承自BroadcastReceiver对象，用于得到Activity发送过来的命令  
	String PC_IP; //服务器地址
	int port_number; //端口号
	private OutputStream output;   //发送（输出）流
	private Socket clientSocket;   //套接字
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
  //前台Activity调用startService时，该方法自动执行
    public int onStartCommand(Intent intent, int flags, int startId) {  
        // TODO Auto-generated method stub  
        cmdReceiver = new CommandReceiver();  
        IntentFilter filter=new IntentFilter();  
        filter.addAction("android.intent.action.cmd");  
//        filter.addCategory("android.intent.category.LAUNCHER");
        registerReceiver(cmdReceiver,filter); 
    	Bundle extras = intent.getExtras();
//        IntentFilter filter2 = new IntentFilter();//创建IntentFilter对象  
    	PC_IP = extras.getString("PC_IP");
//        filter2.addAction("android.intent.action.coomd");  //注册一个广播，用于接收Activity传送过来的命令，控制Service的行为，如：发送数据，停止服务等   
//        registerReceiver(cmdReceiver, filter2);        //注册Broadcast Receiver 
    	String he=extras.getString("Portnumber");
    	port_number = Integer.parseInt(he);    	
    	connect();
    	return super.onStartCommand(intent, flags, startId); 
    }
	public void connect() {   //连接服务器	
		Thread th=new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					clientSocket=new Socket(PC_IP, port_number);
					if(clientSocket.isConnected()){
						showToast("连接服务器成功！",CMD_SHOW_TOAST);	
					}else{
						showToast("连接服务器失败！",CMD_SHOW_TOAST);	
					}
					output = clientSocket.getOutputStream();
					/* 客户端接收服务器数据  */
					InputStream input = clientSocket.getInputStream();
					byte[] buf=new byte[1024];
					while(true) {
						try {
							input.read(buf);	
							//			    			System.out.println("Receive:"+new String(buf));
							showToast("Receive:" + new String(buf) + "\r\n",CMD_RECEIVE_DATA);
							//			    			socket.close();
						} catch (IOException e) {
							break;
						}
					}	
				} catch (IOException e) {
					showToast("Socket失效",CMD_SHOW_TOAST);	
					e.printStackTrace();
				}
			}
		});
		th.start();
	}
	
	public void sent(final byte[] hehe) { //向网络发送数据
		new Thread() {
			public void run() {
				try {
					output.write(hehe);
				} catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					showToast("Fail!!!",CMD_SHOW_TOAST);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					showToast("Fail!!!",CMD_SHOW_TOAST);
				}			}
		}.start();
	}
	
    //接收Activity传送过来的命令  
   private class CommandReceiver extends BroadcastReceiver{  
       @Override  
       public void onReceive(Context context, Intent intent) {  
    	   if(intent.getAction().equals("android.intent.action.cmd")){  
    		   Bundle bundle = intent.getExtras();
    		   int cmd = bundle.getInt("cmd");                              
    		   if(cmd == CMD_STOP_SERVICE){  
    			   stopSelf();//停止服务
    		   }                     
    		   else if(cmd == CMD_SEND_DATA)  
    		   {   
    			   byte[] data=intent.getByteArrayExtra("value");
    			   sent(data);
    		   }                                           
    	   }     
       }                          
   }  

   public void showToast(String str,int cmd){//显示提示信息  
       Intent intent = new Intent();  
       intent.putExtra("cmd", cmd);  
       intent.putExtra("str", str);  
       intent.setAction("android.intent.action.lxx");  
       sendBroadcast(intent);    
   } 
}

