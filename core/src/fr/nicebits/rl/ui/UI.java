package fr.nicebits.rl.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import fr.nicebits.rl.Soul;

public abstract class UI implements Screen {

    protected Soul game;
    protected Stage stage;

    public UI(Soul game) {
        this.game = game;
        this.stage = new Stage(new ScreenViewport());
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void hide() {}
    @Override
    public void resume() {}
    @Override
    public void pause() {}
    @Override
    public void resize(int width, int height) {}

    @Override
    public void render(float delta) {
        stage.act(1 / 30f);
        stage.draw();
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

}
