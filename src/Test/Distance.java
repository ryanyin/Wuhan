package Test;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Distance {

	public double culdistance(Data start,Data end)       //两个相邻记录点间的速度
	{
		
		double s=0;   //距离
		double length1=roadlength(start.getRoadID());   //道路1的长度
		double length2=roadlength(end.getRoadID());   //道路2的长度
		if (start.getRoadID() == end.getRoadID()        //道路相同，方向相同
				&& start.getDigDirect() == end.getDigDirect()) {
			s = length1
					* Math.abs(start.getPointPos().doubleValue()
							- end.getPointPos().doubleValue());
		}
		if(start.getRoadID() == end.getRoadID()         //道路相同，方向不同
				&&start.getDigDirect() != end.getDigDirect())
		{
			System.out.println("pay attention,可能出现错误   "+start.getVehicleID()+"   time  "+start.getUtcTime());   //在道路中间掉头
			if (start.getDigDirect()) {
				s = length1 * (1 - start.getPointPos().doubleValue()) + length2
						* (1 - end.getPointPos().doubleValue());
			} else
				s = length1 * (start.getPointPos().doubleValue()) + length2
						* (end.getPointPos().doubleValue());
		}
		if(start.getRoadID() != end.getRoadID()         //道路不同，方向相同
				&&start.getDigDirect() == end.getDigDirect())
		{
			if(start.getDigDirect())
			{
				s=length1*(1-start.getPointPos().doubleValue())
						+length2*(end.getPointPos().doubleValue());
			}else
				s=length1*(start.getPointPos().doubleValue())
				+length2*(1-end.getPointPos().doubleValue());
		}
		if(start.getRoadID() != end.getRoadID()         //道路不同，方向不同
				&&start.getDigDirect() != end.getDigDirect())
		{
			if (start.getDigDirect()) {
				s = length1 * (1 - start.getPointPos().doubleValue()) + length2
						* (1 - end.getPointPos().doubleValue());
			} else
				s = length1 * (start.getPointPos().doubleValue()) + length2
						* (end.getPointPos().doubleValue());
		}
		return s;
		
	}

	public double roadlength(int roadid) {
		double roadlength = 0;
		switch (roadid) {
		case 10970:
			roadlength = 285.7460920;
			break;
		case 10973:
			roadlength = 200.0079870;
			break;
		case 10972:
			roadlength = 302.8748200;
			break;
		case 10471:
			roadlength = 107.0683920;
			break;
		case 10450:
			roadlength = 289.5760520;
			break;
		case 13891:
			roadlength = 132.0954040;
			break;
		case 13892:
			roadlength = 66.6138110;
			break;
		case 10976:
			roadlength = 304.1389460;
			break;
		case 10977:
			roadlength = 103.2860190;
			break;
		case 10967:
			roadlength = 13.0601900;
			break;
		case 10968:
			roadlength = 16.5402290;
			break;
		case 10969:
			roadlength = 16.7391720;
			break;
		default:
			roadlength = outof(roadid);
			break;
		}
		if (roadlength == 0)
			System.out.println("出现bug了");
		return roadlength;
	}

	private double outof(int roadid) {
		// TODO Auto-generated method stub
		double roadlength = 0;
		ConnectSql n = new ConnectSql();
		Connection con = n.Connect();

		Statement stm = null;
		ResultSet rs = null;
		try {
			stm = con.createStatement();
			String sql = "select length from RoadLength where roadid=" + roadid
					+ ";";
			rs = stm.executeQuery(sql);
			while (rs.next()) {
				roadlength=rs.getBigDecimal("length").doubleValue();
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
		
		return roadlength;
	}
	
	
	
	//补齐自动添加点之后的距离
	public double getdistance(Data start,Data end){
		ConnectSql n = new ConnectSql();
		Connection con = n.Connect();

		Statement stm = null;
		ResultSet rs = null;
		double sum=0;
		Data a= new Data();
		Data b= new Data();
		try {
			stm = con.createStatement();
			String sql="select * from MatchedTraj20090901 where VehicleID="+start.getVehicleID()+
					"  and UtcTime>="+start.getUtcTime()+" and UtcTime<="+end.getUtcTime()+" order by UtcTime;";
			rs = stm.executeQuery(sql);
			while(rs.next()){
				a.setDigDirect(rs.getBoolean("DigDirect"));
				a.setPointPos(rs.getBigDecimal("PointPos"));
				a.setRoadID(rs.getInt("RoadID"));
				a.setTemValue(rs.getBigDecimal("TemValue").intValue());
				a.setVehicleID(rs.getInt("VehicleID"));
				a.setUtcTime(rs.getBigDecimal("UtcTime"));
				if(b.getVehicleID()!=0){
					sum+=culdistance(b, a);
				}
				
				b.setDigDirect(a.getDigDirect());
				b.setPointPos(a.getPointPos());
				b.setRoadID(a.getRoadID());
				b.setTemValue(a.getTemValue());
				b.setVehicleID(a.getVehicleID());
				b.setUtcTime(a.getUtcTime());
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sum;
		
	} 

}
