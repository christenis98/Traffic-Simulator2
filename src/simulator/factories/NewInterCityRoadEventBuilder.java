package simulator.factories;

import org.json.JSONObject;

import simulator.model.Event;
import simulator.model.NewInterCityRoadEvent;
import simulator.model.Weather;

public class NewInterCityRoadEventBuilder extends Builder<Event> {
	
	private int time;
	private String id;
	private String src;
	private String dest;
	private int length;
	private int co2limit;
	private int maxspeed;
	private Weather weather;

	public NewInterCityRoadEventBuilder(String string) {
		super(string);
	}
	
	@Override
	protected NewInterCityRoadEvent createTheInstance(JSONObject data) {
		
		this.time = data.getInt("time");
		this.id = data.getString("id");
		this.src = data.getString("src");
		this.dest = data.getString("dest");
		this.length = data.getInt("length");
		this.co2limit = data.getInt("co2limit");
		this.maxspeed = data.getInt("maxspeed");
		this.weather = Weather.valueOf(data.getString("weather"));
		
		return new NewInterCityRoadEvent(time, id, src, dest, length, co2limit, maxspeed, weather);

	}
	
}