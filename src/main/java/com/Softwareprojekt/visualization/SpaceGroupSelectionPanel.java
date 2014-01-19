package com.Softwareprojekt.visualization;

import com.Softwareprojekt.interfaces.Controller;
import com.Softwareprojekt.interfaces.LatticeType;
import com.Softwareprojekt.interfaces.SpaceGroupID;
import com.Softwareprojekt.interfaces.View;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Label;
import java.awt.Panel;
import java.util.ResourceBundle;

import javax.swing.JComboBox;

public class SpaceGroupSelectionPanel extends Panel implements View {

	private final Controller _controller;
	private final ResourceBundle bundle = ResourceBundle.getBundle("resources.Messages");
	private final JComboBox<CenteringTypeListItem> _centeringTypeList;
	private final JComboBox<SpaceGroupID> _spaceGroupList;
	
	
	public SpaceGroupSelectionPanel(Controller controller) {
		super();
		this._controller = controller;
		
		this.setLayout(new FlowLayout(FlowLayout.LEFT));
		this.setPreferredSize(new Dimension(600, 50));
		this.setBackground(org.jzy3d.colors.ColorAWT.toAWT(SpaceGroupView.Viewport_Background));
		this.setForeground(org.jzy3d.colors.ColorAWT.toAWT(SpaceGroupView.Foregrond_Color));
		
		this._centeringTypeList = new JComboBox<CenteringTypeListItem>(new CenteringTypeListItem[] {
			new CenteringTypeListItem(LatticeType.CenteringType.I),
			new CenteringTypeListItem(LatticeType.CenteringType.P),			
			new CenteringTypeListItem(LatticeType.CenteringType.C),
			new CenteringTypeListItem(LatticeType.CenteringType.F)
		});
		
		this._spaceGroupList = new JComboBox<SpaceGroupID>();
		//this._spaceGroupList.addItem("<html>I4<small>1</small>32</html>");
	
		this.add(new Label(bundle.getString("centeringType")));
		this.add(this._centeringTypeList);
		
		this.add(new Label(bundle.getString("spaceGroup")));
		this.add(this._spaceGroupList);
	}
	
	@Override
	public void invalidateView() {
		// TODO Auto-generated method stub

	}

	@Override
	public void invalidateViewOptions() {
		// TODO Auto-generated method stub
	}

}
