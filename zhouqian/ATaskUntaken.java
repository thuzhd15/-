package com.example.zhouqian.myapplication;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.app.AlertDialog;

public class ATaskUntaken extends Activity {
    public static final String TASK_ID = "com.example.zhouqian.myapplication.MESSAGE";
    Task TASK = new Task();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atask_untaken);

        // 从父页面获取任务ID
        Intent intent = getIntent();
        String TaskID = intent.getStringExtra(MainActivity.TASK_ID);

        //通过任务ID获取任务详情并初始化界面

        //TASK.Init(TaskID);
        InitInfo(TASK);

        // Capture the layout's TextView and set the string as its text
        //       TextView textView = (TextView) findViewById(R.id.textView);
//        textView.setText(message);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_atask_untaken, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void DeleConfirm(View view) {
        AlertDialog.Builder alertdialogbuilder = new AlertDialog.Builder(this);
        alertdialogbuilder.setMessage("撤销影响信誉！");
        alertdialogbuilder.setPositiveButton("确定", ToTaskDele);
        alertdialogbuilder.setNegativeButton("取消", cancel);
        AlertDialog alertdialog1 = alertdialogbuilder.create();
        alertdialog1.show();
    }

    private DialogInterface.OnClickListener ToTaskDele=new DialogInterface.OnClickListener() {
        // 转到撤销任务页面
        @Override
        public void onClick(DialogInterface arg0, int arg1) {
//        Intent intent = new Intent(this, ATaskDele.class);
        //       String message=TASK.GetTNO();
        //       intent.putExtra(TASK_ID, message);
        //      startActivity(intent);
        }
    };
    private DialogInterface.OnClickListener cancel=new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface arg0,int arg1)
        {
            arg0.cancel();
        }
    };

    public void ToTaskModify(View view) {
        // 转到修改任务页面
        Intent intent = new Intent(this, ATaskModify.class);
        //String id=TASK.GetTNO();
        //intent.putExtra(TASK_ID, id);
        startActivity(intent);
    }

    public void ToTaskConfirm(View view) {
        // 转到确认收货页面
//        Intent intent = new Intent(this, ATaskConfirm.class);
        //       String message=TASK.GetTNO();
        //       intent.putExtra(TASK_ID, message);
        //      startActivity(intent);
    }

    private void InitInfo(Task MyTask){
        /*
        String Label_TaskName=MyTask.GetTaskName();
        String Label_OutTime=MyTask.GetOutTime();
        String Label_Address=MyTask.GetOutAddress();
        String Label_Usr2Name=MyTask.GetUsr2Name();
        //int Coins=MyTask.GetCoins();
        int Coins=100;
        String Label_Coins=String.valueOf(Coins);
    //    String Label_InTime=MyTask.Getddl();
        String Label_TaskID=MyTask.GetTNO();

        TextView taskname = (TextView)findViewById(R.id.TaskUntaken_A_TaskName);
        taskname.setText(Label_TaskName);
        TextView outtime = (TextView)findViewById(R.id.TaskUntaken_A_OutTime);
        outtime.setText(Label_OutTime);
        TextView address = (TextView)findViewById(R.id.TaskUntaken_A_Address);
        address.setText(Label_Address);
        TextView usr2name = (TextView)findViewById(R.id.TaskUntaken_A_Usr2);
        usr2name.setText(Label_Usr2Name);
        TextView coins = (TextView)findViewById(R.id.TaskUntaken_A_Coins);
        coins.setText(Label_Coins);
        TextView intime = (TextView)findViewById(R.id.TaskUntaken_A_InTime);
      //  intime.setText(Label_InTime);
        TextView taskid = (TextView)findViewById(R.id.TaskUntaken_A_TaskID);
        taskid.setText(Label_TaskID);
        */
    }

    public void CallUsr2(View view){
/*
        //获取输入的电话号码
        String phone = "13996047780";
        //创建打电话的意图
        Intent intent = new Intent();
        //设置拨打电话的动作
        intent.setAction(Intent.ACTION_CALL);
        //设置拨打电话的号码
        intent.setData(Uri.parse("tel:" + phone));
        //开启打电话的意图
        startActivity(intent);
*/

    }
}
