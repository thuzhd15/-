package com.example.mysocketdemo;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.example.mysocketdemo.MainActivity.MyReceiver;

import android.support.v7.app.ActionBarActivity;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class MissionActivity extends ActionBarActivity {
	static final int CMD_STOP_SERVICE = 0x01;
	static final int CMD_SEND_DATA = 0x02;
	static final int CMD_RECEIVE_DATA = 0x03;
	static final int CMD_SHOW_TOAST = 0x04; // 预定义的命令
	ListView lv_mission; // 可选设备
	List<String> missions;
	MyReceiver receiver;
	private Handler mHandler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mision);
		lv_mission = (ListView) findViewById(R.id.listmissions);
		receiver = new MyReceiver(); // 注册广播
		IntentFilter filter = new IntentFilter();
		filter.addAction("android.intent.action.lxx");
		registerReceiver(receiver, filter);
		mHandler = new Handler() {
			public void handleMessage(android.os.Message msg) {
				String str = msg.obj.toString();
				Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT)
						.show();
			};
		};
		Showmissions();
	}

	private void Showmissions() {
		missions = new ArrayList<String>();
		if (missions.size() > 0) {
			ArrayAdapter aa = new ArrayAdapter(MissionActivity.this,
					android.R.layout.simple_list_item_1, missions);
			lv_mission.setAdapter(aa);
			lv_mission.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					String missionID = missions.get(arg2);
					String JIAID = " ", YIID = " "; // 三种类型，甲方/乙方/无关
					if (missionID.equals(JIAID)) {
						Intent intent = new Intent(MissionActivity.this,
								JIAFActivity.class);
						intent.putExtra("missionID", missionID);
						startActivity(intent);
					} else if (missionID.equals(YIID)) {
						Intent intent = new Intent(MissionActivity.this,
								YIFActivity.class);
						intent.putExtra("missionID", missionID);
						startActivity(intent);
					} else {
						Intent intent = new Intent(MissionActivity.this,
								OTHERActivity.class);
						intent.putExtra("missionID", missionID);
						startActivity(intent);
					}
				}
			});
		}

	}

	public class MyReceiver extends BroadcastReceiver { // 接收service传来的信息
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			if (intent.getAction().equals("android.intent.action.lxx")) {
				Bundle bundle = intent.getExtras();
				int cmd = bundle.getInt("cmd");

				if (cmd == CMD_SHOW_TOAST) {
					String str = bundle.getString("str");
					showToast(str);
				} else if (cmd == CMD_RECEIVE_DATA) {
					String str = bundle.getString("str");
					Message msg = new Message();
					msg.obj = str;
					mHandler.sendMessage(msg);
				}
			}
		}
	}

	public void showToast(String str) {// 显示提示信息
		Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT).show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.mision, menu);
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
