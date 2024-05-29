package com.cpa.project.Screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.kotcrab.vis.ui.VisUI;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.cpa.project.State.PlayState;
import com.cpa.project.Utils.AssetManager;
import com.kotcrab.vis.ui.widget.VisProgressBar;

import static com.cpa.project.Survivors.audioHandler;

public class GameScreen implements Screen {

    private Game game;
    private PlayState playState;
    SpriteBatch batch;

    private VisProgressBar healthBar;
    private VisProgressBar xpBar;

    private final Stage gameStage = new Stage();

    private final Music ButtonClickSound;

    public GameScreen(Game game) {
        Gdx.input.setInputProcessor(gameStage);
        batch = new SpriteBatch();
        ButtonClickSound = audioHandler.loadMusic("audio/click2.wav");
        playState = new PlayState();
        this.game = game;
    }

    @Override
    public void show() {
        if (!VisUI.isLoaded())
            VisUI.load(AssetManager.getSkin());
        // Init the state, when the game is done I think this needs a loading screen
        Skin skin = AssetManager.getSkin();

        // The div for the pause button
        Table gameTable = new Table();

        gameTable.top().right();
        // The div for the resume button
        Table resumeTable = new Table();
        gameTable.setFillParent(true);
        resumeTable.setFillParent(true);

        // Pause button
        TextButton pauseButton = new TextButton("Pause", skin);
        // Pause button OnClick
        pauseButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.postRunnable(() -> audioHandler.addSoundEffect("ButtonClick", ButtonClickSound)); // on utilise postRunnable pour éviter les problèmes de thread avec openGL
                PlayState.isPaused = true;
                gameTable.setVisible(false);
                resumeTable.setVisible(true);
            }
        });

        // Resume button
        TextButton resumeButton = new TextButton("Resume", skin);
        // Resume button OnClick
        resumeButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.postRunnable(() -> audioHandler.addSoundEffect("ButtonClick", ButtonClickSound));
                PlayState.isPaused = false;
                resumeTable.setVisible(false);
                gameTable.setVisible(true);
            }
        });

        // Add the buttons to the tables
        gameTable.add(pauseButton).padBottom(20f).width(200f).height(50f);
        resumeTable.add(resumeButton).padBottom(20f).width(200f).height(50f);

        // Add new row ie. new line
        resumeTable.row();

        // Return to menu button
        TextButton returnMenu = new TextButton("Return to Menu", skin);

        // Return to menu button OnClick
        returnMenu.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.postRunnable(() -> audioHandler.addSoundEffect("ButtonClick", ButtonClickSound));
                Gdx.app.postRunnable(() -> dispose());
                game.setScreen(new MenuScreen(game));
            }
        });
        // Add the return to menu button to the resume table
        resumeTable.add(returnMenu).padBottom(20f).width(200f).height(50f);

        // Set the resume table to invisible
        resumeTable.setVisible(false);

        Texture sonicWave = AssetManager.getSonicWave();
        Image sonicWaveImage = new Image(sonicWave);
        Texture fireBall = AssetManager.getFireBall();
        Image fireBallImage = new Image(fireBall);
        Texture autoFireBall = AssetManager.getAutoFireBall();
        Image autoFireBallImage = new Image(autoFireBall);
        Texture heal = AssetManager.getHeal();
        Image healImage = new Image(heal);

        Table skillsTable = new Table();
        skillsTable.bottom().left().setPosition(Gdx.graphics.getWidth() / 2f - 155, 50);
        skillsTable.defaults().padRight(40f).spaceBottom(10f);
        skillsTable.add(fireBallImage).width(50f).height(50f);
        skillsTable.add(sonicWaveImage).width(50f).height(50f);
        skillsTable.add(autoFireBallImage).width(50f).height(50f);
        skillsTable.add(healImage).width(50f).height(50f);
        skillsTable.row();
        skillsTable.add(new Label("Fire Ball", skin));
        skillsTable.add(new Label("Sonic Wave", skin));
        skillsTable.add(new Label("Auto Fire Ball", skin));
        skillsTable.add(new Label("Heal", skin));
        skillsTable.row();
        skillsTable.add(new Label("LMB", skin));
        skillsTable.add(new Label("SPACE", skin));
        skillsTable.add(new Label("Autocast", skin));
        skillsTable.add(new Label("H", skin));

        healthBar = new VisProgressBar(0, 1, 0.01f, false);
        healthBar.setValue(1);
        healthBar.setAnimateInterpolation(Interpolation.smooth);
        healthBar.setAnimateDuration(.8f);
        healthBar.setColor(new Color(1f, 0.2f, 0.4f, 1));
        healthBar.setWidth(Gdx.graphics.getWidth() / 5f * 3);
        healthBar.setHeight(20);
        healthBar.setPosition(Gdx.graphics.getWidth() / 4.5f, 10);

        xpBar = new VisProgressBar(0, 1, 0.01f, false);
        xpBar.setValue(0);
        xpBar.setAnimateInterpolation(Interpolation.smooth);
        xpBar.setAnimateDuration(.8f);
        xpBar.setColor(new Color(1f, 1f, 0f, 1));
        xpBar.setWidth(Gdx.graphics.getWidth() / 5f * 3);
        xpBar.setHeight(20);
        xpBar.setPosition(Gdx.graphics.getWidth() / 4.5f, 30);


        gameStage.addActor(healthBar);
        gameStage.addActor(xpBar);
        gameStage.addActor(resumeTable);
        gameStage.addActor(gameTable);
        gameStage.addActor(skillsTable);

        audioHandler.playMusic("game");
    }

    @Override
    public void render(float delta) {
        if (PlayState.player.getHealth() <= 0) {
            game.setScreen(new GameOverScreen(game));
//            this.dispose();
            PlayState.isPaused = true;
//            dispose(); // attention ce dispose() fait crash le jeu ...
        }
        if (!PlayState.isPaused) {
            playState.update(delta);
            playState.render(batch);
        }
        xpBar.setValue(PlayState.player.getExperience() / PlayState.player.getExperienceToNextLevel());
        healthBar.setValue(PlayState.player.getHealth() / PlayState.player.getMaxHealth());
        gameStage.draw();
        gameStage.act();
        batch.begin();
        BitmapFont font = AssetManager.getFont();
        String sonicWaveCD = String.format("%.2f", PlayState.player.getSpells().get("SonicWave").getTimeToReady());
        String healCD = String.format("%.2f", PlayState.player.getSpells().get("Heal").getTimeToReady());
        String autoFireBallCD = String.format("%.2f", PlayState.player.getSpells().get("AutoFireBall").getTimeToReady());
        Vector3 unprojectedPos = PlayState.topDownCamera.unproject(new Vector3(100, 800, 0));
        if (PlayState.player.getSpells().get("SonicWave").getTimeToReady() > 0.08f) {
            font.draw(batch, sonicWaveCD, unprojectedPos.x + 458 + 188 + 5, unprojectedPos.y + 32);
        }
        if (PlayState.player.getSpells().get("AutoFireBall").getTimeToReady() > 0.08f) {
            font.draw(batch, autoFireBallCD, unprojectedPos.x + 570 + 188 + 5, unprojectedPos.y + 32);
        }
        if (PlayState.player.getSpells().get("Heal").getTimeToReady() > 0.08f) {
            font.draw(batch, healCD, unprojectedPos.x + 673 + 188 + 5, unprojectedPos.y + 32);
        }
        font.draw(batch, "Lv. " + PlayState.player.getLevel(),
                unprojectedPos.x + 570 + 140, unprojectedPos.y - 53);
        font.draw(batch, Math.round(PlayState.player.getHealth() / PlayState.player.getMaxHealth() * 100) + "%",
                unprojectedPos.x + 570 + 142, unprojectedPos.y - 73);

        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        playState.resize(width, height);
    }

    @Override
    public void pause() {
        playState.pause();
        audioHandler.pauseMusic("game");
    }

    @Override
    public void resume() {
        playState.resume();
        audioHandler.playMusic("game");
    }

    @Override
    public void hide() {
        audioHandler.stopMusic("game");
    }

    @Override
    public void dispose() {
        playState.dispose();
        gameStage.dispose();
        batch.dispose();
        playState = null;
//        VisUI.dispose();
    }
}
