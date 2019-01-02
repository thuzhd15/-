package com.example.lazy_man_client;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
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
import android.widget.Toast;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AReleaseTask extends Activity {
    Task TASK;
    Usr USR = new Usr();
    String Usrinfo;
    private Socket socket;
    MyReceiver receiver;
    private Handler mHandler;
    NetworkStateReceiver networkstatereceiver;
    private Spinner size_spinner;
    private Spinner add_spinner_In1;
    private Spinner add_spinner_Out1;
    private Spinner add_spinner_In2;
    private boolean[] out = {true,true,true};
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
    private List<String> olists = new ArrayList<String>();
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
    private String Content = "";//������ֻ�β�ſ���Ϊ��
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
        setContentView(R.layout.activity_arelease_task);

        receiver = new MyReceiver(); // ע��㲥
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.AReleaseTask");
        registerReceiver(receiver, filter);
        
        Usrinfo = "�û�����zhoug15 ��ң�100 ����ֵ��100";//�����Ҫ���ϼ�ҳ���ã��鷳����������ʯ�����Լ���ҳ���һ��

        mHandler = new Handler() {
            public void handleMessage(android.os.Message msg) {
                String str = msg.obj.toString();
                //				Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT)
                //						.show();
                String substr = str.substring(0, 3);
                if (substr.equals("&03")) { // ��ʼ��
                    USR.Initial(str);
                    System.out.println(str);

                    int[][] add = {{0,0},{0,0},{0,0}};
                    add[0] = USR.GetAddress1();
                    add[1] = USR.GetAddress2();
                    add[2] = USR.GetAddress3();
                    for(int i=0;i<3;i++){
                        if(add[i][0]==0){out[i]=false;}
                        else{olists.add(Data_all.Section[add[i][0]]+Data_all.Address[add[i][0]][add[i][1]]);}
                    }

                    add_spinner_Out2 = (Spinner) findViewById(R.id.TaskRelease_A_OutAddress2);
                    //out_add_list2 = getResources().getStringArray(R.array.address21);
                    addout_arr_adapter2 = new ArrayAdapter<String>(AReleaseTask.this,android.R.layout.simple_spinner_item,olists);
                    addout_arr_adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    add_spinner_Out2.setAdapter(addout_arr_adapter2);
                    add_spinner_Out2.setOnItemSelectedListener(new OutAdd2Listener());
                }
                else if(substr.equals("&50")){

                	Toast.makeText(getApplicationContext(), "��������ɹ���", Toast.LENGTH_LONG)
					.show();
                }
            };
        };

        String GetUsrInfo = "&03&06" + Data_all.User_ID;
        sent(GetUsrInfo);

        Init(); // ��ʼ��ҳ��
    }
    
    /*    @Override
    protected void onResume() {
        if (networkstatereceiver == null) {
            networkstatereceiver = new NetworkStateReceiver();
        }
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkstatereceiver, filter);
        System.out.println("ע��");
        super.onResume();
    }*/

    //onPause()����ע��
    @Override
    protected void onPause() {
//        unregisterReceiver(networkstatereceiver);
        unregisterReceiver(receiver);
        System.out.println("ע��");
        super.onPause();
    }

    public void Init(){//��ʼ���������пؼ�
        TextView info = (TextView)findViewById(R.id.TaskRelease_Info);
        info.setText(Usrinfo);

        //���ý�������13��������ѡ��
        size_spinner = (Spinner) findViewById(R.id.TaskRelease_A_Size);
        size_list = getResources().getStringArray(R.array.size);
        size_arr_adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,size_list);
        size_arr_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        size_spinner.setAdapter(size_arr_adapter);
        size_spinner.setOnItemSelectedListener(new sizeListener());
