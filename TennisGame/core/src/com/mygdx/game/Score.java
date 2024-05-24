package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

public class Score {

    private final BitmapFont scoreFont;
    private final BitmapFont gameOverFont;
    private String gameOverText = "";
    private int scoreLeft;
    private int scoreRight;
    private final int TOTAL_POINTS = 7;
    private final int POINTS_DIFFERENCE= 2;

    public Score(){

        // Se asigna una fuente, tamaño
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/roboto-regular.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 12;
        scoreFont = generator.generateFont(parameter); // font size 12 pixels
        gameOverFont = generator.generateFont(parameter);
        generator.dispose(); // don't forget to dispose to avoid memory leaks!

        configureFont(scoreFont,0.2f, 0.2f);

        configureFont(gameOverFont,0.5f, 0.5f);

    }

    // Se configura la fuente, tanto el tamaño como el color
    public void configureFont(BitmapFont font, float scaleX, float scaleY){

        font.setUseIntegerPositions(false);
        font.getRegion().getTexture().setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        font.getData().setScale(scaleX, scaleY);
        font.setColor(Color.WHITE);
    }

    // Incrementa el puntaje de la izquierda
    public void increaseScoreLeft(){

        scoreLeft = scoreLeft + 1;
    }

    // Incrementa el puntaje de la derecha
    public void increaseScoreRight(){

        scoreRight = scoreRight + 1;
    }

    // Termina el juego si uno de los jugadores llega a 15 pts y/o la diferencia es de 2 pts después de llegar a 15pts
    public boolean gameOver() {
        // si un jugador tiene 15 pts y la diferencia de pts es de 2, entonces ha ganado
        if(scoreLeft >= TOTAL_POINTS && scoreRight <= scoreLeft - POINTS_DIFFERENCE){
            gameOverText = "Left player wins";
            return true;
        } else if (scoreRight >= TOTAL_POINTS && scoreLeft <= scoreRight - POINTS_DIFFERENCE) {
            gameOverText = "Right player wins";
            return true;
        }

        return false;
    }

    // Dibujar el scoreFont y el gameOverFont
    public void draw(SpriteBatch batch) {

        scoreFont.draw(batch, String.valueOf(scoreLeft),2 , 4);
        scoreFont.draw(batch, String.valueOf(scoreRight),60 , 4);

        gameOverFont.draw(batch, gameOverText, 10, 20);
    }
    public void dispose() {

        scoreFont.dispose();
    }

    // Se reinicia el juego otra vez
    public void reset() {
        scoreRight = 0;
        scoreLeft = 0;
        gameOverText = "";
    }
}
