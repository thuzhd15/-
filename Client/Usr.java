package com.example.zhouqian.myapplication;

/**
 * Created by Zhouqian on 2018/11/21.
 */

//用法与Task类似


public class Usr {
    private String UsrID;//建议增加用户id项，用于检索
    private String UsrName;//用户名
    private String TeleNumber;//电话号码
    private String Email;//邮箱
    private String Address1;//地址1
    private String RealName;//姓名
    private String SchoolID;//学工号
    private String School;//院系
    private String Address2;//地址2
    private String Address3;//地址3
    private int Coins;//金币
    private int Credit;//信誉

    //private String Password;//密码
    //private String Sex;//性别
 //   public void print() {
   //     System.out.println(UsrName);
   // }

    //需要与数据库对接
    public void Init(String ID){
        UsrID="0001";
        UsrName="zhoug15";
//        Password="123";
        RealName="";
        SchoolID="";
//        Sex="";
        Address1="紫荆公寓3号楼106B";
        Address2="";
        Address3="";
        TeleNumber="13900000000";
        Email="";
        School="";
        Coins=100;
    }
    public void SendInfotoSql(){}

    //获取用户信息
    public String GetUsrID(){return UsrID;}
    public String GetUsrName(){return UsrName;}
    //    public String GetPassword(){return Password;}
    public String GetRealName(){return  RealName;}
    public String GetSchoolID(){return SchoolID;}
    //    public String GetSex(){return Sex;}
    public String GetAddress1(){return Address1;}
    public String GetAddress2(){return Address2;}
    public String GetAddress3(){return Address3;}
    public String GetTeleNumber(){return TeleNumber;}
    public String GetEmail(){return Email;}
    public String GetSchool(){return School;}
    public int GetCoins(){return Coins;}
    public int GetCredit() {return Credit;}

    //修改用户信息
    public void SetUsrID(String ID){UsrID=ID;}
    public void SetUsrName(String name){UsrName=name;}
    //    public void SetPassword(String pw){Password=pw;}
    public void SetRealName(String name){RealName=name;}
    public void SetSchoolID(String id){SchoolID=id;}
    //    public void SetSex(String sex){Sex=sex;}
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
        }
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
            switch(flag) {
                case "00":
                    if(strr[2]=='0'){
                        //TODO
                    }
                    else{
                        //TODO
                    }
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
                case "07":
                    //学工号
                    String schoolid = cut(strr);
                    this.SchoolID = schoolid;
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
