package com.example.task;

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
    private Button button2;
    
    private TextView User;
    private TextView DDL;
    private TextView Coins;
    
	private Handler mHandler;
	MyReceiver receiver;
	Mytask task = new Mytask();
	
	private String TaskId;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
	       super.onCreate(savedInstanceState);
	       setContentView(R.layout.activity_addetail);
	       
	       receiver = new MyReceiver(); // ע��㲥
	       IntentFilter filter = new IntentFilter();
	       filter.addAction("android.intent.action.AdoptTaskActivity"); //�˴��ĳ��Լ�activity������
	       
	       init();
	       
	       Bundle bundle = this.getIntent().getExtras();
	       TaskId = bundle.getString("TaskId");
	       
	       //���Ͳ�ѯ������Ϣ
	       String sendstr = "&54&00" + TaskId;
	       sent(sendstr);
	       mHandler = new Handler() {
				public void handleMessage(android.os.Message msg) {
					String str = msg.obj.toString();
					Toast.makeText(getApplicationContext(), "handle"+str, Toast.LENGTH_SHORT)
					.show();	
					if (str.equals("000")) { // ��½�ɹ�

					}
					else if (str.equals("001")) { 
	
					}else{
						task.Initial(str);
						User.setText(task.GetUsr1Name());
						DDL.setText(task.GetInTime().toString());
						Coins.setText(task.GetCoins());
					}
					
				};
			};
			
			
	       button1.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					new AlertDialog.Builder(AdDetailActivity.this)
					.setTitle("��ܰ��ʾ")
					.setMessage("�Ƿ�ȷ���������")
					.setPositiveButton("ȷ��",new DialogInterface.OnClickListener(){
							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								/*����ȷ����������ַ���*/
								byte[] msgBuffer = null;
								String sendstr = "&57&00" + TaskId;
								sent(sendstr);
								
							}						
					})
					.setNegativeButton("ȡ��",null)
					.create()
					.show();
				}
			});
	       
	       button2.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					new AlertDialog.Builder(AdDetailActivity.this)
					.setTitle("��ܰ��ʾ")
					.setMessage("�Ƿ�ȷ����������")
					.setPositiveButton("ȷ��",new DialogInterface.OnClickListener(){
							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								/*���ͷ��������ַ���*/
								byte[] msgBuffer = null;
								String sendstr = "&55&"+"00" + TaskId;
								sent(sendstr);
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
	    button2 = (Button) findViewById(R.id.GiveupTask);//��������
	    User = (TextView)findViewById(R.id.User1_textView);//�׷��û���
	    DDL = (TextView)findViewById(R.id.ADDDL_textView);//�׷��û���
	    Coins = (TextView)findViewById(R.id.ADCoins_textView);//�׷��û���
    }
    
    public void connect() { // ���ӷ�����������Service
		Intent intent = new Intent(AdDetailActivity.this, NetService.class);
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
			if (intent.getAction().equals("android.intent.action.AdDetailActivity")) {
				Bundle bundle = intent.getExtras();
				String str = bundle.getString("str");
				Message msg = new Message();
				msg.obj = str;
				mHandler.sendMessage(msg);
			}
		}
	}
}
