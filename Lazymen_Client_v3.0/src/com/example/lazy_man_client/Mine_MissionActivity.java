//������Ϣ&&������������
package com.example.lazy_man_client;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.view.LayoutInflater;
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
	private String id, username, realname, tele, email, school;
	private int coin, credit;
	private Button button_account;
	private TextView text_username, text_realname, text_tele, text_email, text_school, text_coin, text_credit;
	
	private TabHost tabhost;
	ListView lv_mission; // ��ѡ�豸
	List<String> missions;
	Spinner spinner;
	EditText editcon;
	MyReceiver receiver;
	Button dosearch, myrelease, myaccept, release;
	private Handler mHandler;
	Task curtask;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mine__mission);
		// ��TabActivity�����ȡ����Tab��TabHost
		curtask = new Task();
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
		lv_mission = (ListView) findViewById(R.id.listmissions);
		
		text_username = (TextView) findViewById(R.id.text_username);
		text_realname = (TextView) findViewById(R.id.text_realname);
		text_tele = (TextView) findViewById(R.id.text_tele);
		text_school = (TextView) findViewById(R.id.text_school);
		text_email = (TextView) findViewById(R.id.text_email);
		text_coin = (TextView) findViewById(R.id.text_coin);
		text_credit = (TextView) findViewById(R.id.text_credit);
		
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
		release.setOnClickListener(new View.OnClickListener() {            
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//�������ťʱ,���ȡ�༭���е�����,Ȼ���ύ���߳�
				Intent intent = new Intent(Mine_MissionActivity.this,
						AReleaseTask.class);
				startActivity(intent);			
			}
		});
				
		receiver = new MyReceiver(); // ע��㲥
		IntentFilter filter = new IntentFilter();
		filter.addAction("android.intent.action.Mine_MissionActivity");
		registerReceiver(receiver, filter);
		
		mHandler = new Handler() {
			public void handleMessage(android.os.Message msg) {
				String str = msg.obj.toString();
				//				Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT)
				//						.show();
				String substr = str.substring(0, 3);	
				if (substr.equals("&03")) { // ��ʼ��
					init(str);
					Showmissions(false);
				}
				else if(substr.equals("&54")){
					curtask.Initial(str);
					Showmissions(true);
				}
			};
		};
		
		// ����һ��ArrayAdapter���ڴ�ż�����
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				Mine_MissionActivity.this,
				android.R.layout.simple_spinner_item, getData());
		// �Ѷ���õ�Adapter�趨��spinner��
		spinner.setAdapter(adapter);
		Showmissions(false);
		//		spinner.getSelectedItemPosition();
		/*
		 * ����ɸѡ������Ҫ������������Ӧ��������Ϣ
		 * ʾ���������ս�����򣻷����ַ�     &53&31
		 * ��Ҫ��������������Ӧ��������Ϣ
		 * ������ģʽ��30/31/32ʱ������ĩβ�ַ����������еĻ���
		 */
		
		dosearch = (Button) findViewById(R.id.dosearch);
		dosearch.setOnClickListener(new View.OnClickListener() {            
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//�������ťʱ,���ȡ�༭���е�����,Ȼ���ύ���߳�
				int spiselect = spinner.getSelectedItemPosition();
				String cmd = String.valueOf(spiselect+30);
				String sendstr = "&54&"+cmd+editcon.getText().toString();
				sent(sendstr);

				editcon.setText(sendstr); //test method
			}
		});
		
		button_account = (Button) findViewById(R.id.button_account);
		button_account.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
				Bundle bundle = new Bundle();
				bundle.putString("KEY",Data_all.User_ID);
				Intent intent = new Intent(Mine_MissionActivity.this,ChangeActivity.class);
				intent.putExtras(bundle);
				startActivity(intent);
				finish();
			}
		});
		
		//***********��ʼ��mineҳ��
		String sendstr = "&03&06"+Data_all.User_ID;
		sent(sendstr);  //������������ظ�����Ϣ
	}

	private List<String> getData() {
		// ����Դ
		List<String> dataList = new ArrayList<String>();
		dataList.add("Ĭ������");
		dataList.add("�������");
		dataList.add("��������");
		dataList.add("����ַ����");
		dataList.add("��ʱ������");
		return dataList;
	}

	public void init(String str){
		Usr mine = new Usr();
		mine.Initial(str);
		username = mine.GetUsrName();
		realname = mine.GetRealName();
		tele = mine.GetTeleNumber();
		email = mine.GetEmail();
		school = mine.GetSchool();
		coin = mine.GetCoins();
		credit = mine.GetCredit();
		text_username.setText("�û���:"+username);
		text_realname.setText("����:"+realname);
		text_tele.setText("�绰�ţ�"+tele);
		text_school.setText("ѧԺ:"+school);
		text_email.setText("���䣺"+email);
		text_coin.setText("�������"+String.valueOf(coin));
		text_credit.setText("����ֵ��"+String.valueOf(credit));
				
	}
	
	private void Showmissions(boolean havem) {
		String size_list[]=getResources().getStringArray(R.array.size);
		//		String mon_list[]=getResources().getStringArray(R.array.mon);
		missions = new ArrayList<String>();
		if(havem){
			for(int i=0;i<curtask.GetTasklist().length;i++){
				Task.T curTask = curtask.GetTasklist()[i];
				String des = size_list[curTask.Size]+String.valueOf(curTask.In_Time[1])+"��"+Data_all.Section[curTask.Out_Address[0]];
				missions.add(des);
			}
		}
		if (missions.size() > 0) {
			ArrayAdapter<String> aa = new ArrayAdapter<String>(
					Mine_MissionActivity.this,
					android.R.layout.simple_list_item_1, missions);
			// ListView����ʹ��й�����
			lv_mission.setAdapter(aa);
			lv_mission.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					Showdialog(curtask.GetTasklist()[arg2]);

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
				// if (cmd == CMD_SHOW_TOAST) {
				// String str = bundle.getString("str");
				// showToast(str);
				// } else if (cmd == CMD_RECEIVE_DATA) {
				String str = bundle.getString("str");
				Message msg = new Message();
				msg.obj = str;
				mHandler.sendMessage(msg);
				// }
			}
		}
	}

	private void Showdialog(final Task.T curTask) {   // ��ʾ��Ϣ�ĶԻ���
		String mon_list[]=getResources().getStringArray(R.array.mon);
		LayoutInflater factory=LayoutInflater.from(Mine_MissionActivity.this);
		final View v1=factory.inflate(R.layout.dialogview,null);
		//R.layout.login��login.xml�ļ�����Ӧ,��loginת����View����
		AlertDialog.Builder dialog=new AlertDialog.Builder(Mine_MissionActivity.this);
		dialog.setTitle("��������");
		dialog.setView(v1);//����ʹ��View
		//���ÿؼ�Ӧ����v1.findViewById �������
		final TextView diatv = (TextView) v1.findViewById(R.id.updatetv);
		String str = "��Ʒ���ͣ�"+Data_all.Size[curTask.Size]+"\n"+"ʱ�䣺"+String.valueOf(mon_list[curTask.In_Time[0]])
				+String.valueOf(curTask.In_Time[1])+"��,  ��"+String.valueOf(curTask.In_Time[2])+"ʱ~"+String.valueOf(curTask.In_Time[3])+"ʱ"
				+"\n�ص㣺"+Data_all.Section[curTask.Out_Address[0]]+Data_all.Address[curTask.Out_Address[0]][curTask.Out_Address[1]];
		diatv.setText(str);
		/*		�����������������ַ�����      &55+����ID
		��Ҫ���������أ��Ƿ����ɹ�*/
		dialog.setPositiveButton("��������",new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				String str = "&55"+String.valueOf(curTask.TNO);
				sent(str);
			}
		});

		dialog.setNegativeButton("����",new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				//	        	dialog.dismiss();
			}
		});
		dialog.show();
	}

	public void showToast(String str) {// ��ʾ��ʾ��Ϣ
		Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT).show();
	}

	public void sent(String bs) { // ͨ��Service��������
		Intent intent = new Intent();// ����Intent����
		intent.setAction("android.intent.action.cmd");
		intent.putExtra("value", bs);
		sendBroadcast(intent);// ���͹㲥
	}
}
