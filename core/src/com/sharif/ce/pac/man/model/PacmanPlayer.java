package com.sharif.ce.pac.man.model;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.MoveByAction;
import com.sharif.ce.pac.man.Pacman;
import com.sharif.ce.pac.man.controller.AssetController;
import javafx.util.Pair;

public class PacmanPlayer extends Actor {
    private final float width = AssetController.getPacmanLength();
    private final float height = AssetController.getPacmanLength();
    private final float speed = 0.2f;
    private int currentRotation = 0;
    private int downKey = -10000;
    private int row;
    private int startRow;
    private int startColumn;
    private int column;
    private Sprite pacmanSprite;

    public PacmanPlayer(Texture pacmanTexture) {
        super();
        locateStartCell();
        row = startRow;
        column = startColumn;
        getMap().emptyCell(row, column);
        this.pacmanSprite = new Sprite(pacmanTexture,(int)width,(int)height);
        pacmanSprite.setOrigin(width/2,height/2);
        setBounds(getX(), getY(), width, height);
        addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                downKey = keycode;
                return true;
            }

            @Override
            public boolean keyUp(InputEvent event, int keycode) {
                //downKey = -10000;
                return true;
            }
        });
    }

    public void resetPosition() {
        downKey = -10000;
        row = startRow;
        column = startColumn;
        getActions().clear();
        getMap().emptyCell(getRow(), getColumn());
    }

    private void locateStartCell() {
        getMap().banCell(1, 1);
        getMap().banCell(1, AssetController.getColumnCellCount());
        getMap().banCell(AssetController.getRowCellCount(), 1);
        getMap().banCell(AssetController.getRowCellCount(), AssetController.getColumnCellCount());
        Pair<Integer, Integer> startCell = getMap().getRandomUnbannedCell();
        startRow = startCell.getKey();
        startColumn = startCell.getValue();
        getMap().unBanCell(1, 1);
        getMap().unBanCell(1, AssetController.getColumnCellCount());
        getMap().unBanCell(AssetController.getRowCellCount(), 1);
        getMap().unBanCell(AssetController.getRowCellCount(), AssetController.getColumnCellCount());
    }

    @Override
    public void act(float delta) {
        if (Pacman.getGameController().getState().isPaused())
            return;
        if (downKey == Input.Keys.RIGHT)
            addMoveAction(MoveDirection.RIGHT);
        if (downKey == Input.Keys.LEFT)
            addMoveAction(MoveDirection.LEFT);
        if (downKey == Input.Keys.UP)
            addMoveAction(MoveDirection.UP);
        if (downKey == Input.Keys.DOWN)
            addMoveAction(MoveDirection.DOWN);
        super.act(delta);
    }

    private void addMoveAction(MoveDirection direction) {
        if (getActions().size > 0)
            return;
        MoveByAction action = new MoveByAction();
        action.setDuration(speed);
        if (direction.equals(MoveDirection.DOWN) && !getCell(row, column).isDownWall()) {
            currentRotation = 1;
            row++;
            action.setAmount(0, -AssetController.getCellWidth());
        }
        if (direction.equals(MoveDirection.UP) && !getCell(row, column).isUpWall()) {
            currentRotation = -1;
            row--;
            action.setAmount(0, AssetController.getCellHeight());
        }
        if (direction.equals(MoveDirection.LEFT) && !getCell(row, column).isLeftWall()) {
            currentRotation = 2;
            column--;
            action.setAmount(-AssetController.getCellWidth(), 0);
        }
        if (direction.equals(MoveDirection.RIGHT) && !getCell(row, column).isRightWall()) {
            currentRotation = 0;
            column++;
            action.setAmount(AssetController.getCellWidth(), 0);
        }
        addAction(action);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        pacmanSprite.setX(getX());
        pacmanSprite.setY(getY());
        if (currentRotation == 2){
            pacmanSprite.setFlip(true,false);
            pacmanSprite.draw(batch);
            pacmanSprite.setFlip(false,false);
        }
        else
            batch.draw(pacmanSprite,getX(),getY(),width/2, height/2,width,height,1,1,
                -90 * currentRotation);
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    private CellMap getMap() {
        return Pacman.getGameController().getState().getMap();
    }

    private Cell getCell(int row, int column) {
        return Pacman.getGameController().getState().getMap().getCell(row, column);
    }

}
