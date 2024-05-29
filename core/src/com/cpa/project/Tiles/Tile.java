package com.cpa.project.Tiles;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.math.Vector2;

public class Tile extends StaticTiledMapTile {
    private int id ;
    private BlendMode blendMode = BlendMode.ALPHA; // on utilise le mode alpha par défaut
    private TextureRegion textureRegion ;
    private boolean isReacheable ;

    private Vector2 position;

    public Tile(int id , TextureRegion textureRegion) {
        super(textureRegion);
        this.id = id ;
        this.textureRegion = textureRegion ;
        this.isReacheable = true ;
    }

    public Tile(int id , TextureRegion textureRegion , boolean b ) {
        super(textureRegion);
        this.id = id ;
        this.textureRegion = textureRegion ;
        this.isReacheable = b;
    }

    public void setPosition(Vector2 position) {
        this.position = position;
    }

    public Vector2 getPosition() {
        return this.position;
    }


    public void setIsReachable(boolean value)
    {
        this.isReacheable = value;
    }

    public boolean isReachable()
    {
        return this.isReacheable;
    }

    @Override
    public int getId()
    {
        return this.id;
    }

    @Override
    public void setId(int id)
    {
        this.id = id;

    }

    @Override
    public BlendMode getBlendMode()
    {
        return this.blendMode;
    }

    @Override
    public void setBlendMode(BlendMode blendMode)
    {
        this.blendMode = blendMode;

    }

    @Override
    public TextureRegion getTextureRegion()
    {
        return this.textureRegion;
    }


    public void update(float dt , SpriteBatch batch ,  float x , float y ) {
        // draw the tile
        batch.draw(textureRegion, x, y);
    }

    public Tile clone(int id) {
        return new Tile(id, this.textureRegion, this.isReacheable);
    }

    public boolean isIn(int[] ids) {
        for (int i = 0; i < ids.length; i++) {
            if (this.id == ids[i]) {
                return true;
            }
        }
        return false;
    }

    public TileType getTileType() {
        //System.out.println("this.id : " + this.id);
        //System.out.println("List.of(terrainFloorTiles.SAND) : " );
        //System.out.println(Arrays.toString(terrainFloorTiles.SAND));
        if (isIn(terrainFloorTiles.SAND)) {
            return TileType.Sand;
        }
        if (isIn(terrainFloorTiles.GRASS)) {
            return TileType.Grass;
        }
        if (isIn(terrainFloorTiles.ROCK)) {
            return TileType.Rock;
        }
        if (isIn(terrainFloorTiles.WATER)) {
            return TileType.Water;
        }

        return TileType.None;
    }

    public Tile getTransition(TileType top, TileType right, TileType bottom, TileType left,
                              TileType topLeft, TileType topRight, TileType bottomLeft, TileType bottomRight) {
        TileType centerType = getTileType();
        if (centerType == TileType.Sand) {
            if (top == TileType.Grass && left == TileType.Grass) {
                return terrainFloorTiles.sandCenterWGrass[0][0].clone(14);
            }
            if (top == TileType.Grass && right == TileType.Grass) {
                return terrainFloorTiles.sandCenterWGrass[0][2].clone(14);
            }
            if (bottom == TileType.Grass && left == TileType.Grass) {
                return terrainFloorTiles.sandCenterWGrass[2][0].clone(14);
            }
            if (bottom == TileType.Grass && right == TileType.Grass) {
                return terrainFloorTiles.sandCenterWGrass[2][2].clone(14);
            }
            if (left == TileType.Grass && right == TileType.Grass){
                if (bottom == TileType.Sand && top == TileType.Sand){
                    return terrainFloorTiles.sandCenterWGrass[1][1].clone(14);
                }
                return terrainFloorTiles.sandCenterWGrass[1][1].clone(14);
            }

            if (top == TileType.Grass) {
                return terrainFloorTiles.sandCenterWGrass[0][1].clone(14);
            }
            if (right == TileType.Grass) {
                return terrainFloorTiles.sandCenterWGrass[1][2].clone(14);
            }
            if (bottom == TileType.Grass) {
                return terrainFloorTiles.sandCenterWGrass[2][1].clone(14);
            }
            if (left == TileType.Grass) {
                return terrainFloorTiles.sandCenterWGrass[1][0].clone(14);
            }

        }

        if (centerType == TileType.Rock) {
            if (top == TileType.Sand && left == TileType.Sand) {
                return terrainFloorTiles.rockCenterWSand[0][0].clone(24);
            }
            if (top == TileType.Sand && right == TileType.Sand) {
                return terrainFloorTiles.rockCenterWSand[0][2].clone(24);
            }
            if (bottom == TileType.Sand && left == TileType.Sand) {
                return terrainFloorTiles.rockCenterWSand[2][0].clone(24);
            }
            if (bottom == TileType.Sand && right == TileType.Sand) {
                return terrainFloorTiles.rockCenterWSand[2][2].clone(24);
            }
            if (top == TileType.Sand) {
                return terrainFloorTiles.rockCenterWSand[0][1].clone(24);
            }
            if (right == TileType.Sand) {
                return terrainFloorTiles.rockCenterWSand[1][2].clone(24);
            }
            if (bottom == TileType.Sand) {
                return terrainFloorTiles.rockCenterWSand[2][1].clone(24);
            }
            if (left == TileType.Sand) {
                return terrainFloorTiles.rockCenterWSand[1][0].clone(24);
            }
        }
        if (centerType == TileType.Water) {
            return terrainFloorTiles.waterTiles[1][1].clone(44);
        }

        return this;
    }

    public void dispose(){
        textureRegion.getTexture().dispose();
    }

}