package simulator.view;

import java.util.ArrayList;
import java.util.List;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JToolBar;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import simulator.control.Controller;
import simulator.misc.Pair;
import simulator.model.Event;
import simulator.model.NewSetContClassEvent;
import simulator.model.NewSetWeatherEvent;
import simulator.model.Road;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;
import simulator.model.Vehicle;
import simulator.model.Weather;

public class ControlPanel extends JPanel implements TrafficSimObserver{
	
	private static final long serialVersionUID = 1L;
	
	private RoadMap _rmap;
	private Controller _controller;
	private int _ticks = 10;
	private boolean _stopped = false;
	private JToolBar toolbar;
	private JFileChooser fc;
	private JButton carga_button;
	private JButton cont_button;
	private JButton weather_button;
	private JButton run_button;
	private JButton stop_button;
	private JButton exit_button;
	private int time;

	private JLabel ticks_label;
	
	private JSpinner ticks_spinner;
	
	public ControlPanel(Controller controller) {
		_controller = controller;
		controller.addObserver(this);
		initGUI();
	}

	private void initGUI() {
		toolbar = new JToolBar();
		toolbar.setFloatable(false);
		setLayout(new BorderLayout());
		add(toolbar, BorderLayout.PAGE_START);
		
		//Carga del fichero de eventos
		createCargaButton();
		toolbar.addSeparator();
		
		//Cambio de la clase de contaminación de un vehículo
		createContButton();
		
		//Cambio de las condiciones atmosféricas de una carretera
		createWeatherButton();
		toolbar.addSeparator();
		
		// Run button
		createRunButton();
		
		// Stop
		createStopButton();
		toolbar.addSeparator();
		
		//Ticks
		ticks_label = new JLabel();
		ticks_label.setText(" Ticks: ");
		toolbar.add(ticks_label);
		createStepsSpinner();
		
		// Exit
		createExitButton();
	}

