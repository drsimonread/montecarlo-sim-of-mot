import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class DataAnalysis {
	
	public static void main(String[] args){
		
		Configurations data;
		ObjectInputStream file;
		double t;
		
		try{
			file = new ObjectInputStream(new FileInputStream("simulation_data.dat"));
			data = (Configurations) file.readObject();
			file.close();

			for(int k = 0; k < data.size(); k++){
				SimulationData Sim = data.get(k);
				int range = Sim.size();
				double[][]smoothData = new double[2][range];
				double i_min = min(Sim.toArray());
				double i_max = max(Sim.toArray());
				double step = (i_max-i_min)/range;
				System.out.println("Frequency is: " + Sim.frequency());				
				for(int i = 0; i < smoothData[0].length; i++) {
					t = i_min + (i * step);
					smoothData[0][i] = t;
					smoothData[1][i] = Math.abs(KDE.f_hat(Sim, t));					
				}
//			plots.myLine(smoothData, "Kernel Density Estimation Plot, Detuning = " + Sim.frequency(), "Velocities", "Probability");
			
			plots.myLine(truncateData(smoothData), "Test", "Velocity", "Probability");
//			plots.histogram(Sim.toArray(), "Histogram of Raw Data");
			}
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
		double max = 0.0;
		
		for(int i = 0; i < data[1].length; i++){
			if (data[1][i] > max){
				max = data[1][i];
				indexATmax = i;
			}
		}
		System.out.println(indexATmax);
		
//		for(int i = indexATmax - 10; i < indexATmax + 10; i++){
//			System.out.println("Index is..." + i + " Value is..." + data[1][i]);
//		}
		
		double min = data[1][indexATmax];
		System.out.println("Max is..." + (min) + " index..." + indexATmax);
		for (int i = indexATmax; i < 10000; i++){
			if((data[1][i] < data[1][i + 3])){
				min = data[1][i];
				rt = i;
				System.out.println("rt = " + rt);
				break;
			}
		}
		
		min = data[1][indexATmax];
		System.out.println("Max is..." + (min) + " index..." + indexATmax);
		for (int i = indexATmax; i > 0; i--){
			if((data[1][i] < data[1][i - 3])){
				min = data[1][i];
				lt = i;
				System.out.println("lt = " + lt);
				break;
			}
		}
		
		length = rt-lt;
		System.out.println("array length..." + length);
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
