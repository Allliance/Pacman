package com.sharif.ce.pac.man.model;

public class Cell {
    private boolean downWall;
    private boolean leftWall;
    private boolean upWall;
    private boolean rightWall;

    public Cell(){
        downWall = true;
        upWall = true;
        leftWall = true;
        rightWall = true;
    }

    public boolean isDownWall(){
        return downWall;
    }

    public boolean isLeftWall() {
        return leftWall;
    }

    public boolean isRightWall() {
        return rightWall;
    }

    public boolean isUpWall() {
        return upWall;
    }

    public void takeRightWall(){
        rightWall = false;
    }

    public void takeUpWall(){
        upWall = false;
    }

    public void takeDownWall(){
        downWall = false;
    }

    public void takeLeftWall(){
        leftWall = false;
    }

    public void putUpWall(){
        upWall = true;
    }

    public void putRightWall(){
        rightWall = true;
    }

    public void putDownWall(){
        downWall = true;
    }

    public void putLeftWall(){
        leftWall = true;
    }

    public static Cell clone(Cell initialCell){
        Cell cell = new Cell();
        if (!initialCell.isDownWall())
            cell.takeDownWall();
        if (!initialCell.isUpWall())
            cell.takeUpWall();
        if (!initialCell.isLeftWall())
            cell.takeLeftWall();
        if (!initialCell.isRightWall())
            cell.takeRightWall();
        return cell;
    }

}
