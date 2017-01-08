package fr.nicebits.rl;

import com.badlogic.gdx.Game;
import fr.nicebits.rl.ui.MainMenuUI;

public class Soul extends Game {

    @Override
    public void create() {
        setScreen(new MainMenuUI(this));
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        super.dispose();
    }

}
