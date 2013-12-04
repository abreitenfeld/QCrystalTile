package visualization;

import interfaces.Controller;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;
import javax.swing.*;

import org.eclipse.swt.internal.win32.BUTTON_IMAGELIST;

public class SpaceGroupToolPanel extends Panel implements ActionListener {

	private final Controller _controller;
	private final JToggleButton _btnVertices;
	private final JToggleButton _btnWireframe;
	private final JToggleButton _btnFace;
	
	private final ResourceBundle bundle = ResourceBundle.getBundle("resources.Messages");
	private static final Dimension BUTTON_SIZE = new Dimension(40, 40);
	
	public SpaceGroupToolPanel(Controller controller) {
		this._controller = controller;
		
		this.setPreferredSize(new Dimension(50, 600));
		this.setBackground(Color.white);
		
		final BoxLayout layout = new BoxLayout(this, BoxLayout.PAGE_AXIS);
		this.setLayout(layout);
		
		_btnVertices = new JToggleButton(new ImageIcon(ClassLoader.getSystemResource("images/poly_vertice.png")));
		_btnVertices.setToolTipText(bundle.getString("showVertices"));
		_btnVertices.setSelected(this._controller.getViewOption(Controller.ViewOptions.ShowVertices));
		_btnVertices.addActionListener(this);
		styleButton(_btnVertices);
		this.add(_btnVertices);

		_btnWireframe = new JToggleButton(new ImageIcon(ClassLoader.getSystemResource("images/poly_wire.png")));
		_btnWireframe.setToolTipText(bundle.getString("showWireframe"));
		_btnWireframe.setSelected(this._controller.getViewOption(Controller.ViewOptions.ShowWireframe));
		_btnWireframe.addActionListener(this);
		styleButton(_btnWireframe);
		this.add(_btnWireframe);
		
		_btnFace = new JToggleButton(new ImageIcon(ClassLoader.getSystemResource("images/poly_face.png")));
		_btnFace.setToolTipText(bundle.getString("showFaces"));
		_btnFace.setSelected(this._controller.getViewOption(Controller.ViewOptions.ShowFaces));
		_btnFace.addActionListener(this);
		styleButton(_btnFace);
		this.add(_btnFace);

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
	}
	
}
