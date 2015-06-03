package getFeature;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import Test.ConnectSql;
import entity.Data;
import entity.Distance;

//������ȡ1000�����Ĺ켣���ٶ�
public class CulSpeed2 {

	private int tra=0;   //�ж����ĵ�����һ��������һ��
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("��ʼ��TAR");
		CulSpeed2 cul = new CulSpeed2();
		cul.getvel();
		System.out.println("������TRA");
		System.out.println(new Date());
	}

	public void getvel(){
		ConnectSql n = new ConnectSql();
		String sql = "select distinct VehicleID from feature_speed_VehicleID1  where VehicleID=11201 order by VehicleID;";

		try (Connection con = n.Connect();
				Statement stm = con.createStatement();
				ResultSet rs = stm.executeQuery(sql);) {
			while(rs.next()){
				
				int vel=rs.getInt("VehicleID");
				System.out.println(vel);
				culspeed(vel,con);
				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}
	
	public void culspeed(int vel, Connection con2)  //ĳ��¼���ٶ���ȥ�ü�¼��֮���¼֮����ٶȣ�����֮��û�м�¼
	{
		Distance dis=new Distance(con2);   
		double distance=0;             //����
		double time=0;                 //ʱ��
		double speed=0;                //�ٶ�
		/*ConnectSql n = new ConnectSql();
		Connection con = n.Connect();*/
    
		Statement stm = null;
		ResultSet rs = null;
		Data start = new Data();
		Data end = new Data();
		try {
			stm = con2.createStatement();
			//String sql = "select VehicleID,UtcTime,TemValue,RoadID,PointPos,DigDirect from feature_speed_all1 where Type=1 and VehicleID="+vel+" order by UtcTime";
			String sql="select VehicleID,UtcTime,TemValue,RoadID,PointPos,DigDirect from feature_speed_all1 where Type=1 and VehicleID=11281 and UtcTime=1251840874.000000  order by UtcTime";
			//String sql = "select * from selectedroad202 where VehicleID=20200 and UtcTime=1251893383.000000";
			rs = stm.executeQuery(sql);
			while (rs.next()) {
				
				start.setDigDirect(rs.getBoolean("DigDirect"));
				start.setPointPos(rs.getBigDecimal("PointPos"));
				start.setRoadID(rs.getInt("RoadID"));
				start.setTemValue(rs.getBigDecimal("TemValue").intValue());
				start.setVehicleID(rs.getInt("VehicleID"));
				start.setUtcTime(rs.getBigDecimal("UtcTime"));
				
				if(end.getVehicleID()!=0){
					if(start.getUtcTime().subtract(end.getUtcTime()).doubleValue()<600){  //����¼����
						distance =dis.getdistance(end, start);
						time = Math.abs(end.getUtcTime().doubleValue()
								- start.getUtcTime().doubleValue());
						if (time != 0) {
							speed = distance / time * 3.6; // ��λ km/h
							inserttospeed(end,speed,con2);
						} else
							System.out.println("����֮��ʱ��Ϊ0");
						tra=1;
					}else{
						if(tra==2){
							System.out.println("���ֹ�����");
						}else{
							inserttospeed(end,speed,con2);
						}
						tra=2;
					}
				}
				
				end.setDigDirect(start.getDigDirect());
				end.setPointPos(start.getPointPos());
				end.setRoadID(start.getRoadID());
				end.setTemValue(start.getTemValue());
				end.setVehicleID(start.getVehicleID());
				end.setUtcTime(start.getUtcTime());
			}
			
			//���һ����¼ȡǰһ����¼���ٶ�
			inserttospeed(end,speed,con2);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			if (rs != null)
				rs.close();
			if (stm != null)
				stm.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void inserttospeed(Data start, double speed, Connection con2) {
		// TODO Auto-generated method stub
		/*ConnectSql n = new ConnectSql();
		Connection con = n.Connect();*/

		Statement stm = null;
		
		try {
			stm=con2.createStatement();
			String sql = "update feature_speed_all1 set newspeed= " + (float) speed
					+ " where VehicleID=" + start.getVehicleID()
					+ " and UtcTime=" + start.getUtcTime() + ";";
			int flag=stm.executeUpdate(sql);
			if(flag==0)
				System.out.println("����ʧ�� "+sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
	
			if (stm != null)
				stm.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
}
