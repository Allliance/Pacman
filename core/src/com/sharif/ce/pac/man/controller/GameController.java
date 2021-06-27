package com.sharif.ce.pac.man.controller;

import com.badlogic.gdx.graphics.Texture;
import com.sharif.ce.pac.man.Pacman;
import com.sharif.ce.pac.man.model.*;
import com.sharif.ce.pac.man.view.GameOverMenu;
import com.sharif.ce.pac.man.view.GameScreen;

import java.util.ArrayList;
import java.util.Collections;

public class GameController {

    private Pacman game;
    private PacmanPlayer pacman;
    private GameScreen gameScreen;
    private ArrayList<Ghost> ghosts;
    private static final int rowsCount = AssetController.getRowCellCount();
    private static final int columnsCount = AssetController.getColumnCellCount();

    public GameController(Pacman game, CellMap map, GameMode mode,int initialLifePoints) {
        this.game = game;
        DataController.setGameState(new GameState(map, mode,initialLifePoints));
        getState().getMap().clearMap();
        getState().getMap().fillMap(mode.getModeBombDistance());
    }

    public GameController(Pacman game) {
        this.game = game;
    }

    public GameScreen getGameScreen() {
        return gameScreen;
    }

    public void run() {
        SoundController.stopBackgroundMusic();
        SoundController.playGameBackgroundMusic();
        getState().startGame();
        initializePlayers();
        gameScreen = new GameScreen(game);
        game.setScreen(gameScreen);
    }

    private void initializePlayers() {
        pacman = new PacmanPlayer(TextureController.getPlayingPacmanTexture());
        ghosts = new ArrayList<>();
        ArrayList<Texture> ghostsTextures = TextureController.getGhostsTextures();
        Collections.shuffle(ghostsTextures);
        ghosts.add(new Ghost(ghostsTextures.get(0), 1, 1));
        ghosts.add(new Ghost(ghostsTextures.get(1), 1, columnsCount));
        ghosts.add(new Ghost(ghostsTextures.get(2), rowsCount, 1));
        ghosts.add(new Ghost(ghostsTextures.get(3), rowsCount, columnsCount));
    }

    public void startAgain(boolean resetGhostsPosition) {
        getState().setPaused(true);
        if (resetGhostsPosition) {
            for (Ghost ghost : ghosts) {
                ghost.resetPosition(2);
                gameScreen.locateGhost(ghost);
            }
        }
        pacman.resetPosition();
        gameScreen.locatePacman();
    }

    public void restartGame() {
        DataController.setGameState(new GameState(getState().getMap(), getState().getMode(),getState().getInitialLifePoints()));
        getState().startGame();
        getState().getMap().clearMap();
        getState().getMap().fillMap(getState().getMode().getModeBombDistance());
        startAgain(true);
        game.setScreen(gameScreen);
    }

    public void renderCheck(float delta) {
        if (getState().isPaused())
            return;
        getState().decreaseEnergy(delta);
        for (Ghost ghost : ghosts) {
            ghost.decreaseDelay(delta);
        }
        if (checkForFood())
            eatFood();
        if (!getState().getMap().isAnyCellFull()) {
            SoundController.playWinSound();
            getState().addLifePoint();
            getState().getMap().fillMap(getState().getMode().getModeBombDistance());
            startAgain(true);
            return;
        }
        Ghost hitGhost = checkForGhostHit();
        if (hitGhost != null)
            hitGhost(hitGhost);
        if (checkForEnergy())
            useEnergyPack();
    }

    public boolean checkForEnergy() {
        for (int i = 1; i <= rowsCount; ++i) {
            for (int j = 1; j <= columnsCount; ++j) {
                if (!getState().getMap().hasBomb(i, j))
                    continue;
                if (getDistanceFromPacman(i, j) <= AssetController.getFoodLength() / 2 + AssetController.getPacmanLength() / 2) {
                    removeEnergy(i, j);
                    return true;
                }
            }
        }
        return false;
    }

