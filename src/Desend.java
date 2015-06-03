import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;


public class Desend {

	private int count=0;
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println(new Date());
		Desend d=new Desend();
		for(int i=1;i<=255;i++){
			d.getnum(i);
		}
		System.out.println("最后的结果为+"+d.count);
		System.out.println(new Date());
	}

	public void getnum(int a){
		ConnectSql n = new ConnectSql();
		Connection con = n.Connect();

		Statement stm = null;
		ResultSet rs = null;
		try {
			stm = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
			String sql ="select * from forslow where Id="+a+" order by UtcTime";
			rs=stm.executeQuery(sql);
			BigDecimal x=new BigDecimal(0);
			BigDecimal y=new BigDecimal(0);
			if(rs.next()){
				x=rs.getBigDecimal("UtcTime");
			}
			boolean b=rs.absolute(-2);   //倒数第二行
			if(b){
				y=rs.getBigDecimal("UtcTime");
				if(y.subtract(x).doubleValue()<100){
					count+=1;
				}
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
}
