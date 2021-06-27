package com.sharif.ce.pac.man.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Timer;
import com.sharif.ce.pac.man.Pacman;
import com.sharif.ce.pac.man.controller.AssetController;
import com.sharif.ce.pac.man.controller.DataController;
import com.sharif.ce.pac.man.controller.GameController;
import com.sharif.ce.pac.man.controller.UserController;
import com.sharif.ce.pac.man.model.MenuButton;

public class MainMenu extends Menu {

    private Label welcomeLabel;
    private MenuButton resumeGameButton;
    private MenuButton newGameButton;
    private MenuButton scoreboardButton;
    private MenuButton optionsButton;
    private MenuButton logoutButton;
    private MenuButton quitButton;
    private Container<Table> tableContainer;
    private Table itemsTable;

    public MainMenu(Pacman game) {
        super(game);
        initializeWidgets();
        arrangeWidgets();
        addListeners();
    }

    private void initializeWidgets() {
        Skin buttonSkin = AssetController.getButtonSkin();
        welcomeLabel = new Label("", AssetController.getDefaultSkin(), "title");
        resumeGameButton = new MenuButton("Resume", buttonSkin);
        newGameButton = new MenuButton("New Game", buttonSkin);
        scoreboardButton = new MenuButton("Scoreboard", buttonSkin);
        optionsButton = new MenuButton("Options", buttonSkin);
        logoutButton = new MenuButton("Logout", buttonSkin);
        quitButton = new MenuButton("Quit", buttonSkin);
        tableContainer = new Container<>();
        itemsTable = new Table();
        tableContainer.setActor(itemsTable);
    }

    private void arrangeWidgets() {
        int windowWidth = Gdx.graphics.getWidth(), windowHeight = Gdx.graphics.getHeight();
        tableContainer.setSize(windowWidth / 2, windowHeight * 0.7f);
        tableContainer.setPosition(windowWidth / 2 - tableContainer.getWidth() / 2, windowHeight / 2 - tableContainer.getHeight() / 2);
        itemsTable.add(welcomeLabel).expandX().padBottom(10);
        itemsTable.row();
        Timer.Task task = new Timer.Task() {
            @Override
            public void run() {

            }
        };
        newGameButton.setClip(true);
        addButton(resumeGameButton);
        addButton(newGameButton);
        addButton(scoreboardButton);
        addButton(optionsButton);
        addButton(logoutButton);
        addButton(quitButton);
        stage.addActor(tableContainer);
        stage.addActor(musicButton);
    }

    private void addButton(MenuButton button) {
        itemsTable.add(button).width(250).height(50).padBottom(5);
        itemsTable.row();
    }

    private void disableButton(MenuButton button) {
        button.setVisible(false);
        button.setTouchable(Touchable.disabled);
    }

    private void enableButton(MenuButton button) {
        button.setVisible(true);
        button.setTouchable(Touchable.enabled);
    }

    @Override
    public void show() {
        game.disposeMenus();
        if (DataController.getGameState().isGameRunning())
            enableButton(resumeGameButton);
        else
            disableButton(resumeGameButton);
        if (UserController.getLoggedUser().isGuest())
            optionsButton.setTouchable(Touchable.disabled);
        else
            optionsButton.setTouchable(Touchable.enabled);
        welcomeLabel.setText("Welcome , " + UserController.getLoggedUser().getUsername());
        super.show();
    }

    private void addListeners() {
        resumeGameButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Pacman.setGameController(new GameController(game));
                Pacman.getGameController().run();
                super.clicked(event, x, y);
            }
        });
        newGameButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (DataController.getGameState().isGameRunning()) {
                    new DialogBox(stage).confirmationDialog("There is an unfinished game. Do you want to end it?", new ClickListener() {
                        @Override
                        public void clicked(InputEvent event, float x, float y) {
                            game.setScreen(game.getSelectMapMenu());
                        }
                    });
                } else {
                    game.setScreen(game.getSelectMapMenu());
                }
                super.clicked(event, x, y);
            }
        });
        scoreboardButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(game.getScoreboardMenu());
            }
        });
        optionsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(game.getOptionMenu());
            }
        });
        logoutButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                new DialogBox(stage).confirmationDialog("Are you sure , you want to logout?", new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        UserController.logout();
                        game.setScreen(game.getStartMenu());
                        super.clicked(event, x, y);
                    }
                });
                super.clicked(event, x, y);
            }
        });
        quitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                new DialogBox(stage).askToQuit();
                super.clicked(event, x, y);
            }
        });
        stage.addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (keycode == Input.Keys.ESCAPE)
                    new DialogBox(stage).askToQuit();
                return true;
            }
        });
    }

}
