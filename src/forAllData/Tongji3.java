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

	 
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println(new Date());
		double time1=1251763200.000000;
		Tongji3 tong=new Tongji3();
		int sum2=0;
		for(int i =1;i<25;i++){
			if(i==23) tong.getvel(time1);
			System.out.println("----"+i+"    "+time1);
			System.out.println("count01  "+tong.count01);
			System.out.println("count10  "+tong.count10);
			System.out.println("sum  "+tong.sum);
			System.out.println(new Date());
			time1+=3600;
			sum2+=tong.sum;
			tong.count01=0;
			tong.count10=0;
			tong.sum=0;
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
		
		double time2=time1+3600;
		String sql = "select VehicleID,UtcTime,TemValue from MatchedTraj20090901for180 where flag=1 and  UtcTime>="
				+ time1
				+ " and UtcTime<"
				+ time2
				+ " order by VehicleID,UtcTime; ";
		System.out.println(sql);
		Data first=new Data();
		Data second=new Data();
		int temp01=0;
		int temp10=0;
		try (
				Statement stm = con.createStatement();
				ResultSet rs = stm.executeQuery(sql);) {
			while(rs.next()){
				first.setVehicleID(rs.getInt("VehicleID"));
				first.setUtcTime(rs.getBigDecimal("UtcTime"));
				first.setTemValue(rs.getBigDecimal("TemValue").intValue());
				
				if(second.getVehicleID()!=first.getVehicleID()){				
					System.out.println("count01 "+temp01+"   count10 "+temp10+ " Car "+second.getVehicleID());
					update(temp01+temp10, second.getVehicleID());
					count01+=temp01;
					count10+=temp10;
					temp01=0;
					temp10=0;
					second.setVehicleID(0);
				}
				if(second.getVehicleID()!=0){
					if(first.getUtcTime().subtract(second.getUtcTime()).doubleValue()<=600){
						if(first.getTemValue()==1&&second.getTemValue()==0){
							temp01++;
							sum++;
						}
						if(first.getTemValue()==0&&second.getTemValue()==1){
							temp10++;
							sum++;
						}
					}
					
				}
				second.setVehicleID(first.getVehicleID());
				second.setUtcTime(first.getUtcTime());
				second.setTemValue(first.getTemValue());
			}
			count01+=temp01;
			count10+=temp10;
			update(temp01+temp10, second.getVehicleID());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}
	private void update( int count,int carId) {
		// TODO Auto-generated method stub
		 
		String sql = "update LostRate set  for22after="+count+" where VehicleID="+carId+";";
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
