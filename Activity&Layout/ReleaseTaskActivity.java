package com.example.task;


import java.util.ArrayList;

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
    /*�˴������ݿ��ѯ�ҷ��������񣬲����ϳ��б�*/
    Mytask task = new Mytask();
    String[] array;
    ArrayList<String> MyTaskList = new ArrayList<String>();

    @Override
	protected void onCreate(Bundle savedInstanceState) {
	       super.onCreate(savedInstanceState);
	       setContentView(R.layout.activity_task_release);
	       
	       receiver = new MyReceiver(); // ע��㲥
	       IntentFilter filter = new IntentFilter();
	       filter.addAction("android.intent.action.ReleaseTaskActivity"); //�˴��ĳ��Լ�activity������
	       registerReceiver(receiver, filter);
	       
	       //���Ͳ�ѯ�����б�
	       String sendstr = "&54&39&40&07"+Usr.UsrID;
	       sent(sendstr);
	       MyTaskList.add("�����б�");
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
							time+=String.valueOf(task.Tasklist[i].In_Time[j]);
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
	       list = (ListView)findViewById(R.id.TaskListView);
	       ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, R.layout.array_list_view, array);
	       list.setAdapter(adapter1);
	       list.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// arg2��ʾ������ǵڼ����б�
				Intent intent =new Intent(ReleaseTaskActivity.this,ReDetailActivity.class);
				//��BundleЯ������
			    //Bundle bundle=new Bundle();
			    //����name����Ϊtinyphp
			    //int TNO = task.Tasklist[arg2].TNO;
			    //bundle.putString("TaskId", String.valueOf(TNO));
			    //intent.putExtras(bundle);
				startActivity(intent);
			}	    	   
	       });
	      
	    } 
    
    public void connect() { // ���ӷ�����������Service
		Intent intent = new Intent(ReleaseTaskActivity.this, NetService.class);
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
