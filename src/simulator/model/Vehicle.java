package simulator.model;

import org.json.JSONObject;
import java.util.*;

public class Vehicle extends SimulatedObject {
	
	private List<Junction> itinerary;
	private int maximumSpeed;
	private int currentSpeed;
	private VehicleStatus status;	
	private Road road;				
	private int location;
	private int contaminationClass;
	private int totalContamination;
	private int totalTravelledDistance;
	private int lastJunctionVisited;
	
	Vehicle(String id, int maxSpeed, int contClass, List<Junction> itinerary) {
		super(id);
		if(maxSpeed <= 0)
			throw new IllegalArgumentException("maxSpeed tiene que ser mayor que 0.");
		else
			this.maximumSpeed= maxSpeed;
		if((contClass < 0 ) || (contClass > 10))
			throw new IllegalArgumentException("ContClass tiene que ser un argumento valido.");
		else
			this.contaminationClass = contClass;
		if(itinerary.size() < 2)
			throw new IllegalArgumentException("El itinerario es menor que 2.");
		else {
			this.itinerary = Collections.unmodifiableList(new ArrayList<>(itinerary));
		}	
		this.location =0;
		this.totalContamination =0;
		this.totalTravelledDistance=0;
		this.currentSpeed=0;
		this.lastJunctionVisited = 0;
		this.road = null;
		this.status = VehicleStatus.PENDING;
	}
	
	public int getSpeed() {
		return this.currentSpeed;
	}
	
	public int getLocation() {
		return this.location;
	}

	void setSpeed(int s) {
		if(s < 0 )
			throw new IllegalArgumentException("No se puede introducir una S negativa.");
		else
			this.currentSpeed = (this.maximumSpeed < s)?this.maximumSpeed:s;
	}
	
	void setContaminationClass(int c) {
		if((c < 0 ) || (c > 10))
			throw new IllegalArgumentException("c tiene que ser un argumento valido.");
		else
			this.contaminationClass = c;
	}
	
	int getcontaminationClass() {
		return this.contaminationClass;
	}
	
	
	@Override
	void advance(int time) {
		if(this.status == VehicleStatus.TRAVELING) {
			
			int locationaux = this.location;
			this.location = this.location + this.currentSpeed;
			if(this.location >= this.road.getLength()) {
				this.location = this.road.getLength();
			}
			int c = 0;
			int d = this.location - locationaux;
			c = this.contaminationClass*(d);
			this.totalTravelledDistance += d;
			this.totalContamination += c;
			this.road.addTotalContamination(c);
			//(llamando al método enterde la clase Junction). Recuerda que debes modificarel estado del vehículo
			//c
			//TODO
			if(this.location >= this.road.getLength()) {
				//this.currentSpeed =0;
				this.lastJunctionVisited++;
				this.status = VehicleStatus.WAITING;
				this.itinerary.get(this.itinerary.indexOf(this.road.getDestination())).Enter(this);
			}
		}
	}
	
	void moveToNextRoad(){
		if(this.status != VehicleStatus.PENDING && this.status != VehicleStatus.WAITING) {
			throw new IllegalArgumentException("El coche no tiene un status valido.");
		}
		if(this.road != null) {
			this.road.exit(this);
		}
		if(this.lastJunctionVisited == this.itinerary.size()-1) {
			this.status = VehicleStatus.ARRIVED;
			this.road = null;
			this.location = 0;
		}
		/*else if(this.status == VehicleStatus.PENDING){
			this.road = this.itinerary.get(lastJunctionVisited);
			
		}*/
		else {
			this.road = this.itinerary.get(lastJunctionVisited).roadTo
					(this.itinerary.get(lastJunctionVisited+1));
			this.road.enter(this);
			this.location =0;
			this.status = VehicleStatus.TRAVELING;
		}
	}

	@Override
	public JSONObject report() {
		
		JSONObject obj = new JSONObject();
		obj.put("id", this.toString());				//!!!!!!!!!!!!!
		if(this.status == VehicleStatus.TRAVELING){
			obj.put("speed", this.currentSpeed);
		}
		else {obj.put("speed", 0);}
		obj.put("distance", this.totalTravelledDistance);
		obj.put("co2", this.totalContamination);
		obj.put("class", this.contaminationClass);
		obj.put("status", this.status);
		if((this.status != VehicleStatus.PENDING) && (this.status != VehicleStatus.ARRIVED)) {
			obj.put("road", this.road.toString());		//!!!!!!!!!!!!!
			obj.put("location", this.location);
		}
		
		return obj;
	}

	public List<Junction> getItinerary() {
		return itinerary;
	}

	public void setItinerary(List<Junction> itinerary) {
		this.itinerary = itinerary;
	}
	
	public Road getRoad() {
		return this.road;
	}
	
	public VehicleStatus getStatus() {
		return this.status;
	}
	
	public int getContClass() {
		return this.contaminationClass;
	}
	
	public int getTotalCont() {
		return this.totalContamination;
	}
	
	public int getTotalTravelledDistance() {
		return this.totalTravelledDistance;
	}
	
	public int getMaxSpeed() {
		return this.maximumSpeed;
	}

}
