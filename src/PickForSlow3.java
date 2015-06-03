import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;


public class PickForSlow3 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println(new Date());
		PickForSlow3 pic=new PickForSlow3();
		pic.getvel();
		System.out.println(new Date());
	}

	public void getvel(){
		ConnectSql n = new ConnectSql();
		String sql = "select distinct VehicleID from speed2 order by VehicleID;";

		try (Connection con = n.Connect();
				Statement stm = con.createStatement();
				ResultSet rs = stm.executeQuery(sql);) {
			while(rs.next()){
				int vel=rs.getInt("VehicleID");
				insert(vel);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}

	private void insert(int vel) {
		// TODO Auto-generated method stub
		ConnectSql n = new ConnectSql();
		Connection con = n.Connect();

		Statement stm = null;
		
		try {
			stm = con.createStatement();
			String sql =  "insert into fortraall (VehicleID,UtcTime,TemValue,RoadID,mmX,mmY,SysTime) select VehicleID,UtcTime,TemValue,RoadID,mmX,mmY,SysTime from "
					+ "MatchedTraj20090901 where Type=1 and VehicleID="+vel+" order by UtcTime;";
			//System.out.println(sql);
			int flag=stm.executeUpdate(sql);
			if(flag==0){
				System.out.println("≤Â»Î¥ÌŒÛ≥ˆœ÷£°£°£°");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			if (stm != null)
				stm.close();
			if (con != null)
				con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
