package com.quesillostudios.testgamegdx.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class BackgroundLayer extends Layer {
    private TextureRegion[][] tiles;
    private boolean fill;

    public BackgroundLayer(int[][] layerData, TextureRegion[][] tiles, boolean fill) {
        super(layerData);
        this.tiles = tiles;
        this.fill = fill;
    }

    @Override
    public void draw(SpriteBatch spriteBatch, int tileWidth, int tileHeight) {
        int tilesX = layerPattern[0].length;  // Ancho en tiles, basado en la capa original
        int tilesY = layerPattern.length;     // Alto en tiles, basado en la capa original

        if (fill) {
            tilesX = (int) Math.ceil((float) Gdx.graphics.getWidth() / tileWidth);
            tilesY = (int) Math.ceil((float) Gdx.graphics.getHeight() / tileHeight);
        }

        // Dibuja cada tile según las dimensiones ajustadas
        for (int row = 0; row < tilesY; row++) {
            for (int col = 0; col < tilesX; col++) {
                // Calcular el índice del tile, asegurando que se repita si es necesario
                int tileIndex = layerPattern[row % layerPattern.length][col % layerPattern[0].length];

                // Dibuja solo si el índice es válido (por ejemplo, distinto de 0 para "sin tile")
                if (tileIndex != 0) {
                    float x = col * tileWidth;
                    float y = (tilesY - row - 1) * tileHeight;

                    // Dibuja el tile usando los valores de posición y tamaño
                    spriteBatch.draw(tiles[tileIndex / tiles[0].length][tileIndex % tiles[0].length], x, y, tileWidth, tileHeight);
                }
            }
        }
    }
}
