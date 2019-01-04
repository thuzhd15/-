package com.example.lazy_man_client;

import java.util.ArrayList;
//import java.util.List;
import android.app.Activity; 
import android.content.BroadcastReceiver; 
import android.content.Context; 
import android.content.Intent; 
import android.content.IntentFilter; 
import android.os.Bundle; 
import android.os.Handler;
//import android.os.Handler; 
//import android.os.IBinder; 
import android.os.Message; 
//import android.view.Menu; 
//import android.view.MenuItem; 
import android.view.View; 
import android.widget.AdapterView; 
import android.widget.ArrayAdapter; 
import android.widget.Button; 
//import android.widget.CheckBox; 
//import android.widget.CompoundButton; 
//import android.widget.EditText; 
//import android.widget.ListView; 
import android.widget.RadioButton; 
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
//import android.widget.TabHost; 
import android.widget.TextView; 
import android.widget.Toast; 
//import android.widget.ToggleButton; 
//import android.widget.AdapterView.OnItemClickListener; 
//import android.widget.CompoundButton.OnCheckedChangeListener; 
import android.widget.Spinner;

public class AddressActivity extends Activity{
	private String user_id;
	private Spinner sp_section_1, sp_section_2, sp_section_3, sp_address_1, sp_address_2, sp_address_3;
	private TextView text_address_1,text_address_2, text_address_3, text_priority;
	private Button button_submit, button_backwards;
	private RadioButton radioButton_address_1, radioButton_address_2, radioButton_address_3;
	private RadioGroup radioGroup_address;
	private int section_1, section_2, section_3, address_1, address_2, address_3;
	private String[][] addresses;
	private String[] sections;
	private Data_all data;
	int sign1,sign2,sign3;
	private MyReceiver receiver;
	private Handler mHandler;
	
