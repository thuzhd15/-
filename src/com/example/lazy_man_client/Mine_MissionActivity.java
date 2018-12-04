package com.example.lazy_man_client;

import java.util.ArrayList;
import java.util.List;

import com.example.mysocketdemo.R;

import android.support.v7.app.ActionBarActivity;
import android.app.TabActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Spinner;

@SuppressWarnings("deprecation")
public class Mine_MissionActivity extends TabActivity {
	private TabHost tabhost;
	ListView lv_mission; // ��ѡ�豸
	List<String> missions;
	Spinner spinner;
	EditText editcon;
	private int stateflag = 0;  // 0:wait   1:OK   -1:fail
	MyReceiver receiver;
	Button dosearch, myrelease, myaccept, release;
	private Handler mHandler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mine__mission);
		// ��TabActivity�����ȡ����Tab��TabHost
		tabhost = getTabHost();
		tabhost.addTab(tabhost
		// �����±�ǩone
				.newTabSpec("one")
				// ���ñ�ǩ����
				.setIndicator("�ҵ�")
				// ���øñ�ǩ�Ĳ�������
				.setContent(R.id.widget_layout_red));
		tabhost.addTab(tabhost.newTabSpec("two").setIndicator("��������")
				.setContent(R.id.widget_layout_yellow));
		spinner = (Spinner) findViewById(R.id.spinner_condition);
		editcon = (EditText) findViewById(R.id.edit_condition);
		dosearch = (Button) findViewById(R.id.dosearch);
		lv_mission = (ListView) findViewById(R.id.listmissions);
		dosearch = (Button) findViewById(R.id.dosearch);
		myrelease = (Button) findViewById(R.id.myrelease);
		myrelease.setOnClickListener(new View.OnClickListener() {            
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                //�������ťʱ,���ȡ�༭���е�����,Ȼ���ύ���߳�
					Intent intent = new Intent(Mine_MissionActivity.this,
							ReleaseTaskActivity.class);
					startActivity(intent);			
            }
        });
		myaccept = (Button) findViewById(R.id.myaccept);
		myaccept.setOnClickListener(new View.OnClickListener() {            
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                //�������ťʱ,���ȡ�༭���е�����,Ȼ���ύ���߳�
					Intent intent = new Intent(Mine_MissionActivity.this,
							AdoptTaskActivity.class);
					startActivity(intent);			
            }
        });		
		release = (Button) findViewById(R.id.torelease);
//		release.setOnClickListener(new View.OnClickListener() {            
//            @Override
//            public void onClick(View v) {
//                // TODO Auto-generated method stub
//                //�������ťʱ,���ȡ�༭���е�����,Ȼ���ύ���߳�
//					Intent intent = new Intent(Mine_MissionActivity.this,
//							AdoptTaskActivity.class);
//					startActivity(intent);			
//            }
//        });	
		
		receiver = new MyReceiver(); // ע��㲥
		IntentFilter filter = new IntentFilter();
		filter.addAction("android.intent.action.Mine_MissionActivity");
		registerReceiver(receiver, filter);
		mHandler = new Handler() {
			public void handleMessage(android.os.Message msg) {
				String str = msg.obj.toString();
				Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT)
						.show();
			};
		};
		// ����һ��ArrayAdapter���ڴ�ż�����
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				Mine_MissionActivity.this,
				android.R.layout.simple_spinner_item, getData());
		// �Ѷ���õ�Adapter�趨��spinner��
		spinner.setAdapter(adapter);
		Showmissions();

//		spinner.getSelectedItemPosition();
		/*
		 * ����ɸѡ������Ҫ������������Ӧ��������Ϣ
		 * ʾ�������������10�����ϵ����񣻷����ַ�     &53&3110
		 * ��Ҫ��������������Ӧ��������Ϣ
		 */	
		dosearch.setOnClickListener(new View.OnClickListener() {            
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                //�������ťʱ,���ȡ�༭���е�����,Ȼ���ύ���߳�
            	byte[]  msgBuffer = null;
            	String sendstr = "&53&"+"&0"+spinner.getSelectedItemPosition()+editcon.getText().toString();
            	msgBuffer = sendstr.getBytes();
//                sent(msgBuffer);
//				waiting();
                stateflag=1;
//				if (1 == stateflag) {
//					Toast.makeText(getApplicationContext(), "��¼�ɹ�",
//							Toast.LENGTH_SHORT).show();
//					Intent intent = new Intent(Mine_MissionActivity.this,
//							Mine_MissionActivity.class);
//					startActivity(intent);
//				}
                editcon.setText(sendstr);
            }
		});
	}

	private List<String> getData() {
		// ����Դ
		List<String> dataList = new ArrayList<String>();
		dataList.add("����");
		dataList.add("ʱ��");
		dataList.add("�ص�");
		return dataList;
	}

	private void Showmissions() {
		missions = new ArrayList<String>();
		missions.add("xixihaha!");
		missions.add("youxi!");
		missions.add("enen!");
		missions.add("heq865y49uf9qh!");
		missions.add("youxi!");
		missions.add("enen!");
		missions.add("heq865y49uf9qh!");
		missions.add("youxi!");
		missions.add("enen!");
		missions.add("heq865y49uf9qh!");
		missions.add("youxi!");
		missions.add("enen!");
		missions.add("heq865y49uf9qh!");
		missions.add("youxi!");
		missions.add("enen!");
		missions.add("heq865y49uf9qh!");
		missions.add("youxi!");
		missions.add("enen!");
		missions.add("heq865y49uf9qh!");
		missions.add("youxi!");
		missions.add("enen!");
		if (missions.size() > 0) {
			ArrayAdapter<String> aa = new ArrayAdapter<String>(
					Mine_MissionActivity.this,
					android.R.layout.simple_list_item_1, missions);
			// ListView����ʹ��й�����
			lv_mission.setAdapter(aa);
			lv_mission.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					String missionID = missions.get(arg2);
					String JIAID = "xixihaha!", YIID = "youxi!"; // �������ͣ��׷�/�ҷ�/�޹�
//					if (missionID.equals(JIAID)) {
//						Intent intent = new Intent(Mine_MissionActivity.this,
//								JIAFActivity.class);
//						intent.putExtra("missionID", missionID);
//						startActivity(intent);
//					} else if (missionID.equals(YIID)) {
//						Intent intent = new Intent(Mine_MissionActivity.this,
//								YIFActivity.class);
//						intent.putExtra("missionID", missionID);
//						startActivity(intent);
//					} else {
//						Intent intent = new Intent(Mine_MissionActivity.this,
//								OTHERActivity.class);
//						intent.putExtra("missionID", missionID);
//						startActivity(intent);
//					}
				}
			});
		}

	}

	private class MyReceiver extends BroadcastReceiver { // ����service��������Ϣ
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			if (intent.getAction().equals(
					"android.intent.action.Mine_MissionActivity")) {
				Bundle bundle = intent.getExtras();
				int cmd = bundle.getInt("cmd");

				// if (cmd == CMD_SHOW_TOAST) {
				// String str = bundle.getString("str");
				// showToast(str);
				// } else if (cmd == CMD_RECEIVE_DATA) {
				// String str = bundle.getString("str");
				// Message msg = new Message();
				// msg.obj = str;
				// mHandler.sendMessage(msg);
				// }
			}
		}
	}

	public void showToast(String str) {// ��ʾ��ʾ��Ϣ
		Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT).show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.mine__mission, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
