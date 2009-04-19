package edu.smcm.physics.mot;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

public class SimulationData implements Serializable, Iterable<Double>{
	private static final long serialVersionUID = 1L;
	private double frequency;
	private double temperature;
	private ArrayList<Double> velocities;
	private Particle particle;
	
	public SimulationData(Double detuning, Double initial_temp){
		this.frequency = detuning;
		this.temperature = initial_temp;
		this.velocities = new ArrayList<Double>();
	}
	
	public void addVelocity(double velocity){
		velocities.add(velocity);
	}
	
	public void addParticle(double velocity, double position){
		particle.addParticle(velocity, position);
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
		return particle.size();
	}
	
	public double getVelocity(int index){
		ArrayList<Double> temp  = new ArrayList<Double>();
		temp = particle.getParticle(index);
		return temp.get(0);
	}
	
	public double getPosition(int index){
		ArrayList<Double> temp  = new ArrayList<Double>();
		temp = particle.getParticle(index);
		return temp.get(1);
	}
	
	public Iterator<Double> iterator(){
		return velocities.iterator(); 
	}
	
	public void sort(){
		Collections.sort(velocities);
	}
	
	public double getVelocity(){
		return PositionInsensitiveDataGeneration.V_calc(temperature);
	}
	
	public double getAmplitude(){
		double amp;
		int size = velocities.size();
		double[] arr = new double[size];
		
		for (int i = 0; i < size; i++){
			arr[i] = velocities.get(i);
		}
		
		amp = DataAnalysis.max(DataAnalysis.binnedData(arr, size)[1]);
		
		return amp;
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
