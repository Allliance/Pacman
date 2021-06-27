package com.sharif.ce.pac.man.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;

import java.util.ArrayList;

public class TextureController {

    private static Texture lifePointTexture;
    private static Texture energyBarTexture;
    private static Texture infectedGhostTexture;
    private static Texture background;
    private static Texture muteTexture;
    private static Texture unmuteTexture;
    private static Texture playingPacmanTexture;
    private static ArrayList<Texture> ghostsTextures;
    private static Texture foodTexture;
    private static Texture horizontalWallTexture;
    private static Texture verticalWallTexture;
    private static Texture tileTexture;
    private static Texture bombTexture;
    private static Texture energySmallBarTexture;

    public static void loadTextures(){
        energySmallBarTexture = new Texture(Gdx.files.internal("Icons/EnergySmallBar.png"));
        lifePointTexture = new Texture(Gdx.files.internal("Icons/Life Point.png"));
        energyBarTexture = new Texture(Gdx.files.internal("Icons/Energy bar.png"));
        bombTexture = new Texture(Gdx.files.internal("Icons/Bomb.png"));
        foodTexture = new Texture(Gdx.files.internal("Icons/gold.png"));
        horizontalWallTexture = new Texture(Gdx.files.internal("Icons/HorizontalWall.png"));
        verticalWallTexture = new Texture(Gdx.files.internal("Icons/VerticalWall.png"));
        tileTexture = new Texture(Gdx.files.internal("Icons/tile.png"));
        playingPacmanTexture = stretchImage(Gdx.files.internal("Icons/playerPacman.png"),(int)AssetController.getPacmanLength()
        ,(int)AssetController.getPacmanLength());
        infectedGhostTexture = stretchImage(Gdx.files.internal("Icons/InfectedGhost.png"),(int)AssetController.getGhostWidth(),
                (int)AssetController.getGhostHeight());
        ghostsTextures = new ArrayList<>();
        for (int i = 1; i <= 4; ++i)
            ghostsTextures.add(stretchImage(Gdx.files.internal("Icons/Ghost" + i + ".png"),(int)AssetController.getGhostWidth(),
                    (int)AssetController.getGhostHeight()));
        background = stretchImage(Gdx.files.internal("background.jpg"), Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        muteTexture = stretchImage(Gdx.files.internal("icons/mute.png"), 30, 30);
        unmuteTexture = stretchImage(Gdx.files.internal("icons/unmute.png"), 30, 30);
    }


    public static Texture getBombTexture() {
        return bombTexture;
    }

    public static Texture getLifePointTexture() {
        return lifePointTexture;
    }

    public static Texture getEnergySmallBarTexture() {
        return energySmallBarTexture;
    }

    public static Texture getEnergyBarTexture() {
        return energyBarTexture;
    }

    public static Texture getFoodTexture() {
        return foodTexture;
    }

    public static Texture getInfectedGhostTexture() {
        return infectedGhostTexture;
    }

    public static Texture getMuteTexture() {
        return muteTexture;
    }

    public static Texture getUnmuteTexture() {
        return unmuteTexture;
    }

    public static Texture getHorizontalWallTexture() {
        return horizontalWallTexture;
    }

    public static Texture getVerticalWallTexture() {
        return verticalWallTexture;
    }

    public static Texture getBackground() {
        return background;
    }

    public static Texture getPlayingPacmanTexture() {
        return playingPacmanTexture;
    }

    public static Texture getTileTexture() {
        return tileTexture;
    }

    public static ArrayList<Texture> getGhostsTextures() {
        return ghostsTextures;
    }

    public static Texture stretchImage(FileHandle file, int width, int height) {
        Pixmap originalImage = new Pixmap(file);
        Pixmap stretchedImage = new Pixmap(width, height, originalImage.getFormat());
        stretchedImage.drawPixmap(originalImage, 0, 0, originalImage.getWidth(), originalImage.getHeight()
                , 0, 0, width, height);
        Texture stretchedTexture = new Texture(stretchedImage);
        originalImage.dispose();
        stretchedImage.dispose();
        return stretchedTexture;
    }


}
