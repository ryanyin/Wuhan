import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;


public class Desend2 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println(new Date());
		Desend2 de=new Desend2();
		for(int i=1;i<=2;i++){
			switch (i) {
			case 1:
				System.out.println("开始90秒降频");
				de.forcoun(90,"fortra20190");
				break;
			case 2:
				System.out.println("开始180秒降频");
				de.forcoun(180,"fortra201180");
				break;
			/*case 3:
				System.out.println("开始100秒降频");
				de.forcoun(100,"fortra201100");
				break;
			case 4:
				System.out.println("开始120秒降频");
				de.forcoun(120,"fortra201120");
				break;
			case 5:
				System.out.println("开始150秒降频");
				de.forcoun(150,"fortra201150");
				break;*/
			default:
				break;
			}
		}		
		System.out.println(new Date());
	/*	System.out.println("开始整个路网的降频");
		System.out.println(new Date());
		Desend3 des=new Desend3();
		des.cul();
		System.out.println(new Date());*/
	}
	public void forcoun(int time, String tablename){
		int count=1;
		while(count<=5939){
			//System.out.println("当前  "+count);
			desend(count,time,tablename);
			count++;
			
		}
	}
	public void desend(int count,int time, String tablename){
		ConnectSql n = new ConnectSql();		 
		String sql = "select VehicleID,UtcTime,TemValue from fortra201 where count="
				+ count + " order by UtcTime; ";
		Data first=new Data();
		BigDecimal t1=new BigDecimal(-1);
		try (Connection con = n.Connect();
				Statement stm=con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
				ResultSet rs=stm.executeQuery(sql)){
			while(rs.next()){
				first.setVehicleID(rs.getInt("VehicleID"));
				first.setUtcTime(rs.getBigDecimal("UtcTime"));
				first.setTemValue(rs.getBigDecimal("TemValue").intValue());
								
				if(t1.doubleValue()>0){
					if(first.getUtcTime().subtract(t1).doubleValue()<time){
						//如果rs游标不是最后一个first载客状态为1的记录
						boolean flag=rs.isLast()&&first.getTemValue()==1;
						if(!flag){
							delete(first,tablename);
						}						
					}else{
						t1=first.getUtcTime();
					}
				}
				
				if(t1.doubleValue()<0){
					t1=first.getUtcTime();
				}
				
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void delete(Data first, String tablename) {
		// TODO Auto-generated method stub
		ConnectSql n = new ConnectSql();		 
		String sql = "delete from "+tablename+" where VehicleID="+first.getVehicleID()
				+" and UtcTime="+first.getUtcTime()+";";
		try (Connection con = n.Connect();
				Statement stm=con.createStatement()){
			
			int flag=stm.executeUpdate(sql);
			if(flag==0){
				System.out.println(sql);
				System.out.println("删除出现错误");
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
