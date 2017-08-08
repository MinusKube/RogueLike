package fr.nicebits.rl.ui.components;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import fr.nicebits.rl.Soul;

public class TextButton extends com.badlogic.gdx.scenes.scene2d.ui.TextButton {

    private static final TextButton.TextButtonStyle STYLE;

    static {
        Skin skin = Soul.instance().getSkinManager().getSkin("textButton");

        STYLE = new TextButton.TextButtonStyle();
        STYLE.up = skin.getDrawable("button");
        STYLE.down = skin.getDrawable("buttonDown");
        STYLE.font = skin.getFont("font");
    }

    public TextButton(String text) {
        super(text, STYLE);

        setOrigin(160, 30);
        setTransform(true);

        getLabelCell().fill(false).expand(false, false);
        getLabelCell().padTop(-5);

        addListener(new InputListener() {

            private boolean touchDown = false;
            private boolean down = false;

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                touchDown = true;

                if(down)
                    return true;

                getLabel().moveBy(0, -3);
                down = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                touchDown = false;

                if(!down)
                    return;

                getLabel().moveBy(0, 3);
                down = false;
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                if(!down || !touchDown)
                    return;

                getLabel().moveBy(0, 3);
                down = false;
            }

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                if(down || !touchDown)
                    return;

                getLabel().moveBy(0, -3);
                down = true;
            }

        });
    }

}
