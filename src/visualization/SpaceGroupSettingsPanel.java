package visualization;

import interfaces.Controller;
import interfaces.Controller.VisualizationSteps;
import interfaces.View;

import java.awt.*;
import java.awt.event.*;
import java.util.Hashtable;
import java.util.ResourceBundle;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class SpaceGroupSettingsPanel extends Panel implements ChangeListener, View {

	private final Controller _controller;
	private final ResourceBundle bundle = ResourceBundle.getBundle("resources.Messages");
	private final JSlider _stepSlider;
	private final JComboBox<String> _spaceGroupList;
	
	private static final int Slider_Min_Step = 0;
	private static final int Slider_Max_Step = 2;
	
	public SpaceGroupSettingsPanel(Controller controller) {
		super();
		this._controller = controller;
		
		this.setLayout(new FlowLayout(FlowLayout.LEFT));
		this.setPreferredSize(new Dimension(600, 70));
		this.setBackground(org.jzy3d.colors.ColorAWT.toAWT(SpaceGroupView.Viewport_Background));
		this.setForeground(org.jzy3d.colors.ColorAWT.toAWT(SpaceGroupView.Foregrond_Color));
		
		this._spaceGroupList = new JComboBox<String>();
		this._spaceGroupList.addItem("<html>I4<small>1</small>32</html>");
		
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

		this.add(new Label(bundle.getString("spaceGroup")));
		this.add(this._spaceGroupList);
		this.add(new Label(bundle.getString("visualizationStep")));
		this.add(this._stepSlider);
		
		this.invalidateViewOptions();
		
		// attach listeners
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

	@Override
	public void invalidateView() {

	}

	@Override
	public void invalidateViewOptions() {
		switch (this._controller.getVisualizationStep()) {
			case ConvexHull: this._stepSlider.setValue(0); break;
			case DelaunayTriangulation: this._stepSlider.setValue(1); break;
			case VoronoiTesselation: this._stepSlider.setValue(2); break;
		}
	}

}
