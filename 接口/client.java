package test; // you need to change the package name according to your own project
import java.io.*;
import java.net.*;
public class client {
    public static void main(String[] args) throws Exception{
        String strLocal, strSocket; 

        Socket socketClient = new Socket("localhost", 20000);
        BufferedReader brInFromUser = new BufferedReader(new InputStreamReader(System.in)); // ������ʹ��UTF-8�����������ַ������
        BufferedReader brInFromServer = new BufferedReader(new InputStreamReader(socketClient.getInputStream(),"UTF-8"));
	    BufferedWriter dosOutToServer = new BufferedWriter(new OutputStreamWriter(socketClient.getOutputStream(),"UTF-8"));
	    
        do
        {
        	strLocal = brInFromUser.readLine();
        	System.out.println("Client: " + strLocal);
        	dosOutToServer.write(strLocal + '\n');
        	dosOutToServer.flush();
        	strSocket = brInFromServer.readLine();
        	System.out.println("Server: " + strSocket);
        	
        	// ����һ�д������Ϊ��ȡ��ַ�б�Ĳ���input
        	// strSocket = "&03&000&1914&200&21����6A214&201&21�Ͼ�1��¥&202&21�Ͼ�2��¥&203&21�Ͼ�3��¥&204&21�Ͼ�4��¥&205&21�Ͼ�5��¥&206&21�Ͼ�6��¥&207&21�Ͼ�7��¥&208&21�Ͼ�8��¥&209&21�Ͼ�9��¥&2010&21�Ͼ�10��¥&2011&21�Ͼ�11��¥&2012&21�Ͼ�12��¥&2013&21�Ͼ�13��¥";
        	
        	// resolve the string
        	Usr user = new Usr();
         	user.Initial(strSocket);
         	
         	System.out.println("error_type:" + user.GetErrorType() + " " + "UsrID:" + user.GetUsrID() 
         	+ " " + "UsrName:" + user.GetUsrName() + " " + "RealName:" + user.GetRealName() 
         	+ " " + "Address1:" + user.GetAddress1() + " " + "Address2:" + user.GetAddress2() 
         	+ " " + "Address3:" + user.GetAddress3() + " " + "TeleNumber:" + user.GetTeleNumber()
         	+ " " + "Email:" + user.GetEmail() + " " + "School:" + user.GetSchool()
         	+ " " + "Coins:" + user.GetCoins() + " " + "Credit:" + user.GetCredit() );
         	
         	String[] addr_list = user.GetAddrList();
			if (addr_list != null)
				for (int i = 0; i < addr_list.length; i++) {
					System.out.println("Address " + i + ": " + addr_list[i]);
				}
        }
        while(!strSocket.equals("bye"));
        
        socketClient.close();
    } 
}
