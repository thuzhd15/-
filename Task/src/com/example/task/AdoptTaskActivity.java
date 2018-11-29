package com.example.task;

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


public class AdoptTaskActivity extends Activity{
	

    private ListView list;
    String[] arr1 = new String[]{"任务一","任务二","任务三","任务四"};
	protected void onCreate(Bundle savedInstanceState) {
	       super.onCreate(savedInstanceState);
	       setContentView(R.layout.activity_task_adopt);     
	       list = (ListView)findViewById(R.id.TaskListView2);
	       ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, R.layout.array_list_view, arr1);
	       list.setAdapter(adapter1);
	       list.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// arg2表示点击的是第几个列表
				Intent intent =new Intent(AdoptTaskActivity.this,AdDetailActivity.class);
				//用Bundle携带数据
			    Bundle bundle=new Bundle();
			    //传递name参数为tinyphp
			    bundle.putString("TaskId", "tinyphp");
			    intent.putExtras(bundle);
				startActivity(intent);
			}	    	   
	       });
	    }
	
	@Override
	protected void onResume() {
		super.onResume();
	}
	@Override
	protected void onDestroy() {

		super.onDestroy();
	}

}
