package edu.smcm.physics.mot;
import edu.smcm.physics.mot.MyRandom;
import edu.smcm.physics.mot.SimulationData;
import edu.smcm.physics.mot.Temperature;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.lang.Math;
import java.util.ArrayList;
import java.util.Calendar;

public class PositionInsensitiveDataGeneration{//Equations{
	static final double Lambda_not = 780.24 * Math.pow(10, -9);
	static final double k = 2 * Math.PI / Lambda_not;
	static final double h_bar = 6.6260657E-34/ (2 * Math.PI);
	static final double Gamma = 6e6;//2 * Math.PI * 3E6;// 1 / tau;
//	static final double delta = -3e6;//(-1) * Gamma / 2;
	static final double I_Isat = 0.5;
	static final double mass = 87.4678 * 1.661E-27;
	static final double boltzman = 1.3806503E-23;
	static final double term1 = h_bar * k * Gamma / 2; 
	static double delta; 
	static MyRandom rand = new MyRandom();

	public static void main(String[] args){
		Configurations configs;
		SimulationData data;
		ObjectOutputStream file;
		ArrayList<Double> velocities = new ArrayList<Double>();
		Temperature initial_temp = new Temperature();
		initial_temp.addKnownTemperature(1);
		double initialVelocity;//initial_temp = 1, 	//units of Kelvin, then meters per second.
		long start, end;
		double time;
		start = System.nanoTime();
		Calendar rightNow = Calendar.getInstance();
		long timeStamp = rightNow.getTimeInMillis();
		double step = 0.5e6;
		configs = new Configurations();
		
		
//		for(int s = 0; s < 4; s++ ){
			int pt = 100000;//(int) (1 * Math.pow(10, 2 + s));
//			System.out.println("Starting configuration " + s + ".");
			delta = -3e6;//(0.5e6 + (s * step));
			data = new SimulationData(delta, initial_temp.getTemperature());
			for(int i = 0; i < pt; i++){
				initialVelocity = rand.nextGaussian(0, V_calc(initial_temp.getTemperature()));
				velocities.add(initialVelocity);
				
				for(int j = 0; j < 30000; j++){
					initialVelocity = senario(initialVelocity);
				}
				
				data.addVelocity(initialVelocity);
			}
			configs.addConfiguration(data);
			
			end = System.nanoTime();
			time =  ((end - start)/ 1e9)/60;
			System.out.println("Run Time:  " + time + ".  Number of atoms " + pt);			
//		}
		


		for (int i = 0; i < configs.size(); i++) {
			data = configs.get(i);
			System.out.println("Configuration " + i + " has " + data.size() + " simulations at a detuning of " + data.frequency() + ".");
			System.out.println("And has an amplitude of " + data.getAmplitude() + "and an initial velocity of " + data.getVelocity() + ".");
		}
		
		try{
			file = new ObjectOutputStream(new FileOutputStream("simulation_data " + timeStamp + ".dat"));
			file.writeObject(configs);
			file.close();
		}catch(IOException caught){
			System.err.println(caught);
		}
	}

	public static double V_calc(double temp) {
		/**
		 *  Calculates the velocity of an atom with a given temperature.
		 */
		return Math.sqrt(2 * boltzman * temp / mass);
	}

	public double F_calc(double velocity, boolean direction) {
		/**
		 *  Calculates the force of an atom given a velocity.
		 *  Also takes into account the Doppler Effect.
		 */
		double force, term2, term2_1, term2_2;
		term2_1 = 1 + I_Isat;
		
		if (direction) {
			term2_2 = 4 * DataAnalysis.square(delta - k * velocity) / DataAnalysis.square(Gamma);
		} else {
			term2_2 = 4 * DataAnalysis.square(delta + k * velocity) / DataAnalysis.square(Gamma);
		}

		term2 = I_Isat / (term2_1 + term2_2);

		if (Math.signum(velocity) != 1) {
			force = -term1 * term2;
		} else {
			force = term1 * term2;
		}
		return force;
	}

	public static double Prob_Absorb(double velocity, boolean direction) {
		/**
		 *  Calculates the probability of absorption of an atom.
		 *  Which should be between 0.0 and 1.0.
		 */
		double term2_1, term2_2;
		term2_1 = 1 + I_Isat;
		if (direction) {
			term2_2 = 4 * DataAnalysis.square(delta - k * velocity) / DataAnalysis.square(Gamma);
		} else {
			term2_2 = 4 * DataAnalysis.square(delta + k * velocity) / DataAnalysis.square(Gamma);
		}
		return (I_Isat / (term2_1 + term2_2));
	}

	public static double Prob_Emit() { 
		/** 
		 * Gives a random double between 0 and PI which is
		 * the probability of the direction of emission.
		 */
		return Math.PI * rand.nextDouble();
	}

	public static double senario(double velocity) {
		double V_photon, P_Absorb_R, P_Absorb_L;
		boolean left, right;
		right = false;
		left = false;
		V_photon = h_bar * k / mass;
		P_Absorb_R = Prob_Absorb(velocity, true);	// computes probability of absorption from the right.
		P_Absorb_L = Prob_Absorb(velocity, false);	// computes the probability of absorption from the left.
		right = P_Absorb_R > rand.nextDouble();		// tests for absorption, from the right.
		left = P_Absorb_L > rand.nextDouble();  	// tests for absorption, from the left.
		
		if (right && left) {
			//handles the case when photons from both beams reach the atom simultaneously.
			right = rand.nextBoolean();
			left = right;
		}
		if (right) {
			//Calculates the change in velocity of the atom from an absorption event from the right.
			velocity = velocity + V_photon;
		}
		if (left){
			//Calculates the change in velocity of the atom from an absorption event from the left.
			velocity = velocity - V_photon;
		}	
		if(left || right){
			velocity = velocity - V_photon*Math.cos(Prob_Emit());
		}
		return velocity;
	}
}