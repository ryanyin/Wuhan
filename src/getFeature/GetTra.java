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
 * ����������ȡ����180s��Ƶ������ݣ���ȡ���õ�����
 * ��fortraall180����ȡ���� �浽��feature_speed180
 */
public class GetTra {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
	//��ȡ���õļ�¼
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
