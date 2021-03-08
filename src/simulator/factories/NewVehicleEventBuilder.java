package simulator.factories;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.model.Event;
import simulator.model.NewVehicleEvent;

public class NewVehicleEventBuilder extends Builder<Event>{

	private int time;
	private String id;
	private int maxSpeed;
	private int contClass;
	
	public NewVehicleEventBuilder(String type) {
		super(type);
	}

	@Override
	protected NewVehicleEvent createTheInstance(JSONObject data) {
		
		this.time = data.getInt("time");
		this.id = data.getString("id");
		this.maxSpeed = data.getInt("maxspeed");
		this.contClass = data.getInt("class");
		
		JSONArray jsarray = data.getJSONArray("itinerary");
		
		List<String> _iti = new ArrayList<String>();
		
		for(int i =0; i < jsarray.length(); i++) {
			_iti.add(jsarray.getString(i));
		}
		
		return new NewVehicleEvent(time, id, maxSpeed, contClass, _iti);
	}

}