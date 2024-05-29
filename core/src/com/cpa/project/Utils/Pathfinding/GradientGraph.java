package com.cpa.project.Utils.Pathfinding;

import com.badlogic.gdx.math.Vector2;
import com.cpa.project.State.PlayState;
import com.cpa.project.Tiles.Tile;

import java.util.*;

public class GradientGraph {

    private Location[][] graph;
    private final Tile[][] tiles = PlayState.map.getTiles();
    private Queue<Location> frontier;
    private final int[][] directionsInt = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}
            , {1, 1}, {1, -1}, {-1, 1}, {-1, -1}
    };

    public GradientGraph() {
        this.graph = new Location[PlayState.map.getWidth()][PlayState.map.getHeight()];
//        for (int i = 0; i < PlayState.map.getWidth(); i++) {
//            for (int j = 0; j < PlayState.map.getHeight(); j++) {
//                if (tiles[i][j].isReachable()) {
//                    graph[i][j] = new Location(i, j, -1, new Vector2(0, 0));
//                } else {
//                    graph[i][j] = new Location(i, j, Integer.MAX_VALUE, new Vector2(0, 0));
//                }
//            }
//        }
    }

    // Source: https://www.redblobgames.com/pathfinding/a-star/implementation.html
    // Source: https://howtorts.github.io/2014/01/04/basic-flow-fields.html
    public void compute() {
        for (int i = 0; i < PlayState.map.getWidth(); i++) {
            for (int j = 0; j < PlayState.map.getHeight(); j++) {
//                if (tiles[i][j].isReachable()) {
//                    graph[i][j].setCost(-1);
//                } else {
//                    graph[i][j].setCost(Integer.MAX_VALUE);
//                }
                graph[i][j] = null;
            }
        }
        int numTiles = 0;
        Vector2 playerTileXY = PlayState.map.getEntityTileXY(PlayState.player);
        int tileX = (int) playerTileXY.x;
        int tileY = (int) playerTileXY.y;
        // Frontier queue holds the tiles that we need to explore
        frontier = new LinkedList<>();
        this.graph[tileX][tileY] = new Location(tileX, tileY, 0, new Vector2(0, 0));
        this.graph[tileX][tileY].setCost(0);
        frontier.add(this.graph[tileX][tileY]);
        while(!frontier.isEmpty() && numTiles < 60 * 60) {
            Location current = frontier.poll();
//            for (Location next : getNeighbors(current)) {
//                if (next.getCost() == -1){
//                    next.setCost(current.getCost() + 1);
//                    frontier.add(next);
//                }
//            }
            for (int[] dir : directionsInt) {
                int newX = current.getX() + dir[0];
                int newY = current.getY() + dir[1];
                if (newX >= 0 && newX < PlayState.map.getWidth() && newY >= 0 && newY < PlayState.map.getHeight()) {
                    if (graph[newX][newY] == null) {
                        if (tiles[newX][newY].isReachable()) {
                            graph[newX][newY] = new Location(newX, newY, current.getCost() + 1, new Vector2(0, 0));
                            frontier.add(graph[newX][newY]);
                        } else {
                            graph[newX][newY] = new Location(newX, newY, Integer.MAX_VALUE, new Vector2(0, 0));
                        }
                    }
                }
            }
            numTiles++;
        }

        // Set the direction of the tiles
        for (int i = 0; i < graph.length; i++) {
            for (int j = 0; j < graph[i].length; j++) {
                if (graph[i][j] == null) {
                    continue;
                }
                if (graph[i][j].getCost() == Integer.MAX_VALUE){
                    continue;
                }
                int minCost = Integer.MAX_VALUE;
                Location minLocation = null;
                Vector2 pos = new Vector2(i, j);
//                for (Location neighbor : getNeighbors(graph[i][j])) {
//                    if (neighbor.getCost() < minCost) {
//                        minCost = neighbor.getCost();
//                        minLocation = neighbor;
//                    }
//                }
                for (int[] dir : directionsInt) {
                    int newX = i + dir[0];
                    int newY = j + dir[1];
                    if (newX >= 0 && newX < PlayState.map.getWidth() && newY >= 0 && newY < PlayState.map.getHeight()) {
                        if (graph[newX][newY] != null && graph[newX][newY].getCost() < minCost && graph[newX][newY].getCost() != Integer.MAX_VALUE) {
                            minCost = graph[newX][newY].getCost();
                            minLocation = graph[newX][newY];
                        }
                    }
                }

                if (minLocation != null) {
                    Vector2 direction = new Vector2(minLocation.getX() - pos.x, minLocation.getY() - pos.y).nor();
                    graph[i][j].setDirection(direction);
                }
            }
        }

        // Smoothing the directional vectors by averaging the directions of the neighbors
        for (int i = 0; i < graph.length; i++) {
            for (int j = 0; j < graph[i].length; j++) {
                if (graph[i][j] == null) {
                    continue;
                }
                if (graph[i][j].getCost() == Integer.MAX_VALUE){
                    continue;
                }
                smoothVector(i, j, 5);
            }
        }


        frontier = null;
    }

    public Vector2 getDirection(int x, int y) {
        return (x <= graph.length && y <= graph[0].length && graph[x][y] != null)
                ? graph[x][y].getDirection()
                : new Vector2(0, 0);
    }

    public List<Location> getNeighbors(Location tile) {
        List<Location> neighbors = new ArrayList<>();
        int x = tile.getX();
        int y = tile.getY();
        for (int[] dir : directionsInt) {
            int newX = x + dir[0];
            int newY = y + dir[1];
            if (newX >= 0 && newX < PlayState.map.getWidth() && newY >= 0 && newY < PlayState.map.getHeight()) {
                neighbors.add(graph[newX][newY]);
            }
        }
        return neighbors;
    }

    public Location[][] getGraph() {
        return this.graph;
    }

    public void smoothVector(int i, int j, int neighborSize){
        if (neighborSize == 0) {
            return;
        }
        int startX = Math.max(0, i - neighborSize);
        int endX = Math.min(PlayState.map.getWidth(), i + neighborSize);
        int startY = Math.max(0, j - neighborSize);
        int endY = Math.min(PlayState.map.getHeight(), j + neighborSize);
        Vector2 sum = new Vector2(0, 0);
        int count = 0;
        for (int x = startX; x < endX; x++) {
            for (int y = startY; y < endY; y++) {
                if (graph[x][y] == null) {
                    continue;
                }
                if (graph[x][y].getCost() == Integer.MAX_VALUE){
                    continue;
                }
                sum.add(graph[x][y].getDirection());
                count++;
            }
        }
        if (count > 0) {
            sum.scl(1f / count);
            graph[i][j].setDirection(sum);
        }
    }
}