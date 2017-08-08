package fr.nicebits.rl.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import fr.nicebits.rl.Soul;
import fr.nicebits.rl.ui.components.TextButton;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeOut;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.run;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

public class MainMenuUI extends UI {

    public MainMenuUI(final Soul game) {
        super(game);

        Table table = new Table();
        table.defaults().size(320, 60).pad(10);

        table.add(new TextButton("Play")).row();
        table.add(new TextButton("Options")).row();

        TextButton quitBtn = new TextButton("Quit");
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

        Batch batch = stage.getBatch();

        batch.begin();
        {
            batch.setColor(Color.WHITE);
            batch.draw(new Texture(Gdx.files.internal("textures/main_bg.png")),
                    0, 0, stage.getWidth(), stage.getHeight());
        }
        batch.end();

        super.render(delta);
    }

    @Override
    public void dispose() {
        super.dispose();
    }

}
