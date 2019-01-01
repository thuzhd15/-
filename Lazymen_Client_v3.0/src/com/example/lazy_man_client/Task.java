package com.example.lazy_man_client;

/**
* Created by Zhouqian on 2018/11/21.
* Modified by Zhouqian on 2018/12/10.
* Modified by Zhong Haodong on 2018/12/21.
*/
public class Task {
   //����Ϊ���������Ϣ

   /*ʹ�÷�����
    Task Mytask = new Task();
    Mytask.Initial(example2);
    String taskid = Mytask.GetTNO();
   */

   //      ��������
   //		final String example2= "&51&000&010&020&030&04��Ǭ&05����&06ȡ���&07���ӿ�ȥ����ȡ���&08106B&0912.10&107780&1120&12�ǳ���";
   //      final String example3= "&53&000&010&000&011&000&012";

	// �������ͣ�0Ϊ�ɹ���1Ϊָ�����2Ϊ�������ݿ�ʧ�ܣ�3Ϊ��ȡ���������ʧ�ܣ�4λ�ͻ����ṩ���ݲ��Ϲ淶
	private int error_type;
	private int TNO; // ����id
	private int Task_state; // ����״̬
	private boolean IsMoidfying; // �Ƿ����޸�
	private String Usr1;// �׷��û���
	private String Usr2;// �ҷ��û���
	private String Usr1_TelephoneNumber;// �׷��ֻ�����
	private String Usr2_TelephoneNumber;// �ҷ��ֻ�����
	private int Size;// �����С���Ĵ��ţ�
	private String Content;// ��������
	private String Tele_Last4num;// �ֻ��ź���λ
	private int Coins;// ���ͽ��
	private String Evaluate;// ��������
   // ȡ��ݵ�ַ
   private int[] In_Address = {1,1}; //��λ����
   // ȡ���ʱ��
   private int[] In_Time = {1,1,10,18}; //��λ����
   // ���ӿ�ݵ�ַ
   private int[] Out_Address = {1,1};
   // ���ӿ��ʱ��
   private int[] Out_Time = {1,1,10,22};
   
   public class T{
       public int TNO;
       public int Size; //�����С
       public int[] In_Time = {1,1,10,18}; //ȡ���ʱ��
       public int[] Out_Address = {1,1}; //���ӿ�ݵص�
   };
   private T[] Tasklist;//�����б�
   private int Tasks_Number;
   
