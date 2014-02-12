package com.Softwareprojekt.visualization;

import com.Softwareprojekt.InternationalShortSymbol.SpaceGroupFactoryImpl;
import com.Softwareprojekt.interfaces.*;
import com.Softwareprojekt.InternationalShortSymbol.InternationalShortSymbolEnum;
import com.Softwareprojekt.InternationalShortSymbol.ID;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.*;

public class SpaceGroupSelectionPanel extends Panel implements View, ActionListener {

    private final Controller<ID> _controller;
    private final JComboBox<LatticeSystemListItem> _latticeSystemList;
    private final JComboBox<CenteringTypeListItem> _centeringTypeList;
    private final JComboBox<SpaceGroupIDListItem> _spaceGroupList;
    private final Label _informationLabel;
    private final SpaceGroupEnumeration<ID> _spaceGroupIDEnum = new InternationalShortSymbolEnum();

	private final ResourceBundle bundle = ResourceBundle.getBundle("Messages");

    private final Map<ID, SpaceGroupIDListItem> _idToListItem =
            new HashMap<ID, SpaceGroupIDListItem>();
    private final Map<LatticeType.System, List<ID>> _systemToGroupID =
            new HashMap<LatticeType.System, List<ID>>();
    private final Map<LatticeType.CenteringType, List<ID>> _centeringTypeToGroupID =
            new HashMap<LatticeType.CenteringType, List<ID>>();
	
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
        final JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        leftPanel.setBackground(org.jzy3d.colors.ColorAWT.toAWT(SpaceGroupView.Viewport_Background));
        leftPanel.setForeground(org.jzy3d.colors.ColorAWT.toAWT(SpaceGroupView.Foreground_Color));

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

        final JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightPanel.setBackground(org.jzy3d.colors.ColorAWT.toAWT(SpaceGroupView.Viewport_Background));
        rightPanel.setForeground(org.jzy3d.colors.ColorAWT.toAWT(SpaceGroupView.Foreground_Color));
        this._informationLabel = new Label();

        leftPanel.add(new Label(bundle.getString("latticeSystem")));
        leftPanel.add(this._latticeSystemList);
        leftPanel.add(new Label(bundle.getString("centeringType")));
        leftPanel.add(this._centeringTypeList);
        leftPanel.add(new Label(bundle.getString("spaceGroup")));
        leftPanel.add(this._spaceGroupList);
        rightPanel.add(this._informationLabel);

        c.gridx = 0;
        c.weightx = 0.8;
        this.add(leftPanel, c);
        c.gridx = 1;
        c.weightx = 0.2;
        this.add(rightPanel, c);

        this.prepareListControls();
        //final LatticeType.CenteringType selectedSpaceGroupID = ((CenteringTypeListItem)this._centeringTypeList.getSelectedItem()).getType();
        this.setSpaceGroupListItems(this._spaceGroupIDEnum);

        this.invalidateViewOptions();

        this._latticeSystemList.addActionListener(this);
        this._spaceGroupList.addActionListener(this);
        this._centeringTypeList.addActionListener(this);
	}

    private void prepareListControls() {
        try {
            final SpaceGroupFactoryImpl factory = new SpaceGroupFactoryImpl();
            // create list items
            for (ID id : this._spaceGroupIDEnum) {
                this._idToListItem.put(id, new SpaceGroupIDListItem(id));
            }

            for (LatticeType.System system : LatticeType.System.values()) {
                Set<ID> idSet = factory.getIDbySystem(system);
                List<ID> idList = Arrays.asList(idSet.toArray(new ID[idSet.size()]));
                this._systemToGroupID.put(system, idList);
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
	public void invalidateView() { }

	@Override
	public void invalidateViewOptions() {

	}

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this._centeringTypeList || e.getSource() == this._latticeSystemList) {
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
                this._controller.setSpaceGroup(groupID.getID());
            }
        }
    }
}
