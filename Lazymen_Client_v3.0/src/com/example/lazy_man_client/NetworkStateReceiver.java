package com.example.lazy_man_client;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
//import android.net.Network;
import android.net.NetworkInfo;
import android.widget.Toast;

/**
 * Created by Carson_Ho on 16/10/31.
 */
public class NetworkStateReceiver extends BroadcastReceiver {
    public boolean Iswifi = false;
    public boolean Isdata = false;
    @Override
    public void onReceive(Context context, Intent intent) {

        System.out.println("����״̬�����仯");
        //���API�ǲ���С��23����Ϊ����API23֮��getNetworkInfo(int networkType)����������
        if (true) {

            //���ConnectivityManager����
            ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            //��ȡConnectivityManager�����Ӧ��NetworkInfo����
            //��ȡWIFI���ӵ���Ϣ
            NetworkInfo wifiNetworkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            //��ȡ�ƶ��������ӵ���Ϣ
            NetworkInfo dataNetworkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (wifiNetworkInfo.isConnected() && dataNetworkInfo.isConnected()) {
                Toast.makeText(context, "WIFI������,�ƶ�����������", Toast.LENGTH_SHORT).show();
                Iswifi = true;
                Isdata = true;
            } else if (wifiNetworkInfo.isConnected() && !dataNetworkInfo.isConnected()) {
                Toast.makeText(context, "WIFI������,�ƶ������ѶϿ�", Toast.LENGTH_SHORT).show();
                Iswifi = true;
                Isdata = false;
            } else if (!wifiNetworkInfo.isConnected() && dataNetworkInfo.isConnected()) {
                Toast.makeText(context, "WIFI�ѶϿ�,�ƶ�����������", Toast.LENGTH_SHORT).show();
                Iswifi = false;
                Isdata = true;
            } else {
                Toast.makeText(context, "WIFI�ѶϿ�,�ƶ������ѶϿ�", Toast.LENGTH_SHORT).show();
                Iswifi = false;
                Isdata = false;
            }
//API����23ʱʹ������ķ�ʽ�����������
        }else {

            System.out.println("API level ����23");
            //���ConnectivityManager����
            ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            //��ȡ�����������ӵ���Ϣ
           /* Network[] networks = connMgr.getAllNetworks();
            //���ڴ������������Ϣ
            StringBuilder sb = new StringBuilder();
            //ͨ��ѭ����������Ϣ���ȡ����
            for (int i=0; i < networks.length; i++){
                //��ȡConnectivityManager�����Ӧ��NetworkInfo����
                NetworkInfo networkInfo = connMgr.getNetworkInfo(networks[i]);
                sb.append(networkInfo.getTypeName() + " connect is " + networkInfo.isConnected());
            }
            Toast.makeText(context, sb.toString(),Toast.LENGTH_SHORT).show();*/
        }
    }
}