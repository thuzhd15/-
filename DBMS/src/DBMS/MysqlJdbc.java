package DBMS;
import java.sql.*;
public class MysqlJdbc 
{
  public Connection connect;

  public MysqlJdbc()
  {
    try
    {
      Class.forName("com.mysql.cj.jdbc.Driver");     //加载MYSQL JDBC驱动程序   
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
      //连接URL为   jdbc:mysql//服务器地址/数据库名  ，后面的2个参数分别是登陆用户名和密码
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