package com.cpa.project.State;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.cpa.project.Camera.TopDownCamera;
import com.cpa.project.Entities.Actors.Mobs.FlyingBat;
import com.cpa.project.Entities.Actors.Mobs.Skeleton;
import com.cpa.project.Entities.Actors.Player;
import com.cpa.project.Entities.Entity;
import com.cpa.project.Entities.Spells.SonicWave;
import com.cpa.project.Tiles.Tile;
import com.cpa.project.Utils.AssetManager;
import com.cpa.project.Utils.CollisionDetector;
import com.cpa.project.Utils.Pathfinding.GradientGraph;
import com.cpa.project.Utils.Pathfinding.Location;
import com.cpa.project.Utils.PoolManager;
import com.cpa.project.Utils.SonicWaveProps;
import com.cpa.project.World.GameMap;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Timer;


import java.util.*;

public class PlayState {
    private final boolean PATH_FINDING_DEBUG = false;
    public static TopDownCamera topDownCamera;
    public static Player player;

    public static Map<Entity, SonicWaveProps> affectedBySonicWave;

    public static Set<Entity> playerProjectiles;
    public static Set<Entity> enemyProjectiles;
    public static List<Entity> removedEntities;
    public static Set<Entity> enemies;
    public Vector2 lastTileXY;

    public static boolean isPaused;

    public static GameMap map;

    private GradientGraph gradientGraph;

    private final float spawnInterval = 5;
    private float spawnTimer = spawnInterval;

    public PlayState() {

        playerProjectiles = new HashSet<>();
        enemyProjectiles = new HashSet<>();
        removedEntities = new ArrayList<>();
        affectedBySonicWave = new HashMap<>();
        enemies = new HashSet<>();
        // FOR TESTING PURPOSES
        Sprite playerSprite = new Sprite(AssetManager.getPlayerTexture());
        playerSprite.setScale(0.20f);
        Player player = new Player(new Vector2(800, 240), playerSprite);
//        player.setSpeed(200);
//        player.setHealth(100);
//        player.setDamage(10);
//        player.setLevel(1);
        PlayState.player = player;

        topDownCamera = new TopDownCamera();
        topDownCamera.setTarget(player);
        topDownCamera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        isPaused = false;

        Vector2 playerPosition = player.getPosition().cpy();
        map = new GameMap(topDownCamera, playerPosition);
        map.init();
        map.addNoiseMapToTiledMap(playerPosition);

        Vector2 worldcenter = map.getWorldCenter();
        player.setPosition(worldcenter.cpy());

        this.lastTileXY = map.getEntityTileXY(PlayState.player);
        this.gradientGraph = new GradientGraph();
        this.gradientGraph.compute();
    }

    private void spawnEnemyAroundPlayer() {
//        float distance = MathUtils.random(0, 1000); // Random distance from player within radius
        float distance = MathUtils.random(600, 1000);
        float angle = MathUtils.random(0, MathUtils.PI2); // Random angle for full 360 degrees

        float enemyX = player.getPosition().x + distance * MathUtils.cos(angle);
        float enemyY = player.getPosition().y + distance * MathUtils.sin(angle);
        Vector2 tileXY = map.getTilePosition(new Vector2(enemyX, enemyY), AssetManager.getSkeletonTexture().getHeight());
        if (this.gradientGraph.getGraph()[(int) tileXY.x][(int) tileXY.y] == null) {
            return;
        }
        if (!map.getTiles()[(int) tileXY.x][(int) tileXY.y].isReachable()) {
            return;
        }
        Entity enemy = chooseEnemyTypeBasedOnPlayerLevel(player.getLevel(), enemyX, enemyY);

        enemy.resetSpeed();
        Vector2 entityTileXY = map.getEntityTileXY(enemy);
        Vector2 velocity = gradientGraph.getDirection((int) entityTileXY.x - 1, (int) entityTileXY.y - 1); // we get the direction of the player from the gradient graph
        enemy.setVelocity(velocity);
        enemies.add(enemy);
    }

