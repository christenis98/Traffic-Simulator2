package simulator.factories;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.misc.Pair;
import simulator.model.Event;
import simulator.model.NewSetWeatherEvent;
import simulator.model.Weather;

public class SetWeatherEventBuilder extends Builder<Event>{
	
	private int time;
	

	public SetWeatherEventBuilder(String type) {
		super(type);
	}

	@Override
	protected Event createTheInstance(JSONObject data) {
		
		this.time = data.getInt("time");
		
		JSONArray ja = data.getJSONArray("info");
		
		List<Pair<String,Weather>> ws = new ArrayList<Pair<String,Weather>>();
		
		for(int i = 0; i < ja.length(); i++) {
			ws.add(new Pair<String, Weather> (ja.getJSONObject(i).getString("road"),
					Weather.valueOf(ja.getJSONObject(i).getString("weather").toUpperCase())));
		}
		
		return new NewSetWeatherEvent(time, ws);
	}

}
