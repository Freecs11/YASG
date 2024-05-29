package com.cpa.project.Entities.Actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.cpa.project.Entities.Entity;
import com.cpa.project.Entities.Projectiles.BasicFireBall;
import com.cpa.project.Entities.Projectiles.Projectile;
import com.cpa.project.Entities.Spells.AutoFireBall;
import com.cpa.project.Entities.Spells.Heal;
import com.cpa.project.Entities.Spells.SonicWave;
import com.cpa.project.Entities.Spells.Spell;
import com.cpa.project.State.PlayState;
import com.cpa.project.Tiles.Tile;
import com.cpa.project.Utils.AnimationHandler;
import com.cpa.project.Utils.AssetManager;
import com.cpa.project.Utils.Direction;

import java.util.HashMap;
import java.util.Map;

import static com.cpa.project.Survivors.audioHandler;

public class Player extends Entity {
    protected final float DEFAULT_AS = 0.5f;
    protected float ATT_SPEED;
    protected Map<String, Spell> spells;
    protected float attackRate;
    protected float experience;
    protected float experienceToNextLevel;
    protected int level;

    private final Music fireballSound = Gdx.audio.newMusic(Gdx.files.internal("audio/flaunch.wav"));
    private final Music healSound = Gdx.audio.newMusic(Gdx.files.internal("audio/healspell2.wav"));
    private final Music sonicWaveSound = Gdx.audio.newMusic(Gdx.files.internal("audio/rlaunch.wav"));

    // Animation
    protected AnimationHandler animationHandler;
    protected float elapsedTime = 0;

    // state enum
    protected enum State {
        IDLE, WALKING , DAMAGED , ATTACKING
    }

    protected State lastState ;
    protected Direction lastDirection;

    protected final String IDLE = "Idle";
    protected final String WALK = "Walk";
    protected final String WALK_UP = "WalkUp";
    protected final String WALK_DOWN = "WalkDown";
    protected final String DAMAGE = "Damage";
    protected final String ATTACK = "Attack";

    protected final float TimeFrame = 0.25f;

    public Player(Vector2 position, Sprite sprite) {
        super(position, sprite);
        this.entityType = EntityType.PLAYER;
        this.speed = 230;
        this.health = 100;
        this.maxHealth = 100;
        this.damage = 10;
        this.spells = new HashMap<>();
        this.spells.put("AutoFireBall", new AutoFireBall());
        this.spells.put("SonicWave", new SonicWave());
        this.spells.put("Heal", new Heal());
        this.experience = 0;
        this.experienceToNextLevel = 100;
        this.level = 1;
        this.attackRate = 0;
        this.ATT_SPEED = DEFAULT_AS;

        this.animationHandler = new AnimationHandler();

        this.lastState = State.IDLE;
        this.lastDirection = Direction.RIGHT;
        initAnimations();
    }

    public Player(Vector2 position) {
        super(position);
        this.entityType = EntityType.PLAYER;
        this.speed = 100;
        this.health = 100;
        this.maxHealth = 100;
        this.damage = 10;
        this.spells = new HashMap<>();
        this.spells.put("AutoFireBall", new AutoFireBall());
        this.spells.put("SonicWave", new SonicWave());
        this.spells.put("Heal", new Heal());
        this.experience = 0;
        this.experienceToNextLevel = 100;
        this.level = 1;
        this.attackRate = 0;
        this.ATT_SPEED = DEFAULT_AS;

        this.animationHandler = new AnimationHandler();

        this.lastState = State.IDLE;
        this.lastDirection = Direction.RIGHT;

        initAnimations();
    }

