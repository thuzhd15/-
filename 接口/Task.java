package com.example.zhouqian.myapplication;

/**
 * Created by Zhouqian on 2018/11/21.
 */
public class Task {
    //以下为任务基本信息

    /*使用方法：
     Task Mytask = new Task();
     Mytask.Initial(example2);
     String taskid = Mytask.GetTNO();
    */

    //      测试用例
    //		final String example2= "&51&000&010&020&030&04周乾&05周坤&06取快递&07儿子快去给我取快递&08106B&0912.10&107780&1120&12非常好";
    //      final String example3= "&53&000&010&000&011&000&012";

    private String TNO;//任务id
    enum ConditionType{UnRelease,Untaken,Solving,Solved}
    private ConditionType TaskCondition;//进行状态
    private boolean IsMoidfying;//是否在修改
    private String Usr1;//甲方用户
    private String Usr2;//乙方用户
    private String TaskName;//任务标题
    private String Content;//任务描述
    private String Out_Address;//交接快递地址
    private String Out_Time;//交接快递时间
    private String Tele_Last4num;//手机号后四位，感觉并不需要
    private int Coins;//悬赏金币
    private String Evaluate;//任务评价
    private String Reason_DRelease;//撤销发布的理由
    private String Reason_DAccept;//撤销接受的理由
    private String Usr1_TelephoneNumber;//手机号码，可能还需要乙方手机号
    private class T{
        private String TNO;
        private String TaskName;
        //根据需要自行添加
    };
    private T Tasklist[] = new T[5];//任务列表
    private int NumberOfTasks;
//    private int Usr1Id[];//甲方用户id
//   private int Usr2Id[];
    /*
    private String TaskName;//任务标题
    private String In_Address;//收货地址
    private String CompanyName;//快递公司
    private String Number;//快递单号（建议更改变量名）
    */
    // private String ddl;//
//    private String Text;//正文

    //字符拆分并赋值
    public void Init(String ID){
        TNO="0001";
        Usr1="周乾";
        Usr2="周坤";
        Out_Address="紫荆三号楼106B";
        Out_Time="2018//11//21";
        Usr1_TelephoneNumber="13900000000";
    } //做数据库的同学需要写这个部分,从数据库获得数据

    public void InfoSendtoSql(){}//connect/sent可以放里面
 //   public void changtask(){}

    public String GetTNO(){return TNO;}
    public ConditionType GetCondition(){return TaskCondition;}
    public boolean Ismodify(){return IsMoidfying;}
    public String GetUsr1Name(){return Usr1;}
    public String GetUsr2Name(){return Usr2;}
    public String GetTaskName(){return TaskName;}
    public String GetContent(){return Content;}
    public String GetOutAddress(){return Out_Address;}
    public String GetOutTime(){return Out_Time;}
    public String GetLast4Tele(){return Tele_Last4num;}
    public int GetCoins(){return Coins;}
    public String GetEva(){return Evaluate;}
    public String GetReasonOfDeleRelease(){return Reason_DRelease;}
    public String GetReasonOfDeleAccept(){return Reason_DAccept;}

//    public String GetUsr1Tele(){return Usr1_TelephoneNumber;}
 /*   public String GetLast4TeleNumber(String Tele){
        int length = Tele.length();
        if(length >= 4){
            String str = Tele.substring(length-4, length);
            return str;
        }else{
            return Tele;
        }
    }//如果长度不足4位就返回原数组
    */
    //   public String GetInAddress(){return In_Address;}
    //   public String GetCompanyName(){return CompanyName;}
    //   public String GetNumber(){return Number;}
 //   public String GetText(){return Text;}
 //   public String Getddl(){return ddl;}

    //数据修改
    public void SetTNO(String ID){TNO=ID;}
    public void SetTaskName(String Name){TaskName=Name;}
    public void SetUsr1Tele(String Tele){Usr1_TelephoneNumber=Tele;}
    public void SetUsr1Name(String Name){Usr1=Name;}
    public void SetUsr2Name(String Name){Usr2=Name;}
    public void SetOutAddress(String Add){Out_Address=Add;}
    public void SetOutTime(String Time){Out_Time=Time;}
    //   public void SetInAddress(String Add){In_Address=Add;}
    //   public void SetCompanyName(String Name){CompanyName=Name;}
    //   public void SetNumber(String Num){Number=Num;}
  //  public void SetText(String T){Text=T;}
 //   public void Setddl(String d){ddl=d;}



