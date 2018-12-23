package Client; // you need to change the package name according to your own project
import java.io.*;
import java.net.*;
public class client {
    public static void main(String[] args) throws Exception{
        String strLocal, strSocket; 

        Socket socketClient = new Socket("localhost", 20000);
        BufferedReader brInFromUser = new BufferedReader(new InputStreamReader(System.in)); // 这里若使用UTF-8，遇到中文字符会出错！
        BufferedReader brInFromServer = new BufferedReader(new InputStreamReader(socketClient.getInputStream(),"UTF-8"));
	    BufferedWriter dosOutToServer = new BufferedWriter(new OutputStreamWriter(socketClient.getOutputStream(),"UTF-8"));
	    
	    int ind_client = 0, ind_dbms = 0;
        do
        {       	
        	strLocal = brInFromUser.readLine();
        	// 从全局变量类中调取字符串发给数据库
        	if(ind_client >= Data_test.Data_client.length)
        		break;
        	strLocal = Data_test.Data_client[ind_client];
        	//ind_client ++;
        	
        	System.out.println("Client: " + strLocal);
        	dosOutToServer.write(strLocal + '\n');
        	dosOutToServer.flush();
        	        	
        	/**----我是分割线：客户端编程人员的测试部分主要是以下代码！----
        	 * 
        	 */
        	       	
        	strSocket = brInFromServer.readLine();
        	// 数据库发给客户端信息的测试用例
        	if(ind_dbms >= Data_test.Data_dbms.length)
        		break;
        	strSocket = Data_test.Data_dbms[ind_dbms]; //调整ind_dbms可以选择使用哪一条测试用例
        	ind_dbms ++;
        	System.out.println("Server: " + strSocket);
        	        	
        	// resolve the string
        	Usr user = new Usr();
         	user.Initial(strSocket);
         	if( ! user.GetUsrID().isEmpty() ) {
         		System.out.println("error_type:" + user.GetErrorType() + " " + "UsrID:" + user.GetUsrID() 
         		+ " " + "UsrName:" + user.GetUsrName() + " " + "RealName:" + user.GetRealName() 
         		+ " " + "Address1:" + Data_all.Section[ user.GetAddress1()[0] ] 
         		+ " " +  Data_all.Address[ user.GetAddress1()[0] ][ user.GetAddress1()[1] ] 
         		+ " " + "Address2:" + Data_all.Section[ user.GetAddress2()[0] ]
         	    + " " +  Data_all.Address[ user.GetAddress2()[0] ][ user.GetAddress2()[1] ]
         		+ " " + "Address3:" + Data_all.Section[ user.GetAddress3()[0] ]
         		+ " " +  Data_all.Address[ user.GetAddress3()[0] ][ user.GetAddress3()[1] ]
         		+ " " + "TeleNumber:" + user.GetTeleNumber() + " " + "Email:" + user.GetEmail() + " " + "School:" + user.GetSchool()
         		+ " " + "Coins:" + user.GetCoins() + " " + "Freez_Coins:" + user.GetFreez() + " " + "Credit:" + user.GetCredit() );
         	}
         	
            // resolve the string
        	Task task = new Task();
         	task.Initial(strSocket);
         	System.out.println(task.GetTNO());
         	if( task.GetTNO() != -1 ) {
         		System.out.println("error_type:" + task.GetErrorType() + " " + "TaskID:" + task.GetTNO() 
         				 + " " + "Taskstate:" + task.GetTaskstate() + " " + "Ismodify:" + task.Ismodify()
         				 + " " + "User1:" + task.GetUsr1Name() + " " + "User2:" + task.GetUsr2Name() 
         				 + " " + "User1tele:" + task.GetUsr1Tele() + " " + "User1tele:" + task.GetUsr2Tele()
         				 + " " + "Size:" + task.GetSize() + " " + "Content:" + task.GetContent()
         				 + " " + "Last4Tele:" + task.GetLast4Tele() + " " + "Coins:" + task.GetCoins()
         				 + " " + "Evaluation:" + task.GetEva() 
         				 + " " + "In_Address:" + Data_all.Section[ task.GetInAddress()[0] ]
         				 + " " + Data_all.Address[ task.GetInAddress()[0] ][ task.GetInAddress()[1] ]
         				 + " " + "Out_Address:" + Data_all.Section[ task.GetOutAddress()[0] ]
         				 + " " + Data_all.Address[ task.GetOutAddress()[0] ][ task.GetOutAddress()[1] ]
         				 + " " + "In_Time:" + task.GetInTime()[0] + "月 " + task.GetInTime()[1] + "日 " 
         				 + task.GetInTime()[2] + ":00-" + task.GetInTime()[3] + ":00"
         				 + " " + "Out_Time:" + task.GetOutTime()[0] + "月 " + task.GetOutTime()[1] + "日 " 
        				 + task.GetOutTime()[2] + ":00-" + task.GetOutTime()[3] + ":00" );
         	}
        }
        while(!strSocket.equals("bye"));
        
        socketClient.close();
    } 
}
