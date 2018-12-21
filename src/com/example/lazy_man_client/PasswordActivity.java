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
public class PasswordActivity extends Activity{
	private Button button_backwards, button_submit;
	private TextView text_oldpass, text_newpass_1, text_newpass_2;
	private EditText edit_oldpass, edit_newpass_1, edit_newpass_2;
	private String id, oldpass, newpass1, newpass2;
	private String old_pass;
	MyReceiver receiver;
	protected void onCreate(Bundle savedIntanceState){
		super.onCreate(savedIntanceState);
		setContentView(R.layout.activity_change);
		oldpass = newpass1 = newpass2 = null;
		Intent intent = this.getIntent();
		Bundle bundle = intent.getExtras();
		String s = bundle.getString("KEY");
		id = s;
		init();
		receiver = new MyReceiver();
		IntentFilter filter = new IntentFilter(); 
		filter.addAction("android.intent.action.PasswordActivity"); 
		registerReceiver(receiver, filter); 
		byte[] msgBuffer = null;
		String sendstr = "&03&06&"+id;
		msgBuffer = sendstr.getBytes();
		send(msgBuffer);
		button_backwards.setOnClickListener(new View.OnClickListener(){
			@Override
			Bundle bundle = new Bundle();
			bundle.putString("KEY",id);
			Intent intent = new Intent(PasswordActivity.this,ChangeActivity.class);
			intent.putExtras(bundle);
			startActivity(intent);
			finish();
		});
		button_submit.setOnClickListener(new View.OnClickListener(){
			@Override
			oldpass = edit_oldpass.getText();
			if(old_pass == oldpass){
				newpass1 = edit_newpass_1.getText();
				newpass2 = edit_newpass_2.getText();
				if(newpass1 == null)
					Toast.makeText(getApplicationContext(),"新密码不能为空",Toast.LENGTH_SHORT).show();
				else{
					if(newpass1 != newpass2)
						Toast.makeText(getApplicationContext(),"两次新密码不同",Toast.LENGTH_SHORT).show();
					else{
						byte[] msgBuffer = null;
						String sendstr = "&02"+"&06"+id+"&01"+newpass;
						msgBuffer = sendstr.getBytes();
						sent(msgBuffer);
					}
				}
			}
			else
				Toast.makeText(getApplicationContext(),"旧密码不正确",Toast.LENGTH_SHORT).show();
		});
	}
	public void init(){
		text_oldpass = (TextView) findViewById(R.id.text_oldpass);
		text_newpass_1 = (TextView) findViewById(R.id.text_newpass_1);
		text_newpass_2 = (TextView) findViewById(R.id.text_newpass_2);
		edit_oldpass = (EditText) findViewById(R.id.edit_oldpass);
		edit_newpass_1 = (EditText) findViewById(R.id.edit_newpass_1);
		edit_newpass_2 = (EditText) findViewById(R.id.edit_newpass_2);
		button_backwards = (Button) findViewById(R.id.backwards);
		button_submit = (Button) findViewById(R.id.submit);
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
				oldpass = user.GetPassword();
			}
		}
	}
}
