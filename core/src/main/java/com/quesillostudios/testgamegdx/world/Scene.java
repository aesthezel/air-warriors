package com.quesillostudios.testgamegdx.world;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Scene {
    private BackgroundLayer backgroundLayer;
    private DecorationLayer decorationLayer;

    // Propiedades adicionales de la escena
    private String name;
    private int width;
    private int height;

    public Scene(String name, int width, int height, int[][] backgroundMap, int[][] decorationMap, Texture texture) {
        this.name = name;
        this.width = width;
        this.height = height;

        // Cargar las texturas para las capas
        TextureRegion[][] tiles = TextureRegion.split(texture, 16, 16);

        this.backgroundLayer = new BackgroundLayer(backgroundMap, tiles, true);
        this.decorationLayer = new DecorationLayer(decorationMap, tiles);
    }

    public void drawLayers(SpriteBatch spriteBatch, int tileWidth, int tileHeight) {
        backgroundLayer.draw(spriteBatch, tileWidth, tileHeight);
        decorationLayer.draw(spriteBatch, tileWidth, tileHeight);
    }

    // Getters para las propiedades de la escena
    public String getName() {
        return name;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
