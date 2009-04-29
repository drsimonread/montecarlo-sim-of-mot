package edu.smcm.physics.mot;

import java.io.Serializable;
import java.util.ArrayList;

public class Particle implements Serializable{
	private static final long serialVersionUID = 1L;
	private ArrayList<Double> v;
	private ArrayList<Double> p;
	private double velocity;
	private double position;
	
	public Particle (){
		v = new ArrayList<Double>();
		p = new ArrayList<Double>();

	}
	
	public void addParticle(double velocity, double position){
		this.velocity = velocity;
		v.add(velocity);
		this.position = position;
		p.add(position);
	}
	
	public double getVelocity(){
		return v.get(0);
	}
	
	public double getPosition(){
		return p.get(1);
	}
}
