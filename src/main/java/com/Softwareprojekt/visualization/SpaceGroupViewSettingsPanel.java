package com.Softwareprojekt.visualization;

import com.Softwareprojekt.interfaces.Controller;
import com.Softwareprojekt.interfaces.View;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;
import javax.swing.*;

public class SpaceGroupViewSettingsPanel extends Panel implements ActionListener, View {

	private final Controller _controller;
	private final JToggleButton _btnVertices;
	private final JToggleButton _btnWireframe;
	private final JToggleButton _btnFace;
	private final JToggleButton _btnSpacing;
	private final JToggleButton _btnChromaticFaces;
	private final ResourceBundle bundle = ResourceBundle.getBundle("Messages");
	private static final Dimension BUTTON_SIZE = new Dimension(40, 40);
	private static final Color Selected_Color = Color.green;
	
	public SpaceGroupViewSettingsPanel(Controller controller) {
		super();
		this._controller = controller;
		
		this.setPreferredSize(new Dimension(50, 600));
		this.setBackground(org.jzy3d.colors.ColorAWT.toAWT(SpaceGroupView.Viewport_Background));
		this.setForeground(org.jzy3d.colors.ColorAWT.toAWT(SpaceGroupView.Foregrond_Color));
		
		final BoxLayout layout = new BoxLayout(this, BoxLayout.PAGE_AXIS);
		this.setLayout(layout);
		
		_btnVertices = new JToggleButton(new ImageIcon(ClassLoader.getSystemResource("poly_vertice.png")));
		_btnVertices.setToolTipText(bundle.getString("showVertices"));
		styleButton(_btnVertices);
		this.add(_btnVertices);

		_btnWireframe = new JToggleButton(new ImageIcon(ClassLoader.getSystemResource("poly_wire.png")));
		_btnWireframe.setToolTipText(bundle.getString("showWireframe"));
		styleButton(_btnWireframe);
		this.add(_btnWireframe);
		
		_btnFace = new JToggleButton(new ImageIcon(ClassLoader.getSystemResource("poly_face.png")));
		_btnFace.setToolTipText(bundle.getString("showFaces"));
		styleButton(_btnFace);
		this.add(_btnFace);

		_btnChromaticFaces = new JToggleButton(new ImageIcon(ClassLoader.getSystemResource("poly_chromatic.png")));
		_btnChromaticFaces.setToolTipText(bundle.getString("showChromaticFaces"));
		styleButton(_btnChromaticFaces);
		this.add(_btnChromaticFaces);
		
		_btnSpacing = new JToggleButton(new ImageIcon(ClassLoader.getSystemResource("poly_spacing.png")));
		_btnSpacing.setToolTipText(bundle.getString("showSpacing"));
		styleButton(_btnSpacing);
		this.add(_btnSpacing);
		
		this.invalidateViewOptions();
		
		// attach action listener
		this._btnVertices.addActionListener(this);
		this._btnWireframe.addActionListener(this);
		this._btnFace.addActionListener(this);
		this._btnSpacing.addActionListener(this);
		this._btnChromaticFaces.addActionListener(this);
	}
	
	private static void styleButton(AbstractButton btn) {
		btn.setPreferredSize(BUTTON_SIZE);
		btn.setFocusable(false);	
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == _btnVertices) {
			this._controller.setViewOption(Controller.ViewOptions.ShowVertices, _btnVertices.isSelected());
		}
		else if (e.getSource() == _btnFace) {
			this._controller.setViewOption(Controller.ViewOptions.ShowFaces, _btnFace.isSelected());
		}
		else if (e.getSource() == _btnWireframe) {
			this._controller.setViewOption(Controller.ViewOptions.ShowWireframe, _btnWireframe.isSelected());
		}
		else if (e.getSource() == _btnSpacing) {
			this._controller.setViewOption(Controller.ViewOptions.ShowSpacing, _btnSpacing.isSelected());
		}
		else if (e.getSource() == _btnChromaticFaces) {
			this._controller.setViewOption(Controller.ViewOptions.ShowChromaticFaces, _btnChromaticFaces.isSelected());
		}
	}

	@Override
	public void invalidateView() {

	}

	@Override
	public void invalidateViewOptions() {
		this._btnVertices.setSelected(this._controller.getViewOption(Controller.ViewOptions.ShowVertices));
		this._btnWireframe.setSelected(this._controller.getViewOption(Controller.ViewOptions.ShowWireframe));
		this._btnFace.setSelected(this._controller.getViewOption(Controller.ViewOptions.ShowFaces));
		this._btnSpacing.setSelected(this._controller.getViewOption(Controller.ViewOptions.ShowSpacing));
		this._btnChromaticFaces.setSelected(this._controller.getViewOption(Controller.ViewOptions.ShowChromaticFaces));
	}
	
}
