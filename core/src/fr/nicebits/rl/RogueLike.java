package fr.nicebits.rl;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

public class RogueLike extends Game {

    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;
    private BitmapFont font;

    private World world;

    private Body ground;
    private Body object;

    private OrthographicCamera cam;
    private Box2DDebugRenderer debugRenderer;

    @Override
	public void create () {

        /* *** INIT *** */

        Gdx.app.setLogLevel(Application.LOG_DEBUG);
        Box2D.init();

        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();


        /* *** FONTS *** */

        font = new BitmapFont();

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/TubeOfCorn.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter param = new FreeTypeFontGenerator.FreeTypeFontParameter();

        param.size = 24;
        param.color = Color.GRAY;

        font = generator.generateFont(param);
        generator.dispose();


        /* *** WORLD *** */

        world = new World(new Vector2(0f, -9.81f) /* Applied force */, true);


        /* *** GROUND *** */

        BodyDef groundBodyDef = new BodyDef();
        groundBodyDef.type = BodyDef.BodyType.StaticBody;
        groundBodyDef.position.set(0, 150);

        ground = world.createBody(groundBodyDef);

        PolygonShape groundBox = new PolygonShape();
        groundBox.setAsBox(800, 10);
        ground.createFixture(groundBox, 0.0f);

        groundBox.dispose();


        /* *** OBJECT *** */

        BodyDef objectBodyDef = new BodyDef();
        objectBodyDef.type = BodyDef.BodyType.DynamicBody;
        objectBodyDef.position.set(800, 400);

        object = world.createBody(objectBodyDef);

        CircleShape circle = new CircleShape();
        circle.setRadius(25);

        FixtureDef objectFixtureDef = new FixtureDef();

        objectFixtureDef.shape = circle;
        objectFixtureDef.density = 0.5f;

        Fixture fixture = object.createFixture(objectFixtureDef);

        object.getMassData().mass = 50;

        circle.dispose();


        /* *** DEBUG *** */

        cam = new OrthographicCamera(30, 30 * (Gdx.graphics.getHeight() / Gdx.graphics.getWidth()));

        cam.position.set(cam.viewportWidth / 2f, cam.viewportHeight / 2f, 0);
        cam.update();

        debugRenderer = new Box2DDebugRenderer();

    }

	@Override
	public void render () {
        super.render();

        Gdx.gl.glClearColor(.8f, .8f, .8f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT
                | (Gdx.graphics.getBufferFormat().coverageSampling ? GL20.GL_COVERAGE_BUFFER_BIT_NV : 0));

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.FIREBRICK);
        shapeRenderer.ellipse(object.getPosition().x - 25, object.getPosition().y - 25, 50, 50);
        shapeRenderer.setColor(Color.CHARTREUSE);
        shapeRenderer.rect(ground.getPosition().x, ground.getPosition().y, 800, 10);
        shapeRenderer.end();

        batch.begin();
        font.draw(batch, "FPS: " + Gdx.graphics.getFramesPerSecond(), 10, Gdx.graphics.getHeight() - 10);
        batch.end();

        world.step(1/20f, 6, 2);
        debugRenderer.render(world, cam.combined);

        Vector2 pos = object.getPosition();
        Vector2 vel = object.getLinearVelocity();

        if (Gdx.input.isKeyPressed(Input.Keys.Q)) {
            object.applyForceToCenter(-50f, 0, true);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            object.applyForceToCenter(50f, 0, true);
        }

	}
	
	@Override
	public void dispose () {
		super.dispose();

		batch.dispose();
	}
}
