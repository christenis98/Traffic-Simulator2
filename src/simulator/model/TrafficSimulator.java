package simulator.model;

import java.util.*;

import org.json.JSONObject;

import simulator.misc.SortedArrayList;

public class TrafficSimulator implements Observable<TrafficSimObserver>{
	
	private RoadMap roadMap;
	private List<Event> listEvent;
	private int time;
	private List<TrafficSimObserver> observerList;
	private ComparatorEvent Comp;
	
	public TrafficSimulator(){
		this.roadMap = new RoadMap();
		this.listEvent = new ArrayList<Event>();
		this.time =0;
		this.observerList = new ArrayList<TrafficSimObserver>();
		this.Comp = new ComparatorEvent();
	}
	
	public void addEvent(Event e){
		this.listEvent.add(e);
		this.onEventAddedNotify(roadMap, listEvent,  time, e);
	}
	
	public void advance(){
		//1
		this.time++;
		listEvent.sort(Comp);
		//Notificamos a los observadores
		onAdvanceStartNotify(roadMap, listEvent, time);
			
		//2
		//int i = 0;
		
		while(this.listEvent.size() > 0 && listEvent.get(0).getTime() == time) {
			listEvent.remove(0).execute(roadMap);
			//i++;
		}
		
		/*for(int i =0; i < listEvent.size(); i++) {
			if(listEvent.get(i).getTime() == time) {
				listEvent.remove(i).execute(roadMap);
			}
		}*/

		//3
		for(Junction j: this.roadMap.getJunctions()) {
			j.advance(time);
		}
		//4
		for(Road r: this.roadMap.getRoads()) {
			r.advance(time);
		}
		
		//Notificamos a los observadores
		onAdvanceEndNotify(roadMap, listEvent, time);
	}
	
	public void onAdvanceStartNotify(RoadMap map, List<Event> events, int time) {
		for(TrafficSimObserver o : this.observerList) {	
			o.onAdvanceStart(map, events, time);
		}
	}
	
	//TODO
	public void onAdvanceEndNotify(RoadMap map, List<Event> events, int time) {
		for(TrafficSimObserver o : this.observerList) {	
			o.onAdvanceEnd(map, events, time);
		}
	}
	public void onEventAddedNotify(RoadMap map, List<Event> events, int time, Event e) {
		for(TrafficSimObserver o : this.observerList) {	
			o.onEventAdded(roadMap, listEvent, e, time);
		}
	}
	
	public void reset() {
		this.roadMap.reset();
		this.listEvent.clear();
		this.time =0;
		onResetNotify(roadMap, listEvent, time);
	}
	
	public void onResetNotify(RoadMap map, List<Event> events, int time) {
		for(TrafficSimObserver o : this.observerList) {	
			o.onReset(roadMap, listEvent, time);
		}
	}
	
	public JSONObject report() {
		
		JSONObject obj = new JSONObject();
		
		obj.put("time", this.time);
		
		obj.put("state", this.roadMap.report());
		
		return obj;
	}

	@Override
    public void addObserver(TrafficSimObserver o) { 
        try {
            this.observerList.add(o);
            this.notifyOnRegister();
        }
        catch(Exception e){
            this.notify_OnError("No se pudo add este observer");
        }
    }
	
	public void notify_OnError(String msg) {

        if(this.observerList.isEmpty()) {
            throw new NullPointerException("La lista de observers esta vacia.");
        }

        for(TrafficSimObserver o : this.observerList) {
            o.onError(msg);
        }
    }
	
	public void notifyOnRegister() {
		for(TrafficSimObserver o : this.observerList) {	
			o.onReset(roadMap, listEvent, time);
		}
	}

	@Override
	public void removeObserver(TrafficSimObserver o) {
		
		if(this.observerList.isEmpty()) {
			throw new NullPointerException("La ObserverList esta vacia.");
		}
		
		if(this.observerList.indexOf(o)==-1) {
			throw new NullPointerException("El Observer no esta en la lista.");
		}
		
		this.observerList.remove(o);
		
	}
	
	public int getTime() {
		return this.time;
	}


	
}
