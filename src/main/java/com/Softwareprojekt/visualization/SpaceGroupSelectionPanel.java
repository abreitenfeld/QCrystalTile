package com.Softwareprojekt.visualization;

import com.Softwareprojekt.InternationalShortSymbol.ID;
import com.Softwareprojekt.InternationalShortSymbol.InternationalShortSymbolEnum;
import com.Softwareprojekt.InternationalShortSymbol.SpaceGroupFactoryImpl;
import com.Softwareprojekt.interfaces.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

public class SpaceGroupSelectionPanel extends JPanel implements View, ActionListener {

    private final Controller<ID> _controller;
    private final ResourceBundle bundle = ResourceBundle.getBundle("Messages");
    private final JRadioButton _radioScatterPlot;
    private final JRadioButton _radioVoronoi;
    private final JButton _btnCalculate;
    private final JTextField _inputXCoord;
    private final JTextField _inputYCoord;
    private final JTextField _inputZCoord;
    private final JTextField _inputSpace;

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

    private static final int Slider_Min_Step = 0;
    private static final int Slider_Max_Step = 1;
    private static final int Min_Coord_Value = 0;
    private static final float Max_Coord_Value = 1f;
    private static final int Min_Space_Value = 1;
    private static final int Max_Space_Value = 10;
    private static Dimension Box_Size = new Dimension(150, 25);
    private static Dimension Field_Size = new Dimension(80, 25);

	public SpaceGroupSelectionPanel(Controller<ID> controller) {
		super(new GridBagLayout());
		this._controller = controller;

        this.setDoubleBuffered(true);
		this.setPreferredSize(new Dimension(600, 70));
        styleComponent(this);

        final JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        final JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        final JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        // add components
        this._latticeSystemList = new JComboBox<LatticeSystemListItem>(new LatticeSystemListItem[] {
            new LatticeSystemListItem(LatticeType.System.CUBIC),
            new LatticeSystemListItem(LatticeType.System.MONOCLINIC),
            new LatticeSystemListItem(LatticeType.System.ORTHORHOMBIC),
            new LatticeSystemListItem(LatticeType.System.TETRAGONAL),
            new LatticeSystemListItem(LatticeType.System.TRICLINIC),
                new LatticeSystemListItem(LatticeType.System.HEXAGONAL)
        });
        this._latticeSystemList.setLightWeightPopupEnabled(true);
        this._latticeSystemList.setPreferredSize(Box_Size);
        this._latticeSystemList.setFocusable(false);

		this._centeringTypeList = new JComboBox<CenteringTypeListItem>(new CenteringTypeListItem[] {
            new CenteringTypeListItem(null, bundle.getString("all")),
			new CenteringTypeListItem(LatticeType.CenteringType.I),
			new CenteringTypeListItem(LatticeType.CenteringType.P),			
			new CenteringTypeListItem(LatticeType.CenteringType.C),
			new CenteringTypeListItem(LatticeType.CenteringType.F)
		});
        this._centeringTypeList.setLightWeightPopupEnabled(true);
        this._centeringTypeList.setPreferredSize(Box_Size);
        this._centeringTypeList.setFocusable(false);

		this._spaceGroupList = new JComboBox<SpaceGroupIDListItem>();
        this._spaceGroupList.setLightWeightPopupEnabled(true);
        this._spaceGroupList.setPreferredSize(Box_Size);
        this._spaceGroupList.setFocusable(false);

        // create coord inputs
        this._inputXCoord = new JTextField();
        this._inputXCoord.setPreferredSize(Field_Size);
        this._inputXCoord.setHorizontalAlignment(JTextField.RIGHT);
        this._inputYCoord = new JTextField();
        this._inputYCoord.setPreferredSize(Field_Size);
        this._inputYCoord.setHorizontalAlignment(JTextField.RIGHT);
        this._inputZCoord = new JTextField();
        this._inputZCoord.setPreferredSize(Field_Size);
        this._inputZCoord.setHorizontalAlignment(JTextField.RIGHT);

        // create space inputs
        this._inputSpace = new JTextField();
        this._inputSpace.setPreferredSize(Field_Size);
        this._inputSpace.setHorizontalAlignment(JTextField.RIGHT);

        this._btnCalculate = new JButton(bundle.getString("calculate"));
        this._btnCalculate.setFocusable(false);
        // create the step slider
        this._radioScatterPlot = new JRadioButton(bundle.getString("spaceGroup"));
        this._radioVoronoi = new JRadioButton(bundle.getString("tiling"));
        final ButtonGroup group = new ButtonGroup();
        group.add(this._radioScatterPlot);
        group.add(this._radioVoronoi);

        this.prepareListControls();

        topPanel.add(styleComponent(new JLabel(bundle.getString("latticeSystem"), JLabel.LEFT)));
        topPanel.add(this._latticeSystemList);
        topPanel.add(styleComponent(new JLabel(bundle.getString("centeringType"))));
        topPanel.add(this._centeringTypeList);
        topPanel.add(styleComponent(new JLabel(bundle.getString("spaceGroup"))));
        topPanel.add(this._spaceGroupList);

        bottomPanel.add(styleComponent(new JLabel(bundle.getString("origin"))));
        bottomPanel.add(styleComponent(new JLabel("X")));
        bottomPanel.add(this._inputXCoord);
        bottomPanel.add(styleComponent(new JLabel("Y")));
        bottomPanel.add(this._inputYCoord);
        bottomPanel.add(styleComponent(new JLabel("Z")));
        bottomPanel.add(this._inputZCoord);

        bottomPanel.add(styleComponent(new JLabel(bundle.getString("grid"))));
        bottomPanel.add(this._inputSpace);

        bottomPanel.add(this._btnCalculate);

        rightPanel.add(styleComponent(new JLabel(bundle.getString("view"))));
        rightPanel.add(styleComponent(this._radioScatterPlot));
        rightPanel.add(styleComponent(this._radioVoronoi));

        final GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.7f;
        c.gridx = 0;
        c.gridy = 0;
        this.add(styleComponent(topPanel), c);

        c.gridx = 0;
        c.gridy = 1;
        this.add(styleComponent(bottomPanel), c);

        c.weightx = 0.3f;
        c.gridy = 0;
        c.gridx = 1;
        c.gridheight = 2;
        this.add(styleComponent(rightPanel), c);

        this.invalidateViewOptions();

        // attach listeners
        this._latticeSystemList.addActionListener(this);
        this._centeringTypeList.addActionListener(this);
        this._radioScatterPlot.addActionListener(this);
        this._radioVoronoi.addActionListener(this);
        this._btnCalculate.addActionListener(this);
	}

