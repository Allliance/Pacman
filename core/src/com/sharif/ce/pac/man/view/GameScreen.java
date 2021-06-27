package com.sharif.ce.pac.man.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.sharif.ce.pac.man.Pacman;
import com.sharif.ce.pac.man.controller.*;
import com.sharif.ce.pac.man.model.*;

import java.util.ArrayList;

public class GameScreen extends Menu {

    private final float cellWidth = AssetController.getCellWidth();
    private final float cellHeight = AssetController.getCellHeight();
    private final int rowCount = AssetController.getRowCellCount();
    private final int columnCount = AssetController.getColumnCellCount();
    private final float energyBarWidth = 150;
    private final float energyBarHeight = 30;
    private final Texture lifePointTexture = TextureController.getLifePointTexture();
    private final Texture energyBarTexture = TextureController.getEnergyBarTexture();
    private final Texture energySmallBarTexture = TextureController.getEnergySmallBarTexture();
    private int currentRemainingLives;
    private boolean isGamePaused = false;
    private Label scoreLabel;
    private Label remainingLivesLabel;
    private Table remainingLivesTable;
    private Table itemsTable;
    private Container<Table> tableContainer;
    private TileMap tileMap;

    public GameScreen(Pacman game) {
        super(game, false);
        initializeWidgets();
        arrangeWidgets();
        addListeners();
    }

    private void initializeWidgets() {
        tableContainer = new Container<>();
        itemsTable = new Table();
        tableContainer.setActor(itemsTable);
        remainingLivesTable = new Table();
        scoreLabel = new Label("0", AssetController.getScoreSkin(), "title-3");
        scoreLabel.setAlignment(1);
        remainingLivesLabel = new Label("x" + getGameState().getRemainingLives(), AssetController.getDefaultSkin(), "title");
        tileMap = new TileMap(getGameState().getMap(),900,450);
    }

    private void arrangeWidgets() {
        itemsTable.setFillParent(true);
        tableContainer.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        tableContainer.center();
        Table energyTable = new Table();
        energyTable.add(new Actor() {
            @Override
            public void draw(Batch batch, float parentAlpha) {
                batch.draw(energyBarTexture, getX(), getY(), energyBarWidth, energyBarHeight);
                for (int i = 1; i <= 5; ++i) {
                    if (getGameState().getEnergy() > (i - 1) * 2) {
                        batch.draw(energySmallBarTexture, getX() + 29 * (i - 1) + 5, getY() + 0.1f * energyBarHeight, 24, energyBarHeight * 0.8f);
                    }
                }
                super.draw(batch, parentAlpha);
            }
        }).colspan(1).fillX().height(30).center();
        itemsTable.add(energyTable).colspan(1).expandX().fillX();
        itemsTable.add(scoreLabel).expandX().fillX().center().colspan(1);
        itemsTable.add(remainingLivesTable).expandX().fillX().colspan(1);
        itemsTable.row();
        itemsTable.add(tileMap.getContainer()).center().colspan(4).width(900).height(450);
        itemsTable.row();
        stage.addActor(tableContainer);
        musicButton.setPosition(0, Gdx.graphics.getHeight() - musicButton.getHeight());
        stage.addActor(musicButton);
        locatePacman();
        stage.addActor(getPacman());
        for (Ghost ghost : getGhosts()) {
            locateGhost(ghost);
            stage.addActor(ghost);
        }
    }

