package com.Softwareprojekt.visualization;

import org.jzy3d.plot3d.primitives.AbstractDrawable;
import org.jzy3d.plot3d.rendering.ordering.BarycentreOrderingStrategy;
import org.jzy3d.plot3d.text.DrawableTextWrapper;

public class TextFirstOrderingStrategy extends BarycentreOrderingStrategy {

    @Override
    public double score(AbstractDrawable drawable) {
        // drawable text gets a lower score
        if (drawable instanceof DrawableTextWrapper) {
            return 0.5f * super.score(drawable);
        }
        else {
            return super.score(drawable);
        }
    }
}
