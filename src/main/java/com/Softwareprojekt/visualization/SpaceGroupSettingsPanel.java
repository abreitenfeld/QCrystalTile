package com.Softwareprojekt.visualization;

import com.Softwareprojekt.interfaces.Controller;
import com.Softwareprojekt.interfaces.Controller.VisualizationSteps;
import com.Softwareprojekt.interfaces.Vector3D;
import com.Softwareprojekt.interfaces.View;

import com.Softwareprojekt.InternationalShortSymbol.ID;

import java.awt.*;
import java.awt.event.*;
import java.util.Hashtable;
import java.util.ResourceBundle;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class SpaceGroupSettingsPanel extends Panel implements ChangeListener, View, KeyListener, ActionListener {

	private final Controller<ID> _controller;
	private final ResourceBundle bundle = ResourceBundle.getBundle("Messages");
	private final JSlider _stepSlider;
    private final JButton _btnCalculate;
	private final JTextField _inputXCoord;
    private final JTextField _inputYCoord;
    private final JTextField _inputZCoord;
    private final JTextField _inputXSpace;
    private final JTextField _inputYSpace;
    private final JTextField _inputZSpace;
	private final ImageIcon _loadingIcon;

	private static final int Slider_Min_Step = 0;
	private static final int Slider_Max_Step = 1;
	private static final int Min_Coord_Value = 0;
	private static final float Max_Coord_Value = 1f;
    private static final int Max_Space_Value = 4;
	private static Dimension Field_Size = new Dimension(50, 25);
    private static Dimension Space_Field_Size = new Dimension(30, 25);

	public SpaceGroupSettingsPanel(Controller<ID> controller) {
		super();
		this._controller = controller;
		
		this.setLayout(new GridLayout(0,2));
		this.setPreferredSize(new Dimension(100, 60));
		this.setBackground(org.jzy3d.colors.ColorAWT.toAWT(SpaceGroupView.Viewport_Background));
		this.setForeground(org.jzy3d.colors.ColorAWT.toAWT(SpaceGroupView.Foreground_Color));

        final JPanel leftPanel = new JPanel();
        leftPanel.setBackground(org.jzy3d.colors.ColorAWT.toAWT(SpaceGroupView.Viewport_Background));
        leftPanel.setForeground(org.jzy3d.colors.ColorAWT.toAWT(SpaceGroupView.Foreground_Color));
        leftPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		// create coord inputs
		this._inputXCoord = new JTextField();
		this._inputXCoord.setPreferredSize(Field_Size);
		this._inputYCoord = new JTextField();
		this._inputYCoord.setPreferredSize(Field_Size);
		this._inputZCoord = new JTextField();
		this._inputZCoord.setPreferredSize(Field_Size);

        // create space inputs
        this._inputXSpace = new JTextField();
        this._inputXSpace.setPreferredSize(Space_Field_Size);
        this._inputYSpace = new JTextField();
        this._inputYSpace.setPreferredSize(Space_Field_Size);
        this._inputZSpace = new JTextField();
        this._inputZSpace.setPreferredSize(Space_Field_Size);

        this._btnCalculate = new JButton(bundle.getString("apply"));
        this._btnCalculate.setEnabled(false);
        this._loadingIcon = new ImageIcon(ClassLoader.getSystemResource("loading.gif"));

        final JPanel rightPanel = new JPanel();
        rightPanel.setBackground(org.jzy3d.colors.ColorAWT.toAWT(SpaceGroupView.Viewport_Background));
        rightPanel.setForeground(org.jzy3d.colors.ColorAWT.toAWT(SpaceGroupView.Foreground_Color));
        rightPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		// create the step slider
		this._stepSlider = new JSlider(JSlider.HORIZONTAL, Slider_Min_Step, Slider_Max_Step, 0);
		this._stepSlider.setForeground(org.jzy3d.colors.ColorAWT.toAWT(SpaceGroupView.Foreground_Color));
		this._stepSlider.setPreferredSize(new Dimension(200, 50));
		this._stepSlider.setSnapToTicks(true);
		this._stepSlider.setMajorTickSpacing(1);
		this._stepSlider.setPaintTicks(true);
		this._stepSlider.setPaintLabels(true);
		
		// create the labels of slider
		Hashtable<Integer,JLabel> labels = new Hashtable<Integer,JLabel>();
		labels.put(new Integer(0), new JLabel(bundle.getString("spaceGroup")) );
		labels.put(new Integer(1), new JLabel(bundle.getString("tiling")) );
		this._stepSlider.setLabelTable(labels);

        leftPanel.add(new Label(bundle.getString("origin") + " (XYZ)"));
        leftPanel.add(this._inputXCoord);
        leftPanel.add(this._inputYCoord);
        leftPanel.add(this._inputZCoord);

        leftPanel.add(new Label(bundle.getString("spaceToFill") + " (XYZ)"));
        leftPanel.add(this._inputXSpace);
        leftPanel.add(this._inputYSpace);
        leftPanel.add(this._inputZSpace);

        leftPanel.add(this._btnCalculate);

        rightPanel.add(new Label(bundle.getString("visualizationStep")));
        rightPanel.add(this._stepSlider);

        this.add(leftPanel);
        this.add(rightPanel);

		this.invalidateViewOptions();
		
		// attach listeners
		this._inputXCoord.addKeyListener(this);
		this._inputYCoord.addKeyListener(this);
		this._inputZCoord.addKeyListener(this);
        this._inputXSpace.addKeyListener(this);
        this._inputYSpace.addKeyListener(this);
        this._inputZSpace.addKeyListener(this);
		this._stepSlider.addChangeListener(this);
        this._btnCalculate.addActionListener(this);
	}
	
	@Override
	public void stateChanged(ChangeEvent e) {
		if (e.getSource() == this._stepSlider) {
			// sets the current visualization step in controller
			switch (this._stepSlider.getValue()) {
				case 0: this._controller.setVisualizationStep(VisualizationSteps.ScatterPlot); break;
				case 1: this._controller.setVisualizationStep(VisualizationSteps.VoronoiTesselation); break;
			}
		}
	}

	private void applyPoint() {
		try {
            // parse coordinates
			double x = Double.parseDouble(this._inputXCoord.getText());
			double y = Double.parseDouble(this._inputYCoord.getText());
			double z = Double.parseDouble(this._inputZCoord.getText());
			
			x = Math.max(Math.min(x, Max_Coord_Value), Min_Coord_Value);
			y = Math.max(Math.min(y, Max_Coord_Value), Min_Coord_Value);
			z = Math.max(Math.min(z, Max_Coord_Value), Min_Coord_Value);
			
			this._inputXCoord.setText(Double.toString(x));
			this._inputYCoord.setText(Double.toString(y));
			this._inputZCoord.setText(Double.toString(z));

            // parse space fields
            int xSpace = Integer.parseInt(this._inputXSpace.getText());
            int ySpace = Integer.parseInt(this._inputYSpace.getText());
            int zSpace = Integer.parseInt(this._inputZSpace.getText());

            xSpace = Math.max(Math.min(xSpace, Max_Space_Value), Min_Coord_Value);
            ySpace = Math.max(Math.min(ySpace, Max_Space_Value), Min_Coord_Value);
            zSpace = Math.max(Math.min(zSpace, Max_Space_Value), Min_Coord_Value);

            this._inputXSpace.setText(Integer.toString(xSpace));
            this._inputYSpace.setText(Integer.toString(ySpace));
            this._inputZSpace.setText(Integer.toString(zSpace));

            // apply values
            this._controller.getModel().setSpaceToFill(new Vector3D(new double[] { xSpace, ySpace, zSpace }));
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
			case ScatterPlot: this._stepSlider.setValue(0); break;
			case VoronoiTesselation: this._stepSlider.setValue(1); break;
		}
		
		// set coordinates to input fields
		Vector3D originPt = this._controller.getOriginPoint();
		this._inputXCoord.setText(Double.toString(originPt.get(0)));
		this._inputYCoord.setText(Double.toString(originPt.get(1)));
		this._inputZCoord.setText(Double.toString(originPt.get(2)));

        // set space to input fields
        Vector3D spacePt = this._controller.getModel().getSpaceToFill();
        this._inputXSpace.setText(Integer.toString((int)spacePt.get(0)));
        this._inputYSpace.setText(Integer.toString((int) spacePt.get(1)));
        this._inputZSpace.setText(Integer.toString((int) spacePt.get(2)));
	}

	@Override
	public void keyTyped(KeyEvent e) { }

	@Override
	public void keyPressed(KeyEvent e) { }

	@Override
	public void keyReleased(KeyEvent e) {
        this._btnCalculate.setEnabled(true);
	}

    @Override
    public void actionPerformed(ActionEvent e) {
        this._btnCalculate.setEnabled(false);
        this.applyPoint();
    }
}
