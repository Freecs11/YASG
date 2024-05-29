package com.cpa.project.Tiles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.cpa.project.Utils.AssetManager;

public class terrainFloorTiles {
    // we define the ids of the tiles in the terrain tileset
    public static final int[] GRASS = {0, 1, 2, 3, 4, 5, 6, 7, 8};
    public static final int[] SAND = { 10, 11, 12, 13, 14, 15, 16, 17, 18};
    public static final int[] ROCK = {20, 21, 22, 23, 24, 25, 26, 27, 28};
    public static final int[] VOID = {30, 31, 32, 33, 34, 35, 36, 37, 38};
    public static final int[] WATER = {40, 41, 42, 43, 44, 45, 46, 47, 48};

    // our terrain tileset is "terrain.png"
    public static Texture terrainTiles ;



    public static TextureRegion[][] terrainTilesSplit ;

    // we load the tree tile
    public static Texture treeTexture ;
    public static TextureRegion treeTile;

    public static Tile tree1Tile ;



    // -- these are from the terrain tiles
    // we define each box as a 3x3 matrix of tiles
    // we define the center tile of each box as the main tile
    // and the surrounding tiles as the surrounding tiles
    public static Tile[][] sandCenterWGrass ;
    public static Tile[][] grassCenterWSand ;
    public static Tile[][] rockCenterWSand ;
    public static Tile[][]  voidCenterWRock ;
    public static Tile[][]  waterTiles;
    public static Tile[][] flowerTiles;



    public static void init() {

        terrainTiles =  AssetManager.getTerrainTiles();
        terrainTilesSplit = TextureRegion.split(terrainTiles, 48, 48);
        treeTexture = AssetManager.getTreeTexture(); // pas le meme tileset
        int treeTileWidth = treeTexture.getWidth() / 8;
        int treeTileHeight = treeTexture.getHeight() / 2;
        treeTile = new TextureRegion(treeTexture, 0, 0, treeTileWidth, treeTileHeight);
        tree1Tile = new Tile(50, treeTile, false);
        sandCenterWGrass = new Tile[][] {
                {new Tile(SAND[0], terrainTilesSplit[0][0]), new Tile(SAND[1], terrainTilesSplit[0][1]), new Tile(SAND[2], terrainTilesSplit[0][2])},
                {new Tile(SAND[3], terrainTilesSplit[1][0]), new Tile(SAND[4], terrainTilesSplit[1][1]), new Tile(SAND[5], terrainTilesSplit[1][2])},
                {new Tile(SAND[6], terrainTilesSplit[2][0]), new Tile(SAND[7], terrainTilesSplit[2][1]), new Tile(SAND[8], terrainTilesSplit[2][2])}
        };

        grassCenterWSand = new Tile[][] {
                {new Tile(GRASS[0], terrainTilesSplit[0][3]), new Tile(GRASS[1], terrainTilesSplit[0][4]), new Tile(GRASS[2], terrainTilesSplit[0][5])},
                {new Tile(GRASS[3], terrainTilesSplit[1][3]), new Tile(GRASS[4], terrainTilesSplit[1][4]), new Tile(GRASS[5], terrainTilesSplit[1][5])},
                {new Tile(GRASS[6], terrainTilesSplit[2][3]), new Tile(GRASS[7], terrainTilesSplit[2][4]), new Tile(GRASS[8], terrainTilesSplit[2][5])}
        };

        rockCenterWSand = new Tile[][] {
                {new Tile(ROCK[0], terrainTilesSplit[0][7]), new Tile(ROCK[1], terrainTilesSplit[0][8]), new Tile(ROCK[2], terrainTilesSplit[0][9])},
                {new Tile(ROCK[3], terrainTilesSplit[1][7]), new Tile(ROCK[4], terrainTilesSplit[1][8]), new Tile(ROCK[5], terrainTilesSplit[1][9])},
                {new Tile(ROCK[6], terrainTilesSplit[2][7]), new Tile(ROCK[7], terrainTilesSplit[2][8]), new Tile(ROCK[8], terrainTilesSplit[2][9])}
        };

        voidCenterWRock = new Tile[][] {
                {new Tile(VOID[0], terrainTilesSplit[0][9]), new Tile(VOID[1], terrainTilesSplit[0][10]), new Tile(VOID[2], terrainTilesSplit[0][11])},
                {new Tile(VOID[3], terrainTilesSplit[1][9] ), new Tile(VOID[4], terrainTilesSplit[1][10]), new Tile(VOID[5], terrainTilesSplit[1][11])},
                {new Tile(VOID[6], terrainTilesSplit[2][9]), new Tile(VOID[7], terrainTilesSplit[2][10]), new Tile(VOID[8], terrainTilesSplit[2][11])}
        };

        waterTiles = new Tile[][] {
                {new Tile(WATER[0], terrainTilesSplit[11][3] , false), new Tile(WATER[1], terrainTilesSplit[11][4],false), new Tile(WATER[2], terrainTilesSplit[11][5],false)},
                {new Tile(WATER[3], terrainTilesSplit[11][6], false), new Tile(WATER[4], terrainTilesSplit[11][7],false), new Tile(WATER[5], terrainTilesSplit[11][8],false)},
                {new Tile(WATER[6], terrainTilesSplit[11][9], false), new Tile(WATER[7], terrainTilesSplit[11][10],false), new Tile(WATER[8], terrainTilesSplit[11][11],false)}
        };

        flowerTiles = new Tile[][] {
                {new Tile(50, terrainTilesSplit[3][0]), new Tile(50, terrainTilesSplit[3][1]), new Tile(50, terrainTilesSplit[3][2])},
                {new Tile(50, terrainTilesSplit[4][0]), new Tile(50, terrainTilesSplit[4][1]), new Tile(50, terrainTilesSplit[4][2])}
        };


    }

    public  Tile[][] getTileBox(int id) {
        switch (id) {
            case 0, 1, 2, 3, 4, 5, 6, 7, 8:
                return sandCenterWGrass;
            case 10, 11, 12, 13, 14, 15, 16, 17, 18:
                return grassCenterWSand;
            case 20, 21, 22, 23, 24, 25, 26, 27, 28:
                return rockCenterWSand;
            case 30, 31, 32, 33, 34, 35, 36, 37, 38:
                return voidCenterWRock;
            case 40, 41, 42, 43, 44, 45, 46, 47, 48:
                return waterTiles;
                default:
                return null;
        }
    }
    public terrainFloorTiles() {
        init();
    }




}
