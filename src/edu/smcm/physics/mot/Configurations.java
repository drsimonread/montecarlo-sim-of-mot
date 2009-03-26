package edu.smcm.physics.mot;
import edu.smcm.physics.mot.SimulationData;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;


public class Configurations implements Serializable, Iterable<SimulationData>{
	private static final long serialVersionUID = 1L;
	private ArrayList<SimulationData> simulations;
	
	public Configurations(){
		this.simulations = new ArrayList<SimulationData>();
	}
	
	public void addConfiguration(SimulationData data){
		simulations.add(data);
	}
	
	public int size(){
		return simulations.size();
	}
	
	public Iterator<SimulationData> iterator()
	{
		return simulations.iterator();
	}

	public SimulationData get(int n) 
	{
		return simulations.get(n);
	}
	
}
