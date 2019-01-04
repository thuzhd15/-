package DBMS;
import java.net.*;
import java.io.*;
import java.sql.*;
import java.util.*;

public class Mythread extends Thread {
	public Socket socket;
	public MysqlJdbc jdbc;
	public Statement stmt;
	
	public String str_in; // the input string
	public String[] array_in; // the input string array
	public String str_out; // the output string
	
	model_user muser; //用户解析对象
	model_task mtask; //任务解析对象
	
	Timer timer; //设置一个定时器
	TimerTask timertask; //定时任务
	
	public Mythread(Socket ss) {
		socket = ss;
		muser = null; //用户解析对象
		mtask = null; //任务解析对象
		
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
			timer_reset();
			
			do {
				str_in = brInFromClient.readLine();
				// 收到一条消息则立刻把定时器重置！
				timer.cancel();
				timer_reset();
				
				// 通常客户端异常关闭的时候会发来null信息（但断线时不会发来任何消息！）
				if(str_in==null)
					break; // 此时把服务关闭
				
				System.out.println("\nClient " + socket.getRemoteSocketAddress().toString() + ": " + str_in);
				response();
				System.out.println("Server: " + str_out); // test
				dosOutToClient.write(str_out + '\n');
				dosOutToClient.flush();
			} while (true);	
			
		} catch (IOException e) {
			System.out.print("\nnet connection error!\n");
			e.printStackTrace();
		} finally {
			shutdown(); //最后断开所有连接
		}
	}

	public void response() {
		if( str_in!=null && !str_in.isEmpty() )
			array_in = str_in.split("&");
		else {
			str_out = "&&001";
			return;
		}
		
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
				muser = new model_user(stmt, array_in);
				str_out = muser.response();
				break;
			case '1':
				muser = new model_user(stmt, array_in);
				str_out = muser.response();
				break;
			case '5':
				mtask = new model_task(stmt, array_in);
				str_out = mtask.response();
				break;
			case '6':
				mtask = new model_task(stmt, array_in);
				str_out = mtask.response();
				break;
			 
			default:
				System.out.println("开头指令出错！\n");
				str_out += "&001";
			}
		} catch (SQLException e) {
			e.printStackTrace();
			str_out += "&002"; // connect error
		}
	}
	
	// 设定或重置定时器（若30分钟没收到消息，则断开连接）
	public void timer_reset() {
		timer = new Timer(true);
		timertask = new TimerTask() { // 定时任务
			public void run() {
				shutdown(); // 断开连接
			}
		};
		timer.schedule(timertask, 1800*1000, 1800*1000); // 开启定时器
	}
	
	// 断开连接（同时注意把被锁定的任务解锁）
	public void shutdown() {
		
		timer.cancel(); // 关闭定时器
		
		// 把被标记处于修改状态的任务解锁
		if( (stmt != null) && (mtask != null) ) {
			int i;
			for(i=0; i < mtask.tno_array.size(); i++) {
				String update = "update Task\nset iflock = 0\nwhere TNO=" + mtask.tno_array.get(i);
				System.out.println(update);
				try {
					stmt.executeUpdate(update);
					System.out.println("解锁任务成功：" + mtask.tno_array.get(i));
				} catch (SQLException e) {
					System.out.println("解锁任务失败：" + mtask.tno_array.get(i));
					e.printStackTrace();
				}
			}
		}
		
		try {
			if (socket != null) {
				socket.close();
				System.out.println("\n关闭连接："
						+ socket.getRemoteSocketAddress().toString());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			if (stmt != null) {
				stmt.close();
				System.out.println("关闭sql句柄");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			if ((jdbc != null) && (jdbc.connect != null)) {
				jdbc.connect.close();
				System.out.println("关闭sql连接");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}