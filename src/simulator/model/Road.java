package simulator.model;

import java.util.*;

import org.json.JSONArray;
import org.json.JSONObject;

public abstract class Road extends SimulatedObject {

	private int length;
	private int totalContamination;
	private Junction source;
	private Junction destination;
	private int currentSpeedLimit;
	private int maximumSpeed;
	private int contaminationAlarmLimit;
	private Weather weatherConditions;
	private List<Vehicle> vehicles;
	private ComparatorVehicle Comp;
	
	Road( String id, Junction srcJunc, Junction destJunc, int maxSpeed, int contLimit, int length,
			Weather weather) {
		super(id);
		if((maxSpeed < 0) || (contLimit < 0) || (length <= 0) ||
				(srcJunc== null)||(destJunc == null) ||(weather == null)) {
			throw new IllegalArgumentException("Argumentos erroneos.");
		} 
		this.totalContamination =0;
		
		this.weatherConditions= weather;
		this.vehicles = new ArrayList<Vehicle>();
		this.Comp = new ComparatorVehicle();
		this.source = srcJunc;
		this.currentSpeedLimit =0;
		this.destination = destJunc;
		this.maximumSpeed = maxSpeed;
		this.contaminationAlarmLimit = contLimit;
		this.length = length;
		this.source.addOutGoingRoad(this);
		this.destination.addIncommingRoad(this);
		}
	
	void enter(Vehicle v) {
		/*if((v.getSpeed() != 0)||(v.getLocation() != 0)) {
			throw new IllegalArgumentException("Argumentos invalidos.");
		}*/
		vehicles.add(v);
	}
	
	void exit(Vehicle v) {
		vehicles.remove(v);
	}
	
	void setWeather(Weather w) {
		this.weatherConditions = w;
	}
	
	public Weather getWeather() {
		return this.weatherConditions;
	}
	
	void addContamination(int c) {
		if(c<0) {
			throw new IllegalArgumentException("Contaminacion negativa?");
		}
		this.totalContamination += c;
	}
	
	abstract void reduceTotalContamination();

	abstract void updateSpeedLimit();
	
	abstract int calculateVehicleSpeed(Vehicle v);
	
	@Override
	void advance(int time) {
		this.reduceTotalContamination();
		this.updateSpeedLimit();
		for(Vehicle v : vehicles) {
			v.setSpeed(this.calculateVehicleSpeed(v));
			v.advance(time);
		}
		vehicles.sort(Comp);
	}
	
	public Junction getDestination() {
		return this.destination;
	}
	
	public Junction getSource() {
		return this.source;
	}

	@Override
	public JSONObject report() {
	
		JSONObject obj = new JSONObject();
		obj.put("id", this.toString());
		obj.put("speedlimit", this.currentSpeedLimit);
		obj.put("weather", this.weatherConditions);
		obj.put("co2", this.totalContamination);
		//obj.put("vehicles",vehicles);
		
		JSONArray ja = new JSONArray();
		
		obj.put("vehicles", ja); 
		
		for(Vehicle v: vehicles) {
			ja.put(v.getId());
		}
		
		return obj;
	}

	public int getLength() {
		return length;
	}
	
	public int getcontaminationAlarmLimit() {
		return contaminationAlarmLimit;
	}

	public void setLength(int length) {
		this.length = length;
	}
	
	public int getmaximumSpeed() {
		return this.maximumSpeed;
	}
	
	public List<Vehicle> getVehicles() {
		return this.vehicles;
	}
	
	public void setcurrentmaxSpeed(int speed) {
		this.currentSpeedLimit = speed;
	}

	public int getTotalContamination() {
		return totalContamination;
	}

	public void setTotalContamination(int totalContamination) {
		this.totalContamination = totalContamination;
	}
	
	public void addTotalContamination(int totalContamination) {
		this.totalContamination += totalContamination;
	}

}
