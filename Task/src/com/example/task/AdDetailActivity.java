package com.example.task;

import android.widget.Button;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class AdDetailActivity extends Activity {
    private Button button1;
    private Button button2;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
	       super.onCreate(savedInstanceState);
	       setContentView(R.layout.activity_readopt);
	       button1 = (Button) findViewById(R.id.AchieveTask);//任务完成
	       button2 = (Button) findViewById(R.id.GiveupTask);//放弃任务
	       Bundle bundle = this.getIntent().getExtras();
	       String TaskId = bundle.getString("TaskId");
	       
	       button1.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					new AlertDialog.Builder(AdDetailActivity.this)
					.setTitle("温馨提示")
					.setMessage("是否确定完成任务")
					.setPositiveButton("确定",new DialogInterface.OnClickListener(){
							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								/*发送确认任务完成字符串*/
								
							}						
					})
					.setNegativeButton("取消",null)
					.create()
					.show();
				}
			});
	       
	       button2.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					new AlertDialog.Builder(AdDetailActivity.this)
					.setTitle("温馨提示")
					.setMessage("是否确定放弃任务")
					.setPositiveButton("确定",new DialogInterface.OnClickListener(){
							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								/*发送放弃任务字符串*/
								
							}	
					})
					.setNegativeButton("取消",null)
					.create()
					.show();
				}
			});
    }
}
