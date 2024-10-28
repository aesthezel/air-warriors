package com.quesillostudios.testgamegdx.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;
import com.quesillostudios.testgamegdx.entities.interfaces.Damagable;
import com.quesillostudios.testgamegdx.objects.Bullet;
import com.quesillostudios.testgamegdx.utils.FileUtils;

import java.util.ArrayList;
import java.util.Iterator;

public class Ship extends Entity implements Damagable, Disposable {
    protected TextureRegion shipRegion;
    protected Texture bulletTexture;
    protected ArrayList<Bullet> bullets;

    protected Rectangle bounds;

    // Sonidos
    protected Sound hittedSound;
    protected Sound beatedSound;

    public Ship(Vector2 position, float speed, Vector2 spriteSector, Texture shipTexture, Texture bulletTexture, String hittedSoundFilePath, String beatedSoundFilePath) {
        super(position.x, position.y, speed);
        setSpriteBySector(shipTexture, spriteSector);

        this.bulletTexture = bulletTexture;
        this.bullets = new ArrayList<>();
        this.bounds = new Rectangle(position.x, position.y, shipRegion.getRegionWidth(), shipRegion.getRegionHeight()); // Ajusta según el tamaño de tu sprite
        this.hittedSound = Gdx.audio.newSound(FileUtils.GetInternalPath(hittedSoundFilePath));
        this.beatedSound = Gdx.audio.newSound(FileUtils.GetInternalPath(beatedSoundFilePath));
    }

    private void setSpriteBySector(Texture shipTexture, Vector2 sector) {
        int sectorWidth = 32;
        int sectorHeight = 32;
        int x = ((int)sector.x - 1) * sectorWidth;
        int y = ((int)sector.y - 1) * sectorHeight;
        this.shipRegion = new TextureRegion(shipTexture, x, y, sectorWidth, sectorHeight);
    }

    private void move(float delta) {
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

    private void shoot() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
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

    private void boundLimits() {
        position.x = MathUtils.clamp(position.x, 0, Gdx.graphics.getWidth() - shipRegion.getRegionWidth());
        position.y = MathUtils.clamp(position.y, 0, Gdx.graphics.getHeight() - shipRegion.getRegionHeight());
    }

    @Override
    public Rectangle getBounds() {
        return bounds;
    }

    @Override
    public void update(float delta, ArrayList<Damagable> targets) {
        move(delta);
        boundLimits();
        shoot();
        controlBullets(delta, targets);
    }

    @Override
    public void draw(SpriteBatch spriteBatch) {
        spriteBatch.draw(shipRegion, position.x, position.y);

        // Draw bullet
        for (Bullet bullet : bullets) {
            bullet.draw(spriteBatch);
        }
    }

    @Override
    public void takeDamage(float damage) {
        // Reproducir sonido de daño
        hittedSound.play(1f);
        // Implementar lógica para la salud
    }

    @Override
    public boolean isAlive() {
        return true; // Implementar lógica según tu juego
    }

    @Override
    public void dispose() {
        hittedSound.dispose();
        beatedSound.dispose();
    }
}
