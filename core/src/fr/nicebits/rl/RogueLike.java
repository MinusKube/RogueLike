package fr.nicebits.rl;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import java.text.DecimalFormat;

public class RogueLike extends Game {

    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;
    private BitmapFont font;

    private World world;

    private Body ground;
    private Body object;

    //private OrthographicCamera cam;
    //private Box2DDebugRenderer debugRenderer;

    @Override
	public void create() {

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

        world = new World(new Vector2(0f, -10) /* Applied force */, true);


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
        objectFixtureDef.density = 1f;
        objectFixtureDef.friction = 1f;
        objectFixtureDef.restitution = 1f;

        Fixture fixture = object.createFixture(objectFixtureDef);

        circle.dispose();


        /* *** DEBUG *** */

        /*cam = new OrthographicCamera(30, 30 * (Gdx.graphics.getHeight() / Gdx.graphics.getWidth()));

        cam.position.set(cam.viewportWidth / 2f, cam.viewportHeight / 2f, 0);
        cam.update();*/

        //debugRenderer = new Box2DDebugRenderer();

        Controllers.getControllers().get(0).addListener(new ControllerListener() {
            @Override
            public void connected(Controller controller) {

            }

            @Override
            public void disconnected(Controller controller) {

            }

            @Override
            public boolean buttonDown(Controller controller, int buttonCode) {
                Gdx.app.debug("Controller", "Button Down: " + buttonCode);
                return false;
            }

            @Override
            public boolean buttonUp(Controller controller, int buttonCode) {
                Gdx.app.debug("Controller", "Button Up: " + buttonCode);
                return false;
            }

            @Override
            public boolean axisMoved(Controller controller, int axisCode, float value) {
                Gdx.app.debug("Controller", "Axis Moved: " + axisCode + " -> " + value);
                return false;
            }

            @Override
            public boolean povMoved(Controller controller, int povCode, PovDirection value) {
                Gdx.app.debug("Controller", "Pov Moved: " + povCode + " -> " + value.name());
                return false;
            }

            @Override
            public boolean xSliderMoved(Controller controller, int sliderCode, boolean value) {
                Gdx.app.debug("Controller", "xSlider Moved: " + sliderCode + " -> " + value);
                return false;
            }

            @Override
            public boolean ySliderMoved(Controller controller, int sliderCode, boolean value) {
                Gdx.app.debug("Controller", "ySlider Moved: " + sliderCode + " -> " + value);
                return false;
            }

            @Override
            public boolean accelerometerMoved(Controller controller, int accelerometerCode, Vector3 value) {
                Gdx.app.debug("Controller", "Accel Moved: " + accelerometerCode + " -> "
                        + value.toString());
                return false;
            }
        });
    }

	@Override
	public void render() {
        super.render();

        Gdx.gl.glClearColor(.8f, .8f, .8f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.FIREBRICK);
        shapeRenderer.ellipse(object.getPosition().x - 25, object.getPosition().y - 25, 50, 50);
        shapeRenderer.setColor(Color.CHARTREUSE);
        shapeRenderer.rect(ground.getPosition().x, ground.getPosition().y, 800, 10);
        shapeRenderer.end();

        int a = 0;
        for(Controller controller : Controllers.getControllers()) {
            batch.begin();
            for(int i = 0; i < 5; i++) {
                font.draw(batch, i + ": " + new DecimalFormat("#.##").format(controller.getAxis(i)),
                        50 + (200 * a), 500 - (i * 50));
            }
            batch.end();

            float lxAxis = controller.getAxis(1);
            float lyAxis = controller.getAxis(0);

            float rxAxis = controller.getAxis(3);
            float ryAxis = controller.getAxis(2);

            object.applyLinearImpulse(lxAxis, -lyAxis,
                    object.getPosition().x, object.getPosition().y, true);

            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(Color.GRAY);
            shapeRenderer.ellipse(550 + (300 * a), 610, 100, 100);
            shapeRenderer.setColor(new Color(Color.DARK_GRAY).sub(0.05f, 0.05f, 0.05f, 0));
            shapeRenderer.ellipse(550 + (300 * a) + 30 + (28 * lxAxis), 610 + 30 - (28 * lyAxis),
                    40, 40);
            shapeRenderer.setColor(Color.DARK_GRAY);
            shapeRenderer.ellipse(550 + (300 * a) + 30 + (30 * lxAxis), 610 + 30 - (30 * lyAxis),
                    40, 40);

            shapeRenderer.setColor(Color.GRAY);
            shapeRenderer.ellipse(700 + (300 * a), 610, 100, 100);
            shapeRenderer.setColor(new Color(Color.DARK_GRAY).sub(0.05f, 0.05f, 0.05f, 0));
            shapeRenderer.ellipse(700 + (300 * a) + 30 + (28 * rxAxis), 610 + 30 - (28 * ryAxis),
                    40, 40);
            shapeRenderer.setColor(Color.DARK_GRAY);
            shapeRenderer.ellipse(700 + (300 * a) + 30 + (30 * rxAxis), 610 + 30 - (30 * ryAxis),
                    40, 40);
            shapeRenderer.end();

            a++;
        }

        batch.begin();
        font.draw(batch, "FPS: " + Gdx.graphics.getFramesPerSecond(), 10, Gdx.graphics.getHeight() - 10);

        batch.end();

        world.step(1/45f, 6, 2);
        //debugRenderer.render(world, cam.combined);

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
	public void dispose() {
		super.dispose();

		batch.dispose();
	}
}
