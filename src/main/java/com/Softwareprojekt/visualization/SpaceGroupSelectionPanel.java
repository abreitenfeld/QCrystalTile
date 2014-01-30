package com.Softwareprojekt.visualization;

import com.Softwareprojekt.interfaces.Controller;
import com.Softwareprojekt.interfaces.LatticeType;
import com.Softwareprojekt.interfaces.SpaceGroupID;
import com.Softwareprojekt.interfaces.View;
import com.Softwareprojekt.InternationalShortSymbol.Enum;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.ResourceBundle;

import javax.swing.*;

public class SpaceGroupSelectionPanel extends Panel implements View, ActionListener {

	private final Controller _controller;
    private final JComboBox<LatticeSystemListItem> _latticeSystemList;
    private final JComboBox<CenteringTypeListItem> _centeringTypeList;
    private final JComboBox<SpaceGroupIDListItem> _spaceGroupList;
    private final Label _informationLabel;

	private final ResourceBundle bundle = ResourceBundle.getBundle("Messages");

    private final Map<LatticeType.CenteringType, ComboBoxModel<SpaceGroupIDListItem>> _centeringTypeToGroupID =
            new HashMap<LatticeType.CenteringType, ComboBoxModel<SpaceGroupIDListItem>>();
	
	public SpaceGroupSelectionPanel(Controller controller) {
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
            new LatticeSystemListItem(LatticeType.System.HEXAGONAL),
            new LatticeSystemListItem(LatticeType.System.MONOCLINIC),
            new LatticeSystemListItem(LatticeType.System.ORTHORHOMBIC),
            new LatticeSystemListItem(LatticeType.System.RHOMBOHEDRAL),
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
        this._informationLabel = new Label("some extra information");

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

        this.prepareSpaceGroupList();
        final LatticeType.CenteringType selectedSpaceGroupID = ((CenteringTypeListItem)this._centeringTypeList.getSelectedItem()).getType();
        //this._spaceGroupList.setModel(this._centeringTypeToGroupID.get(selectedSpaceGroupID));

        this.invalidateViewOptions();

        this._latticeSystemList.addActionListener(this);
        this._spaceGroupList.addActionListener(this);
        this._centeringTypeList.addActionListener(this);
	}

    private void prepareSpaceGroupList() {
        final Enum enu = new Enum();

        for (LatticeType.CenteringType type : LatticeType.CenteringType.values()) {
            char cCentering = type.toString().charAt(0);
            Vector<SpaceGroupIDListItem> vect = new Vector<SpaceGroupIDListItem>();
            for (SpaceGroupID id : enu) {
                if (id.stringRepr().charAt(0) == cCentering) {
                    vect.add(new SpaceGroupIDListItem(id));
                }
            }
            this._centeringTypeToGroupID.put(type, new DefaultComboBoxModel<SpaceGroupIDListItem>(vect));
        }
    }

	@Override
	public void invalidateView() { }

	@Override
	public void invalidateViewOptions() {

	}

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this._centeringTypeList) {
            final CenteringTypeListItem type = (CenteringTypeListItem) this._centeringTypeList.getSelectedItem();
            if (type.getType() != null) {
                this._spaceGroupList.setModel(this._centeringTypeToGroupID.get(type.getType()));
            }
        }
        else if (e.getSource() == this._latticeSystemList) {
            final LatticeSystemListItem system = (LatticeSystemListItem) this._latticeSystemList.getSelectedItem();
            if (system.getSystem() != null) {

            }
        }
        else if (e.getSource() == this._spaceGroupList) {
            final SpaceGroupIDListItem groupID = (SpaceGroupIDListItem) this._spaceGroupList.getSelectedItem();
            this._controller.setSpaceGroup(groupID.getID());
        }
    }
}
