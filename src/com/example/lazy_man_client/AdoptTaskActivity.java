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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;




public class AdoptTaskActivity extends Activity{
	
	private Handler mHandler;
	MyReceiver receiver;
	
    private ListView list;
    List<String> missions;
    
    
    
    /*此处向数据库查询我接受的任务*/
    Task task = new Task();
    ArrayList<String> MyTaskList = new ArrayList<String>();

    String[] array;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
	       super.onCreate(savedInstanceState);
	       setContentView(R.layout.activity_task_adopt); 
	       
	       list = (ListView)findViewById(R.id.TaskListView2);
	       receiver = new MyReceiver(); // 注册广播
	       IntentFilter filter = new IntentFilter();
	       filter.addAction("android.intent.action.AdoptTaskActivity"); //此处改成自己activity的名字
	       registerReceiver(receiver, filter);
	       
	       //发送查询任务列表
	       String sendstr = "&54&41&08"+Data_all.User_ID;
	       sent(sendstr);
	       
	       MyTaskList.add("当前无任务");
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
			
			// 声明一个ArrayAdapter用于存放简单数据
	    }
	

	
	public void connect() { // 连接服务器，启动Service
		Intent intent = new Intent(AdoptTaskActivity.this, NetService.class);
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
		//		String mon_list[]=getResources().getStringArray(R.array.mon);
		missions = new ArrayList<String>();
		if(havem){
			for(int i=0;i<task.GetTasklist().length;i++){
				Task.T curTask = task.GetTasklist()[i];
				String des = size_list[curTask.Size]+String.valueOf(curTask.In_Time[1])+"日"+Data_all.Section[curTask.Out_Address[0]];
				missions.add(des);
			}
		}
		if (missions.size() > 0) {
			
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
			if (intent.getAction().equals("android.intent.action.AdoptTaskActivity")) {
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
