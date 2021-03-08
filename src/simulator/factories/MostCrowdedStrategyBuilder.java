package simulator.factories;

import org.json.JSONObject;

import simulator.model.LightSwitchStrategy;
import simulator.model.MostCrowdedStrategy;

public class MostCrowdedStrategyBuilder extends Builder<LightSwitchStrategy> {

	public MostCrowdedStrategyBuilder(String type) {
		super(type);
		
	}

	@Override
	protected LightSwitchStrategy createTheInstance(JSONObject data) {
		
		int timeslot=0;
		
		if(data.has("timeslot")) {
			timeslot= data.getInt("timeslot");
			if(timeslot == 0) {
				timeslot = 1;
			}
			
		}
		
		return new MostCrowdedStrategy(timeslot);
	
	}

}
