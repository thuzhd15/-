package DBMS;

import java.sql.*;

public class model_task {
	public Statement stmt;
	public String str_out;
	public String[] array_in;
	
	int tno, state, iflock, coins, size;
	String user1, user2, in_addr, in_time, out_addr, out_time, tel_last4num, text, comment;

	public model_task(Statement st, String[] array) {
		stmt = st;
		array_in = array;
		str_out = "";
		
		tno = 0;
		state = 0;
		iflock = 0;
		coins = 0;
		size = 2;
		user1 = "";
		user2 = "";		
		in_addr = "";
		in_time = "";
		out_addr = "";
		out_time = "";
		tel_last4num = "";
		text = "";
		comment = "";
	}

	public String response() {
		
		str_out = "&" + array_in[1]; // 以指令类型开头！

		if (!resolve()) { // 解析字符串
			str_out += "&001"; // command error
			return str_out;
		}

		switch (array_in[1]) {
		case "56":
			task_info();
		case "50":
			release();
			break;
		case "51":
			modify_request();
			break;
		case "59":
			modify_cancel();
			break;
		case "52":
			modify_task();
			break;
		case "53":
			delete();
			break;
		case "54":
			//查看任务列表
			task_list();
			break;
		case "55":
			accept();
			break;
		case "57":
			// 乙方申请已完成
			finish_apply();
		case "58":
			// 甲方确认并评价
			finish_confirm();
		default:
			str_out += "&001"; // command error
		}
		return str_out;
	}
	
	public boolean resolve() { // false means command error
		for(int i=2; i<array_in.length; i++)
		{
			String part1 = array_in[i].substring(0, 2);
			String part2 = array_in[i].substring(2);
			switch(part1)
			{
			case "00":				
				if( !part2.isEmpty() )
					tno = Integer.parseInt(part2); // task_id
				break;
			case "01":
				if( !part2.isEmpty() )
					coins = Integer.parseInt(part2);
				break;			
			case "04":
				text = part2;
				break;
			case "07":
				user1 = part2;
				break;
			case "08":
				user2 = part2;
				break;
			case "09":
				if( !part2.isEmpty() )
					size = Integer.parseInt(part2);
				break;
			case "02":
				in_addr = part2;
				break;
			case "03":
				in_time = part2;
				break;			
			case "10":
				out_addr = part2;
				break;
			case "11":
				out_time = part2;
				break;
			case "12":
				tel_last4num = part2;
				break;
			case "13":
				comment = part2;
				break;
			default:
				return false;
			}
		}
		return true;
	}

	public void task_info() { // get task information
		
	}
	
