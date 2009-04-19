package edu.smcm.physics.mot;

import java.util.ArrayList;

public class Particle {
	private double velocity;
	private double position;
	private ArrayList<Double> thisParticle; 
	private ArrayList<ArrayList<Double>> particle;
	
	public Particle (){
		this.thisParticle = new ArrayList<Double>();
		this.particle = new ArrayList<ArrayList<Double>>();
	}
	
	public ArrayList<Double> getParticle(int index){
		return particle.get(index);
	}
	
	public double getVelocity(){
		return this.velocity;
	}
	
	public double getPosition(){
		return this.position;
	}
	
	private ArrayList<Double> addThisParticle(double velocity, double position){
		thisParticle.add(velocity);
		thisParticle.add(position);
		
		return thisParticle;
	}
	
	public void addParticle(double velocity, double position){
		this.velocity = velocity;
		this.position = position;
		
		particle.add(addThisParticle(velocity, position));
	}

	public int size() {
		return particle.size();
	}

}
