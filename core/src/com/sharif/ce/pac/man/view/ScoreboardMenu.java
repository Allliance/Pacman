package com.sharif.ce.pac.man.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.sharif.ce.pac.man.Pacman;
import com.sharif.ce.pac.man.controller.AssetController;
import com.sharif.ce.pac.man.controller.DataController;
import com.sharif.ce.pac.man.controller.UserController;
import com.sharif.ce.pac.man.model.GameResult;
import com.sharif.ce.pac.man.model.MenuButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ScoreboardMenu extends Menu{

    private MenuButton backButton;
    private Container<Table> tableContainer;
    private Table scoreboard;
    private Label highScoreLabel;
    private Table itemsTable;
    private Label rankLabel;
    private Label userLabel;
    private Label scoreLabel;

    public ScoreboardMenu(Pacman game) {
        super(game);
        initializeWidgets();
        arrangeWidgets();
        addListeners();
    }

    private void initializeWidgets(){
        tableContainer = new Container<>();
        itemsTable = new Table();
        scoreboard = new Table();
        tableContainer.setActor(itemsTable);
        backButton = new MenuButton("Back",AssetController.getButtonSkin());
        rankLabel = new Label("#",AssetController.getScoreSkin(),"title-1");
        userLabel = new Label("user",AssetController.getScoreSkin(),"title-1");
        scoreLabel = new Label("score",AssetController.getScoreSkin(),"title-1");
        highScoreLabel = new Label("Your High Score : " + getUserHighScore(),AssetController.getDefaultSkin(),"title");
    }

    private int getUserHighScore(){
        int ans = 0;
        for(GameResult result:DataController.getGameResults()){
            if (result.getUser().getUsername().equals(UserController.getLoggedUser().getUsername())) ans = Math.max(ans,result.getScore());
        }
        return ans;
    }

    private void arrangeWidgets(){
        int windowWidth = Gdx.graphics.getWidth(),windowHeight = Gdx.graphics.getHeight();
        int showingResults = 5;
        tableContainer.setSize(windowWidth*0.5f,(showingResults+1)*40f+backButton.getHeight());
        tableContainer.setPosition(windowWidth/2-tableContainer.getWidth()*0.5f,(windowHeight-tableContainer.getHeight()*0.9f)/2);
        updateScores();
        itemsTable.setFillParent(true);
        itemsTable.add(highScoreLabel).expandX().fillX().center().colspan(7);
        itemsTable.row();
        itemsTable.add(rankLabel).expandX().fillX().center().left().colspan(1);
        itemsTable.add(userLabel).expandX().fillX().center().left().colspan(3);
        itemsTable.add(scoreLabel).expandX().fillX().center().left().colspan(3);
        itemsTable.row();
        itemsTable.add(new ScrollPane(scoreboard,AssetController.getScoreSkin())).fill().expandX().left().colspan(7).height(tableContainer.getHeight()-scoreLabel.getHeight()-5-backButton.getHeight());
        itemsTable.row();
        itemsTable.add(backButton).expandX().fillX().colspan(7);
        stage.addActor(tableContainer);
        stage.addActor(musicButton);
    }

    private void updateScores(){
        scoreboard.clear();
        ArrayList<GameResult> results = DataController.getGameResults();
        if (results == null || results.size() == 0)
            return;
        Collections.sort(results, new Comparator<GameResult>() {
            @Override
            public int compare(GameResult o1, GameResult o2) {
                if (o1.getScore() != o2.getScore())
                    return o2.getScore() - o1.getScore();
                return o1.getId() - o2.getId();
            }
        });
        int rank = 1;
        int last = results.get(0).getScore();
        for(int i = 0;i<Math.min(10,results.size());++i){
            GameResult result = results.get(i);
            if (result.getScore() < last){
                rank = i+1;
                last = result.getScore();
            }
            scoreboard.add(new Label("" + rank,AssetController.getScoreSkin(),"title-2")).fillX().expandX().colspan(1);
            scoreboard.add(new Label(result.getUser().getUsername(),AssetController.getScoreSkin(),"title-2")).fillX().expandX().colspan(3);
            scoreboard.add(new Label("" + result.getScore(),AssetController.getScoreSkin(),"title-2")).fillX().expandX().colspan(3);
            scoreboard.row();
        }
        highScoreLabel.setText("Your High Score : " + getUserHighScore());
    }

    private void addListeners(){
        backButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(game.getMainMenu());
            }
        });
    }

    @Override
    public void show(){
        updateScores();
        super.show();
    }

}
