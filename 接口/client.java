package test; // you need to change the package name according to your own project
import java.io.*;
import java.net.*;
public class client {
    public static void main(String[] args) throws Exception{
        String strLocal, strSocket; 

        Socket socketClient = new Socket("localhost", 20000);
        BufferedReader brInFromUser = new BufferedReader(new InputStreamReader(System.in)); // ÕâÀïÈôÊ¹ÓÃUTF-8£¬Óöµ½ÖĞÎÄ×Ö·û»á³ö´í£¡
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
        	
        	// ÒÔÏÂÒ»ĞĞ´úÂë¿É×÷Îª»ñÈ¡µØÖ·ÁĞ±íµÄ²âÊÔinput
        	// strSocket = "&03&000&1914&200&21Áù½Ì6A214&201&21×Ï¾£1ºÅÂ¥&202&21×Ï¾£2ºÅÂ¥&203&21×Ï¾£3ºÅÂ¥&204&21×Ï¾£4ºÅÂ¥&205&21×Ï¾£5ºÅÂ¥&206&21×Ï¾£6ºÅÂ¥&207&21×Ï¾£7ºÅÂ¥&208&21×Ï¾£8ºÅÂ¥&209&21×Ï¾£9ºÅÂ¥&2010&21×Ï¾£10ºÅÂ¥&2011&21×Ï¾£11ºÅÂ¥&2012&21×Ï¾£12ºÅÂ¥&2013&21×Ï¾£13ºÅÂ¥";
        	
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
