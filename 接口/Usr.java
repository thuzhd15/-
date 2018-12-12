package test; // you need to change the package name according to your own project

/**
 * Created by Zhouqian on 2018/11/21.
 * Modified by Zhouqian on 2018/12/10.
 * Modified by ZhongHaodong on 2018/12/12.
     主要改动：添加了错误类型变量，客户端可以根据这一int变量识别数据库操作是否成功以及错误类型；
     去掉了学工号属性；
     添加了地址列表的变量和操作：数据库中用户表只存储地址编号，在注册和更改信息的时候需要同时获取用户信息和所有地址的列表以匹配显示
     删掉或注释掉了一些暂时不需要的函数，把原来的Init()函数改为Usr类的构造函数
 */

//用法与Task类似

public class Usr {
	 // 错误类型，0为成功，1为指令错误，2位连接数据库失败，3为获取或更改数据失败
	// 4位客户端提供数据不合规范，5为用户名或学工号已被注册或用户名密码错误
	private int error_type;
	
    private String UsrID;//用户id项，用于检索
    private String UsrName;//用户名
    private String TeleNumber;//电话号码
    private String Email;//邮箱
    private String Address1;//地址1
    private String RealName;//姓名
    private String School;//院系
    private String Address2;//地址2
    private String Address3;//地址3
    private int Coins;//金币
    private int Credit;//信誉
    private String[] addr_list; // 所有地址的列表

 //   public void print() {
   //     System.out.println(UsrName);
   // }

    // 在构造函数中初始化
    public Usr(){
    	error_type = 0;
        UsrID="";
        UsrName="";
        RealName="";
        Address1="";
        Address2="";
        Address3="";
        TeleNumber="";
        Email="";
        School="";
        Coins=100;
        Credit=100;
        addr_list=null;
        
        // 当希望用默认变量测试的时候，可以使用以下注释掉的代码段
    	/*error_type = 0;
        UsrID="2015012031";
        UsrName="zhoug15";
        RealName="";
        Address1="3";
        Address2="";
        Address3="";
        TeleNumber="13900000000";
        Email="";
        School="";
        Coins=100;
        Credit=100;
        addr_list=null;*/
    }
    
    //获取用户信息
    public int GetErrorType() {return error_type;}  
    public String GetUsrID(){return UsrID;}
    public String GetUsrName(){return UsrName;}
    public String GetRealName(){return  RealName;}
    public String GetAddress1(){return Address1;}
    public String GetAddress2(){return Address2;}
    public String GetAddress3(){return Address3;}
    public String GetTeleNumber(){return TeleNumber;}
    public String GetEmail(){return Email;}
    public String GetSchool(){return School;}
    public int GetCoins(){return Coins;}
    public int GetCredit() {return Credit;}
    public String[] GetAddrList() {return addr_list;}

    //修改用户信息
    public void SetUsrID(String ID){UsrID=ID;}
    public void SetUsrName(String name){UsrName=name;}
    public void SetRealName(String name){RealName=name;}
    public void SetAddress1(String add1){Address1=add1;}
    public void SetAddress2(String add2){Address2=add2;}
    public void SetAddress3(String add3){Address3=add3;}
    public void SetTeleNumber(String tele){TeleNumber=tele;}
    public void SetEmail(String email){Email=email;}
    public void SetSchool(String school){School=school;}
    public void SetCoins(int coins){Coins=coins;}

    //选择拆分方法，方法可自行添加
    public void Initial(String example){
        String[] str = example.split("&");
        String action = new String();
        switch(str[1]) {
            case "00":
                action = "regist";//根据数据库数据格式
                str2(example);
                break;
            case "01":
                action = "login";
                str2(example);
                break;
            case "02":
                action = "modifyinfo";
                str2(example);
                break;
            case "03":
            	action = "address_list";
            	str2(example);
            	break;
        }
    }

    //一般拆分
    public void str2(String example) {
        String[] str = example.split("&");
        int index = 0; //地址列表的序号（需要定义在循环之前！）
        
        for (int i = 2; i < str.length; i++) {
            char strr[] = str[i].toString().toCharArray();
            char f[]=new char[2];
            f[0]=strr[0];
            f[1]=strr[1];
            String flag=new String(f);
            switch(flag) {
                case "00":
                    int error_type = Integer.valueOf(str[i]);
                    this.error_type = error_type;
                    break;
                case "01":
                    //用户ID
                    String UsrID = cut(strr);
                    this.UsrID = UsrID;
                    break;
                case "02":
                    //用户名
                    String UsrName = cut(strr);
                    this.UsrName = UsrName;
                    break;
                case "03":
                    //手机号
                    String Tele = cut(strr);
                    this.TeleNumber = Tele;
                    break;
                case "04":
                    //邮箱
                    String email = cut(strr);
                    this.Email = email;
                    break;
                case "05":
                    //地址1
                    String add1 = cut(strr);
                    this.Address1 = add1;
                    break;
                case "06":
                    //姓名
                    String name = cut(strr);
                    this.RealName = name;
                    break;
                case "08":
                    //院系
                    String school = cut(strr);
                    this.School = school;
                    break;
                case "09":
                    //地址2
                    String add2 = cut(strr);
                    this.Address2 = add2;
                    break;
                case "10":
                    //地址3
                    String add3 = cut(strr);
                    this.Address3 = add3;
                    break;
                case "11":
                    //金币
                    String coins = cut(strr);
                    this.Coins = Integer.parseInt(coins);
                    break;
                case "12":
                    //信誉度
                    String credit = cut(strr);
                    this.Credit = Integer.parseInt(credit);
                    break;
                //后面三项都是与地址列表相关
                case "19":                	
                	int num = Integer.parseInt(cut(strr));
                	this.addr_list = new String[num]; //为地址列表分配空间
                	
                	// 字符串数组初始化为每个元素都为空（防止有不存在的索引）；
                	// 调用者注意在字符串数组中加入对每个字符串元素是否为空的判断，非空者才显示出来且可更改
                	for(int j=0;j<num;j++)
                		this.addr_list[j] = "";
                	
                	break;
                case "20":
                	index = Integer.parseInt(cut(strr)); //记录索引值
                	break;
                case "21":
                	String address = cut(strr);
                	this.addr_list[index] = address; //地址列表元素赋值
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
