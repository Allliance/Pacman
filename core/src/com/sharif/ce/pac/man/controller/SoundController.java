package com.sharif.ce.pac.man.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class SoundController {
    private static Sound ghostDeathSound;
    private static Sound gameOverSound;
    private static Sound energySound;
    private static Sound mouseHoverSound;
    private static Sound mouseClickSound;
    private static Sound eatSound;
    private static Sound dieSound;
    private static Sound winSound;
    private static Sound loseSound;
    private static Music backgroundMusic;
    private static Music gameBackgroundMusic;
    private static boolean isMute;
    private static int volume = 100;

    public static void loadSounds() {
        mouseHoverSound = Gdx.audio.newSound(Gdx.files.internal("Sound Effects/mouse hover.wav"));
        mouseClickSound = Gdx.audio.newSound(Gdx.files.internal("Sound Effects/drop.wav"));
        eatSound = Gdx.audio.newSound(Gdx.files.internal("Sound Effects/Eat Sound.wav"));
        dieSound = Gdx.audio.newSound(Gdx.files.internal("Sound Effects/Die Sound.wav"));
        gameOverSound = Gdx.audio.newSound(Gdx.files.internal("Sound Effects/GameOver Sound.wav"));
        ghostDeathSound = Gdx.audio.newSound(Gdx.files.internal("Sound Effects/Ghost Death Sound.wav"));
        winSound = Gdx.audio.newSound(Gdx.files.internal("Sound Effects/Win Sound.wav"));
        energySound = Gdx.audio.newSound(Gdx.files.internal("Sound Effects/Energy Sound.wav"));
        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("backgroundMusic.mp3"));
        gameBackgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("gameBackgroundMusic.mp3"));
        isMute = false;
        backgroundMusic.setLooping(true);
        playBackgroundMusic();
    }

    public static boolean isMute() {
        return isMute;
    }

    public static void playEnergySound() {
        if (!isMute)
            energySound.play();
    }

    public static void playWinSound() {
        if (!isMute)
            winSound.play();
    }

    public static void unMute() {
        backgroundMusic.setVolume(volume);
        gameBackgroundMusic.setVolume(volume);
        isMute = false;
    }

    public static void mute() {
        backgroundMusic.setVolume(0);
        gameBackgroundMusic.setVolume(0);
        isMute = true;
    }

    public static void playBackgroundMusic() {
        if (isMute())
            return;
        if (!backgroundMusic.isPlaying())
            backgroundMusic.play();
    }

    public static void stopBackgroundMusic() {
        if (backgroundMusic.isPlaying())
            backgroundMusic.stop();
    }

    public static void playGameBackgroundMusic() {
        if (isMute())
            return;
        if (!gameBackgroundMusic.isPlaying())
            gameBackgroundMusic.play();
    }

    public static void stopGameBackgroundMusic() {
        if (gameBackgroundMusic.isPlaying())
            gameBackgroundMusic.stop();
    }

    public static void playMouseHoverSound() {
        if (!isMute)
            mouseHoverSound.play();
    }

    public static void playGameOverSound() {
        if (!isMute)
            gameOverSound.play();
    }

    public static void playGhostDeathSound() {
        if (!isMute)
            ghostDeathSound.play();
    }

    public static void playMouseClickSound() {
        if (!isMute)
            mouseClickSound.play();
    }

    public static void playEatSound() {
        if (!isMute())
            eatSound.play();
    }

    public static void playDieSound() {
        if (!isMute())
            dieSound.play();
    }

}
