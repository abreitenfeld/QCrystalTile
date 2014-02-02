package com.Softwareprojekt.visualization;

import com.Softwareprojekt.interfaces.Mesh;

import java.util.EventListener;
import java.util.List;

public interface SpaceGroupViewControllerListener extends EventListener {

    public enum Action { showAll, hideAll, setMonochromColors, setChromaticColors, setFaceColors, setViewPositionTop,
    setViewPositionProfile, setViewPositionFree };

    public void actionPerformed(Action action);

}
