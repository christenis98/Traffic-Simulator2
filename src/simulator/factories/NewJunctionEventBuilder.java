package simulator.factories;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.model.DequeuingStrategy;
import simulator.model.Event;
import simulator.model.LightSwitchStrategy;
import simulator.model.NewJunctionEvent;

public class NewJunctionEventBuilder extends Builder<Event>{
	
	private Factory<LightSwitchStrategy> lss;
	private Factory<DequeuingStrategy> ds;
	private int time;
	private String id;
	private int xcoor;
	private int ycoor;
	private LightSwitchStrategy _lss;
	private DequeuingStrategy _ds;
	
	public NewJunctionEventBuilder (String string, Factory<LightSwitchStrategy> lssFactory,
			Factory<DequeuingStrategy> dqsFactory) {
		super(string);
		this.lss = lssFactory;
		this.ds = dqsFactory;
	}

	@Override
	protected NewJunctionEvent createTheInstance(JSONObject data) {
		
		time = data.getInt("time");
		id = data.getString("id");

		JSONArray jsarray = data.getJSONArray("coor");
		xcoor = jsarray.getInt(0);
		ycoor = jsarray.getInt(1);
		
		_lss = lss.createInstance(data.getJSONObject("ls_strategy"));
		_ds = ds.createInstance(data.getJSONObject("dq_strategy"));
		
		return new NewJunctionEvent(time, id, _lss, _ds, xcoor, ycoor);
	}

}

