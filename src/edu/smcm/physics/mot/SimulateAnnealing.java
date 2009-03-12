import java.util.ArrayList;
import java.util.Random;

public class SimulateAnnealing{
	
	static final double Lambda_not = 780.24 * Math.pow(10, -9);
	static final double k = 2 * Math.PI / Lambda_not;
	static final double Gamma = 6e6;//2 * Math.PI * 3E6;// 1 / tau;
	private static SimulationData Sim; 
	private static double[][] rawData;
	private static double[][] P_X;

	public SimulateAnnealing(SimulationData Sim){
		SimulateAnnealing.Sim = Sim;
		
		ArrayList<Double> parameters = new ArrayList<Double>();
		int b = 0, range = Sim.size();
		P_X = new double [2][range];
		double e, cnt = 1, variable, up = 0, down = 0, temp;
		double V1, V2, V3, A, B, C, Ti, Tf, Err, T = 10000;
		Random r = new Random();
		
		Ti = 1;
		Tf = 100e-6;
		V1 = Equations.V_calc(Ti);
		V2 = Equations.V_calc(Tf);
		V3 = (2*Gamma)/k;//should be capture velocity, Foot page 190.
		A = 0.1;
		B = A*10;
		C = A;
		
		parameters.add(V1);
		parameters.add(V2);
		parameters.add(V3);
		parameters.add(A);
		parameters.add(B);
		parameters.add(C);
		
		SimulateAnnealing.P_X = expCurve(parameters);
		Err = e = sumSqErr();
//		System.out.println("Initial Error is..." + e);

		while(T > 0.0001){// && e > emax){
//			System.out.println("Count = " + cnt);
//			b = r.nextInt(6);
		for(b = 0; b < 6; b++){
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
//				System.out.println("down, Error = " + down);
			}else{
				Err = up;
				parameters.set(b, variable + variable * 0.5);
//				System.out.println("up, Error = " + up);
			}
			
			if(Err < e){
				e = Err;
//				System.out.println("Error = " + e);
//				System.out.println("");
			}else if(Math.exp(-(Err/(k*T))) > r.nextDouble()){
//				System.out.println("Probability = " + (Math.exp(-(Err/(k*T)))));	
//				System.out.println("Error = " + Err);
//				System.out.println("");
				e = Err;							
			}
			T = T-T/2;
		}
		cnt = cnt+1;
		}	
		
		System.out.println("P(x) = " + parameters.get(3) + " * p(V2 = " + parameters.get(0) + ") + " + parameters.get(4) + " * p(V1 = " + parameters.get(1) + ") + " + parameters.get(5) + "p * (V3 = " + parameters.get(2) + ")");
//		System.out.println("V1 = " + parameters.get(0));
		System.out.println("V2 = " + parameters.get(1));
//		System.out.println("V3 = " + parameters.get(2));
//		System.out.println("A = " + parameters.get(3));
		System.out.println("B = " + parameters.get(4));
//		System.out.println("C = " + parameters.get(5));
		
	}
	
	private static double sumSqErr(){//SimulationData Sim, double[][] P_X, double[][] rawData){
		double sumErr = 0;
		int range = Sim.size();
		rawData = DataAnalysis.binnedData(Sim.toArray(), range);
		
		for(int i = 0; i < range; i++){
			sumErr = sumErr + DataAnalysis.square(P_X[1][i] - rawData[1][i]/1000);
		}
		return Math.abs(sumErr);
	}

	private static double[][] expCurve(ArrayList<Double> parameters){
		int range = Sim.size();		
		double i_min = DataAnalysis.min(Sim.toArray());
		double i_max = DataAnalysis.max(Sim.toArray());
		double step = (i_max-i_min)/range, A, B, C, V1, V2, V3, p1, p2, p3, v;
		
		V1 = parameters.get(0);
		V2 = parameters.get(1);
		V3 = parameters.get(2);
		A = parameters.get(3);
		B = parameters.get(4);
		C = parameters.get(5);
		
		for(int i = 0; i < Sim.size(); i++){
			v = i_min + i*step;
			p1 = A/(Math.sqrt(Math.PI)*V1)*Math.exp(-((v*v)/(V1*V1)));
			p2 = B/(Math.sqrt(Math.PI)*V1)*Math.exp(-((v*v)/(V2*V2)));
			p3 = C/(Math.sqrt(Math.PI)*V1)*Math.exp(-((v*v)/(V3*V3)));
			
			P_X[0][i] = v;
			P_X[1][i] = p1 + p2 - p3;	
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