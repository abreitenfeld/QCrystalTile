package com.Softwareprojekt.visualization;

import com.Softwareprojekt.InternationalShortSymbol.ID;
import com.Softwareprojekt.interfaces.Controller;
import com.Softwareprojekt.interfaces.View;

import javax.swing.*;
import java.awt.*;
import java.util.ResourceBundle;

public class SpaceGroupViewStatus extends JPanel implements View {

    private final Controller<ID> _controller;
    private final JLabel _leftLabel;
    private final JLabel _rightLabel;
    private volatile boolean _showLoadingIndicator = false;

    protected final ResourceBundle bundle = ResourceBundle.getBundle("Messages");

    public SpaceGroupViewStatus(Controller<ID> controller) {
        super(new GridLayout(0, 2));
        this._controller = controller;

        this.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        this.setBackground(org.jzy3d.colors.ColorAWT.toAWT(SpaceGroupView.Viewport_Background));

        this._leftLabel = new JLabel();
        this._leftLabel.setForeground(org.jzy3d.colors.ColorAWT.toAWT(SpaceGroupView.Foreground_Color));
        this._rightLabel = new JLabel(bundle.getString("calculating")
                , new ImageIcon(ClassLoader.getSystemResource("loading.gif")), JLabel.RIGHT);
        this._rightLabel.setFont(this._rightLabel.getFont().deriveFont(Font.BOLD));
        this._rightLabel.setForeground(org.jzy3d.colors.ColorAWT.toAWT(SpaceGroupView.Foreground_Color));
        this._rightLabel.setHorizontalAlignment(JLabel.RIGHT);
        this._rightLabel.setVerticalAlignment(JLabel.BOTTOM);

        this.add(this._leftLabel);
        this.add(this._rightLabel);
    }

    public void setStatusCaption(String caption) {
        this._leftLabel.setText(caption);
    }

    public void enableLoadingIndicator(boolean enable) {
        this._showLoadingIndicator = enable;
        this._rightLabel.setVisible(enable);
    }


    @Override
    public void invalidateView() {

    }

    @Override
    public void invalidateViewOptions() {

    }
}
