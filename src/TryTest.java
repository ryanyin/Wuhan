import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Calendar;


public class TryTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		/*TryTest t=new TryTest();
	    t.test();*/
		//System.out.println(d);
		/*int a=5/10;
		System.out.println(a);*/
		/*Timestamp ts = new Timestamp(System.currentTimeMillis());
		String s="2009-09-01 00:00:00";
		ts = Timestamp.valueOf(s);  
        System.out.println(ts);
        
        Timestamp ts2=new Timestamp(ts.getTime()+300000);
        //ts=ts.getTime()+300000;
        System.out.println(ts2);*/
       
		/*String s="select count(starttime) as number from zaike0901 where lasttime<180;";
		System.out.println(s);
		int a=180;
		int b=300;
		for(int i=1;i<12;i++){
			s="select count(starttime) as number from zaike0901 where lasttime>="+a+" and lasttime<"+b+" ;";
			System.out.println(s);
			a+=120;
			b+=120;
		}*/
		int a=(int)122.2/10;
		System.out.println(a);
	}

	
	
	
	public  void  test(){
		ConnectSql n = new ConnectSql();
		Connection con = n.Connect();

		Statement stm = null;
		ResultSet rs = null;
		try {
			stm = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
			/*String sql = "select DigDirect from qunguang where VehicleID=11283 "
					+ "and UtcTime=1251784970.705320;";*/
			String sql = "select VehicleID,UtcTime,RoadID,TemValue,mmX,mmY,DigDirect,SysTime,newspeed from shortqunguang where VehicleID=11209 order by UtcTime;";
			rs = stm.executeQuery(sql);
			int rowCount = 0;
			boolean a=rs.last();
			if(a){
				rowCount = rs.getRow();
				if(rowCount>=3){    //有超过3行，可以继续
					rs.first();
					do{
						System.out.println(rs.getRow());
						System.out.println(rs.getBigDecimal("UtcTime"));
					}while(rs.next());
				}
			}else
				System.out.println("出现错误");
			/*while (rs.next()) {
				//Boolean a=rs.getBoolean("DigDirect");
				System.out.println(rs.getBoolean("DigDirect"));
				System.out.println(a);
			}*/
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
	
	
	public Data test2(){
		Data end =new Data();
		if(end.getRoadID()!=0)
		{
			System.out.println("11111111111");
		}else
			System.out.println("22222222222");
		
		return end;
		
	}
}
