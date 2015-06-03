import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;


public class GetRoadSpeed {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println(new Date());
		GetRoadSpeed g=new GetRoadSpeed();
		g.get();
		System.out.println(new Date());
	}
	
	public void get(){
		ConnectSql n = new ConnectSql();		 
		String sql="select VehicleID,UtcTime from forslowbackup; ";
		Data a=new Data();
		try (Connection con = n.Connect();
				Statement stm=con.createStatement();
				ResultSet rs=stm.executeQuery(sql)){
			while(rs.next()){
				a.setVehicleID(rs.getInt("VehicleID"));
				a.setUtcTime(rs.getBigDecimal("UtcTime"));
				float roadspeed=getRoadSpeed(a);
				insert(roadspeed, a);
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public float getRoadSpeed(Data a){
		float speed=0;
		ConnectSql n = new ConnectSql();
		Connection con = n.Connect();

		Statement stm = null;
		ResultSet rs = null;
		try {
			stm=con.createStatement();
			String sql="select Speed from SpeedRoad207 where EndTime>="+a.getUtcTime()+" and EndTime<"+a.getUtcTime().add(new BigDecimal(300))+" ;";
			
			//System.out.println(sql);
			rs=stm.executeQuery(sql);
			while(rs.next()){
				speed=rs.getFloat("Speed");
				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			if (rs != null)
				rs.close();
			if (stm != null)
				stm.close();
			if (con != null)
				con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return speed;
		
	}
	
	public void insert(float s,Data a){
		ConnectSql n = new ConnectSql();
		String sql = "update forslow207 set roadspeed=" + s + " where VehicleID="
				+ a.getVehicleID() + " and UtcTime=" + a.getUtcTime() + " ;";
		//System.out.println(sql);
		try (Connection con = n.Connect();
				Statement stm=con.createStatement();
				){
			int flag=stm.executeUpdate(sql);
			if(flag==0){
				System.out.println("³öÏÖ´íÎó");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
