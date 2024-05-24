package com.mygdx.game;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Gestiona el movimiento de la raqueta izquierda
 */
public class RacketLeft {

    private final Sprite sprite;
    private final Rectangle gameArea;
    private final World world;

    private Body body;

    public RacketLeft(Texture texture, Rectangle gameArea, World world){

        this.gameArea = gameArea;
        this.world = world;

        sprite = new Sprite(texture, 0, 0, texture.getWidth(), texture.getHeight());
        sprite.setSize(0.5f, 5);
        float racketOffset = 5f;
        sprite.setPosition(0 + racketOffset - sprite.getWidth(), gameArea.y + gameArea.getHeight() / 2 - sprite.getHeight() / 2);
        createBody();
    }

    private void createBody(){
        //crear la definición de un body
        BodyDef bodyDef = new BodyDef();
        //establecer la posición de su mundo
        bodyDef.position.set(sprite.getX() + sprite.getWidth() / 2, sprite.getY() + sprite.getHeight() / 2);

        //crear un body desde la definición y añadirlo al mundo
        body = world.createBody(bodyDef);


        //crear un poligon shape
        PolygonShape box = new PolygonShape();
        //establecer la forma del poligono como forma de caja 2D. (setAsBox toma como parámetros la mitad del ancho y alto)
        box.setAsBox(sprite.getWidth() / 2 , sprite.getHeight() / 2 );
        //crear un fixture desde la forma de nuestro polígono y agregarlo al body
        body.createFixture(box, 0.0f);
        //eliminar lo que no usamos más
        box.dispose();

    }

    // Actualiza la posición del body en base al sprite
    private void updatePosition() {
        body.setTransform(sprite.getX() + sprite.getWidth() / 2, sprite.getY() + sprite.getHeight() / 2, 0);
    }

    public void draw(SpriteBatch batch) {
        sprite.draw(batch);
    }

    // Se establece el movimiento de la raqueta
    public void move(float positionX, float positionY) {
        if(sprite.getX() + sprite.getWidth() > positionX){
            sprite.setY(positionY - sprite.getHeight() / 2);

            // la parte baja de la raqueta debe chocar con el área de juego
            if(sprite.getY() < gameArea.y) {
                sprite.setY(gameArea.y);
            }

            // la parte alta de la raqueta debe chocar con el área de juego
            if (sprite.getY() + sprite.getHeight() > gameArea.y + gameArea.getHeight()){
                sprite.setY(gameArea.y + gameArea.getHeight() - sprite.getHeight());
            }

            updatePosition();
        };

    }
    public void dispose() {
        sprite.getTexture().dispose();
    }
}
