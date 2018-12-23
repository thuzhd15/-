package com.example.zhouqian.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;

public class ATaskModify extends Activity {
    Task TASK = new Task();
    Usr USR = new Usr();
    String Usrinfo;
    String TaskInfo;
    private Socket socket;
    final Handler handler = new MyHandler();
    MyReceiver receiver;
    NetworkStateReceiver networkstatereceiver;
    private Spinner size_spinner;
    private Spinner add_spinner_In1;
    private Spinner add_spinner_Out1;
    private Spinner add_spinner_In2;
    private Spinner add_spinner_Out2;
    private Spinner timeout_month;
    private Spinner timeout_day;
    private Spinner timeout_starthour;
    private Spinner timeout_endhour;
    private Spinner timein_month;
    private Spinner timein_day;
    private Spinner timein_starthour;
    private Spinner timein_endhour;
    private String[] size_list;
    private String[] out_add_list1;
    private String[] out_add_list2;
    private String[] in_add_list1;
    private String[] in_add_list2;
    private String[] time_month;
    private String[] time_day;
    private String[] time_hour;
    private ArrayAdapter<String> size_arr_adapter;
    private ArrayAdapter<String> addout_arr_adapter1;
    private ArrayAdapter<String> addout_arr_adapter2;
    private ArrayAdapter<String> addin_arr_adapter1;
    private ArrayAdapter<String> addin_arr_adapter2;
    private ArrayAdapter<String> timeout_month_adapter;
    private ArrayAdapter<String> timeout_day_adapter;
    private ArrayAdapter<String> timeout_starthour_adapter;
    private ArrayAdapter<String> timeout_endhour_adapter;
    private ArrayAdapter<String> timein_month_adapter;
    private ArrayAdapter<String> timein_day_adapter;
    private ArrayAdapter<String> timein_starthour_adapter;
    private ArrayAdapter<String> timein_endhour_adapter;
    private EditText content;
    private EditText coins;
    private EditText tele4;
    private String Content = "";//这个和手机尾号可以为空
    private String Coins = null;
    private String Tele4 = "";
    private int Size = -1;
    private int Out_Address[] = {-1,-1};
    private int In_Address[] = {-1,-1};
    private int OutTime[]={-1,-1,-1,-1};
    private int InTime[]={-1,-1,-1,-1};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atask_modify);
        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        TaskInfo = intent.getStringExtra(MainActivity.TASK_ID);
        //通过任务ID获取任务详情

        receiver = new MyReceiver(); // 注册广播
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.AReleaseTask");
        registerReceiver(receiver, filter);
        registnet();
