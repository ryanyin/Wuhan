package Test;



import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

public class Test01 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("开始了"+new Date());
		Test01 r=new Test01();
		double d=1251809700.000000;
		String lie="for60";
		r.getroad(d,lie);
		System.out.println(new Date());
	}
	public void getroad(double d,String lie){
		ConnectSql n = new ConnectSql();
		String sql = "select distinct RoadID from  RoadAllTime where "+lie+"=0  order by RoadID;";

		try (Connection con = n.Connect();
				Statement stm = con.createStatement();
				ResultSet rs = stm.executeQuery(sql);) {
			while(rs.next()){
				int roadid=rs.getInt("RoadID");
				//System.out.println("RoadID "+roadid);
				getroadspeed(roadid,d,lie);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void getroadspeed(int roadid, double d, String lie) {
		// TODO Auto-generated method stub
		BigDecimal start=new BigDecimal(d);
		BigDecimal gap=new BigDecimal(300);
		BigDecimal end=new BigDecimal(0);
		end=start.add(gap);
		ConnectSql n = new ConnectSql();
		String sql = "select newspeed from selectedall where newspeed<100 and RoadID =" + roadid
				+ " and UtcTime>=" + start + " and UtcTime<" + end + ";";
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
	    	insert(roadid,a,lie);
	    	if(a!=0){
	    		float b=(float) (getroaglength(roadid)/(a/3.6))/60;   //分钟
		    	insertintotime(roadid, b, lie);
	    	}else{
	    		insertintotime(roadid, 0, lie);
	    	}
	    	
	    }/*else{
	    	//System.out.println("道路速度上没有速度");
	    	insert(roadid,roadspeed,lie);
	    }*/
		
	}
	private void insert(int roadid, float roadspeed, String lie) {
		// TODO Auto-generated method stub
		ConnectSql n = new ConnectSql();		 
		String sql = "update RoadAllSpeed set "+lie+"="+roadspeed+" where RoadID="+roadid+";";
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
	private void insertintotime(int roadid, float roadtime, String lie) {
		// TODO Auto-generated method stub
		ConnectSql n = new ConnectSql();		 
		String sql = "update RoadAllTime set "+lie+"="+roadtime+" where RoadID="+roadid+";";
		try (Connection con = n.Connect();
				Statement stm=con.createStatement()){
			
			int flag=stm.executeUpdate(sql);
			System.out.println(sql);
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

}
