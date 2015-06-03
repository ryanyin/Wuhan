package entity;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class ConnectSql {

	public static Connection Connect(){
		String driverName = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
		 
		 String url = "jdbc:sqlserver://localhost:1433;databaseName=Wuhan_FCD_MM_Week;user=sa;password=123";
		 Connection dbConn = null;  
	        try {  
	  
	            Class.forName(driverName).newInstance();  
	        } catch (Exception ex) {  
	            System.out.println("驱动加载失败");  
	            ex.printStackTrace();  
	        }  
	        
	        try {  
	            dbConn = DriverManager.getConnection(url);  
	            //System.out.println("成功连接数据库！");  
	        } catch (Exception e) {  
	            e.printStackTrace();  
	        } /*finally {  
	  
	            try {  
	                if (dbConn != null)  
	                    dbConn.close();  
	            } catch (SQLException e) {  
	                // TODO Auto-generated catch block  
	                e.printStackTrace();  
	            }  
	        }  */
	        return dbConn;
	}
	
	public void closeConnection(Connection a){
		 try {  
             if (a != null)  
                 a.close();  
         } catch (SQLException e) {  
             // TODO Auto-generated catch block  
             e.printStackTrace();  
         }  
	}
	
}
