package com.Softwareprojekt.Utilities;

//import com.Softwareprojekt.interfaces.LatticeType;
import org.jzy3d.maths.IntegerCoord2d;
import org.jzy3d.picking.PickingSupport;
import org.jzy3d.plot3d.rendering.scene.Graph;
import org.jzy3d.plot3d.rendering.view.View;

import javax.media.opengl.GL;
import javax.media.opengl.glu.GLU;

public class ExtendedPickingSupport extends PickingSupport {

    private long _lastPickTime = 0;

    public long getLastPickTime() {
        return this._lastPickTime;
    }

    public void clear() {
        this.pickableTargets.clear();
        this.pickables.clear();
    }

    @Override
    public void pickObjects(GL gl, GLU glu, View view, Graph graph, IntegerCoord2d pickPoint) {
        this._lastPickTime = System.currentTimeMillis();
        super.pickObjects(gl, glu, view, graph, pickPoint);
    }
}
