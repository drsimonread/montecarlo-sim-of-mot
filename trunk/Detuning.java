import java.util.ArrayList;

public class Detuning {
	
	private double freq;
	private ArrayList<Double> freqList;

	public Detuning(){
		this.freqList = new ArrayList<Double>();
	}
	
	public void addDetuning(double detuning){
		this.freq = detuning;
		freqList.add(freq);
	}
	
	public double getDetuning(){
		return this.freq;
	}
	
	public int size(){
		return freqList.size();
	}
	
	public double get(int index){
		return freqList.get(index);
	}
}
