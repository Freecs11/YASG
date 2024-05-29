package com.cpa.project.Screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.cpa.project.Utils.AssetManager;
import com.cpa.project.Utils.AudioHandler;

import static com.cpa.project.Survivors.audioHandler;

public class MenuScreen implements Screen {
    private final Stage menuStage = new Stage();
    private Game game;

    private final Music ButtonClickSound;

    public MenuScreen(Game game){
        Gdx.input.setInputProcessor(menuStage);
        this.game = game;
        audioHandler.playMusic("menu");
        ButtonClickSound = audioHandler.loadMusic("audio/click.wav");
    }


    @Override
    public void show() {
        Skin skin = AssetManager.getSkin();
        Table menuTable = new Table(skin);
        menuTable.setFillParent(true);
        menuTable.defaults().pad(15f);
        Image title = new Image(new Texture(Gdx.files.internal("SplashTitle.png")));
        menuTable.add(title).padBottom(200f);
        menuTable.row();
        TextButton playButton = new TextButton("Play", skin);
        playButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.postRunnable(() -> audioHandler.addSoundEffect("ButtonClick" , ButtonClickSound));
                Gdx.app.postRunnable(() -> dispose());
                game.setScreen(new GameScreen(game));
            }
        });
        menuTable.add(playButton).width(200f).height(50f);
        menuTable.row();
        TextButton guideButton = new TextButton("How To", skin);
        guideButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.postRunnable(() -> audioHandler.addSoundEffect("ButtonClick" , ButtonClickSound));
                Gdx.app.postRunnable(() -> dispose());
                game.setScreen(new GuideScreen(game));
            }
        });
        menuTable.add(guideButton).width(200f).height(50f);
        menuTable.row();
        TextButton exitButton = new TextButton("Exit", skin);

        exitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.postRunnable(() -> audioHandler.addSoundEffect("ButtonClick" , ButtonClickSound));
//                dispose();
                System.exit(0);
            }
        });
        menuTable.add(exitButton).width(200f).height(50f);
        menuStage.addActor(menuTable);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(.9f, .9f, .9f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        menuStage.act();
        menuStage.draw();
    }

    @Override
    public void resize(int width, int height) {
        menuStage.getViewport().update(width, height, true);
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
        menuStage.clear();
        audioHandler.stopMusic("menu");
    }

    @Override
    public void dispose() {
        menuStage.dispose();
        audioHandler.disposeSound(ButtonClickSound);
    }
}
