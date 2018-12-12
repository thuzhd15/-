package test; // you need to change the package name according to your own project
import java.io.*;
import java.net.*;
public class client {
    public static void main(String[] args) throws Exception{
        String strLocal, strSocket; 

        Socket socketClient = new Socket("localhost", 20000);
        BufferedReader brInFromUser = new BufferedReader(new InputStreamReader(System.in)); // 这里若使用UTF-8，遇到中文字符会出错！
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
        	
        	// resolve the string
        	Usr user = new Usr();
         	user.Initial(strSocket);
         	System.out.println("error_type:" + user.GetErrorType() + " " + "UsrID:" + user.GetUsrID() 
         	+ " " + "UsrName:" + user.GetUsrName() + " " + "RealName:" + user.GetRealName() 
         	+ " " + "Address1:" + user.GetAddress1() + " " + "Address2:" + user.GetAddress2() 
         	+ " " + "Address3:" + user.GetAddress3() + " " + "TeleNumber:" + user.GetTeleNumber()
         	+ " " + "Email:" + user.GetEmail() + " " + "School:" + user.GetSchool()
         	+ " " + "Coins:" + user.GetCoins() + " " + "Credit:" + user.GetCredit() );
        }
        while(!strSocket.equals("bye"));
        
        socketClient.close();
    } 
}
