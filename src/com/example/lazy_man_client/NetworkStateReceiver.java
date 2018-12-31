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

        System.out.println("缃戠粶鐘舵�佸彂鐢熷彉鍖�");
        //妫�娴婣PI鏄笉鏄皬浜�23锛屽洜涓哄埌浜咥PI23涔嬪悗getNetworkInfo(int networkType)鏂规硶琚純鐢�
        if (true) {

            //鑾峰緱ConnectivityManager瀵硅薄
            ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            //鑾峰彇ConnectivityManager瀵硅薄瀵瑰簲鐨凬etworkInfo瀵硅薄
            //鑾峰彇WIFI杩炴帴鐨勪俊鎭�
            NetworkInfo wifiNetworkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            //鑾峰彇绉诲姩鏁版嵁杩炴帴鐨勪俊鎭�
            NetworkInfo dataNetworkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (wifiNetworkInfo.isConnected() && dataNetworkInfo.isConnected()) {
                Toast.makeText(context, "WIFI宸茶繛鎺�,绉诲姩鏁版嵁宸茶繛鎺�", Toast.LENGTH_SHORT).show();
                Iswifi = true;
                Isdata = true;
            } else if (wifiNetworkInfo.isConnected() && !dataNetworkInfo.isConnected()) {
                Toast.makeText(context, "WIFI宸茶繛鎺�,绉诲姩鏁版嵁宸叉柇寮�", Toast.LENGTH_SHORT).show();
                Iswifi = true;
                Isdata = false;
            } else if (!wifiNetworkInfo.isConnected() && dataNetworkInfo.isConnected()) {
                Toast.makeText(context, "WIFI宸叉柇寮�,绉诲姩鏁版嵁宸茶繛鎺�", Toast.LENGTH_SHORT).show();
                Iswifi = false;
                Isdata = true;
            } else {
                Toast.makeText(context, "WIFI宸叉柇寮�,绉诲姩鏁版嵁宸叉柇寮�", Toast.LENGTH_SHORT).show();
                Iswifi = false;
                Isdata = false;
            }
//API澶т簬23鏃朵娇鐢ㄤ笅闈㈢殑鏂瑰紡杩涜缃戠粶鐩戝惉
        }else {

            System.out.println("API level 澶т簬23");
            //鑾峰緱ConnectivityManager瀵硅薄
            ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            //鑾峰彇鎵�鏈夌綉缁滆繛鎺ョ殑淇℃伅
           /* Network[] networks = connMgr.getAllNetworks();
            //鐢ㄤ簬瀛樻斁缃戠粶杩炴帴淇℃伅
            StringBuilder sb = new StringBuilder();
            //閫氳繃寰幆灏嗙綉缁滀俊鎭�愪釜鍙栧嚭鏉�
            for (int i=0; i < networks.length; i++){
                //鑾峰彇ConnectivityManager瀵硅薄瀵瑰簲鐨凬etworkInfo瀵硅薄
                NetworkInfo networkInfo = connMgr.getNetworkInfo(networks[i]);
                sb.append(networkInfo.getTypeName() + " connect is " + networkInfo.isConnected());
            }
            Toast.makeText(context, sb.toString(),Toast.LENGTH_SHORT).show();*/
        }
    }
}