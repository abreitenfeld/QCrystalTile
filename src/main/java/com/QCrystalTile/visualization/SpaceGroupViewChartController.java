package com.QCrystalTile.visualization;

import com.QCrystalTile.utilities.ExtendedPickingSupport;
import org.jzy3d.chart.Chart;
import org.jzy3d.chart.controllers.camera.AbstractCameraController;
import org.jzy3d.chart.controllers.thread.camera.CameraThreadController;
import org.jzy3d.maths.BoundingBox3d;
import org.jzy3d.maths.Coord2d;
import org.jzy3d.maths.IntegerCoord2d;
import org.jzy3d.plot3d.rendering.canvas.CanvasNewtAwt;
import org.jzy3d.plot3d.rendering.canvas.ICanvas;
import org.jzy3d.plot3d.rendering.scene.Graph;
import org.jzy3d.plot3d.rendering.view.modes.ViewPositionMode;

import com.jogamp.newt.event.MouseEvent;
import com.jogamp.newt.event.MouseListener;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.glu.GLU;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class SpaceGroupViewChartController extends AbstractCameraController implements
        ActionListener, MouseListener {

    protected final ExtendedPickingSupport _pickingSupport;
    protected final Chart _chart;
    protected final GLU glu = new GLU();
    protected final JPopupMenu _contextMenu;
    protected  final Map<JMenuItem, ViewPositionMode> _modes = new HashMap<JMenuItem, ViewPositionMode>();

    protected final ResourceBundle bundle = ResourceBundle.getBundle("Messages");

    private static final float Min_Bounding_Box_Value = -10f;
    private static final float Max_Bounding_Box_Value = 1f;

    public SpaceGroupViewChartController(Chart chart) {
        register(chart);
        addSlaveThreadController(new CameraThreadController(chart));

        this._chart = chart;
        this._chart.addKeyController();
        this._chart.addScreenshotKeyController();
        this._pickingSupport = new ExtendedPickingSupport();

        this._contextMenu = new JPopupMenu();
        this._contextMenu.setLightWeightPopupEnabled(false);
        // create view menu items
        final JMenu mnuView = new JMenu(bundle.getString("camera"));
        final JMenuItem miViewFree = new JMenuItem(bundle.getString("cameraFree"));
        this._modes.put(miViewFree, ViewPositionMode.FREE);
        final JMenuItem miViewTop = new JMenuItem(bundle.getString("cameraTop"));
        this._modes.put(miViewTop, ViewPositionMode.TOP);
        final JMenuItem miViewProfile = new JMenuItem(bundle.getString("cameraProfile"));
        this._modes.put(miViewProfile, ViewPositionMode.PROFILE);

        miViewFree.addActionListener(this);
        miViewTop.addActionListener(this);
        miViewProfile.addActionListener(this);

        mnuView.add(miViewFree);
        mnuView.add(miViewTop);
        mnuView.add(miViewProfile);
        this._contextMenu.add(mnuView);
    }

    public void register(Chart chart) {
        super.register(chart);
        chart.getCanvas().addMouseController(this);
    }

    public void dispose() {
        for (Chart chart : targets) {
            chart.getCanvas().removeMouseController(this);
        }
        super.dispose();
    }

    public boolean handleSlaveThread(MouseEvent e) {
        if (e.getClickCount() > 1) {
            if (threadController != null) {
                threadController.start();
                return true;
            }
        }
        if (threadController != null)
            threadController.stop();
        return false;
    }

    public JPopupMenu getPopupMenu() {
        return this._contextMenu;
    }

    public ExtendedPickingSupport getPickingSupport() {
        return this._pickingSupport;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        //
        if (handleSlaveThread(e))
            return;

        prevMouse.x = e.getX();
        prevMouse.y = e.getY();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JMenuItem) {
            final JMenuItem item = (JMenuItem)e.getSource();
            if (this._modes.containsKey(item)) {
                _chart.getView().setViewPositionMode(this._modes.get(item));
            }
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        Coord2d mouse = new Coord2d(e.getX(), e.getY());

        // Rotate
        if (e.getButton() == MouseEvent.BUTTON1) {
            Coord2d move = mouse.sub(prevMouse).div(100);
            rotate(move);
        }
        this.prevMouse = mouse;
    }

    @Override
    public void mouseWheelMoved(MouseEvent e) {
        float factor = (e.getWheelRotation() / 10.0f);
        final BoundingBox3d bound = this._chart.getView().getBounds();

        bound.setXmin(Math.max(Math.min(bound.getXmin() - factor, Max_Bounding_Box_Value), Min_Bounding_Box_Value));
        bound.setYmin(Math.max(Math.min(bound.getYmin() - factor, Max_Bounding_Box_Value), Min_Bounding_Box_Value));
        bound.setZmin(Math.max(Math.min(bound.getZmin() - factor, Max_Bounding_Box_Value), Min_Bounding_Box_Value));
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
           int yflip = -e.getY() + this._chart.getCanvas().getRendererHeight();
            Graph graph = _chart.getScene().getGraph();
            final GLAutoDrawable glAutoDrawable = getCanvasAsGLAutoDrawable();
            glAutoDrawable.getContext().makeCurrent();
            // will trigger vertex selection event to those subscribing to PickingSupport.
            this._pickingSupport.pickObjects(glAutoDrawable.getGL(), glu, this._chart.getView(), graph, new IntegerCoord2d(e.getX(), yflip));
            // release gl context
            glAutoDrawable.getContext().release();
        }
        else if (e.getButton() == MouseEvent.BUTTON3) {
            this._contextMenu.show((Component)_chart.getCanvas(), e.getX(), e.getY());
        }
    }

    protected GLAutoDrawable getCanvasAsGLAutoDrawable() {
        ICanvas canvas = _chart.getCanvas();

        if (canvas instanceof CanvasNewtAwt) {
            return ((CanvasNewtAwt) canvas).getWindow();
        }
        else if (canvas instanceof GLAutoDrawable) {
            return ((GLAutoDrawable) canvas);
        }
        return null;
    }

    @Override
    public void mouseMoved(MouseEvent e) { }

    @Override
    public void mouseReleased(MouseEvent e) { }

    @Override
    public void mouseEntered(MouseEvent e) { }

    @Override
    public void mouseExited(MouseEvent e) { }

}
