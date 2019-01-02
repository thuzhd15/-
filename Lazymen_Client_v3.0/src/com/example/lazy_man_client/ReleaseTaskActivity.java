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
    
    private Button button1;
    private Button button2;
    private Button button3;
    
    Task task = new Task();
    
    String[] array;
    ArrayList<String> MyTaskList = new ArrayList<String>();

    @Override
	protected void onCreate(Bundle savedInstanceState) {
	       super.onCreate(savedInstanceState);
	       setContentView(R.layout.activity_task_release);
	       
	       init();
	       
	       receiver = new MyReceiver(); // ע��㲥
	       IntentFilter filter = new IntentFilter();
	       filter.addAction("android.intent.action.ReleaseTaskActivity"); //�˴��ĳ��Լ�activity������
	       registerReceiver(receiver, filter);
	       //�ֱ��ѯ�����ܵĺ���δ�����ܵ�����
	       String sendstr = "&54&39&07"+Data_all.User_ID;
	       sent(sendstr);
	       MyTaskList.add("�����б�");
	       mHandler = new Handler() {
				public void handleMessage(android.os.Message msg) {
					String strall = msg.obj.toString();
					String str = strall.substring(1, 3);
					if (str.equals("54")) { // ���ճɹ�
						MyTaskList.clear();
						task.Initial(strall);
						Showmissions(true);
					}		
				};
			};
			button1.setOnClickListener(new OnClickListener() {
				//�����δ��ɵ�����
				public void onClick(View v){
					String sendstr = "&54&39&07"+Data_all.User_ID;
				    sent(sendstr);
				    button1.setEnabled(false);
				    button2.setEnabled(true);
				    button3.setEnabled(true);
				}
			});
		   button2.setOnClickListener(new OnClickListener() {
				//���������ɵ�����
				public void onClick(View v){
					String sendstr = "&54&36&07"+Data_all.User_ID;
				    sent(sendstr);
				    button1.setEnabled(true);
				    button2.setEnabled(false);
				    button3.setEnabled(true);
				}
			});
		   button3.setOnClickListener(new OnClickListener() {
				//�����δ��ɵ�����
				public void onClick(View v){
					String sendstr = "&54&37&07"+Data_all.User_ID;
				    sent(sendstr);
				    button1.setEnabled(true);
				    button2.setEnabled(true);
				    button3.setEnabled(false);
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
	
	private void Showmissions(boolean havem) {
		String size_list[]=getResources().getStringArray(R.array.size);
		missions = new ArrayList<String>();
		if(havem){
			for(int i=0;i<task.GetTasklist().length;i++){
				Task.T curTask = task.GetTasklist()[i];
				String des = size_list[curTask.Size]+" "+String.valueOf(curTask.In_Time[0])+"��"+curTask.In_Time[1]+"�� "+Data_all.Section[curTask.Out_Address[0]];
				missions.add(des);
			}
		}
		if (missions.size() >= 0) {
		    ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, R.layout.array_list_view, missions);

			// ListView����ʹ��й�����
			list.setAdapter(adapter1);
			list.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					// arg2��ʾ������ǵڼ����б�
					Intent intent =new Intent(ReleaseTaskActivity.this,ReDetailActivity.class);
					//��BundleЯ������
				    Bundle bundle=new Bundle();
				    int TNO = (task.GetTasklist())[arg2].TNO;
				    bundle.putString("TaskId", String.valueOf(TNO));
				    intent.putExtras(bundle);
					startActivity(intent);

				}
			});
		}

	}
	
	public void init(){
		list = (ListView)findViewById(R.id.TaskListView);
		
	 	button1 = (Button) findViewById(R.id.Unfinished);//δ��ɵ�����
	 	button1.setEnabled(false);//Ĭ��״̬����ʼ���ɵ��
	 	
        button2 = (Button) findViewById(R.id.Finished);//����ɵ�����
        button2.setEnabled(true);//�鿴��ʷ���񣬳�ʼ����Ϊ�ɵ��
        
        button3 = (Button) findViewById(R.id.Annulled);//�ѳ���������
        button3.setEnabled(true);//�鿴��ʷ���񣬳�ʼ����Ϊ�ɵ��
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

