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
        	
        	// 下面几行被注释的代码可作为获取用户信息和所有地址列表的测试input
        	/* strSocket = "&03&000&012015012031&02zhoug15&0312345678901&04zhoug15@mails.tsinghua.edu.cn"
        	+ "&058&06郝青硕&08水利工程学院&094&103&11100&12100"
        	+ "&1914&200&21六教6A214&201&21紫荆1号楼&202&21紫荆2号楼&203&21紫荆3号楼&204&21紫荆4号楼&205&21紫荆5号楼"
        	+ "&206&21紫荆6号楼&207&21紫荆7号楼&208&21紫荆8号楼&209&21紫荆9号楼&2010&21紫荆10号楼&2011&21紫荆11号楼"
        	+ "&2012&21紫荆12号楼&2013&21紫荆13号楼"; */
        	
        	
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
