package DBMS;
import java.sql.*;
public class MysqlJdbc 
{
  public Connection connect;

  public MysqlJdbc()
  {
    try
    {
      Class.forName("com.mysql.cj.jdbc.Driver");     //����MYSQL JDBC��������   
      //Class.forName("org.gjt.mm.mysql.Driver");
     System.out.println("Success loading Mysql Driver!");
    }
    catch (Exception e) 
    {
      System.out.print("Error loading Mysql Driver!");
      e.printStackTrace();
    }
    try
    {
      //����URLΪ   jdbc:mysql//��������ַ/���ݿ���  �������2�������ֱ��ǵ�½�û���������
      connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/lazymen?serverTimezone=UTC","root","password");

      System.out.println("Success connect Mysql server!");
    }
    catch (Exception e) 
    {
      System.out.print("fail to connect Mysql server!");
      e.printStackTrace();
    }
  }
}