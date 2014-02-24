package com.Softwareprojekt.visualization;

import com.Softwareprojekt.InternationalShortSymbol.ID;
import com.Softwareprojekt.interfaces.Controller;
import com.Softwareprojekt.interfaces.View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class SpaceGroupViewSettingsPanel extends JPanel implements ActionListener, View {

	private final Controller<ID> _controller;
	private final JToggleButton _btnVertices;
	private final JToggleButton _btnWireframe;
	private final JToggleButton _btnFace;
	private final JToggleButton _btnSpacing;
    private final JToggleButton _btnLabeling;
    private final Map<Component, String> _labels = new HashMap<Component, String>();
	private final ResourceBundle bundle = ResourceBundle.getBundle("Messages");
	private static final Dimension BUTTON_SIZE = new Dimension(40, 40);
	private static final Color Selected_Color = Color.green;

    private static final String ToolTip_Format = "%s (%s)";

	public SpaceGroupViewSettingsPanel(Controller<ID> controller) {
		super();
		this._controller = controller;

        this.setDoubleBuffered(true);
		this.setPreferredSize(new Dimension(50, 600));
		this.setBackground(org.jzy3d.colors.ColorAWT.toAWT(SpaceGroupView.Viewport_Background));
		this.setForeground(org.jzy3d.colors.ColorAWT.toAWT(SpaceGroupView.Foreground_Color));
		
		final BoxLayout layout = new BoxLayout(this, BoxLayout.PAGE_AXIS);
		this.setLayout(layout);
		
		_btnVertices = new JToggleButton(new ImageIcon(ClassLoader.getSystemResource("poly_vertice.png")));
		this.add(styleButton(_btnVertices));

        _btnLabeling = new JToggleButton(new ImageIcon(ClassLoader.getSystemResource("mesh_labels.png")));
        this.add(styleButton(_btnLabeling));

        _btnWireframe = new JToggleButton(new ImageIcon(ClassLoader.getSystemResource("poly_wire.png")));
		this.add(styleButton(_btnWireframe));
		
		_btnFace = new JToggleButton(new ImageIcon(ClassLoader.getSystemResource("poly_face.png")));
		this.add(styleButton(_btnFace));
		
		_btnSpacing = new JToggleButton(new ImageIcon(ClassLoader.getSystemResource("poly_spacing.png")));
		this.add(styleButton(_btnSpacing));

        this._labels.put(this._btnVertices, bundle.getString("showVertices"));
        this._labels.put(this._btnLabeling, bundle.getString("showLabeling"));
        this._labels.put(this._btnWireframe, bundle.getString("showWireframe"));
        this._labels.put(this._btnFace, bundle.getString("showFaces"));
        this._labels.put(this._btnSpacing, bundle.getString("showSpacing"));

		this.invalidateViewOptions();
		
		// attach action listener
		this._btnVertices.addActionListener(this);
        this._btnLabeling.addActionListener(this);
		this._btnWireframe.addActionListener(this);
		this._btnFace.addActionListener(this);
		this._btnSpacing.addActionListener(this);
	}

    private void updateToggleButton(JToggleButton btn, boolean selected) {
        final String toggleStatus = selected ? bundle.getString("on") : bundle.getString("off");
        btn.setSelected(selected);
        btn.setToolTipText(String.format(ToolTip_Format, this._labels.get(btn), toggleStatus));
    }

	private static <T extends AbstractButton> T styleButton(T btn) {
		btn.setPreferredSize(BUTTON_SIZE);
		btn.setFocusable(false);
        return btn;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == _btnVertices) {
			this._controller.setViewOption(Controller.ViewOptions.ShowVertices, _btnVertices.isSelected());
		}
        else if (e.getSource() == _btnLabeling) {
            this._controller.setViewOption(Controller.ViewOptions.ShowLabeledMeshes, _btnLabeling.isSelected());
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
	}

	@Override
	public void invalidateView() {

	}

	@Override
	public void invalidateViewOptions() {
        updateToggleButton(this._btnVertices, this._controller.getViewOption(Controller.ViewOptions.ShowVertices));
        updateToggleButton(this._btnLabeling, this._controller.getViewOption(Controller.ViewOptions.ShowLabeledMeshes));
        updateToggleButton(this._btnWireframe, this._controller.getViewOption(Controller.ViewOptions.ShowWireframe));
        updateToggleButton(this._btnFace, this._controller.getViewOption(Controller.ViewOptions.ShowFaces));
        updateToggleButton(this._btnSpacing, this._controller.getViewOption(Controller.ViewOptions.ShowSpacing));
	}
	
}
