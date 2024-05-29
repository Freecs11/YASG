package com.cpa.project.Entities.Spells;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.cpa.project.Entities.Projectiles.SmallFireBall;
import com.cpa.project.State.PlayState;
import com.cpa.project.Utils.AssetManager;

public class AutoFireBall extends Spell {


    // This spell adds an auto fireball to the player
    // With each level, a fireball is added
    // The fireballs are shot in a circle around the player
    // The fireballs are shot in the direction of the cursor
    // Maximum number of fireballs is 20
    // The damage of the fireballs increases with the player's level
    // The speed of the fireballs is constant
    // The cooldown of the spell is constant
    protected final int MAX_FIREBALLS = 20;
    protected final float DEFAULT_DAMAGE = 10;
    protected final float DEFAULT_COOLDOWN = 1f;
    protected final float DEFAULT_SPEED = 200;


    public AutoFireBall() {
        this.timeToReady = 0;
        this.speed = this.DEFAULT_SPEED;
        this.spellCooldown = this.DEFAULT_COOLDOWN;
        this.damage = this.DEFAULT_DAMAGE;
        this.autoCast = false;
        this.spellName = "AutoFireBall";
    }

    @Override
    public void cast() {
        Vector2 playerPosition = PlayState.player.getPosition();
        Vector3 cursorPos3D = PlayState.topDownCamera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
        Vector2 cursorPos = new Vector2(cursorPos3D.x, cursorPos3D.y);
        Vector2 bulletDir = cursorPos.sub(playerPosition).nor();

        int numOfFireballs = PlayState.player.getLevel();
        if (numOfFireballs > MAX_FIREBALLS) {
            numOfFireballs = MAX_FIREBALLS;
        }
        for (int i = 0; i < numOfFireballs; i++) {
            SmallFireBall fireBall = new SmallFireBall(playerPosition,
                    new Sprite(AssetManager.getAutofireball()),
                    new Vector2(bulletDir).rotateDeg(i * 360f / numOfFireballs),
                    this.speed,
                    this.damage);
            PlayState.playerProjectiles.add(fireBall);
        }
        this.timeToReady = this.DEFAULT_COOLDOWN;
    }


}
