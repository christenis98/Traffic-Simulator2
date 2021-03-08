package simulator.control;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import simulator.factories.Factory;
import simulator.model.Event;
import simulator.model.TrafficSimObserver;
import simulator.model.TrafficSimulator;

public class Controller {
	private TrafficSimulator simulator;
	private Factory<Event> evFactory;
	
	public Controller(TrafficSimulator sim, Factory<Event> eventsFactory)
	{
		if (sim == null && eventsFactory == null) 
			throw new IllegalArgumentException("Argumentos erroneos.");
			
		this.simulator = sim;
		this.evFactory = eventsFactory;
		
	}
	
	public void loadEvents(InputStream in) {
		JSONObject jo = new JSONObject(new JSONTokener(in));
		
		JSONArray events = jo.getJSONArray("events");
		if (events == null)
			throw new NullPointerException("Empty list or invalid format.");
		
		for (int i = 0; i < events.length(); i++) {
			this.simulator.addEvent(this.evFactory.createInstance(events.getJSONObject(i)));
		}
	}
	
	public void run (int n , OutputStream out) {
		
		PrintStream printer = new PrintStream(out);
		printer.println("{ \"states\": [");
		for (int i = 0; i < n; i++) {
			this.simulator.advance();
			printer.print(this.simulator.report());
			if (i != n-1) {
				printer.println(", ");
			}
		}
		printer.print("] }");
	}
	
	public void run (int n) {
		for (int i = 0; i < n; i++) {
			this.simulator.advance();
		}
	}
	
	public void reset() {
		this.simulator.reset();
	}
	
	public void addObserver(TrafficSimObserver	o) {
		this.simulator.addObserver(o);
	}
	
	public void removeObserver(TrafficSimObserver o) {
		this.simulator.removeObserver(o);
	}
	
	public void addEvent(Event e) {
		this.simulator.addEvent(e);
	}
	
	public int getTime() {
		return this.simulator.getTime();
	}

}