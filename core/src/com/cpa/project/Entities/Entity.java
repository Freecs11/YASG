package com.cpa.project.Entities;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.cpa.project.Utils.AssetManager;

import java.io.Serializable;

public abstract class Entity implements Serializable {

    final protected Sprite sprite;
    protected Vector2 velocity;

    protected float default_speed;
    protected float speed;
    protected float damage;

    protected EntityType entityType;

    protected float health;
    protected float maxHealth;

    public Entity(Vector2 position, Sprite sprite) {
        this.sprite = sprite;
        this.sprite.setPosition(position.x, position.y);
        this.velocity = new Vector2(0, 0);
    }

    public Entity(Vector2 position){
        this.sprite = new Sprite(AssetManager.getPlayerTexture()); // default texture for us is the player texture for now ( avoids null pointer exception)
        this.health = 100000000;
        this.maxHealth = 100000000;
        this.speed = 100;
        this.default_speed = 100;
        this.damage = 40;
        this.entityType = EntityType.ENEMY;
        this.sprite.setPosition(position.x, position.y);
        this.velocity = new Vector2(0, 0);
    }

    public Entity(float x, float y, Sprite sprite, Vector2 velocity, float speed, float damage) {
        this.sprite = sprite;
        this.sprite.setPosition(x, y);
        this.velocity = velocity;
        this.speed = speed;
        this.damage = damage;
    }


    public Entity(Vector2 position, Sprite sprite, float speed, float damage) {
        this.sprite = sprite;
        this.sprite.setPosition(position.x, position.y);
        this.speed = speed;
        this.damage = damage;
    }

    public void update(float dt){
        this.sprite.translate(this.velocity.x * this.speed * dt, this.velocity.y * this.speed * dt);
    }

    public void dispose() {
        sprite.getTexture().dispose();
    }

    public abstract void collidesWith(Entity other);


    // Position is the center of the sprite
    public Vector2 getPosition() {
        return new Vector2(sprite.getX() + sprite.getWidth() / 2, sprite.getY() + sprite.getHeight() / 2);
    }

    public void setPosition(Vector2 position) {
        this.sprite.setPosition(position.x, position.y);
        this.sprite.setOriginCenter();
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public void setVelocity(Vector2 velocity) {
        this.velocity = velocity;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public float getDamage() {
        return damage;
    }

    public void setDamage(float damage) {
        this.damage = damage;
    }

    public Sprite getSprite() {
        return sprite;
    }

    public abstract Entity clone();

    public EntityType getEntityType() {
        return this.entityType;
    }

    public void resetSpeed() {
        this.speed = default_speed;
    }


    public float getHealth() {
        return health;
    }

    public void setHealth(float health) {
        this.health = health;
    }

    public float getMaxHealth() {
        return maxHealth;
    }

    public void setMaxHealth(float maxHealth) {
        this.maxHealth = maxHealth;
    }


    public enum EntityType {
        ENEMY,
        ENEMY_PROJECTILE,
        PLAYER,
        PLAYER_PROJECTILE,
        ENVIRONMENT
    }

    public void handleSound(Vector2 playerPosition) {}
}