    /**
     * Initialize the animations for the player
     * this loads atlas files and creates animations for the player
     */
    public void initAnimations(){
        // load the animations
        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("animations/player/walk_lr.atlas"));
        Animation<TextureRegion> idleAnimation = new Animation<>(TimeFrame, atlas.findRegions("walk_lr"));
        this.animationHandler.add(WALK, idleAnimation);
        atlas = new TextureAtlas(Gdx.files.internal("animations/player/idle.atlas"));
        Animation<TextureRegion> walkAnimation = new Animation<>(TimeFrame, atlas.findRegions("idle"));
        this.animationHandler.add(IDLE, walkAnimation);
        atlas = new TextureAtlas(Gdx.files.internal("animations/player/walk_u.atlas"));
        Animation<TextureRegion> walkUpAnimation = new Animation<>(TimeFrame, atlas.findRegions("walk_u"));
        this.animationHandler.add(WALK_UP, walkUpAnimation);
        atlas = new TextureAtlas(Gdx.files.internal("animations/player/walk_d.atlas"));
        Animation<TextureRegion> walkDownAnimation = new Animation<>(TimeFrame, atlas.findRegions("walk_d"));
        this.animationHandler.add(WALK_DOWN, walkDownAnimation);
        atlas = new TextureAtlas(Gdx.files.internal("animations/player/damage.atlas"));
        Animation<TextureRegion> damageAnimation = new Animation<>(TimeFrame, atlas.findRegions("damage"));
        this.animationHandler.add(DAMAGE, damageAnimation);
        atlas = new TextureAtlas(Gdx.files.internal("animations/player/attack.atlas"));
        Animation<TextureRegion> attackAnimation = new Animation<>(TimeFrame, atlas.findRegions("attack"));
        this.animationHandler.add(ATTACK, attackAnimation);

