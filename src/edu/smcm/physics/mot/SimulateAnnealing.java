package edu.smcm.physics.mot;
import java.util.ArrayList;
import java.util.Random;
import edu.smcm.mathcs.util.KDE;

/**
 *
 */
public class SimulateAnnealing{
	
	static final double Lambda_not = 780.24 * Math.pow(10, -9);
	static final double k = 2 * Math.PI / Lambda_not;
	static final double Gamma = 6e6;//2 * Math.PI * 3E6;// 1 / tau;
	private static SimulationData Sim; 
	private static double[][] P_X;
	private static double[][] normData;
	
	public SimulateAnnealing(SimulationData Sim_){
		Sim = Sim_;
		
		ArrayList<Double> parameters = new ArrayList<Double>();
		int range = Sim.size();
		normData = new double [2][range]; 
		P_X = new double [2][range];
		double[][]p_x = new double[2][range];
//		double[][]kde = KDE.SmoothIt(Sim); 
		double e, cnt = 1, up = 0, down = 0;
		double V1, V2, V3, A, B, C;
		LineGraph myLine;
		Random r = new Random();

		V1 = 20;//Equations.V_calc(1);
		V2 = 0.05;//Equations.V_calc(100e-6);
		V3 = 2*(2*Gamma)/k;//should be capture velocity, Foot page 190.		
		A = 155;
		B = A*40;
		C = A;

		parameters.add(V1);
		parameters.add(V2);
		parameters.add(V3);
		parameters.add(A);
		parameters.add(B);
		parameters.add(C);
		
		normData = DataAnalysis.binnedData(Sim.toArray(), range);	
		P_X = expCurve(parameters);
		
		for(int i  = 0; i < range; i++){
			p_x[0][i] = P_X[0][i];
			p_x[1][i] = P_X[1][i];
		}
		
		e = sumSqErr(P_X);
		
		System.out.println("Initial Error is..." + e);

		int n = 1;
		while(cnt < 1000){			
			for (int i = 0; i < 6; i++){
				int index = i;//r.nextInt(6);
				double element = parameters.get(index);
				double d = (n * 1);
				
				parameters.set(index, element + element*d);
				up = sumSqErr(expCurve(parameters));
				parameters.set(index, element);
				
				parameters.set(index, element - element*d);
				down = sumSqErr(expCurve(parameters));
				parameters.set(index, element);
	
				if (up >= e && down >= e){
					n = n + 1;
				}else if (up < down && up < e){
					parameters.set(index, element + element*d);
					e = up;
				}else if (down < e){
					parameters.set(index, element - element*d);
					e = down;
				}
				cnt = cnt+1;
			}
		}	
		System.out.println("Final Error........" + e);
		myLine = new LineGraph("Data, Expected, and Fitted Curves", "Velocity (m/s)", "Probability");
		myLine.addSeries("Simulated Data", P_X);
		myLine.addSeries("Expected", p_x);
		myLine.addSeries("Normalized Data", normData);
		myLine.plotIT();
		System.out.println("P(x) = " + parameters.get(3) + " * p(V2 = " + parameters.get(0) + ") + " + parameters.get(4) + " * p(V1 = " + parameters.get(1) + ") + " + parameters.get(5) + "p * (V3 = " + parameters.get(2) + ")");
		System.out.println("A = " + parameters.get(3));
		System.out.println("B = " + parameters.get(4));
	}
	
	private static double sumSqErr(double arr[][]){
		double sumErr = 0;
		int i_max = arr.length;
		
		for(int i = 0; i < i_max; i++){
//			if(arr[1][i]*10000 > 0){
//				System.out.println("Guess distribution is..." + arr[1][i]);
//			}
			sumErr = sumErr + DataAnalysis.square(arr[1][i]*10000 - normData[1][i]);
		}
		return sumErr;
	}

	private static double[][] expCurve(ArrayList<Double> parameters){
		int range = Sim.size();		
		double i_min = DataAnalysis.min(Sim.toArray());
		double i_max = DataAnalysis.max(Sim.toArray());
		double step = (i_max-i_min)/range, A, B, C, V1, V2, V3, p1, p2, p3, v;
		
		V1 = parameters.get(0);
		V2 = 1/V1;//parameters.get(1);
		V3 = parameters.get(2);
		A = parameters.get(3);
		B = A*40;//parameters.get(4);
		C = A;//parameters.get(5);
		
		for(int i = 0; i < range; i++){
			v = i_min + i*step;
			p1 = A/(Math.sqrt(2*Math.PI)*V1)*Math.exp(-((v*v)/(V1*V1)));
			p2 = B/(Math.sqrt(2*Math.PI)*V1)*Math.exp(-((v*v)/(V2*V2)));
			p3 = C/(Math.sqrt(2*Math.PI)*V1)*Math.exp(-((v*v)/(V3*V3)));
			
			P_X[0][i] = v;
			P_X[1][i] = (p1 + p2 - p3);
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