package com.cpa.project.World;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.cpa.project.Entities.Entity;
import com.cpa.project.State.PlayState;
import com.cpa.project.Tiles.Tile;
import com.cpa.project.Tiles.terrainFloorTiles;
import com.cpa.project.World.procGen.NoiseProceduralGen;

public class GameMap {
    private final static int tileSize = 48; // Size of a tile in pixels
    private final static int chunkSize = 64; // Size of a chunk in tiles

    private TiledMap tiledMap;
    private OrthographicCamera camera;
    private SpriteBatch batch;


    private terrainFloorTiles terrainFloorTiles;
    private OrthogonalTiledMapRenderer renderer;

    BitmapFont font;

    NoiseProceduralGen noiseProceduralGen;
    Tile[][] outputNoiseMap;

    public GameMap(OrthographicCamera camera, Vector2 playerPos) {
        this.camera = camera;
        this.terrainFloorTiles = new terrainFloorTiles();
        this.tiledMap = new TiledMap();
        this.renderer = new OrthogonalTiledMapRenderer(tiledMap);
        this.font = new BitmapFont();
        this.noiseProceduralGen = new NoiseProceduralGen(5, 100, 100);

    }

    public void init() {
        this.noiseProceduralGen.generateMap();
        this.outputNoiseMap = noiseProceduralGen.getMap();
    }

//    // take the output of the WFC algorithm and add it to the tiled map for rendering
//    // it's a terrain so add it to the terrain layer
//    public void addWFCtoTiledMap() {
//        TiledMapTileLayer terrainLayer = new TiledMapTileLayer(wfc.getWidth(), wfc.getHeight(), tileSize, tileSize);
//        // declare a new layer for the objects (e.g. trees, rocks, etc.) and add it to the tiled map
//        TiledMapTileLayer backgroudLayer = new TiledMapTileLayer(wfc.getWidth(), wfc.getHeight(), tileSize, tileSize);
//
//
//        for (int i = 0; i < WFCoutput.size(); i++) {
//            Tile tile = WFCoutput.get(i);
//            terrainLayer.setCell((int) tile.getPosition().x / tileSize, (int) tile.getPosition().y / tileSize, new TiledMapTileLayer.Cell().setTile(tile));
//        }
//        tiledMap.getLayers().add(terrainLayer);
//        tiledMap.getLayers().add(backgroudLayer);
//    }


    public void addNoiseMapToTiledMap(Vector2 playerposition) {
        TiledMapTileLayer terrainLayer = new TiledMapTileLayer(noiseProceduralGen.getWidth(), noiseProceduralGen.getHeight(), tileSize, tileSize);
        TiledMapTileLayer bgLayer = new TiledMapTileLayer(noiseProceduralGen.getWidth(), noiseProceduralGen.getHeight(), tileSize, tileSize);

        int centerX = outputNoiseMap.length / 2;
        int centerY = outputNoiseMap[0].length / 2;

        for (int i = 0; i < outputNoiseMap.length; i++) {
            for (int j = 0; j < outputNoiseMap[i].length; j++) {
                Tile tile = outputNoiseMap[i][j];

                // Calculate the position based on the center of the map
                int posX = (i - centerX) * tileSize;
                int posY = (j - centerY) * tileSize;

                // Offset position by the player's start position
                tile.setPosition(new Vector2(playerposition.x + posX, playerposition.y + posY));
                terrainLayer.setCell(i, j, new TiledMapTileLayer.Cell().setTile(tile));
            }
        }
        tiledMap.getLayers().add(bgLayer);
        tiledMap.getLayers().add(terrainLayer);
    }

    public Vector2 getWorldCenter() {
        Vector2 spawnLocation =  new Vector2(
                (float) (this.getWidth() * tileSize) / 2,
                (float) (this.getHeight() * tileSize) / 2
        );
        while (!getTileAt(spawnLocation, 0).isReachable()) {
            spawnLocation = new Vector2(
                    (float) (this.getWidth() * tileSize) / 2 + (float) Math.random() * 100,
                    (float) (this.getHeight() * tileSize) / 2 + (float) Math.random() * 100
            );
        }
        return spawnLocation;
    }


    public void render() {
        // Render the WFC output tiles
        renderer.setView(camera);
        renderer.render();

        // access the terrain layer
//        TiledMapTileLayer terrainLayer = (TiledMapTileLayer) tiledMap.getLayers().get(1);

        // access the tile at the player's position
//        TiledMapTileLayer.Cell cell = terrainLayer.getCell((int) playerPos.x / tileSize, (int) playerPos.y / tileSize);
//        Tile tile = (Tile) cell.getTile();
//        System.out.println("Tile at player position: " + tile.isReachable());

    }


    public Tile getTileAt(Vector2 position, int playerHe) {
        TiledMapTileLayer terrainLayer = (TiledMapTileLayer) tiledMap.getLayers().get(1);

        // Adjust the Y position based on the height of the player sprite
        // we do this because we had an off by one error in the tile position retrieval
        int tileX = (int) position.x / tileSize;
        int tileY = ((int) position.y - playerHe) / tileSize;

        TiledMapTileLayer.Cell cell = terrainLayer.getCell(tileX, tileY);
        if (cell == null) {
            return null;
        }
        return (Tile) cell.getTile();
    }

    public Vector2 getTilePosition(Vector2 position, int playerHe) {
        int tileX = (int) position.x / tileSize;
        int tileY = ((int) position.y - playerHe) / tileSize;

        return new Vector2(tileX, tileY);
    }

    public void update(float dt, TiledMapRenderer tiledMapRenderer) {
        this.camera = camera;
        this.batch = batch;
//        this.render();
    }

    public int getWidth() {
        return this.noiseProceduralGen.getWidth();
    }

    public int getHeight() {
        return this.noiseProceduralGen.getHeight();
    }

    public Tile[][] getTiles() {
        return this.outputNoiseMap;
    }

    public Vector2 getEntityTileXY(Entity entity) {
        int entityHeight = entity.getSprite().getRegionHeight();
        Vector2 position = entity.getPosition();
        int vecX = (int) position.x / tileSize;
        int vecY = (int) (position.y - entityHeight) / tileSize;
        return new Vector2(vecX, vecY);
    }

    public void dispose() {
        tiledMap.dispose();
        renderer.dispose();
        noiseProceduralGen.dispose();
    }
}


