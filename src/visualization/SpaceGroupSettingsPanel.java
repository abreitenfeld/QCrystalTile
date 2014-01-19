package visualization;

import interfaces.Controller;
import interfaces.Controller.VisualizationSteps;
import interfaces.Vector3D;
import interfaces.View;

import java.awt.*;
import java.awt.event.*;
import java.util.Hashtable;
import java.util.ResourceBundle;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class SpaceGroupSettingsPanel extends Panel implements ChangeListener, View, DocumentListener, KeyListener {

	private final Controller _controller;
	private final ResourceBundle bundle = ResourceBundle.getBundle("resources.Messages");
	private final JSlider _stepSlider;
	private final JTextField _inputXCoord;
	private final JTextField _inputYCoord;
	private final JTextField _inputZCoord;
	
	private static final int Slider_Min_Step = 0;
	private static final int Slider_Max_Step = 2;
	private static final float Min_Coord_Value = 0f;
	private static final float Max_Coord_Value = 1f;
	private static Dimension Field_Size = new Dimension(60, 30);
	
	public SpaceGroupSettingsPanel(Controller controller) {
		super();
		this._controller = controller;
		
		this.setLayout(new FlowLayout(FlowLayout.LEFT));
		this.setPreferredSize(new Dimension(600, 70));
		this.setBackground(org.jzy3d.colors.ColorAWT.toAWT(SpaceGroupView.Viewport_Background));
		this.setForeground(org.jzy3d.colors.ColorAWT.toAWT(SpaceGroupView.Foregrond_Color));
		
		// create coord inputs
		this._inputXCoord = new JTextField();
		this._inputXCoord.setPreferredSize(Field_Size);
		this._inputYCoord = new JTextField();
		this._inputYCoord.setPreferredSize(Field_Size);
		this._inputZCoord = new JTextField();
		this._inputZCoord.setPreferredSize(Field_Size);
		
		// create the step slider
		this._stepSlider= new JSlider(JSlider.HORIZONTAL, Slider_Min_Step, Slider_Max_Step, 0);
		this._stepSlider.setForeground(org.jzy3d.colors.ColorAWT.toAWT(SpaceGroupView.Foregrond_Color));
		this._stepSlider.setPreferredSize(new Dimension(500, 50));
		this._stepSlider.setSnapToTicks(true);
		this._stepSlider.setMajorTickSpacing(1);
		this._stepSlider.setPaintTicks(true);
		this._stepSlider.setPaintLabels(true);
		
		// create the labels of slider
		Hashtable labels = new Hashtable();
		labels.put(new Integer(0), new JLabel(bundle.getString("convexHull")) );
		labels.put(new Integer(1), new JLabel(bundle.getString("delaunayTriangulation")) );
		labels.put(new Integer(2), new JLabel(bundle.getString("voronoiTesselation")) );
		this._stepSlider.setLabelTable(labels);

		this.add(new Label("XYZ"));
		this.add(this._inputXCoord);
		this.add(this._inputYCoord);
		this.add(this._inputZCoord);
		
		this.add(new Label(bundle.getString("visualizationStep")));
		this.add(this._stepSlider);
		
		this.invalidateViewOptions();
		
		// attach listeners
		/*this._inputXCoord.getDocument().addDocumentListener(this);
		this._inputYCoord.getDocument().addDocumentListener(this);
		this._inputZCoord.getDocument().addDocumentListener(this);*/
		this._inputXCoord.addKeyListener(this);
		this._inputYCoord.addKeyListener(this);
		this._inputZCoord.addKeyListener(this);
		this._stepSlider.addChangeListener(this);
	}
	
	@Override
	public void stateChanged(ChangeEvent e) {
		if (e.getSource() == this._stepSlider) {
			// sets the current visualization step in controller
			switch (this._stepSlider.getValue()) {
				case 0: this._controller.setVisualizationStep(VisualizationSteps.ConvexHull); break;
				case 1: this._controller.setVisualizationStep(VisualizationSteps.DelaunayTriangulation); break;
				case 2: this._controller.setVisualizationStep(VisualizationSteps.VoronoiTesselation); break;
			}
		}
	}

	private void applyPoint() {
		try {
			double x = Double.parseDouble(this._inputXCoord.getText());
			double y = Double.parseDouble(this._inputYCoord.getText());
			double z = Double.parseDouble(this._inputZCoord.getText());
			
			x = Math.max(Math.min(x, Max_Coord_Value), Min_Coord_Value);
			y = Math.max(Math.min(y, Max_Coord_Value), Min_Coord_Value);
			z = Math.max(Math.min(z, Max_Coord_Value), Min_Coord_Value);
			
			this._controller.setOriginPoint(new Vector3D(new double[] {x, y, z}));
		}
		catch (NumberFormatException e) {	
		}
	}
	
	@Override
	public void invalidateView() {

	}

	@Override
	public void invalidateViewOptions() {
		// set current visualization step
		switch (this._controller.getVisualizationStep()) {
			case ConvexHull: this._stepSlider.setValue(0); break;
			case DelaunayTriangulation: this._stepSlider.setValue(1); break;
			case VoronoiTesselation: this._stepSlider.setValue(2); break;
		}
		
		// set coordinates to input fields
		Vector3D originPt = this._controller.getOriginPoint();
		this._inputXCoord.setText(Double.toString(originPt.get(0)));
		this._inputYCoord.setText(Double.toString(originPt.get(1)));
		this._inputZCoord.setText(Double.toString(originPt.get(2)));
	}

	@Override
	public void insertUpdate(DocumentEvent e) { 
		this.applyPoint();
	}

	@Override
	public void removeUpdate(DocumentEvent e) {
		this.applyPoint();
	}

	@Override
	public void changedUpdate(DocumentEvent e) {
		this.applyPoint();
	}

	@Override
	public void keyTyped(KeyEvent e) { }

	@Override
	public void keyPressed(KeyEvent e) { }

	@Override
	public void keyReleased(KeyEvent e) {
		if (Character.isDigit(e.getKeyChar())) {
			this.applyPoint();
		}
	}

}