    //选择拆分方法，方法可自行添加
    public void Initial(String example){
        String[] str = example.split("&");
        String action = new String();
        switch(str[1]) {
            case "50":
                action = "releasetask";//根据数据库数据格式
                str2(example);
                break;
            case "51":
                action = "modifytask";
                str2(example);
                break;
            case "52":
                action = "deletask";
                str2(example);
                break;
            case "53":
                action = "searchtask";
                str1(example);
                break;
            case "54":
                action = "accepttask";
                str2(example);
                break;
            case "55":
                action = "deleacceptedtask";
                str2(example);
                break;
            case "56":
                action = "???";
                str2(example);
                break;
            case "57":
                action = "???";
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
                    //任务ID
                    String TASKID = cut(strr);
                    this.TNO = TASKID;
                    break;
                case "02":
                    //进行状态
                    String State = cut(strr);
                    switch(State){
                        case"1":
                            this.TaskCondition = ConditionType.UnRelease;
                            break;
                        case"2":
                            break;
                    }
                    break;
                case "03":
                    //是否在修改
                    char ismodify = str[i].charAt(2);
                    if (ismodify=='0'){
                        this.IsMoidfying = false;
                    }
                    else if(ismodify=='1'){
                        this.IsMoidfying = true;
                    }

                    break;
                case "04":
                    //甲方用户
                    String Usr1ID = cut(strr);
                    this.Usr1 = Usr1ID;
                    break;
                case "05":
                    //乙方用户
                    String Usr2ID = cut(strr);
                    this.Usr2 = Usr2ID;
                    break;
                case "06":
                    //任务标题
                    String TaskName = cut(strr);
                    this.TaskName = TaskName;
                    break;
                case "07":
                    //任务描述
                    String content = cut(strr);
                    this.Content = content;
                    break;
                case "08":
                    //交接快递地址
                    String address = cut(strr);
                    this.Out_Address = address;
                    break;
                case "09":
                    //交接快递时间
                    String time = cut(strr);
                    this.Out_Time = time;
                    break;
                case "10":
                    //手机尾号
                    String tele = cut(strr);
                    this.Tele_Last4num = tele;
                    break;
                case "11":
                    //悬赏金币
                    String coins = cut(strr);
                    this.Coins = Integer.parseInt(coins);
                    break;
                case "12":
                    //任务评价
                    String evaluate = cut(strr);
                    this.Evaluate = evaluate;
                    break;
                case "13":
                    //撤销发布理由
                    String reasonr = cut(strr);
                    this.Reason_DRelease = reasonr;
                    break;
                case "14":
                    //撤销接受理由
                    String reasona = cut(strr);
                    this.Reason_DAccept = reasona;
                    break;
            }
        }
    }
    //任务列表
    public void str1(String example){
        String[] str = example.split("&");
        this.NumberOfTasks = 0;
        for (int i = 2; i < str.length; i++) {
            char strr[] = str[i].toString().toCharArray();
            char f[]=new char[2];
            f[0]=strr[0];
            f[1]=strr[1];
            String flag=new String(f);
            switch(flag) {
                case "00":
                    if(strr[2]=='0'){
                        this.Tasklist[this.NumberOfTasks] = new T();
                        this.NumberOfTasks++;
                    }
                    else if(strr[2]=='1'){
                        //TODO
                    }
                    else if(strr[2]=='2'){
                        //TODO
                    }
                    else if(strr[2]=='3'){
                        //TODO
                    }
                    break;
                case "01":
                    //任务ID
                    String TASKID = cut(strr);
                    this.Tasklist[this.NumberOfTasks-1].TNO = TASKID;
                    break;
                case "06":
                    //任务标题
                    String TaskName = cut(strr);
                    this.Tasklist[this.NumberOfTasks-1].TaskName = TaskName;
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
