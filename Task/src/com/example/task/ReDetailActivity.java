package com.example.task;

import android.app.Activity;
import android.app.Dialog;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.os.Bundle;
import android.widget.Button;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class ReDetailActivity extends Activity{
	
	
    private Button button1;
    private Button button2;
    private Button button3;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	       super.onCreate(savedInstanceState);
	       setContentView(R.layout.activity_redetail);
	       
	       Bundle bundle = this.getIntent().getExtras();
	       String TaskId = bundle.getString("TaskId");
	       
	       button1 = (Button) findViewById(R.id.FinishTask);//确认收货
	       button2 = (Button) findViewById(R.id.AnnulTask);//撤销任务
	       button3 = (Button) findViewById(R.id.ModifyTask);//修改任务
	       
	       button1.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					new AlertDialog.Builder(ReDetailActivity.this)
					.setTitle("温馨提示")
					.setMessage("是否确定任务已经被完成")
					.setPositiveButton("确定",new DialogInterface.OnClickListener(){
							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								
								//记录用户的满意度选择
								final boolean[] checkedItems = new boolean[3];
								new AlertDialog.Builder(ReDetailActivity.this)
								.setTitle("评价")
								.setMultiChoiceItems(new String[]{"满意","一般","呵呵"}, checkedItems, new OnMultiChoiceClickListener() {
									public void onClick(DialogInterface dialog, int which, boolean isChecked) {
										// TODO Auto-generated method stub
										checkedItems[which] = isChecked;
									}
								}).setPositiveButton("确定",new DialogInterface.OnClickListener(){
									@Override
									public void onClick(DialogInterface arg0, int arg1) {
										// 发送任务完成信息
										for (int i = 0;i<checkedItems.length;i++){
											if (checkedItems[i]){
												/*根据i来发送任务完成情况
												 * i==0， 满意
												 * i==1，一般
												 * i==2，呵呵
												 */
											}
										}
									}
								}).create()
								.show();
							}				
					})
					.setNegativeButton("取消",null)
					.create()
					.show();
				}
			});
	       
	       button2.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					new AlertDialog.Builder(ReDetailActivity.this)
					.setTitle("温馨提示")
					.setMessage("是否确认撤销任务")
					.setPositiveButton("确定",new DialogInterface.OnClickListener(){
							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								// 发送撤销任务信息
								
							}
								
					})
					.setNegativeButton("取消",null)
					.create()
					.show();	
				}
			});
	       
	       button3.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					new AlertDialog.Builder(ReDetailActivity.this)
					.setTitle("温馨提示")
					.setMessage("是否确定更改任务")
					.setPositiveButton("确定",new DialogInterface.OnClickListener(){
							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								/*进入任务信息填写页面*/	
							}			
					})
					.setNegativeButton("取消",null)
					.create()
					.show();					
				}
			});
	}
}
