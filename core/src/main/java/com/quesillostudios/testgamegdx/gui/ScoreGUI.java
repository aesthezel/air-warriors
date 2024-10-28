package com.quesillostudios.testgamegdx.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

public class ScoreGUI extends GameGUI {
    private Label pointLabel;
    private int score;

    public ScoreGUI(Vector2 viewport, Vector2 size) {
        super(viewport, size, "");

        score = 0;
        pointLabel = new Label(String.format("Points: %d", score), skin);

        this.window.add(pointLabel).row();
        this.window.pack();
        this.window.setPosition((Gdx.graphics.getWidth() / 2) - 30f, Gdx.graphics.getHeight());
        this.stage.addActor(window);
    }

    public void addScore(int amount) {
        score = MathUtils.clamp(score + amount, 0, Integer.MAX_VALUE);
        pointLabel.setText(String.format("Points: %d", score));
    }
}
