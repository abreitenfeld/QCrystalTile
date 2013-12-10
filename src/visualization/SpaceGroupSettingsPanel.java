package visualization;

import interfaces.Controller;
import interfaces.Controller.VisualizationSteps;

import java.awt.*;
import java.awt.event.*;
import java.util.Hashtable;
import java.util.ResourceBundle;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class SpaceGroupSettingsPanel extends Panel implements ChangeListener {

	private final Controller _controller;
	private final ResourceBundle bundle = ResourceBundle.getBundle("resources.Messages");
	private final JSlider _stepSlider;
	
	private static final int Slider_Min_Step = 0;
	private static final int Slider_Max_Step = 2;
	
	public SpaceGroupSettingsPanel(Controller controller) {
		this._controller = controller;
		this.setLayout(new FlowLayout(FlowLayout.CENTER));
		this.setPreferredSize(new Dimension(600, 70));
		this.setBackground(Color.white);
		
		int currentStep = 0;
		switch (this._controller.getVisualizationStep()) {
			case ConvexHull: currentStep = 0; break;
			case DelaunayTriangulation: currentStep = 1; break;
			case VoronoiTesselation: currentStep = 2; break;
		}

		this.add(new Label(bundle.getString("visualizationStep")));
		
		// create the step slider
		this._stepSlider= new JSlider(JSlider.HORIZONTAL, Slider_Min_Step, Slider_Max_Step, currentStep);
		this._stepSlider.setPreferredSize(new Dimension(500, 50));
		this._stepSlider.setSnapToTicks(true);
		this._stepSlider.addChangeListener(this);
		this._stepSlider.setMajorTickSpacing(1);
		this._stepSlider.setPaintTicks(true);
		this._stepSlider.setPaintLabels(true);
		
		// create the labels of slider
		Hashtable labelTable = new Hashtable();
		labelTable.put(new Integer(0), new JLabel(bundle.getString("convexHull")) );
		labelTable.put(new Integer(1), new JLabel(bundle.getString("delaunayTriangulation")) );
		labelTable.put(new Integer(2), new JLabel(bundle.getString("voronoiTesselation")) );
		this._stepSlider.setLabelTable( labelTable );

		this.add(this._stepSlider);
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

}
