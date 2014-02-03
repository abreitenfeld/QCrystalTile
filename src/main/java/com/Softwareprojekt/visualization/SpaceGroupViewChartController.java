package com.Softwareprojekt.visualization;

import com.Softwareprojekt.Utilities.ExtendedPickingSupport;
import org.jzy3d.chart.Chart;
import org.jzy3d.chart.controllers.mouse.camera.AWTCameraMouseController;
import org.jzy3d.maths.IntegerCoord2d;
import org.jzy3d.plot3d.rendering.scene.Graph;
import org.jzy3d.plot3d.rendering.view.modes.ViewPositionMode;

import javax.media.opengl.GL;
import javax.media.opengl.glu.GLU;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.List;
import java.util.LinkedList;
import java.util.ResourceBundle;

public class SpaceGroupViewChartController extends AWTCameraMouseController {

    protected final ExtendedPickingSupport _pickingSupport;
    protected final Chart _chart;
    protected final GLU glu = new GLU();
    protected final JPopupMenu _contextMenu;

    protected final ResourceBundle bundle = ResourceBundle.getBundle("Messages");

    public SpaceGroupViewChartController(Chart chart) {
        super(chart);
        this._chart = chart;
        this._chart.addKeyController();
        this._chart.addScreenshotKeyController();
        this._pickingSupport = new ExtendedPickingSupport();

        this._contextMenu = new JPopupMenu();

        // create view menu items
        final JMenu mnuView = new JMenu(bundle.getString("camera"));
        final JMenuItem miViewFree = new JMenuItem(bundle.getString("cameraFree"));
        miViewFree.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                _chart.getView().setViewPositionMode(ViewPositionMode.FREE);
            }
        });

        final JMenuItem miViewTop = new JMenuItem(bundle.getString("cameraTop"));
        miViewTop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                _chart.getView().setViewPositionMode(ViewPositionMode.TOP);
            }
        });

        final JMenuItem miViewProfile = new JMenuItem(bundle.getString("cameraProfile"));
        miViewProfile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                _chart.getView().setViewPositionMode(ViewPositionMode.PROFILE);
            }
        });
        mnuView.add(miViewFree);
        mnuView.add(miViewTop);
        mnuView.add(miViewProfile);
        this._contextMenu.add(mnuView);
    }

    public JPopupMenu getPopupMenu() {
        return this._contextMenu;
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        //super.mouseWheelMoved(e);
    }

    public ExtendedPickingSupport getPickingSupport() {
        return this._pickingSupport;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (SwingUtilities.isLeftMouseButton(e)) {
            int yflip = -e.getY() + this._chart.getCanvas().getRendererHeight();
            Graph graph = _chart.getScene().getGraph();
            GL gl = _chart.getView().getCurrentGL();

            // will trigger vertex selection event to those subscribing to PickingSupport.
            this._pickingSupport.pickObjects(gl, glu, this._chart.getView(), graph, new IntegerCoord2d(e.getX(), yflip));
            // release gl context
            this._chart.getView().getCurrentContext().release();
        }
        else if (SwingUtilities.isRightMouseButton(e)) {
            this._contextMenu.show(e.getComponent(), e.getX(), e.getY());
        }
    }
}
