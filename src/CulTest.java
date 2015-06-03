import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;


public class CulTest {   //计算记录的时间间隔

	private int a10=0;  //0-10s
	private int a20=0;  //10-20s
	private int a30=0;  //20-30s
	private int a40=0;  //30-40s
	private int a50=0;  //40-50s
	private int a60=0;  //50-60s
	private int a70=0;  //60-70s
	private int a80=0;  //70-80s
	private int a90=0;  //80-90s
	private int a100=0;  //90-100s
	private int a110=0;  //100-110s
	private int a120=0;  //110-120s
	private int a130=0;  //120-130s
	private int a140=0;  //130-140s
	private int a150=0;  //140-150s
	private int a160=0;  //150-160s
	private int a170=0;  //160-170s
	private int a180=0;  //170-180s
	private int a190=0;  //180-190s
	private int a200=0;  //190-200s
	private int a210=0;  //200-210s
	private int a220=0;  //210-220s
	private int a230=0;  //220-230s
	private int a240=0;  //230-240s
	
	private int sumtime=0;
	private double sumcount=0;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println(new Date());
		CulTest t=new CulTest();
		t.getresult();
		System.out.println(t.a10);
		System.out.println(t.a20);
		System.out.println(t.a30);
		System.out.println(t.a40);
		System.out.println(t.a50);
		System.out.println(t.a60);
		System.out.println(t.a70);
		System.out.println(t.a80);
		System.out.println(t.a90);
		System.out.println(t.a100);
		System.out.println(t.a110);
		System.out.println(t.a120);
		System.out.println(t.a130);
		System.out.println(t.a140);
		System.out.println(t.a150);
		System.out.println(t.a160);
		System.out.println(t.a170);
		System.out.println(t.a180);
		System.out.println(t.a190);
		System.out.println(t.a200);
		System.out.println(t.a210);
		System.out.println(t.a220);
		System.out.println(t.a230);
		System.out.println(t.a240);
		
		System.out.println("次数："+t.sumcount);
		System.out.println("总时间："+t.sumtime);
		System.out.println("平均时间："+t.sumtime/t.sumcount);
		System.out.println(new Date());
	}

	
	public void getresult(){
		ConnectSql n=new ConnectSql();
		Connection con=n.Connect();
		
		Statement stm=null;
		ResultSet rs=null;
		
		try {
			stm=con.createStatement();
			String sql="select distinct VehicleID from MatchedTraj20090901 order by VehicleID;";
			rs=stm.executeQuery(sql);
			while(rs.next()){
				int vel=rs.getInt("VehicleID");
				forvel(vel);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			if(rs!=null)
				rs.close();
			if(stm!=null)
				stm.close();
			if(con!=null)
				con.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void forvel(int a){
		ConnectSql n=new ConnectSql();
		Connection con=n.Connect();
		
		Statement stm=null;
		ResultSet rs=null;
		Data first=new Data();
		Data second=new Data();
		try {
			stm=con.createStatement();
			String sql="select VehicleID,UtcTime from MatchedTraj20090901 where VehicleID="+a+" and Type=1 order by UtcTime";
			rs=stm.executeQuery(sql);
			while(rs.next()){
				first.setVehicleID(rs.getInt("VehicleID"));
				first.setUtcTime(rs.getBigDecimal("UtcTime"));
				if(second.getVehicleID()!=0){
					if(first.getUtcTime().subtract(second.getUtcTime()).doubleValue()<=240){
						int x=(int)(first.getUtcTime().subtract(second.getUtcTime()).doubleValue())/10;
						switch (x) {
						case 0:
							a10+=1;
							break;
						case 1:
							a20+=1;
							break;
						case 2:
							a30+=1;
							break;
						case 3:
							a40+=1;
							break;
						case 4:
							a50+=1;
							break;
						case 5:
							a60+=1;
							break;
						case 6:
							a70+=1;
							break;
						case 7:
							a80+=1;
							break;
						case 8:
							a90+=1;
							break;
						case 9:
							a100+=1;
							break;
						case 10:
							a110+=1;
							break;
						case 11:
							a120+=1;
							break;
						case 12:
							a130+=1;
							break;
						case 13:
							a140+=1;
							break;
						case 14:
							a150+=1;
							break;
						case 15:
							a160+=1;
							break;
						case 16:
							a170+=1;
							break;
						case 17:
							a180+=1;
							break;
						case 18:
							a190+=1;
							break;
						case 19:
							a200+=1;
							break;
						case 20:
							a210+=1;
							break;
						case 21:
							a220+=1;
							break;
						case 22:
							a230+=1;
							break;
						case 23:
							a240+=1;
							break;
						default:
							break;
						}
						sumcount++;
						sumtime+=first.getUtcTime().subtract(second.getUtcTime()).doubleValue();
					}
				}
				second.setVehicleID(first.getVehicleID());
				second.setUtcTime(first.getUtcTime());
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			if(rs!=null)
				rs.close();
			if(stm!=null)
				stm.close();
			if(con!=null)
				con.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
