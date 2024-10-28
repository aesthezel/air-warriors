package com.quesillostudios.testgamegdx.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.quesillostudios.testgamegdx.entities.interfaces.Damagable;
import com.quesillostudios.testgamegdx.objects.Bullet;
import com.quesillostudios.testgamegdx.utils.interfaces.EnemyEventListener;

import java.util.ArrayList;

public class EnemyShip extends Entity implements Damagable {

    private float health;
    private float damage;
    private TextureRegion shipRegion; // Textura del enemigo
    private Texture bulletTexture;
    private Rectangle bounds; // Área de colisión
    private EnemyEventListener killListener;

    private float direction = 1; // 1 = derecha, -1 = izquierda
    private float fireCooldown = 1.0f; // Tiempo entre disparos en segundos
    private float timeSinceLastShot = 0f; // Tiempo transcurrido desde el último disparo

    private ArrayList<Bullet> bullets; // Lista de balas

    public EnemyShip(float x, float y, float speed, Texture shipTexture, Texture bulletTexture, EnemyEventListener killListener) {
        super(x, y, speed);
        this.health = 3; // Salud inicial del enemigo
        this.damage = 10; // Daño que inflige
        this.shipRegion = new TextureRegion(shipTexture, 0, 128, 32, 32);
        this.bounds = new Rectangle(x, y, shipRegion.getRegionWidth(), shipRegion.getRegionHeight());
        this.bulletTexture = bulletTexture;
        this.killListener = killListener;
        this.bullets = new ArrayList<>();
        // Voltear la textura verticalmente para que apunte hacia abajo
        this.shipRegion.flip(false, true);
    }

    @Override
    public Rectangle getBounds() {
        return bounds; // Devuelve el área de colisión
    }

    @Override
    public void update(float delta, ArrayList<Damagable> targets) {
        move(delta); // Movimiento en zigzag descendente
        checkForCollisions(targets); // Verificar colisiones con otros objetivos
        shoot(delta); // Disparo automático
    }

    // Movimiento en zigzag descendente
    private void move(float delta) {
        position.x += direction * speed * delta; // Movimiento lateral

        // Cambiar de dirección y descender al llegar a los bordes de la pantalla
        if (position.x <= 0 || position.x + bounds.width >= Gdx.graphics.getWidth()) {
            direction *= -1; // Cambia de dirección
            position.y -= 20; // Desciende cada vez que cambia de dirección
        }

        bounds.setPosition(position.x, position.y); // Actualizar área de colisión
    }

    // Lógica de disparo automático
    private void shoot(float delta) {
        timeSinceLastShot += delta;
        if (timeSinceLastShot >= fireCooldown) {
            timeSinceLastShot = 0f;

            // Crear una nueva bala y añadirla al juego
            Vector2 bulletPosition = new Vector2(position.x + bounds.width / 2, position.y);
            Bullet bullet = new Bullet(bulletTexture, bulletPosition.x, bulletPosition.y, 1f); // Velocidad hacia abajo

            bullets.add(bullet);
        }
    }

    // Verificar colisiones con otros objetivos
    private void checkForCollisions(ArrayList<Damagable> targets) {
        for (Damagable target : targets) {
            if (target instanceof Entity) {
                Entity entity = (Entity) target;
                if (this.getBounds().overlaps(entity.getBounds())) {
                    target.takeDamage(damage); // Aplica daño al objetivo
                    System.out.println("Enemy hit target!");
                }
            }
        }
    }

    @Override
    public void takeDamage(float damage) {
        health -= damage;
        if (health <= 0) {
            killListener.onKill(this); // Notificar que el enemigo ha sido destruido
        } else {
            killListener.onTriggered(); // Otras acciones al recibir daño
        }
    }

    @Override
    public boolean isAlive() {
        return health > 0; // Devuelve verdadero si el enemigo aún está vivo
    }

    public void draw(SpriteBatch spriteBatch) {
        spriteBatch.draw(shipRegion, position.x, position.y);

        for (Bullet bullet : bullets)
        {
            bullet.draw(spriteBatch);
        }
    }
}
