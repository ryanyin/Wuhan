import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;


public class Allroadtime {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Allroadtime a=new Allroadtime();
		a.getVehicleID(21685);
	}
	
	public void getvel(){
		ConnectSql n = new ConnectSql();
		String sql = "select distinct VehicleID from selectedall  order by VehicleID;";

		try (Connection con = n.Connect();
				Statement stm = con.createStatement();
				ResultSet rs = stm.executeQuery(sql);) {
			while(rs.next()){
				int VehicleID=rs.getInt("VehicleID");
				//System.out.println("RoadID "+roadid);
				getVehicleID(VehicleID);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void getVehicleID(int vehicleID) {
		// TODO Auto-generated method stub
		ConnectSql n = new ConnectSql();
		String sql = "select  VehicleID,mmX,mmY,UtcTime from MatchedTraj20090901 where VehicleID="+vehicleID+" and Type=1 order by UtcTime;";
		double first=0;
		double second=0;
		int rowcount=0;
		ArrayList<Integer> map=new ArrayList<Integer>();
		try (Connection con = n.Connect();
				Statement stm = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
				ResultSet rs = stm.executeQuery(sql);) {
			while(rs.next()){
				first=rs.getBigDecimal("UtcTime").doubleValue();
				rowcount++;
				if(second!=0){
					if((first-second)>600){
						//System.out.println(first+" - "+second+"="+(first-second));
						map.add(rs.getRow());
					}
				}
				second=first;
				
			}
			
			if(map.size()==0){
				dodiedai(rs,1,rowcount);
			}else{
				for(int i=0;i<map.size();i++){
					if(i==0){
						dodiedai(rs,1,map.get(0)-1);
					}else{
						dodiedai(rs,map.get(i-1),map.get(i)-1);
					}
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	private void dodiedai(ResultSet rs, int a, int b) throws SQLException {
		// TODO Auto-generated method stub
		ArrayList<Data> list=new ArrayList<Data>();
		
		rs.absolute(a);
		do{
			Data temp=new Data();
			temp.setVehicleID(rs.getInt("VehicleID"));
			temp.setMmX(rs.getBigDecimal("mmX"));
			temp.setMmY(rs.getBigDecimal("mmY"));
			temp.setUtcTime(rs.getBigDecimal("UtcTime"));
			//System.out.println(temp.getUtcTime());
			list.add(temp);
		}while(rs.next()&&rs.getRow()<=b);
		System.out.println(list.toString());
		for(int i=0;i<list.size();i++){
			if((list.size()-i)>2){
				function(list.get(i),list.get(i+1));
				function(list.get(i),list.get(i+2));
			}else if((list.size()-i)==2){
				function(list.get(i),list.get(i+1));
			}else if((list.size()-i)==1){
				System.out.println("最后一条记录");
			}
		}
	}

	private void function(Data data, Data data2) {
		// TODO Auto-generated method stub
		System.out
				.println(data2.getUtcTime()
						+ " - "
						+ data.getUtcTime()
						+ " = "
						+ (data2.getUtcTime().subtract(data.getUtcTime())
								.doubleValue()));
	}
	}
	
	
	
	

