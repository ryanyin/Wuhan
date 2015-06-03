import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;


public class PickSpecial {
	private int count=0;
	public static void main(String[] args) {
		System.out.println("开始了"+new Date());
		PickSpecial p=new PickSpecial();
		p.pickVel();
		System.out.println(p.count);
		System.out.println("结束了"+new Date());
	}
	public void pickVel(){      //挑选车辆
		//String sql = "select VehicleID,UtcTime,RoadID,TemValue,mmX,mmY,DigDirect,SysTime,newspeed from shortqunguang order by VehicleID,UtcTime;";
		
		ConnectSql n = new ConnectSql();
		Connection con = n.Connect();

		Statement stm = null;
		ResultSet rs = null;
		try {
			stm = con.createStatement();
			String sql="select distinct VehicleID from selectedroad207 where Type=1 order by VehicleID;";
			rs=stm.executeQuery(sql);
			while(rs.next()){
				int vel=rs.getInt("VehicleID");
				pick1(vel);
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
	
	
	public void pick1(int vel){
		ConnectSql n = new ConnectSql();
		Connection con = n.Connect();

		Statement stm = null;
		ResultSet rs = null;
		try {
			stm = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
			String sql = "select VehicleID,UtcTime,Speed,RoadID,TemValue,mmX,mmY,DigDirect,PointPos,SysTime,newspeed from speed207 where VehicleID="+vel+" order by UtcTime;";
			rs=stm.executeQuery(sql);
			int rowCount = 0;
			boolean a=rs.last();
			if(a){
				rowCount = rs.getRow();
				/*if(rowCount>=3){    //  对于101 有超过3行，可以继续
					//System.out.println("出现3行");
					rs.first();
					forthree(rs);
				}*/
				if(rowCount>=2){     //  对于10或是01 有超过2行则继续
					rs.first();
					fortwo(rs);
				}
				/*if(rowCount>=2){     //  对于11 有超过2行 且速度满足条件则继续
					rs.first();
					fordouble(rs);
				}*/
			}else
				System.out.println("出现错误");
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
	
	public void fortwo(ResultSet rs){
		Data a = new Data();
		Data b = new Data();
		try {
			do {
				
				a.setVehicleID(rs.getInt("VehicleID"));
				a.setUtcTime(rs.getBigDecimal("UtcTime"));
				a.setRoadID(rs.getInt("RoadID"));
				a.setTemValue(rs.getBigDecimal("TemValue").intValue());
				a.setMmX(rs.getBigDecimal("mmX"));
				a.setMmY(rs.getBigDecimal("mmY"));
				a.setDigDirect(rs.getBoolean("DigDirect"));
				a.setSysTime(rs.getTimestamp("SysTime"));
				a.setNewspeed(rs.getFloat("newspeed"));
				a.setPointPos(rs.getBigDecimal("PointPos"));
				a.setSpeed(rs.getBigDecimal("Speed"));
				if (b.getVehicleID() != 0) {
					if (a.getUtcTime().subtract(b.getUtcTime()).doubleValue() <= 240) {
						if (a.getTemValue() == 1 && b.getTemValue() == 0) {
							//System.out.println("出现一条符合的记录");
							count+=1;
							insertfortwo(b, getRoadSpeed(b));
							insertfortwo(a, getRoadSpeed(a));
						}else if(a.getTemValue() == 0 && b.getTemValue() == 1){
							//System.out.println("出现一条符合的记录");
							count+=1;
							insertfortwo(b, getRoadSpeed(b));
							insertfortwo(a, getRoadSpeed(a));
						}
					}
				}
				b.setVehicleID(a.getVehicleID());
				b.setUtcTime(a.getUtcTime());
				b.setRoadID(a.getRoadID());
				b.setTemValue(a.getTemValue());
				b.setMmX(a.getMmX());
				b.setMmY(a.getMmY());
				b.setDigDirect(a.getDigDirect());
				b.setSysTime(a.getSysTime());
				b.setNewspeed(a.getNewspeed());
				b.setPointPos(a.getPointPos());
				b.setSpeed(a.getSpeed());
			} while (rs.next());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public void forthree(ResultSet rs){
		Data a=new Data();
		Data b=new Data();
		Data c=new Data();
		try {
			do{
				
				a.setVehicleID(rs.getInt("VehicleID"));
				a.setUtcTime(rs.getBigDecimal("UtcTime"));
				a.setRoadID(rs.getInt("RoadID"));
				a.setTemValue(rs.getBigDecimal("TemValue").intValue());
				a.setMmX(rs.getBigDecimal("mmX"));
				a.setMmY(rs.getBigDecimal("mmY"));
				a.setDigDirect(rs.getBoolean("DigDirect"));
				a.setSysTime(rs.getTimestamp("SysTime"));
				a.setNewspeed(rs.getFloat("newspeed"));
				a.setPointPos(rs.getBigDecimal("PointPos"));
				a.setSpeed(rs.getBigDecimal("Speed"));
				if(c.getVehicleID()!=0){
					if(a.getUtcTime().subtract(b.getUtcTime()).doubleValue()<=240&&
							b.getUtcTime().subtract(c.getUtcTime()).doubleValue()<=240){
						if(a.getTemValue()==1&&b.getTemValue()==0&&c.getTemValue()==1){
							/*System.out.println("出现一条符合的记录");
							System.out.println(a.getVehicleID());*/
							insertforthree(c, getRoadSpeed(c));
							insertforthree(b, getRoadSpeed(b));
							insertforthree(a, getRoadSpeed(a));
							count+=1;
						}
					}
				}
				if(b.getVehicleID()!=0){
					c.setVehicleID(b.getVehicleID());
					c.setUtcTime(b.getUtcTime());
					c.setRoadID(b.getRoadID());
					c.setTemValue(b.getTemValue());
					c.setMmX(b.getMmX());
					c.setMmY(b.getMmY());
					c.setDigDirect(b.getDigDirect());
					c.setSysTime(b.getSysTime());
					c.setNewspeed(b.getNewspeed());
					c.setPointPos(b.getPointPos());
					c.setSpeed(b.getSpeed());
				}
								
				b.setVehicleID(a.getVehicleID());
				b.setUtcTime(a.getUtcTime());
				b.setRoadID(a.getRoadID());
				b.setTemValue(a.getTemValue());
				b.setMmX(a.getMmX());
				b.setMmY(a.getMmY());
				b.setDigDirect(a.getDigDirect());
				b.setSysTime(a.getSysTime());
				b.setNewspeed(a.getNewspeed());
				b.setPointPos(a.getPointPos());
				b.setSpeed(a.getSpeed());
			}while(rs.next());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void fordouble(ResultSet rs){
		Data a = new Data();
		Data b = new Data();
		try {
			do {
				
				a.setVehicleID(rs.getInt("VehicleID"));
				a.setUtcTime(rs.getBigDecimal("UtcTime"));
				a.setRoadID(rs.getInt("RoadID"));
				a.setTemValue(rs.getBigDecimal("TemValue").intValue());
				a.setMmX(rs.getBigDecimal("mmX"));
				a.setMmY(rs.getBigDecimal("mmY"));
				a.setDigDirect(rs.getBoolean("DigDirect"));
				a.setSysTime(rs.getTimestamp("SysTime"));
				a.setNewspeed(rs.getFloat("newspeed"));
				a.setPointPos(rs.getBigDecimal("PointPos"));
				a.setSpeed(rs.getBigDecimal("Speed"));
				if (b.getVehicleID() != 0) {
					if (a.getUtcTime().subtract(b.getUtcTime()).doubleValue() <= 240) {
						if (a.getTemValue() == 1 && b.getTemValue() == 1) {
							//System.out.println("出现一条符合的记录");
							float speeda=getRoadSpeed(a);
							float speedb=getRoadSpeed(b);
							
							if(a.getNewspeed()<=speeda*0.4713 && b.getNewspeed()<=speedb*0.4713){
								count+=1;
								/*System.out.print("当前记录： "+count);
								System.out.println("  对应的车："+a.getVehicleID());*/
								insertfordouble(b, getRoadSpeed(b));
								insertfordouble(a, getRoadSpeed(a));
								
							}
								
							/*insertfortwo(b, getRoadSpeed(b));
							insertfortwo(a, getRoadSpeed(a));*/
						}
					}
				}
				b.setVehicleID(a.getVehicleID());
				b.setUtcTime(a.getUtcTime());
				b.setRoadID(a.getRoadID());
				b.setTemValue(a.getTemValue());
				b.setMmX(a.getMmX());
				b.setMmY(a.getMmY());
				b.setDigDirect(a.getDigDirect());
				b.setSysTime(a.getSysTime());
				b.setNewspeed(a.getNewspeed());
				b.setPointPos(a.getPointPos());
				b.setSpeed(a.getSpeed());
			} while (rs.next());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public float getRoadSpeed(Data a){
		float speed=0;
		ConnectSql n = new ConnectSql();
		Connection con = n.Connect();

		Statement stm = null;
		ResultSet rs = null;
		try {
			stm=con.createStatement();
			String sql="select Speed from SpeedRoad207 where EndTime>="+a.getUtcTime()+" and EndTime<"+a.getUtcTime().add(new BigDecimal(300))+" ;";
			rs=stm.executeQuery(sql);
			while(rs.next()){
				speed=rs.getFloat("Speed");
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
		return speed;
		
	}
	
	public void insertforthree(Data a,float speed){
		ConnectSql n = new ConnectSql();
		Connection con = n.Connect();

		Statement stm = null;
		ResultSet rs = null;
		int c=-1;
		if(a.getDigDirect()){
			c=1;
		}else
			c=0;
		try {
			stm=con.createStatement();
			String sql = "insert into forthree values(" + a.getVehicleID()
					+ " , " + a.getUtcTime() + " , " + a.getSpeed() + " , "
					+ a.getTemValue() + " , " + a.getRoadID() + " , "
					+ a.getMmX() + " , " + a.getMmY() + " , " + a.getPointPos()
					+ " , " + c + " , '" + a.getSysTime() + "' , "
					+ a.getNewspeed() + " , " + speed + ");";
			
			if(stm.executeUpdate(sql)==0){
				System.out.println(sql);
				System.out.println("插入失败了");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void insertfortwo(Data a,float speed){
		ConnectSql n = new ConnectSql();
		Connection con = n.Connect();

		Statement stm = null;
		ResultSet rs = null;
		int c=-1;
		if(a.getDigDirect()){
			c=1;
		}else
			c=0;
		try {
			stm=con.createStatement();
			String sql = "insert into fortwo207 values(" + a.getVehicleID()
					+ " , " + a.getUtcTime() + " , " + a.getSpeed() + " , "
					+ a.getTemValue() + " , " + a.getRoadID() + " , "
					+ a.getMmX() + " , " + a.getMmY() + " , " + a.getPointPos()
					+ " , " + c + " , '" + a.getSysTime() + "' , "
					+ a.getNewspeed() + " , " + speed + ");";
			
			if(stm.executeUpdate(sql)==0){
				System.out.println(sql);
				System.out.println("插入失败了");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void insertfordouble(Data a,float speed){
		ConnectSql n = new ConnectSql();
		Connection con = n.Connect();

		Statement stm = null;
	
		int c=-1;
		if(a.getDigDirect()){
			c=1;
		}else
			c=0;
		try {
			stm=con.createStatement();
			String sql = "insert into Road2double values(" + a.getVehicleID()
					+ " , " + a.getUtcTime() + " , " + a.getSpeed() + " , "
					+ a.getTemValue() + " , " + a.getRoadID() + " , "
					+ a.getMmX() + " , " + a.getMmY() + " , " + a.getPointPos()
					+ " , " + c + " , '" + a.getSysTime() + "' , "
					+ a.getNewspeed() + " , " + speed + ");";
			
			if(stm.executeUpdate(sql)==0){
				System.out.println(sql);
				System.out.println("插入失败了");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
