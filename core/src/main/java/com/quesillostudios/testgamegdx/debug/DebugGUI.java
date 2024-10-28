package com.quesillostudios.testgamegdx.debug;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.quesillostudios.testgamegdx.entities.Ship;
import com.quesillostudios.testgamegdx.gui.GameGUI;
import com.quesillostudios.testgamegdx.world.Scene;

public class DebugGUI extends GameGUI {
    private Label sceneNameLabel;
    private Label positionLabel;
    private Label speedLabel;

    public DebugGUI(Vector2 viewport, Vector2 size, Scene scene, Ship playerShip) {
        super(viewport, size, "Debug Info", "border");

        sceneNameLabel = new Label("Scene: " + scene.getName(), skin);
        positionLabel = new Label(String.format(java.util.Locale.US, "Position: %.1f, %.1f", playerShip.getPosition().x, playerShip.getPosition().y), skin);
        speedLabel = new Label(String.format(java.util.Locale.US, "Speed: %.1f", playerShip.getSpeed()), skin);

        window.add(sceneNameLabel).row();
        window.add(positionLabel).row();
        window.add(speedLabel).row();
        window.pack();

        window.setPosition(Gdx.graphics.getWidth() - window.getWidth() - 10, Gdx.graphics.getHeight() - window.getHeight() - 10);

        stage.addActor(window);
    }

    public void update(Ship playerShip) {
        // Actualiza la información de depuración en cada frame
        positionLabel.setText(String.format(java.util.Locale.US, "Position: %.1f, %.1f", playerShip.getPosition().x, playerShip.getPosition().y));
        speedLabel.setText(String.format(java.util.Locale.US, "Speed: %.1f", playerShip.getSpeed()));
    }

    public Stage getStage() {
        return stage;
    }
}
