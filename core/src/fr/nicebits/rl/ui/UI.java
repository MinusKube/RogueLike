package fr.nicebits.rl.ui;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public abstract class UI {

    protected Stage stage;

    public UI() {
        stage = new Stage(new ScreenViewport());
    }

    public void render() {
        stage.act(1 / 30f);
        stage.draw();
    }

    public void dispose() {
        stage.dispose();
    }

}
