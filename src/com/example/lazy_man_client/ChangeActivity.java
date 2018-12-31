package com.example.lazy_man_client;
import android.app.Activity; 
//import android.app.PendingIntent; 
import android.content.BroadcastReceiver; 
import android.content.Context; 
import android.content.Intent; 
import android.content.IntentFilter; 
import android.os.Bundle; 
import android.os.Handler; 
import android.os.Message; 
import android.view.View; 
import android.widget.Button; 
import android.widget.EditText; 
import android.widget.TextView; 
import android.widget.Toast; 
public class ChangeActivity extends Activity{
	private Button button_information, button_password, button_address, button_backwards;
	private String id;
	protected void onCreate(Bundle savedIntanceState){
		super.onCreate(savedIntanceState);
		setContentView(R.layout.activity_change);
		Intent intent = this.getIntent();
		Bundle bundle = intent.getExtras();
		String s = bundle.getString("KEY");
		id = s;
		init();
		button_backwards.setOnClickListener(new View.OnClickListener(){
				@Override
				public void onClick(View v){
					Bundle bundle = new Bundle();
					bundle.putString("KEY",id);
					Intent intent = new Intent(ChangeActivity.this,Mine_MissionActivity.class);
					intent.putExtras(bundle);
					startActivity(intent);
					finish();
				}
		});
		button_information.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
				Bundle bundle = new Bundle();
				bundle.putString("KEY",id);
				Intent intent = new Intent(ChangeActivity.this,InformationActivity.class);
				intent.putExtras(bundle);
				startActivity(intent);
				finish();
			}
		});
		button_password.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
				Bundle bundle = new Bundle();
				bundle.putString("KEY",id);
				Intent intent = new Intent(ChangeActivity.this,PasswordActivity.class);
				intent.putExtras(bundle);
				startActivity(intent);
				finish();
			}
		});
		button_address.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
				Bundle bundle = new Bundle();
				bundle.putString("KEY",id);
				Intent intent = new Intent(ChangeActivity.this,AddressActivity.class);
				intent.putExtras(bundle);
				startActivity(intent);
				finish();
			}
		});
	}
	public void init(){
		button_information = (Button) findViewById(R.id.button_information);
		button_password = (Button) findViewById(R.id.button_password);
		button_address = (Button) findViewById(R.id.button_address);
		button_backwards = (Button) findViewById(R.id.button_backwards);
	}
}
