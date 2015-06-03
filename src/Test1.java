import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class Test1 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String b="";
       for(int a=1;a<8;a++)
       {
    	   switch (a) {
		case 1:
			b="MatchedTraj20090901";
			break;
		case 2:
			b="MatchedTraj20090902";
			break;
		case 3:
			b="MatchedTraj20090903";
			break;
		case 4:
			b="MatchedTraj20090904";
			break;
		case 5:
			b="MatchedTraj20090905";
			break;
		case 6:
			b="MatchedTraj20090906";
			break;
		case 7:
			b="MatchedTraj20090907";
			break;
		default:
			break;
		}
    	   Test1 t=new Test1();
    	   t.cul(b);
       }
	}

	public void cul(String m){
		Statement stmt = null;   
		Statement stmt2 = null;
        ResultSet rs = null; 
        ResultSet rs2 = null;
		ConnectSql c=new ConnectSql();
		Connection con=c.Connect();
		String SQL ="select b.VehicleID from (select a.VehicleID, count (a.VehicleID) as number from(select VehicleID,TemValue from "+m+"  GROUP BY VehicleID ,TemValue) as a group by a.VehicleID) as b where b.number=1;";
		try {
			stmt = con.createStatement();
			rs = stmt.executeQuery(SQL); 
			int sum=0;
			 
			while(rs.next()){
				int a=rs.getInt(1);
				String s="select count(*) as number from "+m+" where VehicleID="+a+";";
				stmt2 = con.createStatement();
				rs2=stmt2.executeQuery(s);
			    while(rs2.next())
			    {
			    	sum+=rs2.getInt(1);
			    }
			}
			System.out.println(m+"的轨迹数目为： "+sum);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
		finally {
			if (rs2 != null)    
                try {    
                    rs2.close();    
                } catch (Exception e) {    
                }    
            if (stmt2 != null)    
                try {    
                    stmt2.close();    
                } catch (Exception e) {    
                } 
            if (rs != null)    
                try {    
                    rs.close();    
                } catch (Exception e) {    
                }    
            if (stmt != null)    
                try {    
                    stmt.close();    
                } catch (Exception e) {    
                }    
            if (con != null)    
                try {    
                    con.close();    
                } catch (Exception e) {    
                }    
        }    
        
	}
}
