package com.example.lazy_man_client;

import java.util.ArrayList;


import android.widget.Button;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.Button;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class AdDetailActivity extends Activity {
    private Button button1;
    
    private TextView User;
    private TextView DDL;
    private TextView Coins;
    private TextView Address;
    private TextView Adtime;
    private TextView infoPhoneOrderer;
    private TextView infoOrderNum;
    private TextView taskMessage;
    
	private Handler mHandler;//��Ϣ����
	private Handler handler;//toastר��
	MyReceiver receiver;
	Task task = new Task();
	
	private String TaskId;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
	       super.onCreate(savedInstanceState);
	       setContentView(R.layout.activity_ad_detail);
	       
	       receiver = new MyReceiver(); // ע��㲥
	       IntentFilter filter = new IntentFilter();
	       filter.addAction("android.intent.action.AdDetailActivity"); //�˴��ĳ��Լ�activity������
	       registerReceiver(receiver, filter);
	       init();
	       
	       Bundle bundle = this.getIntent().getExtras();
	       TaskId = bundle.getString("TaskId");
	       
	       //���Ͳ�ѯ������Ϣ
	       String sendstr = "&56&00" + TaskId;
	       sent(sendstr);
	       mHandler = new Handler() {
				public void handleMessage(android.os.Message msg) {
					String strall = msg.obj.toString();
					String str1 = strall.substring(1, 3);
					String str2 = strall.substring(1, 7);
					if (str1.equals("56")) { // ���ճɹ�
						task.Initial(strall);	
						ShowMessage();	
					}	
					if (str2.equals("57&000")) { // ������ɷ��ͳɹ�
						showToast("���ͳɹ�");
						//��ɰ�ť�����ٵ��
						button1.setEnabled(false);
					}	
					if (str2.equals("57&003")||str2.equals("57&004")||str2.equals("57&002")) { // ������ɷ��ͳɹ�
						showToast("����ʧ��");
						//��ɰ�ť���ٵ��
						button1.setEnabled(true);
					}	
				};
			};	
	       button1.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					new AlertDialog.Builder(AdDetailActivity.this)
					.setTitle("��ܰ��ʾ")
					.setMessage("�Ƿ�ȷ��������� ȷ�Ϻ��޷�ȡ��")
					.setPositiveButton("ȷ��",new DialogInterface.OnClickListener(){
							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								/*����ȷ����������ַ���*/
								byte[] msgBuffer = null;
								String sendstr = "&57&00" + TaskId;
								sent(sendstr);	
								button1.setEnabled(false);
							}						
					})
					.setNegativeButton("ȡ��",null)
					.create()
					.show();
				}
			});
	       
    }
    public void init(){
	    button1 = (Button) findViewById(R.id.AchieveTask);//�������
	    User = (TextView)findViewById(R.id.infoOrderer);//�׷��û�
	    infoPhoneOrderer = (TextView)findViewById(R.id.infoPhoneOrderer);//�׷��绰
	    DDL = (TextView)findViewById(R.id.infoArriTime);//�ʹ�ʱ��
	    Coins = (TextView)findViewById(R.id.infoCoinsNum);//���ͽ��
	    Address = (TextView)findViewById(R.id.infoAddress);//�ͻ���ַ
	    Adtime = (TextView)findViewById(R.id.infoOrderTime);//�µ�ʱ�� 
	    infoOrderNum = (TextView)findViewById(R.id.infoOrderNum);//������
	    taskMessage = (TextView)findViewById(R.id.taskMessage);//������Ϣ
    }
    

	//****************������netservice��ͨ�Ľӿڣ��շ����ݣ����ַ�����ʽ	
	public void sent(String bs) { // ͨ��Service��������
		Intent intent = new Intent();// ����Intent����
		intent.setAction("android.intent.action.cmd");
		intent.putExtra("value", bs);
		sendBroadcast(intent);// ���͹㲥
	}
	public void ShowMessage(){
		User.setText(task.GetUsr1Name());
		DDL.setText(String.valueOf(task.GetOutTime()[0])+"��"+String.valueOf(task.GetOutTime()[1])+"��"+String.valueOf(task.GetOutTime()[2])+"ʱ"+String.valueOf(task.GetOutTime()[3])+"��");
		Coins.setText(String.valueOf(task.GetCoins()));
		Address.setText(Data_all.Address[(task.GetOutAddress())[0] ][(task.GetOutAddress())[1] ] );				
		//����ʱ����Ϣ
		Adtime.setText(String.valueOf(task.GetInTime()[0])+"��"+String.valueOf(task.GetInTime()[1])+"��"+String.valueOf(task.GetInTime()[2])+"ʱ"+String.valueOf(task.GetInTime()[3])+"��");
		infoPhoneOrderer.setText(task.GetUsr1Tele());
		infoOrderNum.setText(String.valueOf(task.GetTNO()));
		taskMessage.setText(task.GetContent());
		if(task.GetTaskstate()==1){
			button1.setEnabled(true);
		}
		if(task.GetTaskstate()==2){
			button1.setEnabled(false);
		}
	}
    private class MyReceiver extends BroadcastReceiver { // ����service��������Ϣ
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			if (intent.getAction().equals("android.intent.action.AdDetailActivity")) {
				Bundle bundle = intent.getExtras();
				String str = bundle.getString("str");
				Message msg = new Message();
				msg.obj = str;
				mHandler.sendMessage(msg);
			}
		}
	}
    
    public void showToast(final String str) {
		handler = new Handler(Looper.getMainLooper());
		handler.post(new Runnable() {
			public void run() {
				Toast.makeText(getApplicationContext(), str,
						Toast.LENGTH_LONG).show();
			}
		});
	}
}
