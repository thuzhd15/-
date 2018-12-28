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
	       
	       receiver = new MyReceiver(); // ע��㲥
	       IntentFilter filter = new IntentFilter();
	       filter.addAction("android.intent.action.ReleaseTaskActivity"); //�˴��ĳ��Լ�activity������
	       registerReceiver(receiver, filter);
	       
	       init();
	       
	       Bundle bundle = this.getIntent().getExtras();
	       TaskId = bundle.getString("TaskId");
	       
	       //���Ͳ�ѯ������Ϣ
	       String sendstr = "&56&00"+TaskId;
	       //sent(sendstr);
	       mHandler = new Handler() {
				public void handleMessage(android.os.Message msg) {
					String str = msg.obj.toString();
					Toast.makeText(getApplicationContext(), "handle"+str, Toast.LENGTH_SHORT)
					.show();	
					if (str.equals("000")) { // ��½�ɹ�

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
					.setTitle("��ܰ��ʾ")
					.setMessage("�Ƿ�ȷ�������Ѿ������")
					.setPositiveButton("ȷ��",new DialogInterface.OnClickListener(){
							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								
								//��¼�û��������ѡ��
								final boolean[] checkedItems = new boolean[3];
								new AlertDialog.Builder(ReDetailActivity.this)
								.setTitle("����")
								.setMultiChoiceItems(new String[]{"����","һ��","�Ǻ�"}, checkedItems, new OnMultiChoiceClickListener() {
									public void onClick(DialogInterface dialog, int which, boolean isChecked) {
										// TODO Auto-generated method stub
										checkedItems[which] = isChecked;
									}
								}).setPositiveButton("ȷ��",new DialogInterface.OnClickListener(){
									@Override
									public void onClick(DialogInterface arg0, int arg1) {
										// �������������Ϣ
										for (int i = 0;i<checkedItems.length;i++){
											if (checkedItems[i]){
												/*����i����������������
												 * i==0�� ����
												 * i==1��һ��
												 * i==2���Ǻ�
												 */
												String sendstr;
												if (i==0){
													sendstr = "&57&"+"&00"+TaskId+"&13����";
												}else if(i==1){
													sendstr = "&57&"+"&00"+TaskId+"&13һ��";
												}else{
													sendstr = "&57&"+"&00"+TaskId+"&13�Ǻ�";
												}
												sent(sendstr);
											}
										}
									}
								}).create()
								.show();
							}				
					})
					.setNegativeButton("ȡ��",null)
					.create()
					.show();
				}
			});
	       
	       button2.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					new AlertDialog.Builder(ReDetailActivity.this)
					.setTitle("��ܰ��ʾ")
					.setMessage("�Ƿ�ȷ�ϳ�������")
					.setPositiveButton("ȷ��",new DialogInterface.OnClickListener(){
							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								// ���ͳ���������Ϣ
								String sendstr = "&53&00" + TaskId;
								sent(sendstr);
							}
								
					})
					.setNegativeButton("ȡ��",null)
					.create()
					.show();	
				}
			});
	       
	       button3.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					new AlertDialog.Builder(ReDetailActivity.this)
					.setTitle("��ܰ��ʾ")
					.setMessage("�Ƿ�ȷ����������")
					.setPositiveButton("ȷ��",new DialogInterface.OnClickListener(){
							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								/*����������Ϣ��дҳ��*/	
								String sendstr = "&51&"+"&00"+TaskId;
								sent(sendstr);
							}			
					})
					.setNegativeButton("ȡ��",null)
					.create()
					.show();					
				}
			});
	}
	 public void init(){
		 	button1 = (Button) findViewById(R.id.FinishTask);//ȷ���ջ�
	        button2 = (Button) findViewById(R.id.AnnulTask);//��������
	        button3 = (Button) findViewById(R.id.ModifyTask);//�޸�����
		    User = (TextView)findViewById(R.id.User2_textView);//�׷��û���
		    DDL = (TextView)findViewById(R.id.REDDL_textView);//�׷��û���
		    Coins = (TextView)findViewById(R.id.RECoins_textView);//�׷��û���
	    }
	 public void connect() { // ���ӷ�����������Service
			Intent intent = new Intent(ReDetailActivity.this, NetService.class);
			startService(intent);
		}

		//****************������netservice��ͨ�Ľӿڣ��շ����ݣ����ַ�����ʽ	
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
