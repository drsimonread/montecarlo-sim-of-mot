import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

public class SimulationData implements Serializable, Iterable<Double>{
	private static final long serialVersionUID = 1L;
	private double frequency;
	private ArrayList<Double> velocities;
	
	public SimulationData(Double detuning){
		this.frequency = detuning;
		this.velocities = new ArrayList<Double>();
	}
	
	public void addVelocity(double velocity){
		velocities.add(velocity);
	}
	
	public double mean(){
		double total;
		
		total = 0.0;
		for (Double velocity : velocities){
			total = total + velocity;
		}
		return total / velocities.size();
	}
	
	public double frequency(){
		return this.frequency;
	}
	
	public int size(){
		return velocities.size();
	}
	
	public double getValue(int index){
		return velocities.get(index);
	}
	
	public Iterator<Double> iterator(){
		return velocities.iterator(); 
	}
	
	public void sort(){
		Collections.sort(velocities);
	}
	
	public int binarySearch(double t){
		int i = Collections.binarySearch(velocities, t); 
		if (i == 0){
			return i;
		}else{
			return Math.abs(i)-1;
		}
	}
	
	public double[] toArray(){
		double[] temp = new double[velocities.size()];
		for(int i = 0; i < velocities.size(); i++){
			temp[i] = velocities.get(i);
		}
		return temp;
	}
}
