package clientinterface; 
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
public class InformationActivity extends Activity{
	private String username, realname, tele, email, school;
	private TextView text_username, text_realname, text_tele, text_email, text_school;
	private EditText edit_username, edit_realname, edit_tele, edit_email, edit_school;
	private Button submit, backwards;
	MyReceiver receiver;
	protected void onCreate(Bundle savedIntanceState){
		super.onCreate(savedIntanceState);
		setContentView(R.layout.activity_change);
		username = realname = tele = email = school = null;
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
		button_backwards.setOnClickListener(new View.OnClickListener(){
			@Override
			Bundle bundle = new Bundle();
			bundle.putString("KEY",id);
			Intent intent = new Intent(InformationActivity.this,ChangeActivity.class);
			intent.putExtras(bundle);
			startActivity(intent);
			finish();
		});
		button_submit.setOnClickListener(new View.OnClickListener(){
			@Override
			username = edit_username.getText();
			realname = edit_realname.getText();
			tele = edit_tele.getText();
			email = edit_email.getText();
			school = edit_school.getText();
			//schoolid = edit_schoolid.getText();
			if(n_username == null || n_realname == null || n_tele == null || n_email == null || n_school == null )
				Toast.makeText(getApplicationContext(),"信息不能为空",Toast.LENGTH_SHORT).show();
			else{
				byte[] msgBuffer = null;
				String sendstr = "&02"+"&06"+id+"&00"+username+"&02"+tele+"&03"+email+"&05"+realname+"&07"+school;
				msgBuffer = sendstr.getBytes();
				sent(msgBuffer);
				Toast.makeText(getApplicationContext(),"修改成功",Toast.LENGTH_SHORT).show();
		});
	}
	public void init(){
		text_username = (TextView) findViewById(R.id.text_username);
		text_realname = (TextView) findViewById(R.id.text_realname);
		text_tele = (TextView) findViewById(R.id.text_tele);
		text_school = (TextView) findViewById(R.id.text_school);
		text_schoolid = (TextView) findViewById(R.id.text_schoolid);
		text_email = (TextView) findViewById(R.id.text_email);
		edit_username = (EditText) findViewById(R.id.edit_username);
		edit_realname = (EditText) findViewById(R.id.edit_realname);
		edit_tele = (EditText) findViewById(R.id.edit_tele);
		edit_email = (EditText) findViewById(R.id.edit_email);
		edit_school = (EditText) findViewById(R.id.edit_school);
		//edit_schoolid = (EditText) findViewById(R.id.schoolid);
		edit_username.setText(username);
		edit_realname.setText(realname);
		edit_tele.setText(tele);
		edit_email.setText(email);
		edit_school.setText(school);
		//edit_schoolid.setText(schoolid);
		button_backwards = (Button) findViewById(R.id.button_backwards);
		button_submit = (Button) findViewById(R.id.button_submit);
		connect();
	}
	public void connect(){
		Intent intent = new Intent(PasswordActivity.this,NetService.class);
		startService(intent);
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
			if(intent.getAction().equals("android.intent.action.PasswordActivity")){
				Bundle bundle = intent.getExtras();
				String str = bundle.getString("str");
				user = new Usr();
				user.str2(str);
				username = user.GetUsrName();
				realname = user.GetRealName();
				tele = user.GetTeleNumber();
				email = user.GetEmail();
				school = user.GetSchool();
				schoolid = user.GetSchoolID();
			}
		}
	}
	
}
