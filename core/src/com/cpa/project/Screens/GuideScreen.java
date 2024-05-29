package com.cpa.project.Screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.cpa.project.Utils.AssetManager;

import static com.cpa.project.Survivors.audioHandler;

public class GuideScreen implements Screen {

    private final Stage guideStage = new Stage();
    Game game;
    private final Music ButtonClickSound;

    public GuideScreen(Game game) {
        Gdx.input.setInputProcessor(guideStage);
        this.game = game;
        audioHandler.playMusic("menu");
        ButtonClickSound = audioHandler.loadMusic("audio/click2.wav");
    }

    @Override
    public void show() {
        Skin skin = AssetManager.getSkin();
        Table guideTable = new Table();
        guideTable.setFillParent(true);
        guideTable.defaults().spaceBottom(10f);
        Label guideString = new Label("Use Z Q S D or the arrow keys to move around." +
                "\nEach time you level up, you will restore to full health." +
                "\nYour spell damages will increase, you can also attack faster.", skin);
        guideString.setFontScale(1.5f);
        guideTable.add(guideString).width(500f);
        guideTable.row();
        Label spells = new Label("Spells:", skin);
        spells.setFontScale(1.5f);
        guideTable.add(spells);
        guideTable.row();
        Texture sonicWave = AssetManager.getSonicWave();
        Image sonicWaveImage = new Image(sonicWave);
        Texture fireBall = AssetManager.getFireBall();
        Image fireBallImage = new Image(fireBall);
        Texture autoFireBall = AssetManager.getAutoFireBall();
        Image autoFireBallImage = new Image(autoFireBall);
        Texture heal = AssetManager.getHeal();
        Image healImage = new Image(heal);
        guideTable.add(fireBallImage).width(50f).height(50f).spaceTop(10f);
        guideTable.row();
        guideTable.add(new Label("FireBall: Shoots a fireball in the direction of the cursor, basic attack", skin)).spaceTop(10f).left();
        guideTable.row();
        guideTable.add(autoFireBallImage).width(50f).height(50f).spaceTop(10f);
        guideTable.row();
        guideTable.add(new Label("AutoFireBall: Shoots fireballs in a circle around the player based on level", skin)).spaceTop(10f).left();
        guideTable.row();
        guideTable.add(sonicWaveImage).width(50f).height(50f).spaceTop(10f);
        guideTable.row();
        guideTable.add(new Label("SonicWave: Displaces enemies around the player, pushing them away", skin)).spaceTop(10f).left();
        guideTable.row();
        guideTable.add(healImage).width(50f).height(50f).spaceTop(10f);
        guideTable.row();
        guideTable.add(new Label("Heal: Heals the player", skin)).spaceTop(10f).left();
        guideTable.row();


        TextButton backButton = new TextButton("Return", skin);
        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.postRunnable(() -> audioHandler.addSoundEffect("ButtonClick" , ButtonClickSound));
                Gdx.app.postRunnable(() -> dispose());
                game.setScreen(new MenuScreen(game));
            }
        });

        guideTable.row();
        guideTable.add(backButton).width(200f).height(50f).spaceTop(100f);
        guideStage.addActor(guideTable);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(.9f, .9f, .9f, 1);
        Gdx.gl.glClear(Gdx.gl.GL_COLOR_BUFFER_BIT);
        guideStage.act();
        guideStage.draw();
    }

    @Override
    public void resize(int width, int height) {
        guideStage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {
        audioHandler.pauseMusic("menu");
    }

    @Override
    public void resume() {
        audioHandler.playMusic("menu");
    }

    @Override
    public void hide() {
        guideStage.clear();
    }

    @Override
    public void dispose() {
        guideStage.dispose();
    }
}
