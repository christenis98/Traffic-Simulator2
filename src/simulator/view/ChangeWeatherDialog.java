package simulator.view;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import simulator.model.Road;
import simulator.model.Weather;

public class ChangeWeatherDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	
	//private Road _road;
	private int _status;
	private JLabel desc;
	private JLabel road;
	private JLabel weather;
	private JLabel ticks;
	private JButton cancelButton;
	private JButton okButton;
	private JSpinner ticks_spinner;
	private JComboBox<Road> _roads;
	private JComboBox<Weather> _weathers;
	private DefaultComboBoxModel<Road> _roadmodel;
	private DefaultComboBoxModel<Weather> _weathermodel;

	

	public ChangeWeatherDialog(Frame padre) {
		super(padre, true);
		initGUI();
	}

	private void initGUI() {
		_status = 0;
		setTitle(" Change Weather");
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		setContentPane(mainPanel);
		
		String stringdesc = "<html><p>Schedule an event to change the weather of a road after a given"
                + "number of simulation ticks from now.</p></html>";
		
		desc = new JLabel(stringdesc);
		
		desc.setAlignmentX(CENTER_ALIGNMENT);
		mainPanel.add(desc);
		
		mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
		
		JPanel viewsPanel = new JPanel();
        viewsPanel.setAlignmentX(CENTER_ALIGNMENT);
        mainPanel.add(viewsPanel);
        
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        
      //BOtones
      	JPanel buttonsPanel = new JPanel();
      	buttonsPanel.setAlignmentX(CENTER_ALIGNMENT);
      	mainPanel.add(buttonsPanel);
		
		road =new JLabel("Road: ");
		road.setAlignmentX(CENTER_ALIGNMENT);
		viewsPanel.add(road);
		//combo box de road
		_roadmodel = new DefaultComboBoxModel<>();
		_roads = new JComboBox<>(_roadmodel);
		_roads.setAlignmentX(LEFT_ALIGNMENT);
		viewsPanel.add(_roads);
		
		weather =new JLabel("Weather: ");
		weather.setAlignmentX(CENTER_ALIGNMENT);
		viewsPanel.add(weather);
		//combo box de weather
		_weathermodel = new DefaultComboBoxModel<>();
		_weathers = new JComboBox<>(_weathermodel);
		_weathers.setAlignmentX(LEFT_ALIGNMENT);
		viewsPanel.add(_weathers);
		
		
		ticks =new JLabel("Ticks: ");
		ticks.setAlignmentX(CENTER_ALIGNMENT);
		viewsPanel.add(ticks);
		createStepsSpinner(viewsPanel);
		
		
		//cancel
		cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				_status = 0;
				ChangeWeatherDialog.this.setVisible(false);
			}
		});
		buttonsPanel.add(cancelButton);

		//ok
		okButton = new JButton("OK");
		okButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if ((_roadmodel.getSelectedItem() != null) &&(_weathermodel.getSelectedItem() != null)) {
					_status = 1;
					ChangeWeatherDialog.this.setVisible(false);
				}
			}
		});
		buttonsPanel.add(okButton);

		setPreferredSize(new Dimension(500, 200));
		pack();
		setResizable(false);
		setVisible(false);
	}
	
	public int open(List<Road> roadlist) {

		// update the comboxBox model -- if you always use the same no
		// need to update it, you can initialize it in the constructor.
		//
		_roadmodel.removeAllElements();
		for (Road r : roadlist)
			_roadmodel.addElement(r);

		_weathermodel.removeAllElements();
		for (Weather w : Weather.values())
			_weathermodel.addElement(w);
		
		// You can chenge this to place the dialog in the middle of the parent window.
		// It can be done using uing getParent().getWidth, this.getWidth(),
		// getParent().getHeight, and this.getHeight(), etc.
		//
		setLocation(getParent().getLocation().x + 10, getParent().getLocation().y + 10);

		setVisible(true);
		return _status;
	}
	
	private void createStepsSpinner(JPanel panel) {
		
		SpinnerNumberModel model = new SpinnerNumberModel(1, 1, 9999, 1);
		ticks_spinner = new JSpinner(model);
		ticks_spinner.setMinimumSize(new Dimension(80, 30));
		ticks_spinner.setMaximumSize(new Dimension(200, 30));
		ticks_spinner.setPreferredSize(new Dimension(80, 30));		
		
		panel.add(ticks_spinner);
		
	}


	public int getTicks() {
		return (int) ticks_spinner.getValue();
	}

	public Road getRoad() {
		return (Road) this._roads.getSelectedItem();
	}

	public Weather getWeather() {
		return (Weather) this._weathers.getSelectedItem();
	}
	
	
	
}
