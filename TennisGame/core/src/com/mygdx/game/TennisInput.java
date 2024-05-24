package com.mygdx.game;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector3;

/**
 * Gestiona eventos de los jugadores
 */
public class TennisInput implements InputProcessor {
    private Camera camera;
    private RacketRight racketRight;
    private RacketLeft racketLeft;
    private Score score;
    private TennisGame tennisGame;

    public TennisInput(Camera camera, RacketRight racketRight, RacketLeft racketLeft, Score score, TennisGame tennisGame) {

        this.camera = camera;
        this.racketRight = racketRight;
        this.racketLeft = racketLeft;
        this.score = score;
        this.tennisGame = tennisGame;
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {

        // Al presionar la pantalla, se reinicia el juego cuando hay un ganador
        if (score.gameOver()){
            tennisGame.startGame();
        }

        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        // cambia la posición de pixeles al área de la cámara
        Vector3 positionInCamera = camera.unproject(new Vector3(screenX,screenY,0));

        racketLeft.move(positionInCamera.x, positionInCamera.y);
        racketRight.move(positionInCamera.x, positionInCamera.y);


        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }
}
