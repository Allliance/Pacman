package com.sharif.ce.pac.man.controller;

import com.badlogic.gdx.files.FileHandle;
import com.google.gson.Gson;
import com.sharif.ce.pac.man.model.CellMap;
import com.sharif.ce.pac.man.model.GameResult;
import com.sharif.ce.pac.man.model.GameState;
import com.sharif.ce.pac.man.model.User;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class DataController {

    private static ArrayList<GameResult> gameResults;
    private static ArrayList<CellMap> maps;

    public static void initialize(){
        loadUsersData();
        loadGameResults();
        loadMaps();
    }

    private static void loadMaps(){
        maps = new ArrayList<>();
        CellMap[] mapsArray = new Gson().fromJson(loadJson("maps"),CellMap[].class);
        if (mapsArray != null){
            for(int i = 0;i<mapsArray.length;++i)
                maps.add(mapsArray[i]);
        }
    }

    private static void loadGameResults(){
        gameResults = new ArrayList<>();
        GameResult[] gameResultsArray = new Gson().fromJson(loadJson("scores"),GameResult[].class);
        if (gameResultsArray != null){
            for(int i = 0;i<gameResultsArray.length;++i)
                gameResults.add(gameResultsArray[i]);
        }
    }

    private static void loadUsersData(){
        ArrayList users = new ArrayList<>();
        User[] usersArray = new Gson().fromJson(loadJson("users"), User[].class);
        if (usersArray != null) {
            for (int i = 0; i < usersArray.length; ++i)
                users.add(usersArray[i]);
        }
        UserController.setUsers(users);
    }

    public static void saveData(){
        saveGameResults();
        saveUsers();
    }

    public static void setGameState(GameState gameState) {
        UserController.getLoggedUser().setState(gameState);
    }

    public static void saveUsers(){
        writeJson(new Gson().toJson(UserController.getUsers()),"users");
    }

    public static void saveGameResults(){
        ArrayList<GameResult> filteredResults = new ArrayList<>();
        for(GameResult result : gameResults){
            if (result.getUser().isGuest())
                continue;
            filteredResults.add(result);
        }
        writeJson(new Gson().toJson(filteredResults),"scores");
    }

    public static void addGameResult(User user,int score){
        if (user.isGuest())
            return;
        gameResults.add(new GameResult(user,score));
    }

    private static void writeJson(String json,String filename){
        FileHandle writer = new FileHandle("core/assets/data/" + filename + ".json");
        writer.writeString(json,false);
    }

    private static String loadJson(String filename){
        Path filePath = Paths.get("core/assets/data/" + filename + ".json");
        if (!Files.exists(filePath))
            return "null";
        String json = "null";
        try {
            json = Files.readString(filePath);
        } catch (IOException e) {
            System.exit(0);
        }
        return json;
    }

    public static ArrayList<CellMap> getMaps() {
        return maps;
    }

    public static GameState getGameState() {
        return UserController.getLoggedUser().getState();
    }

    public static ArrayList<GameResult> getGameResults() {
        return gameResults;
    }


}
