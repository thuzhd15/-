package com.example.zhouqian.myapplication;

/**
 * Created by Zhouqian on 2018/11/21.
 */
public class Usr {
    private String UsrID;//建议增加用户id项，用于检索
    private String UsrName;//用户名
    private String Password;//密码
    private String RealName;//姓名
    private String SchoolID;//学工号
    private String Sex;//性别
    private String Address1;//地址1
    private String Address2;//地址2
    private String Address3;//地址3
    private String TeleNumber;//电话号码
    private String Email;//邮箱
    private String School;//院系
    private int Coins;//金币

    //需要与数据库对接
    public void Init(String ID){
        UsrID="0001";
        UsrName="zhoug15";
        Password="123";
        RealName="";
        SchoolID="";
        Sex="";
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
    public String GetPassword(){return Password;}
    public String GetRealName(){return  RealName;}
    public String GetSchoolID(){return SchoolID;}
    public String GetSex(){return Sex;}
    public String GetAddress1(){return Address1;}
    public String GetAddress2(){return Address2;}
    public String GetAddress3(){return Address3;}
    public String GetTeleNumber(){return TeleNumber;}
    public String GetEmai(){return Email;}
    public String GetSchool(){return School;}
    public int GetCoins(){return Coins;}

    //修改用户信息
    public void SetUsrID(String ID){UsrID=ID;}
    public void SetUsrName(String name){UsrName=name;}
    public void SetPassword(String pw){Password=pw;}
    public void SetRealName(String name){RealName=name;}
    public void SetSchoolID(String id){SchoolID=id;}
    public void SetSex(String sex){Sex=sex;}
    public void SetAddress1(String add1){Address1=add1;}
    public void SetAddress2(String add2){Address2=add2;}
    public void SetAddress3(String add3){Address3=add3;}
    public void SetTeleNumber(String tele){TeleNumber=tele;}
    public void SetEmai(String email){Email=email;}
    public void SetSchool(String school){School=school;}
    public void SetCoins(int coins){Coins=coins;}


}
