package simulator.factories;

//import java.lang.Object;

import org.json.JSONObject;

import simulator.model.LightSwitchStrategy;
import simulator.model.RoundRobinStrategy;

public class RoundRobinStrategyBuilder extends Builder<LightSwitchStrategy> {

	public RoundRobinStrategyBuilder(String string) {
		super(string);
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
		return new RoundRobinStrategy(timeslot);
	}

}
