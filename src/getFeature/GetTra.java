/**
 * 
 */
package getFeature;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import entity.ConnectSql;
import entity.Data;

/**
 * @author ryan
 * 该类用于提取对于180s降频后的数据，抽取有用的数据
 * 从fortraall180表中取数据 存到表feature_speed180
 */
public class GetTra {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
	//获取有用的记录
	public void gettal(){
		String sql="select VehicleID,UtcTime from fortraall180 where flag=1;";
		Data a=new Data();
		try(Connection con=ConnectSql.Connect();
				Statement s=con.createStatement();
				ResultSet rs=s.executeQuery(sql);) {
			while(rs.next()){
				
				a.setVehicleID(rs.getInt("VehicleID"));
				a.setUtcTime(rs.getBigDecimal("UtcTime"));
				insetto(a);
			}
			
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	private void insetto(Data a) {
		// TODO Auto-generated method stub
		String sql="inset into feature_speed180 select * from feature_speed where VehicleID="
				+a.getVehicleID()+" and UtcTime="+a.getUtcTime()+";";
		try(Connection con=ConnectSql.Connect();
				Statement s=con.createStatement();
				ResultSet rs=s.executeQuery(sql);) {
			while(rs.next()){
				
				a.setVehicleID(rs.getInt("VehicleID"));
				a.setUtcTime(rs.getBigDecimal("UtcTime"));
				insetto(a);
			}
			
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}
