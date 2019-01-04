//发布的任务的详情界面
package com.example.lazy_man_client;

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
    private TextView Address;
    private TextView Adtime;
    private TextView OrderID;
    private TextView infoPhoneReci;
    private TextView infoEvaluation;
    private TextView task1_size;
    private TextView task1_Place;
    private TextView task1_Tele;
    private TextView task1_State;
    private TextView task1_content;
    private Handler mHandler;
	MyReceiver receiver;
	Task task = new Task();
	private String TaskId;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	       super.onCreate(savedInstanceState);
	       setContentView(R.layout.activity_re_detail);
	       receiver = new MyReceiver(); // 注册广播
	       IntentFilter filter = new IntentFilter();
	       filter.addAction("android.intent.action.ReDetailActivity");
	       registerReceiver(receiver, filter);
	       init();
	       Bundle bundle = this.getIntent().getExtras();
	       TaskId = bundle.getString("TaskId");
	       
		mHandler = new Handler() {
			public void handleMessage(android.os.Message msg) {
				String strall = msg.obj.toString();
				String str = strall.substring(1, 3);
				if (str.equals("60")) { // 接收成功
					task.Initial(strall);
					ShowMessage();
				} else if (str.equals("58")) { // 任务是否已确认完成
					Task task = new Task();
					task.Initial(strall);
					if (task.GetErrorType() == 0) {
						showToast("任务状态变更成功！");
					} else {
						showToast("任务状态变更失败！");
						// 恢复确认完成的按钮
						button1.setEnabled(true);
					}
				} else if (str.equals("53")) { // 任务是否已成功撤销
					Task task = new Task();
					task.Initial(strall);
					if (task.GetErrorType() == 0) {
						showToast("任务状态变更成功！");
					} else {
						showToast("任务状态变更失败！");
						// 恢复撤销和修改的按钮
						button2.setEnabled(true);
						button3.setEnabled(true);
					}
				}
			}
		};
	       
	       // 确认任务完成，确定后进入个人任务评价页面
	       button1.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					new AlertDialog.Builder(ReDetailActivity.this)
					.setTitle("温馨提示")
					.setMessage("是否确定任务已经被完成？")
					.setPositiveButton("确定",new DialogInterface.OnClickListener(){
							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								final boolean[] checkedItems = new boolean[3]; // 记录用户的满意度选择
								new AlertDialog.Builder(ReDetailActivity.this)
								.setTitle("评价")
								.setMultiChoiceItems(new String[]{"满意","一般","呵呵"}, checkedItems, new OnMultiChoiceClickListener() {
									public void onClick(DialogInterface dialog, int which, boolean isChecked) {
									checkedItems[which] = isChecked;
									}
								}).setPositiveButton("确定",new DialogInterface.OnClickListener(){
									
									// 发送任务完成信息
									@Override
									public void onClick(DialogInterface arg0, int arg1) {
										for (int i = 0;i<checkedItems.length;i++){
											if (checkedItems[i]){
												/*根据i来发送任务完成情况
												 * i==0， 满意
												 * i==1，一般
												 * i==2，呵呵
												 */
												String sendstr;
												if (i==0) {
													sendstr = "&58"+"&00"+TaskId+"&13满意";
												}else if(i==1) {
													sendstr = "&58"+"&00"+TaskId+"&13一般";
												}else {
													sendstr = "&58"+"&00"+TaskId+"&13呵呵";
												}
												sent(sendstr);
												// 设置按钮不可点击
												button1.setEnabled(false);
												button2.setEnabled(false);
												button3.setEnabled(false);
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
	       
	       // 撤销任务
	       button2.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					new AlertDialog.Builder(ReDetailActivity.this)
					.setTitle("温馨提示")
					.setMessage("是否确认撤销任务")
					.setPositiveButton("确定",new DialogInterface.OnClickListener(){
						
						  // 发送撤销任务信息
							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								String sendstr = "&53&00" + TaskId;
								sent(sendstr);
								// 设置按钮不可点击
								button1.setEnabled(false);
								button2.setEnabled(false);
								button3.setEnabled(false);
							}												
					})
					.setNegativeButton("取消",null)
					.create()
					.show();	
				}
			});
	       
	       // 任务修改请求
	       button3.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					new AlertDialog.Builder(ReDetailActivity.this)
					.setTitle("温馨提示")
					.setMessage("是否确认修改任务")
					.setPositiveButton("确定",new DialogInterface.OnClickListener(){
							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								// 设置按钮不可点击
								button1.setEnabled(false);
								button2.setEnabled(false);
								button3.setEnabled(false);
								// 进入任务信息填写页面
								Intent intent =new Intent(ReDetailActivity.this,ATaskModify.class);							
								  //用Bundle携带数据
							    Bundle bundle=new Bundle();
							    bundle.putString("TaskId", TaskId);
							    intent.putExtras(bundle);							    
								startActivity(intent);
							}			
					})
					.setNegativeButton("取消",null)
					.create()
					.show();					
				}
			});
	}
	
	// 信息初始化：获取任务详情
	@Override
	protected void onResume() {
		String sendstr = "&60&00" + TaskId;
		sent(sendstr);
		super.onResume();
	}
	
	// 控件初始化
	public void init(){
		 	button1 = (Button) findViewById(R.id.FinishTask);//确认收货
	    button2 = (Button) findViewById(R.id.AnnulTask);//撤销任务
	    button3 = (Button) findViewById(R.id.ModifyTask);//修改任务
		    User = (TextView)findViewById(R.id.infoRecipient);//甲方用户名
		    DDL = (TextView)findViewById(R.id.infoArriTime);//送达时间
		    Coins = (TextView)findViewById(R.id.infoCoinsNum);//悬赏金额
		    Address =(TextView)findViewById(R.id.infoAddress);//收货地址
		    Adtime = (TextView)findViewById(R.id.infoOrderTime);//收货地址
		    OrderID = (TextView)findViewById(R.id.infoOrderNum);//订单号
		    infoPhoneReci = (TextView)findViewById(R.id.infoPhoneReci); // 手机尾号
		    infoEvaluation = (TextView)findViewById(R.id.infoEvaluation); // 任务评价
		    task1_size = (TextView)findViewById(R.id.task1_size);
		    task1_Place = (TextView)findViewById(R.id.task1_place);
		    task1_Tele = (TextView)findViewById(R.id.task1_Tele);
		    task1_State = (TextView)findViewById(R.id.task1_State);
		    task1_content = (TextView)findViewById(R.id.task1_content);
	    }

	// 两个与netservice沟通的接口，收发数据，字符串格式
	// 发送数据
	public void sent(String bs) {
			Intent intent = new Intent();// 创建Intent对象
			intent.setAction("android.intent.action.cmd");
			intent.putExtra("value", bs);
			sendBroadcast(intent);// 发送广播
		}
	
	//显示任务详细信息
	public void ShowMessage(){
			User.setText(task.GetUsr2Name());
			DDL.setText(String.valueOf(task.GetOutTime()[0])+"月"+String.valueOf(task.GetOutTime()[1])+"日"+String.valueOf(task.GetOutTime()[2])+"时 - "+String.valueOf(task.GetOutTime()[3])+"时");
			Coins.setText(String.valueOf(task.GetCoins()));
			Address.setText(Data_all.Address[(task.GetOutAddress())[0] ][(task.GetOutAddress())[1] ] );				
			Adtime.setText(String.valueOf(task.GetInTime()[0])+"月"+String.valueOf(task.GetInTime()[1])+"日"+String.valueOf(task.GetInTime()[2])+"时 - "+String.valueOf(task.GetInTime()[3])+"时");
			OrderID.setText(String.valueOf(task.GetTNO()));
			infoPhoneReci.setText(task.GetUsr2Tele());
			infoEvaluation.setText(task.GetEva()); // 任务评价
			String size_list[]=getResources().getStringArray(R.array.size);
			task1_size.setText(size_list[task.GetSize()]);
			task1_Place.setText(Data_all.Address[(task.GetInAddress())[0] ][(task.GetInAddress())[1] ] );	
			task1_Tele.setText(task.GetLast4Tele());
			task1_content.setText("任务描述  " + task.GetContent()); // 任务描述
			
			// 任务未被领取，不可确认收货，可撤销，可修改
			if(task.GetTaskstate()==0){
			    	button1.setEnabled(false);
			    	button2.setEnabled(true);
			    	button3.setEnabled(true);
			    	task1_State.setText("未领取");
			    }
			    
			// 任务被领取，可确认收货，不可撤销，不可修改
			if(task.GetTaskstate()==1){
			    	button1.setEnabled(true);
			    	button2.setEnabled(false);
			    	button3.setEnabled(false);
			    	task1_State.setText("已领取");
			    }
			    
			// 任务被领取，可确认收货，不可撤销，不可修改
			if(task.GetTaskstate()==2){
		    	button1.setEnabled(true);
		    	button2.setEnabled(false);
		    	button3.setEnabled(false);
		    	task1_State.setText("乙方已申请完成");
		    	showToast("乙方已申请，是否确认收货？");
		    }
		    
		    // 任务被完成，不可确认收货，不可撤销，不可修改
			if(task.GetTaskstate()==3){
		    	button1.setEnabled(false);
		    	button2.setEnabled(false);
		    	button3.setEnabled(false);
		    	task1_State.setText("已完成");
		    }
		    
		    // 任务被撤销，不可确认收货，不可撤销，不可修改
			if(task.GetTaskstate()==4){
		    	button1.setEnabled(false);
		    	button2.setEnabled(false);
		    	button3.setEnabled(false);
		    	task1_State.setText("已撤销");
		    }
		}
	 
	 // 接收service传来的信息
	 private class MyReceiver extends BroadcastReceiver {
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
	 
	// 显示提示信息
	public void showToast(String str) {
		Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT).show();
	}
}

