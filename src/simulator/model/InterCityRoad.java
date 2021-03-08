package simulator.model;

public class InterCityRoad extends Road{

	InterCityRoad(String id, Junction srcJunc, Junction destJunc, int maxSpeed, int contLimit, int length,
			Weather weather) {
		super(id, srcJunc, destJunc, maxSpeed, contLimit, length, weather);
		
	}

	@Override
	void reduceTotalContamination() {
		//SUNNY, CLOUDY, RAINY, WINDY, STORM
		switch (this.getWeather()) {
			case SUNNY:
					this.setTotalContamination(((100-2)/100)*this.getTotalContamination());
				break;
			case CLOUDY:
				this.setTotalContamination(((100-3)/100)*this.getTotalContamination());
			break;
			case RAINY:
				this.setTotalContamination(((100-10)/100)*this.getTotalContamination());
			break;
			case WINDY:
				this.setTotalContamination(((100-15)/100)*this.getTotalContamination());
			break;
			case STORM:
				this.setTotalContamination(((100-20)/100)*this.getTotalContamination());
			break;
		}
		
	}

	@Override
	void updateSpeedLimit() {
		if(this.getTotalContamination() > this.getcontaminationAlarmLimit()) {
			this.setcurrentmaxSpeed((int) (this.getmaximumSpeed()*0.5));//CASTEO A INT????????????????????? o lo quiere en double
		}else {
			this.setcurrentmaxSpeed(this.getmaximumSpeed());
		}
	}

	@Override
	int calculateVehicleSpeed(Vehicle v) {
		if(this.getWeather() == Weather.STORM) {
			return (int) (v.getSpeed()*0.8);
		}else {
			return v.getSpeed();
		}
	}

}
