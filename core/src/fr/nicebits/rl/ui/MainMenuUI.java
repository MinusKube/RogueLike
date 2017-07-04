package fr.nicebits.rl.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import fr.nicebits.rl.Soul;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeOut;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.run;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

public class MainMenuUI extends UI {

    public MainMenuUI(final Soul game) {
        super(game);

        Table table = new Table();
        table.defaults().size(320, 60).pad(10);

        table.add(getBtn("Play")).row();
        table.add(getBtn("Options")).row();

        TextButton quitBtn = getBtn("Quit");
        table.add(quitBtn);

        quitBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor_) {
                table.getChildren()
                        .forEach(actor -> {
                            Action action = fadeOut(0.5f);

                            if(actor == quitBtn)
                                action = sequence(action, run(() -> System.exit(0)));

                            actor.addAction(action);
                        });
            }
        });

        table.setFillParent(true);

        stage.addActor(table);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.51f, 0.25f, 0.61f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.getBatch().begin();
        stage.getBatch().setColor(Color.WHITE);
        stage.getBatch().draw(new Texture(Gdx.files.internal("textures/main_bg.png")),
                0, 0, stage.getWidth(), stage.getHeight());
        stage.getBatch().end();

        super.render(delta);
    }

    @Override
    public void dispose() {
        super.dispose();
    }

    private Texture color(Color color) {
        Pixmap pixmap = new Pixmap(100, 100, Pixmap.Format.RGBA8888);
        pixmap.setColor(color);
        pixmap.fill();

        return new Texture(pixmap);
    }

    public TextButton getBtn(String txt) {
        Skin skin = new Skin();
        skin.add("white", color(Color.WHITE));

        skin.add("test", new Texture(Gdx.files.internal("textures/button.png")));
        skin.add("testDown", new Texture(Gdx.files.internal("textures/buttonDown.png")));

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/TubeOfCorn.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 26;
        parameter.shadowColor = new Color(0.1f, 0.1f, 0.1f, 1);
        parameter.shadowOffsetX = 2;
        parameter.shadowOffsetY = 2;

        skin.add("font", generator.generateFont(parameter));
        generator.dispose();

        BitmapFont font = new BitmapFont();
        skin.add("default", font);

        TextButton.TextButtonStyle btnStyle = new TextButton.TextButtonStyle();
        btnStyle.up = skin.newDrawable("test");
        btnStyle.down = skin.newDrawable("testDown");
        btnStyle.font = skin.getFont("font");

        skin.add("default", btnStyle);

        final TextButton btn = new TextButton(txt, btnStyle);
        btn.setOrigin(160, 30);
        btn.setTransform(true);

        btn.getLabelCell().fill(false).expand(false, false);
        btn.getLabelCell().padTop(-5);

        btn.addListener(new InputListener() {

            private boolean touchDown = false;
            private boolean down = false;

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                touchDown = true;

                if(down)
                    return true;

                btn.getLabel().moveBy(0, -3);
                down = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                touchDown = false;

                if(!down)
                    return;

                btn.getLabel().moveBy(0, 3);
                down = false;
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                if(!down || !touchDown)
                    return;

                btn.getLabel().moveBy(0, 3);
                down = false;
            }

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                if(down || !touchDown)
                    return;

                btn.getLabel().moveBy(0, -3);
                down = true;
            }

        });

        btn.addListener(new ChangeListener() {

            @Override
            public void changed(ChangeEvent event, Actor actor) {
                //game.setScreen(new RogueLikeOld());
            }

        });

        return btn;
    }

}
