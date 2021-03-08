package simulator.view;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.TitledBorder;

import simulator.control.Controller;

public class MainWindow extends JFrame {
	
	private static final long serialVersionUID = 1L;
	
	private Controller _ctrl ;
	
	public MainWindow(Controller ctrl ) {
		super ( "Traffic Simulator" );
		_ctrl = ctrl ;
		initGUI();
	}
	
	private void initGUI() {
		JPanel mainPanel = new JPanel( new BorderLayout());
		this .setContentPane( mainPanel );
		
		mainPanel.add( new ControlPanel( _ctrl ), BorderLayout. PAGE_START );
		mainPanel.add( new StatusBar( _ctrl ),BorderLayout. PAGE_END );
		
		JPanel viewsPanel = new JPanel( new GridLayout(1, 2));
		mainPanel.add( viewsPanel , BorderLayout. CENTER );
		
		JPanel tablesPanel = new JPanel();
		tablesPanel.setLayout( new BoxLayout( tablesPanel , BoxLayout.Y_AXIS ));
		viewsPanel.add( tablesPanel );
		
		JPanel mapsPanel = new JPanel();
		mapsPanel.setLayout( new BoxLayout( mapsPanel , BoxLayout.Y_AXIS ));
		viewsPanel.add( mapsPanel );
		
		// tables
		
			//Tabla eventos:
				JPanel eventsView =
						createViewPanel( new JTable( new EventsTableModel( _ctrl )), "Events" );
				eventsView.setPreferredSize( new Dimension(500, 200));
				tablesPanel.add( eventsView );
			
			//Tabla vehiculos:
				JPanel vehiclesView =
						createViewPanel( new JTable( new VehiclesTableModel( _ctrl )), "Vehicles" );
				vehiclesView.setPreferredSize( new Dimension(500, 200));
				tablesPanel.add( vehiclesView );
			
			//Tabla carreteras:
				JPanel roadsView =
						createViewPanel( new JTable( new RoadsTableModel( _ctrl )), "Roads" );
				roadsView.setPreferredSize( new Dimension(500, 200));
				tablesPanel.add( roadsView );
			
			//Tabla cruces:
				JPanel junctionsView =
						createViewPanel( new JTable( new JunctionsTableModel( _ctrl )), "Junctions" );
				junctionsView.setPreferredSize( new Dimension(500, 200));
				tablesPanel.add( junctionsView );
		
		// maps
			
			//Map de arriba (el que tiene todo pintado)
				
			JPanel mapView = createViewPanel( new MapComponent( _ctrl ), "Map" );
			mapView.setPreferredSize( new Dimension(500, 400));
			mapsPanel.add(mapView);
			
			// DONe? add a map for MapByRoadComponent
			JPanel mapByRoadView = createViewPanel( new MapByRoad( _ctrl ), "Map by Road" );
			mapByRoadView.setPreferredSize( new Dimension(500, 400));
			mapsPanel.add(mapByRoadView);
		
		this.setDefaultCloseOperation( DO_NOTHING_ON_CLOSE );
		this.pack();
		this.setVisible( true );
	}
	
	private JPanel createViewPanel(JComponent c , String title ) {
		JPanel p = new JPanel( new BorderLayout() );
		//Titled borders
		TitledBorder _title;
		_title = BorderFactory.createTitledBorder(title);
		p.setBorder(_title);
		p .add( new JScrollPane( c ));
		return p ;
	}
}
