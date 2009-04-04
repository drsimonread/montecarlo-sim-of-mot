package edu.smcm.physics.mot;

import java.util.ArrayList;
import java.util.Random;

public class HillClimbing {
	
	static final double Lambda_not = 780.24 * Math.pow(10, -9);
	static final double k = 2 * Math.PI / Lambda_not;
	static final double Gamma = 6e6;//2 * Math.PI * 3E6;// 1 / tau;
	private static double[] P_X;
	private static SimulationData data;
	private static double[][] mydata;
	private static int range;
	private static Random r = new Random();

	public HillClimbing(SimulationData Sim){
		data = Sim;
		range = data.size();
		int max_iteration = 1000;
		int iteration = 1;
		ArrayList<Double> values = new ArrayList<Double>();
		
		mydata = DataAnalysis.binnedData(data.toArray(), range);
		
		double maxB = DataAnalysis.max(mydata[1]);
		values = generateParameters(maxB);
		double error = computeError(values, mydata);
		System.out.println("Initial Error = " + error);
		double delta = 1/1000;
		
		while(iteration != max_iteration){

			for(int i = 2; i < 4; i++){
				double thisValue = values.get(i);
				
				values.set(i, thisValue + thisValue * delta);
				double up = computeError(values, mydata);
				values.set(i, thisValue);

				values.set(i, thisValue - thisValue * delta);
				double down = computeError(values, mydata);
				values.set(i, thisValue);

				if (up >= error && down >= error){
					delta = delta/5;
				}else if (up < down && up < error){
					values.set(i, thisValue + thisValue * delta);
					error = up;
					System.out.println("Up.....Error = " + error);
				}else if (down < error){
					values.set(i, thisValue - thisValue * delta);
					error = down;
					System.out.println("Down...Error = " + error);
				}
			}
			iteration = iteration + 1;
//			System.out.println("Count = " + iteration);
		}		
	}
	
	private static ArrayList<Double> generateParameters(double b){
		ArrayList<Double> somevalue = new ArrayList<Double>();
		double V1 = 13.78;//data.getVelocity();
		double A = data.getAmplitude();
		double B = b;
		
		double V2 = 3;//V1 * r.nextDouble();
		double V3 = 0.75;//V2 * r.nextDouble();
		
		double C = 10*(B * V2 / V3);
		
		somevalue.add(V1);
		somevalue.add(A);
		
		somevalue.add(V2);
		somevalue.add(V3);
		somevalue.add(B);
		
		somevalue.add(C);
		
		return somevalue;
	}
	
	private static double computeError(ArrayList<Double> values, double[][] theData){
		double sumErr = 0;
		int i_max = theData.length;
		double[] the_prediction = expCurve(values);
		for(int i = 0; i < i_max; i++){
			sumErr = sumErr + Math.sqrt(DataAnalysis.square(the_prediction[i] - theData[1][i]));
			} 
		return sumErr;
	}

	private static double[] expCurve(ArrayList<Double> values){
		P_X = new double[range];
		double i_min = DataAnalysis.min(data.toArray());//mydata[0]);
		double i_max = DataAnalysis.max(data.toArray());//mydata[0]);
		double step = (i_max-i_min)/range, A, B, C, V1, V2, V3, p1, p2, p3, v;
		
		V1 = values.get(0);
		A = values.get(1);
		V2 = values.get(2);
		V3 = values.get(3);
		
		B = values.get(4);
		C = values.get(5);
		
		for(int i = 0; i < range; i++){
			v = i_min + i*step;
			p1 = A/(Math.sqrt(2*Math.PI)*V1)*Math.exp(-((v*v)/(V1*V1)));
			p2 = B/(Math.sqrt(2*Math.PI)*V1)*Math.exp(-((v*v)/(V2*V2)));
			p3 = C/(Math.sqrt(2*Math.PI)*V1)*Math.exp(-((v*v)/(V3*V3)));
			
			P_X[i] = (p1 - p2 + p3);
		}	
		return P_X;
	}
	
	public int size(){
		return range;
	}
	
	public double[][] to2DArray(){
		double i_min = DataAnalysis.min(data.toArray());
		double i_max = DataAnalysis.max(data.toArray());
		double step = (i_max-i_min)/range;
		double[][]twoD = new double[2][range];
		
		for(int i = 0; i < range; i++){
			twoD[0][i] = i_min + i*step;
			twoD[1][i] = P_X[i];
		}
		
		return twoD;
	}
}
