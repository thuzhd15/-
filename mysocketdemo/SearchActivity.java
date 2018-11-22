package com.example.mysocketdemo;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SearchActivity extends ActionBarActivity {
    static final int CMD_STOP_SERVICE = 0x01;  
    static final int CMD_SEND_DATA = 0x02;  
    static final int CMD_RECEIVE_DATA = 0x03;  
	private Button button_search;
	private EditText E_condition;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		E_condition = (EditText) findViewById(R.id.Econdition);
		button_search = (Button) findViewById(R.id.button_SASearch);
		button_search.setOnClickListener(new View.OnClickListener() {            
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                //当点击按钮时,会获取编辑框中的数据,然后提交给线程
            	byte[]  msgBuffer = null;
            	msgBuffer = E_condition.getText().toString().getBytes();
                sent(msgBuffer);
                Toast.makeText(getApplicationContext(), "搜索", Toast.LENGTH_SHORT).show();
    			Intent intent =new Intent(SearchActivity.this,MainActivity.class);
    			startActivity(intent);	
            }
        });
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.regist, menu);
		return true;
	}
	public void sent(byte[] bs){  //通过Service发送数据
		Intent intent = new Intent();//创建Intent对象  
		intent.setAction("android.intent.action.cmd");  
		intent.putExtra("cmd", CMD_SEND_DATA);  
		intent.putExtra("value", bs);  
		sendBroadcast(intent);//发送广播      
	}  

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
