package com.sharif.ce.pac.man.model;

public class GameState {
    private boolean isGameRunning;
    private boolean isPaused;
    private float energy;
    private int ghostKilledCount;
    private int score;
    private int remainingLives;
    private int initialLifePoints;
    private GameMode mode;
    private CellMap map;

    public GameState(CellMap map, GameMode mode,int initialLifePoints) {
        this.initialLifePoints = initialLifePoints;
        remainingLives = initialLifePoints;
        this.mode = mode;
        energy = 0;
        isPaused = true;
        this.map = CellMap.clone(map);
     }

    public GameState() {
        remainingLives = 3;
        energy = 0;
        isPaused = true;
    }

    public int getInitialLifePoints() {
        return initialLifePoints;
    }

    public boolean isGameRunning() {
        return isGameRunning;
    }

    public float getEnergy() {
        return energy;
    }

    public GameMode getMode() {
        return mode;
    }

    public void decreaseEnergy(float amount) {
        energy -= amount;
        if (energy < 0 || energy == 0) {
            energy = 0;
            ghostKilledCount = 0;
        }
    }

    public void useEnergyPack() {
        energy = 10;
    }

    public void killGhost() {
        ghostKilledCount++;
        score += 200 * ghostKilledCount;
    }

    public void setRemainingLives(int amount){
        remainingLives = amount;
    }

    public int getScore() {
        return score;
    }

    public void increaseScore(int amount) {
        score += amount;
    }

    public void die() {
        remainingLives--;
    }

    public void addLifePoint() {
        remainingLives++;
        remainingLives = Math.min(5, remainingLives);
    }

    public boolean isPaused() {
        return isPaused;
    }

    public void setPaused(boolean isPaused) {
        this.isPaused = isPaused;
    }

    public void startGame() {
        isGameRunning = true;
    }

    public void endGame() {
        isGameRunning = false;
    }

    public int getRemainingLives() {
        return remainingLives;
    }

    public CellMap getMap() {
        return map;
    }
}
