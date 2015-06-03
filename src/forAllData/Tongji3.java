package forAllData;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import entity.ConnectSql;
import entity.ConnectWuhan;
import entity.Data;



//分时段计算--24个时段计算丢失率
public class Tongji3 {

	private static Connection con=ConnectWuhan.Connect();
	private int count01=0;
	private int count10=0;
	private int sum=0;
	private int temp_vel=0;
	private int temp_count=0;
	 
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println(new Date());
		double time1=1251763200.000000;
		Tongji3 tong=new Tongji3();
		int sum2=0;
		for(int i =1;i<13;i++){
			tong.getvel(time1);
			System.out.println("----"+i+"    "+time1);
			System.out.println("count01  "+tong.count01);
			System.out.println("count10  "+tong.count10);
			System.out.println("sum  "+tong.sum);
			System.out.println(new Date());
			time1+=7200;
			sum2+=tong.sum;
			tong.count01=0;
			tong.count10=0;
			tong.sum=0;
			tong.temp_count=0;
			System.out.println(new Date());
		}
		System.out.println(sum2);
		
		try {
			if(!con.isClosed())
				con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public void getvel(double time1){
		
		double time2=time1+7200;
		String sql = "select distinct VehicleID from MatchedTraj20090901for180  order by VehicleID;";

		try (
				Statement stm = con.createStatement();
				ResultSet rs = stm.executeQuery(sql);) {
			while(rs.next()){
				int vel=rs.getInt("VehicleID");
				System.out.println("VehicleID "+vel);
				tongji(vel,time1);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}
	private void tongji(int vel,double time1) {
		// TODO Auto-generated method stub
		
		double time2=time1+7200;
		String sql = "select VehicleID,UtcTime,TemValue from MatchedTraj20090901for180 where  VehicleID="
				+ vel
				+ " and UtcTime>="
				+ time1
				+ " and UtcTime<"
				+ time2
				+ " order by UtcTime; ";
		Data first=new Data();
		Data second=new Data();
		try (
				Statement stm=con.createStatement();
				ResultSet rs=stm.executeQuery(sql)){
			while(rs.next()){
				first.setVehicleID(rs.getInt("VehicleID"));
				first.setUtcTime(rs.getBigDecimal("UtcTime"));
				first.setTemValue(rs.getBigDecimal("TemValue").intValue());
				
				if(temp_vel==0){
					temp_vel=first.getVehicleID();
				}
				if(temp_vel>0&&temp_vel!=first.getVehicleID()){
					//update(temp_vel,temp_count);
					temp_vel=first.getVehicleID();
					temp_count=0;
					
				}
				
				if(second.getVehicleID()!=0){
					if(first.getUtcTime().subtract(second.getUtcTime()).doubleValue()<=600){
						if(first.getTemValue()==1&&second.getTemValue()==0){
							count01++;
							sum++;
							temp_count++;
						}
						if(first.getTemValue()==0&&second.getTemValue()==1){
							count10++;
							sum++;
							temp_count++;
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
	}

}
