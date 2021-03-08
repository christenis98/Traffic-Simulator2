package simulator.model;

public class CityRoad extends Road {

	CityRoad(String id, Junction srcJunc, Junction destJunc, int maxSpeed, int contLimit, int length, Weather weather) {
		super(id, srcJunc, destJunc, maxSpeed, contLimit, length, weather);
	}

	@Override
	void reduceTotalContamination() {
		//SUNNY, CLOUDY, RAINY, WINDY, STORM
			switch (this.getWeather()) {
				case WINDY:
					this.setTotalContamination(((100-10)/100)*this.getTotalContamination());
				break;
				case STORM:
					this.setTotalContamination(((100-10)/100)*this.getTotalContamination());
				break;
				default:
					this.setTotalContamination(((100-2)/100)*this.getTotalContamination());
			}
	}

	@Override
	void updateSpeedLimit() {
		//siempre es la maxima, no cambia.
		this.setcurrentmaxSpeed(getmaximumSpeed());
	}

	@Override
	int calculateVehicleSpeed(Vehicle v) {
		return ((int)(((11.0-v.getcontaminationClass())/11.0)*this.getmaximumSpeed()));
	}

}