    private void addListeners() {
        stage.addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (isGamePaused)
                    return false;
                if (keycode == Input.Keys.RIGHT || keycode == Input.Keys.LEFT ||
                        keycode == Input.Keys.UP || keycode == Input.Keys.DOWN) {
                    getGameController().getState().setPaused(false);
                    Array<EventListener> listeners = getPacman().getListeners();
                    for (int i = 0; i < listeners.size; ++i) {
                        EventListener eventListener = listeners.get(i);
                        if (eventListener instanceof InputListener) {
                            ((InputListener) eventListener).keyDown(event, keycode);
                        }
                    }
                } else if (keycode == Input.Keys.ESCAPE) {
                    getGameController().getState().setPaused(true);
                    showPauseDialog();
                } else
                    return false;
                return true;
            }
        });
    }

    private void showPauseDialog() {
        isGamePaused = true;
        final Dialog pauseDialog = new Dialog("", AssetController.getScoreSkin());
        pauseDialog.setModal(true);
        pauseDialog.setResizable(false);
        pauseDialog.setMovable(false);
        Label pauseLabel = new Label("Paused", AssetController.getDefaultSkin());
        MenuButton resumeButton = new MenuButton("Resume", AssetController.getDefaultSkin());
        MenuButton restartButton = new MenuButton("Restart", AssetController.getDefaultSkin());
        MenuButton mainMenuButton = new MenuButton("Main Menu", AssetController.getDefaultSkin());
        resumeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                killPauseDialog(pauseDialog);
            }
        });
        restartButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                killPauseDialog(pauseDialog);
                getGameController().restartGame();
            }
        });
        mainMenuButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                killPauseDialog(pauseDialog);
                SoundController.stopGameBackgroundMusic();
                SoundController.playBackgroundMusic();
                game.setScreen(game.getMainMenu());
            }
        });
        pauseDialog.getContentTable().add(pauseLabel).expandX();
        pauseDialog.getButtonTable().add(resumeButton).expandX().padBottom(5);
        pauseDialog.getButtonTable().row();
        pauseDialog.getButtonTable().add(restartButton).expandX().padBottom(5);
        pauseDialog.getButtonTable().row();
        pauseDialog.getButtonTable().add(mainMenuButton).expandX();
        pauseDialog.getButtonTable().row();
        pauseDialog.show(stage);
    }

    private void killPauseDialog(Dialog dialog) {
        isGamePaused = false;
        dialog.hide();
        dialog.cancel();
        dialog.remove();
    }

    public void locatePacman() {
        getPacman().setPosition((getPacman().getColumn() - 1) * cellWidth + (cellWidth - getPacman().getWidth()) / 2
                , (rowCount - getPacman().getRow()) * cellHeight + (cellHeight - getPacman().getHeight()) / 2);
    }

    public void locateGhost(Ghost ghost) {
        ghost.setPosition((ghost.getColumn() - 1) * cellWidth + (cellWidth - ghost.getWidth()) / 2,
                (rowCount - ghost.getRow()) * cellHeight + (cellHeight - ghost.getHeight()) / 2);
    }

    @Override
    public void show() {
        tileMap.setMap(getGameState().getMap());
        game.disposeMenus();
        super.show();
    }

    @Override
    public void render(float delta) {
        getGameController().renderCheck(delta);
        DataController.setGameState(getGameState());
        if (getGameState().getRemainingLives() != currentRemainingLives)
            updateRemainingLives();
        scoreLabel.setText(getGameState().getScore());
        super.render(delta);
    }

    public void updateRemainingLives() {
        currentRemainingLives = getGameState().getRemainingLives();
        remainingLivesLabel.setText("x" + currentRemainingLives);
        remainingLivesTable.clear();
        for (int i = 1; i <= currentRemainingLives; ++i) {
            remainingLivesTable.add(new Actor() {
                @Override
                public void draw(Batch batch, float parentAlpha) {
                    batch.draw(lifePointTexture, getX(), getY(), 40, 40);
                    super.draw(batch, parentAlpha);
                }
            }).height(40).width(40).padLeft(5).padRight(5);
        }
        remainingLivesTable.add(remainingLivesLabel).height(50);
    }

    private GameState getGameState() {
        return game.getGameController().getState();
    }

    private GameController getGameController() {
        return Pacman.getGameController();
    }

    private PacmanPlayer getPacman() {
        return getGameController().getPacman();
    }

    private ArrayList<Ghost> getGhosts() {
        return getGameController().getGhosts();
    }

}

