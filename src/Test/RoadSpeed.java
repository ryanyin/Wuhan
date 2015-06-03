package Test;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Date;


public class RoadSpeed {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("开始了"+new Date());
		RoadSpeed r=new RoadSpeed();
		double d=1251806400.000000;
		String lie="for05";
		for(int i=1;i<13;i++){
			switch (i) {
			case 1:
				d=1251806400.000000;
				lie="for05";
				break;
			case 2:
				d=1251806700.000000;
				lie="for10";
				break;
			case 3:
				d=1251807000.000000;
				lie="for15";
				break;
			case 4:
				d=1251807300.000000;
				lie="for20";
				break;
			case 5:
				d=1251807600.000000;
				lie="for25";
				break;
			case 6:
				d=1251807900.000000;
				lie="for30";
				break;
			case 7:
				d=1251808200.000000;
				lie="for35";
				break;
			case 8:
				d=1251808500.000000;
				lie="for40";
				break;
			case 9:
				d=1251808800.000000;
				lie="for45";
				break;
			case 10:
				d=1251809100.000000;
				lie="for50";
				break;
			case 11:
				d=1251809400.000000;
				lie="for55";
				break;
			case 12:
				d=1251809700.000000;
				lie="for60";
				break;
			default:
				break;
			}
			System.out.println(lie);
		  // String lie="for35";
			r.getroad(d,lie);
			System.out.println(new Date());
		}
		
		System.out.println("结束了"+new Date());
	}

	
	public void getroad(double d,String lie){
		ConnectSql n = new ConnectSql();
		String sql = "select  RoadID ,DigDirect from selectedall group  by RoadID ,DigDirect order by RoadID;";

		try (Connection con = n.Connect();
				Statement stm = con.createStatement();
				ResultSet rs = stm.executeQuery(sql);) {
			while(rs.next()){
				int roadid=rs.getInt("RoadID");
				boolean DigDirect=rs.getBoolean("DigDirect");
				//System.out.println("RoadID "+roadid);
				int digDirect=0;
				if(DigDirect){
					digDirect=1;
				}
				getroadspeed(roadid,digDirect,d,lie);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void getroadspeed(int roadid, int digDirect, double d, String lie) {
		// TODO Auto-generated method stub
		BigDecimal start=new BigDecimal(d);
		BigDecimal gap=new BigDecimal(300);
		BigDecimal end=new BigDecimal(0);
		end=start.add(gap);
		
		ConnectSql n = new ConnectSql();
		String sql = "select newspeed from selectedall where newspeed<100 and RoadID =" + roadid
				+ " and digDirect="+digDirect+" and UtcTime>=" + start + " and UtcTime<" + end + ";";
		float roadspeed=0;
		int count=0;

		try (Connection con = n.Connect();
				Statement stm = con.createStatement();
				ResultSet rs = stm.executeQuery(sql);) {
			while(rs.next()){
				float ss=rs.getFloat("newspeed");			
				 roadspeed+=ss;
				 count++;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	    if(count!=0){
	    	float a=roadspeed/count;
	    	insert(roadid,digDirect,a,lie);
	    	if(a!=0){
	    		float b=(float) (getroaglength(roadid)/(a/3.6))/60;   //分钟
		    	insertintotime(roadid,digDirect, b, lie);
	    	}else{
	    		//insertintotime(roadid,digDirect, 0, lie);
	    		System.out.println("道路为 "+roadid+" 方向为  "+digDirect+" 列为 "+lie);
	    	}
	    	
	    }/*else{
	    	//System.out.println("道路速度上没有速度");
	    	insert(roadid,roadspeed,lie);
	    }*/
		
	}


	private void insert(int roadid,int digDirect, float roadspeed, String lie) {
		// TODO Auto-generated method stub
		ConnectSql n = new ConnectSql();		 
		String sql = "update RoadAllSpeedsecond set "+lie+"="+roadspeed+" where RoadID="+roadid+" and DigDirect="+digDirect+";";
		try (Connection con = n.Connect();
				Statement stm=con.createStatement()){
			
			int flag=stm.executeUpdate(sql);
			if(flag==0){
				System.out.println(sql);
				System.out.println("更新出现错误");
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println(sql);
			e.printStackTrace();
		}
	}

	public double getroaglength(int roadid){
		ConnectSql n = new ConnectSql();
		float roadlength = 0;
		String sql = "select length from RoadLength where roadid=" + roadid
				+ ";";
		try (Connection con = n.Connect();
				Statement stm = con.createStatement();
				ResultSet rs = stm.executeQuery(sql);) {
			while(rs.next()){
				roadlength=rs.getInt("length");
				//System.out.println("length "+roadlength);
				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println(sql);
			e.printStackTrace();
		}
		return roadlength;
		
	}
	private void insertintotime(int roadid,int digDirect, float roadtime, String lie) {
		// TODO Auto-generated method stub
		ConnectSql n = new ConnectSql();		 
		String sql = "update RoadAllTimesecond set "+lie+"="+roadtime+" where RoadID="+roadid+" and DigDirect="+digDirect+";";
		try (Connection con = n.Connect();
				Statement stm=con.createStatement()){
			
			int flag=stm.executeUpdate(sql);
			if(flag==0){
				System.out.println(sql);
				System.out.println("更新出现错误");
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println(sql);
			e.printStackTrace();
		}
	}
	//简单的计算，只要记录点在道路上则将其平均
	public void culspeed(){
		BigDecimal start=new BigDecimal(1252281600.000000);
		BigDecimal gap=new BigDecimal(300);
		Timestamp ts = new Timestamp(System.currentTimeMillis());
		String s="2009-09-07 00:00:00";
		ts = Timestamp.valueOf(s); 
		while(start.doubleValue()<1252368000.000000){
			culsp(start, ts);
			start=start.add(gap);
			ts=new Timestamp(ts.getTime()+300000);
		}
		
		
	}
	
	public void culsp(BigDecimal start,Timestamp s){
		BigDecimal gap=new BigDecimal(300);
		BigDecimal end=new BigDecimal(0);
		ConnectSql n = new ConnectSql();
		Connection con = n.Connect();
		int count=0;
		float speed=0;
		float roadspeed=0;
		Statement stm = null;
		ResultSet rs = null;
		end=start.add(gap);
		try {
			stm=con.createStatement();
			String sql="select * from speed207 where  UtcTime>="+start+" and UtcTime<="+end+" ;";
			rs=stm.executeQuery(sql);
			while(rs.next()){
				count+=1;
				speed+=rs.getFloat("newspeed");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(count!=0){
			roadspeed=speed/count;
			//System.out.println("speed   "+speed+"  count   "+count+"  roadspeed  "+roadspeed);
		}else{
			System.out.println("五分钟内没有记录点，可能出现错误");
			roadspeed=0;
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
		insertTo(start, s, roadspeed);
		System.out.println(s);
	}
	
	
	
	//插入数据库
	public void insertTo(BigDecimal start,Timestamp s,float speed){
		BigDecimal end=start.add(new BigDecimal(300));
		Timestamp e=new Timestamp(s.getTime()+300000);
		ConnectSql n = new ConnectSql();
		Connection con = n.Connect();
		Statement stm = null;
	
		try {
			stm=con.createStatement();                           
			String sql = "insert into SpeedRoad207 values(" + start + " , " + end
					+ " , '" + s + "' , '"+e+"' , "+speed+")";
			//System.out.println(sql);
			int flag=stm.executeUpdate(sql);
			if(flag==0){
				System.out.println("插入出现错误");
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			
			if (stm != null)
				stm.close();
			if (con != null)
				con.close();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
}
