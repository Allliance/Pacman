package com.sharif.ce.pac.man.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.sharif.ce.pac.man.Pacman;
import com.sharif.ce.pac.man.controller.AssetController;
import com.sharif.ce.pac.man.controller.DataController;
import com.sharif.ce.pac.man.controller.GameController;
import com.sharif.ce.pac.man.controller.UserController;
import com.sharif.ce.pac.man.model.CellMap;
import com.sharif.ce.pac.man.model.GameMode;
import com.sharif.ce.pac.man.model.MenuButton;
import com.sharif.ce.pac.man.model.TileMap;

import java.util.ArrayList;

public class SelectMapMenu extends Menu{


    public static int x = 0;
    private CellMap selectedMap;
    private TileMap previewTileMap;
    private GameMode selectedMode;
    private int startLifePoints;
    private int selectedMapIndex;
    private ArrayList<CellMap> maps;
    private Array<String> mapsNames;
    private Array<String> modesNames;

    private Label gameModeLabel;
    private SelectBox<String> modeSelectBox;
    private Label startLifePointsLabel;
    private Slider lifePointsSlider;
    private Label selectMapLabel;
    private SelectBox<String> mapSelectBox;
    private Label errorLabel;
    private MenuButton backButton;
    private MenuButton saveMapButton;
    private MenuButton generateMapButton;
    private MenuButton startGameButton;
    private Container<Table> tableContainer;
    private Table itemsTable;
    private Table previewTable;
    private Table optionsTable;

    public SelectMapMenu(Pacman game){
        super(game,false);
        maps = new ArrayList<>();
        modesNames = new Array<>();
        modesNames.add("Easy");
        modesNames.add("Hard");
        mapsNames = new Array<>();
        loadMaps();
        initializeWidgets();
        arrangeWidgets();
        addListeners();
    }

    private void initializeWidgets(){
        tableContainer = new Container<>();
        itemsTable = new Table();
        previewTable = new Table();
        optionsTable = new Table();
        gameModeLabel = new Label("Mode : ", AssetController.getDefaultSkin(),"button");
        modeSelectBox = new SelectBox<>(AssetController.getDefaultSkin());
        modeSelectBox.setItems(modesNames);
        lifePointsSlider = new Slider(2,5,1,false,AssetController.getDefaultSkin());
        startLifePoints = (int)lifePointsSlider.getValue();
        startLifePointsLabel = new Label("Life Points : " + startLifePoints,AssetController.getDefaultSkin(),"button");
        startLifePointsLabel.setAlignment(1);
        selectMapLabel = new Label("Map : ",AssetController.getDefaultSkin(),"button");
        mapSelectBox = new SelectBox<>(AssetController.getDefaultSkin());
        mapSelectBox.setItems(mapsNames);
        errorLabel = new Label("",AssetController.getDefaultSkin());
        errorLabel.setAlignment(2);
        generateMapButton = new MenuButton("New Map",AssetController.getButtonSkin());
        saveMapButton = new MenuButton("Save Map",AssetController.getButtonSkin());
        disableButton(saveMapButton);
        startGameButton = new MenuButton("Start",AssetController.getButtonSkin());
        backButton = new MenuButton("Back",AssetController.getButtonSkin());
        selectMapItem(0);
    }

    private void arrangeWidgets(){
        itemsTable.setFillParent(true);
        tableContainer.setFillParent(true);
        tableContainer.setActor(itemsTable);
        tableContainer.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        optionsTable.add(gameModeLabel).colspan(1);
        optionsTable.add(modeSelectBox).colspan(1);
        optionsTable.row();
        optionsTable.add(startLifePointsLabel).colspan(1).padRight(10);
        optionsTable.add(lifePointsSlider).colspan(1);
        optionsTable.row();
        optionsTable.add(selectMapLabel).colspan(1);
        optionsTable.add(mapSelectBox).colspan(1).width(modeSelectBox.getWidth()+10);
        optionsTable.row();
        optionsTable.add(errorLabel).colspan(2).center();
        optionsTable.row();
        optionsTable.add(startGameButton).height(45).fillX().colspan(2).padBottom(5);
        optionsTable.row();
        optionsTable.add(saveMapButton).height(45).fillX().colspan(1).padRight(5).padBottom(5);
        optionsTable.add(generateMapButton).height(45).fillX().colspan(1).padBottom(5);
        optionsTable.row();
        optionsTable.add(backButton).height(45).fillX().colspan(2);
        previewTable.add(previewTileMap.getContainer()).fill().expand().center();
        itemsTable.add(optionsTable).colspan(2).padRight(20).fill().center();
        itemsTable.add(previewTable).center().colspan(4).fill();
        itemsTable.row();
        tableContainer.setPosition(0,-100);
        stage.addActor(tableContainer);
        stage.addActor(musicButton);
    }

