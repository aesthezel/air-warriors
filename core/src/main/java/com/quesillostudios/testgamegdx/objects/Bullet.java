package com.quesillostudios.testgamegdx.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Bullet {
    private Vector2 position;
    private float speed;
    private float damage;
    private TextureRegion bulletRegion;

    public Bullet(Texture bulletTexture, float x, float y, float damage, float speed, float direction) {
        this.position = new Vector2(x, y);
        this.speed = speed * direction; // Velocidad de la bala
        this.bulletRegion = new TextureRegion(bulletTexture, 0, 0, 16, 16); // Cambia la región según tu tileset
        this.damage = damage;
    }

    public void update(float delta) {
        position.y += speed * delta; // La bala se mueve hacia arriba
    }

    public void draw(SpriteBatch spriteBatch) {
        spriteBatch.draw(bulletRegion, position.x, position.y);
    }

    public Vector2 getPosition() {
        return position;
    }

    public float getDamage() {
        return damage;
    }

    public boolean isOffScreen() {
        return position.y > Gdx.graphics.getHeight();
    }

    public Rectangle getBounds() {
        return new Rectangle(position.x, position.y, bulletRegion.getRegionWidth(), bulletRegion.getRegionHeight());
    }
}