    public boolean checkForFood() {
        for (int i = 1; i <= rowsCount; ++i) {
            for (int j = 1; j <= columnsCount; ++j) {
                if (getState().getMap().isCellEmpty(i, j))
                    continue;
                if (getDistanceFromPacman(i, j) <= AssetController.getFoodLength() / 2 + AssetController.getPacmanLength() / 2) {
                    removeFood(i, j);
                    return true;
                }
            }
        }
        return false;
    }

    public Ghost checkForGhostHit() {
        float pacmanX = getPacman().getX() + pacman.getWidth() / 2;
        float pacmanY = getPacman().getY() + pacman.getHeight() / 2;
        for (Ghost ghost : ghosts) {
            if (!ghost.isAvailable()) continue;
            float ghostCenterX = ghost.getX() + ghost.getWidth() / 2;
            float ghostCenterY = ghost.getY() + ghost.getHeight() / 2;
            float distance = (float) Math.sqrt((pacmanX - ghostCenterX) * (pacmanX - ghostCenterX) + (pacmanY - ghostCenterY) * (pacmanY - ghostCenterY));
            if (distance <= ghost.getHeight() / 2 + pacman.getWidth() / 2)
                return ghost;
        }
        return null;
    }

    public float getDistanceFromPacman(int row, int column) {
        float pacmanX = getPacman().getX() + pacman.getWidth() / 2;
        float pacmanY = getPacman().getY() + pacman.getHeight() / 2;
        float cellCenterX = (column - 1 + 0.5f) * AssetController.getCellWidth();
        float cellCenterY = (rowsCount - row + 0.5f) * AssetController.getCellHeight();
        return (float) Math.sqrt((pacmanX - cellCenterX) * (pacmanX - cellCenterX) + (pacmanY - cellCenterY) * (pacmanY - cellCenterY));
    }

    public void removeFood(int row, int column) {
        getState().getMap().emptyCell(row, column);
    }

    public void removeEnergy(int row, int column) {
        getState().getMap().removeBomb(row, column);
    }

    public void useEnergyPack() {
        getState().useEnergyPack();
        SoundController.playEnergySound();
    }

    public void eatFood() {
        SoundController.playEatSound();
        getState().increaseScore(5);
    }

    public void hitGhost(Ghost ghost) {
        if (!ghost.isAvailable())
            return;
        if (getState().getEnergy() > 0) {
            killGhost(ghost);
            return;
        }
        SoundController.playDieSound();
        if (getState().getRemainingLives() == 0)
            endGame(true);
        getState().die();
        getState().setPaused(true);
        ghost.resetPosition(0);
        gameScreen.locateGhost(ghost);
        pacman.resetPosition();
        gameScreen.locatePacman();
        Ghost otherHittingGhost;
        while((otherHittingGhost = checkForGhostHit()) != null){
            otherHittingGhost.resetPosition(0);
            gameScreen.locateGhost(otherHittingGhost);

        }
    }

    public void killGhost(Ghost ghost) {
        SoundController.playGhostDeathSound();
        getState().killGhost();
        ghost.resetPosition(5);
        gameScreen.locateGhost(ghost);
    }

    public void endGame(boolean saveScore) {
        getState().endGame();
        SoundController.stopGameBackgroundMusic();
        SoundController.playBackgroundMusic();
        if (saveScore) {
            SoundController.playGameOverSound();
            DataController.addGameResult(UserController.getLoggedUser(), getState().getScore());
            game.setScreen(new GameOverMenu(game, getState().getScore()));
        } else {
            game.setScreen(game.getMainMenu());
        }
    }

    public ArrayList<Ghost> getGhosts() {
        return ghosts;
    }

    public PacmanPlayer getPacman() {
        return pacman;
    }

    public GameState getState() {
        return DataController.getGameState();
    }

}
