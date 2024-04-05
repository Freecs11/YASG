package com.cpa.project;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import com.cpa.project.Camera.OrthographicCamera;
import com.cpa.project.Entities.Actors.Mobs.Skeleton;
import com.cpa.project.Entities.Actors.Player;
import com.cpa.project.Entities.Entity;
import com.cpa.project.World.World;

import java.util.ArrayList;
import java.util.List;

public class Survivors extends Game {
    SpriteBatch batch;
    World world;

    @Override
    public void create() {
        batch = new SpriteBatch();
        Player player = new Player(new Vector2(800, 240), new Sprite(new Texture("badlogic.jpg")));
        List<Entity> entities = new ArrayList<>();
        entities.add(player);
        Entity ske1 = new Skeleton(new Vector2(400, 240), new Sprite(new Texture("badlogic.jpg")));
        entities.add(ske1);
        OrthographicCamera camera = new OrthographicCamera();
        world = new World(player, entities, camera);
    }

    @Override
    public void render() {
        ScreenUtils.clear(1, 0, 0, 1);
        batch.begin();
        world.update(Gdx.graphics.getDeltaTime());
        world.getPlayer().getSprite().draw(batch);
        for (Entity entity : world.getEntities()) {
            entity.getSprite().draw(batch);
        }
        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        world.dispose();
    }
}
