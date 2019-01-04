package com.example.lazy_man_client;// you need to change the package name according to your own project

/**
 * Created by Zhouqian on 2018/11/21.
 * Modified by Zhouqian on 2018/12/10.
 * Modified by ZhongHaodong on 2018/12/12.
     主要改动：添加了错误类型变量，客户端可以根据这一int变量识别数据库操作是否成功以及错误类型；
     去掉了学工号属性；
     添加了地址列表的变量和操作：数据库中用户表只存储地址编号，在注册和更改信息的时候需要同时获取用户信息和所有地址的列表以匹配显示
     删掉或注释掉了一些暂时不需要的函数，把原来的Init()函数改为Usr类的构造函数
 */

/**
 * Modified by ZhongHaodong on 2018/12/21.
 * 主要改动：去掉了地址列表，地址编号拆分为Area与Address
 * Modified by ZhongHaodong on 2018/12/22.
   新增了密码Password的成员变量和解析方法。
 */

//用法与Task类似

public class Usr {
	 // 错误类型，0为成功，1为指令错误，2为连接数据库失败，3为获取或更改数据失败
	// 4位客户端提供数据不合规范，5为用户名或学工号已被注册或用户名密码错误
	private int error_type;
	
    private String UsrID;//用户id项，用于检索
    private String UsrName;//用户名
    private String Password; //密码
    private String TeleNumber;//电话号码
    private String Email;//邮箱
    private String RealName;//姓名
    private String School;//院系 
    private int Coins;//金币
    private int Credit;//信誉
    private int freez_c; //冻结金币
    
    private int[] Address1 = {0,0};//地址1，两位数组
    private int[] Address2 = {0,0};//地址2
    private int[] Address3 = {0,0};//地址3

    // 在构造函数中初始化
    public Usr(){
    	error_type = 0;
        UsrID="";
        UsrName="";
        Password = "";
        RealName="";
        TeleNumber="";
        Email="";
        School="";
        Coins=100;
        freez_c=0;
        Credit=100;
    }
    
    //获取用户信息
    public int GetErrorType() {return error_type;}
    public String GetUsrID() {return UsrID;}
    public String GetUsrName() {return UsrName;}
    public String GetPassword() {return Password;}
    public String GetRealName() {return  RealName;}
    public int[] GetAddress1() {return Address1;}
    public int[] GetAddress2() {return Address2;}
    public int[] GetAddress3() {return Address3;}
    public String GetTeleNumber() {return TeleNumber;}
    public String GetEmail() {return Email;}
    public String GetSchool(){return School;}
    public int GetCoins() {return Coins;}
    public int GetFreez() {return freez_c;}
    public int GetCredit() {return Credit;}

    // 初始化字符串
	public void Initial(String example) {
		str2(example);
	}

    //一般拆分
    public void str2(String example) {
        String[] str = example.split("&");
        
        for (int i = 2; i < str.length; i++) {
            char strr[] = str[i].toString().toCharArray();
            char f[]=new char[2];
            f[0]=strr[0];
            f[1]=strr[1];
            String flag=new String(f);
			switch (flag) {
			case "00":
				int error_type = Integer.parseInt(str[i]);
				this.error_type = error_type;
				break;
			case "01":
				// 用户ID
				String UsrID = cut(strr);
				this.UsrID = UsrID;
				break;
			case "02":
				// 用户名
				String UsrName = cut(strr);
				this.UsrName = UsrName;
				break;
			case "14":
				// 密码
				String Password = cut(strr);
				this.Password = Password;
				break;
			case "03":
				// 手机号
				String Tele = cut(strr);
				this.TeleNumber = Tele;
				break;
			case "04":
				// 邮箱
				String email = cut(strr);
				this.Email = email;
				break;
			case "05":
				// 地址1
				String add1 = cut(strr);
				if (add1.length() == 4) {
					this.Address1[0] = Integer.parseInt(add1.substring(0, 2));
					this.Address1[1] = Integer.parseInt(add1.substring(2));
				}
				break;
			case "06":
				// 姓名
				String name = cut(strr);
				this.RealName = name;
				break;
			case "08":
				// 院系
				String school = cut(strr);
				this.School = school;
				break;
			case "09":
				// 地址2
				String add2 = cut(strr);
				if (add2.length() == 4) {
					this.Address2[0] = Integer.parseInt(add2.substring(0, 2));
					this.Address2[1] = Integer.parseInt(add2.substring(2));
				}
				break;
			case "10":
				// 地址3
				String add3 = cut(strr);
				if (add3.length() == 4) {
					this.Address3[0] = Integer.parseInt(add3.substring(0, 2));
					this.Address3[1] = Integer.parseInt(add3.substring(2));
				}
				break;
			case "11":
				// 金币
				String coins = cut(strr);
				this.Coins = Integer.parseInt(coins);
				break;
			case "12":
				// 信誉度
				String credit = cut(strr);
				this.Credit = Integer.parseInt(credit);
				break;
			case "13":
				// 冻结金币
				String freez_c = cut(strr);
				this.freez_c = Integer.parseInt(freez_c);
				break;
			}
        }
    }

    public static String cut(char strr[]){
        char string[]=new char[strr.length-2];
        for(int j=0;j<strr.length-2;j++){string[j]=strr[j+2];}
        String S = new String(string);
        return S;
    }
}
