package com.cpa.project.Screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.cpa.project.Utils.AssetManager;

import static com.cpa.project.Survivors.audioHandler;

public class GameOverScreen implements Screen {

    private final Stage gameOverStage = new Stage();
    Game game;
    private final Music ButtonClickSound;

    public GameOverScreen(Game game) {
        Gdx.input.setInputProcessor(gameOverStage);
        audioHandler.playMusic("gameover");
        this.game = game;
        ButtonClickSound = audioHandler.loadMusic("audio/click.wav");
    }

    @Override
    public void show() {
        Skin skin = AssetManager.getSkin();
        Table gameOverTable = new Table();
        gameOverTable.setFillParent(true);
        Texture gameOverTexture = new Texture(Gdx.files.internal("GameOver.png"));
        Image gameOverImage = new Image(gameOverTexture);
        gameOverTable.add(gameOverImage).height(500f);
        gameOverTable.row();
        TextButton returnMenu = new TextButton("Return to Menu", skin);

        // Return to menu button OnClick
        returnMenu.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.postRunnable(() -> audioHandler.addSoundEffect("ButtonClick" , ButtonClickSound));
                Gdx.app.postRunnable(() -> dispose());
                game.setScreen(new MenuScreen(game));
            }
        });
        gameOverTable.row();
        gameOverTable.add(returnMenu).width(200f).height(50f);
        gameOverStage.addActor(gameOverTable);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(.9f, .9f, .9f, 1);
        Gdx.gl.glClear(Gdx.gl.GL_COLOR_BUFFER_BIT);
        gameOverStage.act();
        gameOverStage.draw();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {
        audioHandler.pauseMusic("gameover");
    }

    @Override
    public void resume() {
        audioHandler.playMusic("gameover");
    }

    @Override
    public void hide() {
        audioHandler.stopMusic("gameover");
        gameOverStage.clear();
    }

    @Override
    public void dispose() {
        gameOverStage.dispose();
    }
}
