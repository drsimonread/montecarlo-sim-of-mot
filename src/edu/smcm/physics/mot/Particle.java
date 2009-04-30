package edu.smcm.physics.mot;

import java.io.Serializable;
import java.util.ArrayList;

public class Particle implements Serializable, Comparable<Particle> {
	private static final long serialVersionUID = 1L;
	private double velocity;
	private double position;
	
	public Particle (double velocity, double position){
		this.velocity = velocity;
		this.position = position;
	}
	
	public double velocity(){
		return velocity;
	}
	
	public double position(){
		return position;
	}
	
	// TODO This worries me.... equality of reals?
	public int compareTo(Particle that)
	{
		return Double.compare(this.velocity(), that.velocity());
	}
}
