package com.cpa.project.Utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class AssetManager {
    private static final Texture playerTexture = new Texture("wizard.png");
    private static final Texture skeletonTexture = new Texture("skeleton.png");
    private static final Texture FireBallSmall = new Texture("FireBallSmall.png");

    private static final Texture Autofireball = new Texture("firebullet.png");

    private static final Skin skin = new Skin(Gdx.files.internal("skin/OS Eight.json"));

    private static final BitmapFont font = new BitmapFont();




    private static final Texture sonicWave = new Texture(Gdx.files.internal("icons/BTNInnerFire.jpg"));
    private static final Texture fireBall = new Texture(Gdx.files.internal("icons/BTNFireBolt.jpg"));
    private static final Texture autoFireBall = new Texture(Gdx.files.internal("icons/BTNOrbOfFire.jpg"));
    private static final Texture heal = new Texture(Gdx.files.internal("icons/BTNHeal.jpg"));


    private static final Texture splashTitle = new Texture(Gdx.files.internal("SplashTitle.png"));

    private final static Texture terrainTiles = new Texture(Gdx.files.internal("terrain.png"));
    private final static Texture treeTexture = new Texture(Gdx.files.internal("map/grassland_trees.png"));

    private final static Texture arrowTexture = new Texture(Gdx.files.internal("arrow.png"));

    public static Texture getSonicWave(){ return sonicWave; }

    public static Texture getFireBall(){ return fireBall; }

    public static Texture getAutoFireBall(){ return autoFireBall; }

    public static Texture getTreeTexture() {
        return treeTexture;
    }

    public static Texture getTerrainTiles() {
        return terrainTiles;
    }

    public static Texture getSplashTitle() {
        return splashTitle;
    }

    public static Texture getHeal(){ return heal; }



    public static Texture getPlayerTexture() {
        return playerTexture;
    }

    public static Texture getSkeletonTexture() {
        return skeletonTexture;
    }

    public static Texture getFireBallSmall() {
        return FireBallSmall;
    }

    public static Texture getAutofireball() {
        return Autofireball;
    }

    public static Skin getSkin() {
        return skin;
    }

    public static Texture getArrowTexture() {
        return arrowTexture;
    }

    public static BitmapFont getFont() {
        return font;
    }

}