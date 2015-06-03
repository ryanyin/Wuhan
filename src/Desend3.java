import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;


public class Desend3 {

	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("��ʼ����·���Ľ�Ƶ");
		System.out.println(new Date());
		Desend3 des=new Desend3();
		des.cul();
		System.out.println(new Date());
	}
	public void cul(){
		for(int i=1;i<=5;i++){
			switch (i) {
			case 1:
				System.out.println("��ʼ60�뽵Ƶ");
				getvel(60,"fortraall60");
				System.out.println(new Date());
				break;
			case 2:
				System.out.println("��ʼ80�뽵Ƶ");
				getvel(80,"fortraall80");
				System.out.println(new Date());
				break;
			case 3:
				System.out.println("��ʼ100�뽵Ƶ");
				getvel(100,"fortraall100");
				System.out.println(new Date());
				break;
			case 4:
				System.out.println("��ʼ120�뽵Ƶ");
				getvel(120,"fortraall120");
				System.out.println(new Date());
				break;
			case 5:
				System.out.println("��ʼ150�뽵Ƶ");
				getvel(150,"fortraall150");
				System.out.println(new Date());
				break;
			default:
				break;
			}
		}
	}
	public void getvel(int time,String tablename){
		ConnectSql n = new ConnectSql();
		String sql = "select distinct VehicleID from speed2 order by VehicleID;";

		try (Connection con = n.Connect();
				Statement stm = con.createStatement();
				ResultSet rs = stm.executeQuery(sql);) {
			while(rs.next()){
				int vel=rs.getInt("VehicleID");
				desend(vel,time,tablename);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}
	public void desend(int vel,int time, String tablename) {
		// TODO Auto-generated method stub
		ConnectSql n = new ConnectSql();		 
		String sql = "select VehicleID,UtcTime,TemValue from fortraall where VehicleID="+vel+ " order by UtcTime; ";
		Data first=new Data();
		BigDecimal t1=new BigDecimal(-1);
		try (Connection con = n.Connect();
				Statement stm=con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
				ResultSet rs=stm.executeQuery(sql)){
			while(rs.next()){
				first.setVehicleID(rs.getInt("VehicleID"));
				first.setUtcTime(rs.getBigDecimal("UtcTime"));
				first.setTemValue(rs.getBigDecimal("TemValue").intValue());
								
				if(t1.doubleValue()>0){
					if(first.getUtcTime().subtract(t1).doubleValue()<time){						
							delete(first,tablename);											
					}else{
						t1=first.getUtcTime();
					}
				}
				
				if(t1.doubleValue()<0){
					t1=first.getUtcTime();
				}
				
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void delete(Data first, String tablename) {
		// TODO Auto-generated method stub
		ConnectSql n = new ConnectSql();		 
		String sql = "delete from "+tablename+" where VehicleID="+first.getVehicleID()
				+" and UtcTime="+first.getUtcTime()+";";
		try (Connection con = n.Connect();
				Statement stm=con.createStatement()){
			
			int flag=stm.executeUpdate(sql);
			if(flag==0){
				System.out.println(sql);
				System.out.println("ɾ�����ִ���");
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
