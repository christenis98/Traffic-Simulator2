package simulator.model;

import java.util.List;

import simulator.misc.Pair;

public class NewSetWeatherEvent extends Event{
		
	private List<Pair<String, Weather>> aux;
	
	public NewSetWeatherEvent(int time, List<Pair<String,Weather>> ws) {
		super(time);
		if(ws.isEmpty()) {
			throw new IllegalArgumentException("Argumentos erroneos.");
		}
		aux = ws;		
	}

	@Override
	void execute(RoadMap map) {
		for(Pair<String, Weather> w: aux) {
			if(map.getRoad(w.getFirst())!= null) {
				map.getRoad(w.getFirst()).setWeather(w.getSecond());
			}else {
				throw new NullPointerException("Road is not in the road map.");
			}
			
		}
	}
	/*
	@Override
	void execute(RoadMap map) {
		for(Pair<String, Weather> c: aux) {
			Road r = map.getRoad(c.getFirst());
			if(c == null){
				throw new IllegalArgumentException("Road no puede ser null.");
			}
			r.setWeather(c.getSecond());
		}
	}*/
	
	@Override
	public String toString() {
		return "New SetWeather ' " + this._time + " ' "+ this.aux.get(0).getFirst()
				+ " ' "+ this.aux.get(0).getSecond() +" ' " ;
	}
}
