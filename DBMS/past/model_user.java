package DBMS;

import java.io.*;
import java.sql.*;
import java.util.*;

public class model_user {
	public Statement stmt;
	public String str_out;
	public String[] array_in;
	
	String uno, user_name, pw, tel, mail, name, number, school, add_str1, add_str2, add_str3;
	int addr1, addr2, addr3, coins, credit;

	public model_user(Statement st, String[] array, String str) {
		stmt = st;
		str_out = str;
		array_in = array;
		
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

		if (!resolve()) { // ½âÎö×Ö·û´®
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
			user_info(); // modify the user information
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
		ResultSet rs;
		int rnum;
		try {
			if( !data_verify() ) {
				str_out += "&004"; // if data not enough
				return;
			}
			
			String query = "select name from user where UNO=" + "\""+uno+"\"" + " or user_name=" + "\""+user_name+"\"";			
			System.out.println(query); // test
			rs = stmt.executeQuery(query);
			if (rs.next()) {
				str_out += "&005"; // if be the same as some of the existed users
				return;
			}
			
			addr1 = Integer.valueOf(add_str1);
			query = "select add_name from address where add_id=" + addr1;
			System.out.println(query); // test
			rs = stmt.executeQuery(query);
			if (!rs.next()) {
				System.out.println("address1 is not existed!");
				str_out += "&004"; // the address data wrong
			}
			if (!add_str2.isEmpty()) {
				addr2 = Integer.valueOf(add_str2);
				query = "select add_name from address where add_id=" + addr2;
				System.out.println(query); // test
				rs = stmt.executeQuery(query);
				if (!rs.next()) {
					System.out.println("address2 is not existed!");
					str_out += "&004"; // the address data wrong
				}
			}
			if (!add_str3.isEmpty()) {
				addr3 = Integer.valueOf(add_str3);
				query = "select add_name from address where add_id=" + addr3;
				System.out.println(query); // test
				rs = stmt.executeQuery(query);
				if (!rs.next()) {
					System.out.println("address3 is not existed!");
					str_out += "&004"; // the address data wrong
				}
			}
			
			String update = "insert into user(UNO,user_name,pw,tel,mail,name,school,address1,address2,address3)"
					+ "values(" + "\""+uno+"\"" + ",\""+user_name+"\"" + ",\""+pw+"\"" + ",\""+tel+"\"" + ",\""+mail+"\"" + ",\""+name+"\"" 
					+ ",\""+school+"\"" + ",\""+add_str1+"\"" + ",\""+add_str2+"\"" + ",\""+add_str3+"\"" + ")";
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

	public void login() {
		ResultSet rs;
		try {
			String query = "select * from user where user_name=" + "\""+user_name+"\"" + " and pw=" + "\""+pw+"\"";			
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

				if (!add_str1.isEmpty()) {
					addr1 = Integer.valueOf(add_str1);
					query = "select add_name from address where add_id=" + addr1;
					System.out.println(query); // test
					rs = stmt.executeQuery(query);
					if (rs.next()) {
						add_str1 = rs.getString("add_name");
					} else {
						System.out.println("Kidding? : address1 is empty!");
					}
				} else {
					System.out.println("Kidding? : address1 is empty!");
				}
				if (!add_str2.isEmpty()) {
					addr2 = Integer.valueOf(add_str2);
					query = "select add_name from address where add_id=" + addr2;
					System.out.println(query); // test
					rs = stmt.executeQuery(query);
					if (rs.next())
						add_str2 = rs.getString("add_name");
				}
				if (!add_str3.isEmpty()) {
					addr3 = Integer.valueOf(add_str3);
					query = "select add_name from address where add_id=" + addr3;
					System.out.println(query); // test
					rs = stmt.executeQuery(query);
					if (rs.next())
						add_str3 = rs.getString("add_name");
				}

				// output string
				str_out += "&01" + uno + "&02" + user_name + "&03" + tel;
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

	public void user_info() // change user's information
	{
		ResultSet rs;
		try {
			rs = stmt.executeQuery("select * from address");
			while (rs.next()) {
				str_out += "&" + rs.getString("add_name");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			str_out += "&000";
		}
	}
	
	public void addr_list() {
		ResultSet rs;
		try {
			String query = "select * from user where user_name=" + "\""+user_name+"\"" + " and pw=" + "\""+pw+"\"";			
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
				String add_name1, add_name2, add_name3;
				add_name1 = rs.getString("address1");
				add_name2 = rs.getString("address2");
				add_name3 = rs.getString("address3");

				if (!add_name1.isEmpty()) {
					addr1 = Integer.valueOf(add_name1);
					query = "select add_name from address where add_id=" + addr1;
					System.out.println(query); // test
					rs = stmt.executeQuery(query);
					if (rs.next()) {
						add_name1 = rs.getString("add_name");

						if (!add_name2.isEmpty()) {
							addr2 = Integer.valueOf(add_name2);
							query = "select add_name from address where add_id=" + addr2;
							System.out.println(query); // test
							rs = stmt.executeQuery(query);
							if (rs.next())
								add_name2 = rs.getString("add_name");
						}
						if (!add_name3.isEmpty()) {
							addr3 = Integer.valueOf(add_name3);
							query = "select add_name from address where add_id=" + addr3;
							System.out.println(query); // test
							rs = stmt.executeQuery(query);
							if (rs.next())
								add_name3 = rs.getString("add_name");
						}

						// output string
						str_out += "&01" + uno + "&02" + user_name + "&03" + tel;
						str_out += "&04" + mail + "&05" + add_name1 + "&06" + name;
						str_out += "&08" + school + "&09" + add_name2;
						str_out += "&10" + add_name3 + "&11" + coins + "&12" + credit;

					} else {
						str_out += "&003";
					}

				} else {
					str_out += "&003";
				}

			} else {
				str_out += "&004";
			}
		} catch (SQLException e) {
			e.printStackTrace();
			str_out += "&003";
		}
	}

	public boolean data_verify() { // verify if the data is right
		if( uno.length()==10 && !user_name.isEmpty() && user_name.length()<=20 && !pw.isEmpty() && pw.length()<=10 
				&& tel.length()==11 && !mail.isEmpty() && mail.length()<=50 && !add_str1.isEmpty() && add_str1.length()<=5 
				&& !name.isEmpty() && name.length()<=20 && add_str2.length()<=5 && add_str3.length()<=5 && school.length()<=50 )
			return true;
		else
			return false;
	}
}