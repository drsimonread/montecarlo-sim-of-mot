package edu.smcm.physics.mot;

import java.util.ArrayList;

public class HillClimbing {
	
	static final double Lambda_not = 780.24 * Math.pow(10, -9);
	static final double k = 2 * Math.PI / Lambda_not;
	static final double Gamma = 6e6;//2 * Math.PI * 3E6;// 1 / tau;
	private static double[] P_X;
	private static SimulationData data;
	private static int range;

	public HillClimbing(SimulationData Sim){
		data = Sim;
		range = data.numberOfParticles();
		int max_iteration = 1000, iteration = 1;
		ArrayList<Double> values = new ArrayList<Double>();
		double[][]normalized_data = normalize(DataAnalysis.binnedData(data.toArray_velocity(), data.numberOfParticles()));
		values = generateParameters();
		double error = computeError(values, normalized_data);
		System.out.println("Initial Error = " + error);
		double delta = 1/1000;
		
		while(iteration != max_iteration){

			for(int i = 0; i < 6; i++){
				double thisValue = values.get(i);
				
				values.set(i, thisValue + thisValue * delta);
				double up = computeError(values, normalized_data);
				values.set(i, thisValue);

				values.set(i, thisValue - thisValue * delta);
				double down = computeError(values, normalized_data);
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
		
		System.out.println("V1 = " + values.get(0));
		System.out.println("V2 = " + values.get(2));
		System.out.println("V3 = " + values.get(3));
		System.out.println("A = " + values.get(1));
		System.out.println("B = " + values.get(4));
		System.out.println("C = " + values.get(5));
		System.out.println("Final Error =   " + error);
	}
	
	private double[][] normalize(double [][] binned_data){ 
		double[][] normalized_data = new double [2][data.numberOfParticles()];
		double Z;
		
		Z = 0;
		for(int i = 0; i < data.numberOfParticles(); i++){
			Z = Z + binned_data[1][i];
		}
		
		for (int i = 0; i < data.numberOfParticles(); i++){
			normalized_data[0][i] = binned_data[0][i];
			normalized_data[1][i] = binned_data[1][i]/Z;
		}
		return normalized_data;
	}
	
	private static ArrayList<Double> generateParameters(){
		ArrayList<Double> somevalue = new ArrayList<Double>();
		double V1 = data.getVelocity();
		double A = data.getAmplitude()/data.numberOfParticles();
		
		double V2 = 2*V1/10;
		double B = A;
		
		double V3 = V2/45;// * r.nextDouble();
		double C = 0.75 * B * V2 / V3;
		
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
			sumErr = sumErr + Math.sqrt(DataAnalysis.square((the_prediction[i] - theData[1][i])*i_max));
		} 
		return sumErr;
	}

	private static double[] expCurve(ArrayList<Double> values){
		P_X = new double[range];
		double i_min = DataAnalysis.min(data.toArray_velocity());//mydata[0]);
		double i_max = DataAnalysis.max(data.toArray_velocity());//mydata[0]);
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
		double i_min = DataAnalysis.min(data.toArray_velocity());
		double i_max = DataAnalysis.max(data.toArray_velocity());
		double step = (i_max-i_min)/range;
		double[][]twoD = new double[2][range];
		
		for(int i = 0; i < range; i++){
			twoD[0][i] = i_min + i*step;
			twoD[1][i] = P_X[i];
		}
		
		return twoD;
	}
}