        this.animationHandler.setCurrent(IDLE);
    }


    public void move(float dt) {
        Vector2 newPosition = this.getPosition().cpy(); // Copy current position to a new vector

        // Determine new velocity based on input
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.Q)) {
            if (Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.Z))
                this.velocity = new Vector2(-this.speed * dt, this.speed * dt).nor();
            else if (Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.S))
                this.velocity = new Vector2(-this.speed * dt, -this.speed * dt).nor();
            else
                this.velocity = new Vector2(-this.speed * dt, 0).nor();
        } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D)) {
            if (Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.Z))
                this.velocity = new Vector2(this.speed * dt, this.speed * dt).nor();
            else if (Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.S))
                this.velocity = new Vector2(this.speed * dt, -this.speed * dt).nor();
            else
                this.velocity = new Vector2(this.speed * dt, 0).nor();
        } else if (Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.Z)) {
            this.velocity = new Vector2(0, this.speed * dt).nor();
        } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.S)) {
            this.velocity = new Vector2(0, -this.speed * dt).nor();
        } else {
            this.velocity = new Vector2(0, 0); // No movement if no key pressed
        }

        // Calculate the potential new position
        newPosition.add(this.velocity.x * this.speed * dt, this.velocity.y * this.speed * dt);

        // Check the tile at the potential new position
        Tile tile = PlayState.map.getTileAt(newPosition , this.sprite.getRegionHeight());
        if (tile != null && tile.isReachable()) {
            // Apply the movement since the tile is reachable
            this.sprite.translate(this.velocity.x * this.speed * dt, this.velocity.y * this.speed * dt);
        } else {
            // Reset velocity if the new position is not reachable
//            System.out.println("Not reachable");
            this.velocity = new Vector2(0, 0);
        }


        // Determine state based on velocity (IDLE or WALKING ) and also take into account the last state to determine if the player is attacking or damaged
        State newState ;
        if (lastState == State.DAMAGED) {
            if (animationHandler.isFinished()) {
                newState = State.IDLE;
            } else {
                newState = State.DAMAGED;
            }
        }else if (lastState == State.ATTACKING) {
            if (animationHandler.isFinished()) {
                newState = State.IDLE;
            } else {
                newState = State.ATTACKING;
            }
        }
        else if (velocity.isZero()) {
            newState = State.IDLE;
        } else {
            newState = State.WALKING;
        }


        // Determine direction based on velocity
        Direction newDirection = lastDirection;
        if (!velocity.isZero()) {
            if (Math.abs(velocity.x) > Math.abs(velocity.y)) {
                newDirection = (velocity.x > 0) ? Direction.RIGHT : Direction.LEFT;
            } else {
                newDirection = (velocity.y > 0) ? Direction.TOP : Direction.DOWN;
            }
        }

        if (animationHandler.isCurrent(ATTACK) || animationHandler.isCurrent(DAMAGE)) {
            if (animationHandler.isFinished()) {
                animationHandler.setCurrent(IDLE, true);
            }
            else {
                return;
            }
        }

        // Update the animation only if the state or direction has changed
        if ((newState != lastState || newDirection != lastDirection )  )  {
            switch (newState) {
                case IDLE:
                    animationHandler.setCurrent(IDLE, true);
                    break;
                case WALKING:
                    if (newDirection == Direction.TOP) {
                        animationHandler.setCurrent(WALK_UP, true);
                    } else if (newDirection == Direction.DOWN) {
                        animationHandler.setCurrent(WALK_DOWN, true);
                    } else {
                        // For LEFT and RIGHT, use WALK and flip if necessary
                        animationHandler.setCurrent(WALK, true);
                        TextureRegion currentFrame = animationHandler.getFrame();

                        // Flip the texture if the direction is LEFT
                        if (newDirection == Direction.LEFT) {
                            for (TextureRegion frame : animationHandler.getFrames(WALK)) {
                                if (!frame.isFlipX()) frame.flip(true, false);
                            }
                        }else {
                            for (TextureRegion frame : animationHandler.getFrames(WALK)) {
                                if (frame.isFlipX()) frame.flip(true, false);
                            }
                        }
                    }
                    break;
                case ATTACKING:
                    animationHandler.setCurrent(ATTACK, false);
                    break;
                case DAMAGED:
                    animationHandler.setCurrent(DAMAGE, false);
                    break;

            }

//             Update lastState and lastDirection
            lastState = newState;
            lastDirection = newDirection;
        }

    }


    @Override
    public void update(float dt) {
        // Update the animation
        this.elapsedTime += dt;

        // update the animation
        TextureRegion currentFrame = this.animationHandler.getFrame();
        if (currentFrame != null) {
            this.sprite.setRegion(currentFrame);
        }


        if (this.experience > experienceToNextLevel) {
            this.experienceToNextLevel *= 1.5f;
            this.level++;
            this.experience = 0;
            // Increase health by 10 per level
            this.maxHealth = 100 + this.level * 10;
            // Reset health to max health
            this.health = this.maxHealth;
            // Increase damage by 5 per level
            this.damage = 10 + this.level * 5;
            // Increase attack speed by 0.05f per level
            this.ATT_SPEED = DEFAULT_AS - this.level * 0.005f;
        }
        this.move(dt);
        if (Gdx.input.isTouched()) {
            if (this.attackRate > 0) {
                this.attackRate -= dt;
            } else { // we fire a projectile when user clicks the mouse button
                Vector2 playerPosition = this.getPosition();
                Vector3 cursorPos3D = PlayState.topDownCamera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
                Vector2 cursorPos = new Vector2(cursorPos3D.x, cursorPos3D.y);
                Vector2 bulletDir = cursorPos.sub(playerPosition).nor();
                Projectile basicBall = new BasicFireBall(
                        playerPosition,
                        new Sprite(
                                AssetManager.getFireBallSmall()
                        ),
                        bulletDir,
                        400,
                        this.damage
                );
                PlayState.playerProjectiles.add(basicBall);
                this.attackRate = ATT_SPEED;
            }
        }

        // The way spells' cooldowns are handled is that we check the time to ready
        // if it is less than or equal to 0, we cast the spell
        // if it is greater than 0, we decrement it by dt
        // after casting the spell, we set the time to ready to the default cooldown

        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            Gdx.app.postRunnable(() -> audioHandler.addSoundEffect("sonicWave" , sonicWaveSound));
            float castTime = this.spells.get("SonicWave").getTimeToReady();
            castTime -= dt;
            if (castTime <= 0) {
                this.spells.get("SonicWave").cast();
            } else {
                this.spells.get("SonicWave").setSpellTime(castTime);
            }

        }
        if (Gdx.input.isKeyPressed(Input.Keys.H)) {
            Gdx.app.postRunnable(() -> audioHandler.addSoundEffect("heal" , healSound));
            float castTime = this.spells.get("Heal").getTimeToReady();
            castTime -= dt;
            if (castTime <= 0) {
                this.spells.get("Heal").cast();
            } else {
                this.spells.get("Heal").setSpellTime(castTime);
            }
        }

        if (Gdx.input.isKeyPressed(Input.Keys.F)) {
            Gdx.app.postRunnable(() -> audioHandler.addSoundEffect("fireball" , fireballSound));
            float castTime = this.spells.get("AutoFireBall").getTimeToReady();
            castTime -= dt;
            if (castTime <= 0) {
                this.spells.get("AutoFireBall").cast();
            } else {
                this.spells.get("AutoFireBall").setSpellTime(castTime);
            }
            this.lastState = State.ATTACKING;
        }

        // Loop for spells with autocast on
        this.spells.values().forEach(spell ->
        {
            float castTime = spell.getTimeToReady();
            castTime -= dt;
            if (castTime <= 0) {
                if (spell.isAutoCast()) {
                    spell.cast();
                }
            } else {
                spell.setSpellTime(castTime);
            }
        });

    }

    @Override
    public void collidesWith(Entity other) {
        if (other.getEntityType() == EntityType.ENEMY) {
            // Apply damage to the player
            this.setHealth(this.getHealth() - other.getDamage());

            // Calculate knockback direction: it's the normalized vector pointing from the enemy to the player
            Vector2 knockbackDirection = new Vector2(
                    this.getPosition().x - other.getPosition().x,
                    this.getPosition().y - other.getPosition().y
            );

            // Set the magnitude of the knockback
            float knockbackStrength = 2;

            this.velocity.x += knockbackDirection.x * knockbackStrength * Gdx.graphics.getDeltaTime();
            this.velocity.y += knockbackDirection.y * knockbackStrength * Gdx.graphics.getDeltaTime();

            this.sprite.translate(this.velocity.x * this.speed * Gdx.graphics.getDeltaTime(), this.velocity.y * this.speed * Gdx.graphics.getDeltaTime());


            this.lastState = State.DAMAGED;

        }
    }


    @Override
    public Entity clone() {
        return new Player(new Vector2(this.getSprite().getX(), this.getSprite().getY()), this.sprite);
    }


    public void setExperience(float experience) {
        this.experience = experience;
    }

    public void addExperience(float experience) {
        this.experience += experience;
    }

    public float getExperience() {
        return experience;
    }

    public void setLevel(int level) {
        this.level = level;
        // Increase health by 10 per level
        this.maxHealth = 100 + this.level * 10;
        // Reset health to max health
        this.health = this.maxHealth;
        // Increase damage by 5 per level
        this.damage = 10 + this.level * 5;
        // Increase attack speed by 0.05f per level
        this.ATT_SPEED = DEFAULT_AS - this.level * 0.005f;
    }

    public int getLevel() {
        return level;
    }

    public void addSpell(Spell spell) {
        this.spells.put(spell.getSpellName(), spell);
    }

    public void setSpells(Map<String, Spell> spells) {
        this.spells = spells;
    }

    public Map<String, Spell> getSpells() {
        return spells;
    }

    public void setAttackRate(float attackRate) {
        this.attackRate = attackRate;
    }

    public float getAttackRate() {
        return attackRate;
    }

    public void setExperienceToNextLevel(float experienceToNextLevel) {
        this.experienceToNextLevel = experienceToNextLevel;
    }

    public float getExperienceToNextLevel() {
        return experienceToNextLevel;
    }


}
