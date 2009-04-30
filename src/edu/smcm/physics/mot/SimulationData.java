package edu.smcm.physics.mot;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

public class SimulationData implements Serializable, Iterable<Particle>{
	private static final long serialVersionUID = 1L;
	private double frequency;
	private double temperature;
	private ArrayList<Particle> particles;
	
	public SimulationData(Double detuning, Double initial_temp){
		this.frequency = detuning;
		this.temperature = initial_temp;
		this.particles = new ArrayList<Particle>();
	}
		
	public void addParticle(Particle particle){
		particles.add(particle);
	}
	
	public double meanVelocity(){
		double total;
		
		total = 0.0;
		for (Particle particle : particles) {
			total = total + particle.velocity();
		}
		return total / particles.size();
	}
	
	public double frequency(){
		return this.frequency;
	}
	
	public int numberOfParticles(){
		return particles.size();
	}
	
	public double getVelocity(int index){
		return particles.get(index).velocity();
	}
	
	public double getPosition(int index){
		return particles.get(index).position();
	}
	
	public Iterator<Particle> iterator(){
		return particles.iterator(); 
	}
	
	public void sort(){
		Collections.sort(particles);
	}
	
	public double getVelocity(){
		return PositionInsensitiveDataGeneration.V_calc(temperature);
	}
	
	public double getAmplitude(){
		double amp;
		double[] arr = new double[numberOfParticles()];
		
		for (int i = 0; i < numberOfParticles(); i++){
			arr[i] = particles.get(i).velocity();
		}
		
		amp = DataAnalysis.max(DataAnalysis.binnedData(arr, numberOfParticles())[1]);
		
		return amp;
	}
	
	// TODO Examine the design associated with this function and KDE
	public int binarySearch(double t){
		assert (false);
		return 0;
	}
	
	public double[] toArray_velocity(){
		double temp[] = new double[particles.size()];
		
		for (int i = 0; i < particles.size(); i++){ 
			temp[i] = particles.get(i).velocity();
		}
		return temp;
	}
	
	public double[][] toArray_position(){
			System.out.println(particles.size());
			double temp[][] = new double[2][particles.size()];
			double p;
			double max = 0,  min = 0;
			
			for(int i = 0; i < particles.size(); i++){
				p = particles.get(i).position();	
				if (min > p){
					min = p;
				}
				
				if (max < p){
					max = p;
				}
			}
			
			double step = (max - min)/particles.size();
			
			for(int i = 0; i < particles.size(); i++){
				p = particles.get(i).position();
				temp[0][i] = min + step * i;
				temp[1][i] = p;
			}
			return temp;
		
	}
}
