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
	private ArrayList<Particle> particles;
	private Particle p;
	
	public SimulationData(Double detuning, Double initial_temp){
		this.frequency = detuning;
		this.temperature = initial_temp;
		this.velocities = new ArrayList<Double>();
		this.particles = new ArrayList<Particle>();
		p = new Particle();
	}
	
	public void addVelocity(double velocity){
		p.addParticle(velocity, 0.0);
		velocities.add(velocity);
	}
	
	public void addParticle(double velocity, double position){
		p.addParticle(velocity, position);
		particles.add(p);
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
		return particles.size();
	}
	
	public double getVelocity(int index){
		return particles.get(index).getVelocity();
	}
	
	public double getPosition(int index){
		return particles.get(index).getPosition();
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
	
	public double[] toArray_velocity(){
		double temp[] = new double[particles.size()];
		
		for (int i = 0; i < particles.size(); i++){ 
			temp[i] = particles.get(i).getVelocity();
		}
		return temp;
	}
	
	public double[][] toArray_position(){
			System.out.println(particles.size());
			double temp[][] = new double[2][particles.size()];
			double p;
			double max = 0,  min = 0;
			
			for(int i = 0; i < particles.size(); i++){
				p = particles.get(i).getPosition();	
				if (min > p){
					min = p;
				}
				
				if (max < p){
					max = p;
				}
			}
			
			double step = (max - min)/particles.size();
			
			for(int i = 0; i < particles.size(); i++){
				p = particles.get(i).getPosition();
				temp[0][i] = min + step * i;
				temp[1][i] = p;
				System.out.println("pos = " + temp[1][i] + ", vel = " + temp[0][i]);
			}
			return temp;
		
	}
}
