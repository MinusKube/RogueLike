package fr.nicebits.rl;

import com.badlogic.gdx.*;
import com.badlogic.gdx.controllers.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.physics.box2d.*;

import java.text.DecimalFormat;

public class RogueLike extends Game {

    private static final int VIEWPORT_WIDTH = 12;
    private static final int VIEWPORT_HEIGHT = 10;

    private static final float PPM = 100f;

    private boolean debug = false;

    private SpriteBatch batch;
    private SpriteBatch hudBatch;
    private ShapeRenderer shapeRenderer;
    private ShapeRenderer hudRenderer;

    private BitmapFont font24;
    private BitmapFont font18;

    private World world;

    private Body ground;
    private Body object;

    private OrthographicCamera cam;
    private Box2DDebugRenderer debugRenderer;

    private Sprite bgSprite;

    @Override
    public void create() {

        /* *** INIT *** */

        Gdx.app.setLogLevel(Application.LOG_DEBUG);
        Box2D.init();

        batch = new SpriteBatch();
        hudBatch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        hudRenderer = new ShapeRenderer();


        /* *** FONTS *** */

        FreeTypeFontGenerator generatorTube = new FreeTypeFontGenerator(Gdx.files.internal("fonts/TubeOfCorn.ttf"));
        FreeTypeFontGenerator generatorArial = new FreeTypeFontGenerator(Gdx.files.internal("fonts/arial.ttf"));

        FreeTypeFontGenerator.FreeTypeFontParameter param = new FreeTypeFontGenerator.FreeTypeFontParameter();

        param.color = Color.GRAY;

        param.size = 24;
        font24 = generatorTube.generateFont(param);

        param.size = 18;
        font18 = generatorArial.generateFont(param);

        generatorTube.dispose();
        generatorArial.dispose();

        /* *** WORLD *** */

        world = new World(new Vector2(0f, -9.81f) /* Applied force */, true);


        /* *** GROUND *** */

        float groundW = Gdx.graphics.getWidth() / PPM;
        float groundH = 50 / PPM;

        // Body

        BodyDef groundBodyDef = new BodyDef();
        groundBodyDef.type = BodyDef.BodyType.StaticBody;
        groundBodyDef.position.set(0, 180 / PPM);

        ground = world.createBody(groundBodyDef);

        // Shape

        PolygonShape groundBox = new PolygonShape();
        groundBox.setAsBox(groundW / 2, groundH / 2,
                new Vector2(groundW / 2, groundH / 2), 0);
        FixtureDef groundFixtureDef = new FixtureDef();

        groundFixtureDef.shape = groundBox;

        ground.createFixture(groundFixtureDef);

        groundBox.dispose();

        /* *** OBJECT *** */

        BodyDef objectBodyDef = new BodyDef();
        objectBodyDef.type = BodyDef.BodyType.DynamicBody;
        objectBodyDef.position.set(400 / PPM, 800 / PPM);

        object = world.createBody(objectBodyDef);
        object.setFixedRotation(true);

        CircleShape circle = new CircleShape();
        circle.setRadius(25 / PPM);

        FixtureDef objectFixtureDef = new FixtureDef();

        objectFixtureDef.shape = circle;
        objectFixtureDef.density = 0.6f;
        objectFixtureDef.friction = 0.15f;
        objectFixtureDef.restitution = 0.2f;

        object.getMassData().mass = 5f;

        Fixture fixture = object.createFixture(objectFixtureDef);

        circle.dispose();

        debugRenderer = new Box2DDebugRenderer();
        cam = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        cam.position.set(cam.viewportWidth / 2, cam.viewportHeight / 2, 0);
        cam.update();

        if(Controllers.getControllers().size == 0)
            return;

        Controllers.getControllers().get(0).addListener(new ControllerListener() {
            @Override
            public void connected(Controller controller) {

            }

            @Override
            public void disconnected(Controller controller) {

            }

            @Override
            public boolean buttonDown(Controller controller, int buttonCode) {
                if(buttonCode == 0) {
                    object.applyForceToCenter(0, 20f, true);
                }
                return false;
            }

            @Override
            public boolean buttonUp(Controller controller, int buttonCode) {
                return false;
            }

            @Override
            public boolean axisMoved(Controller controller, int axisCode, float value) {
                return false;
            }

            @Override
            public boolean povMoved(Controller controller, int povCode, PovDirection value) {
                return false;
            }

            @Override
            public boolean xSliderMoved(Controller controller, int sliderCode, boolean value) {
                return false;
            }

            @Override
            public boolean ySliderMoved(Controller controller, int sliderCode, boolean value) {
                return false;
            }

            @Override
            public boolean accelerometerMoved(Controller controller, int accelerometerCode, Vector3 value) {
                Gdx.app.debug(controller.getName(), "Accel Moved: " + accelerometerCode + " -> "
                        + value.toString());
                return false;
            }
        });

        Texture background = new Texture(new Pixmap(Gdx.files.internal("bg.png")));
        background.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);

