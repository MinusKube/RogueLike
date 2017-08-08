package fr.nicebits.rl.ui.skin;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import java.util.HashMap;
import java.util.Map;

public class SkinManager {

    private Map<String, Skin> skins = new HashMap<>();

    public void load() {
        skins.put("textButton", loadTextButtonSkin());
    }

    public Skin getSkin(String key) { return skins.get(key); }

    private Skin loadTextButtonSkin() {
        Skin skin = new Skin();
        skin.add("button", new Texture(Gdx.files.internal("textures/button.png")));
        skin.add("buttonDown", new Texture(Gdx.files.internal("textures/buttonDown.png")));

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/tubeOfCorn.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

        parameter.size = 26;
        parameter.shadowColor = new Color(0.1f, 0.1f, 0.1f, 1);
        parameter.shadowOffsetX = 2;
        parameter.shadowOffsetY = 2;

        skin.add("font", generator.generateFont(parameter));

        generator.dispose();
        return skin;
    }

    private Texture color(Color color) {
        Pixmap pixmap = new Pixmap(100, 100, Pixmap.Format.RGBA8888);
        pixmap.setColor(color);
        pixmap.fill();

        return new Texture(pixmap);
    }

}
