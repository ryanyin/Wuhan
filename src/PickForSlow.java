import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;


public class PickForSlow {

	private int count=0;
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println(new Date());
		PickForSlow p=new PickForSlow();
		p.pickvel();
		System.out.println("记录数目为： "+p.count);
		System.out.println(new Date());
	}

	
	public void pickvel(){
		ConnectSql n = new ConnectSql();
		Connection con = n.Connect();

		Statement stm = null;
		ResultSet rs = null;
		
		try {
			stm = con.createStatement();
			String sql="select count(VehicleID) as number,VehicleID  from fortwo207 group by VehicleID  having count(VehicleID)>2 order by count(VehicleID),VehicleID;";
			rs=stm.executeQuery(sql);
			while(rs.next()){
				int vel=rs.getInt("VehicleID");
				pick(vel);
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
	}
	
	public void pick(int a){
		ConnectSql n = new ConnectSql();
		Connection con = n.Connect();

		Statement stm = null;
		ResultSet rs = null;
		Data first=new Data();
		Data second=new Data();
		Data tra=new Data();   //记录
		try {
			stm = con.createStatement();
			String sql="select * from speed207 where VehicleID="+a+"  order by UtcTime;";
			rs=stm.executeQuery(sql);
			while(rs.next()){
				first.setVehicleID(rs.getInt("VehicleID"));
				first.setUtcTime(rs.getBigDecimal("UtcTime"));
				first.setTemValue(rs.getBigDecimal("TemValue").intValue());
				if(second.getVehicleID()!=0){
					if(first.getUtcTime().subtract(second.getUtcTime()).doubleValue()<300)
					{
						if(second.getTemValue()==1&&first.getTemValue()==0){
							tra.setVehicleID(second.getVehicleID());
							tra.setUtcTime(second.getUtcTime());
							tra.setTemValue(second.getTemValue());
						}
						if(second.getTemValue()==0&&first.getTemValue()==1){
							if(tra.getVehicleID()!=0){
								
								count+=1;
								//做插入
								insert(tra, first);
								tra.setVehicleID(0);
							}
						}
					}else{
						second.setVehicleID(0);
						tra.setVehicleID(0);
					}
				}
				second.setVehicleID(first.getVehicleID());
				second.setUtcTime(first.getUtcTime());
				second.setTemValue(first.getTemValue());
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	public void insert(Data a,Data b){
		ConnectSql n = new ConnectSql();
		Connection con = n.Connect();

		Statement stm = null;
		Statement stm2 = null;
		try {
			stm = con.createStatement();
			String sql = "insert into forslow207 (VehicleID,UtcTime,Speed,TemValue,RoadID,mmX,mmY,PointPos,DigDirect,"
					+ "SysTime,newspeed) select VehicleID,UtcTime,Speed,TemValue,RoadID,mmX,mmY,PointPos,DigDirect,SysTime,newspeed from "
					+ "speed207 where VehicleID="
					+ a.getVehicleID()
					+ " and UtcTime>="
					+ a.getUtcTime()
					+ " and UtcTime<="
					+ b.getUtcTime() + " ;";
			int flag=stm.executeUpdate(sql);
			if(flag==0){
				System.out.println("插入错误出现！！！");
			}else{
				
				stm2 = con.createStatement();
				String sql2 = "update forslow207 set Id=" + count
						+ " where VehicleID=" + a.getVehicleID()
						+ " and UtcTime>=" + a.getUtcTime() + " and UtcTime<="
						+ b.getUtcTime() + " ;";
				int flag2=stm2.executeUpdate(sql2);
				if(flag2==0){
					System.out.println("更新错误出现！！！");
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			if (stm2 != null)
				stm2.close();
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
