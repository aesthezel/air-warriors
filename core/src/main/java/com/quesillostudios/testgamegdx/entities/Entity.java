package com.quesillostudios.testgamegdx.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.quesillostudios.testgamegdx.entities.interfaces.Damagable;

import java.util.ArrayList;

public abstract class Entity {
    protected Vector2 position;
    protected float speed;

    public Entity(float x, float y, float speed) {
        this.position = new Vector2(x, y);
        this.speed = speed;
    }

    public Vector2 getPosition() {
        return position;
    }

    public void setPosition(float x, float y) {
        this.position.set(x, y);
    }

    public float getSpeed() {
        return speed;
    }

    // Método abstracto para obtener el rectángulo de colisión
    public abstract Rectangle getBounds();

    public abstract void update(float delta, ArrayList<Damagable> targets);
    public abstract void draw(SpriteBatch spriteBatch);
}
