package com.example.zhouqian.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class ATaskSolving extends Activity {
    public static final String TASK_ID = "com.example.zhouqian.myapplication.MESSAGE";//今后需要修改
    Task TASK = new Task();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atask_solving);

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        String TaskID = intent.getStringExtra(MainActivity.TASK_ID);

        //通过任务ID获取任务详情
        //TASK.Init(TaskID);
        //初始页面信息
        InitInfo(TASK);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_atask_solving, menu);
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

    public void To_TaskDele(View view) {
        // 转到撤销任务页面
//        Intent intent = new Intent(this, ATaskDele.class);
        //       String message=TASK.GetTNO();
        //       intent.putExtra(TASK_ID, message);
        //      startActivity(intent);
    }

    public void To_TaskModify(View view) {
        // 转到修改任务页面
        Intent intent = new Intent(this, ATaskModify.class);
        //String id=TASK.GetTNO();
        //intent.putExtra(TASK_ID, id);
        //startActivity(intent);
    }

    public void To_TaskSolved(View view) {
        // 转到确认收货页面
//        Intent intent = new Intent(this, ATaskEva.class);
        //       String message="0003";
        //       intent.putExtra(TASK_ID, message);
        //      startActivity(intent);
    }

    private void InitInfo(Task MyTask){
        //String Label_TaskName=MyTask.GetTaskName();
        /*
        String Label_OutTime=MyTask.GetOutTime();
        String Label_Address=MyTask.GetOutAddress();
        String Label_Usr2Name=MyTask.GetUsr2Name();
        //int Coins=MyTask.GetCoins();
        int Coins=100;
        String Label_Coins=String.valueOf(Coins);
     //   String Label_InTime=MyTask.Getddl();
        String Label_TaskID=MyTask.GetTNO();

        TextView taskname = (TextView)findViewById(R.id.TaskSolving_A_TaskName);
        taskname.setText(Label_TaskName);
        TextView outtime = (TextView)findViewById(R.id.TaskSolving_A_OutTime);
        outtime.setText(Label_OutTime);
        TextView address = (TextView)findViewById(R.id.TaskSolving_A_Address);
        address.setText(Label_Address);
        TextView usr2name = (TextView)findViewById(R.id.TaskSolving_A_Usr2);
        usr2name.setText(Label_Usr2Name);
        TextView coins = (TextView)findViewById(R.id.TaskSolving_A_Coins);
        coins.setText(Label_Coins);
        TextView intime = (TextView)findViewById(R.id.TaskSolving_A_InTime);
       // intime.setText(Label_InTime);
        TextView taskid = (TextView)findViewById(R.id.TaskSolving_A_TaskID);
        taskid.setText(Label_TaskID);
        */
    }

    public void CallUsr2(View view){}

}
