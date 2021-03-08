package simulator.view;

import java.awt.*;
import java.util.List;

import javax.swing.*;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;

public class StatusBar extends JPanel implements TrafficSimObserver {
	
	private static final long serialVersionUID = 1L;
	
	private JLabel time;
	private JLabel event;
	private JLabel _currTime;
	private JLabel _currEvent;
	
	public StatusBar(Controller controller) {
		initGUI();
		controller.addObserver(this);
	}

	private void initGUI() {
		
		setLayout(new FlowLayout(FlowLayout.LEFT));
		initLabels();
		
	}

	private void initLabels() {
		
		this._currTime = new JLabel();
		this._currEvent = new JLabel();
		this.time = new JLabel("Time: ");
		this.event = new JLabel("Event Added: ");
		this.add(time);
		this.add(_currTime);
		this.add(new JSeparator(SwingConstants.HORIZONTAL));
		this.add(event);
		this.add(_currEvent);
		this.add(new JSeparator(SwingConstants.HORIZONTAL));
	}

	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
		this._currTime.setText(String.valueOf(time));
        this._currEvent.setText("");
	}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		this._currTime.setText(String.valueOf(time));
        this._currEvent.setText("");
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		this._currTime.setText(String.valueOf(time));
        this._currEvent.setText(e.toString());
	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		this._currTime.setText(String.valueOf(0));
		this._currEvent.setText("Ninguno.");
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
		this._currTime.setText(String.valueOf(time));
        this._currEvent.setText("Wellcome");
	}

	@Override
	public void onError(String err) {
	}
	
}
