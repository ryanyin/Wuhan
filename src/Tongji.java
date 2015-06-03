import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;


public class Tongji {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println(new Date());
		Tongji tong=new Tongji();
		tong.forcoun();
		System.out.println("count01  "+tong.count01);
		System.out.println("count10  "+tong.count10);
		System.out.println("sum  "+tong.sum);
		System.out.println(new Date());
	}

	private int count01=0;
	private int count10=0;
	private int sum=0;
	private int temp_vel=0;
	private int temp_count=0;
	public void forcoun(){
		int count=1;
		while(count<=5939){
			getcount(count);
			if(count==5939){   //最后一条轨迹
				update(temp_vel,temp_count);
				System.out.println(temp_vel+"   "+temp_count);
			}
			count++;		
		}
	}
	
	public void getcount(int count){
		ConnectSql n = new ConnectSql();		 
		String sql = "select VehicleID,UtcTime,TemValue from fortra201180 where count="
				+ count + " order by UtcTime; ";
		Data first=new Data();
		Data second=new Data();
		try (Connection con = n.Connect();
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
					update(temp_vel,temp_count);
					temp_vel=first.getVehicleID();
					temp_count=0;
					
				}
				
				
				if(second.getVehicleID()!=0){
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
				second.setVehicleID(first.getVehicleID());
				second.setUtcTime(first.getUtcTime());
				second.setTemValue(first.getTemValue());
			}

			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void update(int temp_vel2, int temp_count2) {
		// TODO Auto-generated method stub
		ConnectSql n = new ConnectSql();		 
		String sql = "update fortra201desend set for180="+temp_count2+" where VehicleID="+temp_vel2+";";
		try (Connection con = n.Connect();
				Statement stm=con.createStatement()){
			
			int flag=stm.executeUpdate(sql);
			if(flag==0){
				System.out.println(sql);
				System.out.println("更新出现错误");
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
