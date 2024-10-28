package com.quesillostudios.testgamegdx.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.quesillostudios.testgamegdx.entities.interfaces.Damagable;
import com.quesillostudios.testgamegdx.objects.Bullet;

import java.util.ArrayList;
import java.util.Iterator;

public class Ship extends Entity implements Damagable
{
    private TextureRegion shipRegion;
    private Texture bulletTexture;
    private ArrayList<Bullet> bullets;

    private Rectangle bounds;

    public Ship(Texture shipTexture, Texture bulletTexture) {
        super(0, 0, 200f);
        this.shipRegion = new TextureRegion(shipTexture, 0, 0, 32, 32);
        this.bulletTexture = bulletTexture;
        this.bullets = new ArrayList<>();
        this.bounds = new Rectangle(0, 0, shipRegion.getRegionWidth(), shipRegion.getRegionHeight());
    }

    private void move(float delta)
    {
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            position.y += speed * delta;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            position.y -= speed * delta;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            position.x -= speed * delta;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            position.x += speed * delta;
        }

        bounds.setPosition(position.x, position.y);
    }

    // TODO: create a delay to shoot again...
    private void shoot()
    {
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE))
        {
            Vector2 bulletPosition = new Vector2((position.x + shipRegion.getRegionWidth() / 2) - 8, (position.y + shipRegion.getRegionHeight() - 16));
            Bullet bullet = new Bullet(bulletTexture, bulletPosition.x, bulletPosition.y, 1f, 500f, 1);
            bullets.add(bullet);
        }
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

    private void boundLimits()
    {
        position.x = MathUtils.clamp(position.x, 0, Gdx.graphics.getWidth() - shipRegion.getRegionWidth());
        position.y = MathUtils.clamp(position.y, 0, Gdx.graphics.getHeight() - shipRegion.getRegionHeight());
    }

    @Override
    public Rectangle getBounds() {
        return bounds;
    }

    @Override
    public void update(float delta, ArrayList<Damagable> targets)
    {
        move(delta);
        boundLimits();

        shoot();
        controlBullets(delta, targets);
    }

    @Override
    public void draw(SpriteBatch spriteBatch)
    {
        spriteBatch.draw(shipRegion, position.x, position.y);

        // Draw bullet
        for (Bullet bullet : bullets)
        {
            bullet.draw(spriteBatch);
        }
    }

    @Override
    public void takeDamage(float damage) {

    }

    @Override
    public boolean isAlive() {
        return true;
    }
}
