package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class TennisGame extends ApplicationAdapter {

	private final float TICK_RATE = 60;//Number of updates per second
	private final float TIME_STEP = 1f / TICK_RATE;//Seconds per tick
	private final int VELOCITY_ITERATIONS = 6;
	private final int POSITION_ITERATIONS = 2;
	private SpriteBatch batch;
	private RacketRight racketRight;
	private RacketLeft racketLeft;
	private Ball ball;
	private Score score;
	private ShapeRenderer sr;
	private OrthographicCamera camera;
	private Rectangle gameArea;
	private TennisInput tennisInput;
	private World world;
	private Box2DDebugRenderer debugRenderer;
	private float accumulator = 0;
	private static final float viewportScale = 64;

	@Override
	public void create () {

		float screenWidth = Gdx.graphics.getWidth();
		float screenHeight = Gdx.graphics.getHeight();


		camera = new OrthographicCamera(viewportScale, viewportScale * (screenHeight / screenWidth));
		camera.position.set( camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0);

		camera.update();

		sr = new ShapeRenderer();

		float pointsAreaHeight = camera.viewportHeight * 0.2f;
		gameArea = new Rectangle(0, pointsAreaHeight, camera.viewportWidth, camera.viewportHeight - pointsAreaHeight);

		world = new World(Vector2.Zero,true);
		debugRenderer = new Box2DDebugRenderer();

		batch = new SpriteBatch();
		Texture img = new Texture("img/paleta.png");
		racketRight = new RacketRight(img, gameArea, world);
		racketLeft = new RacketLeft(img, gameArea, world);

		Texture textureBall = new Texture("img/bola.png");
		ball = new Ball(textureBall, gameArea, world);

		score = new Score();

		tennisInput = new TennisInput(camera, racketRight, racketLeft, score, this);

		// Crear el box de colisión para que el body colisione en la parte superior e inferior del área de juego
		createCollisionBox(new Vector2(0,gameArea.y - 2f));
		createCollisionBox(new Vector2(0,gameArea.y + gameArea.getHeight() + 2f));

		Gdx.input.setInputProcessor(tennisInput);

		startGame();
	}

	@Override
	public void render () {

		camera.update();
		batch.setProjectionMatrix(camera.combined);

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		drawBackground();

		batch.begin();

		racketRight.draw(batch);
		racketLeft.draw(batch);
		ball.draw(batch);
		score.draw(batch);
		batch.end();

		//debugRenderer.render(world, camera.combined);
		doPhysicsStep(Gdx.graphics.getDeltaTime());
	}
	public void startGame() {
		score.reset();
		ball.startMovement();
	}

	// Actualiza la fisica una vez cada TIME_STEP (60 veces por segundo)
	//https://gamedev.stackexchange.com/questions/184610/fixed-timestep-updates-per-second-keeps-changing
	private void doPhysicsStep (float deltaTime){
		//to avoid freezing on slow devices
		float frameTime = Math.min(deltaTime, 0.25f);
		accumulator += frameTime;
		while (accumulator >= TIME_STEP){
			checkBallOut();

			world.step(TIME_STEP, VELOCITY_ITERATIONS, POSITION_ITERATIONS);
			accumulator -= TIME_STEP;
		}
	}

	// Checkear si la bola está fuera del área de juego
	private void checkBallOut() {
		// si la bola se fue, está fuera del área de juego

		// la posición de la bola es menor a la posición de la x del área de juego
		if(ball.getPosition().x < gameArea.x){
			ball.resetPosition();
			score.increaseScoreRight();
		}
		// la posición de la bola es mayor a la posición x del área de juego
		if (ball.getPosition().x > gameArea.getWidth()){
			ball.resetPosition();
			score.increaseScoreLeft();
		}

		// Si el juego se termina, la bola para su movimiento
		if (score.gameOver()) {
			ball.stopMovement();
		}

	}

	// Crea un body para que se pueda dar la colisión con los bordes del juego
	private void createCollisionBox(Vector2 position){
		//crear la definición de un body
		BodyDef bodyDef = new BodyDef();
		//establecer la posición de su mundo
		bodyDef.position.set(position);

		//crear un body desde la definición y añadirlo al mundo
		Body body = world.createBody(bodyDef);

		//crear un poligon shape
		PolygonShape box = new PolygonShape();
		//establecer la forma del poligono como forma de caja 2D. (setAsBox toma como parámetros la mitad del ancho y alto)
		box.setAsBox(camera.viewportWidth, 2f);
		//crear un fixture desde la forma de nuestro polígono y agregarlo al body
		body.createFixture(box, 0.0f);
		//eliminar lo que no usamos más
		box.dispose();
	}

	private void drawBackground() {
		float lineThickness = 12f;
		float halfThickness = lineThickness / 2.0f;

		float screenHeight = Gdx.graphics.getHeight();
		float screenWidth = Gdx.graphics.getWidth();


		sr.begin(ShapeRenderer.ShapeType.Filled);
		sr.setColor(1, 1, 1, 1);

		//vertical-izquierda
		sr.rectLine(new Vector2(0 + halfThickness, 0), new Vector2(0 + halfThickness, screenHeight), lineThickness);

		//vertical-derecha
		sr.rectLine(new Vector2(screenWidth - halfThickness, 0), new Vector2(screenWidth - halfThickness, screenHeight), lineThickness);

		//vertical-medio
		sr.rectLine(new Vector2(screenWidth / 2.0f, screenHeight * 0.2f), new Vector2(screenWidth  / 2.0f, screenHeight), lineThickness);

		//horizontal-abajo
		sr.rectLine(new Vector2(0, 0 + halfThickness), new Vector2(screenWidth, 0 + halfThickness), lineThickness);

		//horizontal-arriba
		sr.rectLine(new Vector2(0, screenHeight - halfThickness), new Vector2(screenWidth, screenHeight - halfThickness), lineThickness);

		//horizontal-medio
		sr.rectLine(new Vector2(0, screenHeight * 0.2f), new Vector2(screenWidth, screenHeight * 0.2f), lineThickness);

		sr.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		racketRight.dispose();
		racketLeft.dispose();
		ball.dispose();
		score.dispose();
	}
}