    private static <T extends JComponent> T styleComponent(T component) {
        component.setForeground(org.jzy3d.colors.ColorAWT.toAWT(SpaceGroupView.Foreground_Color));
        component.setBackground(org.jzy3d.colors.ColorAWT.toAWT(SpaceGroupView.Viewport_Background));
        component.setFocusable(false);
        return component;
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

    private void prepareListControls() {
        int selectedLatticeIndex = 0;
        try {
            final SpaceGroupFactoryImpl factory = new SpaceGroupFactoryImpl();

            for (int i = 0; i < this._latticeSystemList.getModel().getSize(); i++) {
                if (this._latticeSystemList.getModel().getElementAt(i).getSystem() != null) {
                    LatticeType.System system = this._latticeSystemList.getModel().getElementAt(i).getSystem();
                    Set<ID> idSet = factory.getIDbySystem(system);
                    List<ID> idList = Arrays.asList(idSet.toArray(new ID[idSet.size()]));
                    this._systemToGroupID.put(system, idList);

                    // create corresponding list items
                    for (ID id : idList) {
                        if (id.stringRepr().equals(this._controller.getSpaceGroupID().stringRepr())) {
                            selectedLatticeIndex = i;
                        }
                        // create list items
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

        // pre select list items
        LatticeType.System system = this._latticeSystemList.getModel().getElementAt(selectedLatticeIndex).getSystem();
        this.setSpaceGroupListItems( this._systemToGroupID.get(system));
        this._latticeSystemList.setSelectedIndex(selectedLatticeIndex);

        if (this._idToListItem.containsKey(this._controller.getSpaceGroupID())) {
            this._spaceGroupList.setSelectedItem(this._idToListItem.get(this._controller.getSpaceGroupID()));
        }
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

    private void applySettings() {
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
            int space = Integer.parseInt(this._inputSpace.getText());
            space = Math.max(Math.min(space, Max_Space_Value), Min_Space_Value);
            this._inputSpace.setText(Integer.toString(space));

            // apply values
            Vector3D origin = new Vector3D(new double[] {x, y, z});
            Vector3D spaceVect = new Vector3D(new double[]{space, space, space});

            if (this._spaceGroupList.getSelectedItem() != null) {
                final SpaceGroupIDListItem groupID = (SpaceGroupIDListItem) this._spaceGroupList.getSelectedItem();
                this._controller.configure(groupID.getID(), origin, spaceVect);
            }
            else {
                this._controller.configure(origin, spaceVect);
            }
        }
        catch (NumberFormatException e) { }
    }

	@Override
	public void invalidateView() { }

    @Override
    public void invalidateViewOptions() {
        // set current visualization step
        switch (this._controller.getVisualization()) {
            case ScatterPlot: this._radioScatterPlot.setSelected(true); break;
            case VoronoiTesselation: this._radioVoronoi.setSelected(true); break;
        }

        // set coordinates to input fields
        Vector3D originPt = this._controller.getModel().getPoint();
        this._inputXCoord.setText(Double.toString(originPt.get(0)));
        this._inputYCoord.setText(Double.toString(originPt.get(1)));
        this._inputZCoord.setText(Double.toString(originPt.get(2)));

        // set space to input fields
        Vector3D spacePt = this._controller.getModel().getSpace();
        this._inputSpace.setText(Integer.toString((int)spacePt.get(0)));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this._btnCalculate) {
            this.applySettings();
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
            final List<ID> filteredBySystem = this._systemToGroupID.get(system.getSystem());

            this.setSpaceGroupListItems(intersection(filteredByType, filteredBySystem));
        }
        else if (e.getSource() == this._radioScatterPlot) {
            this._controller.setVisualization(Controller.Visualization.ScatterPlot);
        }
        else if (e.getSource() == this._radioVoronoi) {
            this._controller.setVisualization(Controller.Visualization.VoronoiTesselation);
        }
    }
}
