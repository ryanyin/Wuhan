/**
 * 
 */
package getFeature;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import entity.ConnectSql;
import entity.RoadSpeedEntity;
import entity.RoadTimeEntity;

/**
 * @author ryan
 * 针对每个时刻的每条道路计算其时间
 */
public class CulTime {

	
	private Connection con;
	
	public CulTime() {
		super();
		con = ConnectSql.Connect();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		CulTime cul=new CulTime();
		cul.getRoadID();
		if(cul.con!=null){
			try {
				cul.con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void getRoadID(){
		//String sql="select * from RoadLength order by roadid";
		String sql ="select RoadAllTime00.RoadID,RoadLength.length,RoadAllTime00.Time from RoadAllTime00,RoadLength where RoadAllTime00.RoadID=RoadLength.roadid and RoadAllTime00.for05 is null order by RoadAllTime00.RoadID;";
		try (Statement stm=con.createStatement();
				ResultSet rs=stm.executeQuery(sql);){
			while(rs.next()){
				int roadID=rs.getInt("RoadID");
				float roadlength=rs.getBigDecimal("length").floatValue();
				System.out.println("当前的道路ID  "+roadID);
				int time=rs.getInt("Time");
				foreachroad(roadID,roadlength,time);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	private void foreachroad(int roadID, float roadlength, int time) {
		// TODO Auto-generated method stub
		String sql="select * from RoadAllSpeed00 where RoadID ="+roadID+" and Time="+time+" order by Time";
		roadlength=(float) (roadlength*0.06);   //已分钟为单位
		try (Statement stm=con.createStatement();
				ResultSet rs=stm.executeQuery(sql);){
			RoadTimeEntity r=new RoadTimeEntity();
			while(rs.next()){
				r.setRoadID(rs.getInt("RoadID"));
				r.setFor05(roadlength/(rs.getFloat("for05")));
				r.setFor10(roadlength/(rs.getFloat("for10")));
				r.setFor15(roadlength/(rs.getFloat("for15")));
				r.setFor20(roadlength/(rs.getFloat("for20")));
				r.setFor25(roadlength/(rs.getFloat("for25")));
				r.setFor30(roadlength/(rs.getFloat("for30")));
				r.setFor35(roadlength/(rs.getFloat("for35")));
				r.setFor40(roadlength/(rs.getFloat("for40")));
				r.setFor45(roadlength/(rs.getFloat("for45")));
				r.setFor50(roadlength/(rs.getFloat("for50")));
				r.setFor55(roadlength/(rs.getFloat("for55")));
				r.setFor60(roadlength/(rs.getFloat("for60")));
				r.setPATHCLASS(rs.getInt("PATHCLASS"));
				r.setFlag(rs.getString("flag"));
				r.setTime(rs.getShort("Time"));
				updatetime(r);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	private void updatetime(RoadTimeEntity r) {
		// TODO Auto-generated method stub
		String sql = "update RoadAllTime00 set for05=" + r.getFor05()
				+ " , for10=" + r.getFor10() + " , for15=" + r.getFor15()
				+ " , for20=" + r.getFor20() + " , for25=" + r.getFor25()
				+ " , for30=" + r.getFor30() + " , for35=" + r.getFor35()
				+ " , for40=" + r.getFor40() + " , for45=" + r.getFor45()
				+ " , for50=" + r.getFor50() + " , for55=" + r.getFor55()
				+ " , for60=" + r.getFor60() + " where RoadID="
				+ r.getRoadID() + " and Time=" + r.getTime()
				+ " and PATHCLASS=" + r.getPATHCLASS() + ";";
		try (Statement stm=con.createStatement();){
			int flag=stm.executeUpdate(sql);
			if(flag==0){
				System.out.println(sql);
				System.out.println("更新出现异常");
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}
