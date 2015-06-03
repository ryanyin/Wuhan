/**
 * 
 */
package Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * @author ryan
 *
 */
public class Test02 {

	/**
	 * @param args
	 */
	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		ArrayList<Float> speed=new ArrayList<Float>();
		speed.add((float) 0.2);
		speed.add((float) 0.3);
		speed.add((float) 0.1);
		speed.add((float) 0.02);
		speed.add((float) 0.6);
		speed.add((float) 0.24);
		speed.add((float) 0.9);
		speed.add((float) 0.2);
		speed.add((float) 0.6);
		speed.add((float) 0.27);
		//speed.add((float) 0.1);
		System.out.println(speed.toString());
		Collections.sort(speed, new Comparator(){

			@Override
			public int compare(Object arg0, Object arg1) {
				// TODO Auto-generated method stub
				Float a=(Float)arg0;
				Float b=(Float)arg1;
				
				return a.compareTo(b);
			}
			
		});
		System.out.println(speed.toString());
		if(speed.size()%2==0){
			float a=speed.get((speed.size())/2).floatValue();
			float b=speed.get((speed.size())/2-1).floatValue();
			float c=(a+b)/2;
			System.out.println(c);
		}else{
			System.out.println(speed.get((speed.size()-1)/2));
		}
	}

}
