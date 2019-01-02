//ע�����

package com.example.lazy_man_client;

import android.support.v7.app.ActionBarActivity;
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

public class RegistActivity extends ActionBarActivity {
	private Button button_regist;
	//	private int stateflag = 0;  // 0:wait   1:OK   -1:fail
	private EditText username, stdnum, mail, phonenum, rname, key, key_R, add,
	depinfo, add2;
	private MyReceiver receiver;
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
		add = (EditText) findViewById(R.id.edit_Raddress);
		depinfo = (EditText) findViewById(R.id.edit_Rdepinfo);
		add2 = (EditText) findViewById(R.id.edit_Raddress2);
		button_regist = (Button) findViewById(R.id.button_Rregist);
		receiver = new MyReceiver(); // ע��㲥
		IntentFilter filter = new IntentFilter();
		filter.addAction("android.intent.action.RegistActivity");
		registerReceiver(receiver, filter);
		
		
// ��������		
		username.setText("�ǺǺ�");
		stdnum.setText("2015010783");
		mail.setText("zguidbaiu@ndviubfiu.com");
		phonenum.setText("15236945783");
		rname.setText("�Ź���");
		key.setText("123456789");
		key_R.setText("123456789");
		add.setText("0106");
		depinfo.setText("qichexi");
		add2.setText("0105");
				
		/*
		 * ע��,���ɹ��򷵻ص�¼ҳ�� ����ע����Ϣ
		 * ʾ����ע���û��������ġ���ѧ����2015000123������sss@tsinghua.educn���ֻ���00001111222������ ��˼˼������20482048����ַ �Ͼ�8#110B ��Ժϵ��Ϣ �Ǻ�ϵ����ַ2 �Ͼ�8#120B 
		 * �����ַ�     &00&00����&062015000123&03sss@tsinghua.educn&020000111122&05��˼˼&0120482048&04�Ͼ�8#110B&07�Ǻ�ϵ&08�Ͼ�8#120B
		 * 
		 * ��Ҫ�������������û��Ƿ��Ѿ����ڣ����Ƿ�ɹ�ע��
		 */
		button_regist.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
								
				// TODO Auto-generated method stub
				// �������ťʱ,���ȡ�༭���е�����,Ȼ���ύ���߳�
				if(key_R.getText().toString().equals(key.getText().toString())){
					String str="&00&00"+username.getText().toString()
							+"&06"+stdnum.getText().toString()
							+"&03"+mail.getText().toString()
							+"&02"+phonenum.getText().toString()
							+"&05"+rname.getText().toString()
							+"&01"+key.getText().toString()
							+"&04"+add.getText().toString()
							+"&07"+depinfo.getText().toString()
							+"&08"+add2.getText().toString();
					sent(str);
					button_regist.setEnabled(false);
				}
				else{
					Toast.makeText(getApplicationContext(), "�����������벻һ�£����ٴ�ȷ��^-^", Toast.LENGTH_SHORT)
					.show();
				}
			}
		});
		mHandler = new Handler() {
			public void handleMessage(android.os.Message msg) {
				//				Toast.makeText(getApplicationContext(), "handle"+str, Toast.LENGTH_SHORT)
				//				.show();	
				String strall = msg.obj.toString();
				String str = strall.substring(3, 7);
				if (str.equals("&000")) { // �ɹ�
					button_regist.setEnabled(true);
					Toast.makeText(getApplicationContext(), "ע��ɹ�", Toast.LENGTH_SHORT)
					.show();
					Intent intent = new Intent(RegistActivity.this,
							MainActivity.class);
					startActivity(intent);
					finish();
				}
				else if (str.equals("&001")) { 
					button_regist.setEnabled(true);
					Toast.makeText(getApplicationContext(), "ָ�����", Toast.LENGTH_SHORT)
					.show();		
				}
				else if (str.equals("&002")) { 
					button_regist.setEnabled(true);
					Toast.makeText(getApplicationContext(), "�������ݿ�ʧ�ܣ�", Toast.LENGTH_SHORT)
					.show();		
				}
				else if (str.equals("&003")) { 
					button_regist.setEnabled(true);
					Toast.makeText(getApplicationContext(), "��ȡ����ʧ�ܣ�", Toast.LENGTH_SHORT)
					.show();		
				}
				else if (str.equals("&004")) { 
					button_regist.setEnabled(true);
					Toast.makeText(getApplicationContext(), "��Ϣ���淶��", Toast.LENGTH_SHORT)
					.show();		
				}
				else if (str.equals("&005")) { 
					button_regist.setEnabled(true);
					Toast.makeText(getApplicationContext(), "�û�����ѧ�����ѱ�ע�ᣡ", Toast.LENGTH_SHORT)
					.show();		
				}
			};
		};
	}

	public void sent(String bs) { // ͨ��Service��������
		Intent intent = new Intent();// ����Intent����
		intent.setAction("android.intent.action.cmd");
		intent.putExtra("value", bs);
		sendBroadcast(intent);// ���͹㲥
	}

	private class MyReceiver extends BroadcastReceiver { // ����service��������Ϣ
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
