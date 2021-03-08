package simulator.model;

import java.util.*;

import org.json.JSONArray;
import org.json.JSONObject;

public class RoadMap {
	private List<Junction> junctionList;
	private List<Road> roadList;
	private List<Vehicle> vehicleList;
	private Map<String, Junction> junctionMap;
	private Map<String,Road> roadMap;
	private Map<String, Vehicle> vehicleMap;
	
	RoadMap() {
		this.junctionList = new ArrayList<Junction>();
		this.junctionMap = new HashMap<String, Junction>();
		this.roadList = new ArrayList<Road>();
		this.roadMap = new HashMap<String, Road>();
		this.vehicleList = new ArrayList<Vehicle>();
		this.vehicleMap = new HashMap<String, Vehicle>();
	}
	
	void addRoad(Road r) {
		this.roadList.add(r);
		this.roadMap.put(r.getId(), r);
	}
	
	public void addJunction(Junction j) {
		this.junctionList.add(j);
		this.junctionMap.put(j.getId(), j);
	}
	
	void addVehicle(Vehicle v) {
		this.vehicleList.add(v);
		this.vehicleMap.put(v.getId(), v);
	}
	
	public Junction getJunction(String id) {
		return this.junctionMap.get(id);
	}
	
	public Road getRoad(String id) {
		return this.roadMap.get(id);
	}
	
	public Vehicle getVehicle(String id) {
		return this.vehicleMap.get(id);
	}
	
	public List<Junction> getJunctions(){
		return Collections.unmodifiableList(junctionList);
		
	}
	
	public List<Road> getRoads(){
		return Collections.unmodifiableList(roadList);
		
	}
	public List<Vehicle> getVehicles(){
		return Collections.unmodifiableList(vehicleList);
	}
	
	
	
	void reset() {
		this.roadList.clear();
		this.vehicleList.clear();
		this.junctionList.clear();
		this.roadMap.clear();
		this.vehicleMap.clear();		
		this.junctionMap.clear();
	}
	
	public JSONObject report() {
		JSONObject obj = new JSONObject();
		
		JSONArray ja = new JSONArray();
		obj.put("junctions", ja); 
		
		for(Junction j: junctionList) {
			ja.put(j.report());
		}
		
		JSONArray ra = new JSONArray();
		obj.put("roads", ra); 
		
		for(Road r: roadList) {
			ra.put(r.report());
		}
		
		JSONArray va = new JSONArray();
		obj.put("vehicles", va); 
		
		for(Vehicle v: vehicleList) {
			va.put(v.report());
		}
		
		return obj;
	}

	
	
	
	
	
	
	
	
	
	
	
}
