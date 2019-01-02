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

public class ReDetailActivity extends Activity {

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

	private TextView task1_size;
	private TextView task1_Place;
	private TextView task1_Tele;
	private TextView task1_State;
	private TextView task_Content;

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
		filter.addAction("android.intent.action.ReDetailActivity"); // 此处改成自己activity的名字
		registerReceiver(receiver, filter);

		init();

		Bundle bundle = this.getIntent().getExtras();
		TaskId = bundle.getString("TaskId");

		// 发送查询任务信息
		String sendstr = "&56&00" + TaskId;
		sent(sendstr);
		mHandler = new Handler() {
			public void handleMessage(android.os.Message msg) {
				String strall = msg.obj.toString();
				String str = strall.substring(1, 3);
				if (str.equals("56")) { // 接收成功
					task.Initial(strall);
					ShowMessage();
				}
			};
		};

		button1.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				new AlertDialog.Builder(ReDetailActivity.this)
						.setTitle("温馨提示")
						.setMessage("是否确定任务已经被完成")
						.setPositiveButton("确定",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface arg0,
											int arg1) {

										// 记录用户的满意度选择
										final boolean[] checkedItems = new boolean[3];
										new AlertDialog.Builder(
												ReDetailActivity.this)
												.setTitle("评价")
												.setMultiChoiceItems(
														new String[] { "满意",
																"一般", "呵呵" },
														checkedItems,
														new OnMultiChoiceClickListener() {
															public void onClick(
																	DialogInterface dialog,
																	int which,
																	boolean isChecked) {
																// TODO
																// Auto-generated
																// method stub
																checkedItems[which] = isChecked;
															}
														})
												.setPositiveButton(
														"确定",
														new DialogInterface.OnClickListener() {
															@Override
															public void onClick(
																	DialogInterface arg0,
																	int arg1) {
																// 发送任务完成信息
																for (int i = 0; i < checkedItems.length; i++) {
																	if (checkedItems[i]) {
																		/*
																		 * 根据i来发送任务完成情况
																		 * i==0，
																		 * 满意
																		 * i==
																		 * 1，一般
																		 * i
																		 * ==2，
																		 * 呵呵
																		 */
																		String sendstr;
																		if (i == 0) {
																			sendstr = "&58"
																					+ "&00"
																					+ TaskId
																					+ "&13满意";
																		} else if (i == 1) {
																			sendstr = "&58"
																					+ "&00"
																					+ TaskId
																					+ "&13一般";
																		} else {
																			sendstr = "&58"
																					+ "&00"
																					+ TaskId
																					+ "&13呵呵";
																		}
																		sent(sendstr);
																		button1.setEnabled(false);
																	}
																}
															}
														}).create().show();
									}
								}).setNegativeButton("取消", null).create()
						.show();
			}
		});

		button2.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				new AlertDialog.Builder(ReDetailActivity.this)
						.setTitle("温馨提示")
						.setMessage("是否确认撤销任务")
						.setPositiveButton("确定",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface arg0,
											int arg1) {
										// 发送撤销任务信息
										String sendstr = "&53&00" + TaskId;
										sent(sendstr);
									}

								}).setNegativeButton("取消", null).create()
						.show();
			}
		});

		button3.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				new AlertDialog.Builder(ReDetailActivity.this)
						.setTitle("温馨提示")
						.setMessage("是否确定更改任务")
						.setPositiveButton("确定",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface arg0,
											int arg1) {
										/* 进入任务信息填写页面 */
										Intent intent = new Intent(
												ReDetailActivity.this,
												ATaskModify.class);
										// 用Bundle携带数据
										Bundle bundle = new Bundle();
										bundle.putString("TaskId", TaskId);
										intent.putExtras(bundle);
										startActivity(intent);
									}
								}).setNegativeButton("取消", null).create()
						.show();
			}
		});
	}

	public void init() {
		button1 = (Button) findViewById(R.id.FinishTask);// 确认收货
		button2 = (Button) findViewById(R.id.AnnulTask);// 撤销任务
		button3 = (Button) findViewById(R.id.ModifyTask);// 修改任务
		User = (TextView) findViewById(R.id.infoRecipient);// 甲方用户名
		DDL = (TextView) findViewById(R.id.infoArriTime);// 送达时间
		Coins = (TextView) findViewById(R.id.infoCoinsNum);// 悬赏金额
		Address = (TextView) findViewById(R.id.infoAddress);// 收货地址
		Adtime = (TextView) findViewById(R.id.infoOrderTime);// 收货地址
		OrderID = (TextView) findViewById(R.id.infoOrderNum);// 订单号
		infoPhoneReci = (TextView) findViewById(R.id.infoPhoneReci);

		task1_size = (TextView) findViewById(R.id.task1_size);
		task1_Place = (TextView) findViewById(R.id.task1_Place);
		task1_Tele = (TextView) findViewById(R.id.task1_Tele);
		task1_State = (TextView) findViewById(R.id.task1_State);
		task_Content = (TextView) findViewById(R.id.task_Content);// 任务描述
	}

	// ****************两个与netservice沟通的接口，收发数据，，字符串格式
	public void sent(String bs) { // 通过Service发送数据
		Intent intent = new Intent();// 创建Intent对象
		intent.setAction("android.intent.action.cmd");
		intent.putExtra("value", bs);
		sendBroadcast(intent);// 发送广播
	}

	public void ShowMessage() {
		User.setText(task.GetUsr2Name());
		DDL.setText(String.valueOf(task.GetOutTime()[0]) + "月"
				+ String.valueOf(task.GetOutTime()[1]) + "日"
				+ String.valueOf(task.GetOutTime()[2]) + "时 - "
				+ String.valueOf(task.GetOutTime()[3]) + "时");
		Coins.setText(String.valueOf(task.GetCoins()));
		Address.setText(Data_all.Address[(task.GetOutAddress())[0]][(task
				.GetOutAddress())[1]]);
		// 更新时间信息
		Adtime.setText(String.valueOf(task.GetInTime()[0]) + "月"
				+ String.valueOf(task.GetInTime()[1]) + "日"
				+ String.valueOf(task.GetInTime()[2]) + "时 - "
				+ String.valueOf(task.GetInTime()[3]) + "时");
		OrderID.setText(String.valueOf(task.GetTNO()));
		infoPhoneReci.setText(task.GetUsr2Tele());
		task1_size.setText(String.valueOf(task.GetSize()));
		task1_Place.setText(Data_all.Address[(task.GetInAddress())[0]][(task
				.GetInAddress())[1]]);
		task1_Tele.setText(task.GetLast4Tele());
		task_Content.setText(task.GetContent());

		if (task.GetTaskstate() == 0) {// 任务未被领取，不可确认收货，可撤销，可修改
			button1.setEnabled(false);
			button2.setEnabled(true);
			button3.setEnabled(true);
			task1_State.setText("未被领取");
		}
		if (task.GetTaskstate() == 1) {// 任务被领取，可确认收货，不可撤销
			button1.setEnabled(true);
			button2.setEnabled(false);
			button3.setEnabled(false);
			task1_State.setText("进行中");
		}
		if (task.GetTaskstate() == 2) {// 任务被领取，可确认收货，不可撤销
			button1.setEnabled(true);
			button2.setEnabled(false);
			button3.setEnabled(false);
			task1_State.setText("乙方已申请完成");
			showToast("乙方已提出完成申请！");
		}
		if (task.GetTaskstate() == 3) {// 任务被完成，不可确认收货，不可撤销
			button1.setEnabled(false);
			button2.setEnabled(false);
			button3.setEnabled(false);
			task1_State.setText("已完成");
		}
		if (task.GetTaskstate() == 4) {// 任务被撤销，不可确认收货，不可撤销
			button1.setEnabled(false);
			button2.setEnabled(false);
			button3.setEnabled(false);
			task1_State.setText("已撤销");
		}
	}

	private class MyReceiver extends BroadcastReceiver { // 接收service传来的信息
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			if (intent.getAction().equals(
					"android.intent.action.ReDetailActivity")) {
				Bundle bundle = intent.getExtras();
				String str = bundle.getString("str");
				Message msg = new Message();
				msg.obj = str;
				mHandler.sendMessage(msg);
			}
		}
	}

	public void showToast(String str) {// 显示提示信息
		Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT).show();
	}
}
