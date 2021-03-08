package simulator.view;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.Road;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;

public class RoadsTableModel extends AbstractTableModel implements TrafficSimObserver  {
	
	private static final long serialVersionUID = 1L;
	
	private List<Road> _roads;
	private String cols[] = {"ID", "Lenght", "Weather", "Max Speed", "Speed Limit", "Total cont", "Max Cont"};
	
	public RoadsTableModel(Controller ctrl) {
		ctrl.addObserver(this);
		_roads = new ArrayList<Road>();
	}

	@Override
	public int getRowCount() {
		return _roads.size();
	}
	
	@Override
	public int getColumnCount() {
		// TODO Auto-generated method stub
		return cols.length;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Road r = _roads.get(rowIndex);
		String s = "";
		
		switch(columnIndex) {
		case 0: s += r.getId();break;
		case 1: s += r.getLength();break;
		case 2: s+= r.getWeather();break;
		case 3:	s+= r.getmaximumSpeed();break;
		case 4: s+= r.getmaximumSpeed();break;
		case 5: s+= r.getTotalContamination();break;
		case 6: s+= r.getcontaminationAlarmLimit();break;
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
		this._roads = map.getRoads();
		fireTableDataChanged();			
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		this._roads = map.getRoads();
		fireTableDataChanged();			
	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		this._roads = map.getRoads();
		fireTableDataChanged();			
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
		this._roads = map.getRoads();
		fireTableDataChanged();			
	}

	@Override
	public void onError(String err) {
		// TODO Auto-generated method stub
		
	}
}
