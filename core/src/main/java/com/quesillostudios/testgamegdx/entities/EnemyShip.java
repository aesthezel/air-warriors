package com.quesillostudios.testgamegdx.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.quesillostudios.testgamegdx.entities.interfaces.Damagable;
import com.quesillostudios.testgamegdx.objects.Bullet;
import com.quesillostudios.testgamegdx.utils.interfaces.EnemyEventListener;

import java.util.ArrayList;
import java.util.Iterator;

// TODO: create a POO compatibility with Ship
public class EnemyShip extends Entity implements Damagable {

    private float health;
    private float damage;
    private TextureRegion shipRegion;
    private Texture bulletTexture;
    private Rectangle bounds;
    private EnemyEventListener killListener;

    private float direction = 1; // 1 = RIGHT, -1 = LEFT
    private float fireCooldown = 1.0f;
    private float timeSinceLastShot = 0f;

    private ArrayList<Bullet> bullets;
    public EnemyShip(float x, float y, float speed, Texture shipTexture, Texture bulletTexture, EnemyEventListener killListener) {
        super(x, y, speed);
        this.health = 3;
        this.damage = 10;
        this.shipRegion = new TextureRegion(shipTexture, 0, 128, 32, 32);
        this.bounds = new Rectangle(x, y, shipRegion.getRegionWidth(), shipRegion.getRegionHeight());
        this.bulletTexture = bulletTexture;
        this.killListener = killListener;
        this.bullets = new ArrayList<>();
        this.shipRegion.flip(false, true);
    }

    @Override
    public Rectangle getBounds() {
        return bounds;
    }

    @Override
    public void update(float delta, ArrayList<Damagable> targets) {
        move(delta);
        checkForCollisions(targets);
        controlBullets(delta, targets);
        shoot(delta);
    }

    private void controlBullets(float delta, ArrayList<Damagable> targets) {
        Iterator<Bullet> bulletIterator = bullets.iterator();
        while (bulletIterator.hasNext()) {
            Bullet bullet = bulletIterator.next();
            bullet.update(delta);

            if (bullet.isOffScreen()) {
                bulletIterator.remove();
                continue;
            }

            for (Damagable target : targets) {
                if (target instanceof Entity) {
                    Entity entity = (Entity) target;
                    if (bullet.getBounds().overlaps(entity.getBounds())) {
                        target.takeDamage(bullet.getDamage());
                        bulletIterator.remove();
                        break;
                    }
                }
            }
        }
    }

    private void move(float delta) {
        position.x += direction * speed * delta;

        if (position.x <= 0 || position.x + bounds.width >= Gdx.graphics.getWidth()) {
            direction *= -1;
            position.y -= 20;
        }

        bounds.setPosition(position.x, position.y);
    }

    private void shoot(float delta) {
        timeSinceLastShot += delta;
        if (timeSinceLastShot >= fireCooldown) {
            timeSinceLastShot = 0f;

            Vector2 bulletPosition = new Vector2((position.x + shipRegion.getRegionWidth() / 2) - 8, (position.y + shipRegion.getRegionHeight() - 32));
            Bullet bullet = new Bullet(bulletTexture, bulletPosition.x, bulletPosition.y, 1f, 500f, -1); // Velocidad hacia abajo

            bullets.add(bullet);
        }
    }

    private void checkForCollisions(ArrayList<Damagable> targets) {
        for (Damagable target : targets) {
            if (target instanceof Entity) {
                Entity entity = (Entity) target;
                if (this.getBounds().overlaps(entity.getBounds())) {
                    target.takeDamage(damage);
                    killListener.onKill(this, false);
                    System.out.println("Enemy hit target!");
                }
            }
        }
    }

    @Override
    public void takeDamage(float damage) {
        health -= damage;
        if (health <= 0) {
            killListener.onKill(this, true); // Notificar que el enemigo ha sido destruido
        } else {
            killListener.onTriggered(); // Otras acciones al recibir daño
        }
    }

    @Override
    public boolean isAlive() {
        return health > 0;
    }

    public void draw(SpriteBatch spriteBatch) {
        spriteBatch.draw(shipRegion, position.x, position.y);

        for (Bullet bullet : bullets)
        {
            bullet.draw(spriteBatch);
        }
    }
}
