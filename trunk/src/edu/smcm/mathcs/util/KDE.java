package edu.smcm.mathcs.util;
import edu.smcm.physics.mot.DataAnalysis;
import edu.smcm.physics.mot.SimulationData;


public class KDE {
	private static SimulationData data;
	
	public static double[][] SmoothIt (SimulationData data_){
		double t = 0;
		data = data_;
		int iMax = data.size();
		double[][]KDE = new double[2][iMax];
		double min = DataAnalysis.min(data.toArray());
		double max = DataAnalysis.max(data.toArray());
		double step = (max-min)/iMax;
		for(int i = 0; i < iMax; i++){
			t = min + i * step;
			KDE[0][i] = t;
			KDE[1][i] = Math.abs(f_hat(t));
		}
		return KDE;
	}

	private static double f_hat(double t){
		int i_max = data.size();
		double d_k = D_kofT(k(), t);
		double kernal = 0;
		
		for (int i = 0; i < i_max; i++){
			kernal = kernal + 1/(Math.sqrt(2*Math.PI))*Math.exp(-square((t - data.getValue(i))/d_k)/2);
		}
		return 1/(i_max*d_k)*kernal;
	}
	
	private static int k (){
		int kth = (int)Math.sqrt(data.size());
		return kth;
	}
	
	private static double D_kofT (int k, double t){
		data.sort();
		int i = rank_of_nearest(t);
		int next_nearest_higher_rank = i+1;
		int next_nearest_lower_rank = i-1;
		int next_nearest_rank = 0;
		
		for(int j = 0; j < k; j++){
			boolean no_higher = next_nearest_higher_rank >= data.size();
			boolean no_lower = next_nearest_lower_rank < 0;
			
			if(no_higher && no_lower){
				System.out.println("The Sky Is Falling.");
			}else if (no_higher){
				next_nearest_rank = next_nearest_lower_rank;
				next_nearest_lower_rank = next_nearest_lower_rank - 1;
				
			}else if (no_lower){ 
				next_nearest_rank = next_nearest_higher_rank;
				next_nearest_higher_rank = next_nearest_higher_rank + 1;
				
			}else if(Math.abs(t-data.getValue(next_nearest_higher_rank)) > Math.abs(t-data.getValue(next_nearest_lower_rank))){
				next_nearest_rank = next_nearest_lower_rank;
				next_nearest_lower_rank = next_nearest_lower_rank - 1;
				
			}else{
				next_nearest_rank = next_nearest_higher_rank;
				next_nearest_higher_rank = next_nearest_higher_rank + 1;
			}
		}
		return t - data.getValue(next_nearest_rank);
	}
	
	private static int rank_of_nearest (double t){
		int i = data.binarySearch(t);
		int previous;
		if (i == 0){
			previous = 0;
		}else{
			previous = i-1;
		}
		
		if (Math.abs(t - data.getValue(i)) > (Math.abs(t - data.getValue(previous)))){
			i = i - 1;
		}
		return i;
	}
	
	private static double square(double arg){
		return arg*arg;
	}
}
