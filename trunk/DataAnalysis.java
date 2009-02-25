import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class DataAnalysis {
	
	public static void main(String[] args){
		Configurations data;
		ObjectInputStream file;
//		tempVSdetuning TvsD  = new tempVSdetuning();
//		double[][]temperatureVSDetuning = new double [2][10];
//		double[][]smoothData;		
//		double[][]suspectData;
//		double t;
		SimulationData Sim;
		
		try{
			file = new ObjectInputStream(new FileInputStream("simulation_data.dat"));
			data = (Configurations) file.readObject();
			file.close();

			for(int k = 0; k < data.size(); k++){
				Sim = data.get(k);
				int range = Sim.size();
//				smoothData = new double[2][range];		
				double[][] P_X = new double [2][range];
				double[][] diff = new double [2][range];
				double[][] rawData = new double [2][range];
				double[][] normData = new double[2][range];
				
				double i_min = min(Sim.toArray());
				double i_max = max(Sim.toArray());
				double step = (i_max-i_min)/range;
				
				double V1, V2, V3, v, A, B, C, p1, p2, p3;

				V1 = 13.78;
				V2 = Equations.V_calc(100e-6);
				V3 = 1.5;
				A = 0.012;
				B = 0.13;
				C = 0.012;
				rawData = binnedData(Sim.toArray(), range);
				
				for(int i = 0; i < range; i++){
					
					v = i_min + i*step;
					p1 = A/(Math.sqrt(Math.PI)*V1)*Math.exp(-((v*v)/(V1*V1)));
					p2 = B/(Math.sqrt(Math.PI)*V1)*Math.exp(-((v*v)/(V2*V2)));
					p3 = C/(Math.sqrt(Math.PI)*V1)*Math.exp(-((v*v)/(V3*V3)));
					
					P_X[0][i] = v;
					P_X[1][i] = p1 + p2 - p3;	
					
					normData[0][i] = rawData[0][i];
					normData[1][i] = rawData[1][i]/range;
					
					diff[0][i] = v;
					diff[1][i] = square(P_X[1][i] - normData[1][i]);
				}
				System.out.println("max diff..." + max(diff[1]));
				plots.myLine(P_X, "Plot of Fitting Equation", "Velocity", "Probability");
				plots.myLine(normData, "Normalized Raw Data", "Velocity", "Probability");
				plots.myLine(diff, "Difference", "Velocity", "Probability");
				
/**				plots.myLine(rawData, "Raw Data", "Velocity", "Probability");
				plots.histogram(Sim.toArray(), "Histogram of Raw Data");
				
				System.out.println("Frequency is: " + Sim.frequency());
				
				for(int i = 0; i < smoothData[0].length; i++) {
					t = i_min + (i * step);
					smoothData[0][i] = t;
					smoothData[1][i] = Math.abs(KDE.f_hat(Sim, t));					
				}
				
				suspectData = new double [2][Sim.size()];
				suspectData  = truncateData(smoothData);
				System.out.println("Detuning is..." + Sim.frequency() + ", Temperature is..." + stdDev(suspectData[1]));
				TvsD.addTvsd(Math.abs(Sim.frequency()), stdDev(suspectData[1]));			
				
				plots.myLine(smoothData, "Kernel Density Estimation Plot, Detuning = " + Sim.frequency(), "Velocities", "Probability");
				plots.myLine(truncateData(smoothData), "Kernel Density Estimation Plot - Exploded, Detuning = " + Sim.frequency(), "Velocity", "Probability");
				plots.histogram(Sim.toArray(), "Histogram of Raw Data");
*/				
				k = data.size();
			}
			
//			temperatureVSDetuning = TvsD.toArray();
//			plots.myLine(temperatureVSDetuning, "Temperature VS. Detuning", "Detuning", "Temperature");
			
			
			System.out.println("DONE!");
		}catch(IOException caught){
			System.err.println(caught);
		}catch(ClassNotFoundException caught){
			System.err.println(caught);
		}
	}

	public static double max(double[] doubles){
		double max;
		int i_max;
		max = doubles[0];
		i_max = doubles.length;
		for (int i = 0; i < i_max; i++){
			if (doubles[i] > max) {
	            max = doubles[i];   // new maximum
	        }
		}
		return max;
	}
	
	public static double min(double[] data){
		double min;
		int i_max;
		i_max = data.length;
		min = data[0];
		for (int i = 0; i < i_max; i++){
			if (data[i] < min) {
	            min = data[i];   // new minimum
	        }
		}
		return min;
	}
	
	public static double[][] truncateData(double[][] data){
		int indexATmax = 0, lt = 0, rt = 0;
		int length;
		double min;
		double max = 0.0;
		
		for(int i = 0; i < data[1].length; i++){
			if (data[1][i] > max){
				max = data[1][i];
				indexATmax = i;
			}
		}
//		System.out.println(indexATmax);
				
		min = data[1][indexATmax];
		for (int i = indexATmax; i < 10000; i++){
			if((data[1][i] < data[1][i + 3])){
				min = data[1][i];
				rt = i;
				
				break;
			}
		}
		System.out.println("rt = " + data[0][rt]);
		
		min = data[1][indexATmax];
		for (int i = indexATmax; i > 0; i--){
			if((data[1][i] < data[1][i - 3])){
				min = data[1][i];
				lt = i;
				
				break;
			}
		}
		System.out.println("lt = " + data[0][lt]);
		
		length = rt-lt;
		double[][] temp = new double[2][length];
		for(int i = 0; i < length; i++){
			temp[0][i] = data[0][lt + i];
			temp[1][i] = data[1][lt + i];
		}
		return temp;
	}
	
	public static double square(double arg) {
		// Squares the arguments.
		return arg * arg;
	}
	
    public static double mean(double[] arg){
		double sum = 0;
		int i_max  = arg.length;
		for(int i = 0; i < i_max; i++){
			sum = sum + arg[i];		
		}
		return sum/arg.length;
	}
	
	public static double stdDev(double[] arg){
		double myMean = mean(arg);
		double dev, sqDev, sum;
		sum = 0;
		int i_max  = arg.length;
		for(int i = 0; i < i_max; i++){
			dev = myMean - arg[i];
			sqDev = square(dev);
			sum = sum + sqDev;		
		}
		return Math.sqrt(sum/arg.length);
	}
	
	public static void LSBF(double [][] arr){
		double p, q, r, s, d, a, b;
		p = q = r = s = d = a = b = 0;
		int i_max  = arr.length;
		for (int k = 0; k < i_max; k++){
			p = p + arr[0][k];
//			System.out.println(arr[0][k]);
			q = q + arr[1][k];
			r = r + arr[0][k] * arr[1][k];
			s = s + arr[0][k] * arr[0][k];
		}
		d = (arr.length + 1) * s - square(p);
		a = ((arr.length + 1) * r - p * q)/d;
		b = (s * q - p * r)/d;
		System.out.println("y = " + a + " * x + " + b);
	}
	
	public static double [][] binnedData(double [] arr, int numOfbins){
		double prevBin = 0;
		double data [][] = new double [2][numOfbins];
		double [] freq = new double [numOfbins], bins = new double [numOfbins];
		double max = mean(arr) + 3 * stdDev(arr);
		double min = mean(arr) - 3 * stdDev(arr);
		double range = max - min;
		double size = range/numOfbins;
		
		for(int i = 0; i < numOfbins; i++){
			bins[i] = min + i * size;
			for(int j = 0; j < arr.length; j++){		
				if (prevBin <= arr[j] && arr[j] < bins[i]){
					freq[i] += 1;
				}
			}	
			prevBin = bins[i];
		}
		
		for(int i = 0; i < numOfbins; i++){
			data[0][i] = bins[i];
			data[1][i] = freq[i];
		}
		return data;
	}
}
