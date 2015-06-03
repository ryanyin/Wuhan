import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class PickTra {
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("开始啦");
		PickTra p = new PickTra();
		p.getout();
		System.out.println("结束啦");
	}
	
	
	public  void getout(){
		ConnectSql n=new ConnectSql();
		Connection con=n.Connect();
		
		Statement stm=null;
		ResultSet rs=null;
		
		
		try {
			stm=con.createStatement();
			String sql="select * from zaike0901;";
			rs=stm.executeQuery(sql);
			while (rs.next()) {
				Statement stm2 = con.createStatement();

				String sql2 = "insert into new09012 select * from MatchedTraj20090901 where VehicleID="
						+ rs.getString("VehicleID")
						+ " and UtcTime>="
						+ rs.getBigDecimal("starttime")
						+ "  and UtcTime<="
						+ rs.getBigDecimal("endtime") + " ;";
				// System.out.println(sql2);
				int flag = stm2.executeUpdate(sql2);
				if (flag == 0) {
					System.out.println("插入失败了");
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
