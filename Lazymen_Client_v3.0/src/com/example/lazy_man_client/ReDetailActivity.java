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

		receiver = new MyReceiver(); // ע��㲥
		IntentFilter filter = new IntentFilter();
		filter.addAction("android.intent.action.ReDetailActivity"); // �˴��ĳ��Լ�activity������
		registerReceiver(receiver, filter);

		init();

		Bundle bundle = this.getIntent().getExtras();
		TaskId = bundle.getString("TaskId");

		// ���Ͳ�ѯ������Ϣ
		String sendstr = "&56&00" + TaskId;
		sent(sendstr);
		mHandler = new Handler() {
			public void handleMessage(android.os.Message msg) {
				String strall = msg.obj.toString();
				String str = strall.substring(1, 3);
				if (str.equals("56")) { // ���ճɹ�
					task.Initial(strall);
					ShowMessage();
				}
			};
		};

		button1.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				new AlertDialog.Builder(ReDetailActivity.this)
						.setTitle("��ܰ��ʾ")
						.setMessage("�Ƿ�ȷ�������Ѿ������")
						.setPositiveButton("ȷ��",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface arg0,
											int arg1) {

										// ��¼�û��������ѡ��
										final boolean[] checkedItems = new boolean[3];
										new AlertDialog.Builder(
												ReDetailActivity.this)
												.setTitle("����")
												.setMultiChoiceItems(
														new String[] { "����",
																"һ��", "�Ǻ�" },
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
														"ȷ��",
														new DialogInterface.OnClickListener() {
															@Override
															public void onClick(
																	DialogInterface arg0,
																	int arg1) {
																// �������������Ϣ
																for (int i = 0; i < checkedItems.length; i++) {
																	if (checkedItems[i]) {
																		/*
																		 * ����i����������������
																		 * i==0��
																		 * ����
																		 * i==
																		 * 1��һ��
																		 * i
																		 * ==2��
																		 * �Ǻ�
																		 */
																		String sendstr;
																		if (i == 0) {
																			sendstr = "&58"
																					+ "&00"
																					+ TaskId
																					+ "&13����";
																		} else if (i == 1) {
																			sendstr = "&58"
																					+ "&00"
																					+ TaskId
																					+ "&13һ��";
																		} else {
																			sendstr = "&58"
																					+ "&00"
																					+ TaskId
																					+ "&13�Ǻ�";
																		}
																		sent(sendstr);
																		button1.setEnabled(false);
																	}
																}
															}
														}).create().show();
									}
								}).setNegativeButton("ȡ��", null).create()
						.show();
			}
		});

		button2.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				new AlertDialog.Builder(ReDetailActivity.this)
						.setTitle("��ܰ��ʾ")
						.setMessage("�Ƿ�ȷ�ϳ�������")
						.setPositiveButton("ȷ��",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface arg0,
											int arg1) {
										// ���ͳ���������Ϣ
										String sendstr = "&53&00" + TaskId;
										sent(sendstr);
									}

								}).setNegativeButton("ȡ��", null).create()
						.show();
			}
		});

		button3.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				new AlertDialog.Builder(ReDetailActivity.this)
						.setTitle("��ܰ��ʾ")
						.setMessage("�Ƿ�ȷ����������")
						.setPositiveButton("ȷ��",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface arg0,
											int arg1) {
										/* ����������Ϣ��дҳ�� */
										Intent intent = new Intent(
												ReDetailActivity.this,
												ATaskModify.class);
										// ��BundleЯ������
										Bundle bundle = new Bundle();
										bundle.putString("TaskId", TaskId);
										intent.putExtras(bundle);
										startActivity(intent);
									}
								}).setNegativeButton("ȡ��", null).create()
						.show();
			}
		});
	}

	public void init() {
		button1 = (Button) findViewById(R.id.FinishTask);// ȷ���ջ�
		button2 = (Button) findViewById(R.id.AnnulTask);// ��������
		button3 = (Button) findViewById(R.id.ModifyTask);// �޸�����
		User = (TextView) findViewById(R.id.infoRecipient);// �׷��û���
		DDL = (TextView) findViewById(R.id.infoArriTime);// �ʹ�ʱ��
		Coins = (TextView) findViewById(R.id.infoCoinsNum);// ���ͽ��
		Address = (TextView) findViewById(R.id.infoAddress);// �ջ���ַ
		Adtime = (TextView) findViewById(R.id.infoOrderTime);// �ջ���ַ
		OrderID = (TextView) findViewById(R.id.infoOrderNum);// ������
		infoPhoneReci = (TextView) findViewById(R.id.infoPhoneReci);

		task1_size = (TextView) findViewById(R.id.task1_size);
		task1_Place = (TextView) findViewById(R.id.task1_Place);
		task1_Tele = (TextView) findViewById(R.id.task1_Tele);
		task1_State = (TextView) findViewById(R.id.task1_State);
		task_Content = (TextView) findViewById(R.id.task_Content);// ��������
	}

	// ****************������netservice��ͨ�Ľӿڣ��շ����ݣ����ַ�����ʽ
	public void sent(String bs) { // ͨ��Service��������
		Intent intent = new Intent();// ����Intent����
		intent.setAction("android.intent.action.cmd");
		intent.putExtra("value", bs);
		sendBroadcast(intent);// ���͹㲥
	}

	public void ShowMessage() {
		User.setText(task.GetUsr2Name());
		DDL.setText(String.valueOf(task.GetOutTime()[0]) + "��"
				+ String.valueOf(task.GetOutTime()[1]) + "��"
				+ String.valueOf(task.GetOutTime()[2]) + "ʱ - "
				+ String.valueOf(task.GetOutTime()[3]) + "ʱ");
		Coins.setText(String.valueOf(task.GetCoins()));
		Address.setText(Data_all.Address[(task.GetOutAddress())[0]][(task
				.GetOutAddress())[1]]);
		// ����ʱ����Ϣ
		Adtime.setText(String.valueOf(task.GetInTime()[0]) + "��"
				+ String.valueOf(task.GetInTime()[1]) + "��"
				+ String.valueOf(task.GetInTime()[2]) + "ʱ - "
				+ String.valueOf(task.GetInTime()[3]) + "ʱ");
		OrderID.setText(String.valueOf(task.GetTNO()));
		infoPhoneReci.setText(task.GetUsr2Tele());
		task1_size.setText(String.valueOf(task.GetSize()));
		task1_Place.setText(Data_all.Address[(task.GetInAddress())[0]][(task
				.GetInAddress())[1]]);
		task1_Tele.setText(task.GetLast4Tele());
		task_Content.setText(task.GetContent());

		if (task.GetTaskstate() == 0) {// ����δ����ȡ������ȷ���ջ����ɳ��������޸�
			button1.setEnabled(false);
			button2.setEnabled(true);
			button3.setEnabled(true);
			task1_State.setText("δ����ȡ");
		}
		if (task.GetTaskstate() == 1) {// ������ȡ����ȷ���ջ������ɳ���
			button1.setEnabled(true);
			button2.setEnabled(false);
			button3.setEnabled(false);
			task1_State.setText("������");
		}
		if (task.GetTaskstate() == 2) {// ������ȡ����ȷ���ջ������ɳ���
			button1.setEnabled(true);
			button2.setEnabled(false);
			button3.setEnabled(false);
			task1_State.setText("�ҷ����������");
			showToast("�ҷ������������룡");
		}
		if (task.GetTaskstate() == 3) {// ������ɣ�����ȷ���ջ������ɳ���
			button1.setEnabled(false);
			button2.setEnabled(false);
			button3.setEnabled(false);
			task1_State.setText("�����");
		}
		if (task.GetTaskstate() == 4) {// ���񱻳���������ȷ���ջ������ɳ���
			button1.setEnabled(false);
			button2.setEnabled(false);
			button3.setEnabled(false);
			task1_State.setText("�ѳ���");
		}
	}

	private class MyReceiver extends BroadcastReceiver { // ����service��������Ϣ
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

	public void showToast(String str) {// ��ʾ��ʾ��Ϣ
		Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT).show();
	}
}
