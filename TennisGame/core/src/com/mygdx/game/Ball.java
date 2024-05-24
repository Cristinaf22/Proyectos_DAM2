package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

import java.util.Random;

/**
 * Gestiona la creación, movimientos, posición de la bola
 */
public class Ball {
    private final Sprite sprite;
    private final int BALL_ANGLE = 35;
    private final int INITIAL_BALL_VELOCITY = 300;
    private Body body;
    private final Rectangle gameArea;
    private final World world;
    public Ball(Texture texture, Rectangle gameArea, World world){

        this.gameArea = gameArea;
        this.world = world;

        sprite = new Sprite(texture, 0, 0, texture.getWidth(), texture.getHeight());
        sprite.setSize(1, 1);
        createBody();
        updatePosition();
    }

    // Crea el cuerpo de la bola que se encarga de la física (chocar, rebotar, etc...)
    private void createBody(){
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(gameArea.getWidth() / 2, gameArea.y + gameArea.getHeight() / 2);

        body = world.createBody(bodyDef);
        body.setFixedRotation(true);

        CircleShape circle = new CircleShape();
        circle.setRadius(.5f);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circle;
        fixtureDef.density = .5f;
        fixtureDef.friction = 0f;
        fixtureDef.restitution = 1f;

        Fixture fixture = body.createFixture(fixtureDef);
        circle.dispose();
    }

    // Actualiza la posición del sprite según la posición del cuerpo
    private void updatePosition() {
        sprite.setPosition(body.getPosition().x - sprite.getWidth() / 2, body.getPosition().y - sprite.getHeight() / 2);
    }

    // Inicia el movimiento de la bola que es random, también se indica el ángulo
    public void startMovement() {
        boolean direction = new Random().nextBoolean();
        //int angle = new Random().nextInt();
        int degrees = direction ? BALL_ANGLE: -BALL_ANGLE; // cambiar el ángulo de la bola y que haga un ángulo de 45 al salir
        body.applyForceToCenter(new Vector2(0,INITIAL_BALL_VELOCITY).rotateDeg(degrees), true);
    }

    // Para el movimiento de la bola
    public void stopMovement(){
        body.setAwake(false);
    }

    // Obtiene la posición del sprite
    public Vector2 getPosition(){
        return new Vector2(sprite.getX(), sprite.getY());
    }

    // Reinicia la posición de la bola
    public void resetPosition() {
        // poner la bola al centro
        body.setTransform(gameArea.x + gameArea.getWidth() / 2 , gameArea.y + gameArea.getHeight() / 2, 0);
        body.setLinearVelocity(Vector2.Zero);
        startMovement();

    }

    public void draw(SpriteBatch batch) {
        updatePosition();
        sprite.draw(batch);
    }
    public void dispose() {
        sprite.getTexture().dispose();
    }
}
