
public class STR {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		final String example1= "&00&001&012015012015&02����&030000111122&04lisi15@mails.tsinghua.edu.cn&05�Ͼ�8#110B";
		final String example2= "";
		str1(example1);
//		str2(example2);
	}


	    public static void str1(String example)
	    {
	        String[] str = example.split("&");
	        String action = new String();
	        System.out.println(str[1]);	
	        switch(str[1]) {
	        case "00":
	        	action = "regist";
	        	break;
	        case "01":
	        	action = "login";
	        	break;
	        case "02":
	        	action = "modifyinfo";
	        	break;
	        }

	        for (int i=2;i<str.length;i++){
	            char strr[]=str[i].toString().toCharArray();	           
	            switch(str[i]) {
	            case "00":
	            	if(strr[2]=='1'){
	                        //TODO
	                    }
	                    else{
	                        //TODO
	                    }
	            	break;
	            case "01":
	            	//�û�ID
	            	String UsrID = cut(strr);
	            	break;
	            case "02":
	            	 //�û���
                    String UsrName = cut(strr);
	            	break;
	            case "03":
	            	//�ֻ���
                    String TELE = cut(strr);
	            	break;
	            case "04":
	            	//����
                    String EMAIL = cut(strr);
	            	break;
	            case "05":
	            	//��ַ
                    String Address = cut(strr);
	            	break;
	            case "06":
                    //����
                    String NAME = cut(strr);
	            	break;
	            case "07":
                    String SchoolID = cut(strr);
                    //ѧ����
	            	break;
	            case "08":
                    //Ժϵ��Ϣ
                    String School = cut(strr);
	            	break;
	            case "09":
                    //��ַ2
                    String Address2 = cut(strr);
	            	break;
	            case "10":
                    //��ַ3
                    String Address3 = cut(strr);
	            	break;
	            case "11":
                    //���
                    String coins = cut(strr);
                    int Coins = Integer.parseInt(coins);
	            	break;
	            case "12":
                    //����
                    String xinyu = cut(strr);
                    int Xinyu = Integer.parseInt(xinyu);
	            	break;            	
	            }                
	        }
	    }

	    public static void str2(String example) {
	        String[] str = example.split("&");
	        String action = new String();
	        int numberoftask = 0;
	        switch(str[1]) {
	        case "50":
	        	action = "releasetask";
	        	break;
	        case "51":
	        	action = "modifytask";
	        	break;
	        case "52":
	        	action = "deletask";
	        	break;
	        }
	        for (int i = 2; i < str.length; i++) {
	            char strr[] = str[i].toString().toCharArray();
	            switch(str[i]) {
	            case "00":
	            	if(strr[2]=='1'){
	                        //TODO
	                    }
	                    else{
	                        //TODO
	                    }
	            	break;
	            case "01":
                    //����ID
                    String TASKID = cut(strr);
	            	break;
	            case "02":
	            	 //����״̬
                    String State = cut(strr);
	            	break;
	            case "03":
                    //�Ƿ����޸�
 //                   String Ismodify ==
	            	break;
	            case "04":
                    //�׷��û�
                    String Usr1ID = cut(strr);
	            	break;
	            case "05":
                    //�ҷ��û�
                    String Usr2ID = cut(strr);
	            	break;
	            case "06":
                    //�������
                    String TaskName = cut(strr);
	            	break;
	            case "07":
                    //ȡ��ݵ�ַ
                    String InAddress = cut(strr);
	            	break;
	            case "08":
                    //��ݹ�˾
                    String Company = cut(strr);
	            	break;
	            case "09":
                    //��ݱ��
                    String Number = cut(strr);
	            	break;
	            case "10":
                    //ȡ���ddl
                    String DDL = cut(strr);
	            	break;
	            case "11":
                    //���ӿ�ݵ�ַ
                    String OutAddress = cut(strr);
	            	break;
	            case "12":
                    //���ӿ��ʱ��
                    String OutTime = cut(strr);
	            	break; 
	            case "13":
                    //�ֻ���
                    String Tele = cut(strr);
	            	break; 
	            case "14":
                    //�ֻ�β��
                    String Last4Tele = cut(strr);
	            	break; 
	            case "15":
                    //��������
                    String Text = cut(strr);
	            	break; 
	            case "16":
                    //���ͽ��
                    String coins = cut(strr);
                    int Coins = Integer.parseInt(coins);
	            	break; 
	            case "17":
                    //��������
                    String Evaluate = cut(strr);
	            	break; 
	            case "18":
                    //������������
                    String DRealseR = cut(strr);
	            	break; 
	            case "19":
                    //������������
                    String DAcceptR = cut(strr);
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
