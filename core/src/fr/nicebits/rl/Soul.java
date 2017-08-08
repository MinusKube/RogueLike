package fr.nicebits.rl;

import com.badlogic.gdx.Game;
import fr.nicebits.rl.ui.MainMenuUI;
import fr.nicebits.rl.ui.skin.SkinManager;

public class Soul extends Game {

    private static Soul instance;

    private SkinManager skinManager;

    @Override
    public void create() {
        instance = this;

        this.skinManager = new SkinManager();
        this.skinManager.load();

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

    public SkinManager getSkinManager() { return skinManager; }

    public static Soul instance() { return instance; }

}
