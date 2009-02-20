import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class DataGeneration {
	
	public static void main(String[] args){
		SimulationData data;
		Configurations config;
		ObjectOutputStream file;
		
		config = new Configurations();
		for(int i = 0; i < 1; i++){
			data = new SimulationData(1);
			
			for(int j = -10; j < 0; j++){
				data.addVelocity(j+10);
			}
			
			for(int j = 0; j < 11; j++){
				data.addVelocity(10-j);
			}
			
//			for(int j = 0; j < 20; j++){
//				data.addVelocity(1);
//			}
			config.addConfiguration(data);
		}
		
		for (int i = 0; i < config.size(); i++) {
			data = config.get(i);
			System.out.println("Configuration " + i + " has " + data.size() + " simulations at a detuning of " + data.frequency() +".");
		}
		
		try{
			file = new ObjectOutputStream(new FileOutputStream("simulated_data.dat"));
			file.writeObject(config);
			file.close();
		}catch(IOException caught){
			System.err.println(caught);
		}
	}
}
