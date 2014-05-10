package com.QCrystalTile.app;

import com.QCrystalTile.interfaces.Controller;

import static com.QCrystalTile.visualization.SpaceGroupController.createController;

/**
 * Created by jakob on 1/9/14.
 */
public class App {
    public static void main(String[] args) throws Exception {
        final Controller controller = createController();
        controller.getView().show();
    }
}
