package com.Softwareprojekt.visualization;

import com.Softwareprojekt.interfaces.Controller;
import com.Softwareprojekt.interfaces.LatticeType;
import com.Softwareprojekt.interfaces.SpaceGroupID;
import com.Softwareprojekt.interfaces.View;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.ResourceBundle;

import javax.swing.*;

public class SpaceGroupSelectionPanel extends Panel implements View, ActionListener {

	private final Controller _controller;
	private final ResourceBundle bundle = ResourceBundle.getBundle("Messages");
	private final JComboBox<CenteringTypeListItem> _centeringTypeList;
	private final JComboBox<SpaceGroupID> _spaceGroupList;

    private final Map<LatticeType.CenteringType, ComboBoxModel<SpaceGroupID>> _centeringTypeToGroupID = new HashMap<LatticeType.CenteringType, ComboBoxModel<SpaceGroupID>>();
	
	public SpaceGroupSelectionPanel(Controller controller) {
		super();
		this._controller = controller;
		
		this.setLayout(new FlowLayout(FlowLayout.LEFT));
		this.setPreferredSize(new Dimension(600, 50));
		this.setBackground(org.jzy3d.colors.ColorAWT.toAWT(SpaceGroupView.Viewport_Background));
		this.setForeground(org.jzy3d.colors.ColorAWT.toAWT(SpaceGroupView.Foregrond_Color));

        // add components
		this._centeringTypeList = new JComboBox<CenteringTypeListItem>(new CenteringTypeListItem[] {
			new CenteringTypeListItem(LatticeType.CenteringType.I),
			new CenteringTypeListItem(LatticeType.CenteringType.P),			
			new CenteringTypeListItem(LatticeType.CenteringType.C),
			new CenteringTypeListItem(LatticeType.CenteringType.F)
		});
		
		this._spaceGroupList = new JComboBox<SpaceGroupID>();

		this.add(new Label(bundle.getString("centeringType")));
		this.add(this._centeringTypeList);
		
		this.add(new Label(bundle.getString("spaceGroup")));
		this.add(this._spaceGroupList);

        this._spaceGroupList.addActionListener(this);
        this._centeringTypeList.addActionListener(this);
	}
	
	@Override
	public void invalidateView() { }

	@Override
	public void invalidateViewOptions() {
		//this._spaceGroupList.setSelectedItem(this._controller.getModel().getSpaceGrou;
	}

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this._centeringTypeList) {

        }
        else if (e.getSource() == this._spaceGroupList) {

        }
    }
}
