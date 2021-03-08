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

import simulator.model.Vehicle;


public class ChangeCO2ClassDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	
	//private Road _road;
	private int _status;
	private JLabel desclabel;
	private JLabel vehiclelabel;
	private JLabel co2classlabel;
	private JLabel tickslabel;
	private JButton cancelButton;
	private JButton okButton;
	private JSpinner ticks_spinner;
	private JComboBox<Vehicle> _vehicles;
	private JComboBox<Integer> _co2class;
	private DefaultComboBoxModel<Vehicle> _vehiclemodel;
	private DefaultComboBoxModel<Integer> _co2classmodel;
	
	public ChangeCO2ClassDialog(Frame padre) {
		super(padre, true);
		initGUI();
	}

	private void initGUI() {
		_status = 0;
		setTitle(" Change CO2 Class");
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		setContentPane(mainPanel);
		
		String stringdesc = "<html><p>Schedule an event to change the contamination class of a vehicle after a given"
                + "number of simulation ticks from now.</p></html>";
		
		desclabel = new JLabel(stringdesc);
		
		desclabel.setAlignmentX(CENTER_ALIGNMENT);
		mainPanel.add(desclabel);
		
		mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
		
		JPanel viewsPanel = new JPanel();
        viewsPanel.setAlignmentX(CENTER_ALIGNMENT);
        mainPanel.add(viewsPanel);
        
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        
      //BOtones
      	JPanel buttonsPanel = new JPanel();
      	buttonsPanel.setAlignmentX(CENTER_ALIGNMENT);
      	mainPanel.add(buttonsPanel);
		
		vehiclelabel =new JLabel("Vehicle: ");
		vehiclelabel.setAlignmentX(CENTER_ALIGNMENT);
		viewsPanel.add(vehiclelabel);
		//combo box de road
		_vehiclemodel = new DefaultComboBoxModel<>();
		_vehicles = new JComboBox<>(_vehiclemodel);
		_vehicles.setAlignmentX(LEFT_ALIGNMENT);
		viewsPanel.add(_vehicles);
		
		co2classlabel =new JLabel("CO2 Class: ");
		co2classlabel.setAlignmentX(CENTER_ALIGNMENT);
		viewsPanel.add(co2classlabel);
		//combo box de weather
		_co2classmodel = new DefaultComboBoxModel<>();
		_co2class = new JComboBox<>(_co2classmodel);
		_co2class.setAlignmentX(LEFT_ALIGNMENT);
		viewsPanel.add(_co2class);
		
		
		tickslabel =new JLabel("Ticks: ");
		tickslabel.setAlignmentX(CENTER_ALIGNMENT);
		viewsPanel.add(tickslabel);
		createStepsSpinner(viewsPanel);
		
		
		//cancel
		cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				_status = 0;
				ChangeCO2ClassDialog.this.setVisible(false);
			}
		});
		buttonsPanel.add(cancelButton);

		//ok
		okButton = new JButton("OK");
		okButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if ((_vehiclemodel.getSelectedItem() != null) &&(_co2classmodel.getSelectedItem() != null)) {
					_status = 1;
					ChangeCO2ClassDialog.this.setVisible(false);
				}
			}
		});
		buttonsPanel.add(okButton);

		setPreferredSize(new Dimension(500, 200));
		pack();
		setResizable(false);
		setVisible(false);
	}
	
	public int open(List<Vehicle> vehiclelist) {

		// update the comboxBox model -- if you always use the same no
		// need to update it, you can initialize it in the constructor.
		//
		_vehiclemodel.removeAllElements();
		for (Vehicle v : vehiclelist)
			_vehiclemodel.addElement(v);

		_co2classmodel.removeAllElements();
		for (int i =0; i < 6; i++)
			_co2classmodel.addElement(i);
		
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

	public Vehicle getvehicle() {
		return (Vehicle) this._vehicles.getSelectedItem();
	}

	public int getco2() {
		return (int) this._co2class.getSelectedItem();
	}
}
