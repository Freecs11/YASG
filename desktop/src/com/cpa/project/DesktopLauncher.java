package com.cpa.project;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.cpa.project.Tiles.Tile;
import com.cpa.project.Tiles.terrainFloorTiles;

import java.util.*;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
    public static void main(String[] arg) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setForegroundFPS(60);
        config.setIdleFPS(10);
        config.setTitle("CPA Project : Survivors");
        config.setWindowedMode(1600, 900);
        config.setResizable(false);
        config.setInitialBackgroundColor(new Color(0, 0, 0, 1));
//        config.setWindowIcon("path/to/icon16.png", "path/to/icon32.png");
//        config.setBackBufferConfig(8, 8, 8, 8, 16, 0, 4);
        config.useVsync(true);

        new Lwjgl3Application(new Survivors(), config);
    }

}
