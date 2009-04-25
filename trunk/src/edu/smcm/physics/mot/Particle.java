package edu.smcm.physics.mot;

import java.io.Serializable;
import java.util.ArrayList;

public class Particle implements Serializable{
	private static final long serialVersionUID = 1L;
	private double velocity;
	private double position;
	
	public Particle (){

	}
	
	public void addParticle(double velocity, double position){
		this.velocity = velocity;
		this.position = position;
	}
	
	public double getVelocity(){
		return velocity;
	}
	
	public double getPosition(){
		return position;
	}
}
