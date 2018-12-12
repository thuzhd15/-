package DBMS;
import java.net.*;
import java.io.*;
import java.sql.*;

public class Mythread extends Thread {
	public Socket socket;
	public MysqlJdbc jdbc;
	public Statement stmt;
	
	public String str_in; // the input string
	public String[] array_in; // the input string array
	public String str_out; // the output string

	public Mythread(Socket ss) {
		socket = ss;
		try {
			jdbc = new MysqlJdbc();
		} catch (Exception e) {
			e.printStackTrace();
			str_out += "&002"; // connect error
		}		
	}
	
	// How can I delete a thread?
	public void run() {
		try {
			BufferedReader brInFromClient = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
			BufferedWriter dosOutToClient = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"));
			do {
				str_in = brInFromClient.readLine();
				System.out.println("Client " + socket.getRemoteSocketAddress().toString() + ": " + str_in);
				response();
				System.out.println("Server: " + str_out); // test
				dosOutToClient.write(str_out + '\n');
				dosOutToClient.flush();
			} while (true);			
		} catch (IOException e) {
			System.out.print("net connection error!\n");
			e.printStackTrace();
		} finally { // to close the socket & the connect with the sql database
			try {
				socket.close();
				System.out.println("关闭连接：" + socket.getRemoteSocketAddress().toString());
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				stmt.close();
				System.out.println("关闭sql句柄");
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				jdbc.connect.close();
				System.out.println("关闭sql连接");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public void response() {
		array_in = str_in.split("&");
		
		if(array_in.length < 2) { // if there is no '&' in the input string		
			str_out = "&&001"; // command error
			return;
		}
		else if (array_in[1].length() < 2) { // it the string after the first '&' is shorter than length 2
			str_out = "&&001"; // command error
			return;
		}
		
		str_out = "&" + array_in[1]; // initialize
		
		try {
			stmt = jdbc.connect.createStatement();
			switch (array_in[1].charAt(0)) {
			case '0':
				model_user muser = new model_user(stmt, array_in);
				str_out = muser.response();
				break;
			/*
			 * case '5': model_task mtask = new model_task(); str_out =
			 * mtask.response(array_in); break;
			 */
			default:
				System.out.println("Command error!\n");
				str_out += "&001";
			}
		} catch (SQLException e) {
			e.printStackTrace();
			str_out += "&002"; // connect error
		}
	}
}