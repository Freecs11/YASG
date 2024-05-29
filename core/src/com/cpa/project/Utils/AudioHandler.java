package com.cpa.project.Utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;

// This class is used to handle audio in the game and is used to play, pause, and stop audio.
public class AudioHandler {

    // we're going to use the libGDX Music class to handle the streaming of the audio file
    private Music gameOST;
    private Music menuOST;
    private Music gameOverOST;
    private Music zombieOST;
    private  HashMap<String, Music> soundEffects;

    public AudioHandler() {
        soundEffects = new HashMap<>();
    }

    public void playMusic(String name) {
        switch (name) {
            case "game":
                this.gameOST.setLooping(true);
                this.gameOST.setVolume(0.25f);
                this.gameOST.play();
                break;
            case "menu":
                this.menuOST.setLooping(true);
                this.menuOST.setVolume(0.25f);
                this.menuOST.play();
                break;
            case "gameover":
                this.gameOverOST.setLooping(true);
                this.gameOverOST.play();
                break;
            case "zombie":
                this.zombieOST.setLooping(true);
                this.zombieOST.setVolume(0.25f);
                this.zombieOST.play();
                break;
        }
    }

    public void pauseMusic(String name) {
        switch (name) {
            case "game":
                this.gameOST.pause();
                break;
            case "menu":
                this.menuOST.pause();
                break;
            case "gameover":
                this.gameOverOST.pause();
                break;
            case "zombie":
                this.zombieOST.pause();
                break;
        }
    }

    public void stopMusic(String name) {
        switch (name) {
            case "game":
                this.gameOST.stop();
                break;
            case "menu":
                this.menuOST.stop();
                break;
            case "gameover":
                this.gameOverOST.stop();
                break;
            case "zombie":
                this.zombieOST.stop();
                break;
        }
    }


    // add a sound effect to the sound effects map
    public void addSoundEffect(String name, Music soundEffect) {
        soundEffects.put(name, soundEffect);
        soundEffect.setVolume(0.25f);
        soundEffect.play();
    }

    public void playSoundEffect(String name, float volume) {
        if (soundEffects.containsKey(name)) {
            soundEffects.get(name).setVolume(volume);
            soundEffects.get(name).play();
        }
    }


    // stop a sound effect
    public void stopSoundEffect(String name) {
        soundEffects.get(name).stop();
    }

    // pause a sound effect
    public void pauseSoundEffect(String name) {
        soundEffects.get(name).pause();
    }


    public void dispose() {
        gameOST.dispose();
        menuOST.dispose();
        gameOverOST.dispose();
        zombieOST.dispose();
        for (Map.Entry<String, Music> entry : soundEffects.entrySet()) {
            entry.getValue().dispose();
        }
    }

    public Music loadMusic(String path) {
        return Gdx.audio.newMusic(Gdx.files.internal(path));
    }

    public Sound loadSound(String path) {
        return Gdx.audio.newSound(Gdx.files.internal(path));
    }

    public void setGameOST(Music gameOST) {
        this.gameOST = gameOST;
    }

    public void setMenuOST(Music menuOST) {
        this.menuOST = menuOST;
    }

    public boolean hasSound(String name) {
        return soundEffects.containsKey(name) && soundEffects.get(name).isPlaying();
    }



    public void disposeSound(Music buttonClickSound) {
        buttonClickSound.dispose();
    }

    public void disposeMusic(String menu) {
        switch (menu) {
            case "menu":
                menuOST.dispose();
                break;
            case "game":
                gameOST.dispose();
                break;
            case "gameover":
                gameOverOST.dispose();
                break;
            case "zombie":
                zombieOST.dispose();
                break;
        }
    }

    public void setGameOverOST(Music music) {
        this.gameOverOST = music;
    }

    public void setZombieOST(Music music) {
        this.zombieOST = music;
    }
}
