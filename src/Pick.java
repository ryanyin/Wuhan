import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class Pick {
 
	private int  tra=0;
	private int  num=0;
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("开始了");
     Pick p=new Pick();
    p.cul();
     //p.intoZaike0901(11214);
     System.out.println("结束啦");
	}
	
	
	
	
	
	
//计算每次载客
	
	public void cul()
	{
		ConnectSql n=new ConnectSql();
		Connection con=n.Connect();
		
		Statement stm=null;
		ResultSet rs=null;
		
		try {
			stm=con.createStatement();
			String sql="select distinct VehicleID from MatchedTraj20090901;";
			rs=stm.executeQuery(sql);
			while(rs.next())
			{
				num=0;
				tra+=1;
				int vec=rs.getInt(1);
				intoZaike0901(vec);
				System.out.println("已计算"+tra+"辆车   "+"最后记录   "+vec+"  总共的载客次数为："+num);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally
		{
			
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
	
	public void intoZaike0901(int vec)
	{
		ConnectSql n=new ConnectSql();
		Connection con=n.Connect();
		
		Statement stm=null;
		ResultSet rs=null;
		
		try {
			stm=con.createStatement();
			String sql="select *  from MatchedTraj20090901 where VehicleID="+vec+"  order by UtcTime;";
			rs=stm.executeQuery(sql);
			Data[] data=new Data[2];
			data[0]=new Data();
			data[1]=new Data();
			int flag=0;  //过两次
			int test=0;  //用于记录载客最开始的时间
			BigDecimal begin=null; //记录载客最开始的时间
			double sumtime=0;
			while(rs.next())
			{
				data[0].setTemValue(rs.getBigDecimal("TemValue").intValue());  //载客标识
				data[0].setUtcTime(rs.getBigDecimal("UtcTime"));
				
				if(flag!=0)
				{
					
					double dis=data[0].getUtcTime().subtract(data[1].getUtcTime()).doubleValue(); //获取两次间隔时间
					//System.out.println(sumtime+"  "+dis+"  "+begin);
					if(data[1].getTemValue()==1&&test==0)
					{
						begin=data[1].getUtcTime();
						test=1;
					}
					
					
					if(dis<=180)   //两次记录连续
					{
						if(data[1].getTemValue()==1)  //后标识为1
						{
							sumtime+=dis;
							if(data[0].getTemValue()==0||rs.isAfterLast())  //前标识为0  后标识为1  且运行时间大于两分钟 或这是最后的记录
							{
								//插入数据库
								if(sumtime>=120)
								{
									finishinsert(vec,begin,data[0].getUtcTime(),sumtime);
									num+=1;
									sumtime=0;
									test=0;
								}else
									{
									sumtime=0;  //重新再来
									test=0;
									}
							}
						}
					}else   //两次记录不连续，处理当前数据
					{
						if(sumtime>=120)
						{
							finishinsert(vec,begin,data[0].getUtcTime(),sumtime);
							num+=1;
							sumtime=0;
							test=0;
						}else
						{
							sumtime=0;  //重新再来
							test=0;
							}
					}
					
				}
				
				data[1].setTemValue(data[0].getTemValue());
				data[1].setUtcTime(data[0].getUtcTime());
				flag=1;
				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally
		{
			
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
	
	//实现插入  v,s,e,t 分别代表车标号 开始时间 截止时间 所用时间(分钟)
	public void finishinsert(int v,BigDecimal s,BigDecimal e,double t){
		ConnectSql n=new ConnectSql();
		Connection con=n.Connect();
		
		Statement stm=null;
		
		try {
			stm=con.createStatement();
			String sql="insert into zaike0901 VALUES("+ v+", "+s+", "+e+", "+t+")";
			int flag=stm.executeUpdate(sql);
			if(flag==0)
			{
				System.out.println("插入出错啦");
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
			try {
				if(stm!=null)
					 stm.close();
				if(con!=null)
					con.close();
				
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		
	}
	
}
