package DBMS;

import java.io.*;
import java.sql.*;
import java.util.*;

public class model_task {
	public Statement stmt;
	public String str_out;
	public String[] array_in;
	
	String uno, user_name, pw, tel, mail, name, number, school, add_str1, add_str2, add_str3;
	int addr1, addr2, addr3, coins, credit;

	public model_task(Statement st, String[] array) {
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
			return str_out;
		}

		switch (array_in[1]) {
		case "00":
			register();
			break;
		case "01":
			login();
			break;
		case "02":
			modify_info(); // modify the user information
			break;
		case "03":
			addr_list();
			break;
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

	public void register() {
		String query;
		ResultSet rs;
		int rnum;
		
		try {
			if( !reg_verify() ) {
				str_out += "&004"; // if data not correct
				System.out.println("Register data not correct!");
				return;
			}
			
			query = "select name from user where UNO=" + "'"+uno+"'" + " or user_name=" + "'"+user_name+"'";			
			System.out.println(query); // test
			rs = stmt.executeQuery(query);
			if (rs.next()) {
				str_out += "&005"; // if be the same as some of the existed users
				return;
			}
			
			// addr1 = Integer.valueOf(add_str1);
			query = "select add_name from address where add_id=" + add_str1;
			System.out.println(query); // test
			rs = stmt.executeQuery(query);
			if (!rs.next()) {
				System.out.println("address1 is not existed!");
				str_out += "&004"; // the address data wrong
			}
			if (!add_str2.isEmpty()) {
				//addr2 = Integer.valueOf(add_str2);
				query = "select add_name from address where add_id=" + add_str2;
				System.out.println(query); // test
				rs = stmt.executeQuery(query);
				if (!rs.next()) {
					System.out.println("address2 is not existed!");
					str_out += "&004"; // the address data wrong
				}
			}
			if (!add_str3.isEmpty()) {
				//addr3 = Integer.valueOf(add_str3);
				query = "select add_name from address where add_id=" + add_str3;
				System.out.println(query); // test
				rs = stmt.executeQuery(query);
				if (!rs.next()) {
					System.out.println("address3 is not existed!");
					str_out += "&004"; // the address data wrong
				}
			}
			
			String update = "insert into user(UNO,user_name,pw,tel,mail,name,school,address1,address2,address3)"
					+ "values(" + "'"+uno+"'" + ",'"+user_name+"'" + ",'"+pw+"'" + ",'"+tel+"'" + ",'"+mail+"'" + ",'"+name+"'" 
					+ ",'"+school+"'" + ",'"+add_str1+"'" + ",'"+add_str2+"'" + ",'"+add_str3+"'" + ")";
			System.out.println(update); // test
			rnum = stmt.executeUpdate(update);
			
			if(rnum == 1) {
				str_out += "&000"; // successfully update
				System.out.println("Insert a row of data!");
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

	public boolean reg_verify() { // verify if the data is right for register
		if( uno.length()==10 && !user_name.isEmpty() && user_name.length()<=20 && !pw.isEmpty() && pw.length()<=20 
				&& tel.length()==11 && !mail.isEmpty() && mail.length()<=50 && !add_str1.isEmpty() && add_str1.length()<=5 
				&& !name.isEmpty() && name.length()<=20 && add_str2.length()<=5 && add_str3.length()<=5 && school.length()<=50 )
			return true;
		else
			return false;
	}
	
	public void login() {
		String query;
		ResultSet rs;
		
		try {
			query = "select * from user where user_name=" + "'"+user_name+"'" + " and pw=" + "'"+pw+"'";			
			System.out.println(query); // test			
			rs = stmt.executeQuery(query);
			if (rs.next()) {
				// to acquire the first-hand info
				uno = rs.getString("UNO");
				user_name = rs.getString("user_name");
				tel = rs.getString("tel");
				mail = rs.getString("mail");
				name = rs.getString("name");
				school = rs.getString("school");
				coins = Integer.valueOf(rs.getString("coins"));
				credit = Integer.valueOf(rs.getString("credit"));
				// to acquire the address names
				add_str1 = rs.getString("address1");
				add_str2 = rs.getString("address2");
				add_str3 = rs.getString("address3");
				
				// now we don't need to acquire the address

				// output string
				str_out += "&000" + "&01" + uno + "&02" + user_name + "&03" + tel;
				str_out += "&04" + mail + "&05" + add_str1 + "&06" + name;
				str_out += "&08" + school + "&09" + add_str2;
				str_out += "&10" + add_str3 + "&11" + coins + "&12" + credit;

			} else { // if user_name and password are wrong
				str_out += "&005";
			}
		} catch (SQLException e) {
			e.printStackTrace();
			str_out += "&003";
		}
	}

	public void modify_info() // change user's information (do not change UNO/user_id)
	{
		String query;
		ResultSet rs;
		int rnum;
		
		try {
			if( !mod_info_verify() ) {
				str_out += "&004"; // if data not correct
				System.out.println("User_Info data not correct!");
				return;
			}
			
			// addr1 = Integer.valueOf(add_str1);
			query = "select add_name from address where add_id=" + add_str1;
			System.out.println(query); // test
			rs = stmt.executeQuery(query);
			if (!rs.next()) {
				System.out.println("address1 is not existed!");
				str_out += "&004"; // the address data wrong
			}
			if (!add_str2.isEmpty()) {
				//addr2 = Integer.valueOf(add_str2);
				query = "select add_name from address where add_id=" + add_str2;
				System.out.println(query); // test
				rs = stmt.executeQuery(query);
				if (!rs.next()) {
					System.out.println("address2 is not existed!");
					str_out += "&004"; // the address data wrong
				}
			}
			if (!add_str3.isEmpty()) {
				//addr3 = Integer.valueOf(add_str3);
				query = "select add_name from address where add_id=" + add_str3;
				System.out.println(query); // test
				rs = stmt.executeQuery(query);
				if (!rs.next()) {
					System.out.println("address3 is not existed!");
					str_out += "&004"; // the address data wrong
				}
			}
			
			String update = "update user\nset" + " user_name='"+user_name+"'" + ", pw='"+pw+"'" + ", tel='"+tel+"'"
					 + ", mail='"+mail+"'" + ", name='"+name+"'" + ", school='"+school+"'" 
					 + ", address1="+add_str1 + ", address2="+add_str2 + ", address3="+add_str3 
					+ "\nwhere UNO=" + "'"+uno+"'";
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
	
	public boolean mod_info_verify() { // verify if the data is right for modifying user_info
		if( uno.length()==10 && !user_name.isEmpty() && user_name.length()<=20 && !pw.isEmpty() && pw.length()<=20 
				&& tel.length()==11 && !mail.isEmpty() && mail.length()<=50 && !add_str1.isEmpty() && add_str1.length()<=5 
				&& !name.isEmpty() && name.length()<=20 && add_str2.length()<=5 && add_str3.length()<=5 && school.length()<=50 )
			return true;
		else
			return false;
	}
	
	public void addr_list() { // address list
		ResultSet rs;
		int add_id; //index
		int rnum = 0; //地址列表所需行数（应取索引最大值+1）
		String add_name; //address name
		
		str_out = ""; //重置为空字符串，因为循环过后需要在指令类型和字符串之间插入东西！		
		try {
			String query = "select * from address";
			System.out.println(query); // test
			rs = stmt.executeQuery(query);
			while (rs.next()) {
				add_id = Integer.valueOf( rs.getString("add_id") );				
				add_name = rs.getString("add_name");				
				if(add_id > rnum)
					rnum = add_id;				
				str_out += "&20"+add_id+"&21"+add_name;				
			}
			str_out = "&03&000&19" + (rnum+1) + str_out;
		} catch (SQLException e) {
			e.printStackTrace();
			str_out = "&03&003";
		}
	}
	
}