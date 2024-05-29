package com.cpa.project.Entities.Actors.Mobs;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.cpa.project.Entities.Entity;
import com.cpa.project.Utils.AssetManager;

import static com.cpa.project.Survivors.audioHandler;

public class Skeleton extends Entity {

    public float timeToNextPath = 0;

    private String soundName ;

    public Skeleton(){
        super(new Vector2(0,0) , new Sprite(AssetManager.getSkeletonTexture()));
        this.speed = 100;
        this.default_speed = 100;
        this.health = 100;
        this.maxHealth = 100;
        this.damage = 10;
        this.entityType = EntityType.ENEMY;
        this.velocity = new Vector2(0, 0);
    }
    public Skeleton(Vector2 position, Sprite sprite, float speed, float damage, float health ) {
        super(position, sprite, speed, damage);
        this.health = health;
        this.maxHealth = health;
        this.entityType = EntityType.ENEMY;
    }



    public Skeleton(Vector2 position, Sprite sprite ) {
        super(position, sprite);
        this.speed = 100;
        this.default_speed = 100;
        this.health = 100;
        this.maxHealth = 100;
        this.damage = 10;
        this.entityType = EntityType.ENEMY;
        this.velocity = new Vector2(0, 0);
    }
    @Override
    public void collidesWith(Entity other) {
        if (other.getEntityType() == EntityType.PLAYER_PROJECTILE) {
            this.setHealth(this.getHealth() - other.getDamage());
        }
        // we already  do this in player class
//        if (other.getEntityType() == EntityType.PLAYER) {
//           other.setHealth(other.getHealth() - this.getDamage());
//        }
    }

    @Override
    public Entity clone() {
        return new Skeleton(new Vector2(this.getSprite().getX(),
                this.getSprite().getY()),
                this.sprite, this.speed, this.damage, this.health);
    }

    @Override
    public void handleSound(Vector2 playerPosition) {
        float soundRange = 1500;
        float distance = this.getPosition().dst(playerPosition);

        // Check if the zombie is within the sound range of the player
        if (distance < soundRange) {
            // Check if the sound is already playing, if not, add it & play it
            if (!audioHandler.hasSound("zombieRoaming")) {
                Music zombieSound = audioHandler.loadMusic("audio/monster/Monster-1.wav");
                audioHandler.addSoundEffect("zombieRoaming", zombieSound);
            }
            // Adjust the volume based on distance, quieter if further away
            float volume = 1 - (distance / soundRange);
            audioHandler.playSoundEffect("zombieRoaming", volume);
        } else {
            // Stop the sound if the zombie is out of range
            audioHandler.stopSoundEffect("zombieRoaming");
        }
    }

}
