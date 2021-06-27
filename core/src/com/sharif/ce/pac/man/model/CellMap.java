package com.sharif.ce.pac.man.model;

import com.badlogic.gdx.math.MathUtils;
import com.sharif.ce.pac.man.controller.AssetController;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class CellMap {
    private String name;
    private Cell[][] cells;
    private boolean[][] isEmpty;
    private boolean[][] isBanned;
    private ArrayList<Pair<Integer,Integer>> bombCells;
    private static final int rows = AssetController.getRowCellCount();
    private static final int columns = AssetController.getColumnCellCount();

    public CellMap() {
        name = "default";
        bombCells = new ArrayList<>();
        isEmpty = new boolean[rows + 1][columns + 1];
        isBanned = new boolean[rows + 1][columns + 1];
        cells = new Cell[rows + 1][columns + 1];
        for (int i = 1; i <= rows; ++i) {
            for (int j = 1; j <= columns; ++j) {
                cells[i][j] = new Cell();
            }
        }
    }

    public Cell getCell(int r, int c) {
        Cell cell = cells[r][c];
        return cell;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addBomb(int row, int column){
        bombCells.add(new Pair<>(row,column));
    }

    public void removeBomb(int row,int column){
        for(Pair<Integer,Integer> bombCell : bombCells){
            int bombRow = bombCell.getKey();
            int bombColumn = bombCell.getValue();
            if (bombRow == row && bombColumn == column){
                bombCells.remove(bombCell);
                return;
            }
        }
    }

    public void banCell(int row, int columns) {
        isBanned[row][columns] = true;
    }

    public void unBanCell(int row, int columns) {
        isBanned[row][columns] = false;
    }

    public boolean isCellBanned(int row, int column) {
        return isBanned[row][column];
    }

    public void emptyCell(int row, int column) {
        isEmpty[row][column] = true;
    }

    public boolean isCellEmpty(int row, int column) {
        return isEmpty[row][column];
    }

    public boolean hasBomb(int row,int column){
        for(Pair<Integer,Integer> bombCell:bombCells){
            if (bombCell.getKey() == row && bombCell.getValue() == column)
                return true;
        }
        return false;
    }

    public Pair<Integer, Integer> getRandomUnbannedCell() {
        ArrayList<Pair<Integer, Integer>> availableCells = new ArrayList<>();
        for (int i = 1; i <= rows; ++i) {
            for (int j = 1; j <= columns; ++j) {
                if (!isCellBanned(i, j))
                    availableCells.add(new Pair<>(i, j));
            }
        }
        return availableCells.get(MathUtils.random(0, availableCells.size() - 1));
    }

    public int getDistance(int r1, int c1, int r2, int c2) {
        if (r2 > rows || r2 < 1 || c2 < 1 || c2 > columns)
            return 1000;
        if (isBanned[r2][c2])
            return 1000;
        ArrayList<Pair<Integer,Integer>> startCell = new ArrayList<>();
        startCell.add(new Pair<>(r1,c1));
        return bfs(startCell)[r2][c2];
    }

    private int[][] bfs(ArrayList<Pair<Integer,Integer>> startCells) {
        int[][] dis = new int[rows + 1][columns + 1];
        for (int i = 1; i <= rows; ++i)
            for (int j = 1; j <= columns; ++j)
                dis[i][j] = 1000;
        ArrayList<Pair<Integer, Integer>> workingCells = new ArrayList<>();
        for(Pair<Integer,Integer> startCell : startCells) {
            dis[startCell.getKey()][startCell.getValue()] = 0;
            workingCells.add(startCell);
        }
        for (int i = 0; i < workingCells.size(); ++i) {
            int row = workingCells.get(i).getKey();
            int column = workingCells.get(i).getValue();
            if (isBanned[row][column]) continue;
            int newDistance = dis[row][column] + 1;
            for (int change = -1; change <= 1; change += 2) {
                int newRow = row + change;
                int newColumn = column;
                if (newRow > 0 && newRow <= rows && newColumn > 0 && newColumn <= columns) {
                    if (dis[newRow][newColumn] > newDistance) {
                        workingCells.add(new Pair<>(newRow, newColumn));
                        dis[newRow][newColumn] = newDistance;
                    }
                }
                newRow = row;
                newColumn = column + change;
                if (newRow > 0 && newRow <= rows && newColumn > 0 && newColumn <= columns) {
                    if (dis[newRow][newColumn] > newDistance) {
                        dis[newRow][newColumn] = newDistance;
                        workingCells.add(new Pair<>(newRow, newColumn));
                    }
                }
            }
        }
        return dis;
    }

    public static CellMap generateRandomMap() {
        CellMap map = new CellMap();
        ArrayList<Pair<Integer, Integer>> availableCells = new ArrayList<>();
        for (int i = 2; i < rows-1; ++i)
            for (int j = 2; j < columns-1; ++j)
                availableCells.add(new Pair<>(i, j));
        Random rnd = new Random();
        rnd.setSeed(System.currentTimeMillis());
        Collections.shuffle(availableCells, rnd);
        for (int i = 1; i <= rows*columns/10; ++i) {
            int row = availableCells.get(i).getKey();
            int column = availableCells.get(i).getValue();
            for(int rowChange = 0;rowChange<=1;rowChange++){
                for(int columnChange = 0;columnChange<=1;columnChange++){
                    map.emptyCell(row + rowChange, column + columnChange);
                    map.banCell(row + rowChange, column + columnChange);
                }
            }
        }
        removeInnerWalls(map);
        for(int i = 1;i<=rows*columns;++i)
            banUnreachableCells(map);
        for (int i = 1; i <= rows; ++i) {
            for (int j = 1; j <= columns; ++j) {
                putSurroundingWall(map, i, j);
            }
        }
        return map;
    }

    public void clearMap(){
        for(int i = 1;i<=rows;++i)
            for(int j = 1;j<=columns;++j)
                emptyCell(i,j);
        if (bombCells == null)
            System.exit(10);
        bombCells.clear();
    }

    public void putBombInRandomCells(int maximumDistance){
        for(int i = 1;i<=rows;++i)
            for(int j = 1;j<=columns;++j)
                removeBomb(i,j);
        while(true) {
            ArrayList<Pair<Integer, Integer>> candidateCells = new ArrayList<>();
            int dis[][] = bfs(bombCells);
            for (int i = 1; i <= rows; ++i) {
                for (int j = 1; j <= columns; ++j) {
                    if (i == 1 || j == 1 || i == rows || j == columns)
                        continue;
                    if (!isEmpty[i][j] && dis[i][j] > maximumDistance)
                        candidateCells.add(new Pair<>(i, j));
                }
            }
            if (candidateCells.size() == 0)
                break;
            Random rnd = new Random();
            rnd.setSeed(System.currentTimeMillis());
            Collections.shuffle(candidateCells, rnd);
            bombCells.add(candidateCells.get(0));
            emptyCell(candidateCells.get(0).getKey(), candidateCells.get(0).getValue());
            addBomb(candidateCells.get(0).getKey(), candidateCells.get(0).getValue());
        }
    }

    public static CellMap clone(CellMap initialMap){
        CellMap map = new CellMap();
        for(int i = 1;i<=rows;++i)
            for(int j = 1;j<=columns;++j){
                map.isEmpty[i][j] = initialMap.isEmpty[i][j];
                map.isBanned[i][j] = initialMap.isBanned[i][j];
                map.cells[i][j] = Cell.clone(initialMap.cells[i][j]);
            }
        map.bombCells = new ArrayList<>();
        for(Pair<Integer,Integer> bombCell: initialMap.bombCells)
            map.bombCells.add(new Pair<>(bombCell.getKey(),bombCell.getValue()));
        return map;
    }

    private static void removeInnerWalls(CellMap map) {
        for (int i = 1; i <= rows; ++i) {
            for (int j = 1; j <= columns; ++j) {
                if (i > 1)
                    map.getCell(i, j).takeUpWall();
                if (i < rows)
                    map.getCell(i, j).takeDownWall();
                if (j > 1)
                    map.getCell(i, j).takeLeftWall();
                if (j < columns)
                    map.getCell(i, j).takeRightWall();
            }
        }
    }

    private static void banUnreachableCells(CellMap map){
        for(int i = 1;i<=rows;++i){
            for(int j = 1;j<=columns;++j){
                if (map.getDistance(1, 1, i, j) > 100)
                    map.banCell(i, j);
            }
        }
    }

    private static void putSurroundingWall(CellMap map, int i, int j) {
        if (!map.isCellBanned(i, j))
            return;
        if (i < rows && !map.isCellBanned(i + 1, j)) {
            map.getCell(i, j).putDownWall();
            map.getCell(i + 1, j).putUpWall();
        }
        if (i > 1 && !map.isCellBanned(i - 1, j)) {
            map.getCell(i, j).putUpWall();
            map.getCell(i - 1, j).putDownWall();
        }
        if (j > 1 && !map.isCellBanned(i, j - 1)) {
            map.getCell(i, j).putLeftWall();
            map.getCell(i, j - 1).putRightWall();
        }
        if (j < columns && !map.isCellBanned(i, j + 1)) {
            map.getCell(i, j).putRightWall();
            map.getCell(i, j + 1).putLeftWall();
        }
    }

    public boolean isAnyCellFull(){
        for(int i = 1;i<=rows;++i){
            for(int j = 1;j<=columns;++j){
                if (!isEmpty[i][j])
                    return true;
            }
        }
        return false;
    }

    public void fillMap(int maximumBombDistance){
        for(int i = 1;i<=rows;++i)
            for(int j = 1;j<=columns;++j)
                if (!isCellBanned(i,j))
                    isEmpty[i][j] = false;
        putBombInRandomCells(maximumBombDistance);
    }

}