    private Entity chooseEnemyTypeBasedOnPlayerLevel(int playerLevel, float x, float y) {
//        Texture enemyTexture = AssetManager.getSkeletonTexture(); // Default enemy texture ( animations change this on each enemy )
//        if (playerLevel <= 10) {
//            return new Skeleton(new Vector2(x, y), new Sprite(enemyTexture));
//        } else if (playerLevel <= 20) {
//            return new FlyingBat(new Vector2(x, y), new Sprite(enemyTexture));
//        }
//        else {
//            // if we add more enemies, we can add more cases here
//            // Randomly choose between Skeleton and Bat
//            int random = MathUtils.random(0, 1);
//            if (random == 0) {
//                return new Skeleton(new Vector2(x, y), new Sprite(enemyTexture));
//            }
//            return new FlyingBat(new Vector2(x, y), new Sprite(enemyTexture));
//        }

        if (playerLevel <= 10) {
            Skeleton skeleton = PoolManager.obtainSkeleton();
            skeleton.setPosition(new Vector2(x, y));
            skeleton.setHealth(skeleton.getMaxHealth()); // a little error when we use the pool , we need to reset the health
            return skeleton;
        } else if (playerLevel <= 20) {
            FlyingBat Bat = PoolManager.obtainBat();
            Bat.setPosition(new Vector2(x, y));
            Bat.setHealth(Bat.getMaxHealth());
            return Bat;
        } else {
            // TODO: THIS COULD SPAWN A HUGE WIZARD THAT INSTANTLY KILLS THE PLAYER
            // TODO: AT THIS POINT, IT'S NOT A BUG, IT'S A FEATURE
            // if we add more enemies, we can add more cases here
            // Randomly choose between Skeleton and Bat
            int random = MathUtils.random(0, 1);
            if (random == 0) {
                Skeleton skeleton = PoolManager.obtainSkeleton();
                skeleton.setPosition(new Vector2(x, y));
                return skeleton;
            }
            FlyingBat Bat = PoolManager.obtainBat();
            Bat.setPosition(new Vector2(x, y));
            return Bat;
        }
    }


    public void update(float dt) {
        topDownCamera.update(dt);
        player.update(dt);
        spawnTimer -= dt;
        if (spawnTimer < 0) {
            spawnEnemyAroundPlayer();
            spawnTimer = spawnInterval;
        }
        this.gradientGraph.compute();
        for (Entity entity : playerProjectiles) {
            // if projectile are at a certain distance from the player , remove them
            if (entity.getPosition().dst(player.getPosition()) > 2000) {
                removedEntities.add(entity);
            }
            entity.update(dt);
        }
        for (Entity entity : enemyProjectiles) {
            entity.update(dt);
        }
        for (Entity entity : enemies) {
            entity.handleSound(player.getPosition());
            if (entity.getPosition().sub(player.getPosition()).len() > SonicWave.MAX_DISTANCE_AWAY) {
                affectedBySonicWave.remove(entity);
                entity.resetSpeed();
            }
            Vector2 entityTileXY = map.getEntityTileXY(entity);
            Vector2 velocity = gradientGraph.getDirection((int) entityTileXY.x - 1, (int) entityTileXY.y - 1); // we get the direction of the player from the gradient graph
            entity.setVelocity(velocity);
            if (affectedBySonicWave.get(entity) != null) {
                SonicWaveProps props = affectedBySonicWave.get(entity);
                entity.setVelocity(props.getDirection());
                entity.setSpeed(props.getSpeed());
            }
            // if the enemy is at a certain distance from the player
            // move them towards the player diagonally
            else if (entity.getPosition().dst(player.getPosition()) < 200) {
//                System.out.println("triggered");
                Vector2 direction = player.getPosition().sub(entity.getPosition()).nor();
                entity.resetSpeed();
                entity.setVelocity(direction);
            }
            entity.update(dt);
            if (CollisionDetector.checkCollision(player, entity)) {
                // Put attacking animation here
                player.collidesWith(entity);
            }

        }
        for (Entity entity : removedEntities) {
            playerProjectiles.remove(entity);
            enemyProjectiles.remove(entity);
            enemies.remove(entity);
//            entity.dispose();
            if (entity instanceof Skeleton) { // we use a pool for the enemies in order to avoid creating and disposing them
                PoolManager.freeSkeleton((Skeleton) entity);
            } else if (entity instanceof FlyingBat) {
                PoolManager.freeBat((FlyingBat) entity);
            } else {
//                entity.dispose(); // dispose the other entities ( player projectiles )
                // i removed this because we are now using an asset manager to dispose the textures and handle them
                // if you uncomment the entity.dispose() and try using the fireball spell, it will be good first time
                // but then you will get a black box for the texture of the fireball for the rest
                // the dispose in entity only disposes the texture of the sprite btw
            }
        }
        removedEntities.clear();
    }

