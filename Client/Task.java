package com.example.zhouqian.myapplication;

/**
 * Created by Zhouqian on 2018/11/21.
 */
public class Task {
    //以下为任务基本信息
    private String TNO;//任务id
    private String Usr1;//甲方用户
    private int Usr1Id[];//甲方用户id
    private String Usr2;//乙方用户
    private int Usr2Id[];
    private String TaskName;//任务标题
    private String In_Address;//收货地址
    private String CompanyName;//快递公司
    private String Number;//快递单号（建议更改变量名）
    private String ddl;//取快递的ddl
    private String Out_Address;//收货地址
    private String Out_Time;//收货时间
    private String Usr1_TelephoneNumber;//手机号码，可能还需要乙方手机号
//    public int num[];//手机号后四位，感觉并不需要
    private String Text;//正文

    //数据库接口
    public void Init(String ID){
        TNO="0001";
        Usr1="周乾";
     //   Usr1Id=
        Usr2="周坤";
     //   Usr2Id=
        TaskName="儿子给我取快递";
        In_Address="十四号楼";
        CompanyName="顺丰快递";
        Number="000001";
        ddl="11.21 18:00前";
        Out_Address="紫荆三号楼106B";
        Out_Time="2018//11//21";
        Usr1_TelephoneNumber="13900000000";
        Text="取快递";
    } 
    
    //做数据库的同学需要写这个部分,从数据库获得数据
    public void InfoSendtoSql(){}//传递数据到数据库

    //以下为数据获取接口
    public String GetTNO(){return TNO;}
    public String GetTaskName(){return TaskName;}
    public String GetUsr1Tele(){return Usr1_TelephoneNumber;}
    public String GetLast4TeleNumber(String Tele){
        int length = Tele.length();
        if(length >= 4){
            String str = Tele.substring(length-4, length);
            return str;
        }else{
            return Tele;
        }
    }//如果长度不足4位就返回原数组
    public String GetUsr1Name(){return Usr1;}
    public String GetUsr2Name(){return Usr2;}
    public String GetInAddress(){return In_Address;}
    public String GetCompanyName(){return CompanyName;}
    public String GetNumber(){return Number;}
    public String GetDDL(){return ddl;}
    public String GetOutAddress(){return Out_Address;}
    public String GetOutTime(){return Out_Time;}
    public String GetText(){return Text;}

    //数据修改
    public void SetTNO(String ID){TNO=ID;}
    public void SetUsr1Tele(String Tele){Usr1_TelephoneNumber=Tele;}
    public void SetUsr1Name(String Name){Usr1=Name;}
    public void SetUsr2Name(String Name){Usr2=Name;}
    public void SetInAddress(String Add){In_Address=Add;}
    public void SetCompanyName(String Name){CompanyName=Name;}
    public void SetNumber(String Num){Number=Num;}
    public void SetDDL(String DDL){ddl=DDL;}
    public void SetOutAddress(String Add){Out_Address=Add;}
    public void SetOutTime(String Time){Out_Time=Time;}
    public void SetText(String T){Text=T;}
}
