package com.cpa.project.Entities.Actors.Mobs;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.cpa.project.Entities.Entity;
import com.cpa.project.Utils.AnimationHandler;
import com.cpa.project.Utils.AssetManager;
import com.cpa.project.Utils.Direction;

import static com.cpa.project.Survivors.audioHandler;

public class FlyingBat extends Entity {
    private String soundName ;

    // Animation
    protected AnimationHandler animationHandler;
    protected float elapsedTime = 0;

    protected enum State {
         WALKING  , ATTACKING
    }

    protected FlyingBat.State lastState ;
    protected Direction lastDirection;

    protected final String WALK_RIGHT = "WalkRight";
    protected final String WALK_LEFT = "WalkLeft";
    protected final String WALK_UP = "WalkUp";
    protected final String WALK_DOWN = "WalkDown";
    protected final String DAMAGE = "Damage";
    protected final String ATTACK = "Attack";

    protected final float TimeFrame = 0.25f;

    public FlyingBat(){
        super(new Vector2(0,0) , new Sprite(AssetManager.getSkeletonTexture()));
        this.speed = 100;
        this.default_speed = 100;
        this.health = 100;
        this.maxHealth = 100;
        this.damage = 10;
        this.entityType = EntityType.ENEMY;
        this.velocity = new Vector2(0, 0);

        this.lastState = State.WALKING;
        this.lastDirection = Direction.RIGHT;

        this.animationHandler = new AnimationHandler();
        initAnimations();

    }
    public FlyingBat(Vector2 position, Sprite sprite, float speed, float damage, float health ) {
        super(position, sprite, speed, damage);
        this.health = health;
        this.maxHealth = health;
        this.entityType = EntityType.ENEMY;
        this.default_speed = speed;
        this.lastState = State.WALKING;
        this.lastDirection = Direction.RIGHT;

        this.animationHandler = new AnimationHandler();

        initAnimations();
    }

    public FlyingBat(Vector2 position, Sprite sprite ) {
        super(position, sprite);
        this.speed = 100;
        this.default_speed = 100;
        this.health = 100;
        this.maxHealth = 100;
        this.damage = 10;
        this.entityType = EntityType.ENEMY;
        this.velocity = new Vector2(0, 0);

        this.lastState = State.WALKING;
        this.lastDirection = Direction.RIGHT;

        this.animationHandler = new AnimationHandler();
        initAnimations();
    }

    public FlyingBat(Vector2 position ) {
        super(position);
        this.speed = 150;
        this.default_speed = 120;
        this.health = 100;
        this.maxHealth = 150;
        this.damage = 20;
        this.entityType = EntityType.ENEMY;
        this.velocity = new Vector2(0, 0);

        this.lastState = State.WALKING;
        this.lastDirection = Direction.RIGHT;

        this.animationHandler = new AnimationHandler();
        initAnimations();
    }


    /**
     * Initialize the animations for the player
     * this loads atlas files and creates animations for the player
     */
    public void initAnimations(){
        // load the animations
        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("animations/bat/walk_d/bat_d.atlas"));
        TextureAtlas atlas2 = new TextureAtlas(Gdx.files.internal("animations/bat/walk_u/bat_u.atlas"));
        TextureAtlas atlas3 = new TextureAtlas(Gdx.files.internal("animations/bat/walk_r/bat_r.atlas"));
        TextureAtlas atlas4 = new TextureAtlas(Gdx.files.internal("animations/bat/walk_l/bat_l.atlas"));

        Animation<TextureRegion> walkDown = new Animation<>(TimeFrame, atlas.getRegions());
        Animation<TextureRegion> walkUp = new Animation<>(TimeFrame, atlas2.getRegions());
        Animation<TextureRegion> walkRight = new Animation<>(TimeFrame, atlas3.getRegions());
        Animation<TextureRegion> walkLeft = new Animation<>(TimeFrame, atlas4.getRegions());


        this.animationHandler.add(WALK_DOWN, walkDown);
        this.animationHandler.add(WALK_UP, walkUp);
        this.animationHandler.add(WALK_RIGHT, walkRight);
        this.animationHandler.add(WALK_LEFT, walkLeft);

        this.animationHandler.setCurrent(WALK_DOWN);

    }

    public void move(float dt) {
        // check direction && update the animation
        if (this.velocity.x > 0) {
            this.animationHandler.setCurrent(WALK_RIGHT);
        } else if (this.velocity.x < 0) {
            this.animationHandler.setCurrent(WALK_LEFT);
        } else if (this.velocity.y > 0) {
            this.animationHandler.setCurrent(WALK_UP);
        } else if (this.velocity.y < 0) {
            this.animationHandler.setCurrent(WALK_DOWN);
        }

    }


    @Override
    public void update(float dt) {
        super.update(dt);
        this.elapsedTime += dt;
        move(dt);
        // update the animation
        TextureRegion currentFrame = this.animationHandler.getFrame();
//        System.out.println("curr frame " + currentFrame);
        if (currentFrame != null) {
            this.sprite.setRegion(currentFrame);
        }

        // Check if the player is close enough to the player to attack

        switch (this.lastState) {
            case WALKING:
                // we do nothing
                break;
            case ATTACKING:
                // we launch the attack and then we go back to walking
                this.initAttack();
                this.lastState = State.WALKING;
                break;
        }




    }


    public void initAttack() {
        // animation
    }


    @Override
    public void collidesWith(Entity other) {
        if (other.getEntityType() == EntityType.PLAYER_PROJECTILE) {
            this.setHealth(this.getHealth() - other.getDamage());

            // we take a push back effect ( we move back 10 times the velocity of the projectile)
            this.setPosition(this.getPosition().add(other.getVelocity().scl(-2)));

        }
        // we already  do this in player class
//        if (other.getEntityType() == EntityType.PLAYER) {
//           other.setHealth(other.getHealth() - this.getDamage());
//        }
    }

    @Override
    public Entity clone() {
        return new FlyingBat(new Vector2(this.getSprite().getX(),
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
            if (!audioHandler.hasSound("batZombieRoaming")) {
                Music zombieSound = audioHandler.loadMusic("audio/monster/Monster-9.wav");
                audioHandler.addSoundEffect("batZombieRoaming", zombieSound);
            }
            // Adjust the volume based on distance, quieter if further away
            float volume = 1 - (distance / soundRange);
            audioHandler.playSoundEffect("batZombieRoaming", volume);
        } else {
            // Stop the sound if the zombie is out of range
            audioHandler.stopSoundEffect("batZombieRoaming");
        }
    }

}
