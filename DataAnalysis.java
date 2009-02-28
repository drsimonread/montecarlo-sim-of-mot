import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Random;

public class DataAnalysis {
	
	static final double Lambda_not = 780.24 * Math.pow(10, -9);
	static final double k = 2 * Math.PI / Lambda_not;
	static final double Gamma = 6e6;//2 * Math.PI * 3E6;// 1 / tau;
	
	public static void main(String[] args){
		Configurations data;	
		SimulationData Sim;
		ObjectInputStream file;
		ArrayList<Double> parameters = new ArrayList<Double>();
//		tempVSdetuning TvsD  = new tempVSdetuning();
//		double[][]temperatureVSDetuning = new double [2][10];
		Random r = new Random();
		double V1, V2, V3, A, B, C, Ti, Tf;
		Ti = 1;
		Tf = 100e-6;
		V1 = Equations.V_calc(Ti);
		V2 = Equations.V_calc(Tf);
		V3 = 2*Gamma/k;//should be capture velocity, Foot page 190.
		A = C = 0.05;
		B = 0.5;
		parameters.add(V1);
		parameters.add(V3);
		parameters.add(V2);
		parameters.add(A);
		parameters.add(B);
		parameters.add(C);
		int b = 0;
		
		try{
			file = new ObjectInputStream(new FileInputStream("simulation_data.dat"));
			data = (Configurations) file.readObject();
			file.close();

			for(int a = 0; a < data.size(); a++){
				Sim = data.get(a);
				int range = Sim.size();
				double[][] rawData = new double [2][range];
				double[][] P_X = new double [2][range];
				rawData = binnedData(Sim.toArray(), range);				
				int kmax = 1000, kay = 0;
				P_X = expCurve(Sim, parameters);
				double e = sumSqErr(Sim, P_X, rawData), emax = 4e-4, variable, up = 0, down = 0, temp;
				System.out.println("Initial Error is..." + e);
/**				
				s := s0; e := E(s)                           // Initial state, energy.
				sb := s; eb := e                             // Initial "best" solution
				k := 0                                       // Energy evaluation count.
				while k < kmax and e > emax                  // While time remains & not good enough:
				  sn := neighbour(s)                         //   Pick some neighbour.
				  en := E(sn)                                //   Compute its energy.
				  if en < eb then                            //   Is this a new best?
				    sb := sn; eb := en                       //     Yes, save it.
				  if P(e, en, temp(k/kmax)) > random() then  //   Should we move to it?
				    s := sn; e := en                         //     Yes, change state.
				  k := k + 1                                 //   One more evaluation done
				return sb                                    // Return the best solution found.
*/				
				while(kay < kmax && e > emax){
					System.out.println("Count = " + kay);
//					b = r.nextInt(6);
					for(b = 2; b < parameters.size(); b++){
					variable = parameters.get(b);
					temp = parameters.get(b);
					
					parameters.set(b, variable + variable * 0.5);
					P_X = expCurve(Sim, parameters);
					up = sumSqErr(Sim, P_X, rawData);
					parameters.set(b, temp);
					
					parameters.set(b, variable - variable * 0.5);
					P_X = expCurve(Sim, parameters);
					down = sumSqErr(Sim, P_X, rawData);
					parameters.set(b, temp);
					
//					System.out.println("Probability = " + Math.exp(-(e/2)));
					
					if(up > down && Math.exp(-down/2) > r.nextDouble()){
						e = down;
						parameters.set(b, variable - variable * 0.5);
						System.out.println("down, Error = " + down);
					}else if( up < down && Math.exp(-up/2) > r.nextDouble()){
						e = up;
						parameters.set(b, variable + variable * 0.5);
						System.out.println("up, Error = " + up);							
					}
					if(e < emax){
						break;
					}
					}
					kay = kay + 1;
				}
				
				plots.myLine(P_X, "Plot of Fitting Equation", "Velocity", "Probability");
//				plots.myLine(normData, "Normalized Raw Data", "Velocity", "Probability");
				
				System.out.println("V1 = " + parameters.get(0));
				System.out.println("V2 = " + parameters.get(2));
				System.out.println("V3 = " + parameters.get(1));
				System.out.println("A = " + parameters.get(3));
				System.out.println("B = " + parameters.get(4));
				System.out.println("C = " + parameters.get(5));
				a = data.size();
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
	
	public static double sumSqErr(SimulationData Sim, double[][] P_X, double[][] rawData){
		double sumErr = 0;
		int range = Sim.size();
		rawData = binnedData(Sim.toArray(), range);
		
		for(int i = 0; i < range; i++){
			sumErr = sumErr + (square(P_X[1][i]) - square(rawData[1][i]/range));
		}
		return sumErr;
	}
	
	public static double[][] expCurve(SimulationData Sim, ArrayList<Double> parameters){
		int range = Sim.size();		
		double[][] P_X = new double [2][range];
		double i_min = min(Sim.toArray());
		double i_max = max(Sim.toArray());
		double step = (i_max-i_min)/range, A, B, C, V1, V2, V3, p1, p2, p3, v;
		
		V1 = parameters.get(0);
		V2 = parameters.get(2);
		V3 = parameters.get(1);
		A = parameters.get(3);
		B = parameters.get(4);
		C = A;//parameters.get(5);
		
		for(int i = 0; i < Sim.size(); i++){
			v = i_min + i*step;
			p1 = A/(Math.sqrt(Math.PI)*V1)*Math.exp(-((v*v)/(V1*V1)));
			p2 = B/(Math.sqrt(Math.PI)*V1)*Math.exp(-((v*v)/(V2*V2)));
			p3 = A/(Math.sqrt(Math.PI)*V1)*Math.exp(-((v*v)/(V3*V3)));
			
			P_X[0][i] = v;
			P_X[1][i] = p1 + p2 - p3;	
		}
		
//		System.out.println("The sum of the difference is..." + sumErr);
		return P_X;
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
