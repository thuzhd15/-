package Client;

/**
* Created by Zhouqian on 2018/11/21.
* Modified by Zhouqian on 2018/12/10.
* Modified by Zhong Haodong on 2018/12/21.
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

	// 错误类型：0为成功，1为指令错误，2为连接数据库失败，3为获取或更改数据失败，4位客户端提供数据不合规范
	private int error_type;
	private int TNO; // 任务id
	private int Task_state; // 进行状态
	private boolean IsMoidfying; // 是否在修改
	private String Usr1;// 甲方用户名
	private String Usr2;// 乙方用户名
	private String Usr1_TelephoneNumber;// 甲方手机号码
	private String Usr2_TelephoneNumber;// 乙方手机号码
	private int Size;// 物件大小（的代号）
	private String Content;// 任务描述
	private String Tele_Last4num;// 手机号后四位
	private int Coins;// 悬赏金币
	private String Evaluate;// 任务评价
   // 取快递地址
   private int[] In_Address = {1,1}; //两位数组
   // 取快递时间
   private int[] In_Time = {1,1,10,18}; //四位数组
   // 交接快递地址
   private int[] Out_Address = {1,1};
   // 交接快递时间
   private int[] Out_Time = {1,1,10,22};
   
   private class T{
       private int TNO;
       private int Size; //物件大小
       private int[] In_Time = {1,1,10,18}; //取快递时间
       private int[] Out_Address = {1,1}; //交接快递地点
   };
   private T Tasklist[];//任务列表
   private int Tasks_Number;
   
	public Task() { //构造函数中初始化
		error_type = 0;
		TNO = -1; // 任务id
		Task_state = 0; // 进行状态
		IsMoidfying = false; // 是否在修改
		Usr1 = "";// 甲方用户名
		Usr2 = "";// 乙方用户名
		Usr1_TelephoneNumber = "";// 甲方手机号码
		Usr2_TelephoneNumber = "";// 乙方手机号码
		Size = 2;// 物件大小（的代号）
		Content = "";// 任务描述
		Tele_Last4num = "";// 手机号后四位
		Coins = 0;// 悬赏金币
		Evaluate = "";// 任务评价
	}

	public int GetErrorType() {return error_type;}
	public int GetTNO(){return TNO;}
	public int GetTaskstate(){return Task_state;}
	public boolean Ismodify(){return IsMoidfying;}
	public String GetUsr1Name(){return Usr1;}
	public String GetUsr2Name(){return Usr2;}
	public String GetUsr1Tele(){return Usr1_TelephoneNumber;}
	public String GetUsr2Tele(){return Usr2_TelephoneNumber;}
	public int GetSize(){return Size;}
	public String GetContent(){return Content;}
	public String GetLast4Tele(){return Tele_Last4num;}
	public int GetCoins(){return Coins;}
	public String GetEva(){return Evaluate;}
	public int[] GetInAddress(){return In_Address;}
	public int[] GetInTime(){return In_Time;}
	public int[] GetOutAddress(){return Out_Address;}
	public int[] GetOutTime(){return Out_Time;}

   //开头指令主要用于识别字符串传输的页面（&53指令还有一些不同），仍需要添加实现方法
   public void Initial(String example){
       String[] str = example.split("&");
       String action = new String();
       switch(str[1]) {
       case "55":
    	   str2(example);
    	   break;
       case "50":
           action = "releasetask";//根据数据库数据格式
           str2(example);
           break;
       case "58":
    	   str2(example);
    	   break;
       case "59":
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
       case "53": //此项特殊，需要进入str1方法解析字符串
           action = "searchtask";
           str1(example);
           break;
       case "54":
           action = "accepttask";
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
            	   String error_type = cut(strr);
                   this.error_type = Integer.parseInt(error_type);
                   break;
               case "01":
                   //任务ID
                   String TASKID = cut(strr);
                   this.TNO = Integer.parseInt(TASKID);
                   break;
               case "02":
                   //进行状态
                   String State = cut(strr);
                   this.Task_state = Integer.parseInt(State);
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
                   String Usr1name = cut(strr);
                   this.Usr1 = Usr1name;
                   break;
               case "05":
                   //乙方用户
                   String Usr2name = cut(strr);
                   this.Usr2 = Usr2name;
                   break;
               case "17":
            	   //甲方手机号
            	   String Usr1tele = cut(strr);
                   this.Usr1_TelephoneNumber = Usr1tele;
                   break;
               case "18":
            	   //甲方手机号
            	   String Usr2tele = cut(strr);
                   this.Usr2_TelephoneNumber = Usr2tele;
                   break;    
               case "06":
                   //物件大小
                   String size = cut(strr);
                   this.Size = Integer.parseInt(size);
                   break;
               case "07":
                   //任务描述
                   String content = cut(strr);
                   this.Content = content;
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
               case "15":
                   //取快递地址
                   String address = cut(strr);
                   if( address.length() == 4 ) {
                	   this.In_Address[0] = Integer.parseInt(address.substring(0,2));
                	   this.In_Address[1] = Integer.parseInt(address.substring(2));
                   }
                   break;
               case "16":
                   //取快递时间
                   String time = cut(strr);
                   if( time.length() == 8 ) {
                	   this.In_Time[0] = Integer.parseInt(time.substring(0,2));
                	   this.In_Time[1] = Integer.parseInt(time.substring(2,4));
                	   this.In_Time[2] = Integer.parseInt(time.substring(4,6));
                	   this.In_Time[3] = Integer.parseInt(time.substring(6,8));
                   }
                   break;
               case "08":
                   //交接快递地址
                   String address2 = cut(strr);
                   if( address2.length() == 4 ) {
                	   this.Out_Address[0] = Integer.parseInt(address2.substring(0,2));
                	   this.Out_Address[1] = Integer.parseInt(address2.substring(2));
                   }
                   break;
               case "09":
                   //交接快递时间
                   String time2 = cut(strr);
                   if( time2.length() == 8 ) {
                	   this.Out_Time[0] = Integer.parseInt(time2.substring(0,2));
                	   this.Out_Time[1] = Integer.parseInt(time2.substring(2,4));
                	   this.Out_Time[2] = Integer.parseInt(time2.substring(4,6));
                	   this.Out_Time[3] = Integer.parseInt(time2.substring(6,8));
                   }
                   break;
           }
       }
   }
   
   //任务列表
   public void str1(String example){
       String[] str = example.split("&");
       this.Tasks_Number = 20;
       int Task_index = -1; //任务索引值
       for (int i = 2; i < str.length; i++) {
           char strr[] = str[i].toString().toCharArray();
           char f[]=new char[2];
           f[0]=strr[0];
           f[1]=strr[1];
           String flag=new String(f);
           switch(flag) {
           case "00":
        	   String error_type = cut(strr);
               this.error_type = Integer.parseInt(error_type);
               break;
           case "20":
        	   String number = cut(strr);
               this.Tasks_Number = Integer.parseInt(number);
               this.Tasklist = new T[Tasks_Number];
               break;
			case "01":
				// 任务ID（是一条任务信息的起始）
				Task_index ++;
				String TASKID = cut(strr);
				this.Tasklist[Task_index].TNO = Integer.parseInt(TASKID);
				break;
			case "06":
                //物件大小
                String size = cut(strr);
                this.Tasklist[Task_index].Size = Integer.parseInt(size);
                break;
			case "16":
                //取快递时间
                String time = cut(strr);
                if( time.length() == 8 ) {
             	   this.Tasklist[Task_index].In_Time[0] = Integer.parseInt(time.substring(0,2));
             	   this.Tasklist[Task_index].In_Time[1] = Integer.parseInt(time.substring(2,4));
             	   this.Tasklist[Task_index].In_Time[2] = Integer.parseInt(time.substring(4,6));
             	   this.Tasklist[Task_index].In_Time[3] = Integer.parseInt(time.substring(6,8));
                }
                break;
			case "08":
                //交接快递地址
                String address2 = cut(strr);
                if( address2.length() == 4 ) {
             	   this.Tasklist[Task_index].Out_Address[0] = Integer.parseInt(address2.substring(0,2));
             	   this.Tasklist[Task_index].Out_Address[1] = Integer.parseInt(address2.substring(2));
                }
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
