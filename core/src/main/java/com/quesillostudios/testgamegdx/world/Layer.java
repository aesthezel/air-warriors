package com.quesillostudios.testgamegdx.world;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class Layer {
    protected int[][] layerPattern;

    public Layer(int[][] layerPattern) {
        this.layerPattern = layerPattern;
    }

    public abstract void draw(SpriteBatch spriteBatch, int tileWidth, int tileHeight);
}

