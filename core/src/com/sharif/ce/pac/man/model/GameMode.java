package com.sharif.ce.pac.man.model;

public enum GameMode {
    EASY(4),HARD(10);
    private int bombDistance;
    GameMode(int bombDistance){
        this.bombDistance = bombDistance;
    }

    public int getModeBombDistance(){
        return bombDistance;
    }

}
