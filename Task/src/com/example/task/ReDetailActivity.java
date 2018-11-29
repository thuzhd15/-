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
	       
	       button1 = (Button) findViewById(R.id.FinishTask);//ȷ���ջ�
	       button2 = (Button) findViewById(R.id.AnnulTask);//��������
	       button3 = (Button) findViewById(R.id.ModifyTask);//�޸�����
	       
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
							}			
					})
					.setNegativeButton("ȡ��",null)
					.create()
					.show();					
				}
			});
	}
}
