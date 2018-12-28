package com.example.task;

import java.util.ArrayList;

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
    /*此处向数据库查询我接受的任务*/
    Mytask task = new Mytask();
    ArrayList<String> MyTaskList = new ArrayList<String>();

    String[] array;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
	       super.onCreate(savedInstanceState);
	       setContentView(R.layout.activity_task_adopt);  
	       
	       receiver = new MyReceiver(); // 注册广播
	       IntentFilter filter = new IntentFilter();
	       filter.addAction("android.intent.action.AdoptTaskActivity"); //此处改成自己activity的名字
	       registerReceiver(receiver, filter);
	       
	       //发送查询任务列表
	       //String sendstr = "&01&";
	       //sent(sendstr);
	       
	       MyTaskList.add("任务列表");
	       mHandler = new Handler() {
				public void handleMessage(android.os.Message msg) {
					String str = msg.obj.toString();
					Toast.makeText(getApplicationContext(), "handle"+str, Toast.LENGTH_SHORT)
					.show();	
					task.Initial(str);
					for(int i = 0;i<task.Tasklist.length; i++){
						String time = "";
						String address="";
						for(int j = 0 ; j<task.Tasklist[i].In_Time.length;j++){
							String mon_list[]=getResources().getStringArray(R.array.mon);
							time+="时间："+String.valueOf(mon_list[task.Tasklist[i].In_Time[0]])
									+String.valueOf(task.Tasklist[i].In_Time[1])+"日,  从"+String.valueOf(task.Tasklist[i].In_Time[1])+"时~"+String.valueOf(task.Tasklist[i].In_Time[2])+"时";
						}
						for(int j = 0 ; j<task.Tasklist[i].In_Time.length;j++){
							address+=Data_all.Address[task.Tasklist[i].Out_Address[0]][task.Tasklist[i].Out_Address[1]];
						}
						MyTaskList.add(time+" "+address);
					} 
				};
			};
		   int size = MyTaskList.size();
		   array = (String[])MyTaskList.toArray(new String[size]); 
	       list = (ListView)findViewById(R.id.TaskListView2);
	       ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, R.layout.array_list_view,array );
	       list.setAdapter(adapter1);
	       list.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// arg2表示点击的是第几个列表
				Intent intent =new Intent(AdoptTaskActivity.this,AdDetailActivity.class);
				//用Bundle携带数据
			    //Bundle bundle=new Bundle();
			    //int TNO = task.Tasklist[arg2].TNO;
			    //bundle.putString("TaskId", String.valueOf(TNO));
			    //intent.putExtras(bundle);
				startActivity(intent);
			}	    	   
	       });
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

