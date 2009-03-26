package edu.smcm.physics.mot;
import java.util.ArrayList;

public class Temperature {
	
	static final double mass = 87.4678 * 1.661E-27;
	static final double boltzman = 1.3806503E-23;
	private double stdDev;
	private ArrayList<Double> tempList;

	public Temperature(){
		this.tempList = new ArrayList<Double>();
	}
	
	public void addTemperature(double stdDev){
		this.stdDev = stdDev;
		tempList.add(T_calc());
	}
	
	public void addKnownTemperature(double temperature){
		stdDev = Equations.V_calc(temperature);
		tempList.add(temperature);
	}
	
	public double getTemperature(){
		return this.T_calc();
	}
	
	public int size(){
		return tempList.size();
	}
	
	public double get(int index){
		return tempList.get(index);
	}
	
	public double T_calc() {
		/**
		 *  Calculates the temperature given the standard deviation of the velocities of the atoms.
		 */
		return DataAnalysis.square(stdDev) * mass / (2 * boltzman);
	}
}
