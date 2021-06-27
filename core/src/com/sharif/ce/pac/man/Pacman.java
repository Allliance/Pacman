package com.sharif.ce.pac.man;

import com.badlogic.gdx.Game;
import com.sharif.ce.pac.man.controller.AssetController;
import com.sharif.ce.pac.man.controller.DataController;
import com.sharif.ce.pac.man.controller.GameController;
import com.sharif.ce.pac.man.view.*;


public class Pacman extends Game {

    private StartMenu startMenu;
    private SignUpMenu signUpMenu;
    private OptionMenu optionMenu;
    private MainMenu mainMenu;
    private ScoreboardMenu scoreboardMenu;
    private SelectMapMenu selectMapMenu;
    public static GameController gameController;

    @Override
    public void create() {
        DataController.initialize();
        AssetController.loadAssets();
        this.setScreen(getStartMenu());
    }

    @Override
    public void dispose() {
        super.dispose();
    }

    public void disposeMenus(){
        if (startMenu != null) {
            startMenu.dispose();
            startMenu = null;
        }
        if (optionMenu != null) {
            optionMenu.dispose();
            optionMenu = null;
        }
        if (signUpMenu != null) {
            signUpMenu.dispose();
            signUpMenu = null;
        }

        if (scoreboardMenu != null) {
            scoreboardMenu.dispose();
            scoreboardMenu = null;
        }
        if (selectMapMenu != null) {
            selectMapMenu.dispose();
            selectMapMenu = null;
        }
    }

    @Override
    public void pause() {
        DataController.saveData();
        super.pause();
    }

    public ScoreboardMenu getScoreboardMenu() {
        if (scoreboardMenu == null)
            scoreboardMenu = new ScoreboardMenu(this);
        return scoreboardMenu;
    }

    public SignUpMenu getSignUpMenu() {
        if (signUpMenu == null)
            signUpMenu = new SignUpMenu(this);
        return signUpMenu;
    }

    public OptionMenu getOptionMenu(){
        if (optionMenu == null)
            optionMenu = new OptionMenu(this);
        return optionMenu;
    }

    public StartMenu getStartMenu() {
        if (startMenu == null)
            startMenu = new StartMenu(this);
        return startMenu;
    }

    public MainMenu getMainMenu() {
        if (mainMenu == null)
            mainMenu = new MainMenu(this);
        return mainMenu;
    }

    public SelectMapMenu getSelectMapMenu() {
        if (selectMapMenu == null)
            selectMapMenu = new SelectMapMenu(this);
        return selectMapMenu;
    }

    public static GameController getGameController() {
        return gameController;
    }

    public static void setGameController(GameController gameController) {
        Pacman.gameController = gameController;
    }
}
