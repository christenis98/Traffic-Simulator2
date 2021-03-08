package simulator.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

public class Junction extends SimulatedObject{

	

	private List<Road> roadEntradaList;
	private Map<Junction, Road> roadSalidaMap;
	private List<List<Vehicle>> colaList;
	private Map<Road,List<Vehicle>> roadcolaList;
	private int lastNumSemaforo;
	private LightSwitchStrategy lswStrategy;
	private DequeuingStrategy dqeStrategy;
	private int x;
	private int y;
	private int currGreen;
	
	Junction(String id, LightSwitchStrategy lsStrategy,
			DequeuingStrategy dqStrategy, int xCoor, int yCoor) {
		
		super(id);
		
		if((lsStrategy == null) || (dqStrategy == null) || (xCoor < 0) || (yCoor < 0)) {
			throw new IllegalArgumentException("Argumentos erroneos.");
		} 
		this.roadEntradaList = new ArrayList<Road>();
		this.roadSalidaMap = new HashMap<Junction, Road>();
		this.colaList = new ArrayList<List<Vehicle>>();
		this.roadcolaList = new HashMap<Road,List<Vehicle>>();
		this.lastNumSemaforo = 0;
		this.lswStrategy = lsStrategy;
		this.dqeStrategy = dqStrategy;
		this.x = xCoor;
		this.y = yCoor;
		this.currGreen = -1;
	}
	
	void addIncommingRoad(Road r){
		
		if(r.getDestination() != this) {
			throw new IllegalArgumentException("Este cruce no es el destino.");
		}
		
		this.roadEntradaList.add(r);
		List<Vehicle> vehicleList =new ArrayList<Vehicle>();
		this.colaList.add(vehicleList);
		this.roadcolaList.put(r, vehicleList);
			
	}
	
	void addOutGoingRoad(Road r){
		if(r.getSource() != this) {
			throw new IllegalArgumentException("Este cruce no es el destino.");
		}
		
		if(this.roadSalidaMap.containsValue(r.getDestination())){
			throw new IllegalArgumentException("Ya hay una carretera que va a ese cruce.");
		}
		
		this.roadSalidaMap.put(r.getDestination(), r);
	}
	
	void Enter(Vehicle v) {
		this.roadcolaList.get(v.getRoad()).add(v);
	}
	
	Road roadTo(Junction j) {
		return this.roadSalidaMap.get(j);
	}
	
	@Override
	void advance(int time) {
		//paso 1
		if(this.currGreen != -1) {
			if(!this.colaList.get(this.currGreen).isEmpty()) {
				List<Vehicle> vMove = this.dqeStrategy.dequeue(colaList.get(currGreen));
				//List<Vehicle> cola = colaList.get(currGreen);
				for(Vehicle v: vMove) {
					Road r = v.getRoad();
					v.moveToNextRoad();
					if(v.getStatus() != VehicleStatus.ARRIVED) {
						if(v.getRoad().getId() != r.getId()) {
							this.roadcolaList.get(r).remove(v);
						}
					}
					else {
						this.colaList.get(this.currGreen).remove(v);
					}
				}
			}
		}
		//paso 2
		int next = this.lswStrategy.chooseNextGreen
				(this.roadEntradaList, colaList,currGreen, this.lastNumSemaforo, time);
		if(next != currGreen) {
			currGreen = next;
			this.lastNumSemaforo = time;
		}
	}

	@Override
	public JSONObject report() {
		
		JSONObject obj = new JSONObject();
		
		obj.put("id", this.getId());
		
		if (this.currGreen == -1)
			obj.put("green", "none");
		else
			obj.put("green", this.roadEntradaList.get(this.currGreen).getId());
		
		JSONArray colas = new JSONArray();
		
		obj.put("queues", colas);
		
		//int i = 0; i < this.roadEntradaList.size(); i++
		
		for (Road i: this.roadEntradaList) {
			JSONObject ob = new JSONObject();
			colas.put(ob);
			ob.put("road", i.getId());
			
			JSONArray veh = new JSONArray();
			
			ob.put("vehicles", veh);
			
			for(Vehicle v: this.roadcolaList.get(i)) {
				veh.put(v.getId());
			}
		}
		
		return obj;
	}

	public int getX() {
		
		return this.x;
	}
	public int getY() {
	
		return this.y;
	}
	
	public int getCurrGreen() {
		return this.currGreen;
	}

	public List<Road> getInRoads(){
		return Collections.unmodifiableList(this.roadEntradaList);
		
	}

}
