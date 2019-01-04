package DBMS;

import java.sql.*;

public class model_user {
	public Statement stmt;
	public String str_out;
	public String[] array_in;
	
	// the data associated with users
	String uno, user_name, pw, tel, mail, name, number, school, add_str1, add_str2, add_str3;
	int addr1, addr2, addr3, coins, credit;

	public model_user(Statement st, String[] array) { // initialize the variables
		stmt = st;
		array_in = array;
		str_out = "";
		
		uno=""; 
		user_name="";
		pw="";
		tel="";
		mail="";
		name="";
		number="";
		school="";
		add_str1="";
		add_str2="";
		add_str3="";
		addr1=1;
		addr2=1;
		addr3=1;
		coins=100;
		credit=100;
	}

	public String response() {
		
		str_out = "&" + array_in[1]; // 以指令类型开头！

		if (!resolve()) { // 解析字符串
			str_out += "&001"; // command error
			System.out.println("指令错误！");
			return str_out;
		}
		
		if( array_in[1].equals("00") )
			register();
		else if( array_in[1].equals("01") )
			login();
		else if( array_in[1].equals("02") )
			modify_info(); // 修改用户信息
		else if( (array_in[1].equals("03")) 
				|| ( (Integer.valueOf(array_in[1])>=6) && (Integer.valueOf(array_in[1])<=10) ) )
			get_info(); // 获取用户信息
		else if( array_in[1].equals("04") )
			modify_password(); // 修改密码
		else if( array_in[1].equals("05") )
			modify_address(); // 修改地址
		else {
			str_out += "&001";
			System.out.println("指令错误！");
		}
		return str_out;
	}
	
	// 返回false：指令错误！
	public boolean resolve() {
		for(int i=2; i<array_in.length; i++)
		{
			String part1 = array_in[i].substring(0, 2);
			String part2 = array_in[i].substring(2);
			switch(part1)
			{
			case "00":
				user_name = part2;
				break;
			case "01":
				pw = part2; //password
				break;
			case "02":
				tel = part2; //telephone
				break;
			case "03":
				mail = part2;
				break;
			case "04":
				add_str1 = part2;
				break;
			case "05":
				name = part2; //real name
				break;
			case "06":
				uno = part2; //ID number in THU, and the user ID in the app
				break;
			case "07":
				school = part2;
				break;
			case "08":
				add_str2 = part2;
				break;
			case "09":
				add_str3 = part2;
				break;
			default:
				return false;
			}
		}
		return true;
	}

	// 注册
	public void register() {
		String query;
		ResultSet rs;
		int rnum;
		
		try {
			if( !reg_verify() ) {
				str_out += "&004"; // if data not correct
				System.out.println("信息不规范！");
				return;
			}
			
			query = "select name from user where UNO=" + "'"+uno+"'" + " or user_name=" + "'"+user_name+"'";			
			System.out.println(query); // test
			rs = stmt.executeQuery(query);
			if (rs.next()) {
				str_out += "&005";
				System.out.println("您的用户名或学工号已被注册！");
				return;
			}
			
			String update = "insert into user(UNO,user_name,pw,tel,mail,name,school,address1,address2,address3)"
					+ "values(" + "'"+uno+"'" + ",'"+user_name+"'" + ",'"+pw+"'" + ",'"+tel+"'" + ",'"+mail+"'" + ",'"+name+"'" 
					+ ",'"+school+"'" + ",'"+add_str1+"'" + ",'"+add_str2+"'" + ",'"+add_str3+"'" + ")";
			System.out.println(update); // test
			rnum = stmt.executeUpdate(update);
			
			if(rnum == 1) {
				str_out += "&000"; // successfully update
				System.out.println("注册成功！");
			}
			else {
				str_out += "&003"; // fail to update
				System.out.println("注册失败！");
			}
			
		} catch (SQLException e) {			
			str_out += "&003";
			System.out.println("注册失败！");
			e.printStackTrace();
		}
	}

	// 验证注册信息是否规范
	public boolean reg_verify() {
		if( uno.length()==10 && !user_name.isEmpty() && user_name.length()<=20 && pw.length()>=6 && pw.length()<=20 
				&& tel.length()==11 && !mail.isEmpty() && mail.length()<=50
				&& add_str1.length()==4 && (Integer.valueOf(add_str1.substring(0,2)) != 0) && (Integer.valueOf(add_str1.substring(2)) != 0)
				&& (add_str2.isEmpty() || add_str2.length()==4) && (add_str3.isEmpty() || add_str3.length()==4) 
				&& !name.isEmpty() && name.length()<=20 && school.length()<=50 )
			return true;
		else
			return false;
	}
	
	// 登录
	public void login() {
		String query;
		ResultSet rs;
		
		try {
			query = "select uno from user where user_name=" + "'"+user_name+"'" + " and pw=" + "'"+pw+"'";			
			System.out.println(query); // test			
			rs = stmt.executeQuery(query);
			if (rs.next()) {
				// to acquire the user id
				uno = rs.getString("UNO");
				// output string
				str_out += "&000&01" + uno;
				System.out.println("登录成功！");
			} else { // if user_name or password is wrong		
				str_out += "&005";
				System.out.println("用户名或密码错误！");
			}
		} catch (SQLException e) {			
			str_out += "&003";
			System.out.println("登录失败！");
			e.printStackTrace();
		}
	}

