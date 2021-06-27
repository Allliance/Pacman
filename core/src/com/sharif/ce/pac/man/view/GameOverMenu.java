package com.sharif.ce.pac.man.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.sharif.ce.pac.man.Pacman;
import com.sharif.ce.pac.man.controller.AssetController;
import com.sharif.ce.pac.man.model.MenuButton;

public class GameOverMenu extends Menu{

    private Container<Table> tableContainer;
    private Table itemsTable;
    private Label gameOverLabel;
    private Label scoreLabel;
    private MenuButton mainMenuButton;
    private MenuButton restartGameButton;
    private int score;

    public GameOverMenu(Pacman game,int score) {
        super(game, true);
        this.score = score;
        initializeWidgets();
        arrangeWidgets();
        addListeners();
    }

    private void initializeWidgets(){
        tableContainer = new Container<>();
        itemsTable = new Table();
        tableContainer.setActor(itemsTable);
        gameOverLabel = new Label("Game Over!", AssetController.getDefaultSkin(),"title");
        scoreLabel = new Label("Score : " + score,AssetController.getScoreSkin(),"title-2");
        mainMenuButton = new MenuButton("Go To Main Menu",AssetController.getButtonSkin());
        restartGameButton = new MenuButton("Restart",AssetController.getButtonSkin());
    }

    private void arrangeWidgets(){
        int windowWidth = Gdx.graphics.getWidth(),windowHeight = Gdx.graphics.getHeight();
        tableContainer.setSize(windowWidth*0.4f,windowHeight*0.8f);
        tableContainer.setPosition(windowWidth/2-tableContainer.getWidth()*0.5f,(windowHeight-tableContainer.getHeight())/2);
        itemsTable.setFillParent(true);
        itemsTable.add(gameOverLabel).expandX().colspan(2).padBottom(20);
        itemsTable.row();
        itemsTable.add(scoreLabel).expandX().colspan(2).padBottom(10).expandX();
        itemsTable.row();
        itemsTable.add(restartGameButton).height(45).fillX().colspan(1).padRight(5);
        itemsTable.add(mainMenuButton).height(45).fillX().colspan(1);
        stage.addActor(tableContainer);
        stage.addActor(musicButton);
    }

    private void addListeners(){
        mainMenuButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(game.getMainMenu());
            }
        });
        restartGameButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Pacman.getGameController().restartGame();
            }
        });
    }

}
