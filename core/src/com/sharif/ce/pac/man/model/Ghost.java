package com.sharif.ce.pac.man.model;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.MoveByAction;
import com.badlogic.gdx.scenes.scene2d.actions.ScaleToAction;
import com.sharif.ce.pac.man.Pacman;
import com.sharif.ce.pac.man.controller.AssetController;
import com.sharif.ce.pac.man.controller.GameController;
import com.sharif.ce.pac.man.controller.TextureController;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Ghost extends Actor {

    private final float cellLength = AssetController.getCellWidth();
    private final float width = AssetController.getGhostWidth();
    private final float height = AssetController.getGhostHeight();
    private final Sprite infectedSprite;
    private final Sprite normalSprite;
    private final float speed = 0.4f;
    private final int smartness = 5;
    private PacmanPlayer target;
    private float delayTime;
    private int moveCounter;
    private int row;
    private int startRow;
    private int column;
    private int startColumn;

    public Ghost(Texture ghostTexture, int startRow, int startColumn) {
        super();
        target = getGameController().getPacman();
        this.startRow = startRow;
        delayTime = 2;
        row = startRow;
        this.startColumn = startColumn;
        column = startColumn;
        normalSprite = new Sprite(ghostTexture,(int)width,(int)height);
        infectedSprite = new Sprite(TextureController.getInfectedGhostTexture(),(int)width,(int)height);
        normalSprite.setOrigin(width/2,height/2);
        infectedSprite.setOrigin(width/2,height/2);
        setBounds(getX(), getY(), width, height);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if (Pacman.getGameController().getState().isPaused() || !isAvailable())
            return;
        if (getActions().size == 0) move();
    }

    public void decreaseDelay(float delta) {
        delayTime -= delta;
        if (delayTime < 0)
            delayTime = 0;
    }

    public void setDelayTime(float delayTime) {
        this.delayTime = delayTime;
    }

    public void resetPosition(float delayTime) {
        row = startRow;
        column = startColumn;
        getActions().clear();
        setScale(0.1f);
        ScaleToAction action = new ScaleToAction();
        action.setDuration(0.5f);
        action.setScale(1);
        addAction(action);
        setDelayTime(delayTime);
    }

    private void move() {
        if (moveCounter == smartness)
            moveCounter = 0;
        moveCounter++;
        ArrayList<Pair<Integer, MoveDirection>> directions = new ArrayList<>();
        if (!getCell(row, column).isRightWall())
            directions.add(new Pair<>(getMap().getDistance(target.getRow(), target.getColumn(), row, column + 1), MoveDirection.RIGHT));
        if (!getCell(row, column).isDownWall())
            directions.add(new Pair<>(getMap().getDistance(target.getRow(), target.getColumn(), row + 1, column), MoveDirection.DOWN));
        if (!getCell(row, column).isLeftWall())
            directions.add(new Pair<>(getMap().getDistance(target.getRow(), target.getColumn(), row, column - 1), MoveDirection.LEFT));
        if (!getCell(row, column).isUpWall())
            directions.add(new Pair<>(getMap().getDistance(target.getRow(), target.getColumn(), row - 1, column), MoveDirection.UP));
        Collections.sort(directions, new Comparator<Pair<Integer, MoveDirection>>() {
            @Override
            public int compare(Pair<Integer, MoveDirection> o1, Pair<Integer, MoveDirection> o2) {
                return o1.getKey() - o2.getKey();
            }
        });
        if (getGameController().getState().getMode() == GameMode.EASY || moveCounter == 1)
            Collections.shuffle(directions);
        else if (getGameController().getState().getEnergy() > 0)
            Collections.reverse(directions);
        MoveDirection bestDirection = directions.get(0).getValue();
        MoveByAction action = new MoveByAction();
        action.setDuration(speed);
        if (bestDirection.equals(MoveDirection.RIGHT)) {
            column++;
            action.setAmount(cellLength, 0);
        }
        if (bestDirection.equals(MoveDirection.DOWN)) {
            row++;
            action.setAmount(0, -cellLength);
        }
        if (bestDirection.equals(MoveDirection.LEFT)) {
            column--;
            action.setAmount(-cellLength, 0);
        }
        if (bestDirection.equals(MoveDirection.UP)) {
            row--;
            action.setAmount(0, cellLength);
        }
        addAction(action);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        Sprite drawingSprite;
        if (getGameController().getState().getEnergy() > 0)
            drawingSprite = infectedSprite;
        else
            drawingSprite = normalSprite;
        drawingSprite.setPosition(getX(),getY());
        drawingSprite.setScale(getScaleX(),getScaleY());
        drawingSprite.draw(batch,parentAlpha);
        super.draw(batch, parentAlpha);
    }

    public Cell getCell(int r, int c) {
        Cell cell = Pacman.getGameController().getState().getMap().getCell(r, c);
        return cell;
    }

    public CellMap getMap() {
        return Pacman.getGameController().getState().getMap();
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public float getDelayTime() {
        return delayTime;
    }

    public boolean isAvailable() {
        return delayTime == 0;
    }

    public GameController getGameController() {
        return Pacman.getGameController();
    }

}
