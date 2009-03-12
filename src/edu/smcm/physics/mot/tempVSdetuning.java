public class tempVSdetuning {
	
	private Temperature temp;
	private Detuning detune;
	
	public tempVSdetuning(){
		temp = new Temperature();
		detune = new Detuning();
	}
	
	public double getTemperature(){
		return temp.getTemperature(); 
	}
	
	public double getDetuning(){
		return detune.getDetuning();
	}
	
	public void addTvsd(double temperature, double detuning){
		temp.addTemperature(temperature);
		detune.addDetuning(detuning);
	}
	
	public double[][] toArray(){
		double[][] quick = new double[2][temp.size()];
		if (temp.size() == detune.size()){
			for(int i = 0; i < temp.size(); i++){
				quick[0][i] = detune.get(i);
				quick[1][i] = temp.get(i);
			}
		}else{
			System.out.println("Temperature and Detuning arrays are different sizes.");
		}
		return quick;
	}
}
