package com.example.zhouqian.myapplication;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ATaskSolved extends Activity {
    Task TASK = new Task();
    private Socket socket;
    Handler handler = new MyHandler();
    private static final int NOTIFICATION_FLAG = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atask_solved);

        //从父页面获取任务ID
        Intent intent = getIntent();
        String TaskID = intent.getStringExtra(MainActivity.TASK_ID);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    socket = new Socket("183.172.219.30", 10086);
                    // 接收
                    InputStream inputStream = socket.getInputStream();
                    byte[] buffer = new byte[102400];
                    int len;
                    while ((len = inputStream.read(buffer)) != -1) {
                        String s = new String(buffer, 0, len);
                        //
                        Message message = Message.obtain();
                        message.what = 0;
                        message.obj = s;
                        handler.sendMessage(message);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();



        //通过任务ID获取任务详情
        //TASK.Init(TaskID);
        //显示任务详细信息
        //ShowTaskInfo(TASK);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_atask_solved, menu);
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

    public void ToEvaluate(View view) {
        // 转到评价页面
//        Intent intent = new Intent(this, ATaskEva.class);
 //       String message=TASK.GetTNO();
 //       intent.putExtra(TASK_ID, message);
  //      startActivity(intent);
    }

    public void ShowTaskInfo(Task MyTask){
        //TODO

        TextView textView = (TextView) findViewById(R.id.TaskSolving_A_TaskName);
        //String TN=MyTask.GetText();
        //textView.setText(TN);
    }

    private void sendmessage(final String data){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // 发送
                    OutputStream outputStream = socket.getOutputStream();
                    outputStream.write(("IP:" + getHostIp() + " " + data).getBytes("utf-8"));
                    outputStream.flush();// 清空缓存

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    // 获取IP并转换格式
    private String getHostIp() {

        WifiManager mg = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        if (mg == null){
            return "";
        }

        WifiInfo wifiInfo = mg.getConnectionInfo();
        int ip = wifiInfo.getIpAddress();
        return ((ip & 0xff) + "." + (ip >> 8 & 0xff) + "."
                + (ip >> 16 & 0xff) + "." + (ip >> 24 & 0xff));
    }

    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                String s = (String) msg.obj;

                if(s.charAt(2)=='0') {

                    TASK.Initial(s);
                }
                else if(s.charAt(2)=='1'){

                }
                sendNotification();
                //EditText taskname = (EditText)findViewById(R.id.TaskModify_A_TaskName);
                //taskname.setText(s);
            }
        }
    }
    private void sendNotification() {
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,  new Intent(this, MainActivity.class), 0);

        Notification notify= new Notification.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher) // 设置状态栏中的小图片，尺寸一般建议在24×24， 这里也可以设置大图标
                .setTicker("傻逼陶明伟！")// 设置显示的提示文字
                        .setContentTitle("notification")// 设置显示的标题
                        .setContentText("这他妈傻逼陶明伟")// 消息的详细内容
                        .setContentIntent(pendingIntent) // 关联PendingIntent
                        .setNumber(1) // 在TextView的右方显示的数字，可以在外部定义一个变量，点击累加setNumber(count),这时显示的和
                        .getNotification(); // 需要注意build()是在API level16及之后增加的，在API11中可以使用getNotificatin()来代替
        notify.flags |= Notification.FLAG_AUTO_CANCEL;
        NotificationManager manager =(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(NOTIFICATION_FLAG, notify);
    }

}
