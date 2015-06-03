package getFeature;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import entity.ConnectSql;



	public class RoadSpeed {
	
	private Connection con;
	
	public RoadSpeed() {
		super();
		// TODO Auto-generated constructor stub
		con=ConnectSql.Connect();
	}


	public static void main(String[] args) {
		// TODO Auto-generated method stub
		RoadSpeed r=new RoadSpeed();
		
		double start=1251788400.000000;
		for(int i=0;i<12;i++){
			System.out.println(i+"次运行 "+ start+" "+new Date());
			switch (i) {
			case 0:
				//r.getroad("RoadAllSpeed07",start);
				break;
			case 1:
				//r.getroad("RoadAllSpeed08",start);
				break;
			case 2:
				//r.getroad("RoadAllSpeed09",start);
				break;
			case 3:
				//r.getroad("RoadAllSpeed10",start);
				break;
			case 4:
				r.getroad("RoadAllSpeed11",start);
				break;
			case 5:
				//r.getroad("RoadAllSpeed12",start);
				break;
			case 6:
				//r.getroad("RoadAllSpeed13",start);
				break;
			case 7:
				//r.getroad("RoadAllSpeed14",start);
				break;
			case 8:
				//r.getroad("RoadAllSpeed15",start);
				break;
			case 9:
				//r.getroad("RoadAllSpeed16",start);
				break;
			case 10:
				//r.getroad("RoadAllSpeed17",start);
				break;
			case 11:
				//r.getroad("RoadAllSpeed18",start);
				break;
			default:
				break;
			}
			start+=3600;
		}
		try {
			if(r.con!=null)
				r.con.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("结束运行 "+new Date());
	}

	
	//7点到8点的所有有记录的道路
	public void getroad(String tablename, double start){
		
		//String sql = "select distinct RoadID from feature_speed_all where UtcTime>="+start+" and UtcTime<"+(start+3600)+"  order by RoadID;";
		String sql = "select distinct RoadID from feature_speed_all where UtcTime>="+start+" and UtcTime<"+(start+3600)+" and RoadID=910 order by RoadID;";
		System.out.println(sql);
		try (
				Statement stm = con.createStatement();
				ResultSet rs = stm.executeQuery(sql);) {
			while(rs.next()){
				int roadid=rs.getInt("RoadID");
				getRoadspeed(roadid,tablename,start);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	
	private void getRoadspeed(int roadid, String tablename, double start) {
		// TODO Auto-generated method stub

		String sql = "select VehicleID,UtcTime,newspeed from feature_speed_all where RoadID ="
				+ roadid
				+ " and UtcTime>="+start+" and UtcTime<"+(start+3600)+" order by UtcTime";
		ArrayList<Float> speed00=new ArrayList<Float>();
		ArrayList<Float> speed05=new ArrayList<Float>();
		ArrayList<Float> speed10=new ArrayList<Float>();
		ArrayList<Float> speed15=new ArrayList<Float>();
		ArrayList<Float> speed20=new ArrayList<Float>();
		ArrayList<Float> speed25=new ArrayList<Float>();
		ArrayList<Float> speed30=new ArrayList<Float>();
		ArrayList<Float> speed35=new ArrayList<Float>();
		ArrayList<Float> speed40=new ArrayList<Float>();
		ArrayList<Float> speed45=new ArrayList<Float>();
		ArrayList<Float> speed50=new ArrayList<Float>();
		ArrayList<Float> speed55=new ArrayList<Float>();
		float[] speed=new float[12];
		char[] flag=new char[12];
		try (
				Statement stm = con.createStatement();
				ResultSet rs = stm.executeQuery(sql);) {
			while(rs.next()){
				int a=(int) ((rs.getBigDecimal("UtcTime").doubleValue()-start)/300);
				switch (a) {
				case 0:
					speed00.add(rs.getFloat("newspeed"));
					break;
				case 1:
					speed05.add(rs.getFloat("newspeed"));
					break;
				case 2:
					speed10.add(rs.getFloat("newspeed"));
					break;
				case 3:
					speed15.add(rs.getFloat("newspeed"));
					break;
				case 4:
					speed20.add(rs.getFloat("newspeed"));
					break;
				case 5:
					speed25.add(rs.getFloat("newspeed"));
					break;
				case 6:
					speed30.add(rs.getFloat("newspeed"));
					break;
				case 7:
					speed35.add(rs.getFloat("newspeed"));
					break;
				case 8:
					speed40.add(rs.getFloat("newspeed"));
					break;
				case 9:
					speed45.add(rs.getFloat("newspeed"));
					break;
				case 10:
					speed50.add(rs.getFloat("newspeed"));
					break;
				case 11:
					speed55.add(rs.getFloat("newspeed"));
					break;
				default:
					break;
				}
			}
			for(int i=0;i<12;i++){
				switch (i) {
				case 0:
					speed[0]=getmiddle(speed00);
					flag[0]=getflag(speed[0]);  //没有速度的为0
					break;
				case 1:
					speed[1]=getmiddle(speed05);
					flag[1]=getflag(speed[1]);  //没有速度的为0
					break;
				case 2:
					speed[2]=getmiddle(speed10);
					flag[2]=getflag(speed[2]);  //没有速度的为0
					break;
				case 3:
					speed[3]=getmiddle(speed15);
					flag[3]=getflag(speed[3]);  //没有速度的为0
					break;
				case 4:
					speed[4]=getmiddle(speed20);
					flag[4]=getflag(speed[4]);  //没有速度的为0
					break;
				case 5:
					speed[5]=getmiddle(speed25);
					flag[5]=getflag(speed[5]);  //没有速度的为0
					break;
				case 6:
					speed[6]=getmiddle(speed30);
					flag[6]=getflag(speed[6]);  //没有速度的为0
					break;
				case 7:
					speed[7]=getmiddle(speed35);
					flag[7]=getflag(speed[7]);  //没有速度的为0
					break;
				case 8:
					speed[8]=getmiddle(speed40);
					flag[8]=getflag(speed[8]);  //没有速度的为0
					break;
				case 9:
					speed[9]=getmiddle(speed45);
					flag[9]=getflag(speed[9]);  //没有速度的为0
					break;
				case 10:
					speed[10]=getmiddle(speed50);
					flag[10]=getflag(speed[10]);  //没有速度的为0
					break;
				case 11:
					speed[11]=getmiddle(speed55);
					flag[11]=getflag(speed[11]);  //没有速度的为0
					break;
				default:
					break;
				}
				
			}
			inserttoroad(roadid,speed,flag,tablename);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void inserttoroad(int roadid, float[] speed, char[] flag, String tablename) {
		// TODO Auto-generated method stub
		String f=new String(flag);
		String sql = "update "+tablename+" set for05=" + speed[0]
				+ " , for10=" + speed[1] + " , for15=" + speed[2]
				+ " , for20=" + speed[3] + " , for25=" + speed[4]
				+ " , for30=" + speed[5] + " , for35=" + speed[6]
				+ " , for40=" + speed[7] + " , for45=" + speed[8]
				+ " , for50=" + speed[9] + " , for55=" + speed[10]
				+ " , for60=" + speed[11]+" , flag='"+f+"' where RoadID="+roadid+";";
		try (Statement stm=con.createStatement()){
			System.out.println(sql);
			/*int e=stm.executeUpdate(sql);
			if(e==0){
				System.out.println(sql);
				System.out.println("更新出现错误");
			}*/
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println(sql);
			e.printStackTrace();
		}
	}


	@SuppressWarnings("unchecked")
	private float getmiddle(ArrayList<Float> speed){
		float result;
		if(speed.isEmpty())
			return -1;  //没有速度
		else{
			Collections.sort(speed, new Comparator(){

				@Override
				public int compare(Object arg0, Object arg1) {
					// TODO Auto-generated method stub
					Float a=(Float)arg0;
					Float b=(Float)arg1;
					
					return a.compareTo(b);
				}
				
			});
			if(speed.size()%2==0){
				float a=speed.get((speed.size())/2).floatValue();
				float b=speed.get((speed.size())/2-1).floatValue();
				 result=(a+b)/2;
			}else{
				result=speed.get((speed.size()-1)/2);
			}
		/*	if(result>100)
				result=-1;*/
			return result;
		}
	}

	private char getflag(float a){  //没有速度的为0
		if(a==-1)
			return '0';
		else
			return '1';
	}


}
