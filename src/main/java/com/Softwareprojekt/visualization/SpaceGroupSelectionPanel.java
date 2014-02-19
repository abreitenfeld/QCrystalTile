package com.Softwareprojekt.visualization;

import com.Softwareprojekt.InternationalShortSymbol.SpaceGroupFactoryImpl;
import com.Softwareprojekt.interfaces.*;
import com.Softwareprojekt.InternationalShortSymbol.InternationalShortSymbolEnum;
import com.Softwareprojekt.InternationalShortSymbol.ID;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.*;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class SpaceGroupSelectionPanel extends Panel implements ChangeListener, View, KeyListener, ActionListener {

    private final JComboBox<LatticeSystemListItem> _latticeSystemList;
    private final JComboBox<CenteringTypeListItem> _centeringTypeList;
    private final JComboBox<SpaceGroupIDListItem> _spaceGroupList;
    private final SpaceGroupEnumeration<ID> _spaceGroupIDEnum = new InternationalShortSymbolEnum();

    private final Map<ID, SpaceGroupIDListItem> _idToListItem =
            new HashMap<ID, SpaceGroupIDListItem>();
    private final Map<LatticeType.System, List<ID>> _systemToGroupID =
            new HashMap<LatticeType.System, List<ID>>();
    private final Map<LatticeType.CenteringType, List<ID>> _centeringTypeToGroupID =
            new HashMap<LatticeType.CenteringType, List<ID>>();

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
    private static final LatticeType.System[] System_Filter = new LatticeType.System[] {
        LatticeType.System.CUBIC,
        LatticeType.System.MONOCLINIC,
        LatticeType.System.ORTHORHOMBIC,
        LatticeType.System.RHOMBOHEDRAL,
        LatticeType.System.TETRAGONAL,
        LatticeType.System.TRICLINIC
    };

	public SpaceGroupSelectionPanel(Controller<ID> controller) {
		super();
		this._controller = controller;

        this.setLayout(new GridBagLayout());
        final GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridy = 0;

		this.setPreferredSize(new Dimension(600, 50));
		this.setBackground(org.jzy3d.colors.ColorAWT.toAWT(SpaceGroupView.Viewport_Background));
		this.setForeground(org.jzy3d.colors.ColorAWT.toAWT(SpaceGroupView.Foreground_Color));

        // add components

        this._latticeSystemList = new JComboBox<LatticeSystemListItem>(new LatticeSystemListItem[] {
            new LatticeSystemListItem(null),
            new LatticeSystemListItem(LatticeType.System.CUBIC),
            //new LatticeSystemListItem(LatticeType.System.HEXAGONAL),
            new LatticeSystemListItem(LatticeType.System.MONOCLINIC),
            new LatticeSystemListItem(LatticeType.System.ORTHORHOMBIC),
            //new LatticeSystemListItem(LatticeType.System.RHOMBOHEDRAL),
            new LatticeSystemListItem(LatticeType.System.TETRAGONAL),
            new LatticeSystemListItem(LatticeType.System.TRICLINIC)
        });
        this._latticeSystemList.setFocusable(false);

		this._centeringTypeList = new JComboBox<CenteringTypeListItem>(new CenteringTypeListItem[] {
            new CenteringTypeListItem(null),
			new CenteringTypeListItem(LatticeType.CenteringType.I),
			new CenteringTypeListItem(LatticeType.CenteringType.P),			
			new CenteringTypeListItem(LatticeType.CenteringType.C),
			new CenteringTypeListItem(LatticeType.CenteringType.F)
		});
        this._centeringTypeList.setFocusable(false);

		this._spaceGroupList = new JComboBox<SpaceGroupIDListItem>();
        this._spaceGroupList.setFocusable(false);

        this.prepareListControls();
        this.setSpaceGroupListItems(this._spaceGroupIDEnum);

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


        this.add(new Label(bundle.getString("latticeSystem")));
        this.add(this._latticeSystemList);
        //this.add(new Label(bundle.getString("centeringType")));
        //this.add(this._centeringTypeList);
        this.add(new Label(bundle.getString("spaceGroup")));
        this.add(this._spaceGroupList);

        this.add(new Label(bundle.getString("origin") + " (XYZ)"));
        this.add(this._inputXCoord);
        this.add(this._inputYCoord);
        this.add(this._inputZCoord);

        this.add(new Label(bundle.getString("spaceToFill") + " (XYZ)"));
        this.add(this._inputXSpace);
        this.add(this._inputYSpace);
        this.add(this._inputZSpace);

        this.add(this._btnCalculate);

        this.add(new Label(bundle.getString("visualizationStep")));
        this.add(this._stepSlider);


        this.invalidateViewOptions();

        // attach listeners
        this._latticeSystemList.addActionListener(this);
        this._spaceGroupList.addActionListener(this);
        this._centeringTypeList.addActionListener(this);
        this._inputXCoord.addKeyListener(this);
        this._inputYCoord.addKeyListener(this);
        this._inputZCoord.addKeyListener(this);
        this._inputXSpace.addKeyListener(this);
        this._inputYSpace.addKeyListener(this);
        this._inputZSpace.addKeyListener(this);
        this._stepSlider.addChangeListener(this);
        this._btnCalculate.addActionListener(this);
	}

    private void prepareListControls() {
        try {
            Set<LatticeType.System> filter = new HashSet<LatticeType.System>(Arrays.asList(System_Filter));

            final SpaceGroupFactoryImpl factory = new SpaceGroupFactoryImpl();

            for (LatticeType.System system : LatticeType.System.values()) {
                if (filter.contains(system)) {
                    Set<ID> idSet = factory.getIDbySystem(system);
                    List<ID> idList = Arrays.asList(idSet.toArray(new ID[idSet.size()]));
                    this._systemToGroupID.put(system, idList);

                    // create corresponding list items
                    for (ID id : idList) {
                        if (!this._idToListItem.containsKey(id)) {
                            this._idToListItem.put(id, new SpaceGroupIDListItem(id));
                        }
                    }
                }
            }

            for (LatticeType.CenteringType type : LatticeType.CenteringType.values()) {
                Set<ID> idSet = factory.getIDbyCentering(type);
                List<ID> idList = Arrays.asList(idSet.toArray(new ID[idSet.size()]));
                this._centeringTypeToGroupID.put(type, idList);
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    protected static <T> List<T> intersection(List<T> listA, List<T> listB) {
        List<T> intersect = new LinkedList<T>();

        for (T item : listA) {
            if (listB.contains(item)) {
                intersect.add(item);
            }
        }
        return intersect;
    }

    protected  void setSpaceGroupListItems(List<ID> ids) {
        final List<SpaceGroupIDListItem> items = new ArrayList<SpaceGroupIDListItem>(ids.size());
        this._spaceGroupList.removeAllItems();

        for (SpaceGroupID id : ids) {
            if (this._idToListItem.containsKey(id)) {
                items.add(this._idToListItem.get(id));
            }
        }
        Collections.sort(items);
        for (SpaceGroupIDListItem item : items) {
            this._spaceGroupList.addItem(item);
        }
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        if (e.getSource() == this._stepSlider) {
            // sets the current visualization step in controller
            switch (this._stepSlider.getValue()) {
                case 0: this._controller.setVisualizationStep(Controller.VisualizationSteps.ScatterPlot); break;
                case 1: this._controller.setVisualizationStep(Controller.VisualizationSteps.VoronoiTesselation); break;
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
	public void invalidateView() { }

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

        if (this._idToListItem.containsKey(this._controller.getSpaceGroupID())) {
            this._spaceGroupList.setSelectedItem(this._idToListItem.get(this._controller.getSpaceGroupID()));
        }
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
        if (e.getSource() == this._btnCalculate) {
            this._btnCalculate.setEnabled(false);
            this.applyPoint();
        }
        else if (e.getSource() == this._centeringTypeList || e.getSource() == this._latticeSystemList) {
            // filter by centering type
            final CenteringTypeListItem type = (CenteringTypeListItem) this._centeringTypeList.getSelectedItem();
            final List<ID> filteredByType;
            if (type.getType() != null) {
                filteredByType = this._centeringTypeToGroupID.get(type.getType());
            }
            else {
                filteredByType = this._spaceGroupIDEnum;
            }

            // filter by lattice system
            final LatticeSystemListItem system = (LatticeSystemListItem) this._latticeSystemList.getSelectedItem();
            final List<ID> filteredBySystem;
            if (system.getSystem() != null) {
                filteredBySystem = this._systemToGroupID.get(system.getSystem());
            }
            else {
                filteredBySystem = this._spaceGroupIDEnum;
            }

            this.setSpaceGroupListItems(intersection(filteredByType, filteredBySystem));
        }
        else if (e.getSource() == this._spaceGroupList) {
            if (this._spaceGroupList.getSelectedItem() != null) {
                final SpaceGroupIDListItem groupID = (SpaceGroupIDListItem) this._spaceGroupList.getSelectedItem();
                if (groupID.getID() != this._controller.getSpaceGroupID()) {
                    this._controller.setSpaceGroup(groupID.getID());
                }
            }
        }
    }
}
