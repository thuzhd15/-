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
import android.widget.AdapterView.OnItemClickListener; 
//import android.widget.CompoundButton.OnCheckedChangeListener; 
import android.widget.Spinner; 
public class AddressActivity extends Activity{
	private String id;
	private Spinner sp_address_1, sp_address_2, sp_address_3;
	private TextView text_address_1,text_address_2, text_address_3, text_priority;
	private Button button_submit, button_backwards;
	private RadioButton radioButton_address_1, radioButton_address_2, radioButton_address_3;
	private RadioGroup radioGroup_address;
	private int address_1, address_2, address_3;
	private List<String> adresses;
	protected void onCreate(Bundle savedIntanceState){
		super.onCreate(savedIntanceState);
		setContentView(R.layout.activity_change);
		address_1 = address_2 = address_3 = null;
		Intent intent = this.getIntent();
		Bundle bundle = intent.getExtras();
		String s = bundle.getString("KEY");
		id = s;
		receiver = new MyReceiver(); // 注册广播 
		IntentFilter filter = new IntentFilter(); 
		filter.addAction("android.intent.action.MainActivity"); 
		registerReceiver(receiver, filter); 
		byte[] msgBuffer = null;
		String sendstr = "&03&06"+id;
		msgBuffer = sendstr.getBytes();
		connect();
		sent(msgBuffer);
		init();
		sp_address_1.setOnItemSelectedListener(new AdapterView..OnItemSelectedListener(){
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id){
				address_1 = position;
			}
		});
		sp_address_2.setOnItemSelectedListener(new AdapterView..OnItemSelectedListener(){
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id){
				address_2 = position;
			}
		});
		sp_address_3.setOnItemSelectedListener(new AdapterView..OnItemSelectedListener(){
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id){
				address_3 = position;
			}
		});
		radioGroup_address.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangedListener(){
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId){
				if(group.isPressed()){
					int a;
					switch (checkedId){
						case R.id.address_1:
							if(address_1 == 0)
								Toast.makeText(getApplicationContext(),"不能将空地址设为首地址",Toast.LENGTH_SHORT).show();
							else
								radioButton_address_1.setChecked(false);
						case R.id.address_2:
							if(address_2 == 0)
								Toast.makeText(getApplicationContext(),"不能将空地址设为首地址",Toast.LENGTH_SHORT).show();
							else{
								a = address_1;
								address_1 = address_2;
								address_2 = a;
								sp_address_1.setSelection(address_1);
								sp_address_2.setSelection(address_2);
								radioButton_address_2.setChecked(false);
							}
						case R.id.address_3:
							if(address_3 == 0)
								Toast.makeText(getApplicationContext(),"不能将空地址设为首地址",Toast.LENGTH_SHORT).show();
							else{
								a = address_1;
								address_1 = address_3;
								address_3 = a;
								sp_address_1.setSelection(address_1);
								sp_address_3.setSelection(address_3);
								radioButton_address_3.setChecked(false);
							}
					}
				}
			}
		});
		button_backwards.setOnClickListener(new View.OnClickListener(){
			@Override
			Bundle bundle = new Bundle();
			bundle.putString("KEY",id);
			Intent intent = new Intent(AddressActivity.this,ChangeActivity.class);
			intent.putExtras(bundle);
			startActivity(intent);
			finish();
		});
		button_submit.setOnClickListener(new View.OnClickListener(){
			@Override
			if(address_1 == 0 || address_2 == 0 || address_3 == 0)
				Toast.makeText(getApplicationContext(),"所有地址不能为空",Toast.LENGTH_SHORT).show();
			else{
				if(address_1 == 0)
					Toast.makeText(getApplicationContext(),"首地址不能为空",Toast.LENGTH_SHORT).show();
				else{
					byte[] msgBuffer = null;
					String sendstr = "&02"+"&06"+id+"&04"+String.valueOf(address_1)+"&08"+String.valueOf(address_2)+"&09"+String.valueOf(address_3);
					msgBuffer = sendstr.getBytes();
					sent(msgBuffer);
					Toast.makeText(getApplicationContext(),"修改成功",Toast.LENGTH_SHORT).show();
				}
			}
			
		});
	}
	public void init(){
		sp_address_1 = (Spinner) findViewById(R.id.sp_address_1);
		sp_address_2 = (Spinner) findViewById(R.id.sp_address_2);
		sp_address_3 = (Spinner) findViewById(R.id.sp_address_3);
		ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,address);  
		sp_address_1.setAdapter(adapter);
		sp_address_2.setAdapter(adapter);
		sp_address_3.setAdapter(adapter);
		sp_address_1.setSelection(address_1);
		sp_address_2.setSelection(address_2);
		sp_address_3.setSelection(address_3);
		radioButton_address_1 = (RadioButton) findViewById(R.id.rb_address_1);
		radioButton_address_1 = (RadioButton) findViewById(R.id.rb_address_2);
		radioButton_address_1 = (RadioButton) findViewById(R.id.rb_address_3);
		radioGroup_address = (RadioGroup) findViewById(R.id.address);
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
	private class MyReceiver extends BroadcastReceiver{
		@Override
		public void onReceive(Context context, Intent intent){
			if(intent.getAction().equals("android.intent.action.AddressActivity")){
				Bundle bundle = intent.getExtras();
				String str = bundle.getString("str");
				user = new Usr();
				user.str2(str);
				address = user.GetAddrList();
				address_1 = user.GetAddress1();
				address_2 = user.GetAddress2();
				address_3 = user.GetAddress3();
			}
		}
	}
		
}
