package com.sharif.ce.pac.man.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class AssetController {

    private static final int rowCellCount = 11;
    private static final int columnCellCount = rowCellCount * 2;
    private static final float cellWidth = 450f /(float) rowCellCount;
    private static final float cellHeight = 900f /(float) columnCellCount;
    private static final float pacmanLength = cellWidth * 0.7f;
    private static final float ghostWidth = cellWidth * 0.7f;
    private static final float ghostHeight = cellHeight * 0.8f;
    private static final float foodLength = cellHeight / 2;
    private static Skin defaultSkin;
    private static Skin buttonSkin;
    private static Skin scoreSkin;

    public static void loadAssets() {
        SoundController.loadSounds();
        TextureController.loadTextures();
        defaultSkin = new Skin(Gdx.files.internal("Skins/freezing/skin/freezing-ui.json"));
        buttonSkin = new Skin(Gdx.files.internal("Skins/lgdxs/skin/lgdxs-ui.json"));
        scoreSkin = new Skin(Gdx.files.internal("Skins/level-plane/skin/level-plane-ui.json"));
    }

    public static float getGhostHeight() {
        return ghostHeight;
    }

    public static float getGhostWidth() {
        return ghostWidth;
    }

    public static float getFoodLength() {
        return foodLength;
    }

    public static float getPacmanLength() {
        return pacmanLength;
    }

    public static Skin getDefaultSkin() {
        return defaultSkin;
    }

    public static Skin getButtonSkin() {
        return buttonSkin;
    }

    public static Skin getScoreSkin() {
        return scoreSkin;
    }

    public static int getRowCellCount() {
        return rowCellCount;
    }

    public static int getColumnCellCount() {
        return columnCellCount;
    }

    public static float getCellWidth() {
        return cellWidth;
    }

    public static float getCellHeight() {
        return cellHeight;
    }
}
