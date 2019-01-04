//注册界面

package com.example.lazy_man_client;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class RegistActivity extends Activity {
	private Button button_regist;
	//	private int stateflag = 0;  // 0:wait   1:OK   -1:fail
	private EditText username, stdnum, mail, phonenum, rname, key, key_R, depinfo;
	private MyReceiver receiver;
	private Spinner sp_section, sp_address, sp_section2, sp_address2,sp_section3, sp_address3;

	private Handler mHandler;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_regist);
		username = (EditText) findViewById(R.id.edit_Rusername);
		stdnum = (EditText) findViewById(R.id.edit_Rstdnumber);
		mail = (EditText) findViewById(R.id.edit_Rmailbox);
		phonenum = (EditText) findViewById(R.id.edit_Rphonenumber);
		rname = (EditText) findViewById(R.id.edit_Rrealname);
		key = (EditText) findViewById(R.id.edit_Rkey);
		key_R = (EditText) findViewById(R.id.edit_Rkeycomfirm);
		depinfo = (EditText) findViewById(R.id.edit_Rdepinfo);
		button_regist = (Button) findViewById(R.id.button_Rregist);
		
		 // 注册广播
		receiver = new MyReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction("android.intent.action.RegistActivity");
		registerReceiver(receiver, filter);

		//地址
		sp_section = (Spinner) findViewById(R.id.sp_address1_1);
		ArrayAdapter<String> adaptersec=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,Data_all.Section);
		sp_section.setAdapter(adaptersec);
		sp_address = (Spinner) findViewById(R.id.sp_address1_2);
		sp_section.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id){
				ArrayAdapter<String> adapter_1=new ArrayAdapter<String>(RegistActivity.this,android.R.layout.simple_spinner_item,Data_all.Address[position]);
				sp_address.setAdapter(adapter_1);
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub			
			}
		});		

		sp_section2 = (Spinner) findViewById(R.id.sp_address2_1);
		sp_section2.setAdapter(adaptersec);
		sp_address2 = (Spinner) findViewById(R.id.sp_address2_2);
		sp_section2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id){
				ArrayAdapter<String> adapter_2=new ArrayAdapter<String>(RegistActivity.this,android.R.layout.simple_spinner_item,Data_all.Address[position]);
				sp_address2.setAdapter(adapter_2);
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub			
			}
		});	

		sp_section3 = (Spinner) findViewById(R.id.sp_address3_1);
		sp_section3.setAdapter(adaptersec);
		sp_address3 = (Spinner) findViewById(R.id.sp_address3_2);
		sp_section3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id){
				ArrayAdapter<String> adapter_3=new ArrayAdapter<String>(RegistActivity.this,android.R.layout.simple_spinner_item,Data_all.Address[position]);
				sp_address3.setAdapter(adapter_3);
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub			
			}
		});

		// 测试用例		
		/*username.setText("呵呵呵");
		stdnum.setText("2015010783");
		mail.setText("zguidbaiu@ndviubfiu.com");
		phonenum.setText("15236945783");
		rname.setText("张国旺");
		key.setText("123456789");
		key_R.setText("123456789");
		depinfo.setText("qichexi");*/
		username.setText("");
		stdnum.setText("");
		mail.setText("");
		phonenum.setText("");
		rname.setText("");
		key.setText("");
		key_R.setText("");
		depinfo.setText("");

		/*
		 * 注册,若成功则返回登录页面 发送注册信息
		 * 示例：注册用户名“李四”，学工号2015000123，邮箱sss@tsinghua.educn，手机号00001111222，姓名 李思思，密码20482048，地址 紫荆8#110B ，院系信息 呵呵系，地址2 紫荆8#120B 
		 * 发送字符     &00&00李四&062015000123&03sss@tsinghua.educn&020000111122&05李思思&0120482048&04紫荆8#110B&07呵呵系&08紫荆8#120B
		 * 
		 * 需要服务器反馈：用户是否已经存在，即是否成功注册
		 */
		button_regist.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {


				// TODO Auto-generated method stub
				// 当点击按钮时,会获取编辑框中的数据,然后提交给线程
				if(stdnum.getText().toString().length()!=10){
					Toast.makeText(getApplicationContext(),"学工号需要是10位", Toast.LENGTH_SHORT)
					.show();
				}
				else if(phonenum.getText().toString().length()!=11){
					Toast.makeText(getApplicationContext(),"电话号码需要是11位", Toast.LENGTH_SHORT)
					.show();
				}	
				else if(username.getText().toString().length()<1||username.getText().toString().length()>20){
					Toast.makeText(getApplicationContext(),"用户名不能为空，且长度小于20", Toast.LENGTH_SHORT)
					.show();
				}	
				else if(key.getText().toString().length()<6||key.getText().toString().length()>20){
					Toast.makeText(getApplicationContext(),"密码为6到20位", Toast.LENGTH_SHORT)
					.show();
				}	
				else if(mail.getText().toString().length()<1||mail.getText().toString().length()>50){
					Toast.makeText(getApplicationContext(),"邮箱长度1到50位", Toast.LENGTH_SHORT)
					.show();
				}	
				else if(rname.getText().toString().length()<1||rname.getText().toString().length()>20){
					Toast.makeText(getApplicationContext(),"姓名长度1到20位", Toast.LENGTH_SHORT)
					.show();
				}
				else if(rname.getText().toString().length()>50){
					Toast.makeText(getApplicationContext(),"院系描述长度应小于50", Toast.LENGTH_SHORT)
					.show();
				}
				else{
					if(key_R.getText().toString().equals(key.getText().toString())){
						String str="&00&00"+username.getText().toString()
								+"&06"+stdnum.getText().toString()
								+"&03"+mail.getText().toString()
								+"&02"+phonenum.getText().toString()
								+"&05"+rname.getText().toString()
								+"&01"+key.getText().toString()
								+"&07"+depinfo.getText().toString();					
						int address[][] = new int[3][2];
						address[0][0] = sp_section.getSelectedItemPosition(); address[0][1] = sp_address.getSelectedItemPosition();
						address[1][0] = sp_section2.getSelectedItemPosition(); address[1][1] = sp_address2.getSelectedItemPosition();
						address[2][0] = sp_section3.getSelectedItemPosition(); address[2][1] = sp_address3.getSelectedItemPosition();
						str += "&04";
						if(address[0][0]==0||address[0][1]==0) {
							Toast.makeText(getApplicationContext(),"首个地址不能为空", Toast.LENGTH_SHORT)
							.show();}
						else{
							if(address[1][1]==-1) address[1][1]++;
							if(address[2][1]==-1) address[2][1]++;
							for(int i=0;i<3;i++){
								if(i>0) str+= "&0"+String.valueOf(i+7);
								for(int j=0;j<2;j++){
									if(address[i][j]<10) str += "0"+String.valueOf(address[i][j]);
									else str += String.valueOf(address[i][j]);	}	
							}

							//						Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT)
							//						.show();
							sent(str);
							button_regist.setEnabled(false);}
					}
					else{
						Toast.makeText(getApplicationContext(), "两次输入密码不一致，请再次确认^-^", Toast.LENGTH_SHORT)
						.show();
					}
				}
			}
		});
		mHandler = new Handler() {
			public void handleMessage(android.os.Message msg) {
				//				Toast.makeText(getApplicationContext(), "handle"+str, Toast.LENGTH_SHORT)
				//				.show();	
				String strall = msg.obj.toString();
				String str = strall.substring(3, 7);
				if (str.equals("&000")) { // 成功
					button_regist.setEnabled(true);
					Toast.makeText(getApplicationContext(), "注册成功", Toast.LENGTH_SHORT)
					.show();
					Intent intent = new Intent(RegistActivity.this,
							MainActivity.class);
					startActivity(intent);
					finish();
				}
				else if (str.equals("&001")) { 
					button_regist.setEnabled(true);
					Toast.makeText(getApplicationContext(), "指令错误", Toast.LENGTH_SHORT)
					.show();		
				}
				else if (str.equals("&002")) { 
					button_regist.setEnabled(true);
					Toast.makeText(getApplicationContext(), "连接数据库失败", Toast.LENGTH_SHORT)
					.show();		
				}
				else if (str.equals("&003")) { 
					button_regist.setEnabled(true);
					Toast.makeText(getApplicationContext(), "获取或更改数据失败", Toast.LENGTH_SHORT)
					.show();		
				}
				else if (str.equals("&004")) { 
					button_regist.setEnabled(true);
					Toast.makeText(getApplicationContext(), "客户端提供数据不足或不符合规范", Toast.LENGTH_SHORT)
					.show();		
				}
				else if (str.equals("&005")) { 
					button_regist.setEnabled(true);
					Toast.makeText(getApplicationContext(), "用户名或学工号已被注册", Toast.LENGTH_SHORT)
					.show();		
				}
			};
		};
	}

	public void sent(String bs) { // 通过Service发送数据
		Intent intent = new Intent();// 创建Intent对象
		intent.setAction("android.intent.action.cmd");
		intent.putExtra("value", bs);
		sendBroadcast(intent);// 发送广播
	}

	private class MyReceiver extends BroadcastReceiver { // 接收service传来的信息
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			if (intent.getAction().equals("android.intent.action.RegistActivity")) {
				Bundle bundle = intent.getExtras();
				String str = bundle.getString("str");
				Message msg = new Message();
				msg.obj = str;
				mHandler.sendMessage(msg);
			}
		}
	}
}
