package simulator.model;

public class NewCityRoadEvent extends Event{

	private String id;
	private String srcJunc;
	private String destJunc;
	private int length;
	private int co2Limit;
	private int maxSpeed;
	private Weather weather;
	
	public NewCityRoadEvent(int time, String id, String srcJun, String destJunc,
			int length, int co2Limit, int maxSpeed, Weather weather) {
		super(time);
			this.id = id;
			this.srcJunc = srcJun;
			this.destJunc = destJunc;
			this.length = length;
			this.co2Limit = co2Limit;
			this.maxSpeed = maxSpeed;
			this.weather = weather;
			
		}

	@Override
	void execute(RoadMap map) {
		map.addRoad(new CityRoad(id, map.getJunction(srcJunc), map.getJunction(destJunc), maxSpeed, co2Limit, length, weather));
		//String id, Junction srcJunc, Junction destJunc, int maxSpeed, int contLimit, int length, Weather weather)
	}
	
	@Override
	public String toString() {
		return "New CityRoad ' " + this.id + " ' " ;
	}

}
