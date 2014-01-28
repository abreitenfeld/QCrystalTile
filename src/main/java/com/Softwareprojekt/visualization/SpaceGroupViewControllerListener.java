package com.Softwareprojekt.visualization;

import com.Softwareprojekt.interfaces.Mesh;

import java.util.EventListener;
import java.util.List;

public interface SpaceGroupViewControllerListener extends EventListener {

    public enum Action { showAll, hideAll, setMonochromColors, setChromaticColors };

    public void actionPerformed(Action action, List<Mesh> meshes);

}
