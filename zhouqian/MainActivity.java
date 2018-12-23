package com.example.zhouqian.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends Activity {
    public static final String TASK_ID = "com.example.zhouqian.myapplication.MESSAGE";
    private static final int NOTIFICATION_FLAG = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        System.out.println("ready to connect");
        connect();
        System.out.println("connected!");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    public void ToUntaketask(View view) {
        // Do something in response to button
        Intent intent = new Intent(this, ATaskUntaken.class);
        String message="0001";
        intent.putExtra(TASK_ID, message);
        startActivity(intent);
    }

    public void ToSolvingtask(View view) {
        // Do something in response to button
        Intent intent = new Intent(this, ATaskModify.class);
        String message="&51&000&010001&061&07取快递&10&1120&080101&0901010101&150101&1601010101";
        intent.putExtra(TASK_ID, message);
        startActivity(intent);
    }

    public void ToSolvedtask(View view) {
        // Do something in response to button
        Intent intent = new Intent(this, ATaskSolved.class);
        String message="0003";
        intent.putExtra(TASK_ID, message);
        startActivity(intent);
    }

    public void Inittask(View view) {
        // Do something in response to button
        Intent intent = new Intent(this, AReleaseTask.class);
//        String message="0003";
//        intent.putExtra(TASK_ID, message);
        startActivity(intent);
    }

    public void connect() { // 连接服务器，启动Service
        Intent intent = new Intent(MainActivity.this, NetService.class);
        startService(intent);
    }

}
