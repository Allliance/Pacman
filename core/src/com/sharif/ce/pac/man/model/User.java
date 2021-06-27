package com.sharif.ce.pac.man.model;

import java.util.ArrayList;

public class User {
    private boolean isGuest;
    private String username;
    private String password;
    private ArrayList<Integer> scores;
    private ArrayList<CellMap> customMaps;
    private GameState state;

    public User(){
        isGuest = true;
        username = "Guest";
        password = "";
        customMaps = new ArrayList<>();
        state = new GameState();
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        customMaps = new ArrayList<>();
        state = new GameState();
    }

    public GameState getState() {
        return state;
    }

    public void setState(GameState state) {
        this.state = state;
    }

    public String getUsername() {
        return username;
    }

    public void addCustomMap(CellMap map) {
        customMaps.add(map);
    }

    public ArrayList<CellMap> getCustomMaps() {
        return customMaps;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public boolean isGuest() {
        return isGuest;
    }
}
