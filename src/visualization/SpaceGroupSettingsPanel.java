package visualization;

import interfaces.Controller;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SpaceGroupSettingsPanel extends Panel implements ActionListener {

	private final Controller _controller;
	
	public SpaceGroupSettingsPanel(Controller controller) {
		this._controller = controller;
		
		this.setPreferredSize(new Dimension(300, 600));
		//this.setBackground(Color.white);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {

		
	}

}
