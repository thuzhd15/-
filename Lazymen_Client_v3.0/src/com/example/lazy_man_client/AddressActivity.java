package com.example.lazy_man_client;

import java.util.ArrayList;
//import java.util.List; 
import android.app.Activity; 
import android.content.BroadcastReceiver; 
import android.content.Context; 
import android.content.Intent; 
import android.content.IntentFilter; 
import android.os.Bundle; 
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
	private String id;
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
	protected void onCreate(Bundle savedIntanceState){
		data = new Data_all();
		super.onCreate(savedIntanceState);
		setContentView(R.layout.activity_address);
		address_1 = address_2 = address_3 = 0;
		section_1 = section_2 = section_3 = 0;
		Intent intent = this.getIntent();
		Bundle bundle = intent.getExtras();
		String s = bundle.getString("KEY");
		sign1 = sign2 = sign3 = 1;
		id = s;
		MyReceiver receiver = new MyReceiver(); // 注册广播 
		IntentFilter filter = new IntentFilter(); 
		filter.addAction("android.intent.action.MainActivity"); 
		registerReceiver(receiver, filter); 
		byte[] msgBuffer = null;
		String sendstr = "&03&06"+id;
		msgBuffer = sendstr.getBytes();
		connect();
		sent(msgBuffer);
		sections = data.Section;
		addresses = data.Address;
		init();
		sp_section_1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id){
				if(sign1==1){
				section_1 = position;
				ArrayAdapter<String> adapter_1=new ArrayAdapter<String>(AddressActivity.this,android.R.layout.simple_spinner_item,addresses[section_1]);
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
					ArrayAdapter<String> adapter_2=new ArrayAdapter<String>(AddressActivity.this,android.R.layout.simple_spinner_item,addresses[section_2]);
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
					ArrayAdapter<String> adapter_3=new ArrayAdapter<String>(AddressActivity.this,android.R.layout.simple_spinner_item,addresses[section_3]);
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
				Bundle bundle = new Bundle();
				bundle.putString("KEY",id);
				Intent intent = new Intent(AddressActivity.this,ChangeActivity.class);
				intent.putExtras(bundle);
				startActivity(intent);
				finish();
			}
		});
		button_submit.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
				if(address_1 == 0)
					Toast.makeText(getApplicationContext(),"首地址不能为空",Toast.LENGTH_SHORT).show();
				else{
					byte[] msgBuffer = null;
					String sendstr = "&02"+"&06"+id+"&04"+String.valueOf(section_1)+String.valueOf(address_1)+"&08"+String.valueOf(section_2)+String.valueOf(address_2)+"&09"+String.valueOf(section_3)+String.valueOf(address_3);
					msgBuffer = sendstr.getBytes();
					sent(msgBuffer);
				}
			}
		});
	}
	public void sub_init(int i){
		sp_section_1.setSelection(section_1,true);
		ArrayAdapter<String> adapter_1=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,addresses[section_1]);
		sp_address_1.setAdapter(adapter_1);
		sp_address_1.setSelection(address_1,true);
		sign1 = 0;
		if(i == 2){
			sp_section_2.setSelection(section_2,true);
			ArrayAdapter<String> adapter_2=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,addresses[section_2]);
			sp_address_2.setAdapter(adapter_2);
			sp_address_2.setSelection(address_2,true);
			sign2 = 0;
		}
		else if(i == 3){
			sp_section_3.setSelection(section_3,true);
			ArrayAdapter<String> adapter_3=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,addresses[section_3]); 
			sp_address_3.setAdapter(adapter_3);
			sp_address_3.setSelection(address_3,true);
			sign3 = 0;
		}
	}
	public void init(){
		sp_address_1 = (Spinner) findViewById(R.id.sp_address_1);
		sp_address_2 = (Spinner) findViewById(R.id.sp_address_2);
		sp_address_3 = (Spinner) findViewById(R.id.sp_address_3);
		sp_section_1 = (Spinner) findViewById(R.id.sp_section_1);
		sp_section_2 = (Spinner) findViewById(R.id.sp_section_2);
		sp_section_3 = (Spinner) findViewById(R.id.sp_section_3);
		ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,sections);
		sp_section_1.setAdapter(adapter);
		sp_section_2.setAdapter(adapter);
		sp_section_3.setAdapter(adapter);
		sp_section_1.setSelection(section_1,true);
		sp_section_2.setSelection(section_2,true);
		sp_section_3.setSelection(section_3,true);
		ArrayAdapter<String> adapter_1=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,addresses[section_1]);
		sp_address_1.setAdapter(adapter_1);
		sp_address_1.setSelection(address_1,true);
		ArrayAdapter<String> adapter_2=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,addresses[section_2]);
		sp_address_2.setAdapter(adapter_2);
		sp_address_2.setSelection(address_2,true);
		ArrayAdapter<String> adapter_3=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,addresses[section_3]); 
		sp_address_3.setAdapter(adapter_3);
		sp_address_3.setSelection(address_3,true);
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
		connect();
	}
	public void sent(byte[] bs){
		Intent intent = new Intent();
		intent.setAction("android.intent.action.cmd");
		intent.putExtra("value",bs);
		sendBroadcast(intent);
	}
	public void connect(){
		Intent intent = new Intent(AddressActivity.this,NetService.class);
		startService(intent);
	}
	private class MyReceiver extends BroadcastReceiver{
		@Override
		public void onReceive(Context context, Intent intent){
			if(intent.getAction().equals("android.intent.action.AddressActivity")){
				Bundle bundle = intent.getExtras();
				String str = bundle.getString("str");
				Usr user = new Usr();
				user.Initial(str);
				if(user.GetUsrID() != ""){
					section_1 = user.GetAddress1()[0];
					section_2 = user.GetAddress2()[0];
					section_3 = user.GetAddress3()[0];
					address_1 = user.GetAddress1()[1];
					address_2 = user.GetAddress2()[1];
					address_3 = user.GetAddress3()[1];
				}
				else{
					if(user.GetErrorType() == 0)
						Toast.makeText(getApplicationContext(),"修改成功",Toast.LENGTH_SHORT).show();
					else{
						int sign = user.GetErrorType();
						switch(sign){
							case 2:
								Toast.makeText(getApplicationContext(),"网络错误",Toast.LENGTH_SHORT).show();
								break;
							case 3:
								Toast.makeText(getApplicationContext(),"修改失败",Toast.LENGTH_SHORT).show();
								break;
							case 4:
								Toast.makeText(getApplicationContext(),"修改失败",Toast.LENGTH_SHORT).show();
								break;
						}
					}
					
				}
			}
		}
	}
		
}
