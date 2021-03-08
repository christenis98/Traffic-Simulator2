package simulator.factories;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import simulator.misc.Pair;
import simulator.model.Event;
import simulator.model.NewSetContClassEvent;

public class SetContClassEventBuilder  extends Builder<Event> {
	
	private int time;
	
	public SetContClassEventBuilder(String type) {
		super(type);
	}

	@Override
	protected Event createTheInstance(JSONObject data) {
		
		this.time = data.getInt("time");
		
		JSONArray ja = data.getJSONArray("info");
		List<Pair<String,Integer>> vs = new ArrayList<Pair<String,Integer>>();
		for(int i = 0; i < ja.length(); i++) {
			JSONObject jo = ja.getJSONObject(i);
			vs.add(new Pair<String, Integer> (jo.getString("vehicle"), (jo.getInt("class"))));
		}
		
		return new NewSetContClassEvent(time, vs);
	}
	
}
