package com.sharif.ce.pac.man.model;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.sharif.ce.pac.man.controller.AssetController;
import com.sharif.ce.pac.man.controller.TextureController;

public class Tile extends Actor {
    private final float width;
    private final float height;
    private final float foodLength;
    private final float wallLength;
    private Texture img;
    private TileMap tileMap;
    private int row;
    private int column;
    private Cell cell;

    public Tile(Cell cell, int row, int column, TileMap tileMap) {
        super();
        img = TextureController.getTileTexture();
        this.row = row;
        this.column = column;
        this.tileMap = tileMap;
        this.cell = cell;
        width = tileMap.getWidth()/AssetController.getColumnCellCount();
        height = tileMap.getHeight()/AssetController.getRowCellCount();
        foodLength = AssetController.getFoodLength()*(width/AssetController.getCellWidth());
        wallLength = width/(float)20;
        setBounds(this.getX(), this.getY(), width, height);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (!isBanned())
            batch.draw(img, getX(), getY(), width, height);
        if (cell.isDownWall())
            batch.draw(TextureController.getHorizontalWallTexture(), getX(), getY(), width, wallLength);
        if (cell.isUpWall())
            batch.draw(TextureController.getHorizontalWallTexture(), getX(), getY() + height - wallLength, width, wallLength);
        if (cell.isRightWall())
            batch.draw(TextureController.getVerticalWallTexture(), getX() + width - wallLength, getY(),wallLength,height);
        if (cell.isLeftWall())
            batch.draw(TextureController.getVerticalWallTexture(),getX(),getY(),wallLength,height);
        if (!isEmpty())
            batch.draw(TextureController.getFoodTexture(),getX() + (width-foodLength)/2,getY()+(height-foodLength)/2,
                    foodLength,foodLength);
        if (hasBomb())
            batch.draw(TextureController.getBombTexture(),getX()+(width - foodLength)/2,getY() + (height-foodLength)/2,
                    foodLength,foodLength);
        super.draw(batch, parentAlpha);
    }

    private boolean isEmpty(){
        return tileMap.getMap().isCellEmpty(row,column);
    }

    private boolean isBanned(){
        return tileMap.getMap().isCellBanned(row,column);
    }

    private boolean hasBomb(){
        return tileMap.getMap().hasBomb(row,column);
    }

}
