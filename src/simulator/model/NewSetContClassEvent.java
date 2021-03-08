package simulator.model;

import java.util.List;
import simulator.misc.Pair;

public class NewSetContClassEvent extends Event{
	
	private List<Pair<String, Integer>> aux;
	
	public NewSetContClassEvent(int time, List<Pair<String,Integer>> cs) {
		super(time);
		if(cs == null) {
			throw new IllegalArgumentException("Argumentos erroneos.");
		}
		aux = cs;		
	}

	@Override
	void execute(RoadMap map) {
		for(Pair<String, Integer> c: aux) {
			Vehicle v = map.getVehicle(c.getFirst());
			if(v == null) {
				throw new IllegalArgumentException("Vehiculo no puede ser null.");
			}
			v.setContaminationClass(c.getSecond());
		}
	}
	
	@Override
	public String toString() {
		return "New SetContClass ' " + this._time + " ' "+ this.aux.get(0).getFirst()
				+ " ' "+ this.aux.get(0).getSecond() +" ' " ;
	}
}
