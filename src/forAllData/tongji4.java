package forAllData;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import entity.ConnectWuhan;
import entity.Data;


//统计时间间隔
public class tongji4 {
	private static Connection con=ConnectWuhan.Connect();
	public static void main(String[] args) {
		System.out.println(new Date());
		double time1=1251763200.000000;
		for(int i =1;i<25;i++){
			double time=0;
			int count=0;
			String sql = "select VehicleID,UtcTime,TemValue from MatchedTraj20090901for180 where UtcTime>= "+time1+" and UtcTime<"+(time1+3600)+" order by VehicleID,UtcTime; ";
			System.out.println(sql);
			Data first=new Data();
			Data second=new Data();
			try (
					Statement stm = con.createStatement();
					ResultSet rs = stm.executeQuery(sql);) {
				while(rs.next()){
					first.setVehicleID(rs.getInt("VehicleID"));
					first.setUtcTime(rs.getBigDecimal("UtcTime"));
					first.setTemValue(rs.getBigDecimal("TemValue").intValue());
					
					if(first.getVehicleID()!=second.getVehicleID()&&first.getTemValue()==0) count++;
					
					if(second.getVehicleID()!=0&&second.getVehicleID()==first.getVehicleID()){
						if(first.getTemValue()==0||second.getTemValue()==0){
							if(first.getUtcTime().subtract(second.getUtcTime()).doubleValue()<=600){
								time+=(first.getUtcTime().subtract(second.getUtcTime())).doubleValue()/60;
								if(first.getTemValue()==0&&second.getTemValue()==1) count++;
							}else{
								if(!(first.getTemValue()==1&&second.getTemValue()==0))count++;
							}
						}
						
						
					}
					second.setVehicleID(first.getVehicleID());
					second.setUtcTime(first.getUtcTime());
					second.setTemValue(first.getTemValue());
				}
				System.out.println("第"+i+"   次数："+count+"  时间："+time+"  平均："+(time/count));
				time1+=3600;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println(new Date());
		}
		
	}

}