	public void release() { // 发布任务
		String query;
		ResultSet rs;
		int rnum;
		
		// 检查信息
		if( !rel_verify() ) {
			str_out += "&004"; // if data not correct
			System.out.println("信息不合规范！");
			return;
		}
		
		// 检查金币
		if( !rel_coins_verify() ) {
			return;
		}
		
		// 冻结金币
		if( !freez_c_modify(coins) )
		{
			str_out += "&003"; // fail to update
			System.out.println("无法冻结金币！");
			return;
		}
		
		// 插入数据
		try {			
			query = "select max(TNO) from Task";
			System.out.println(query); // test
			rs = stmt.executeQuery(query);
			if (rs.next()) {
				String task_id = rs.getString("max(TNO)");
				tno = Integer.parseInt(task_id) + 1; //任务id分配为当前最大值+1
			} else {
				tno = 0;
			}

			String update = "insert into Task(TNO, state, iflock, coins, user1, "
					+ "size, out_addr, out_time, in_addr, in_time, tel_num, text)"
					+ "values(" + tno + ","+state + ","+iflock + ","+coins + ",'"+user1+"'" + ","+size
					+ ",'"+out_addr+"'" + ",'"+out_time+"'" + ",'"+in_addr+"'"
					+ ",'"+in_time+"'" + ",'"+tel_last4num+"'" + ",'"+text+"'" + ")";
			
			System.out.println(update); // test
			rnum = stmt.executeUpdate(update);
			
			if(rnum == 1) {
				str_out += "&000"; // successfully update
				System.out.println("Insert a row of data!");
			}
			else {
				str_out += "&003"; // fail to update
				System.out.println("Data not updated!");
				
				if( !freez_c_modify(-coins) ) // 恢复被冻结的金币值
					System.out.println("数据出现异常！");
				
				return;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			str_out += "&003"; // fail to update
			
			if( !freez_c_modify(-coins) ) // 恢复被冻结的金币值
				System.out.println("数据出现异常！");
			
			return;
		}		
		
	}

	public boolean rel_verify() { // verify if the data is right for register
		if( user1.length()==10 && size>=0 && size<=4 && out_addr.length()==4 
				&& out_time.length()==8 && in_addr.length()==4
				&& in_time.length()==8 && tel_last4num.length()==4 && text.length()<=200 )
			return true;
		else
			return false;
	}
	
	public boolean rel_coins_verify() { // 金币值检查
		try {
			String query;
			ResultSet rs;
			int coins_max = 0;
			
			query = "select coins from user where UNO = " + user1;
			System.out.println(query); // test
			rs = stmt.executeQuery(query);
			if (rs.next()) {
				coins_max = rs.getInt("coins");
			} else {
				str_out += "&004"; // data not correct
				System.out.println("用户不存在！");
				return false;
			}

			if (coins > coins_max) { // 如果悬赏金币数过多
				str_out += "&004"; // data not correct
				System.out.println("悬赏金币值过多！");
				return false;
			}
			else {
				return true;
			}

		} catch (SQLException e) {
			e.printStackTrace();
			str_out += "&003"; // fail to update
			System.out.println("发布任务失败！");
			return false;
		}
	}
	
	public boolean freez_c_modify( int value ) // 冻结金币值的修改
	{
		// 冻结金币
		try {
			String update = "update user\nset" + " freez_c = freez_c+(" + value + ")\nwhere UNO = " + user1;
			System.out.println(update); // test
			int rnum = stmt.executeUpdate(update);

			if (rnum == 1) {
				System.out.println("成功冻结金币！");
				return true;
			} else {
				return false;
			}

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public void modify_request()
	{
		String query;
		ResultSet rs;
		int rnum;
		
		try {
			query = "select state from Task where TNO=" + "'"+tno+"'";
			System.out.println(query); // test
			rs = stmt.executeQuery(query);
			if( !rs.next()) {
				str_out += "&004"; // if data not correct
				System.out.println("Data not correct!");
				return;
			}
			else if( !rs.getString("state").equals('0') ) {
				str_out += "&001"; // this task'state is not 0
				System.out.println("This task can't be modified!");
				return;
			}
			
			String update = "update Task\nset" + " iflock = '1'";
			System.out.println(update); // test
			rnum = stmt.executeUpdate(update);
			
			if(rnum == 1) {
				str_out += "&000"; // successfully update
				System.out.println("update a row of data!");
			}
			else {
				str_out += "&003"; // fail to update
				System.out.println("Data not updated!");
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			str_out += "&003";
		}
	}
	
	public void modify_cancel()
	{
		String query;
		ResultSet rs;
		int rnum;
		
		try {
			query = "select iflock from Task where TNO=" + "'"+tno+"'";
			System.out.println(query); // test
			rs = stmt.executeQuery(query);
			if( !rs.next()) {
				str_out += "&004"; // if data not correct
				System.out.println("User_Info data not correct!");
				return;
			}
			else if( !rs.getString("iflock").equals('1') ) {
				str_out += "&001"; // this task'state is not 0
				System.out.println("This change can't be cancelled again!");
				return;
			}
			
			String update = "update Task\nset" + " iflock = '0'";
			System.out.println(update); // test
			rnum = stmt.executeUpdate(update);
			
			if(rnum == 1) {
				str_out += "&000"; // successfully update
				System.out.println("update a row of data!");
			}
			else {
				str_out += "&003"; // fail to update
				System.out.println("Data not updated!");
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			str_out += "&003";
		}
	}
	
	public void modify_task() // change Task's information
	{
		ResultSet rs;
		int rnum;
		
		try {
			if( !modify_task_verify() ) {
				str_out += "&004"; // if data not correct
				System.out.println("User_Info data not correct!");
				return;
			}					
			String update = "update Task\nset" + " iflock='0'" + ", coins='"+coins+"'" + ", size='"+size+"'"
			        + ", out_addr='"+out_addr+"'" + ", out_time='"+out_time+"'" + ", in_addr='"+in_addr+"'"
					+ ", in_time='"+in_time+"'" + ",  tel_num='"+tel_last4num+"'" + ", text="+text+"'";
			System.out.println(update); // test
			rnum = stmt.executeUpdate(update);
			
			if(rnum == 1) {
				str_out += "&000"; // successfully update
				System.out.println("update a row of data!");
			}
			else {
				str_out += "&003"; // fail to update
				System.out.println("Data not updated!");
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			str_out += "&003";
		}
	}

	public boolean modify_task_verify() { // verify if the data is right for modifying user_info
		if( user1.length()==10 && size>=0 && size<=4 && out_addr.length()==4
				&& out_time.length()==8 && in_addr.length()==4
				&& in_time.length()==8 && tel_last4num.length()==4 && text.length()<=200 )
			return true;
		else
			return false;
	}
	
	public void delete() // cancel task released
	{
		String query;
		ResultSet rs;
		int rnum;
		
		try {
			query = "select state from Task where TNO=" + "'"+tno+"'";
			System.out.println(query); // test
			rs = stmt.executeQuery(query);
			if( !rs.next()) {
				str_out += "&004"; // if data not correct
				System.out.println("Data not correct!");
				return;
			}
			else if( !rs.getString("state").equals('0') ) {
				str_out += "&001"; // this task'state is not 0
				System.out.println("This task can't be modified!");
				return;
			}
			String delete = "delete from Task where TNO=" + "'"+tno+"'";
			System.out.println(delete); // test
			rnum = stmt.executeUpdate(delete);
			
			if(rnum == 1) {
				str_out += "&000"; // successfully delete
				System.out.println("delete a row of data!");
			}
			else {
				str_out += "&003"; // fail to delete
				System.out.println("Data is not deleted!");
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			str_out += "&003";
		}
	}
	
	public void task_list() // 查看任务列表
	{
		
	}
	
	public void accept() // accept a task
	{
		
	}
	
	public void finish_apply() // 乙方申请已完成
	{
		
	}
	
	public void finish_confirm() // 甲方确认已完成
	{
		
	}
}