package com.sharif.ce.pac.man.model;

import com.sharif.ce.pac.man.controller.DataController;

public class GameResult {

    private User user;
    private Integer score;
    private int id;
    private static int IdCounter;
    static{
        IdCounter = DataController.getGameResults().size()+1;
    }

    public GameResult(User user,Integer score){
        this.user = user;
        this.score = score;
        this.id = IdCounter++;
    }

    public User getUser() {
        return user;
    }

    public Integer getScore() {
        return score;
    }

    public int getId() {
        return id;
    }
}
