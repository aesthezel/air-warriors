package com.quesillostudios.testgamegdx;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.quesillostudios.testgamegdx.debug.DebugGUI;
import com.quesillostudios.testgamegdx.entities.EnemyShip;
import com.quesillostudios.testgamegdx.entities.Entity;
import com.quesillostudios.testgamegdx.entities.Ship;
import com.quesillostudios.testgamegdx.entities.interfaces.Damagable;
import com.quesillostudios.testgamegdx.gui.ScoreGUI;
import com.quesillostudios.testgamegdx.utils.FileUtils;
import com.quesillostudios.testgamegdx.utils.interfaces.EnemyEventListener;
import com.quesillostudios.testgamegdx.utils.interfaces.EventListener;
import com.quesillostudios.testgamegdx.world.Scene;

import java.util.ArrayList;
import java.util.Iterator;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter implements EnemyEventListener {
    private Stage stage;
    private Skin skin;

    // Configure Player
    private Texture shipsTexture;
    private Ship playerShip;

    private SpriteBatch spriteBatch;
    private Texture backgroundTexture;

    private Scene currentScene;

    private ArrayList<Damagable> targets;
    private ArrayList<Damagable> players;
    private ArrayList<Damagable> damagablesOnPendingRemoval;

    // UI
    private ScoreGUI scoreGUI;
    private DebugGUI debugGUI;

    // Own Methods
    private void drawGUI(Stage stage, Skin skin) {
        // Window GUI
        Vector2 viewportSize = new Vector2(1280, 720);

        scoreGUI = new ScoreGUI(viewportSize, new Vector2(400, 50));
        debugGUI = new DebugGUI(viewportSize, new Vector2(500, 500),currentScene, playerShip);
        Gdx.input.setInputProcessor(debugGUI.getStage());

        Window window = new Window("Welcome to this alpha of Air Warriors!", skin, "border");
        window.defaults().pad(4f);
        window.add("Controls").row();
        window.add("Movement: W/A/S/D").row();
        window.add("Shoot: [SPACE]").row();
        final TextButton button = new TextButton("LET'S PLAY", skin);
        button.pad(8f);
        button.addListener(new ChangeListener() {
            @Override
            public void changed(final ChangeEvent event, final Actor actor) {
                window.remove();
            }
        });
        button.addAction(Actions.sequence(Actions.alpha(0f), Actions.fadeIn(1f), Actions.rotateBy(360f, 2f)));

        window.add(button);
        window.pack();
        window.setPosition(MathUtils.roundPositive(stage.getWidth() / 2f - window.getWidth() / 2f),
            MathUtils.roundPositive(stage.getHeight() / 2f - window.getHeight() / 2f));
        window.addAction(Actions.sequence(Actions.alpha(0f), Actions.fadeIn(1f)));
        stage.addActor(window);
        Gdx.input.setInputProcessor(stage);
    }

    // LibGDX Methods
    @Override
    public void create() {
        spriteBatch = new SpriteBatch();

        // SCENE MAKER
        backgroundTexture = new Texture(FileUtils.GetInternalPath("tilesets/tiles_packed.png"));
        int[][] backgroundPattern = {{50}};
        int[][] decorationPattern = {
            {48, 48, 48, 48},
        };

        currentScene = new Scene("Main Scene", 640, 480, backgroundPattern, decorationPattern, backgroundTexture);
        // END SCENE MAKER

        // SETUP PLAYER
        shipsTexture = new Texture(FileUtils.GetInternalPath("tilesets/ships_packed.png"));
        Vector2 playerPosition = new Vector2(0f, 0f);
        Vector2 playerSpriteSector = new Vector2(1, 1);
        playerShip = new Ship(playerPosition, 300f, playerSpriteSector, shipsTexture, backgroundTexture, "audio/pepSound2.ogg", "audio/twoTone1.ogg");
        players = new ArrayList<>();
        players.add(playerShip);

        // SETUP ENEMIES
        Vector2 enemyPosition = new Vector2(Gdx.graphics.getWidth() / 2, (Gdx.graphics.getHeight() / 2f) + 50f);
        Vector2 enemySpriteSector = new Vector2(1, 5);
        EnemyShip enemyShip = new EnemyShip(enemyPosition, 200f, enemySpriteSector, shipsTexture, backgroundTexture, "audio/pepSound1.ogg", "audio/tone1.ogg", this);
        targets = new ArrayList<>();
        targets.add(enemyShip);

        damagablesOnPendingRemoval = new ArrayList<>();

        stage = new Stage(new FitViewport(1280, 720));
        skin = new Skin(Gdx.files.internal("ui/uiskin.json"));
        drawGUI(stage, skin);
    }

    @Override
    public void render() {
        ScreenUtils.clear(0f, 0f, 0f, 1f);

        // Actualiza el jugador y pasa la lista de objetivos
        playerShip.update(Gdx.graphics.getDeltaTime(), targets);

        for(Damagable target : targets)
        {
            Entity enemy = (Entity) target;
            enemy.update(Gdx.graphics.getDeltaTime(), players);
        }

        spriteBatch.begin();
        int tileWidth = 16;
        int tileHeight = 16;
        currentScene.drawLayers(spriteBatch, tileWidth, tileHeight);
        spriteBatch.end();

        // Draw calls
        spriteBatch.begin();
        playerShip.draw(spriteBatch);

        for(Damagable target : targets)
        {
            Entity enemy = (Entity) target;
            enemy.draw(spriteBatch);
        }

        spriteBatch.end();

        // Safe to remove
        for (Damagable enemy : damagablesOnPendingRemoval) {
            targets.remove(enemy);
        }
        damagablesOnPendingRemoval.clear();

        // Scene 2D Actors
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();

        scoreGUI.draw();

        // DEBUG
        debugGUI.update(playerShip);
        debugGUI.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height);
        scoreGUI.resize(width, height);
        debugGUI.resize(width, height);
    }

    @Override
    public void dispose() {
        scoreGUI.dispose();
        debugGUI.dispose();
        stage.dispose();
        skin.dispose();
        shipsTexture.dispose();
        backgroundTexture.dispose();
        spriteBatch.dispose();
    }

    @Override
    public void onKill(Damagable enemy, boolean bonus) {
        damagablesOnPendingRemoval.add(enemy);
        if(bonus)
            scoreGUI.addScore(10);
        else
            scoreGUI.addScore(-5);
    }

    @Override
    public void onTriggered() {
        scoreGUI.addScore(1);
    }
}
