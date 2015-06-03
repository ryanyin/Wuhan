import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;


public class Culdis {
	private int VecId=0;	
	private int countA=0;  //0-10s
	private int countB=0;  //10s-20s����
	private int countC=0;  //20s-30s����
	private int countD=0;  //30s-40s����
	private int countE=0;  //40s-50s����
	private int countF=0;  //50s-60s����
	private int countG=0;  //60s����
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println(new Date());
		Culdis a = new Culdis();
		a.culdis();
		System.out.println(new Date());
	}

	public void culdis(){
		ConnectSql n = new ConnectSql();
		Connection con = n.Connect();
    
		Statement stm = null;
		ResultSet rs = null;
		int VehicleID=0;
		double time1=0;
		double time2=0;
		try {
			stm=con.createStatement();
			String sql = "select VehicleID,UtcTime from short0901 order by VehicleID,UtcTime";
			rs=stm.executeQuery(sql);
			while(rs.next()){
				VehicleID = rs.getInt("VehicleID");
				time1 = rs.getBigDecimal("UtcTime").doubleValue();
				if (VehicleID == VecId) {
					double a = time1 - time2;
					if (a <= 180) {        //ʱ����3����
						if (a <= 10) {
							countA++;
						} else if (10 < a && a <= 20) {
							countB++;
						} else if (20 < a && a <= 30) {
							countC++;
						} else if (30 < a && a <= 40) {
							countD++;
						} else if (40 < a && a <= 50) {
							countE++;
						} else if (50 < a && a <= 60) {
							countF++;
						} else if (60 < a) {
							countG++;
						}
					}
					
				} else {
					VecId = VehicleID;
				}
				time2 = time1;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("���0-10s   "+countA);
		System.out.println("���10-20s  "+countB);
		System.out.println("���20-30s  "+countC);
		System.out.println("���30-40s  "+countD);
		System.out.println("���40-50s  "+countE);
		System.out.println("���50-60s  "+countF);
		System.out.println("���60-s    "+countG);
		System.out.println("����ܹ�              "+(countA+countB+countC+countD+countE+countF+countG));
	}
}
