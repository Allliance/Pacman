package com.sharif.ce.pac.man.model;

import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.sharif.ce.pac.man.controller.AssetController;

public class TileMap {

    private final int rows = AssetController.getRowCellCount();
    private final int columns = AssetController.getColumnCellCount();
    private CellMap map;
    private float width;
    private float height;
    private Table contentTable;
    private Container<Table> container;
    private Tile[][] tiles;

    public TileMap(CellMap map,float width,float height){
        this.map = map;
        this.width = width;
        this.height = height;
        initializeWidgets();
    }

    private void initializeWidgets(){
        contentTable = new Table();
        container = new Container<>();
        container.setSize(width,height);
        container.setActor(contentTable);
        tiles = new Tile[rows+1][columns+1];
        for(int i = 1;i<=rows;++i)
            for(int j = 1;j<=columns;++j)
                tiles[i][j] = new Tile(map.getCell(i,j),i,j,this);
        for(int i = 1;i<=rows;++i){
            for(int j = 1;j<=columns;++j){
                contentTable.add(tiles[i][j]).width(width/columns).height(height/rows);
            }
            contentTable.row();
        }
    }

    public void setMap(CellMap map) {
        this.map = map;
    }

    public CellMap getMap() {
        return map;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public Container<Table> getContainer() {
        return container;
    }
}
