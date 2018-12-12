package DBMS;
import java.net.*;
import java.io.*;
import java.util.*;

public class DBMSserver
{
	 // public static ArrayList<Socket> clientsocks = new ArrayList<Socket>();
	  public static int nClientNum = 0;
	  public static int port_number = 20000; // ¶Ë¿ÚºÅ
	  
	  public static void main(String[] args) throws IOException
	  {
		DBMSserver my = new DBMSserver();
	    ServerSocket servSock = new ServerSocket(port_number);
	    while(true)
	    {
	        Socket clientsock = servSock.accept();
	        //clientsocks.add(clientsock);
	        //nClientNum ++;
	        Mythread thread;
	        thread = new Mythread(clientsock);
	        thread.start();
	    }
	  }
}