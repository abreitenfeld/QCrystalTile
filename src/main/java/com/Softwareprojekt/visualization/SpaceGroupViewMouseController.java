package com.Softwareprojekt.visualization;

import com.Softwareprojekt.Utilities.ExtendedPickingSupport;
import org.jzy3d.chart.Chart;
import org.jzy3d.chart.controllers.mouse.camera.AWTCameraMouseController;
import org.jzy3d.maths.IntegerCoord2d;
import org.jzy3d.plot3d.rendering.scene.Graph;

import javax.media.opengl.GL;
import javax.media.opengl.glu.GLU;
import javax.swing.*;
import java.awt.event.MouseEvent;

public class SpaceGroupViewMouseController extends AWTCameraMouseController {

    protected final ExtendedPickingSupport _pickingSupport;
    protected final Chart _chart;
    protected final GLU glu = new GLU();

    public SpaceGroupViewMouseController(Chart chart) {
        super(chart);
        this._chart = chart;
        this._pickingSupport = new ExtendedPickingSupport();
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
    }
}
