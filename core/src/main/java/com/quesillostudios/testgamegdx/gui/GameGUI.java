package com.quesillostudios.testgamegdx.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class GameGUI {
    protected Stage stage;
    protected Skin skin;
    protected Window window;

    public GameGUI(Vector2 viewport, Vector2 size, String title, String styleName)
    {
        stage = new Stage(new FitViewport(viewport.x, viewport.y));
        skin = new Skin(Gdx.files.internal("ui/uiskin.json"));
        window = new Window(title, skin, styleName);
        window.setSize(size.x, size.y);
        window.defaults().pad(10f);
    }

    public GameGUI(Vector2 viewport, Vector2 size, String title)
    {
        stage = new Stage(new FitViewport(viewport.x, viewport.y));
        skin = new Skin(Gdx.files.internal("ui/uiskin.json"));
        window = new Window(title, skin);
        window.setSize(size.x, size.y);
        window.defaults().pad(10f);
    }

    public void draw()
    {
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    public void resize(int width, int height)
    {
        stage.getViewport().update(width, height);
    }

    public void dispose()
    {
        stage.dispose();
        skin.dispose();
    }

    public Stage getStage() {
        return stage;
    }

}
