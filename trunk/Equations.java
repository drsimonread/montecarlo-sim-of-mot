import java.io.File;
import java.io.PrintStream;
import java.lang.Math;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import sun.security.util.BigInt;

public class Equations {
	static final double Lambda_not = 780.24 * Math.pow(10, -9);
	static final double k = 2 * Math.PI / Lambda_not;
	static final double h_bar = 6.6260657E-34/ (2 * Math.PI);
	static final double Gamma = 2 * Math.PI * 6E6;// 1 / tau;
	static final double delta = (-1) * Gamma / 2;
	static final double I_Isat = 2;
	static final double mass = 87.4678 * 1.661E-27;
	static final double boltzman = 1.3806503E-23;
	static final double term1 = h_bar * k * Gamma / 2; 
	int Lcnt, Rcnt;
	MyRandom rand = new MyRandom();

	public void testDistributions() {
		double myVelocity, vel;
		double[] myArr = new double[10000];
		double[] herArr = new double[10000];
		//double x, y, z, a, b;
		Lcnt = Rcnt = 0;
		
		PrintStream datafile;
		try {
			datafile = new PrintStream(new File("data.csv"));
			datafile.println("velocity" + "," + "bin");	
			for(int i = 0; i < 10000; i++){
				myVelocity = rand.nextUniform(-5, 5);
				vel = myVelocity;
				for(int j = 0; j < 30000; j++){
					myVelocity = senario(myVelocity);
//					if (i % 1000 == 0 && j % 10000 == 0){
//						System.out.println(i + ", " + j);
//					}
				}
				//datafile.println(myVelocity);
				myArr[i] = myVelocity;
				herArr[i] = vel;
			}
			datafile.close();
		} catch (Exception caught) {
			System.err.println(caught);
		}
		plots.histogram(herArr, mean(herArr), stdDev(herArr), "Initial Velocity");
		plots.histogram(myArr, stdDev(myArr), mean(myArr), "Final Velocity");
		System.out.println(Lcnt + ", " + Rcnt);
		System.out.println("Final Temp:  " + T_calc(stdDev(myArr)));
	}

	public double V_calc(double temp) {
		// Calculates the velocity of an atom with a given temperature.
		return Math.sqrt(2 * boltzman * temp / mass);
	}

	public double square(double arg) {
		// Squares the arguments.
		return arg * arg;
	}

	public double T_calc(double velocity) {
		// Calculates the temperature given a velocity.
		return square(velocity) * mass / (2 * boltzman);
	}

	public double F_calc(double velocity, boolean direction) {
		// Calculates the force of an atom given a velocity.
		double force, term2, term2_1, term2_2;
		term2_1 = 1 + I_Isat;
		
		if (direction) {
			term2_2 = 4 * square((-Gamma / 2) - k * velocity) / square(Gamma);
		} else {
			term2_2 = 4 * square((-Gamma / 2) + k * velocity) / square(Gamma);
		}

		term2 = I_Isat / (term2_1 + term2_2);

		if (Math.signum(velocity) != 1) {
			force = -term1 * term2;
		} else {
			force = term1 * term2;
		}
		return force;
	}

	public double Prob_Absorb(double velocity, boolean direction) {
		// Calculates the probability of absorption of an atom.
		// Which should be between 0.0 and 1.0.
		double term2_1, term2_2;
		term2_1 = 1 + I_Isat;
		if (direction) {
			term2_2 = 4 * square((-Gamma / 2) - k * velocity) / square(Gamma);
		} else {
			term2_2 = 4 * square((-Gamma / 2) + k * velocity) / square(Gamma);
		}
		return (I_Isat / (term2_1 + term2_2));
	}

	public double Prob_Emit() { 
		// Gives a random double between 0 and 1 which is
		// the probability of the direction of emission.
		return rand.nextDouble();
	}

	public double senario(double velocity) {
		double V_photon, Prob, Rand_prob_R, Rand_prob_L, P_Absorb_R, P_Absorb_L;
		boolean P_emit, left, right;
		right = false;
		left = false;
		V_photon = h_bar * k / mass;
		Prob = rand.nextDouble();
		// computes probability of absorption from the right.
		P_Absorb_R = Prob_Absorb(velocity, true);
		// computes the probability of absorption from the left.
		P_Absorb_L = Prob_Absorb(velocity, false);
		Rand_prob_R = rand.nextDouble();
		// random probability from the right.
		Rand_prob_L = rand.nextDouble();
		// random probability from the left.

		if (P_Absorb_R < Rand_prob_R) {
			// tests for absorption, from the right.
			right = true;
		}
		if (P_Absorb_L < Rand_prob_L) {
			// tests for absorption, from the left.
			left = true;
		}
		if (right && left) {
			//handles the case when photons from both beams reach the atom simultaneously.
			right = Prob > 0.5;
			left = !right;
		}
		if (right) {
			//Calculates the change in velocity of the atom from an absorption event from the right.
			velocity = velocity - V_photon;
			Rcnt = Rcnt + 1;
		} else {
			//Calculates the change in velocity of the atom from an absorption event from the left.
			velocity = velocity + V_photon;
			Lcnt = Lcnt + 1;
		}
		if (Prob_Emit() > 0.5){
			//calls the random probability of emission method.
			P_emit = true;
		}else{
			P_emit = false;
		}
		if (P_emit){
			//Calculates the change in velocity of the atom from an emission event from the right.
			velocity = velocity - V_photon;
		}else{
			//Calculates the change in velocity of the atom from an emission event from the left.
			velocity = velocity + V_photon;
		}
		return velocity;
	}
	
    public double mean(double[] arg){
		double sum = 0;
		for(int i = 0; i < arg.length; i++){
			sum = sum + arg[i];		
		}
		return sum/arg.length;
	}
	
	public double stdDev(double[] arg){
		double myMean = mean(arg);
		double dev, sqDev, sum;
		sum = 0;
		for(int i = 0; i < arg.length; i++){
			dev = myMean - arg[i];
			sqDev = square(dev);
			sum = sum + sqDev;		
		}
		return Math.sqrt(sum/arg.length);
	}
}