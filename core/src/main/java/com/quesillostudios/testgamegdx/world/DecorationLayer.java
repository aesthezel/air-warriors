package com.quesillostudios.testgamegdx.world;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class DecorationLayer extends Layer {
    private TextureRegion[][] tiles;

    public DecorationLayer(int[][] layerData, TextureRegion[][] tiles) {
        super(layerData);
        this.tiles = tiles;
    }

    @Override
    public void draw(SpriteBatch spriteBatch, int tileWidth, int tileHeight) {

        // Dibuja cada tile según las dimensiones ajustadas
        for (int row = 0; row < layerPattern.length; row++) {
            for (int col = 0; col < layerPattern[0].length; col++) {
                // Calcular el índice del tile, asegurando que se repita si es necesario
                int tileIndex = layerPattern[row % layerPattern.length][col % layerPattern[0].length];

                // Dibuja solo si el índice es válido (por ejemplo, distinto de 0 para "sin tile")
                if (tileIndex != 0) {
                    float x = col * tileWidth;
                    float y = (layerPattern.length - row - 1) * tileHeight;

                    // Dibuja el tile usando los valores de posición y tamaño
                    spriteBatch.draw(tiles[tileIndex / tiles[0].length][tileIndex % tiles[0].length], x, y, tileWidth, tileHeight);
                }
            }
        }
    }
}