	// 修改用户信息
	public void modify_info()
	{
		String query;
		ResultSet rs;
		int rnum;
		
		try {
			if( !mod_info_verify() ) {
				str_out += "&004"; // if data not correct
				System.out.println("信息不规范！");
				return;
			}
			
			// 更新信息
			String update = "update user\nset" + " user_name='"+user_name+"'" + ", tel='"+tel+"'"
					 + ", mail='"+mail+"'" + ", name='"+name+"'" + ", school='"+school+"'" 
					+ "\nwhere UNO=" + "'"+uno+"'";
			System.out.println(update); // test
			rnum = stmt.executeUpdate(update);
			
			if(rnum == 1) {
				str_out += "&000"; // successfully update
				System.out.println("修改用户信息成功！");
			}
			else {
				str_out += "&003"; // fail to update
				System.out.println("修改用户信息失败！");
			}
			
		} catch (SQLException e) {		
			str_out += "&003";
			System.out.println("修改用户信息失败！");
			e.printStackTrace();
		}
	}
	
	// 检查修改用户信息的数据
	public boolean mod_info_verify() {
		if( uno.length()==10 && !user_name.isEmpty() && user_name.length()<=20 
				&& tel.length()==11 && !mail.isEmpty() && mail.length()<=50
				&& !name.isEmpty() && name.length()<=20 && school.length()<=50 )
			return true;
		else
			return false;
	}
	
	// 修改密码
	public void modify_password() {
		String query;
		ResultSet rs;
		int rnum;
		
		try {
			if( !mod_pw_verify() ) {
				str_out += "&004"; // if data not correct
				System.out.println("密码长度必须为8-20位！");
				return;
			}			
			
			String update = "update user\nset" + " pw='"+pw+"'";
			System.out.println(update); // test
			rnum = stmt.executeUpdate(update);
			
			if(rnum == 1) {
				str_out += "&000"; // successfully update
				System.out.println("修改密码成功！");
			}
			else {
				str_out += "&003"; // fail to update
				System.out.println("修改密码失败！");
			}
			
		} catch (SQLException e) {		
			str_out += "&003";
			System.out.println("修改密码失败！");
			e.printStackTrace();
		}
	}
	
	public boolean mod_pw_verify() {
		if( pw.length()>=6 && pw.length()<=20  )
			return true;
		else
			return false;
	}
	
	// 修改地址
	public void modify_address() {
		String query;
		ResultSet rs;
		int rnum;
		
		try {
			if( !mod_addr_verify() ) {
				str_out += "&004"; // if data not correct
				System.out.println("信息不规范！");
				return;
			}
			
			String update = "update user\nset" + " address1='"+add_str1+"'" + ", address2='"+add_str2+"'"
			+ ", address3='"+add_str3+"'" + "\nwhere UNO=" + "'"+uno+"'";
			System.out.println(update); // test
			rnum = stmt.executeUpdate(update);
			
			if(rnum == 1) {
				str_out += "&000"; // successfully update
				System.out.println("修改地址成功！");
			}
			else {
				str_out += "&003"; // fail to update
				System.out.println("修改地址失败！");
			}
			
		} catch (SQLException e) {
			str_out += "&003";
			System.out.println("修改地址失败！");
			e.printStackTrace();
		}
	}
	
	public boolean mod_addr_verify() {
		if( add_str1.length()==4 && (add_str2.isEmpty() || add_str2.length()==4) && (add_str3.isEmpty() || add_str3.length()==4) )
			return true;
		else
			return false;
	}
	
	// 获取所有用户信息
	public void get_info() {
		String query;
		ResultSet rs;
		String str1; 
		
		str1 = ""; // the user_info		
		try {
			// the user_info
			query = "select * from user where UNO=" + "'" + uno + "'";
			System.out.println(query); // test
			rs = stmt.executeQuery(query);
			if (rs.next()) {
				uno = rs.getString("UNO");
				user_name = rs.getString("user_name");
				tel = rs.getString("tel");
				mail = rs.getString("mail");
				name = rs.getString("name");
				school = rs.getString("school");
				coins = Integer.valueOf(rs.getString("coins"));
				credit = Integer.valueOf(rs.getString("credit"));
				add_str1 = rs.getString("address1");
				add_str2 = rs.getString("address2");
				add_str3 = rs.getString("address3");
				// now we don't need to acquire the address_name

				// the first part of str_out
				str1 = "&01" + uno + "&02" + user_name + "&03" + tel 
						+ "&04" + mail + "&05" + add_str1 + "&06" + name + "&08" + school 
						+ "&09" + add_str2 + "&10" + add_str3 + "&11" + coins + "&12" + credit;
			} else {
				str_out += "&005";
				System.out.println("用户不存在！");
				return;
			}
			
			str_out += "&000" + str1;
			System.out.println("获取用户信息成功！");

		} catch (SQLException e) {		
			str_out = "&03&003";
			System.out.println("获取用户信息失败！");
			e.printStackTrace();
		}
	}
	
}