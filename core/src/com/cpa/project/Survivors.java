package com.cpa.project;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.cpa.project.Screens.MenuScreen;
import com.cpa.project.Utils.AudioHandler;

public class Survivors extends Game {
    private SpriteBatch batch;
    private MenuScreen menuScreen;
    public static AudioHandler audioHandler;
    public void setScreen(Screen screen) {
        super.setScreen(screen);
    }

    public void create() {
        audioHandler = new AudioHandler();
        audioHandler.setMenuOST(audioHandler.loadMusic("audio/MenuOST.wav"));
        audioHandler.setGameOST(audioHandler.loadMusic("audio/forest.wav"));
        audioHandler.setGameOverOST(audioHandler.loadMusic("audio/GameOverOST.wav"));
        audioHandler.setZombieOST(audioHandler.loadMusic("audio/monster/Monster-1.wav"));
        batch = new SpriteBatch();
        menuScreen = new MenuScreen(this);
        this.setScreen(menuScreen);
    }

    @Override
    public void dispose() {
        super.dispose();
        batch.dispose();
        audioHandler.dispose();
    }

}
