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
	       
	       receiver = new MyReceiver(); // 注册广播
	       IntentFilter filter = new IntentFilter();
	       filter.addAction("android.intent.action.AdoptTaskActivity"); //此处改成自己activity的名字
	       
	       init();
	       
	       Bundle bundle = this.getIntent().getExtras();
	       TaskId = bundle.getString("TaskId");
	       
	       //发送查询任务信息
	       String sendstr = "&54&00" + TaskId;
	       sent(sendstr);
	       mHandler = new Handler() {
				public void handleMessage(android.os.Message msg) {
					String str = msg.obj.toString();
					Toast.makeText(getApplicationContext(), "handle"+str, Toast.LENGTH_SHORT)
					.show();	
					if (str.equals("000")) { // 登陆成功

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
					.setTitle("温馨提示")
					.setMessage("是否确定完成任务")
					.setPositiveButton("确定",new DialogInterface.OnClickListener(){
							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								/*发送确认任务完成字符串*/
								byte[] msgBuffer = null;
								String sendstr = "&57&00" + TaskId;
								sent(sendstr);
								
							}						
					})
					.setNegativeButton("取消",null)
					.create()
					.show();
				}
			});
	       
	       button2.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					new AlertDialog.Builder(AdDetailActivity.this)
					.setTitle("温馨提示")
					.setMessage("是否确定放弃任务")
					.setPositiveButton("确定",new DialogInterface.OnClickListener(){
							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								/*发送放弃任务字符串*/
								byte[] msgBuffer = null;
								String sendstr = "&55&"+"00" + TaskId;
								sent(sendstr);
							}	
					})
					.setNegativeButton("取消",null)
					.create()
					.show();
				}
			});
    }
    public void init(){
	    button1 = (Button) findViewById(R.id.AchieveTask);//任务完成
	    button2 = (Button) findViewById(R.id.GiveupTask);//放弃任务
	    User = (TextView)findViewById(R.id.User1_textView);//甲方用户名
	    DDL = (TextView)findViewById(R.id.ADDDL_textView);//甲方用户名
	    Coins = (TextView)findViewById(R.id.ADCoins_textView);//甲方用户名
    }
    
    public void connect() { // 连接服务器，启动Service
		Intent intent = new Intent(AdDetailActivity.this, NetService.class);
		startService(intent);
	}

	//****************两个与netservice沟通的接口，收发数据，，字符串格式	
	public void sent(String bs) { // 通过Service发送数据
		Intent intent = new Intent();// 创建Intent对象
		intent.setAction("android.intent.action.cmd");
		intent.putExtra("value", bs);
		sendBroadcast(intent);// 发送广播
	}
    private class MyReceiver extends BroadcastReceiver { // 接收service传来的信息
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
