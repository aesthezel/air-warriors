package com.quesillostudios.testgamegdx.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;
import com.quesillostudios.testgamegdx.entities.interfaces.Damagable;
import com.quesillostudios.testgamegdx.objects.Bullet;
import com.quesillostudios.testgamegdx.utils.interfaces.EnemyEventListener;

import java.util.ArrayList;
import java.util.Iterator;

public class EnemyShip extends Ship implements Damagable, Disposable {

    private float health;
    private float damage;
    private EnemyEventListener killListener;

    private float direction = 1; // 1 = RIGHT, -1 = LEFT
    private float fireCooldown = 1.0f;
    private float timeSinceLastShot = 0f;

    public EnemyShip(Vector2 position, float speed, Vector2 spriteSector, Texture shipTexture, Texture bulletTexture, String hittedSoundFilePath, String beatedSoundFilePath, EnemyEventListener killListener) {
        super(position, speed, spriteSector, shipTexture, bulletTexture, hittedSoundFilePath, beatedSoundFilePath);
        this.health = 3;
        this.damage = 10;
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
        move(delta); // Mover el enemigo
        checkForCollisions(targets); // Verificar colisiones
        controlBullets(delta, targets); // Controlar las balas
        shoot(delta); // Disparar
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
                    kill(false);
                    System.out.println("Enemy hit target!");
                }
            }
        }
    }

    private void kill(boolean bonus) {
        beatedSound.play(1f);
        killListener.onKill(this, bonus);
    }

    private void hitted() {
        hittedSound.play(1f);
        killListener.onTriggered();
    }

    @Override
    public void takeDamage(float damage) {
        health -= damage;
        if (health <= 0) {
            kill(true);
        } else {
            hitted();
        }
    }

    @Override
    public boolean isAlive() {
        return health > 0;
    }

    @Override
    public void draw(SpriteBatch spriteBatch) {
        spriteBatch.draw(shipRegion, position.x, position.y);
        super.draw(spriteBatch); // Llama al método draw de Ship para dibujar balas
    }

    @Override
    public void dispose() {
        hittedSound.dispose();
        beatedSound.dispose();
    }
}
