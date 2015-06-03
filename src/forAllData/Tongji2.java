package forAllData;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;


import entity.ConnectWuhan;
import entity.Data;

//计算所有的。。整体的丢失率
public class Tongji2 {
	

	private static Connection con=ConnectWuhan.Connect();

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println(new Date());
		Tongji2 tong=new Tongji2();
		tong.getvel();
//		tong.tongji(25044);
		System.out.println(new Date());
		try {
			if(!con.isClosed())
				con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void getvel(){
		
		String sql = "select distinct VehicleID from MatchedTraj20090901for180   order by VehicleID;";

		try (
				Statement stm = con.createStatement();
				ResultSet rs = stm.executeQuery(sql);) {
			while(rs.next()){
				int vel=rs.getInt("VehicleID");
//				System.out.println("VehicleID "+vel);
				tongji(vel);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}
	private void tongji(int vel) {
		// TODO Auto-generated method stub
		int count01=0;
		int count10=0;
		String sql = "select VehicleID,UtcTime,TemValue from MatchedTraj20090901for180 where  flag=1 and  VehicleID="+vel+ "  order by UtcTime; ";
		Data first=new Data();
		Data second=new Data();
		try (
				Statement stm=con.createStatement();
				ResultSet rs=stm.executeQuery(sql)){
			while(rs.next()){
				first.setVehicleID(rs.getInt("VehicleID"));
				first.setUtcTime(rs.getBigDecimal("UtcTime"));
				first.setTemValue(rs.getBigDecimal("TemValue").intValue());
				
				if(second.getVehicleID()!=0){
					if(first.getUtcTime().subtract(second.getUtcTime()).doubleValue()<=600){
						if(first.getTemValue()==1&&second.getTemValue()==0){
							count01++;				
						}
						if(first.getTemValue()==0&&second.getTemValue()==1){
							count10++;
						}
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
		
		//做同步
		update(count01, count10, vel);
		System.out.println("count01 "+count01+"   count10 "+count10+ " Car "+vel);
		
		
	}
	private void update(int count01, int count10,int carId) {
		// TODO Auto-generated method stub
		 
		String sql = "update LostRate set after01="+count01+", after10="+count10+" where VehicleID="+carId+";";
		try (
				Statement stm=con.createStatement()){
			
			int flag=stm.executeUpdate(sql);
			if(flag==0){
				System.out.println(sql);
				System.out.println("更新出现错误");
				System.err.println("出现错误");
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
