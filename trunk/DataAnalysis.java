import java.lang.reflect.Array;

public class DataAnalysis {

	public static double max(double[] arr){
		double max;
		max = arr[0];
		for (int i = 0; i < arr.length; i++){
			if (arr[i] > max) {
	            max = arr[i];   // new maximum
	        }
		}
		return max;
	}
	
	public static double min(double[] arr){
		double min;
		min = arr[0];
		for (int i = 0; i < arr.length; i++){
			if (arr[i] < min) {
	            min = arr[i];   // new minimum
	        }
		}
		return min;
	}
	
	public static double[][] truncateArray(double[][] arr){
		double min1, min2, max1, max2;
		int min1Index, min2Index;
		min1Index = min2Index = 0;
		min1 = min2 = max1 = max2 = 0;
		double [][] temp = new double [2][25];
//		for(int i = 0; i < arr. length; i++){
//			if(arr[1][i] > max1){
//				max1 = arr[1][i];
//				if (arr[1][i] < min1){
//	//				if (arr[1][i-1] > min1 && min1 < arr[1][i+1]){
//						min1 = arr[1][i];
//						min1Index = i;
//						System.out.println(min1 + "min, " + min1Index);
//	//				}
//				}else if(arr[1][i] > max2){
//					max2 = arr[1][i];
//					if (arr[1][i] < min2){
//	//					if (arr[1][i-1] > min2 && min2 < arr[1][i+1]){
//							min2 = arr[1][i];
//							min2Index = i;
//							System.out.println(min2 + ", " + min2Index);
//	//					}	
//					}
//				}
//			}
//		}	
		for(int j = 0; j < 15; j++){
			temp[0][j] = arr[0][j + 25];
			temp[1][j] = arr[1][j + 25];
		}
		return temp;
	}
	
	public static double square(double arg) {
		// Squares the arguments.
		return arg * arg;
	}
	
    public static double mean(double[] arg){
		double sum = 0;
		for(int i = 0; i < arg.length; i++){
			sum = sum + arg[i];		
		}
		return sum/arg.length;
	}
	
	public static double stdDev(double[] arg){
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
	
	public static void LSBF(double [][] arr){
		double p, q, r, s, d, a, b;
		p = q = r = s = d = a = b = 0;
		for (int k = 0; k < arr.length; k++){
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
//			System.out.println(data[0][i] + ", " + data[1][i]);
		}
		return data;
	}
}
