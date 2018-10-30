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
    static final int CMD_SHOW_TOAST =0x04; //Ԥ���������
	CommandReceiver cmdReceiver;//�̳���BroadcastReceiver�������ڵõ�Activity���͹���������  
	String PC_IP; //��������ַ
	int port_number; //�˿ں�
	private OutputStream output;   //���ͣ��������
	private Socket clientSocket;   //�׽���
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
  //ǰ̨Activity����startServiceʱ���÷����Զ�ִ��
    public int onStartCommand(Intent intent, int flags, int startId) {  
        // TODO Auto-generated method stub  
        cmdReceiver = new CommandReceiver();  
        IntentFilter filter=new IntentFilter();  
        filter.addAction("android.intent.action.cmd");  
//        filter.addCategory("android.intent.category.LAUNCHER");
        registerReceiver(cmdReceiver,filter); 
    	Bundle extras = intent.getExtras();
//        IntentFilter filter2 = new IntentFilter();//����IntentFilter����  
    	PC_IP = extras.getString("PC_IP");
//        filter2.addAction("android.intent.action.coomd");  //ע��һ���㲥�����ڽ���Activity���͹������������Service����Ϊ���磺�������ݣ�ֹͣ�����   
//        registerReceiver(cmdReceiver, filter2);        //ע��Broadcast Receiver 
    	String he=extras.getString("Portnumber");
    	port_number = Integer.parseInt(he);    	
    	connect();
    	return super.onStartCommand(intent, flags, startId); 
    }
	public void connect() {   //���ӷ�����	
		Thread th=new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					clientSocket=new Socket(PC_IP, port_number);
					if(clientSocket.isConnected()){
						showToast("���ӷ������ɹ���",CMD_SHOW_TOAST);	
					}else{
						showToast("���ӷ�����ʧ�ܣ�",CMD_SHOW_TOAST);	
					}
					output = clientSocket.getOutputStream();
					/* �ͻ��˽��շ���������  */
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
					showToast("SocketʧЧ",CMD_SHOW_TOAST);	
					e.printStackTrace();
				}
			}
		});
		th.start();
	}
	
	public void sent(final byte[] hehe) { //�����緢������
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
	
    //����Activity���͹���������  
   private class CommandReceiver extends BroadcastReceiver{  
       @Override  
       public void onReceive(Context context, Intent intent) {  
    	   if(intent.getAction().equals("android.intent.action.cmd")){  
    		   Bundle bundle = intent.getExtras();
    		   int cmd = bundle.getInt("cmd");                              
    		   if(cmd == CMD_STOP_SERVICE){  
    			   stopSelf();//ֹͣ����
    		   }                     
    		   else if(cmd == CMD_SEND_DATA)  
    		   {   
    			   byte[] data=intent.getByteArrayExtra("value");
    			   sent(data);
    		   }                                           
    	   }     
       }                          
   }  

   public void showToast(String str,int cmd){//��ʾ��ʾ��Ϣ  
       Intent intent = new Intent();  
       intent.putExtra("cmd", cmd);  
       intent.putExtra("str", str);  
       intent.setAction("android.intent.action.lxx");  
       sendBroadcast(intent);    
   } 
}