        bgSprite = new Sprite(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.app.debug("Background", bgSprite.getU() + " / " + bgSprite.getV() + " / "
                + bgSprite.getU2() + " / " + bgSprite.getV2());
    }

    @Override
    public void render() {
        super.render();

        if(Gdx.input.isKeyJustPressed(Input.Keys.F3))
            debug = !debug;

        cam.position.set(object.getPosition().scl(PPM), 0);
        cam.update();

        batch.setProjectionMatrix(cam.combined);
        shapeRenderer.setProjectionMatrix(cam.combined);

        world.step(1/45f, 6, 2);

        Gdx.gl.glClearColor(.8f, .8f, .8f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        float x = cam.position.x / 300f;
        float y = cam.position.y / 300f;
        bgSprite.setRegion(x, y, x + 16f, y + 9f);

        hudBatch.begin();
        bgSprite.draw(hudBatch);
        hudBatch.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.FIREBRICK);
        shapeRenderer.ellipse((object.getPosition().x * PPM) - 25, (object.getPosition().y * PPM) - 25, 50, 50);
        shapeRenderer.setColor(Color.BROWN);
        shapeRenderer.rect(ground.getPosition().x * PPM, ground.getPosition().y * PPM, Gdx.graphics.getWidth(), 50);
        shapeRenderer.end();

        int a = 0;

        for(Controller controller : Controllers.getControllers()) {
            if(debug) {
                hudBatch.begin();
                font18.setColor(Color.BLACK);
                for(int i = 0; i < 5; i++) {
                    font18.draw(hudBatch, i + ": " + new DecimalFormat("#.##").format(controller.getAxis(i)),
                            20 + (80 * a), 650 - (i * 30));
                }
                hudBatch.end();
            }

            float lxAxis = controller.getName().toLowerCase().contains("xbox") ? controller.getAxis(1) : controller.getAxis(3);
            float lyAxis = controller.getName().toLowerCase().contains("xbox") ? controller.getAxis(0) : controller.getAxis(2);

            float rxAxis = controller.getName().toLowerCase().contains("xbox") ? controller.getAxis(3) : controller.getAxis(1);
            float ryAxis = controller.getName().toLowerCase().contains("xbox") ? controller.getAxis(2) : controller.getAxis(0);

            object.applyLinearImpulse(lxAxis / PPM, -lyAxis / PPM,
                    object.getPosition().x, object.getPosition().y, true);

            hudRenderer.begin(ShapeRenderer.ShapeType.Filled);
            hudRenderer.setColor(Color.GRAY);
            hudRenderer.ellipse(550 + (300 * a), 610, 100, 100);
            hudRenderer.setColor(new Color(Color.DARK_GRAY).sub(0.05f, 0.05f, 0.05f, 0));
            hudRenderer.ellipse(550 + (300 * a) + 30 + (28 * lxAxis), 610 + 30 - (28 * lyAxis),
                    40, 40);
            hudRenderer.setColor(Color.DARK_GRAY);
            hudRenderer.ellipse(550 + (300 * a) + 30 + (30 * lxAxis), 610 + 30 - (30 * lyAxis),
                    40, 40);

            hudRenderer.setColor(Color.GRAY);
            hudRenderer.ellipse(700 + (300 * a), 610, 100, 100);
            hudRenderer.setColor(new Color(Color.DARK_GRAY).sub(0.05f, 0.05f, 0.05f, 0));
            hudRenderer.ellipse(700 + (300 * a) + 30 + (28 * rxAxis), 610 + 30 - (28 * ryAxis),
                    40, 40);
            hudRenderer.setColor(Color.DARK_GRAY);
            hudRenderer.ellipse(700 + (300 * a) + 30 + (30 * rxAxis), 610 + 30 - (30 * ryAxis),
                    40, 40);
            hudRenderer.end();

            a++;
        }

        if(debug) {
            hudBatch.begin();
            font24.draw(hudBatch, "FPS: " + Gdx.graphics.getFramesPerSecond(), 10, Gdx.graphics.getHeight() - 10);
            hudBatch.end();

            debugRenderer.setDrawAABBs(true);
            debugRenderer.setDrawVelocities(true);
            debugRenderer.setDrawBodies(true);
            debugRenderer.setDrawContacts(true);
            debugRenderer.render(world, cam.combined.cpy().scale(PPM, PPM, 0));
        }
    }

    @Override
    public void dispose() {
        super.dispose();

        batch.dispose();
        hudBatch.dispose();
        world.dispose();
        font24.dispose();
        shapeRenderer.dispose();
        hudRenderer.dispose();
    }

}
