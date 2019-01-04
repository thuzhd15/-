package com.example.lazy_man_client;

import java.util.ArrayList;
import java.util.List;

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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

public class AdoptTaskActivity extends Activity{
	
	private Handler mHandler;
	MyReceiver receiver;
	
    private ListView list;
    List<String> missions;
    
    private Button button1;
    private Button button2;
    
    /*此处向数据库查询我接受的任务*/
    Task task = new Task();

    String[] array;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
	       super.onCreate(savedInstanceState);
	       setContentView(R.layout.activity_task_adopt);
	       
	       receiver = new MyReceiver(); // 注册广播
	       IntentFilter filter = new IntentFilter();
	       filter.addAction("android.intent.action.AdoptTaskActivity"); //此处改成自己activity的名字
	       registerReceiver(receiver, filter);
	       
	       mHandler = new Handler() {
				public void handleMessage(android.os.Message msg) {
					String strall = msg.obj.toString();
					String str = strall.substring(1, 3);
					if (str.equals("64")) { // 接收成功
						task.Initial(strall);
						Showmissions(true);
					}	
				};
			};
			
		   init(); // 控件初始化
		   
		   button1.setOnClickListener(new OnClickListener() {
				//点击了未完成的任务
				public void onClick(View v){
					String sendstr = "&64&41&08"+Data_all.User_ID;
				    sent(sendstr);
				    button1.setEnabled(false);
				    button2.setEnabled(true);
				}
			});
		   button2.setOnClickListener(new OnClickListener() {
				//点击了已完成的任务
				public void onClick(View v){
					String sendstr = "&64&38&08"+Data_all.User_ID;
				    sent(sendstr);
				    button1.setEnabled(true);
				    button2.setEnabled(false);
				}
			});
	    }
	
    // 信息初始化：获取任务列表
    @Override
    protected void onResume() {
    	// 默认查看未完成的任务
		String sendstr = "&64&41&08" + Data_all.User_ID;
		sent(sendstr);
		
		button1.setEnabled(false);
	    button2.setEnabled(true);
    	super.onResume();
    }

	//****************两个与netservice沟通的接口，收发数据，，字符串格式	
	public void sent(String bs) { // 通过Service发送数据
		Intent intent = new Intent();// 创建Intent对象
		intent.setAction("android.intent.action.cmd");
		intent.putExtra("value", bs);
		sendBroadcast(intent);// 发送广播
	}
	
	private void Showmissions(boolean havem) {
		String size_list[] = Data_all.Size;
		missions = new ArrayList<String>();
		missions.add("大小   取货时间        交货时间       交货区域");
		if(havem){
			for(int i=0;i<task.GetTasklist().length;i++){
				Task.T curTask = task.GetTasklist()[i];

				String des = size_list[curTask.Size] + "   ";
				String tmp = "";				
				// 取货相关
				for(int j=2;j<4;j++) {
					tmp = String.valueOf(curTask.In_Time[j]);
					if( tmp.length() == 1 ) {
						des += "0" + String.valueOf(curTask.In_Time[j]);
					} else {
						des += String.valueOf(curTask.In_Time[j]);
					}
					if(j==2) {
						des += ":00-";
					} else {
						des += ":00   ";
					}
				}			
				// 收货相关
				for(int j=2;j<4;j++) {
					tmp = String.valueOf(curTask.Out_Time[j]);
					if( tmp.length() == 1 ) {
						des += "0" + String.valueOf(curTask.Out_Time[j]);
					} else {
						des += String.valueOf(curTask.Out_Time[j]);
					}
					if(j==2) {
						des += ":00-";
					} else {
						des += ":00   ";
					}
				}				
				des += Data_all.Section[curTask.Out_Address[0]];

				missions.add(des);
			}
		}
		if (missions.size() >= 0) {
			
			ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, R.layout.array_list_view,missions);
			// ListView自身就带有滚动条
			list.setAdapter(adapter1);
			list.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					// arg2表示点击的是第几个列表
					Intent intent =new Intent(AdoptTaskActivity.this,AdDetailActivity.class);
					//用Bundle携带数据
				    Bundle bundle=new Bundle();
				    if(arg2!=0) {
				    	int TNO = (task.GetTasklist())[arg2-1].TNO;
					    bundle.putString("TaskId", String.valueOf(TNO));
					    intent.putExtras(bundle);
						startActivity(intent);
				    }
				}
			});
		}

	}
	
	 public void init(){
		 	list = (ListView)findViewById(R.id.TaskListView2);
		 	button1 = (Button) findViewById(R.id.Unfinished);//未完成的任务
		 	button1.setEnabled(false);//默认状态，初始不可点击
	        button2 = (Button) findViewById(R.id.Finished);//已完成的任务
	        button2.setEnabled(true);//查看历史任务，初始设置为可点击
	    }
	
	private class MyReceiver extends BroadcastReceiver { // 接收service传来的信息
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			if (intent.getAction().equals("android.intent.action.AdoptTaskActivity")) {
				Bundle bundle = intent.getExtras();
				String str = bundle.getString("str");
				Message msg = new Message();
				msg.obj = str;
				mHandler.sendMessage(msg);
			}
		}
	}


}
