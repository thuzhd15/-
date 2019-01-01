package com.example.lazy_man_client;


import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Button;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;


public class ReleaseTaskActivity extends Activity {
	
	private Handler mHandler;
	MyReceiver receiver;
	
    private ListView list;
    List<String> missions;
    
    Task task = new Task();
    
    String[] array;
    ArrayList<String> MyTaskList = new ArrayList<String>();

    @Override
	protected void onCreate(Bundle savedInstanceState) {
	       super.onCreate(savedInstanceState);
	       setContentView(R.layout.activity_task_release);
	       
	       receiver = new MyReceiver(); // 注册广播
	       IntentFilter filter = new IntentFilter();
	       filter.addAction("android.intent.action.ReleaseTaskActivity"); //此处改成自己activity的名字
	       registerReceiver(receiver, filter);
	       //分别查询被接受的和尚未被接受的任务
	       String sendstr = "&54&39&07"+Data_all.User_ID;
	       sent(sendstr);
	       MyTaskList.add("任务列表");
	       mHandler = new Handler() {
				public void handleMessage(android.os.Message msg) {
					String strall = msg.obj.toString();
					String str = strall.substring(1, 3);
					if (str.equals("54")) { // 接收成功
						MyTaskList.clear();
						task.Initial(strall);
						Showmissions(true);
					}		
				};
			};
	      
	    } 
    
    public void connect() { // 连接服务器，启动Service
		Intent intent = new Intent(ReleaseTaskActivity.this, NetService.class);
		startService(intent);
	}

	//****************两个与netservice沟通的接口，收发数据，，字符串格式	
	public void sent(String bs) { // 通过Service发送数据
		Intent intent = new Intent();// 创建Intent对象
		intent.setAction("android.intent.action.cmd");
		intent.putExtra("value", bs);
		sendBroadcast(intent);// 发送广播
	}
	
	private void Showmissions(boolean havem) {
		String size_list[]=getResources().getStringArray(R.array.size);
		missions = new ArrayList<String>();
		if(havem){
			for(int i=0;i<task.GetTasklist().length;i++){
				Task.T curTask = task.GetTasklist()[i];
				String des = size_list[curTask.Size]+String.valueOf(curTask.In_Time[1])+"日"+Data_all.Section[curTask.Out_Address[0]];
				missions.add(des);
			}
		}
		if (missions.size() > 0) {
			
		    list = (ListView)findViewById(R.id.TaskListView);
		    ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, R.layout.array_list_view, missions);

			// ListView自身就带有滚动条
			list.setAdapter(adapter1);
			list.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					// arg2表示点击的是第几个列表
					Intent intent =new Intent(ReleaseTaskActivity.this,ReDetailActivity.class);
					//用Bundle携带数据
				    Bundle bundle=new Bundle();
				    int TNO = (task.GetTasklist())[arg2].TNO;
				    bundle.putString("TaskId", String.valueOf(TNO));
				    intent.putExtras(bundle);
					startActivity(intent);

				}
			});
		}

	}
	
	private class MyReceiver extends BroadcastReceiver { // 接收service传来的信息
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			if (intent.getAction().equals("android.intent.action.ReleaseTaskActivity")) {
				Bundle bundle = intent.getExtras();
				String str = bundle.getString("str");
				Message msg = new Message();
				msg.obj = str;
				mHandler.sendMessage(msg);
			}
		}
	}
    
    
    @Override
    protected void onResume() {
        super.onResume();
    }
    @Override
    protected void onDestroy() {

        super.onDestroy();
    }

}