    public void render(SpriteBatch batch) {
        batch.setProjectionMatrix(topDownCamera.combined);
        batch.begin();
        map.render();
        player.getSprite().draw(batch);
        for (Entity entity : playerProjectiles) {
            entity.getSprite().draw(batch);
        }
        for (Entity entity : enemyProjectiles) {
            entity.getSprite().draw(batch);
        }
        for (Entity entity : enemies) {
            entity.getSprite().draw(batch);
        }
        if (PATH_FINDING_DEBUG) {
            Location[][] graph = this.gradientGraph.getGraph();
            BitmapFont font = AssetManager.getFont();
            Tile[][] tiles = map.getTiles();
            for (int i = 0; i < tiles.length; i++) {
                for (int j = 0; j < tiles[0].length; j++) {
                    if (graph[i][j] != null) {
                        font.draw(batch, String.valueOf(graph[i][j].getCost()), i * 48, j * 48 - 5);
                        Vector2 direction = graph[i][j].getDirection();
//                        if (Objects.equals(direction, new Vector2(1, 0))) {
//                            font.draw(batch, ">", i * 48, j * 48);
//                        } else if (Objects.equals(direction, new Vector2(-1, 0))) {
//                            font.draw(batch, "<", i * 48, j * 48);
//                        } else if (Objects.equals(direction, new Vector2(0, 1))) {
//                            font.draw(batch, "^", i * 48, j * 48);
//                        } else if (Objects.equals(direction, new Vector2(0, -1))) {
//                            font.draw(batch, "v", i * 48, j * 48);
//                        } else if (Objects.equals(direction, new Vector2(1, 1).nor())) {
//                            font.draw(batch, "|>", i * 48, j * 48);
//                        } else if (Objects.equals(direction, new Vector2(-1, 1).nor())) {
//                            font.draw(batch, "<|", i * 48, j * 48);
//                        } else if (Objects.equals(direction, new Vector2(1, -1).nor())) {
//                            font.draw(batch, "|>", i * 48, j * 48);
//                        } else if (Objects.equals(direction, new Vector2(-1, -1).nor())) {
//                            font.draw(batch, "<|", i * 48, j * 48);
//                        }
                        Texture arrowTexture = AssetManager.getArrowTexture();
                        Sprite arrowSprite = new Sprite(arrowTexture);

                        batch.draw(arrowSprite, i * 42 + 25, j * 42,
                                arrowSprite.getWidth() / 2f, arrowSprite.getHeight() / 2f,
                                arrowSprite.getWidth(), arrowSprite.getHeight(),
                                0.025f, 0.025f, direction.angleDeg());

                    }
                }
            }
        }
        // UI Elements need to be drawn at the end so that they are on top of everything

        // TODO: Figure out a way to make these UI elements using Scene2D
//        Vector3 unprojectedPos = topDownCamera.unproject(new Vector3(100, 800, 0));
//        Texture healthBar = player.getHealthBar();
//        batch.draw(healthBar, unprojectedPos.x, unprojectedPos.y);
//        String healthText = String.format("%.0f%%", ((float) player.getHealth() / player.getMaxHealth()) * 100);
//        BitmapFont font = AssetManager.getFont();
//        float textX = (unprojectedPos.x + 140);
//        float textY = (unprojectedPos.y + 17);
//        font.draw(batch, healthText, textX, textY);
//        Texture xpBar = player.getXPBar();
//        batch.draw(xpBar, unprojectedPos.x, unprojectedPos.y - 30);
//        // TODO: Change this to get the player's class
//        String xpText = String.format("Mage Lvl.%d", player.getLevel());
//        textX = (unprojectedPos.x + 118);
//        textY = (unprojectedPos.y - 13);
//        font.draw(batch, xpText, textX, textY);
        batch.end();
    }

    public void dispose() {
//        player.dispose();
//        for (Entity entity : playerProjectiles) {
//            entity.dispose();
//        }
//        for (Entity entity : enemyProjectiles) {
//            entity.dispose();
//        }
//        for (Entity entity : enemies) {
//            entity.dispose();
//        }
        playerProjectiles.clear();
        enemyProjectiles.clear();
        enemies.clear();
        affectedBySonicWave.clear();
        removedEntities.clear();
        playerProjectiles = null;
        enemyProjectiles = null;
        enemies = null;
        affectedBySonicWave = null;
        removedEntities = null;
        map.dispose();
        this.gradientGraph = null;
//        AssetManager.dispose();
    }

    public void pause() {
        isPaused = true;
    }

    public void resume() {
        isPaused = false;
    }

    public void resize(int width, int height) {
        topDownCamera.setToOrtho(false, width, height);
    }

}
