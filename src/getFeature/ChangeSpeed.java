/**
 * 
 */
package getFeature;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;

import entity.ConnectSql;
import entity.RoadSpeedEntity;

/**
 * @author ryan
 * ���ٶ��в�����Ҫ��ĸ������
 * �����ı�׼��0��15��2 �ٶ�������100���� ���ֱ��滻Ϊ40 50 55
 *         3��4 �ٶ�������120 ����  �ֱ��滻Ϊ65 70   �Լ��ٶ�Ϊ0��
 *         ��Ҫ����׼��1��Ϊ0
 */
public class ChangeSpeed {

	/**
	 * @param args
	 */
	private Connection con;
	
	public ChangeSpeed() {
		super();
		// TODO Auto-generated constructor stub
		con=ConnectSql.Connect();
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ChangeSpeed c=new ChangeSpeed();
		//c.getspeed();
		for(int i=0;i<12;i++){
			System.out.println(i+"������ "+new Date());
			switch (i) {
			case 0:
				c.byPATHCLASS("for05",i);
				break;
			case 1:
				c.byPATHCLASS("for10",i);
				break;
			case 2:
				c.byPATHCLASS("for15",i);
				break;
			case 3:
				c.byPATHCLASS("for20",i);
				break;
			case 4:
				c.byPATHCLASS("for25",i);
				break;
			case 5:
				c.byPATHCLASS("for30",i);
				break;
			case 6:
				c.byPATHCLASS("for35",i);
				break;
			case 7:
				c.byPATHCLASS("for40",i);
				break;
			case 8:
				c.byPATHCLASS("for45",i);
				break;
			case 9:
				c.byPATHCLASS("for50",i);
				break;
			case 10:
				c.byPATHCLASS("for55",i);
				break;
			case 11:
				c.byPATHCLASS("for60",i);
				break;
			default:
				break;
			}
		}
		System.out.println(new Date());
	}

	public void byPATHCLASS(String for00, int count){
		for(int i=0;i<5;i++){
			if(i==1){
				getspeed(for00, 15,count);
			}else
				getspeed(for00, i,count);	
		}
	}
	public void getspeed(String for00,int PATHCLASS, int count ){
		int limit=0;
		if(PATHCLASS==3||PATHCLASS==4){
			limit=120;
		}else
			limit=100;
		//String sql="select * from RoadAllSpeed00 where "+for00+">"+limit+" and PATHCLASS="+PATHCLASS+" order by RoadID;"; //�ٶ�����
		String sql="select * from RoadAllSpeed00 where "+for00+"=0 and PATHCLASS="+PATHCLASS+" order by RoadID;"; //�ٶ�Ϊ0
		System.out.println("----"+sql);
		try (Statement stm = con.createStatement();
				ResultSet rs = stm.executeQuery(sql);){
			RoadSpeedEntity r=new RoadSpeedEntity();
			while(rs.next()){
				r.setRoadID(rs.getInt("RoadID"));
				int leve=rs.getInt("PATHCLASS");
				r.setTime(rs.getInt("Time"));
				String flag=rs.getString("flag");
				char[] c=flag.toCharArray();
				if(c[count]=='1'){
					c[count]='0';
				}else{
					System.out.println("��׼���ִ��󣡣���");
				}
				r.setFlag(String.valueOf(c));
				float speed=0;  //��Ҫ���µ��ٶ�
				switch (leve) {
				case 0:
					speed=40;
					break;
				case 15:
					speed=50;
					break;
				case 2:
					speed=55;
					break;
				case 3:
					speed=65;
					break;
				case 4:
					speed=70;
					break;
				default:
					break;
				}
				r.setPATHCLASS(leve);
				r.setFor00(speed);
				updatespeed(for00,r);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	private void updatespeed(String for00, RoadSpeedEntity r) {
		// TODO Auto-generated method stub
		String sql = "update RoadAllSpeed00 set "+for00+"=" + r.getFor00()
				+ ",flag='" + r.getFlag() + "' where RoadID=" + r.getRoadID()
				+ " and Time=" + r.getTime() + " and PATHCLASS="
				+ r.getPATHCLASS() + ";"; 
		System.out.println(for00+"~~~~"+sql);
		try (Statement stm = con.createStatement();){
			int flag=stm.executeUpdate(sql);
			if(flag==0){
				System.out.println(sql);
				System.out.println("���³��ִ���");
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
}
