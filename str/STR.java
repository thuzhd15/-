
public class STR {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		final String example1= "&00&001&012015012015&02李四&030000111122&04lisi15@mails.tsinghua.edu.cn&05紫荆8#110B";
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
	            	//用户ID
	            	String UsrID = cut(strr);
	            	break;
	            case "02":
	            	 //用户名
                    String UsrName = cut(strr);
	            	break;
	            case "03":
	            	//手机号
                    String TELE = cut(strr);
	            	break;
	            case "04":
	            	//邮箱
                    String EMAIL = cut(strr);
	            	break;
	            case "05":
	            	//地址
                    String Address = cut(strr);
	            	break;
	            case "06":
                    //姓名
                    String NAME = cut(strr);
	            	break;
	            case "07":
                    String SchoolID = cut(strr);
                    //学工号
	            	break;
	            case "08":
                    //院系信息
                    String School = cut(strr);
	            	break;
	            case "09":
                    //地址2
                    String Address2 = cut(strr);
	            	break;
	            case "10":
                    //地址3
                    String Address3 = cut(strr);
	            	break;
	            case "11":
                    //金币
                    String coins = cut(strr);
                    int Coins = Integer.parseInt(coins);
	            	break;
	            case "12":
                    //信誉
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
                    //任务ID
                    String TASKID = cut(strr);
	            	break;
	            case "02":
	            	 //进行状态
                    String State = cut(strr);
	            	break;
	            case "03":
                    //是否在修改
 //                   String Ismodify ==
	            	break;
	            case "04":
                    //甲方用户
                    String Usr1ID = cut(strr);
	            	break;
	            case "05":
                    //乙方用户
                    String Usr2ID = cut(strr);
	            	break;
	            case "06":
                    //任务标题
                    String TaskName = cut(strr);
	            	break;
	            case "07":
                    //取快递地址
                    String InAddress = cut(strr);
	            	break;
	            case "08":
                    //快递公司
                    String Company = cut(strr);
	            	break;
	            case "09":
                    //快递编号
                    String Number = cut(strr);
	            	break;
	            case "10":
                    //取快递ddl
                    String DDL = cut(strr);
	            	break;
	            case "11":
                    //交接快递地址
                    String OutAddress = cut(strr);
	            	break;
	            case "12":
                    //交接快递时间
                    String OutTime = cut(strr);
	            	break; 
	            case "13":
                    //手机号
                    String Tele = cut(strr);
	            	break; 
	            case "14":
                    //手机尾号
                    String Last4Tele = cut(strr);
	            	break; 
	            case "15":
                    //任务内容
                    String Text = cut(strr);
	            	break; 
	            case "16":
                    //悬赏金币
                    String coins = cut(strr);
                    int Coins = Integer.parseInt(coins);
	            	break; 
	            case "17":
                    //任务评价
                    String Evaluate = cut(strr);
	            	break; 
	            case "18":
                    //撤销发布理由
                    String DRealseR = cut(strr);
	            	break; 
	            case "19":
                    //撤销接受理由
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
