package com.Softwareprojekt.app;

import com.Softwareprojekt.interfaces.Controller;

import static com.Softwareprojekt.visualization.SpaceGroupController.createController;

/**
 * Created by jakob on 1/9/14.
 */
public class App {
    public static void main(String[] args) throws Exception {
        final Controller controller = createController();
        controller.getView().show();
    }
}
