//个人信息&&任务搜索界面

package com.example.lazy_man_client;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.support.v7.app.ActionBarActivity;
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
	private String  username, realname, tele, email, school;
	private int coin, credit;
	private Button button_information, button_password, button_address; // 账户修改相关
	private TextView text_username, text_realname, text_tele, text_email, text_school, text_coin, text_credit;
	private CheckBox box_address, box_time,box_address2, box_time2;
	private EditText edit_day, edit_day2;

	private TabHost tabhost;
	
	private Handler mHandler;

	ListView lv_mission; // 可选设备
	Spinner spinner,sp_section, sp_address, sp_mon,sp_section2, sp_address2, sp_mon2;
	MyReceiver receiver;
	Button dosearch, myrelease, myaccept, release;
	
	String sendstr;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mine__mission);
		
		init(); //控件初始化
		
		 // 注册广播
		receiver = new MyReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction("android.intent.action.Mine_MissionActivity");
		registerReceiver(receiver, filter);
		
		mHandler = new Handler() {
			public void handleMessage(android.os.Message msg) {
				String str = msg.obj.toString();
				String substr = str.substring(0, 3);	
				if (substr.equals("&03")) { // 初始化
					user_init(str);
					Showmissions(false,new Task());
				}
				else if(substr.equals("&54")){  //显示任务列表
					Task curtask = new Task();
					curtask.Initial(str);
					Showmissions(true,curtask);
				}	
				else if(substr.equals("&56")){  //显示任务详情
					Task myTask = new Task();
					myTask.Initial(str);
					Showdialog(myTask);
				}
				else if(substr.equals("&55")){  // 接受任务是否成功的消息
					String str2 = str.substring(3, 7);
					//					Toast.makeText(getApplicationContext(), "handle"+str, Toast.LENGTH_SHORT)
					//					.show();	
					if (str2.equals("&000")) { // 
						Toast.makeText(getApplicationContext(), "领取任务成功！", Toast.LENGTH_SHORT)
						.show();
						// 初始化mission页面信息
						sendstr = "&54&30";
						sent(sendstr);
					}
					else if (str2.equals("&002")) { 
						Toast.makeText(getApplicationContext(), "连接错误！", Toast.LENGTH_SHORT)
						.show();		
					}
					else if (str2.equals("&003")) { 
						Toast.makeText(getApplicationContext(), "领取任务失败！", Toast.LENGTH_SHORT)
						.show();		
					}
					else if (str2.equals("&004")) { 
						Toast.makeText(getApplicationContext(), "信息不规范！", Toast.LENGTH_SHORT)
						.show();		
					}
				}	
			}
		};
		
		// 任务搜索	
		dosearch.setOnClickListener(new View.OnClickListener() {            
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//当点击按钮时,会获取编辑框中的数据,然后提交给线程
				int spiselect = spinner.getSelectedItemPosition();
				String cmd = String.valueOf(spiselect+30);
				sendstr = "&54&"+cmd;
				if(box_time.isChecked()||box_time2.isChecked()) sendstr += "&35";
				if(box_time.isChecked()){
					int month = sp_mon.getSelectedItemPosition()+1;
					if(month<10) sendstr += "&030"+String.valueOf(month);
					else sendstr += "&03"+String.valueOf(month);
					String time = edit_day.getText().toString();
						if(time.length()<2) 
							sendstr += "0"+String.valueOf(time);
						else 
							sendstr += String.valueOf(time);
				}
				if(box_time2.isChecked()){
					int month = sp_mon2.getSelectedItemPosition()+1;
					if(month<10) sendstr += "&100"+String.valueOf(month);
					else sendstr += "&10"+String.valueOf(month);
					String time = edit_day2.getText().toString();
					if(time.length()<2)
						sendstr += "0"+String.valueOf(time);
					else 
						sendstr += String.valueOf(time);
				}
				if(box_address.isChecked()||box_address2.isChecked()) sendstr += "&34";
				if(box_address.isChecked()){
					int pos_sec = sp_section.getSelectedItemPosition();
					if(pos_sec<10) sendstr += "&020"+String.valueOf(pos_sec);
					else sendstr += "&02"+String.valueOf(pos_sec);	
					int pos_add = sp_address.getSelectedItemPosition();
					if(pos_add<10) sendstr += "0"+String.valueOf(pos_add);
					else sendstr += String.valueOf(pos_add);
				}			
				if(box_address2.isChecked()){
					int pos_sec = sp_section2.getSelectedItemPosition();
					if(pos_sec<10) sendstr += "&100"+String.valueOf(pos_sec);
					else sendstr += "&10"+String.valueOf(pos_sec);	
					int pos_add = sp_address2.getSelectedItemPosition();
					if(pos_add<10) sendstr += "0"+String.valueOf(pos_add);
					else sendstr += String.valueOf(pos_add);
				}
				sent(sendstr);
			}
		});

		// 修改账户信息
		// 个人信息
		button_information.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Mine_MissionActivity.this,
						InformationActivity.class);
				startActivity(intent);
			}
		});
		// 密码
		button_password.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Mine_MissionActivity.this,
						PasswordActivity.class);
				startActivity(intent);
			}
		});
		// 地址
		button_address.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Mine_MissionActivity.this,
						AddressActivity.class);
				startActivity(intent);
			}
		});
		
		// 个人任务
		// 我发布的任务
		myrelease.setOnClickListener(new View.OnClickListener() {            
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//当点击按钮时,会获取编辑框中的数据,然后提交给线程
				Intent intent = new Intent(Mine_MissionActivity.this,
						ReleaseTaskActivity.class);
				startActivity(intent);			
			}
		});	
		// 我领取的任务
		myaccept.setOnClickListener(new View.OnClickListener() {            
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//当点击按钮时,会获取编辑框中的数据,然后提交给线程
				Intent intent = new Intent(Mine_MissionActivity.this,
						AdoptTaskActivity.class);
				startActivity(intent);	
			}
		});	
		// 发布任务
		release.setOnClickListener(new View.OnClickListener(){ 
			@Override
			public void onClick(View v){
				Intent intent = new Intent(Mine_MissionActivity.this,AReleaseTask.class);
				startActivity(intent);
			}
		});

	}

	// 信息初始化：获取用户信息和任务列表
	@Override
    protected void onResume() {
		// 初始化mine页面
		String sendstr = "&03&06" + Data_all.User_ID;
		sent(sendstr);
		// 初始化mission页面
		sendstr = "&54&30";
		sent(sendstr);
    	super.onResume();
    }
	
	// 控件初始化
	private void init() {
		// 从TabActivity上面获取放置Tab的TabHost
		tabhost = getTabHost();
		tabhost.addTab(tabhost
		// 创建新标签one
				.newTabSpec("one")
				// 设置标签标题
				.setIndicator("我的")
				// 设置该标签的布局内容
				.setContent(R.id.widget_layout_red));
		tabhost.addTab(tabhost.newTabSpec("two").setIndicator("搜索任务")
				.setContent(R.id.widget_layout_yellow));
		spinner = (Spinner) findViewById(R.id.spinner_condition);
		dosearch = (Button) findViewById(R.id.dosearch);
		lv_mission = (ListView) findViewById(R.id.listmissions);
		dosearch = (Button) findViewById(R.id.dosearch);

		// CheckBox方法
		box_address = (CheckBox) findViewById(R.id.checkBoxAdress);
		box_address.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				sp_section.setEnabled(isChecked);
				sp_address.setEnabled(isChecked);
			}
		});

		box_time = (CheckBox) findViewById(R.id.checkBoxTime);
		box_time.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				sp_mon.setEnabled(isChecked);
				edit_day.setEnabled(isChecked);
			}
		});

		box_address2 = (CheckBox) findViewById(R.id.checkBoxAdress2);
		box_address2.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				sp_section2.setEnabled(isChecked);
				sp_address2.setEnabled(isChecked);

			}
		});

		box_time2 = (CheckBox) findViewById(R.id.checkBoxTime2);
		box_time2.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				sp_mon2.setEnabled(isChecked);
				edit_day2.setEnabled(isChecked);
			}
		});

		// 按时间搜索的选项
		sp_mon = (Spinner) findViewById(R.id.spinner_TaskMonth);
		String months[] = getResources().getStringArray(R.array.mon);
		ArrayAdapter<String> adapter_mon = new ArrayAdapter<String>(
				Mine_MissionActivity.this,
				R.layout.small_spinner, months);
		sp_mon.setAdapter(adapter_mon);
		sp_mon.setEnabled(false);
		edit_day = (EditText) findViewById(R.id.Edit_TaskDay);
		edit_day.setEnabled(false);
		sp_mon = (Spinner) findViewById(R.id.spinner_TaskMonth);

		sp_mon2 = (Spinner) findViewById(R.id.spinner_TaskMonth2);
		sp_mon2.setAdapter(adapter_mon);
		sp_mon2.setEnabled(false);
		edit_day2 = (EditText) findViewById(R.id.Edit_TaskDay2);
		edit_day2.setEnabled(false);

		// 按地址搜索的选项
		sp_section = (Spinner) findViewById(R.id.mm_section);
		sp_section.setEnabled(false);
		ArrayAdapter<String> adaptersec = new ArrayAdapter<String>(this,
				R.layout.small_spinner, Data_all.Section);
		sp_section.setAdapter(adaptersec);
		sp_address = (Spinner) findViewById(R.id.mm_address);
		sp_address.setEnabled(false);
		sp_section
				.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
					@Override
					public void onItemSelected(AdapterView<?> parent,
							View view, int position, long id) {
						ArrayAdapter<String> adapter_1 = new ArrayAdapter<String>(
								Mine_MissionActivity.this,
								R.layout.small_spinner,
								Data_all.Address[position]);
						sp_address.setAdapter(adapter_1);
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
						// TODO Auto-generated method stub
					}
				});

		sp_section2 = (Spinner) findViewById(R.id.mm_section2);
		sp_section2.setEnabled(false);
		sp_section2.setAdapter(adaptersec);
		sp_address2 = (Spinner) findViewById(R.id.mm_address2);
		sp_address2.setEnabled(false);
		sp_section2
				.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
					@Override
					public void onItemSelected(AdapterView<?> parent,
							View view, int position, long id) {
						ArrayAdapter<String> adapter_2 = new ArrayAdapter<String>(
								Mine_MissionActivity.this,
								R.layout.small_spinner,
								Data_all.Address[position]);
						sp_address2.setAdapter(adapter_2);
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
						// TODO Auto-generated method stub
					}
				});

		text_username = (TextView) findViewById(R.id.text_username);
		text_realname = (TextView) findViewById(R.id.text_realname);
		text_tele = (TextView) findViewById(R.id.text_tele);
		text_school = (TextView) findViewById(R.id.text_school);
		text_email = (TextView) findViewById(R.id.text_email);
		text_coin = (TextView) findViewById(R.id.text_coin);
		text_credit = (TextView) findViewById(R.id.text_credit);

		// 按钮控件
		myrelease = (Button) findViewById(R.id.myrelease);
		myaccept = (Button) findViewById(R.id.myaccept);
		release = (Button) findViewById(R.id.torelease);

		button_information = (Button) findViewById(R.id.button_information);
		button_password = (Button) findViewById(R.id.button_password);
		button_address = (Button) findViewById(R.id.button_address);

		// 声明一个ArrayAdapter用于存放简单数据
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				Mine_MissionActivity.this,
				R.layout.small_spinner, getData());
		// 把定义好的Adapter设定到spinner中
		spinner.setAdapter(adapter);
		Showmissions(false, new Task());
	}
	
	// 排序类型的下拉菜单列表
	private List<String> getData() {
		List<String> dataList = new ArrayList<String>();
		dataList.add("默认排序");
		dataList.add("按金币排序");
		dataList.add("按信誉排序");
		return dataList;
	}

	// 用户信息初始化
	public void user_init(String str){
		Usr mine = new Usr();
		mine.Initial(str);
		username = mine.GetUsrName();
		realname = mine.GetRealName();
		tele = mine.GetTeleNumber();
		email = mine.GetEmail();
		school = mine.GetSchool();
		coin = mine.GetCoins();
		credit = mine.GetCredit();
		text_username.setText("    用户名        "+username);
		text_realname.setText("    姓名            "+realname);
		text_tele.setText("    手机            "+tele);
		text_school.setText("    学院            "+school);
		text_email.setText("    邮箱            "+email);
		text_coin.setText("    金币余额    "+String.valueOf(coin));
		text_credit.setText("    信用值        "+String.valueOf(credit));

	}
	
	// 显示任务列表
	private void Showmissions(boolean havem, final Task curtask) {
		String size_list[] = Data_all.Size;
		lv_mission.setAdapter(null);
		//		String mon_list[]=getResources().getStringArray(R.array.mon);
		ArrayList<String> missions = new ArrayList<String>();
		missions.add("大小   取货时间        交货时间       交货区域");
		if(havem){
			for(int i=0;i<curtask.GetTasklist().length;i++){
				Task.T curTask = curtask.GetTasklist()[i];
				
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
		if (missions.size() > 0) {
			ArrayAdapter<String> aa = new ArrayAdapter<String>(
					Mine_MissionActivity.this, R.layout.array_list_view, missions);
			// ListView自身就带有滚动条
			lv_mission.setAdapter(aa);
			lv_mission.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					if (arg2 != 0) {
						sent("&56&00" + curtask.GetTasklist()[arg2-1].TNO);
					}
				}
			});
		}

	}

	// 显示信息的对话框
	private void Showdialog(final Task curTask) { 
		String mon_list[]=getResources().getStringArray(R.array.mon);
		LayoutInflater factory=LayoutInflater.from(Mine_MissionActivity.this);
		final View v1=factory.inflate(R.layout.dialogview,null);
		AlertDialog.Builder dialog=new AlertDialog.Builder(Mine_MissionActivity.this);
		dialog.setTitle("任务详情");
		dialog.setView(v1);//设置使用View
		//设置控件应该用v1.findViewById 否则出错
		final TextView diatv = (TextView) v1.findViewById(R.id.updatetv);

		//		String str0 = Data_all.Size[curTask.Size];
		//		String str1 = String.valueOf(mon_list[curTask.In_Time[0]]);
		//		String str2 = String.valueOf(curTask.In_Time[1]);
		//		String str3 = Data_all.Section[curTask.Out_Address[0]];
		//		String str4 = Data_all.Address[curTask.Out_Address[0]][curTask.Out_Address[1]];

		String str = "物品类型："+Data_all.Size[curTask.GetSize()]+"\n取货时间："+String.valueOf(mon_list[curTask.GetInTime()[0]-1])
				+String.valueOf(curTask.GetInTime()[1])+"日,  从"+String.valueOf(curTask.GetInTime()[2])+"时~"+String.valueOf(curTask.GetInTime()[3])+"时"
				+"\n交货时间："+String.valueOf(mon_list[curTask.GetOutTime()[0]-1])
				+String.valueOf(curTask.GetOutTime()[1])+"日,  从"+String.valueOf(curTask.GetOutTime()[2])+"时~"+String.valueOf(curTask.GetOutTime()[3])+"时"
				+"\n取货地点："+Data_all.Section[curTask.GetInAddress()[0]]+Data_all.Address[curTask.GetInAddress()[0]][curTask.GetInAddress()[1]]
						+"\n交货地点："+Data_all.Section[curTask.GetOutAddress()[0]]+Data_all.Address[curTask.GetOutAddress()[0]][curTask.GetOutAddress()[1]];
		diatv.setText(str);
		
		/*		接受任务函数，发送字符串：      &55+任务ID
		需要服务器返回：是否办理成功*/
		dialog.setPositiveButton("接受任务",new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				String str = "&55&06"+Data_all.User_ID+"&00"+String.valueOf(curTask.GetTNO());
				sent(str);
				sent(sendstr);
			}
		});

		dialog.setNegativeButton("返回",new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				sent(sendstr);
			}
		});
		dialog.show();
	}

	// 显示提示信息
	public void showToast(String str) {
		Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT).show();
	}

	// 通过Service发送数据
	public void sent(String bs) {
		Intent intent = new Intent();// 创建Intent对象
		intent.setAction("android.intent.action.cmd");
		intent.putExtra("value", bs);
		sendBroadcast(intent);// 发送广播
	}
	
	// 接收service传来的信息
	private class MyReceiver extends BroadcastReceiver { 
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(
					"android.intent.action.Mine_MissionActivity")) {
				Bundle bundle = intent.getExtras();
				String str = bundle.getString("str");
				Message msg = new Message();
				msg.obj = str;
				mHandler.sendMessage(msg);
			}
		}
	}
}
