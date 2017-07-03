package fr.nicebits.rl.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import fr.nicebits.rl.RogueLikeOld;
import fr.nicebits.rl.Soul;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.scaleBy;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.scaleTo;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

public class MainMenuUI extends UI {

    public MainMenuUI(final Soul game) {
        super(game);

        Table table = new Table();

        table.add(getBtn()).size(320, 60).pad(10).row();
        table.add(getBtn()).size(320, 60).pad(10).row();
        table.add(getBtn()).size(320, 60).pad(10).row();

        table.setFillParent(true);

        stage.addActor(table);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

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

    public TextButton getBtn() {
        Skin skin = new Skin();
        skin.add("white", color(Color.WHITE));

        BitmapFont font = new BitmapFont();
        skin.add("default", font);

        TextButton.TextButtonStyle btnStyle = new TextButton.TextButtonStyle();
        btnStyle.up = skin.newDrawable("white", Color.GRAY);
        btnStyle.down = skin.newDrawable("white", Color.GRAY);
        btnStyle.checked = skin.newDrawable("white", Color.GRAY);
        btnStyle.over = skin.newDrawable("white", Color.GRAY);
        btnStyle.font = skin.getFont("default");

        skin.add("default", btnStyle);

        final TextButton btn = new TextButton("Potato", btnStyle);
        btn.setOrigin(160, 30);
        btn.setTransform(true);

        btn.addListener(event -> {
            if(!(event instanceof InputEvent))
                return false;

            InputEvent input = (InputEvent) event;

            if(input.getType() == InputEvent.Type.enter)
                btn.addAction(sequence(
                        scaleBy(0.05f, 0.05f, 0.2f),
                        scaleBy(-0.02f, -0.02f, 0.2f)));
            else if(input.getType() == InputEvent.Type.exit) {
                btn.clearActions();
                btn.addAction(scaleTo(1, 1, 0.2f));
            }

            return true;
        });


        btn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                btn.setText("Carrot");

                game.setScreen(new RogueLikeOld());
            }
        });

        return btn;
    }

}