//        connect();

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

        TASK.Initial(TaskInfo);
        Usrinfo = "用户名：zhoug15 金币：100 信用值：100";//这个需要从上级页面获得，麻烦国旺或者天石根据自己的页面改一下

        //初始化页面并设置当前任务信息
        SetInitInfo(TASK);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_atask_modify, menu);
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

    private void registnet(){
        if (networkstatereceiver == null) {
            networkstatereceiver = new NetworkStateReceiver();
        }
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkstatereceiver, filter);
        System.out.println("注册");
    }

    public void SetInitInfo(Task MyTask){
        TextView info = (TextView)findViewById(R.id.TaskModify_Info);
        info.setText(Usrinfo);

        Content = MyTask.GetContent();
        content = (EditText)findViewById(R.id.TaskModify_A_Content);
        content.setText(Content);

        Coins = Integer.toString(MyTask.GetCoins());
        coins = (EditText)findViewById(R.id.TaskModify_A_Coins);
        coins.setText(Coins);

        Tele4 = MyTask.GetLast4Tele();
        tele4 = (EditText)findViewById(R.id.TaskModify_A_Tele4);
        tele4.setText(Tele4);

        //界面里面13个下拉条选项
        Size = MyTask.GetSize();
        size_spinner = (Spinner) findViewById(R.id.TaskModify_A_Size);
        size_list = getResources().getStringArray(R.array.size);
        size_arr_adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,size_list);
        size_arr_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        size_spinner.setAdapter(size_arr_adapter);
        size_spinner.setSelection(Size);
        size_spinner.setOnItemSelectedListener(new sizeListener());

        Out_Address = MyTask.GetOutAddress();
        In_Address = MyTask.GetInAddress();

        add_spinner_Out1 = (Spinner) findViewById(R.id.TaskModify_A_OutAddress1);
        out_add_list1 = getResources().getStringArray(R.array.address1);
        addout_arr_adapter1 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,out_add_list1);
        addout_arr_adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        add_spinner_Out1.setAdapter(addout_arr_adapter1);
        add_spinner_Out1.setSelection(Out_Address[0] - 1);
        add_spinner_Out1.setOnItemSelectedListener(new OutAdd1Listener());

        add_spinner_In1 = (Spinner) findViewById(R.id.TaskModify_A_InAddress1);
        in_add_list1 = getResources().getStringArray(R.array.address1);
        addin_arr_adapter1 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,in_add_list1);
        addin_arr_adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        add_spinner_In1.setAdapter(addin_arr_adapter1);
        add_spinner_In1.setSelection(In_Address[0] - 1);
        add_spinner_In1.setOnItemSelectedListener(new InAdd1Listener());

        add_spinner_Out2 = (Spinner) findViewById(R.id.TaskModify_A_OutAddress2);
        out_add_list2 = getResources().getStringArray(R.array.address21);
        addout_arr_adapter2 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,out_add_list2);
        addout_arr_adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        add_spinner_Out2.setAdapter(addout_arr_adapter2);
        add_spinner_Out2.setSelection(Out_Address[1] - 1);
        add_spinner_Out2.setOnItemSelectedListener(new OutAdd2Listener());

        add_spinner_In2 = (Spinner) findViewById(R.id.TaskModify_A_InAddress2);
        in_add_list2 = getResources().getStringArray(R.array.address21);
        addin_arr_adapter2 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,in_add_list2);
        addin_arr_adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        add_spinner_In2.setAdapter(addin_arr_adapter2);
        add_spinner_In2.setSelection(In_Address[1] - 1);
        add_spinner_In2.setOnItemSelectedListener(new InAdd2Listener());

        OutTime = MyTask.GetOutTime();
        InTime = MyTask.GetInTime();
        time_month = getResources().getStringArray(R.array.mon);
        time_hour = getResources().getStringArray(R.array.hour);
        time_day = new String[31];
        for(int i=0;i<31;i++){time_day[i]=Integer.toString(i+1);}//二月暂时都是29天

        timeout_month = (Spinner) findViewById(R.id.TaskModify_A_OutTimeMonth);
        timeout_month_adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,time_month);
        timeout_month_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        timeout_month.setAdapter(timeout_month_adapter);
        timeout_month.setSelection(OutTime[0] - 1);
        timeout_month.setOnItemSelectedListener(new OutMonthListener());

        timeout_day = (Spinner) findViewById(R.id.TaskModify_A_OutTimeDay);
        timeout_day_adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,time_day);
        timeout_day_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        timeout_day.setAdapter(timeout_day_adapter);
        timeout_day.setSelection(OutTime[1] - 1);
        timeout_day.setOnItemSelectedListener(new OutDayListener());

        timeout_starthour = (Spinner) findViewById(R.id.TaskModify_A_OutTimeStartHour);
        timeout_starthour_adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,time_hour);
        timeout_starthour_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        timeout_starthour.setAdapter(timeout_starthour_adapter);
        timeout_starthour.setSelection(OutTime[2] - 1);
        timeout_starthour.setOnItemSelectedListener(new OutStartHourListener());

        timeout_endhour = (Spinner) findViewById(R.id.TaskModify_A_OutTimeEndHour);
        timeout_endhour_adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,time_hour);
        timeout_endhour_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        timeout_endhour.setAdapter(timeout_endhour_adapter);
        timeout_endhour.setSelection(OutTime[3] - 1);
        timeout_endhour.setOnItemSelectedListener(new OutEndHourListener());

        timein_month = (Spinner) findViewById(R.id.TaskModify_A_InTimeMonth);
        timein_month_adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,time_month);
        timein_month_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        timein_month.setAdapter(timein_month_adapter);
        timein_month.setSelection(InTime[0] - 1);
        timein_month.setOnItemSelectedListener(new InMonthListener());

        timein_day = (Spinner) findViewById(R.id.TaskModify_A_InTimeDay);
        timein_day_adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,time_day);
        timein_day_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        timein_day.setAdapter(timein_day_adapter);
        timein_day.setSelection(InTime[1] - 1);
        timein_day.setOnItemSelectedListener(new InDayListener());

        timein_starthour = (Spinner) findViewById(R.id.TaskModify_A_InTimeStartHour);
        timein_starthour_adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,time_hour);
        timein_starthour_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        timein_starthour.setAdapter(timein_starthour_adapter);
        timein_starthour.setSelection(InTime[2] - 1);
        timein_starthour.setOnItemSelectedListener(new InStartHourListener());

        timein_endhour = (Spinner) findViewById(R.id.TaskModify_A_InTimeEndHour);
        timein_endhour_adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,time_hour);
        timein_endhour_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        timein_endhour.setAdapter(timein_endhour_adapter);
        timein_endhour.setSelection(InTime[3]-1);
        timein_endhour.setOnItemSelectedListener(new InEndHourListener());

    }

    public void EditConfirm(View view) {
        AlertDialog.Builder alertdialogbuilder = new AlertDialog.Builder(this);
        alertdialogbuilder.setMessage("修改影响信誉！");
        alertdialogbuilder.setPositiveButton("确定", TaskEdit);
        alertdialogbuilder.setNegativeButton("取消", cancel);
        AlertDialog alertdialog1 = alertdialogbuilder.create();
        alertdialog1.show();
    }

    //检查是否全为数字，用于判断输入合法性
    public static boolean isNumeric(String str) {
        for (int i = str.length(); --i >= 0;) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    private DialogInterface.OnClickListener TaskEdit=new DialogInterface.OnClickListener() {
        // 转到撤销任务页面
        @Override
        public void onClick(DialogInterface arg0, int arg1) {
            boolean IsLegal = true;//检查输入合法性
            Content = content.getText().toString();
            if(Content.indexOf('&')!=-1){IsLegal = false;System.out.print("0");}//输入不能有&

            Coins = coins.getText().toString();
            if(Coins.length() == 0){IsLegal = false;System.out.print("1");}//金币不能为空
            if(isNumeric(Coins) == false){IsLegal = false;System.out.print("2");}//金币必须使用数字

            Tele4 = tele4.getText().toString();
            if((Tele4.length() != 4)&&(Tele4.length()!=0)){IsLegal = false;System.out.print(Tele4.length());}//手机号码必须为四位或者不填
            if(isNumeric(Tele4) == false){IsLegal = false;System.out.print("4");}//手机必须使用数字

            if(!IsLegal){System.out.println("输入不合法");}
            //      byte[]  msgBuffer = null;
            final String Str2sql = "&51"+"&01"+Coins//金币
                    +"&04"+Content//任务描述
                    +"&07"+"周乾"//甲方用户
                    +"&09"+Integer.toString(Size)//物件大小/重量
                    +"&02"+String.format("%02d",In_Address[0])+String.format("%02d", In_Address[1])//取快递地址
                    +"&03"+String.format("%02d",InTime[0])+String.format("%02d",InTime[1])+String.format("%02d",InTime[2])+String.format("%02d",InTime[3])//取快递时间
                    +"&10"+String.format("%02d",Out_Address[0])+String.format("%02d", Out_Address[1])//交接快递地址
                    +"&11"+String.format("%02d",OutTime[0])+String.format("%02d",OutTime[1])+String.format("%02d",OutTime[2])+String.format("%02d",OutTime[3])//交接快递时间
                    +"&12"+Tele4;//手机尾号
            //     msgBuffer = Str2sql.getBytes();
            //       sent(msgBuffer);
//        st=Str2sql;

            //发送前需要检查网络情况！！！！！！！！！！！！
            if((networkstatereceiver.Isdata==true||networkstatereceiver.Iswifi==true)&&IsLegal){
                System.out.println(Str2sql);
                //sendmessage(Str2sql);
            }
        }
    };
    private DialogInterface.OnClickListener cancel=new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface arg0,int arg1)
        {
            arg0.cancel();
        }
    };

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
                //EditText taskname = (EditText)findViewById(R.id.TaskModify_A_TaskName);
                //taskname.setText(s);
            }
        }
    }

    @Override
    protected void onStart(){
        super.onStart();
        System.out.println("onstart");
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    //onPause()方法注销
    @Override
    protected void onPause() {
        unregisterReceiver(networkstatereceiver);
        unregisterReceiver(receiver);
        System.out.println("注销");
        super.onPause();
    }

    private class MyReceiver extends BroadcastReceiver { // 接收service传来的信息
        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            if (intent.getAction().equals("android.intent.action.AModifyTask")) {
                Bundle bundle = intent.getExtras();
                String str =bundle.getString("str");


/*				if (OK) {
					stateflag = 1   ;
				} else if (cmd == CMD_RECEIVE_DATA) {
					String str = bundle.getString("str");
					Message msg = new Message();
					msg.obj = str;
					mHandler.sendMessage(msg);
				}
				*/
            }
        }
    }

    //这后面是13个下拉菜单的接收器
    class sizeListener implements AdapterView.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view,
                                   int position, long id) {
            String selected = parent.getItemAtPosition(position).toString();
            Size = position;
            System.out.println(selected);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            System.out.println("nothingSelect");
        }
    };


    class OutAdd1Listener implements AdapterView.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view,
                                   int position, long id) {
            String selected = parent.getItemAtPosition(position).toString();
            System.out.println(selected);
            Out_Address[0] = position+1;
            switch(position){
                case 0:
                    out_add_list2 = getResources().getStringArray(R.array.address21);
                    break;
                case 1:
                    out_add_list2 = getResources().getStringArray(R.array.address22);
                    break;
                case 2:
                    out_add_list2 = getResources().getStringArray(R.array.address23);
                    break;
                case 3:
                    out_add_list2 = getResources().getStringArray(R.array.address24);
                    break;
                case 4:
                    out_add_list2 = getResources().getStringArray(R.array.address25);
                    break;
            }
            addout_arr_adapter2 = new ArrayAdapter<String>(ATaskModify.this,android.R.layout.simple_spinner_item,out_add_list2);
            add_spinner_Out2.setAdapter(addout_arr_adapter2);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            System.out.println("nothingSelect");
        }
    };

    class OutAdd2Listener implements AdapterView.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view,
                                   int position, long id) {
            String selected = parent.getItemAtPosition(position).toString();
            Out_Address[1] = position+1;
            System.out.println(selected);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            System.out.println("nothingSelect");
        }
    };

    class InAdd1Listener implements AdapterView.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view,
                                   int position, long id) {
            String selected = parent.getItemAtPosition(position).toString();
            System.out.println(selected);
            In_Address[0] = position+1;
            switch(position){
                case 0:
                    in_add_list2 = getResources().getStringArray(R.array.address21);
                    break;
                case 1:
                    in_add_list2 = getResources().getStringArray(R.array.address22);
                    break;
                case 2:
                    in_add_list2 = getResources().getStringArray(R.array.address23);
                    break;
                case 3:
                    in_add_list2 = getResources().getStringArray(R.array.address24);
                    break;
                case 4:
                    in_add_list2 = getResources().getStringArray(R.array.address25);
                    break;
            }
            addin_arr_adapter2 = new ArrayAdapter<String>(ATaskModify.this,android.R.layout.simple_spinner_item,in_add_list2);
            addin_arr_adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            add_spinner_In2.setAdapter(addin_arr_adapter2);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            System.out.println("nothingSelect");
        }
    };

    class InAdd2Listener implements AdapterView.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view,
                                   int position, long id) {
            String selected = parent.getItemAtPosition(position).toString();
            In_Address[1] = position+1;
            System.out.println(selected);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            System.out.println("nothingSelect");
        }
    };

    class OutMonthListener implements AdapterView.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view,
                                   int position, long id) {
            String selected = parent.getItemAtPosition(position).toString();
            int days = 0;
            OutTime[0]=position+1;

            Integer[] day31 = {1,3,5,7,8,10,12};
            Integer[] day30 = {4,6,9,11};
            List<Integer> list31 = Arrays.asList(day31);
            List<Integer> list30 = Arrays.asList(day30);
            if(list31.contains(position+1)){days = 31;System.out.print(31);}
            else if(list30.contains(position+1)){days = 30;System.out.print(30);}
            else {days = 29;}
            time_day = new String[days];
            for(int i=0;i<days;i++){time_day[i]=Integer.toString(i+1);
                System.out.println(time_day[i]);}

            timeout_day_adapter = new ArrayAdapter<String>(ATaskModify.this,android.R.layout.simple_spinner_item,time_day);
            timeout_day_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            timeout_day.setAdapter(timeout_day_adapter);

            System.out.println(selected);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            System.out.println("nothingSelect");
        }
    };
    class OutDayListener implements AdapterView.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view,
                                   int position, long id) {
            String selected = parent.getItemAtPosition(position).toString();
            OutTime[1]=position+1;
            System.out.println(selected);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            System.out.println("nothingSelect");
        }
    };
    class OutStartHourListener implements AdapterView.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view,
                                   int position, long id) {
            String selected = parent.getItemAtPosition(position).toString();
            OutTime[2]=position+1;
            System.out.println(selected);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            System.out.println("nothingSelect");
        }
    };
    class OutEndHourListener implements AdapterView.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view,
                                   int position, long id) {
            String selected = parent.getItemAtPosition(position).toString();
            OutTime[3]=position+1;
            System.out.println(selected);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            System.out.println("nothingSelect");
        }
    };
    class InMonthListener implements AdapterView.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view,
                                   int position, long id) {
            String selected = parent.getItemAtPosition(position).toString();
            int days = 0;
            InTime[0]=position+1;

            Integer[] day31 = {1,3,5,7,8,10,12};
            Integer[] day30 = {4,6,9,11};
            List<Integer> list31 = Arrays.asList(day31);
            List<Integer> list30 = Arrays.asList(day30);
            if(list31.contains(position+1)){days = 31;System.out.print(31);}
            else if(list30.contains(position+1)){days = 30;System.out.print(30);}
            else {days = 29;}
            time_day = new String[days];
            for(int i=0;i<days;i++){time_day[i]=Integer.toString(i+1);
                System.out.println(time_day[i]);}

            timein_day_adapter = new ArrayAdapter<String>(ATaskModify.this,android.R.layout.simple_spinner_item,time_day);
            timein_day_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            timein_day.setAdapter(timein_day_adapter);

            System.out.println(selected);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            System.out.println("nothingSelect");
        }
    };
    class InDayListener implements AdapterView.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view,
                                   int position, long id) {
            String selected = parent.getItemAtPosition(position).toString();
            InTime[1]=position+1;
            System.out.println(selected);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            System.out.println("nothingSelect");
        }
    };
    class InStartHourListener implements AdapterView.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view,
                                   int position, long id) {
            String selected = parent.getItemAtPosition(position).toString();
            InTime[2]=position+1;
            System.out.println(selected);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            System.out.println("nothingSelect");
        }
    };
    class InEndHourListener implements AdapterView.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view,
                                   int position, long id) {
            String selected = parent.getItemAtPosition(position).toString();
            InTime[3]=position+1;
            System.out.println(selected);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            System.out.println("nothingSelect");
        }
    };
}