	protected void onCreate(Bundle savedIntanceState){
		data = new Data_all();
		super.onCreate(savedIntanceState);
		setContentView(R.layout.activity_address);
		address_1 = address_2 = address_3 = 0;
		section_1 = section_2 = section_3 = 0;
		sign1 = sign2 = sign3 = 1;
		user_id = Data_all.User_ID;
		
		MyReceiver receiver = new MyReceiver(); // 注册广播 
		IntentFilter filter = new IntentFilter(); 
		filter.addAction("android.intent.action.AddressActivity"); 
		registerReceiver(receiver, filter);
		
		mHandler = new Handler() {
			public void handleMessage(android.os.Message msg) {
				String str = msg.obj.toString();
				String substr = str.substring(0, 3);
				
				if (substr.equals("&08")) { // 显示地址信息										
					showaddress(str);							
				} else if(substr.equals("&05")) {  // 修改信息是否成功					
					after_change(str);				
				}
			}
		};
		
		sections = data.Section;
		addresses = data.Address;
		
		init(); // 控件初始化
		
		// 信息初始化：获取用户信息
		String sendstr = "&08&06" + user_id;
		sent(sendstr);
		
		sp_section_1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id){
				if(sign1==1){
				section_1 = position;
				ArrayAdapter<String> adapter_1=new ArrayAdapter<String>(AddressActivity.this,R.layout.small_spinner,addresses[section_1]);
				sp_address_1.setAdapter(adapter_1);
				}
				else
					sign1 = 0;
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		sp_section_2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id){
				if(sign2 == 1){	
					section_2 = position;
					ArrayAdapter<String> adapter_2=new ArrayAdapter<String>(AddressActivity.this,R.layout.small_spinner,addresses[section_2]);
					sp_address_2.setAdapter(adapter_2);
				}
				else
					sign2 = 1;
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		sp_section_3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id){
				if(sign3 == 1){	
					section_3 = position;
					ArrayAdapter<String> adapter_3=new ArrayAdapter<String>(AddressActivity.this,R.layout.small_spinner,addresses[section_3]);
					sp_address_3.setAdapter(adapter_3);
				}
				else
					sign3 = 1;
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		sp_address_1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id){
					address_1 = position;
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		sp_address_2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id){
				address_2 = position;
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		sp_address_3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id){
					address_3 = position;
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		radioGroup_address.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId){
				//if(group.isPressed()){
					int a,b;
					switch (checkedId){
						case R.id.rb_address_1:
							if(section_1 ==0 || address_1 == 0)
								Toast.makeText(getApplicationContext(),"不能将空地址设为首地址",Toast.LENGTH_SHORT).show();
							break;
						case R.id.rb_address_2:
							if(section_2 == 0 || address_2 == 0)
								Toast.makeText(getApplicationContext(),"不能将空地址设为首地址",Toast.LENGTH_SHORT).show();
							else{
								a = section_1;
								b = address_1;
								section_1 = section_2;
								address_1 = address_2;
								section_2 = a;
								address_2 = b;
								sub_init(2);
								radioGroup_address.clearCheck();
							}
							break;
						case R.id.rb_address_3:
							if(section_3 == 0 || address_3 == 0)
								Toast.makeText(getApplicationContext(),"不能将空地址设为首地址",Toast.LENGTH_SHORT).show();
							else{
								a = section_1;
								b = address_1;
								section_1 = section_3;
								address_1 = address_3;
								section_3 = a;
								address_3 = b;
								sub_init(3);
								radioGroup_address.clearCheck();
							}
							break;
					}
				}
			//}
		});
		button_backwards.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
				finish();
			}
		});
		button_submit.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
				if(address_1 == 0)
					showToast("首地址不能为空！");
				else{
					String sendstr = "&05"+"&06"+user_id+"&04"+String.format("%02d",section_1)+String.format("%02d",address_1)
							+"&08"+String.format("%02d",section_2)+String.format("%02d",address_2)+"&09"+String.format("%02d",section_3)
							+String.format("%02d",address_3);
					sent(sendstr);
				}
			}
		});
	}
	
	public void sub_init(int i){
		sp_section_1.setSelection(section_1,true);
		ArrayAdapter<String> adapter_1=new ArrayAdapter<String>(this,R.layout.small_spinner,addresses[section_1]);
		sp_address_1.setAdapter(adapter_1);
		sp_address_1.setSelection(address_1,true);
		sign1 = 0;
		if(i == 2){
			sp_section_2.setSelection(section_2,true);
			ArrayAdapter<String> adapter_2=new ArrayAdapter<String>(this,R.layout.small_spinner,addresses[section_2]);
			sp_address_2.setAdapter(adapter_2);
			sp_address_2.setSelection(address_2,true);
			sign2 = 0;
		}
		else if(i == 3){
			sp_section_3.setSelection(section_3,true);
			ArrayAdapter<String> adapter_3=new ArrayAdapter<String>(this,R.layout.small_spinner,addresses[section_3]); 
			sp_address_3.setAdapter(adapter_3);
			sp_address_3.setSelection(address_3,true);
			sign3 = 0;
		}
	}
	
	// 控件初始化
	public void init(){
		sp_address_1 = (Spinner) findViewById(R.id.sp_address_1);
		sp_address_2 = (Spinner) findViewById(R.id.sp_address_2);
		sp_address_3 = (Spinner) findViewById(R.id.sp_address_3);
		sp_section_1 = (Spinner) findViewById(R.id.sp_section_1);
		sp_section_2 = (Spinner) findViewById(R.id.sp_section_2);
		sp_section_3 = (Spinner) findViewById(R.id.sp_section_3);
		
		ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,R.layout.small_spinner,sections);
		sp_section_1.setAdapter(adapter);
		sp_section_2.setAdapter(adapter);
		sp_section_3.setAdapter(adapter);
		
		ArrayAdapter<String> adapter_1=new ArrayAdapter<String>(this,R.layout.small_spinner,addresses[section_1]);
		sp_address_1.setAdapter(adapter_1);
		ArrayAdapter<String> adapter_2=new ArrayAdapter<String>(this,R.layout.small_spinner,addresses[section_2]);
		sp_address_2.setAdapter(adapter_2);		
		ArrayAdapter<String> adapter_3=new ArrayAdapter<String>(this,R.layout.small_spinner,addresses[section_3]); 
		sp_address_3.setAdapter(adapter_3);
		
		radioGroup_address = (RadioGroup) findViewById(R.id.radioGroup_priority);
		radioButton_address_1 = (RadioButton) findViewById(R.id.rb_address_1);
		radioButton_address_1 = (RadioButton) findViewById(R.id.rb_address_2);
		radioButton_address_1 = (RadioButton) findViewById(R.id.rb_address_3);
		text_address_1 = (TextView) findViewById(R.id.text_address_1);
		text_address_2 = (TextView) findViewById(R.id.text_address_2);
		text_address_3 = (TextView) findViewById(R.id.text_address_3);
		text_priority = (TextView) findViewById(R.id.text_priority);
		button_backwards = (Button) findViewById(R.id.button_backwards);
		button_submit = (Button) findViewById(R.id.button_submit);
	}
	
	// 显示用户地址
	public void showaddress(String str) {
		Usr user = new Usr();
		user.Initial(str);
		
		int sign = user.GetErrorType();
		switch (sign) {
		case 0:
			section_1 = user.GetAddress1()[0];
			section_2 = user.GetAddress2()[0];
			section_3 = user.GetAddress3()[0];
			address_1 = user.GetAddress1()[1];
			address_2 = user.GetAddress2()[1];
			address_3 = user.GetAddress3()[1];
			
			sp_section_1.setSelection(section_1,true);
			sp_section_2.setSelection(section_2,true);
			sp_section_3.setSelection(section_3,true);
			
			// 在此重新设置第二个下拉菜单的列表项！
			ArrayAdapter<String> adapter_1=new ArrayAdapter<String>(this,R.layout.small_spinner,addresses[section_1]);
			sp_address_1.setAdapter(adapter_1);
			ArrayAdapter<String> adapter_2=new ArrayAdapter<String>(this,R.layout.small_spinner,addresses[section_2]);
			sp_address_2.setAdapter(adapter_2);		
			ArrayAdapter<String> adapter_3=new ArrayAdapter<String>(this,R.layout.small_spinner,addresses[section_3]); 
			sp_address_3.setAdapter(adapter_3);
			
			sp_address_1.setSelection(address_1,true);
			sp_address_2.setSelection(address_2,true);
			sp_address_3.setSelection(address_3,true);
			
			break;
		case 2:
			showToast("连接错误！");
			break;
		case 3:
			showToast("获取用户信息失败！");
			break;
		case 5:
			showToast("用户不存在！");
			break;
		}
	}

	// 提交更改后的反馈
	// 处理修改后的反馈信息
	public void after_change(String str) {
		Usr user = new Usr();
		user.Initial(str);
		int sign = user.GetErrorType();
		switch (sign) {
		case 0:
			showToast("修改地址信息成功！");
			break;
		case 2:
			showToast("连接错误！");
			break;
		case 3:
			showToast("修改地址信息失败！");
			break;
		case 4:
			showToast("信息不规范！");
			break;
		}
	}
	
	// 通过Service发送数据
	public void sent(String bs) {
		Intent intent = new Intent();// 创建Intent对象
		intent.setAction("android.intent.action.cmd");
		intent.putExtra("value", bs);
		sendBroadcast(intent);// 发送广播
	}

	private class MyReceiver extends BroadcastReceiver{
		@Override
		public void onReceive(Context context, Intent intent){
			if(intent.getAction().equals("android.intent.action.AddressActivity")){
				Bundle bundle = intent.getExtras();
				String str = bundle.getString("str");
				Message msg = new Message();
				msg.obj = str;
				mHandler.sendMessage(msg);
			}
		}
	}
	
	public void showToast(String str) {// 显示提示信息
		Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT).show();
	}
}