    private void addListeners(){
        modeSelectBox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (modeSelectBox.getSelected().equals("Easy"))
                    selectedMode = GameMode.EASY;
                else
                    selectedMode = GameMode.HARD;
            }
        });
        lifePointsSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                startLifePoints = (int)lifePointsSlider.getValue();
                startLifePointsLabel.setText("Life Points : " + startLifePoints);
            }
        });
        mapSelectBox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                selectMapItem(mapSelectBox.getSelectedIndex());
            }
        });
        addButtonsListeners();
    }

    private void addButtonsListeners(){
        generateMapButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                enableButton(saveMapButton);
                selectedMap = CellMap.generateRandomMap();
                updateMapPreview();
                super.clicked(event, x, y);
            }
        });
        saveMapButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                String mapName = "Custom Map #" + UserController.getLoggedUser().getCustomMaps().size();
                selectedMap.setName(mapName);
                addMapItem(selectedMap);
                selectMapItem(maps.size()-1);
                UserController.getLoggedUser().addCustomMap(selectedMap);
                loadMaps();
                super.clicked(event, x, y);
            }
        });
        startGameButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Pacman.setGameController(new GameController(game,selectedMap,selectedMode,startLifePoints));
                Pacman.getGameController().run();
                super.clicked(event, x, y);
            }
        });
        backButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(game.getMainMenu());
                super.clicked(event, x, y);
            }
        });
    }

    private void addMapItem(CellMap map){
        mapsNames.add(map.getName());
        maps.add(map);
    }

    private void selectMapItem(int index){
        disableButton(saveMapButton);
        selectedMap = maps.get(index);
        updateMapPreview();
    }

    private void loadMaps(){
        mapsNames.clear();
        maps.clear();
        if (DataController.getMaps() != null) {
            for (int i = 0; i < DataController.getMaps().size(); ++i)
                addMapItem(DataController.getMaps().get(i));
        }
        if (UserController.getLoggedUser().getCustomMaps() != null) {
            for (int i = 0; i < UserController.getLoggedUser().getCustomMaps().size(); ++i)
                addMapItem(UserController.getLoggedUser().getCustomMaps().get(i));
        }
    }

    private void resetToDefault(){
        selectedMode = GameMode.EASY;
        startLifePoints = 2;
        selectedMapIndex = 0;
        selectMapItem(0);
        selectedMap = maps.get(selectedMapIndex);
        errorLabel.setText("");
    }

    private void updateMapPreview(){
        if (previewTileMap!= null && previewTileMap.getContainer().getParent() != null)
            previewTileMap.getContainer().remove();
        selectedMap.clearMap();
        selectedMap.fillMap(1000000);
        previewTileMap = new TileMap(selectedMap,Gdx.graphics.getWidth()*4f/7f,
                Gdx.graphics.getHeight()*4f/7f);
        previewTable.add(previewTileMap.getContainer()).fill().expand().center();
    }

    private void disableButton(MenuButton button){
        button.setTouchable(Touchable.disabled);
        button.setChecked(true);
    }

    private void enableButton(MenuButton button){
        button.setTouchable(Touchable.enabled);
        button.setChecked(false);
    }

    @Override
    public void show() {
        resetToDefault();
        loadMaps();
        super.show();
    }
}
