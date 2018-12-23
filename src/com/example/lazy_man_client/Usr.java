package Client; // you need to change the package name according to your own project

/**
 * Created by Zhouqian on 2018/11/21.
 * Modified by Zhouqian on 2018/12/10.
 * Modified by ZhongHaodong on 2018/12/12.
     ��Ҫ�Ķ�������˴������ͱ������ͻ��˿��Ը�����һint����ʶ�����ݿ�����Ƿ�ɹ��Լ��������ͣ�
     ȥ����ѧ�������ԣ�
     ����˵�ַ�б�ı����Ͳ��������ݿ����û���ֻ�洢��ַ��ţ���ע��͸�����Ϣ��ʱ����Ҫͬʱ��ȡ�û���Ϣ�����е�ַ���б���ƥ����ʾ
     ɾ����ע�͵���һЩ��ʱ����Ҫ�ĺ�������ԭ����Init()������ΪUsr��Ĺ��캯��
 */

/**
 * Modified by ZhongHaodong on 2018/12/21.
 * ��Ҫ�Ķ���ȥ���˵�ַ�б���ַ��Ų��ΪArea��Address
 *
 */

//�÷���Task����

public class Usr {
	 // �������ͣ�0Ϊ�ɹ���1Ϊָ�����2Ϊ�������ݿ�ʧ�ܣ�3Ϊ��ȡ���������ʧ��
	// 4λ�ͻ����ṩ���ݲ��Ϲ淶��5Ϊ�û�����ѧ�����ѱ�ע����û����������
	private int error_type;
	
    private String UsrID;//�û�id����ڼ���
    private String UsrName;//�û���
    private String TeleNumber;//�绰����
    private String Email;//����
    private String RealName;//����
    private String School;//Ժϵ 
    private int Coins;//���
    private int Credit;//����
    private int freez_c; //������
    
    private int[] Address1 = {0,0};//��ַ1����λ����
    private int[] Address2 = {0,0};//��ַ2
    private int[] Address3 = {0,0};//��ַ3

    // �ڹ��캯���г�ʼ��
    public Usr(){
    	error_type = 0;
        UsrID="";
        UsrName="";
        RealName="";
        TeleNumber="";
        Email="";
        School="";
        Coins=100;
        freez_c=0;
        Credit=100;
    }
    
    //��ȡ�û���Ϣ
    public int GetErrorType() {return error_type;}
    public String GetUsrID() {return UsrID;}
    public String GetUsrName() {return UsrName;}
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

    //��ͷָ����Ҫ����ʶ���ַ��������ҳ�棬����Ҫ���ʵ�ַ���
    public void Initial(String example){
        String[] str = example.split("&");
        String action = new String();
        switch(str[1]) {
            case "00":
                action = "regist";//�������ݿ����ݸ�ʽ
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
            	action = "get_info";
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
			switch (flag) {
			case "00":
				int error_type = Integer.parseInt(str[i]);
				this.error_type = error_type;
				break;
			case "01":
				// �û�ID
				String UsrID = cut(strr);
				this.UsrID = UsrID;
				break;
			case "02":
				// �û���
				String UsrName = cut(strr);
				this.UsrName = UsrName;
				break;
			case "03":
				// �ֻ���
				String Tele = cut(strr);
				this.TeleNumber = Tele;
				break;
			case "04":
				// ����
				String email = cut(strr);
				this.Email = email;
				break;
			case "05":
				// ��ַ1
				String add1 = cut(strr);
				if (add1.length() == 4) {
					this.Address1[0] = Integer.parseInt(add1.substring(0, 2));
					this.Address1[1] = Integer.parseInt(add1.substring(2));
				}
				break;
			case "06":
				// ����
				String name = cut(strr);
				this.RealName = name;
				break;
			case "08":
				// Ժϵ
				String school = cut(strr);
				this.School = school;
				break;
			case "09":
				// ��ַ2
				String add2 = cut(strr);
				if (add2.length() == 4) {
					this.Address2[0] = Integer.parseInt(add2.substring(0, 2));
					this.Address2[1] = Integer.parseInt(add2.substring(2));
				}
				break;
			case "10":
				// ��ַ3
				String add3 = cut(strr);
				if (add3.length() == 4) {
					this.Address3[0] = Integer.parseInt(add3.substring(0, 2));
					this.Address3[1] = Integer.parseInt(add3.substring(2));
				}
				break;
			case "11":
				// ���
				String coins = cut(strr);
				this.Coins = Integer.parseInt(coins);
				break;
			case "12":
				// ������
				String credit = cut(strr);
				this.Credit = Integer.parseInt(credit);
				break;
			case "13":
				// ������
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
