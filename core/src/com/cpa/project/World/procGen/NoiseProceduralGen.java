package com.cpa.project.World.procGen;

import com.badlogic.gdx.math.Vector2;
import com.cpa.project.Tiles.Tile;
import com.badlogic.gdx.math.MathUtils;
import com.cpa.project.Tiles.TileType;
import com.cpa.project.Tiles.terrainFloorTiles;
import com.cpa.project.Utils.PerlinNoiseGenerator;
import com.cpa.project.Utils.SimplexNoise;

public class NoiseProceduralGen {
    private final int scale; // Scale of the noise map , octave count
    private final int width;
    private final int height; // Width and height of the map in tiles
    private Tile[][] map; // The map array that will hold the final tiles

    public NoiseProceduralGen( int scale, int width, int height) {
        this.scale = scale;
        this.width = width;
        this.height = height;
        this.map = new Tile[width][height];
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public void generateMap() {
        // Generate Perlin noise map with appropriate octave count
        // The octave count can be adjusted to control detail level
        float[][] noiseMap = PerlinNoiseGenerator.generatePerlinNoise(width, height, scale);

        // Map the noise values to terrain tiles
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                map[x][y] = chooseTileBasedOnNoise(noiseMap[x][y]);

                // set the tile position
                map[x][y].setPosition( new Vector2(x * 48, y * 48));

//                double noiseValue = SimplexNoise.noise( scale * x / (double) width, scale * y / (double) height);
//                // Normalize the value to be between 0 and 1
//                double normalizedValue = (noiseValue + 1) / 2;
//                // Choose the tile based on the noise value
//                map[x][y] = chooseTileBasedOnNoise((float) normalizedValue);
            }
        }

        // Apply transitions for tiles
        applyTransitions();
    }



    private Tile chooseTileBasedOnNoise(float noiseValue) {
        if (noiseValue<0.2){
            return terrainFloorTiles.waterTiles[1][1].clone(44); //  center tile of water
        }
        else if (noiseValue < 0.3) {
            return terrainFloorTiles.rockCenterWSand[1][1].clone(24); //  center tile of rock
        } else if (noiseValue < 0.6) {
            return terrainFloorTiles.sandCenterWGrass[1][1].clone(14); //  center tile of sand
        } else if (noiseValue < 0.8) {
            return terrainFloorTiles.grassCenterWSand[1][1].clone(4); //  center tile of grass
        }else{
            // choose randomly between flowerTiles
            int random = MathUtils.random(0, terrainFloorTiles.flowerTiles.length - 1);
            return terrainFloorTiles.flowerTiles[random][0].clone(54); //  flower tile
//            return terrainFloorTiles.tree1Tile.clone(54);
        }
    }

    public void applyTransitions() {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                map[x][y] = getTransitionTile(x, y);
            }
        }
    }

    private Tile getTransitionTile(int x, int y) {
        Tile centerTile = map[x][y];

        // Get the main tile type for this position
        TileType centerType = getTileType(x, y);

        // Identify the types of neighboring tiles
        TileType topType = getTileType(x, y + 1);
        TileType rightType = getTileType(x + 1, y);
        TileType bottomType = getTileType(x, y - 1);
        TileType leftType = getTileType(x - 1, y);

        // Check corners
        TileType topLeftType = getTileType(x - 1, y + 1);
        TileType topRightType = getTileType(x + 1, y + 1);
        TileType bottomLeftType = getTileType(x - 1, y - 1);
        TileType bottomRightType = getTileType(x + 1, y - 1);

        return centerTile.getTransition(topType, rightType, bottomType, leftType, topLeftType, topRightType, bottomLeftType, bottomRightType);
    }
    private TileType getTileType(int x, int y) {
        // Check if the position is within bounds
        if (x < 0 || x >= width || y < 0 || y >= height) {
            return TileType.None;
        }
        return map[x][y].getTileType();
    }


    // Accessor for the map for rendering or other purposes
    public Tile[][] getMap() {
        return map;
    }

    public void dispose() {
        map = null;
    }


}

