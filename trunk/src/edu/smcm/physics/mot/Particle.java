package edu.smcm.physics.mot;

import java.io.Serializable;
import java.util.ArrayList;

public class Particle implements Serializable{
	private static final long serialVersionUID = 1L;
	private double velocity;
	private double position;
	private double[] thisParticle; 
	private ArrayList<double[]> particle;
	
	public Particle (){
		this.thisParticle = new double[2];
		this.particle = new ArrayList<double[]>();
	}
	
	public double[] getParticle(int index){
		return particle.get(index);
	}
	
	public double getVelocity(int index){
		double p[] = particle.get(index);
		return p[0];
	}
	
	public double getPosition(int index){
		double p[] = particle.get(index);
		return p[1];
	}
	
	private double[] addThisParticle(){
		thisParticle[0] = velocity;
		thisParticle[1] = position;
		
		return thisParticle;
	}
	
	public void addParticle(double velocity, double position){
		this.velocity = velocity;
		this.position = position;
		
		particle.add(addThisParticle());
	}

	public int size() {
		return particle.size();
	}

}