/*
        add_spinner_Out1 = (Spinner) findViewById(R.id.TaskRelease_A_OutAddress1);
        out_add_list1 = getResources().getStringArray(R.array.address1);
        addout_arr_adapter1 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,out_add_list1);
        addout_arr_adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        add_spinner_Out1.setAdapter(addout_arr_adapter1);
        add_spinner_Out1.setOnItemSelectedListener(new OutAdd1Listener());
*/

        add_spinner_In1 = (Spinner) findViewById(R.id.TaskRelease_A_InAddress1);
        in_add_list1 = getResources().getStringArray(R.array.address1);
        addin_arr_adapter1 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,in_add_list1);
        addin_arr_adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        add_spinner_In1.setAdapter(addin_arr_adapter1);
        add_spinner_In1.setOnItemSelectedListener(new InAdd1Listener());


        add_spinner_In2 = (Spinner) findViewById(R.id.TaskRelease_A_InAddress2);
        in_add_list2 = getResources().getStringArray(R.array.address21);
        addin_arr_adapter2 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,in_add_list2);
        addin_arr_adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        add_spinner_In2.setAdapter(addin_arr_adapter2);
        add_spinner_In2.setOnItemSelectedListener(new InAdd2Listener());

        time_month = getResources().getStringArray(R.array.mon);
        time_hour = getResources().getStringArray(R.array.hour);
        time_day = new String[31];
        for(int i=0;i<31;i++){time_day[i]=Integer.toString(i+1);}//������ʱ����29��

        timeout_month = (Spinner) findViewById(R.id.TaskRelease_A_OutTimeMonth);
        timeout_month_adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,time_month);
        timeout_month_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        timeout_month.setAdapter(timeout_month_adapter);
        timeout_month.setOnItemSelectedListener(new OutMonthListener());

        timeout_day = (Spinner) findViewById(R.id.TaskRelease_A_OutTimeDay);
        timeout_day_adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,time_day);
        timeout_day_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        timeout_day.setAdapter(timeout_day_adapter);
        timeout_day.setOnItemSelectedListener(new OutDayListener());

        timeout_starthour = (Spinner) findViewById(R.id.TaskRelease_A_OutTimeStartHour);
        timeout_starthour_adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,time_hour);
        timeout_starthour_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        timeout_starthour.setAdapter(timeout_starthour_adapter);
        timeout_starthour.setOnItemSelectedListener(new OutStartHourListener());

        timeout_endhour = (Spinner) findViewById(R.id.TaskRelease_A_OutTimeEndHour);
        timeout_endhour_adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,time_hour);
        timeout_endhour_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        timeout_endhour.setAdapter(timeout_endhour_adapter);
        timeout_endhour.setOnItemSelectedListener(new OutEndHourListener());

        timein_month = (Spinner) findViewById(R.id.TaskRelease_A_InTimeMonth);
        timein_month_adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,time_month);
        timein_month_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        timein_month.setAdapter(timein_month_adapter);
        timein_month.setOnItemSelectedListener(new InMonthListener());

        timein_day = (Spinner) findViewById(R.id.TaskRelease_A_InTimeDay);
        timein_day_adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,time_day);
        timein_day_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        timein_day.setAdapter(timein_day_adapter);
        timein_day.setOnItemSelectedListener(new InDayListener());

        timein_starthour = (Spinner) findViewById(R.id.TaskRelease_A_InTimeStartHour);
        timein_starthour_adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,time_hour);
        timein_starthour_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        timein_starthour.setAdapter(timein_starthour_adapter);
        timein_starthour.setOnItemSelectedListener(new InStartHourListener());

        timein_endhour = (Spinner) findViewById(R.id.TaskRelease_A_InTimeEndHour);
        timein_endhour_adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,time_hour);
        timein_endhour_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        timein_endhour.setAdapter(timein_endhour_adapter);
        timein_endhour.setOnItemSelectedListener(new InEndHourListener());

        content = (EditText)findViewById(R.id.TaskRelease_A_Content);
        coins = (EditText)findViewById(R.id.TaskRelease_A_Coins);
        tele4 = (EditText)findViewById(R.id.TaskRelease_A_Tele4);
    }

    //����Ƿ�ȫΪ���֣������ж�����Ϸ���
    public static boolean isNumeric(String str) {
        for (int i = str.length(); --i >= 0;) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public void Getinfo(View view){ //���ͷ�����������
        boolean IsLegal = true;//�������Ϸ���
        Content = content.getText().toString();
		if (Content.length() == 0) {
			IsLegal = false;
			showToast("������������Ϊ�գ�");
		} // ������������Ϊ��
		if (Content.indexOf('&') != -1) {
			IsLegal = false;
			showToast("�����в��ܺ���&��");
		} // ���벻����&

		Coins = coins.getText().toString();
		if (Coins.length() == 0) {
			IsLegal = false;
			showToast("���ͽ��ֵ����Ϊ�գ�");
		}// ��Ҳ���Ϊ��
		if (isNumeric(Coins) == false) {
			IsLegal = false;
			showToast("���ֵ����ʹ�����֣�");
		}// ��ұ���ʹ������

		Tele4 = tele4.getText().toString();
		if ((Tele4.length() != 4) && (Tele4.length() != 0)) {
			IsLegal = false;
			showToast("�ֻ�β�ű���Ϊ4λ���ֻ���Ϊ�գ�");
		} else if (isNumeric(Tele4) == false) {
			IsLegal = false;
			showToast("�ֻ�β�ű���Ϊ4λ���ֻ���Ϊ�գ�");
			;
		}

        if(!IsLegal){System.out.println("���벻�Ϸ�");}
        final String Str2sql = "&50"+"&01"+Coins//���
                +"&04"+Content//��������
                +"&07"+Data_all.User_ID//�׷��û�
                +"&09"+Integer.toString(Size)//�����С/����
                +"&02"+String.format("%02d",In_Address[0])+String.format("%02d", In_Address[1])//ȡ��ݵ�ַ
                +"&03"+String.format("%02d",InTime[0])+String.format("%02d",InTime[1])+String.format("%02d",InTime[2])+String.format("%02d",InTime[3])//ȡ���ʱ��
                +"&10"+String.format("%02d",Out_Address[0])+String.format("%02d", Out_Address[1])//���ӿ�ݵ�ַ
                +"&11"+String.format("%02d",OutTime[0])+String.format("%02d",OutTime[1])+String.format("%02d",OutTime[2])+String.format("%02d",OutTime[3])//���ӿ��ʱ��
                +"&12"+Tele4;//�ֻ�β��

        if(IsLegal) {
        	sent(Str2sql); //�����ַ���
        }
        
/*        if((networkstatereceiver.Isdata==true||networkstatereceiver.Iswifi==true)&&IsLegal){
            System.out.println(Str2sql);           
            //sendmessage(Str2sql);
        }*/
    }

    private void sendmessage(final String data){//�������֮���ù�����
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // ����
                    OutputStream outputStream = socket.getOutputStream();
                    outputStream.write(("IP:" + getHostIp() + " " + data).getBytes("utf-8"));
                    outputStream.flush();// ��ջ���

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    // ��ȡIP��ת����ʽ
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

    private class MyReceiver extends BroadcastReceiver { // ����service��������Ϣ
        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            if (intent.getAction().equals(
                    "android.intent.action.AReleaseTask")) {
                Bundle bundle = intent.getExtras();
                // if (cmd == CMD_SHOW_TOAST) {
                // String str = bundle.getString("str");
                // showToast(str);
                // } else if (cmd == CMD_RECEIVE_DATA) {
                String str = bundle.getString("str");
                Message msg = new Message();
                msg.obj = str;
                mHandler.sendMessage(msg);
                // }
            }
        }
    }

    public void sent(String bs){  //ͨ��Service��������
        Intent intent = new Intent();//����Intent����
        intent.setAction("android.intent.action.cmd");
        intent.putExtra("value", bs);
        sendBroadcast(intent);//���͹㲥
    }

    public void showToast(String str) {// ��ʾ��ʾ��Ϣ
		Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT).show();
	}
    
    //�������13�������˵��Ľ�����
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
            addout_arr_adapter2 = new ArrayAdapter<String>(AReleaseTask.this,android.R.layout.simple_spinner_item,out_add_list2);
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
            int pos=0;
			for (int i = 0; i <= position; i++, pos++) {
				if (out[pos] == false) {
					pos++;
				}
			}

            switch(pos){
                case 1:
                    Out_Address = USR.GetAddress1();
                    break;
                case 2:
                    Out_Address = USR.GetAddress2();
                    break;
                case 3:
                    Out_Address = USR.GetAddress3();
                    break;
            }

            //Out_Address[1] = position+1;
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
            addin_arr_adapter2 = new ArrayAdapter<String>(AReleaseTask.this,android.R.layout.simple_spinner_item,in_add_list2);
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

            timeout_day_adapter = new ArrayAdapter<String>(AReleaseTask.this,android.R.layout.simple_spinner_item,time_day);
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

            timein_day_adapter = new ArrayAdapter<String>(AReleaseTask.this,android.R.layout.simple_spinner_item,time_day);
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
