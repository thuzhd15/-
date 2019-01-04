package DBMS;

import java.sql.*;
import java.util.*;

public class model_task {
	public Statement stmt;
	public String str_out;
	public String[] array_in;
	ArrayList tno_array = new ArrayList();
	
	int tno, state, iflock, coins, size;
	String user1, user2, in_addr, in_time, out_addr, out_time, tel_last4num, text, comment;
	int [] key = {0,30,0,0}; //查看任务列表的指令
	
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
			System.out.println("指令错误！");
			return str_out;
		}

		if( array_in[1].equals("56") || array_in[1].equals("60")
				|| array_in[1].equals("61") || array_in[1].equals("62") ) {
			task_info(); // 任务详情
		} else if(array_in[1].equals("50")) {
			release(); // 发布任务
		} else if(array_in[1].equals("51")) {
			modify_request(); // 提出任务修改请求（锁定任务）
		} else if(array_in[1].equals("59")) {
			modify_cancel(); // 取消任务修改请求（解锁任务）
		} else if(array_in[1].equals("52")) {
			modify_task(); // 修改任务
		} else if(array_in[1].equals("53")) {
			delete(); // 撤销任务
		} else if( array_in[1].equals("54") || array_in[1].equals("63") || array_in[1].equals("64") ) {
			task_list(); // 查看任务列表
		} else if(array_in[1].equals("55")) {
			accept();// 领取任务
		} else if(array_in[1].equals("57")) {
			finish_apply(); // 乙方申请完成
		} else if(array_in[1].equals("58")) {
			finish_confirm(); // 甲方确认并评价
		} else {
			str_out += "&001";
			System.out.println("指令错误！");
		}
		
		return str_out;
	}
	
	// 若返回false：指令错误！
	public boolean resolve() {
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
				// 查看任务列表的指令
				int key = Integer.parseInt(part1);
				if( (key>=36) && (key<=41) )
					this.key[0] = key; // 用户
				else if( (key>=30) && (key<=32) )
					this.key[1] = key; // 排序
				else if( (key>=34) && (key<=35) ) {
					if( key==34 )
						this.key[2] = key; // 地址搜索
					else if( key==35 )
						this.key[3] = key; // 时间搜索
				} else
					return false;
			}
		}
		return true;
	}

	// 任务详细信息+甲乙方用户名+甲乙方手机号
	public void task_info() {
		String query;
		ResultSet rs;
		String user1_name = "", user2_name = "";
		String user1_tel = "", user2_tel = "";
		
		try {
			// the user_info
			query = "select * from task where TNO=" + tno;
			System.out.println(query); // test
			rs = stmt.executeQuery(query);
			if (rs.next()) {
				tno = rs.getInt("TNO");
				state = rs.getInt("state");
				iflock = rs.getInt("iflock");
				coins = rs.getInt("coins");
				size = rs.getInt("size");
				user1 = rs.getString("user1");
				user2 = rs.getString("user2");
				in_addr = rs.getString("in_addr");
				in_time = rs.getString("in_time");
				out_addr = rs.getString("out_addr");
				out_time = rs.getString("out_time");
				tel_last4num = rs.getString("tel_num");
				text = rs.getString("text");;
				comment = rs.getString("comment");
				
				query = "select * from user where UNO=" + "'"+user1+"'";
				System.out.println(query);			
				rs = stmt.executeQuery(query);
				if (rs.next()) {
					user1_name = rs.getString("user_name");
					user1_tel = rs.getString("tel");
				} else {
					str_out += "&004";
					System.out.println("甲方用户不存在！");
					return;
				}
				
				if ( (user2 != null) && (user2.length()==10) ) {
					query = "select * from user where UNO=" + "'"+user2+"'";
					System.out.println(query); // test
					rs = stmt.executeQuery(query);
					if (rs.next()) {
						user2_name = rs.getString("user_name");
						user2_tel = rs.getString("tel");
					}
				}
				
				// 拼接字符串
				str_out += "&000" + "&01" + tno + "&02" + state + "&03" + iflock 
						+ "&04" + user1_name + "&05" + user2_name + "&17" + user1_tel + "&18" + user2_tel 
						+ "&06" + size + "&07" + text + "&10" + tel_last4num + "&11" + coins
						+ "&12" + comment + "&15" + in_addr + "&16" + in_time
						+ "&08" + out_addr + "&09" + out_time;
				System.out.println("获取任务信息成功！");
			} else {
				str_out += "&004";
				System.out.println("任务不存在！");
				return;
			}

		} catch (SQLException e) {			
			str_out = "&56&003";
			System.out.println("获取任务信息失败！");
			e.printStackTrace();
		}
	}
	
	// 发布任务
	public void release() {
		String query;
		ResultSet rs;
		int rnum;
		
		// 检查信息
		if( !rel_verify() ) {
			str_out += "&004"; // if data not correct
			System.out.println("信息不规范！");
			return;
		}
		
		// 检查金币
		if( !coins_verify(0) ) {
			return;
		}
		
		// 插入数据
		try {
			query = "select max(TNO) from Task";
			System.out.println(query); // test
			rs = stmt.executeQuery(query);
			if (rs.next()) {
				int task_id = rs.getInt("max(TNO)");
				tno = task_id + 1; //任务id分配为当前最大值+1
			} else {
				tno = 0;
			}

			String update = "insert into Task(TNO, state, iflock, coins, user1, user2, "
					+ "size, out_addr, out_time, in_addr, in_time, tel_num, text, comment)"
					+ "values(" + tno + ","+state + ","+iflock + ","+coins + ",'"+user1+"'" + ",'"+user2+"'"
					+ ","+size + ",'"+out_addr+"'" + ",'"+out_time+"'" + ",'"+in_addr+"'"
					+ ",'"+in_time+"'" + ",'"+tel_last4num+"'" + ",'"+text+"'" + ",'"+comment+"'" + ")";
			
			System.out.println(update);
			rnum = stmt.executeUpdate(update);
			
			if(rnum == 1) {
				//任务发布成功，扣除可用金币
				try {
					String update2 = "update user\nset coins = coins - (" + coins + ")\nwhere UNO = '" + user1+"'";
					System.out.println(update2); // test
					int rnum1 = stmt.executeUpdate(update2);

					if (rnum1 == 1) {
						str_out += "&000"; // successfully update
						System.out.println("发布任务成功！");
					} else {				
						str_out += "&003"; // fail to update
						System.out.println("冻结金币失败！");
						return;
					}
				} catch (SQLException e) {
					e.printStackTrace();
					str_out += "&003"; // fail to update
					System.out.println("冻结金币失败！");
					return;
				}				
			}
			else {
				str_out += "&003"; // fail to update
				System.out.println("发布任务失败！");
			}
			
		} catch (SQLException e) {			
			str_out += "&003"; // fail to update
			System.out.println("发布任务失败！");
			e.printStackTrace();
		}		
	}

	 // 检查发布任务的信息
	public boolean rel_verify() {
		if( user1.length()==10 && size>=0 && size<=4 && out_addr.length()==4 
				&& out_time.length()==8 && in_addr.length()==4
				&& in_time.length()==8 && (tel_last4num.isEmpty() || tel_last4num.length()==4) 
				&& text.length()<=200 )
			return true;
		else
			return false;
	}
	
	// 金币值检查，同时适用于发布和修改任务（输入参数为原来的悬赏值，若新发布任务则此值为0）
	public boolean coins_verify(int value) { 
		try {
			String query;
			ResultSet rs;
			int coins_max = 0;
			
			query = "select coins from user where UNO = '" + user1+"'";
			System.out.println(query); // test
			rs = stmt.executeQuery(query);
			if (rs.next()) {
				coins_max = rs.getInt("coins");
			} else {
				str_out += "&004";
				System.out.println("用户不存在！");
				return false;
			}

			if ( coins-value > coins_max ) { // 如果悬赏金币数过多
				str_out += "&005"; // data not correct
				System.out.println("悬赏金币值过多！");
				return false;
			}
			else {
				return true;
			}
		} catch (SQLException e) {			
			str_out += "&003"; // fail to update
			System.out.println("获取金币信息失败！");
			e.printStackTrace();
			return false;
		}
	}

	public void modify_request() {
		String query;
		ResultSet rs;
		int rnum;

		try {
			query = "select state from Task where TNO=" + tno;
			System.out.println(query); // test
			rs = stmt.executeQuery(query);
			if( !rs.next()) {
				str_out += "&004"; // if data is not correct
				System.out.println("任务不存在！");
				return;
			}
			else if( rs.getInt("state") == 1 ) {
				str_out += "&004"; // this task's state is not 0
				System.out.println("任务已领取！");
				return;
			}
			else if( rs.getInt("state") == 2 ) {
				str_out += "&004"; // this task's state is not 0
				System.out.println("对方已发起请求，是否确认完成任务？");
				return;
			}
			else if( rs.getInt("state") == 3 ) {
				str_out += "&004"; // this task's state is not 0
				System.out.println("任务已完成！");
				return;
			}
			else if( rs.getInt("state") == 4 ) {
				str_out += "&004"; // this task's state is not 0
				System.out.println("任务已撤销！");
				return;
			}
			
			String update = "update Task\nset iflock=1\nwhere TNO=" + tno;
			System.out.println(update); // test
			rnum = stmt.executeUpdate(update);
			
			if(rnum == 1) {
				str_out += "&000"; // successfully update
				System.out.println("锁定任务成功！");
				tno_array.add(tno); //把被标记锁定的任务放进队列
			}
			else {
				str_out += "&003"; // fail to update
				System.out.println("锁定任务失败！");
			}
			
		} catch (SQLException e) {
			str_out += "&003";
			System.out.println("锁定任务失败！");
			e.printStackTrace();
		}
	}
	
	public void modify_cancel() {
		String query;
		ResultSet rs;
		int rnum;
		
		try {
			query = "select iflock from Task where TNO=" + tno;
			System.out.println(query); // test
			rs = stmt.executeQuery(query);
			if( !rs.next()) {
				str_out += "&004"; // if data not correct
				System.out.println("任务不存在！");
				return;
			}
			
			String update = "update Task\nset iflock = 0\nwhere TNO=" + tno;
			System.out.println(update); // test
			rnum = stmt.executeUpdate(update);
			
			if(rnum == 1) {
				str_out += "&000"; // successfully update
				System.out.println("解锁任务成功！");
			}
			else {
				str_out += "&003"; // fail to update
				System.out.println("解锁任务失败！");
			}
			
		} catch (SQLException e) {		
			str_out += "&003";
			System.out.println("解锁任务失败！");
			e.printStackTrace();
		}
	}
	
	// 修改任务信息
	public void modify_task() {
		String query;
		ResultSet rs;
		int rnum;
		
		try {
			query = "select * from Task where TNO=" + tno;
			System.out.println(query); // test
			rs = stmt.executeQuery(query);
			if( !rs.next()) {
				str_out += "&004"; // if data not correct
				System.out.println("任务不存在！");
				return;
			}
			else if( rs.getInt("iflock") == 0 ) {
				str_out += "&004";
				System.out.println("任务未锁定！");
				return;
			}
			int coins1 = rs.getInt("coins");		
			if( coins != coins1 ) //如果悬赏金币值发生了变化
			{
				// 检查甲方可用金币
				if( !coins_verify(coins1) ) {
					return;
				}
				// 检查任务信息
				if( !modify_task_verify() ) {
					str_out += "&004"; // if data not correct
					System.out.println("信息不规范！");
					return;
				}
				
				// 修改甲方可用金币
				int delta_coins = coins-coins1; //悬赏金币值的增量（相应为甲方可用金币值的减量）
				try {
					String update1 = "update user\nset coins = coins-(" + delta_coins + ")\nwhere UNO = '" + user1+"'";
					System.out.println(update1); // test
					int rnum1 = stmt.executeUpdate(update1);

					if (rnum1 == 1) {
						System.out.println("修改悬赏金币成功！");
					} else {
						System.out.println("修改悬赏金币失败！");
						str_out += "&003"; // fail to update
						return;
					}
				} catch (SQLException e) {
					e.printStackTrace();
					str_out += "&003"; // fail to update
					System.out.println("修改悬赏金币失败！");
					return;
				}
			}
			// 修改任务信息
			String update = "update Task\nset coins="+coins + ", size="+size
			        + ", out_addr='"+out_addr+"'" + ", out_time='"+out_time+"'" + ", in_addr='"+in_addr+"'"
					+ ", in_time='"+in_time+"'" + ",  tel_num='"+tel_last4num+"'" + ", text='"+text+"'"
					+ "\nwhere TNO=" + tno;
			System.out.println(update); // test
			rnum = stmt.executeUpdate(update);
			
			if(rnum == 1) {
				str_out += "&000"; // successfully update
				System.out.println("修改任务信息成功！");
			}
			else {
				str_out += "&003"; // fail to update
				System.out.println("修改任务信息失败！");
				return;
			}
			
		} catch (SQLException e) {
			str_out += "&003";
			System.out.println("修改任务信息失败！");
			e.printStackTrace();
		}
	}

	// 检验任务信息是否规范
	public boolean modify_task_verify() {
		if( user1.length()==10 && size>=0 && size<=4 && out_addr.length()==4
				&& out_time.length()==8 && in_addr.length()==4
				&& in_time.length()==8 && (tel_last4num.isEmpty() || tel_last4num.length()==4)
				&& text.length()<=200 )
			return true;
		else
			return false;
	}
	
	// 甲方撤销未被领取的任务
	public void delete() {
		String query;
		ResultSet rs;
		int rnum;
		try {
			query = "select * from Task where TNO=" + tno;
			System.out.println(query); // test
			rs = stmt.executeQuery(query);
			if( !rs.next()) {
				str_out += "&004";
				System.out.println("任务不存在！");
				return;
			}
			else if( rs.getInt("state") == 1 ) {
				str_out += "&004";
				System.out.println("任务已被领取！");
				return;
			}
			else if( rs.getInt("state") == 2 ) {
				str_out += "&004";
				System.out.println("对方已发起请求，是否确认完成任务？");
				return;
			}
			else if( rs.getInt("state") == 3 ) {
				str_out += "&004";
				System.out.println("任务已完成！");
				return;
			}
			else if( rs.getInt("state") == 4 ) {
				str_out += "&004";
				System.out.println("任务早就被撤销了！");
				return;
			}
			int coins1 = rs.getInt("coins");
			String user11 = rs.getString("user1");
			
			String update ="update Task\nset state=4\nwhere TNO=" + tno;
			System.out.println(update);
			rnum = stmt.executeUpdate(update);
			
			if(rnum == 1) {
				try {//解冻金币
					String update1 = "update user\nset coins = coins+(" + coins1 + ")\nwhere UNO = '" + user11+"'";
					System.out.println(update1); // test
					int rnum1 = stmt.executeUpdate(update1);

					if (rnum1 == 1) {
						str_out += "&000"; // successfully delete
						System.out.println("撤销任务成功！");
					} else {
						System.out.println("解冻金币失败！");
						str_out += "&003"; // fail to update
						return;
					}

				} catch (SQLException e) {				
					str_out += "&003"; // fail to update
					System.out.println("解冻金币失败！");
					e.printStackTrace();
					return;
				}
			}
			else {
				str_out += "&003"; // fail to delete
				System.out.println("撤销任务失败！");
				return;
			}
			
		} catch (SQLException e) {			
			str_out += "&003";
			System.out.println("撤销任务失败！");
			e.printStackTrace();
		}
	}
	
	 // 查看任务列表
	public void task_list() {
		String query="";
		ResultSet rs;
			
		if( key[0] != 0 ) { // 用户关联，不用搜索或排序
			query = "select * from task\nwhere";
			query = task_list_user(query);
		}
		else {
			query = "select * from task a, (select UNO,credit from user) b"
					+ "\nwhere a.user1=b.UNO and a.state=0 and a.iflock=0";
			if( key[2] != 0 || key[3] != 0 ) // 搜索
				query = task_list_search(query);
			if( key[1] != 0 ) // 排序
				query = task_list_sort(query);		
		}
		
		try {
			System.out.println(query);
			rs = stmt.executeQuery(query);
			
			// 获取结果
			int rnum = 0; //结果的数目
			String str_tmp = ""; //暂存结果
			if (rs.next()) {
				tno = rs.getInt("TNO");
				size = rs.getInt("size");
				in_time = rs.getString("in_time");
				out_addr = rs.getString("out_addr");				
				// 拼接字符串
				str_tmp += "&01" + tno + "&06" + size + "&16" + in_time + "&08" + out_addr;
				rnum += 1;
			} else { // 若一个结果都没有
				str_out += "&000&200";
				System.out.println("没有满足条件的任务！");
				return;
			}
			
			while (rs.next()) {
				tno = rs.getInt("TNO");
				size = rs.getInt("size");
				in_time = rs.getString("in_time");
				out_addr = rs.getString("out_addr");				
				// 拼接字符串
				str_tmp += "&01" + tno + "&06" + size + "&16" + in_time + "&08" + out_addr;
				rnum += 1;				
			}
			
			str_out += "&000&20" + rnum + str_tmp;
			System.out.println("获取任务列表成功！");
			
		} catch (SQLException e) {			
			str_out = "&54&003";
			System.out.println("获取任务列表失败！");
			e.printStackTrace();
		}		
	}

	// 用户关联任务列表
	public String task_list_user(String query) {
		
		switch(key[0]) {
		case 36:
			query += " user1=" + "'"+user1+"'" + " and state=3";
			break;
		case 37:
			query += " user1=" + "'"+user1+"'" + " and state=4";
			break;
		case 38:
			query += " user2=" + "'"+user2+"'" + " and state=3";
			break;
		case 39:
			query += " user1=" + "'"+user1+"'" + " and (state=0 or state=1 or state=2)";
			break;
		case 40:
			query += " user1=" + "'"+user1+"'" + " and (state=1 or state=2)";
			break;
		case 41:
			query += " user2=" + "'"+user2+"'" + " and (state=1 or state=2)";
			break;
		}
		
		return query;
	}
	
	// 任务列表搜索
	public String task_list_search(String query) {
		if(key[2]!=0) {
			if( ! in_addr.isEmpty() ) {
				for(int i=0;i<2;i++) {
					int add_id = Integer.parseInt( in_addr.substring(i*2, i*2+2) );
					if(add_id==0) {
						StringBuilder strBuilder = new StringBuilder(in_addr);
						strBuilder.setCharAt(2*i, '_');
						strBuilder.setCharAt(2*i+1, '_');
						in_addr=strBuilder.toString();
					}
				}
				query += " and a.in_addr like" + "'"+in_addr+"'";
			}
			if( ! out_addr.isEmpty() ) {
				for(int i=0;i<2;i++) {
					int add_id = Integer.parseInt( out_addr.substring(i*2, i*2+2) );
					if(add_id==0) {
						StringBuilder strBuilder = new StringBuilder(out_addr);
						strBuilder.setCharAt(2*i, '_');
						strBuilder.setCharAt(2*i+1, '_');
						out_addr=strBuilder.toString();
					}
				}
				query += " and a.out_addr like" + "'"+out_addr+"'";
			}
		}
		
		if(key[3]!=0) {
			if( ! in_time.isEmpty() ) {
				for(int i=0;i<4;i++) {
					int t = Integer.parseInt( in_time.substring(i*2, i*2+2) );
					if(t==0) {
						StringBuilder strBuilder = new StringBuilder(in_time);
						strBuilder.setCharAt(2*i, '_');
						strBuilder.setCharAt(2*i+1, '_');
						in_time=strBuilder.toString();
					}
				}
				query += " and a.in_time like" + "'"+in_time+"'";
			}
			if( ! out_time.isEmpty() ) {
				for(int i=0;i<4;i++) {
					int t = Integer.parseInt( out_time.substring(i*2, i*2+2) );
					if(t==0) {
						StringBuilder strBuilder = new StringBuilder(out_time);
						strBuilder.setCharAt(2*i, '_');
						strBuilder.setCharAt(2*i+1, '_');
						out_time=strBuilder.toString();
					}
				}
				query += " and a.out_time like" + "'"+out_time+"'";
			}
		}
		return query;
	}

	// 任务列表排序
	public String task_list_sort(String query) {
		switch(key[1]) {
		case 31:
			query += "\norder by a.coins DESC";
			break;
		case 32:
			query += "\norder by b.credit DESC";
			break;
		default:
			query += "\norder by a.TNO ASC";
		}

		return query;
	}

	// 领取任务
	public void accept() {
		String query;
		ResultSet rs;
		int rnum;
		try {
			query = "select * from Task where TNO=" + tno;
			System.out.println(query); // test
			rs = stmt.executeQuery(query);
			if( !rs.next()) {
				str_out += "&004"; // if data not correct
				System.out.println("任务不存在！");
				return;
			}
			else if( rs.getInt("iflock") != 0 ) {
				str_out += "&004"; // this task'state is not 0
				System.out.println("任务正在修改中！");
				return;
			}
			else if( rs.getInt("state") == 4 ) {
				str_out += "&004";
				System.out.println("任务已被撤销！");
				return;
			}
			else if( rs.getInt("state") != 0 ) {
				str_out += "&004";
				System.out.println("任务已被领取！");
				return;
			}
			String update = "update Task\nset user2='" +user2+"'" + ", state=1\nwhere TNO=" + tno;
			System.out.println(update); // test
			rnum = stmt.executeUpdate(update);
			
			if(rnum == 1) {
				str_out += "&000"; // successfully update
				System.out.println("领取任务成功！");
			}
			else {
				str_out += "&003"; // fail to update
				System.out.println("领取任务失败！");
				return;
			}
			
		} catch (SQLException e) {			
			str_out += "&003"; // fail to update
			System.out.println("领取任务失败！");
			e.printStackTrace();
		}
	}

	// 乙方申请已完成
	public void finish_apply()	{
		String query;
		ResultSet rs;
		int rnum;
		try {
			query = "select * from Task where TNO=" + tno;
			System.out.println(query); // test
			rs = stmt.executeQuery(query);
			if( !rs.next()) {
				str_out += "&004"; // if data not correct
				System.out.println("任务不存在！");
				return;
			}
			else if( rs.getInt("state") == 0) {
				str_out += "&004";
				System.out.println("任务未领取！");
				return;
			}
			else if( rs.getInt("state") == 2) {
				str_out += "&004";
				System.out.println("申请已发过，请您耐心等候！");
				return;
			}
			else if( rs.getInt("state") == 3) {
				str_out += "&004";
				System.out.println("任务已确认完成！");
				return;
			}
			else if( rs.getInt("state") == 4) {
				str_out += "&004";
				System.out.println("任务已撤销！");
				return;
			}
			String update = "update Task\nset state=2\nwhere TNO=" + tno;
			System.out.println(update); // test
			rnum = stmt.executeUpdate(update);
			
			if(rnum == 1) {
				str_out += "&000"; // successfully update
				System.out.println("发送申请成功！");
			}
			else {
				str_out += "&003"; // fail to update
				System.out.println("发送申请失败！");
				return;
			}
			
		} catch (SQLException e) {			
			str_out += "&003"; // fail to update
			System.out.println("任务撤销失败！");
			e.printStackTrace();
		}
	}
	
	// 甲方确认已完成
	public void finish_confirm() {
		String query;
		ResultSet rs;
		int rnum;
		try {
			query = "select * from Task where TNO=" + tno;
			System.out.println(query); // test
			rs = stmt.executeQuery(query);
			if( !rs.next()) {
				str_out += "&004"; // if data not correct
				System.out.println("任务不存在！");
				return;
			}
			else if( rs.getInt("state") == 0 ) {
				str_out += "&004";
				System.out.println("任务未领取！");
				return;
			}
			else if( rs.getInt("state") == 4 ) {
				str_out += "&004";
				System.out.println("任务已撤销！");
				return;
			}
			else if( rs.getInt("state") == 3 ) {
				str_out += "&004";
				System.out.println("任务早就完成了！");
				return;
			}
			
			// 乙方金币、信誉值增加
			int coins1 = rs.getInt("coins");
			String user22=rs.getString("user2");
			try {
				String update2 = "update user\nset coins=coins+" + coins1 + ", credit=credit+" + coins1 + "\nwhere UNO = '" + user22+"'";
				System.out.println(update2); // test
				int rnum1 = stmt.executeUpdate(update2);

				if (rnum1 == 1) {
					System.out.println("金币和信誉值修改成功！");
				} else {
					str_out += "&003"; // fail to update
					System.out.println("金币和信誉值修改失败!");
					return;
				}

			} catch (SQLException e) {
				e.printStackTrace();
				System.out.println("金币和信誉值修改失败!");
				str_out += "&003"; // fail to update
				return;
			}

			String update = "update Task\nset comment='" +comment+"'" + ", state=3\nwhere TNO=" + tno;
			System.out.println(update);
			rnum = stmt.executeUpdate(update);
			
			if(rnum == 1) {
				str_out += "&000"; // successfully update
				System.out.println("任务确认完成！");
			}
			else {
				str_out += "&003"; // fail to update
				System.out.println("任务未确认完成！");
				return;
			}
			
		} catch (SQLException e) {			
			str_out += "&003"; // fail to update
			System.out.println("任务未确认完成！");
			e.printStackTrace();
		}
	}
}