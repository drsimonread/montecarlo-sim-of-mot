package edu.smcm.physics.mot;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 */
public class SimulateAnnealing{
	
	static final double Lambda_not = 780.24 * Math.pow(10, -9);
	static final double k = 2 * Math.PI / Lambda_not;
	static final double Gamma = 6e6;//2 * Math.PI * 3E6;// 1 / tau;
	private static double[] fit;
	private static SimulationData data;
	private static int range;
	private static Random r = new Random();
	
	public SimulateAnnealing(SimulationData sim){
			SA(sim);
	}
	
	private ArrayList<Double> SA(SimulationData mySim){
		data = mySim;
		range = data.numberOfParticles();
		int max_iteration = 150, iteration = 1, cnt = 0;
		ArrayList<Double> values = new ArrayList<Double>();
		values = generateParameters();
		ArrayList<Double> best_values = (ArrayList<Double>) values.clone();
		
//		System.out.println("Velocity  = " + data.getVelocity());
//		System.out.println("Amplitude = " + data.getAmplitude());
		
		double[][]normalized_data = normalize(DataAnalysis.binnedData(data.toArray_velocity(), data.numberOfParticles()));
		
		double error = computeError(values, normalized_data);
		System.out.println("Initial Error = " + error);
		double best_error = error;
		double temp_zero = 1000;
		double temp = temp_zero;
		
		while(temp >=10 ){//&& iteration < max_iteration){
			ArrayList<Double> next_values = generateParameters();
			double next_error = computeError(next_values, normalized_data);
			
			if(next_error < best_error){
				best_values = next_values;
				best_error = next_error;
			}
			temp = temp_zero/iteration;
//			System.out.println("Next error = " + next_error + ".  Error = " + error);
			
			if(exciteProbability(error, next_error, temp) > r.nextDouble()){
				cnt = cnt + 1;
				values = next_values;
				error = next_error;
			}
			iteration = iteration + 1;
//			System.out.println("Count = " + iteration + ". Temperature = " + temp);
		}
		System.out.println("V1 = " + best_values.get(0));
		System.out.println("V2 = " + best_values.get(1));
		System.out.println("V3 = " + best_values.get(2));
		System.out.println("A = " + best_values.get(3));
		System.out.println("B = " + best_values.get(4));
		System.out.println("C = " + best_values.get(5));
		System.out.println("Best Error    = " + best_error);
		fit  = expCurve(best_values);
		return best_values;
	}
	
	private static ArrayList<Double> generateParameters(){
		ArrayList<Double> somevalue = new ArrayList<Double>();
		double V1 = data.getVelocity();//initial
		double A = data.getAmplitude()/data.numberOfParticles();//initial
		double V2 = (2*V1/10);// * r.nextDouble();//missing
		double B = A;// * r.nextDouble();//missing
		double V3 = (V2/10) * r.nextDouble();//spike
		double C = 0.76 * B * V2 / V3;//spike
		
		somevalue.add(V1);
		somevalue.add(V2);
		somevalue.add(V3);
		somevalue.add(A);
		somevalue.add(B);
		somevalue.add(C);
		
		return somevalue;
	}
	
	private double[][] normalize(double [][] binned_data){ 
		double[][] normalized_data = new double [2][data.numberOfParticles()];
		double Z = 0;
		
		for(int i = 0; i < data.numberOfParticles(); i++){
			Z = Z + binned_data[1][i];
		}
		
		for (int i = 0; i < data.numberOfParticles(); i++){
			normalized_data[0][i] = binned_data[0][i];
			normalized_data[1][i] = binned_data[1][i]/Z;
		}
		return normalized_data;
	}
	
	private double exciteProbability(double error, double next_error, double temp){
		double prob = 0;
		
		if(error >= next_error){
			prob = 1.0;
		}else{
			prob = Math.exp((error - next_error)/temp);
//			System.out.println("new state Probability = " + prob + ".  e^(" + (error - next_error)/temp + ")");
		}
		return prob;
	}
	
	private static double computeError(ArrayList<Double> values, double[][] myData){
		double sumErr = 0;
		int i_max = myData.length;
		double[] the_prediction = expCurve(values);
		
		for(int i = 0; i < i_max; i++){
			sumErr = sumErr + Math.sqrt(DataAnalysis.square((the_prediction[i] - myData[1][i]) * i_max));
			} 
		return sumErr;
	}

	private static double[] expCurve(ArrayList<Double> values){
		double[] P_X = new double[range];
		double i_min = DataAnalysis.min(data.toArray_velocity());
		double i_max = DataAnalysis.max(data.toArray_velocity());
		double step = (i_max-i_min)/range, A, B, C, V1, V2, V3, p1, p2, p3, v;
		
		V1 = values.get(0);
		V2 = values.get(1);
		V3 = values.get(2);
		A = values.get(3);
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
			twoD[1][i] = fit[i];
		}
		
		return twoD;
	}
}