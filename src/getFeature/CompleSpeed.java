package getFeature;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;

import entity.ConnectSql;

public class CompleSpeed {
	private Connection con;
	
	public CompleSpeed() {
		super();
		// TODO Auto-generated constructor stub
		con=ConnectSql.Connect();
		
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		CompleSpeed com=new CompleSpeed();
		com.foreveryhour(7);
		try {
			com.con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void foreveryhour(int hour) {
		for(int i=0;i<5;i++){
			System.out.println("--------"+i);
			if(i==1)
				forlever(15,hour);
			else
				forlever(i,hour);
		}
	}

	private void forlever(int i, int hour) {
		// TODO Auto-generated method stub
		String  sql="select * from RoadAllSpeed00 where flag is not null and PATHCLASS="+i+";";
		System.out.println(sql);
		ArrayList<Float> speed00=new ArrayList<Float>();
		float sum=0;
		int limte=0;
		switch (i) {
		case 0:
			limte=100;
			break;
		case 15:
			limte=100;
			break;
		case 2:
			limte=100;
			break;
		case 3:
			limte=120;
			break;
		case 4:
			limte=120;
			break;
		default:
			break;
		}
		try (Statement stm = con.createStatement();
				ResultSet rs = stm.executeQuery(sql);){
			while(rs.next()){
				float for05=rs.getFloat("for05");
				float for10=rs.getFloat("for10");
				float for15=rs.getFloat("for15");
				float for20=rs.getFloat("for20");
				float for25=rs.getFloat("for25");
				float for30=rs.getFloat("for30");
				float for35=rs.getFloat("for35");
				float for40=rs.getFloat("for40");
				float for45=rs.getFloat("for45");
				float for50=rs.getFloat("for50");
				float for55=rs.getFloat("for55");
				float for60=rs.getFloat("for60");
				if(for05>0&&for05<limte){
					speed00.add(for05);
					sum+=for05;
				}			
				if(for10>0&&for10<limte){
					speed00.add(for10);
					sum+=for10;
				}
				if(for15>0&&for15<limte){
					speed00.add(for15);
					sum+=for15;
				}
				if(for20>0&&for20<limte){
					speed00.add(for20);
					sum+=for20;
				}
				if(for25>0&&for25<limte){
					speed00.add(for25);
					sum+=for25;
				}
				if(for30>0&&for30<limte){
					speed00.add(for30);
					sum+=for30;
				}
				if(for35>0&&for35<limte){
					speed00.add(for35);
					sum+=for35;
				}
				if(for40>0&&for40<limte){
					speed00.add(for40);
					sum+=for40;
				}
				if(for45>0&&for45<limte){
					speed00.add(for45);
					sum+=for45;
				}
				if(for50>0&&for50<limte){
					speed00.add(for50);
					sum+=for50;
				}
				if(for55>0&&for55<limte){
					speed00.add(for55);
					sum+=for55;
				}
				if(for60>0&&for60<limte){
					speed00.add(for60);
					sum+=for60;
				}
			}
			
			float speed=getmiddle(speed00);
			
			//float speed2=sum/speed00.size();
			//updatenullspeed(speed,i);
			//System.out.println(speed+" "+speed00.size());
			
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	private void updatenullspeed(float speed, int i) {
		// TODO Auto-generated method stub	
		for(int a=1;a<=12;a++){
			String tab="";
			if(a==1)
				tab="05";
			else
				tab=tab+(a*5);
			String  sql="update RoadAllSpeed07 set for"+tab+"="+speed+" where for"+tab+" is null and PATHCLASS="+i+";";
			System.out.println(sql);
			try (Statement stm=con.createStatement()){
				
				/*int e=stm.executeUpdate(sql);
				if(e==0){
					System.out.println(sql);
					System.out.println("更新出现错误");
				}	*/
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				System.out.println(sql);
				e.printStackTrace();
			}	
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
			/*if(speed.size()%2==0){
				float a=speed.get((speed.size())/2).floatValue();
				float b=speed.get((speed.size())/2-1).floatValue();
				 result=(a+b)/2;
			}else{
				result=speed.get((speed.size()-1)/2);
			}
			if(result>100)
				result=-1;*/
			
			//return result;
			int number=0;
			float min=speed.get(0);
			int x=speed.size();
			float max=speed.get(x-1);
			float delta=(max-min)/100;
			int k=0;
			for(int a=0;a<100;a++){
				//System.out.println("1"+" "+k);
				float curspeed=min+a*delta;
				while(speed.get(k)<curspeed){
					k++;
				}
				System.out.println(k+","+speed.get(k));
			}
			System.out.println(x+","+max);
			for(int a=1;a<10;a++){
				number=(int) (speed.size()*0.1*a);
				//System.out.println(number+","+speed.get(number));
			}
			
			return speed.get(number);
		}
	}
}
