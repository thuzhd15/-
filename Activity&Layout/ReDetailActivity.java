package com.example.task;


import android.app.Activity;
import android.app.Dialog;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Button;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class ReDetailActivity extends Activity{
	
	
    private Button button1;
    private Button button2;
    private Button button3;
    private TextView User;
    private TextView DDL;
    private TextView Coins;
    
    private Handler mHandler;
	MyReceiver receiver;
	Mytask task = new Mytask();
	
	private String TaskId;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	       super.onCreate(savedInstanceState);
	       setContentView(R.layout.activity_redetail);
	       
	       receiver = new MyReceiver(); // 注册广播
	       IntentFilter filter = new IntentFilter();
	       filter.addAction("android.intent.action.ReleaseTaskActivity"); //此处改成自己activity的名字
	       registerReceiver(receiver, filter);
	       
	       init();
	       
	       Bundle bundle = this.getIntent().getExtras();
	       TaskId = bundle.getString("TaskId");
	       
	       //发送查询任务信息
	       String sendstr = "&56&00"+TaskId;
	       //sent(sendstr);
	       mHandler = new Handler() {
				public void handleMessage(android.os.Message msg) {
					String str = msg.obj.toString();
					Toast.makeText(getApplicationContext(), "handle"+str, Toast.LENGTH_SHORT)
					.show();	
					if (str.equals("000")) { // 登陆成功

					}
					else if (str.equals("001")) { 
	
					}else{
						task.Initial(str);
						User.setText(task.GetUsr1Name());
						DDL.setText(task.GetInTime().toString());
						Coins.setText(task.GetCoins());
					}
					
				};
			};
	       
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
												String sendstr;
												if (i==0){
													sendstr = "&57&"+"&00"+TaskId+"&13满意";
												}else if(i==1){
													sendstr = "&57&"+"&00"+TaskId+"&13一般";
												}else{
													sendstr = "&57&"+"&00"+TaskId+"&13呵呵";
												}
												sent(sendstr);
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
								String sendstr = "&53&00" + TaskId;
								sent(sendstr);
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
								String sendstr = "&51&"+"&00"+TaskId;
								sent(sendstr);
							}			
					})
					.setNegativeButton("取消",null)
					.create()
					.show();					
				}
			});
	}
	 public void init(){
		 	button1 = (Button) findViewById(R.id.FinishTask);//确认收货
	        button2 = (Button) findViewById(R.id.AnnulTask);//撤销任务
	        button3 = (Button) findViewById(R.id.ModifyTask);//修改任务
		    User = (TextView)findViewById(R.id.User2_textView);//甲方用户名
		    DDL = (TextView)findViewById(R.id.REDDL_textView);//甲方用户名
		    Coins = (TextView)findViewById(R.id.RECoins_textView);//甲方用户名
	    }
	 public void connect() { // 连接服务器，启动Service
			Intent intent = new Intent(ReDetailActivity.this, NetService.class);
			startService(intent);
		}

		//****************两个与netservice沟通的接口，收发数据，，字符串格式	
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
				if (intent.getAction().equals("android.intent.action.ReDetailActivity")) {
					Bundle bundle = intent.getExtras();
					String str = bundle.getString("str");
					Message msg = new Message();
					msg.obj = str;
					mHandler.sendMessage(msg);
				}
			}
		}
}
