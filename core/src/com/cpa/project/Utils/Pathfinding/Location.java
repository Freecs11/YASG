package com.cpa.project.Utils.Pathfinding;

import com.badlogic.gdx.math.Vector2;

public class Location {
    private int cost;
    private int x;
    private int y;
    private Vector2 direction;

    public Location(int x, int y, int cost){
        this.x = x;
        this.y = y;
        this.cost = cost;
        this.direction = new Vector2(0,0);
    }

    public Location(int x, int y, int cost, Vector2 direction){
        this.x = x;
        this.y = y;
        this.cost = cost;
        this.direction = direction;
    }

    public int getCost(){
        return cost;
    }

    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }

    public Vector2 getDirection(){
        return direction;
    }

    public void setDirection(Vector2 direction){
        this.direction = direction;
    }

    public void setCost(int cost){
        this.cost = cost;
    }
}