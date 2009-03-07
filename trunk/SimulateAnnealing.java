import java.util.ArrayList;
import java.util.Random;

/**
 * Simulated Annealing and the Traveling Salesman
 * Copyright 2005 by Heaton Research, Inc.
 * by Jeff Heaton (http://www.heatonresearch.com) 12-2005
 * -------------------------------------------------
 * This source code is copyrighted.
 * You may reuse this code in your own compiled projects.
 * However, if you would like to redistribute this source code
 * in any form, you must obtain permission from Heaton Research.
 * (support@heatonresearch.com).
 * -------------------------------------------------
 *
 * This class implements SimulatedAnnealing.
 *
 * -------------------------------------------------
 * Want to learn more about Neural Network Programming in Java?
 * Have a look at our e-book:
 *
 * http://www.heatonresearch.com/articles/series/1/
 *
 * @author Jeff Heaton (http://www.jeffheaton.com)
 * @version 1.0
 */
public class SimulateAnnealing{
	
	static final double Lambda_not = 780.24 * Math.pow(10, -9);
	static final double k = 2 * Math.PI / Lambda_not;
	static final double Gamma = 6e6;//2 * Math.PI * 3E6;// 1 / tau;
	private static SimulationData Sim; 
	private static double[][] P_X;
	private static double[][] normData;
	private static double Z = 0;//constant of proportionality.
	private static double e_initial, Ai, Bi, Ci, V1i, V2i, V3i;
	
	public SimulateAnnealing(SimulationData Sim_){
		Sim = Sim_;
		
		ArrayList<Double> parameters = new ArrayList<Double>();
		int b = 0, range = Sim.size();
		double[][] rawData;
		normData = new double [2][range]; 
		P_X = new double [2][range];
		double[][]p_x = new double[2][range];
		double[][]kde = KDE.SmoothIt(Sim); 
		double e, cnt = 1, variable, up = 0, down = 0, temp;
		double V1, V2, V3, A, B, C, Ti, Tf, Err, T = 10000;
		Random r = new Random();
		double prob = 0;
		LineGraph myLine;
		KDE.SmoothIt(Sim);

		Ti = 1;
		Tf = 100e-6;
		V1 = V1i = Equations.V_calc(Ti);
		V2 = V2i = Equations.V_calc(Tf);
		V3 = V3i = (2*Gamma)/k;//should be capture velocity, Foot page 190.
		A = Ai = 0.012;
		B = Bi = 0.13;//A*10;
		C = Ci = 0.012;//A;
		
		parameters.add(V1);
		parameters.add(V2);
		parameters.add(V3);
		parameters.add(A);
		parameters.add(B);
		parameters.add(C);
		
		rawData = kde;//DataAnalysis.binnedData(Sim.toArray(), range);
		
		SimulateAnnealing.P_X = expCurve(parameters);
		
		for(int i  = 0; i < range; i++){
			p_x[0][i] = P_X[0][i];
			p_x[1][i] = P_X[1][i];
			Z = Z + rawData[1][i];
		}
		
		for(int i = 0; i < range; i++){
			normData[0][i] = rawData[0][i];
			normData[1][i] = rawData[1][i]/Z;
		}
		
		Err = sumSqErr();
		e = e_initial = 1;
		e_initial = sumSqErr();
		e = sumSqErr();
		
		System.out.println("Initial Error is..." + e);

		while(T > 0.0001){// && e > 9e-4){
			System.out.println("Count = " + cnt);
//			b = r.nextInt(6);
		for(b = 0; b < 6; b++){
			System.out.println("Top of loop Error    = " + e);
			System.out.println("");
			variable = parameters.get(b);
			temp = parameters.get(b);
			
			parameters.set(b, variable + variable * 0.5);
			P_X = expCurve(parameters);
			up = sumSqErr();
			parameters.set(b, temp);
			
			parameters.set(b, variable - variable * 0.5);
			P_X = expCurve(parameters);
			down = sumSqErr();
			parameters.set(b, temp);
			
			if(up > down){
				Err = down;
				parameters.set(b, variable + variable * 0.5);
			}else{
				Err = up;
				parameters.set(b, variable + variable * 0.5);
			}

			prob = Math.exp(-(Z*Err/(2*T)));
			
			if(Err < e){
				e = Err;
				System.out.println("Bottom of loop Error = " + e);
				System.out.println("");
			}else if(false){//prob > r.nextDouble()){
				e = Err;
				System.out.println("Probability          = " + prob);	
				System.out.println("Bottom of loop Error = " + e);
				System.out.println("");
			}			
			T = T - T/2;
		}
			cnt = cnt+1;
		}	
		
		myLine = new LineGraph("Test", "Velocity", "Probability");
		myLine.addSeries("Simulated Data", P_X);
		myLine.addSeries("Expected", p_x);
		myLine.addSeries("Normalized Data", normData);
		myLine.plotIT();
//		System.out.println("P(x) = " + parameters.get(3) + " * p(V2 = " + parameters.get(0) + ") + " + parameters.get(4) + " * p(V1 = " + parameters.get(1) + ") + " + parameters.get(5) + "p * (V3 = " + parameters.get(2) + ")");
		System.out.println("V2 = " + parameters.get(1));
		System.out.println("B = " + parameters.get(4));
		
	}
	
	private static double sumSqErr(){
		double sumErr = 0;
		int i_max = Sim.size();
		
		for(int i = 0; i < i_max; i++){
			sumErr = sumErr + DataAnalysis.square(P_X[1][i] - normData[1][i]);
		}
		return sumErr;
	}

	private static double[][] expCurve(ArrayList<Double> parameters){
		int range = Sim.size();		
		double [][]temp = new double[2][range];
		double i_min = DataAnalysis.min(Sim.toArray());
		double i_max = DataAnalysis.max(Sim.toArray());
		double step = (i_max-i_min)/range, A, B, C, V1, V2, V3, p1, p2, p3, v, z = 0;
		
		V1 = parameters.get(0);
		V2 = parameters.get(1);
		V3 = parameters.get(2);
		A = parameters.get(3);
		B = parameters.get(4);
		C = parameters.get(5);
		
		for(int i = 0; i < range; i++){
			v = i_min + i*step;
			p1 = A/(Math.sqrt(Math.PI)*V1)*Math.exp(-((v*v)/(V1*V1)));
			p2 = B/(Math.sqrt(Math.PI)*V1)*Math.exp(-((v*v)/(V2*V2)));
			p3 = C/(Math.sqrt(Math.PI)*V1)*Math.exp(-((v*v)/(V3*V3)));
			
			temp[0][i] = v;
			temp[1][i] = (p1 + p2 - p3);
			
			z = z + temp[1][i];
		}
		
		for(int i = 0; i < range; i++){
			P_X[0][i] = temp[0][i];
			P_X[1][i] = temp[1][i]/z;
		}
		
		return P_X;
	}
	
	public int size(){
		return P_X.length;
	}
	
	public double[][] to2DArray(){
		return P_X;
	}
}