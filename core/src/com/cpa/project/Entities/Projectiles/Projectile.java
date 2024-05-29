package com.cpa.project.Entities.Projectiles;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.cpa.project.Entities.Entity;
import com.cpa.project.State.PlayState;
import com.cpa.project.Utils.CollisionDetector;

public abstract class Projectile extends Entity{

    public Projectile (Vector2 position, Sprite sprite, Vector2 direction, float speed, float damage) {
          super(position, sprite);
          this.velocity = direction;
          this.speed = speed;
          this.damage = damage;
          this.sprite.setRotation(this.velocity.angleDeg());
    }

    @Override
    public void update(float dt) {

        // Move the projectile
        this.sprite.translate(this.velocity.x * this.speed * dt, this.velocity.y * this.speed * dt);
        // Collision detection with entities
        if (this.entityType == EntityType.PLAYER_PROJECTILE) {
            for (Entity entity : PlayState.enemies) {
                if (CollisionDetector.checkCollision(this, entity)) {
                    this.collidesWith(entity);
                    PlayState.removedEntities.add(this);
                }
            }
        } else if (this.entityType == EntityType.ENEMY_PROJECTILE) {
            if (CollisionDetector.checkCollision(this, PlayState.player)) {
                this.collidesWith(PlayState.player);
                PlayState.removedEntities.add(this);
            }
        }
    }

    @Override
    public void collidesWith(Entity other) {
        other.setHealth(other.getHealth() - this.getDamage());
        if (other.getHealth() <= 0) {
            // TODO: Change mechanism for experience
            PlayState.player.addExperience(10);
            PlayState.removedEntities.add(other);
        }
    }

    @Override
    public Entity clone() {
        return null;
    }
}
