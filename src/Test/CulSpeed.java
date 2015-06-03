package Test;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;


public class CulSpeed {

	private int tra=0;   //�ж����ĵ�����һ��������һ��
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("��ʼ��TAR");
		CulSpeed cul = new CulSpeed();
		cul.getvel();
		System.out.println("������TRA");
		System.out.println(new Date());
	}

	public void getvel(){
		ConnectSql n = new ConnectSql();
		String sql = "select distinct VehicleID from temp  order by VehicleID;";

		try (Connection con = n.Connect();
				Statement stm = con.createStatement();
				ResultSet rs = stm.executeQuery(sql);) {
			while(rs.next()){
				
				int vel=rs.getInt("VehicleID");
				System.out.println(vel);
				culspeed(vel);
				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}
	
	public void culspeed(int vel)  //ĳ��¼���ٶ���ȥ�ü�¼��֮���¼֮����ٶȣ�����֮��û�м�¼
	{
		Distance dis=new Distance();   
		double distance=0;             //����
		double time=0;                 //ʱ��
		double speed=0;                //�ٶ�
		ConnectSql n = new ConnectSql();
		Connection con = n.Connect();
    
		Statement stm = null;
		ResultSet rs = null;
		try {
			stm = con.createStatement();
			String sql = "select VehicleID,UtcTime,TemValue,RoadID,PointPos,DigDirect from temp where Type=1 and VehicleID="+vel+" order by UtcTime";
			//String sql = "select * from selectedroad202 where VehicleID=20200 and UtcTime=1251893383.000000";
			rs = stm.executeQuery(sql);
			while (rs.next()) {
				Data start = new Data();
				start.setDigDirect(rs.getBoolean("DigDirect"));
				start.setPointPos(rs.getBigDecimal("PointPos"));
				start.setRoadID(rs.getInt("RoadID"));
				start.setTemValue(rs.getBigDecimal("TemValue").intValue());
				start.setVehicleID(rs.getInt("VehicleID"));
				start.setUtcTime(rs.getBigDecimal("UtcTime"));
				
				Data end = getnext(start);
				if (end.getVehicleID()!= 0) {
					if(tra==1){
						//distance = dis.culdistance(start, end);
						distance =dis.getdistance(start, end);
					}
					if(tra==2){
						//distance = dis.culdistance(end, start);
						distance =dis.getdistance(end, start);
					}
					if(tra==3){
						System.out.println("���������ִ�����");
					}
					time = Math.abs(end.getUtcTime().doubleValue()
							- start.getUtcTime().doubleValue());
					if (time != 0) {
						speed = distance / time * 3.6; // ��λ km/h
						//System.out.println("VehicleID "+start.getVehicleID()+"  ����"+distance+"  "+time+"  "+speed+"  "+tra);
						inserttospeed(start,speed);
					} else
						System.out.println("����֮��ʱ��Ϊ0");
				} else
					System.out.println("��һ��¼�ǹ�����");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void inserttospeed(Data start, double speed) {
		// TODO Auto-generated method stub
		ConnectSql n = new ConnectSql();
		Connection con = n.Connect();

		Statement stm = null;
		
		try {
			stm=con.createStatement();
			String sql = "update selectedall set newspeed= " + (float) speed
					+ " where VehicleID=" + start.getVehicleID()
					+ " and UtcTime=" + start.getUtcTime() + ";";
			int flag=stm.executeUpdate(sql);
			if(flag==0)
				System.out.println("����ʧ�� "+sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public Data getnext(Data start) {
		
		BigDecimal gap=new BigDecimal(600);
		Data end=new Data();
		ConnectSql n = new ConnectSql();
		Connection con = n.Connect();

		Statement stm = null;
		ResultSet rs = null;
		Statement stm2 = null;
		ResultSet rs2 = null;
		try {
			stm = con.createStatement();
			/*
			 * System.out.println(start.getUtcTime()); System.out.println(gap);
			 * System.out.println(start.getUtcTime().add(gap));
			 */
			String sql = "select TOP 1 * from MatchedTraj20090901  where   VehicleID="
					+ start.getVehicleID()
					+ " and UtcTime>"
					+ start.getUtcTime()
					+ " and UtcTime<"
					+ start.getUtcTime().add(gap) + " order by UtcTime;";
			rs = stm.executeQuery(sql);
			if (rs.next()) // �����˼�¼
			{
				end.setDigDirect(rs.getBoolean("DigDirect"));
				end.setPointPos(rs.getBigDecimal("PointPos"));
				end.setRoadID(rs.getInt("RoadID"));
				end.setTemValue(rs.getBigDecimal("TemValue").intValue());
				end.setVehicleID(rs.getInt("VehicleID"));
				end.setUtcTime(rs.getBigDecimal("UtcTime"));
				//System.out.println(end.getUtcTime());
				tra=1;    //Ϊ1��ʾ��һ����¼����
			} else { // û�м�¼��ѡȡ���ϵ�һ����¼
				stm2 = con.createStatement();
				String sql2 = "select TOP 1 * from MatchedTraj20090901  where    VehicleID="
						+ start.getVehicleID()
						+ " and UtcTime<"
						+ start.getUtcTime()
						+ " and UtcTime>"
						+ start.getUtcTime().subtract(gap)
						+ " order by UtcTime DESC ;";
				rs2 = stm2.executeQuery(sql2);
				if (rs2.next()) {
					end.setDigDirect(rs2.getBoolean("DigDirect"));
					end.setPointPos(rs2.getBigDecimal("PointPos"));
					end.setRoadID(rs2.getInt("RoadID"));
					end.setTemValue(rs2.getBigDecimal("TemValue").intValue());
					end.setVehicleID(rs2.getInt("VehicleID"));
					end.setUtcTime(rs2.getBigDecimal("UtcTime"));
					tra=2;   //Ϊ2��ʾ��һ����¼Ϊ��һ��
				} else {
					System.out.println("���ֹ����㣬DataΪ��" + tra + "  ����Ϊ "
							+ start.getVehicleID() + "  ʱ��Ϊ  "
							+ start.getUtcTime());
					tra=3;   //Ϊ0��ʾû�м�¼
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return end;

	}
}