	public Task() { // ���캯���г�ʼ��
		error_type = 0;
		TNO = -1; // ����id
		Task_state = 0; // ����״̬
		IsMoidfying = false; // �Ƿ����޸�
		Usr1 = "";// �׷��û���
		Usr2 = "";// �ҷ��û���
		Usr1_TelephoneNumber = "";// �׷��ֻ�����
		Usr2_TelephoneNumber = "";// �ҷ��ֻ�����
		Size = 2;// �����С���Ĵ��ţ�
		Content = "";// ��������
		Tele_Last4num = "";// �ֻ��ź���λ
		Coins = 0;// ���ͽ��
		Evaluate = "";// ��������
		Tasklist = null;
		Tasks_Number = 0;
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
	public T[] GetTasklist() {return Tasklist;}

   //��ͷָ����Ҫ����ʶ���ַ��������ҳ�棨&54ָ���һЩ��ͬ��������Ҫ���ʵ�ַ���
   public void Initial(String example){
       String[] str = example.split("&");
       String action = new String();
       switch(str[1]) {
       case "56":
    	   str2(example);
    	   break;
       case "50":
           action = "releasetask";//�������ݿ����ݸ�ʽ
           str2(example);
           break;
       case "51":
    	   str2(example);
    	   break;
       case "59":
    	   str2(example);
    	   break;
       case "52":
           action = "modifytask";
           str2(example);
           break;
       case "53":
           action = "deletask";
           str2(example);
           break;
       case "54": //�������⣬��Ҫ����str1���������ַ���
           action = "searchtask";
           str1(example);
           break;
       case "55":
           action = "accepttask";
           str2(example);
           break;
       case "57":
           action = "???";
           str2(example);
           break;
       case "58":
           action = "???";
           str2(example);
           break;
       }
   }
   
   //һ����
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
                   //����ID
                   String TASKID = cut(strr);
                   this.TNO = Integer.parseInt(TASKID);
                   break;
               case "02":
                   //����״̬
                   String State = cut(strr);
                   this.Task_state = Integer.parseInt(State);
                   break;
               case "03":
                   //�Ƿ����޸�
                   char ismodify = str[i].charAt(2);
                   if (ismodify=='0'){
                       this.IsMoidfying = false;
                   }
                   else if(ismodify=='1'){
                       this.IsMoidfying = true;
                   }
                   break;
               case "04":
                   //�׷��û�
                   String Usr1name = cut(strr);
                   this.Usr1 = Usr1name;
                   break;
               case "05":
                   //�ҷ��û�
                   String Usr2name = cut(strr);
                   this.Usr2 = Usr2name;
                   break;
               case "17":
            	   //�׷��ֻ���
            	   String Usr1tele = cut(strr);
                   this.Usr1_TelephoneNumber = Usr1tele;
                   break;
               case "18":
            	   //�׷��ֻ���
            	   String Usr2tele = cut(strr);
                   this.Usr2_TelephoneNumber = Usr2tele;
                   break;    
               case "06":
                   //�����С
                   String size = cut(strr);
                   this.Size = Integer.parseInt(size);
                   break;
               case "07":
                   //��������
                   String content = cut(strr);
                   this.Content = content;
                   break;
               case "10":
                   //�ֻ�β��
                   String tele = cut(strr);
                   this.Tele_Last4num = tele;
                   break;
               case "11":
                   //���ͽ��
                   String coins = cut(strr);
                   this.Coins = Integer.parseInt(coins);
                   break;
               case "12":
                   //��������
                   String evaluate = cut(strr);
                   this.Evaluate = evaluate;
                   break;
               case "15":
                   //ȡ��ݵ�ַ
                   String address = cut(strr);
                   if( address.length() == 4 ) {
                	   this.In_Address[0] = Integer.parseInt(address.substring(0,2));
                	   this.In_Address[1] = Integer.parseInt(address.substring(2));
                   }
                   break;
               case "16":
                   //ȡ���ʱ��
                   String time = cut(strr);
                   if( time.length() == 8 ) {
                	   this.In_Time[0] = Integer.parseInt(time.substring(0,2));
                	   this.In_Time[1] = Integer.parseInt(time.substring(2,4));
                	   this.In_Time[2] = Integer.parseInt(time.substring(4,6));
                	   this.In_Time[3] = Integer.parseInt(time.substring(6,8));
                   }
                   break;
               case "08":
                   //���ӿ�ݵ�ַ
                   String address2 = cut(strr);
                   if( address2.length() == 4 ) {
                	   this.Out_Address[0] = Integer.parseInt(address2.substring(0,2));
                	   this.Out_Address[1] = Integer.parseInt(address2.substring(2));
                   }
                   break;
               case "09":
                   //���ӿ��ʱ��
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
   
   //�����б�
   public void str1(String example){
       String[] str = example.split("&");
       this.Tasks_Number = 20;
       int Task_index = -1; //��������ֵ
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
				// ����ID����һ��������Ϣ����ʼ��
				Task_index ++;
				String TASKID = cut(strr);
				this.Tasklist[Task_index] = new T();
				this.Tasklist[Task_index].TNO = Integer.parseInt(TASKID);
				break;
			case "06":
                //�����С
                String size = cut(strr);
                this.Tasklist[Task_index].Size = Integer.parseInt(size);
                break;
			case "16":
                //ȡ���ʱ��
                String time = cut(strr);
                if( time.length() == 8 ) {
             	   this.Tasklist[Task_index].In_Time[0] = Integer.parseInt(time.substring(0,2));
             	   this.Tasklist[Task_index].In_Time[1] = Integer.parseInt(time.substring(2,4));
             	   this.Tasklist[Task_index].In_Time[2] = Integer.parseInt(time.substring(4,6));
             	   this.Tasklist[Task_index].In_Time[3] = Integer.parseInt(time.substring(6,8));
                }
                break;
			case "08":
                //���ӿ�ݵ�ַ
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
