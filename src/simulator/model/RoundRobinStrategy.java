package simulator.model;

import java.util.List;

public class RoundRobinStrategy implements LightSwitchStrategy{
	int timeS;

	public RoundRobinStrategy(int timeSlot) {
		this.timeS = timeSlot;
	}
	
	@Override
	public int chooseNextGreen(List<Road> roads, List<List<Vehicle>> qs, int currGreen, 
			int lastSwitchingTime, 	int currTime) {
		if (roads.size() ==0)
			return -1;
		if (currGreen == -1) 
			return 0;
		if (currTime-lastSwitchingTime <timeS) 
			return currGreen;
		else {
			return (currGreen + 1 ) % roads.size();
		}
		
	}
}