	private void createExitButton() {
		exit_button = new JButton();
		exit_button.setToolTipText("Termina la ejecucion del simulador.");
		Image img = null;
		try {
			img = ImageIO.read(new File("resources/icons/exit.png"));
		  } catch (IOException ex) {
		    JOptionPane.showMessageDialog(null,"Load image error.");
		  }
		exit_button.setIcon(new ImageIcon(img));
		exit_button.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				
			int res = JOptionPane.showConfirmDialog(null, "Are sure you want to quit?", "Quit", JOptionPane.YES_NO_OPTION);
			if (res == JOptionPane.YES_OPTION) System.exit(0);
				
			}
			
		});		
		
		toolbar.add( Box.createHorizontalGlue() );
		toolbar.add(exit_button);
		
	}

	private void createStopButton() {
		stop_button = new JButton();
		stop_button.setToolTipText("Para la ejecucion del simulador.");
		Image img = null;
		try {
			img = ImageIO.read(new File("resources/icons/stop.png"));
		  } catch (IOException ex) {
		    JOptionPane.showMessageDialog(null,"Load image error.");
		  }
		stop_button.setIcon(new ImageIcon(img));
		stop_button.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				stop();
			}

		});		
		
		toolbar.add(stop_button);
		
	}
	
	private void stop() {
		_stopped = true;
		
	}
	
	private void createStepsSpinner() {
		
		SpinnerNumberModel model = new SpinnerNumberModel(10, 1, 9999, 10);
		ticks_spinner = new JSpinner(model);
		ticks_spinner.setValue(_ticks);
		ticks_spinner.setMinimumSize(new Dimension(80, 30));
		ticks_spinner.setMaximumSize(new Dimension(200, 30));
		ticks_spinner.setPreferredSize(new Dimension(80, 30));
		ticks_spinner.addChangeListener(new ChangeListener() {

			public void stateChanged(ChangeEvent e) {
				
				_ticks = Integer.valueOf(ticks_spinner.getValue().toString());
				
			}
			
		});		
		
		toolbar.add(ticks_spinner);
		
	}
	
	private void run_sim( int n ) {

        if ( n > 0 && ! _stopped ) {
            try {
                _controller.run(1);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Error in run sim.");
                //_stopped = true ;
                return ;
            }

        SwingUtilities.invokeLater( new Runnable() {

            @Override
            public void run() {
                run_sim(n - 1);
            }

            });

        } else {
            enableToolBar(true);
            _stopped = true ;
        }
    }

	private void createRunButton() {
		run_button = new JButton();
		run_button.setToolTipText("Run del simulator.");
		Image img = null;
		try {
			img = ImageIO.read(new File("resources/icons/run.png"));
		  } catch (IOException ex) {
		    JOptionPane.showMessageDialog(null,"Load image error.");
		  }
		run_button.setIcon(new ImageIcon(img));
		
		run_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				play();			
			}
		});		
		toolbar.add(run_button);
	}
	
	void play() {
		_stopped = false;
		enableToolBar(false);
		run_sim((Integer)ticks_spinner.getValue());	
	}

	//No acabado
	private void createWeatherButton() {
		weather_button = new JButton();
		weather_button.setToolTipText("Cambio del clima de una carretera");
		Image img = null;
		try {
			img = ImageIO.read(new File("resources/icons/weather.png"));
		  } catch (IOException ex) {
		    JOptionPane.showMessageDialog(null,"Load image error.");
		  }
		weather_button.setIcon(new ImageIcon(img));
		
		weather_button.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				List<Road> listroads = new ArrayList<Road>();

		        for (Road r : _rmap.getRoads()) {
		            listroads.add(r);
		        }
				JFrame frame = new JFrame();
				ChangeWeatherDialog wdialog = new ChangeWeatherDialog(frame);
				int estado = wdialog.open(listroads);
				//TODO
				if (estado == 1) {
					List<Pair<String, Weather>> listaeventoRW = new ArrayList<Pair<String, Weather>>();
					Pair<String, Weather> eventoRW = new Pair<String, Weather>(wdialog.getRoad().getId(), wdialog.getWeather());
					listaeventoRW.add(eventoRW);
					NewSetWeatherEvent eventRW = new NewSetWeatherEvent(time+wdialog.getTicks(), listaeventoRW);
					_controller.addEvent(eventRW);
				}
				
			}
			
		});		
		
		toolbar.add(weather_button);
		
	}
	
	
	//TODO
	private void createContButton() {
		cont_button = new JButton();
		cont_button.setToolTipText("Cambio de la calse de contaminacion de un v.");
		Image img = null;
		try {
			img = ImageIO.read(new File("resources/icons/co2class.png"));
		  } catch (IOException ex) {
		    JOptionPane.showMessageDialog(null,"Load image error.");
		  }
		cont_button.setIcon(new ImageIcon(img));
		
		cont_button.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				List<Vehicle> listvehicles = new ArrayList<Vehicle>();

		        for (Vehicle v : _rmap.getVehicles()) {
		            listvehicles.add(v);
		        }
				JFrame frame = new JFrame();
				ChangeCO2ClassDialog cdialog = new ChangeCO2ClassDialog(frame);
				int estado = cdialog.open(listvehicles);
				//TODO
				if (estado == 1) {
					List<Pair<String, Integer>> listaeventoVC = new ArrayList<Pair<String, Integer>>();
					Pair<String, Integer> eventoVC = new Pair<String,Integer>(cdialog.getvehicle().getId(), cdialog.getco2());
					listaeventoVC.add(eventoVC);
					NewSetContClassEvent eventVC = new NewSetContClassEvent(time+cdialog.getTicks(), listaeventoVC);
					_controller.addEvent(eventVC);
				}
			}
			
		});		
		
		toolbar.add(cont_button);
		
	}	

	private void createCargaButton() {
		fc = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("JSON File", "json");
		fc.setFileFilter(filter);
		fc.setDialogTitle("Archivo para cargar los datos: ");
		
		carga_button = new JButton();
		carga_button.setToolTipText("Archivo para cargar datos");
		Image img = null;
		try {
			img = ImageIO.read(new File("resources/icons/open.png"));
		  } catch (IOException ex) {
		    JOptionPane.showMessageDialog(null,"Load image error.");
		  }
		carga_button.setIcon(new ImageIcon(img));
		carga_button.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				
				int ret = fc.showOpenDialog(fc);
				if (ret == JFileChooser.APPROVE_OPTION) {
					JOptionPane.showMessageDialog(fc, "Elegiste: " + fc.getSelectedFile());
				} else {
					JOptionPane.showMessageDialog(fc, "Error en la seleccion.");
				}
				
				File file = fc.getSelectedFile();
				InputStream inputStream;
				
				try {
					inputStream = new FileInputStream(file);
					_controller.reset();
					_controller.loadEvents(inputStream);
				} catch (FileNotFoundException e1) {
					JOptionPane.showMessageDialog(null, "Error en carga.");
				}					
				
			}
			
		});		
		
		toolbar.add(carga_button);
		
	}
	
	private void enableToolBar(boolean b) {
		this.carga_button.setEnabled(b);
		this.cont_button.setEnabled(b);
		this.weather_button.setEnabled(b);
		this.run_button.setEnabled(b);
		this.stop_button.setEnabled(b);
		this.exit_button.setEnabled(b);
		this.ticks_spinner.setEnabled(b);
	}

	@Override
	public void onAdvanceStart(RoadMap map, List<Event> events, int time) {
		_rmap = map;
        this.time = time;
	}

	@Override
	public void onAdvanceEnd(RoadMap map, List<Event> events, int time) {
		
		_rmap = map;
        this.time = time;
	}

	@Override
	public void onEventAdded(RoadMap map, List<Event> events, Event e, int time) {
		
		   _rmap = map;
	        this.time = time;
	}

	@Override
	public void onReset(RoadMap map, List<Event> events, int time) {
		
		_ticks = 10;
        this.time = time;
	}

	@Override
	public void onRegister(RoadMap map, List<Event> events, int time) {
		
		 _rmap = map;
	        this.time = time;
	}

	@Override
	public void onError(String err) {
		
		
	}

}
