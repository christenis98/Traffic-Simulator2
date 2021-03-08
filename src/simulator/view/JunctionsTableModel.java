package simulator.view;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.Junction;
import simulator.model.Road;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;
import simulator.model.Vehicle;

public class JunctionsTableModel extends AbstractTableModel implements TrafficSimObserver{
	
	private List<Junction> _junctions;
	private String cols[] = {"ID", "Current green", "Queue"};

	private static final long serialVersionUID = 1L;
	
	public JunctionsTableModel(Controller ctrl) {
		ctrl.addObserver(this);
		_junctions = new ArrayList<Junction>();
	}
	@Override
	public int getRowCount() {
	
		return _junctions.size();
	}

	@Override
	public int getColumnCount() {
		return cols.length;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Junction j = _junctions.get(rowIndex);
		String s= "";
		switch(columnIndex) {
		case 0: s += j.getId();break;
		case 1: s+= (j.getCurrGreen() == -1)? "NONE" : j.getInRoads().get(j.getCurrGreen());break;
		case 2: 
			String ret = ""; 
			for (Road r: j.getInRoads()) {
				ret += r.getId() + ":[";
				int i = 0;
				for (Vehicle v: r.getVehicles()) {
					ret += v.getId();
					i++;
					if (i <  r.getVehicles().size()) 
						ret += ",";
					else ret += "]\n";
					
				}
			}
			
			return ret;
		}
		return s;
	}

	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		this._junctions = map.getJunctions();
		fireTableDataChanged();
		
	}
	
	@Override
	public String getColumnName(int columna) {
		return cols[columna];
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		this._junctions = map.getJunctions();
		fireTableDataChanged();
		
	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		this._junctions = map.getJunctions();
		fireTableDataChanged();		
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
		this._junctions = map.getJunctions();
		fireTableDataChanged();		
	}

	@Override
	public void onError(String err) {
		// TODO Auto-generated method stub
		
	}

}
