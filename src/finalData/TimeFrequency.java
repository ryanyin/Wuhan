/**
 * 
 */
package finalData;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import entity.ConnectSql;


/**
 * @author ryan
 *
 */
public class TimeFrequency {

	/**
	 * @param args
	 */
	public static int[] a=new int[13];  //a[0]代表0-10，a[1]代表10-20,a[11]代表110-120，a[12]代表120以上
	public static double sum_time;
	public static int sum_count;
	public static Connection con=ConnectSql.Connect();
	public static void main(String[] args) {
		for(int i=0;i<a.length;i++)
			a[i]=0;
		getCar();
//		cul(11222);
		for(int i=0;i<a.length;i++)
			System.out.println("a["+i+"]="+a[i]);
		System.out.println("sum_count:"+sum_count);
		System.out.println("sum_time:"+sum_time);
		System.out.println("平均:"+sum_time/sum_count);
		try {
			con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(new Date());
	}
	//获取每一辆车的ID
	private static void getCar() {
		String sql="select distinct VehicleID from MatchedTraj20090907  order by VehicleID;" ;
		try(Statement stm=con.createStatement();
				ResultSet rs=stm.executeQuery(sql);) {
			while(rs.next()){
				int carID=rs.getInt("VehicleID");
				System.out.println(carID);
				cul(carID);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//针对每一辆车统计计算
	private static void cul(int carID) {
		String sql="select UtcTime from MatchedTraj20090907 where Type=1 and VehicleID="+carID+" order by UtcTime;" ;
//		System.out.println(sql);
		double  time1=0;
		double  time2=0;
		try(Statement stm=con.createStatement();
				ResultSet rs=stm.executeQuery(sql);) {
			while(rs.next()){
				time1=rs.getBigDecimal("UtcTime").doubleValue();
				if((time1-time2)<=600){
					
					double gap=time1-time2;
					int t=(int)gap/10;
					switch (t) {
					case 0:
						a[0]++;
						break;
					case 1:
						a[1]++;
						break;
					case 2:
						a[2]++;
						break;
					case 3:
						a[3]++;
						break;
					case 4:
						a[4]++;
						break;
					case 5:
						a[5]++;
						break;
					case 6:
						a[6]++;
						break;
					case 7:
						a[7]++;
						break;
					case 8:
						a[8]++;
						break;
					case 9:
						a[9]++;
						break;
					case 10:
						a[10]++;
						break;
					case 11:
						a[11]++;
						break;
					default:
						a[12]++;
						break;
					}
					sum_count++;
					sum_time+=gap;
				}/*else
					System.out.println(time1);*/
				time2=time1;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
