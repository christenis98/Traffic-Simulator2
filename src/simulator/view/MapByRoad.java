package simulator.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.Junction;
import simulator.model.Road;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;
import simulator.model.Vehicle;
import simulator.model.VehicleStatus;
import simulator.model.Weather;

public class MapByRoad extends JPanel implements TrafficSimObserver {
	
	private static final long serialVersionUID = 1L;

	private static final int _JRADIUS = 10;

	private static final Color _BG_COLOR = Color.WHITE;
	private static final Color _JUNCTION_COLOR = Color.BLUE;
	private static final Color _JUNCTION_LABEL_COLOR = new Color(200, 100, 0);
	private static final Color _GREEN_LIGHT_COLOR = Color.GREEN;
	private static final Color _RED_LIGHT_COLOR = Color.RED;

	private RoadMap _map;

	private Image _car;
	private Image _weather;
	private Image _cont;
	
	MapByRoad(Controller ctrl) {
		
		initGUI();
		ctrl.addObserver(this);
	}

	private void initGUI() {
		_car = loadImage("car.png");
	}
	
	public void paintComponent(Graphics graphics) {
		super.paintComponent(graphics);
		Graphics2D g = (Graphics2D) graphics;
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

		// clear with a background color
		g.setColor(_BG_COLOR);
		g.clearRect(0, 0, getWidth(), getHeight());

		if (_map == null || _map.getJunctions().size() == 0) {
			g.setColor(Color.red);
			g.drawString("No map yet!", getWidth() / 2 - 50, getHeight() / 2);
		} else {
			//updatePrefferedSize();
			drawMap(g);
		}
	}
	
	private void drawMap(Graphics g) {
		drawRoads(g);
		drawVehicles(g);
	}
	
	private void drawVehicles(Graphics g) {
		int i =0;
		for (Vehicle v : _map.getVehicles()) {
			if (v.getStatus() != VehicleStatus.ARRIVED) {

				Road r = v.getRoad();
				int x1 = 50;
				int y1 = (i+1)*50;
				int x2 = getWidth()-100;
				
				// Choose a color for the vehcile's label and background, depending on its
				// contamination class
				int vLabelColor = (int) (25.0 * (10.0 - (double) v.getContClass()));
				g.setColor(new Color(0, vLabelColor, 0));
				int xcar = x1 + (int) ((x2 - x1) * ((double) v.getLocation() / (double) r.getLength()));
				// draw an image of a car (with circle as background) and it identifier
				g.fillOval(xcar - 1, y1 - 6, 14, 14);	//Para que la imagen del coche se quede redonda
				g.drawImage(_car, xcar-1, y1-6, 16, 16, this);
				g.drawString(v.getId(), xcar, y1 - 6);
			}
			i++;
		}
		
	}

	private void drawRoads(Graphics g) {
		int i =0;
		int contNum = 0;
		//Junction j;
	
		for (Road r : _map.getRoads()) {
			//j = _map.getJunctions().get(i);
			
			// the road goes from (x1,y1) to (x2,y2)
			int x1 = 50;
			int y1 = (i+1)*50;
			int x2 = getWidth()-100;
			int y2 = (i+1)*50;

			// choose a color for the road depending on the total contamination, the darker
			// the
			// more contaminated (wrt its co2 limit)
			//Esta formula es para saber el color de la carretera
			int roadColorValue = 200 - (int) (200.0 * Math.min(1.0, (double) r.getTotalContamination() / (1.0 + (double) r.getcontaminationAlarmLimit())));
			Color roadColor = new Color(roadColorValue, roadColorValue, roadColorValue);
			g.setColor(roadColor);
			g.drawLine(x1, y1, x2, y2);
			g.setColor(_JUNCTION_COLOR);
			
			if(r.getWeather() == Weather.CLOUDY) {
				_weather = loadImage("cloud.png");
				g.drawImage(_weather, x2 + 10, y1 - 20, 32, 32, this);
			}
			if(r.getWeather() == Weather.RAINY) {
				_weather = loadImage("rain.png");
				g.drawImage(_weather, x2 + 10, y1 - 20, 32, 32, this);
			}
			if(r.getWeather() == Weather.STORM) {
				_weather = loadImage("storm.png");
				g.drawImage(_weather, x2 + 10, y1 - 20, 32, 32, this);
			}
			if(r.getWeather() == Weather.SUNNY) {
				_weather = loadImage("sun.png");
				g.drawImage(_weather, x2 + 10, y1 - 20, 32, 32, this);
			}
			if(r.getWeather() == Weather.WINDY) {
				_weather = loadImage("wind.png");
				g.drawImage(_weather, x2 + 10, y1 - 20, 32, 32, this);
			}
			
			g.setColor(Color.BLACK);
			g.drawString(r.getId(), x1/2, y1);	//Le pongo nombre a la carretera
			
			//calcular la contaminacion del 1 al 5
			contNum = (int)Math.floor(Math.min((double)r.getTotalContamination()/(1 + (double)r.getcontaminationAlarmLimit()),1.0) / 0.19);
			
			String contstring = "cont_" + String.valueOf(contNum) + ".png";
			
			_cont = loadImage(contstring);
			g.drawImage(_cont, x2 + 60, y1 - 20, 32, 32, this);
			
			//Draw Junctions:
			
			g.setColor(_JUNCTION_COLOR);
			g.fillOval(x1 - _JRADIUS / 2, y1 - _JRADIUS / 2, _JRADIUS, _JRADIUS);	//Dibujo el primer junction
			
			g.setColor(_JUNCTION_LABEL_COLOR);
			g.drawString(r.getSource().getId(), x1, y1+3);					//Le pongo nombre al junction
			
			g.setColor(_RED_LIGHT_COLOR);
			int idx = r.getDestination().getCurrGreen();
			if (idx != -1 && r.equals(r.getDestination().getInRoads().get(idx))) {	//Para ver en que color tengo que dibujar el junction.
				g.setColor(_GREEN_LIGHT_COLOR);
			}
			
			g.fillOval(x2 - _JRADIUS / 2, y2 - _JRADIUS / 2, _JRADIUS, _JRADIUS);	//Dibujo el segundo juntion
			
			g.setColor(_JUNCTION_LABEL_COLOR);	
			g.drawString(r.getSource().getId(), x1, y1+3);										//Le pongo nombre al junction

			
			i++;
		}
		
	}

	// loads an image from a file
	private Image loadImage(String img) {
		Image i = null;
		try {
			return ImageIO.read(new File("resources/icons/" + img));
		} catch (IOException e) {
		}
		return i;
	}
	
	public void update(RoadMap map) {
		_map = map;
		repaint();
	}
		@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
	}
		@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		update(map);
	}
		@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		update(map);
	}
		@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		update(map);
	}
		@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
		update(map);
	}
		@Override
	public void onError(String err) {
	}
}
