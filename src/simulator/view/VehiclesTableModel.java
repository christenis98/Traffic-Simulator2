package simulator.view;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;
import simulator.model.Vehicle;

public class VehiclesTableModel extends AbstractTableModel implements TrafficSimObserver {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String cols[] = {"ID", "Location", "Itinerary", "Cont Class", "Max Speed", "Actual Speed",
			"Total Cont", "Total Distance"};
	private List<Vehicle> _vehicles;
	
	public VehiclesTableModel(Controller ctrl) {
		_vehicles = new ArrayList<Vehicle>();
		ctrl.addObserver(this);
	}
	
	@Override
	public int getRowCount() {
		return this._vehicles.size();
	}
	
	@Override
	public int getColumnCount() {
		return cols.length;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Vehicle v = this._vehicles.get(rowIndex);
		String s = "";
		switch(columnIndex) {
		case 0:
			s+= v.getId();break;
		case 1:
			switch (v.getStatus()) {
				case PENDING: s+= "Pending";break;
				case TRAVELING: s+= v.getRoad() + ":" + v.getLocation();break;
				case WAITING: s+= "Waiting:" + v.getRoad().getDestination();break;
				case ARRIVED: s+= "Arrived";break;
			}
			break;
		case 2:
			s+= v.getItinerary();break;
		case 3:
			s+= v.getContClass();break;
		case 4:
			s+= v.getMaxSpeed();break;
		case 5:
			s+= v.getSpeed();break;
		case 6:
			s+= v.getTotalCont();break;
		case 7:
			s+= v.getTotalTravelledDistance();break;
		}
		return s;
	}
	
	@Override
	public String getColumnName(int columna) {
		return cols[columna];
	}

	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		this._vehicles = map.getVehicles();
		fireTableDataChanged();		
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		this._vehicles = map.getVehicles();
		fireTableDataChanged();			
	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		this._vehicles = map.getVehicles();
		fireTableDataChanged();			
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
		this._vehicles = map.getVehicles();
		fireTableDataChanged();			
	}

	@Override
	public void onError(String err) {
		// TODO Auto-generated method stub
		
	}

}
