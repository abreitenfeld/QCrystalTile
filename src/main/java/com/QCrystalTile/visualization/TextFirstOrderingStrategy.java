package com.QCrystalTile.visualization;

import org.jzy3d.plot3d.primitives.AbstractDrawable;
import org.jzy3d.plot3d.rendering.ordering.BarycentreOrderingStrategy;
import org.jzy3d.plot3d.rendering.view.View;
import org.jzy3d.plot3d.text.DrawableTextWrapper;

public class TextFirstOrderingStrategy extends BarycentreOrderingStrategy {

    public TextFirstOrderingStrategy(View view) {
        super(view);
    }

    @Override
    public double score(AbstractDrawable drawable) {
        // drawable text gets a score of 0 this layer is rendered last
        if (drawable instanceof DrawableTextWrapper) {
            return 0;
        }
        else {
            return super.score(drawable);
        }
    }
}
