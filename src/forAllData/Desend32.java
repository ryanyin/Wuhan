package forAllData;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import entity.ConnectWuhan;
import entity.Data;


public class Desend32 {
	private static Connection con=ConnectWuhan.Connect();
	private int count=0;
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("��ʼ����·���Ľ�Ƶ");
		System.out.println(new Date());
		Desend32 des=new Desend32();
		des.cul();
		System.out.println(new Date());
		try {
			if(!con.isClosed())
				con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void cul(){
		for(int i=2;i<=2;i++){
			switch (i) {
			case 1:
				System.out.println("��ʼ60�뽵Ƶ");
				count=0;
				getvel(60,"MatchedTraj20090901for60");
				System.out.println(new Date());
				break;
			case 2:
				System.out.println("��ʼ90�뽵Ƶ");
				count=0;
				getvel(90,"MatchedTraj20090901for90");
				System.out.println(new Date());
				break;
			case 3:
				System.out.println("��ʼ120�뽵Ƶ");
				count=0;
				getvel(120,"MatchedTraj20090901for120");
				System.out.println(new Date());
				break;
			case 4:
				System.out.println("��ʼ150�뽵Ƶ");
				count=0;
				getvel(150,"MatchedTraj20090901for150");
				System.out.println(new Date());
				break;
			case 5:
				System.out.println("��ʼ180�뽵Ƶ");
				count=0;
				//getvel(180,"fortraall180");
				getvel(180,"MatchedTraj20090901for180");
				System.out.println(new Date());
				break;
			default:
				break;
			}
		}
	}
	public void getvel(int time,String tablename){
		
		//String sql = "select distinct VehicleID from speed2 where VehicleID<16493 order by VehicleID;";
		String sql = "select distinct VehicleID from "+tablename+"  order by VehicleID;";

		try (
				Statement stm = con.createStatement();
				ResultSet rs = stm.executeQuery(sql);) {
			while(rs.next()){
				count++;
				int vel=rs.getInt("VehicleID");
				desend(vel,time,tablename);
				System.out.println("��ǰ�����ݱ�   "+tablename+"  ��ǰ�����¼   "+count+"  ��ǰ����  "+vel);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}
	public void desend(int vel,int time, String tablename) {
		// TODO Auto-generated method stub
				 
		String sql = "select VehicleID,UtcTime,TemValue from "+tablename+" where VehicleID="+vel+ " order by UtcTime; ";
		Data first=new Data();
		BigDecimal t1=new BigDecimal(-1);
		try (
				Statement stm=con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
				ResultSet rs=stm.executeQuery(sql)){
			while(rs.next()){
				first.setVehicleID(rs.getInt("VehicleID"));
				first.setUtcTime(rs.getBigDecimal("UtcTime"));
				first.setTemValue(rs.getBigDecimal("TemValue").intValue());
								
				if(t1.doubleValue()>0){
					if(first.getUtcTime().subtract(t1).doubleValue()<time){						
							//delete(first,tablename);	
						update(first,tablename);
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
	private void update(Data first, String tablename) {
		// TODO Auto-generated method stub
				 
		String sql = "update "+tablename+" set flag=-1 where VehicleID="+first.getVehicleID()
				+" and UtcTime="+first.getUtcTime()+";";
		try (
				Statement stm=con.createStatement()){
			
			int flag=stm.executeUpdate(sql);
			if(flag==0){
				System.out.println(sql);
				System.out.println("���³��ִ���");
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
