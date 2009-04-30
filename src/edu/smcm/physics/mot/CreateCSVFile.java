package edu.smcm.physics.mot;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintStream;

public class CreateCSVFile {
	
	private SimulationData Sim;
	
	public CreateCSVFile(String fileName){
		getSimulations(fileName);
	}
	
	private void getSimulations(String fileName){
		Configurations data;
		ObjectInputStream file;
		
		try{
			data = new Configurations();
			file = new ObjectInputStream(new FileInputStream(fileName));
			data = (Configurations) file.readObject();
			file.close();
			
			for(int a = 0; a < data.size(); a++){
				Sim = data.get(a);
				System.out.println("simulation " + a + "'s size is..." + Sim.numberOfParticles());
				sendToCSV(Sim);
			}
			
			System.out.println("DONE!");
		}catch(IOException caught){
			System.err.println(caught);
		}catch(ClassNotFoundException caught){
			System.err.println(caught);
		}	
	}

	private void sendToCSV(SimulationData s){
		PrintStream datafile;
		try{
			datafile = new PrintStream(new File("Simulation at " + -(int)s.frequency() + " Hz.csv"));
			for (Particle particle : s) {
				datafile.println(particle.velocity());
				System.out.println(particle.velocity());
			}
			
			datafile.close();
		}catch(Exception caught){
			System.err.println(caught);
		}		
	}
}
