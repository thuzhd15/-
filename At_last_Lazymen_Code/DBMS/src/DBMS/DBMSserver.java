package DBMS;
import java.net.*;
import java.io.*;
import java.util.*;

public class DBMSserver
{
	 // public static ArrayList<Socket> clientsocks = new ArrayList<Socket>();
	  public static int nClientNum = 0;
	  public static int port_number = 20000; // 端口号
	  
	  public static void main(String[] args) throws IOException
	  {
		DBMSserver my = new DBMSserver();
		ServerSocket servSock = null;
		try {
			servSock = new ServerSocket(port_number);
			while (true) {
				Socket clientsock = servSock.accept();
				Mythread thread = new Mythread(clientsock);
				thread.start();
			}
		} finally { // 最后关闭服务器！
			try {
				if (servSock != null) {
					servSock.close();
					System.out.println("服务器关闭成功！");
				}
			} catch (IOException e) {
				System.out.println("服务器关闭失败！");
				e.printStackTrace();
			}
		}
	}
}