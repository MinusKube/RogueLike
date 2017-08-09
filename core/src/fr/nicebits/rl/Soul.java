package fr.nicebits.rl;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.physics.box2d.World;
import fr.nicebits.rl.ui.MainMenuUI;
import fr.nicebits.rl.ui.skin.SkinManager;

public class Soul extends Game {

    private static Soul instance;

    private SkinManager skinManager;
    private World world;

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
