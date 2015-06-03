import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;


public class PickForSlow2 {
    int count=0;  //记录轨迹数目
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println(new Date());
		PickForSlow2 pic=new PickForSlow2();
		pic.getvel();
		//pic.fortra(12694);
		System.out.println(new Date());
	}

	
	public void getvel(){
		ConnectSql n = new ConnectSql();
		String sql="select distinct VehicleID from speed2 order by VehicleID;";
		Connection con = n.Connect();
		Statement stm=null;
		ResultSet rs=null;
		try {
			 stm=con.createStatement();
			 rs=stm.executeQuery(sql);
			while(rs.next()){
				int vel=rs.getInt("VehicleID");
				//
				System.out.println("VehicleID  "+vel);
				fortra(vel);
				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			if (rs != null)
				rs.close();
			if (stm != null)
				stm.close();
			if (con != null)
				con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void fortra(int val){
		ConnectSql n = new ConnectSql();
		String sql="select VehicleID,UtcTime,TemValue from speed2 where VehicleID="+val+" order by UtcTime;";
		Data start =new Data();
		Data end=new Data();
		Data x=new Data();
		Data y=new Data();
		Connection con = n.Connect();
		Statement stm=null;
		ResultSet rs=null;
		try {
			 stm=con.createStatement();
			 rs=stm.executeQuery(sql);
			while(rs.next()){
				x.setVehicleID(rs.getInt("VehicleID"));
				x.setUtcTime(rs.getBigDecimal("UtcTime"));
				x.setTemValue(rs.getBigDecimal("TemValue").intValue());
				if(start.getVehicleID()==0){
					if(x.getTemValue()==0){
						start=getnext(0, x);
					}else{
						start.setVehicleID(x.getVehicleID());
						start.setUtcTime(x.getUtcTime());
						start.setTemValue(x.getTemValue());
					}
					
				}
				
				if(y.getVehicleID()!=0){
					if(x.getUtcTime().subtract(y.getUtcTime()).doubleValue()>600){
						if(y.getTemValue()==0){
							end=getnext(1, y);
						}else{
							end.setUtcTime(y.getUtcTime());
							end.setVehicleID(y.getVehicleID());
							end.setTemValue(y.getTemValue());
						}
						count+=1;
						//System.out.println(start.getVehicleID()+"   "+end.getVehicleID());
						insertinto(start,end,count);  //出现一段轨迹
						
						if(x.getTemValue()==0){       //下一段轨迹开始
							start=getnext(0, x);
						}else{
							start.setVehicleID(x.getVehicleID());
							start.setUtcTime(x.getUtcTime());
							start.setTemValue(x.getTemValue());
						}
					}
				}
				y.setVehicleID(x.getVehicleID());
				y.setUtcTime(x.getUtcTime());
				y.setTemValue(x.getTemValue());
			}
			if(y.getTemValue()==0){
				end=getnext(1, y);
			}else{
				end.setUtcTime(y.getUtcTime());
				end.setVehicleID(y.getVehicleID());
				end.setTemValue(y.getTemValue());
			}
			count+=1;
			//System.out.println(start.getVehicleID()+"   "+end.getVehicleID());
			insertinto(start,end,count);  //最后一次
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			if (rs != null)
				rs.close();
			if (stm != null)
				stm.close();
			if (con != null)
				con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void insertinto(Data start, Data end, int count) {
		// TODO Auto-generated method stub
		ConnectSql n = new ConnectSql();
		Connection con = n.Connect();

		Statement stm = null;
		Statement stm2 = null;
		try {
			stm = con.createStatement();
			String sql = "insert into fortra201backup3 (VehicleID,UtcTime,TemValue,RoadID,mmX,mmY,SysTime) select VehicleID,UtcTime,TemValue,RoadID,mmX,mmY,SysTime from "
					+ "MatchedTraj20090901 where Type=1 and VehicleID="
					+ start.getVehicleID()
					+ " and UtcTime>="
					+ start.getUtcTime()
					+ " and UtcTime<="
					+ end.getUtcTime() + " ;";
			//System.out.println(sql);
			int flag=stm.executeUpdate(sql);
			if(flag==0){
				System.out.println("插入错误出现！！！");
			}else{
				
				stm2 = con.createStatement();
				String sql2 = "update fortra201backup3 set count=" + count
						+ " where VehicleID=" + start.getVehicleID()
						+ " and UtcTime>=" + start.getUtcTime() + " and UtcTime<="
						+ end.getUtcTime() + " ;";
				int flag2=stm2.executeUpdate(sql2);
				if(flag2==0){
					System.out.println("更新错误出现！！！");
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			if (stm2 != null)
				stm2.close();
			if (stm != null)
				stm.close();
			if (con != null)
				con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	//对于两个
	public Data getnext(int a,Data start){
		ConnectSql n = new ConnectSql();
		String sql="";
		Data x=new Data();
		Data y=new Data();
		if(a==0){  //向上遍历
			sql="select VehicleID,UtcTime,TemValue from MatchedTraj20090901 where Type=1 and "
					+ "VehicleID="+start.getVehicleID()+" and UtcTime<="+start.getUtcTime()+" and UtcTime>"
					+ start.getUtcTime().subtract(new BigDecimal(3600))+ " order by UtcTime desc;";
					
		}else if(a==1){ //向下遍历
			sql="select VehicleID,UtcTime,TemValue from MatchedTraj20090901 where Type=1 and "
					+ "VehicleID="+start.getVehicleID()+" and UtcTime>="+start.getUtcTime()+" and UtcTime<"
					+ start.getUtcTime().add(new BigDecimal(3600))+ " order by UtcTime;";
		}
		Connection con = n.Connect();
		Statement stm=null;
		ResultSet rs=null;
		try {
			 stm=con.createStatement();
			 rs=stm.executeQuery(sql);
				while(rs.next()){
					x.setVehicleID(rs.getInt("VehicleID"));
					x.setUtcTime(rs.getBigDecimal("UtcTime"));
					x.setTemValue(rs.getBigDecimal("TemValue").intValue());
					
					if(y.getVehicleID()!=0){
						if(y.getTemValue()==1){
							break;
						}
						if(a==0){
							if(y.getUtcTime().subtract(x.getUtcTime()).doubleValue()>300)
								break;
						}else if(a==1){
							if(x.getUtcTime().subtract(y.getUtcTime()).doubleValue()>300)
								break;
						}
						
					}else{   //第一次
						if(x.getTemValue()==1){
							y.setVehicleID(x.getVehicleID());
							y.setUtcTime(x.getUtcTime());
							y.setTemValue(x.getTemValue());
							break;
						}
					}
					y.setVehicleID(x.getVehicleID());
					y.setUtcTime(x.getUtcTime());
					y.setTemValue(x.getTemValue());
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		try {
			if (rs != null)
				rs.close();
			if (stm != null)
				stm.close();
			if (con != null)
				con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(y.getVehicleID()==0){
			y.setVehicleID(start.getVehicleID());
			y.setUtcTime(start.getUtcTime());
			y.setTemValue(start.getTemValue());
		}
		return y;
	}
}
