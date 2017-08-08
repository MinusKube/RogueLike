package fr.nicebits.rl.ui.components;

import com.badlogic.gdx.graphics.g2d.Batch;
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

    private boolean btnPressed = false;

    public TextButton(String text) {
        super(text, STYLE);

        setOrigin(160, 30);
        setTransform(true);

        getLabelCell().fill(false).expand(false, false);
        getLabelCell().padTop(-5);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);

        updateLabelPos();
    }

    private void updateLabelPos() {
        if(!btnPressed && isPressed()) {
            getLabel().moveBy(0, -3);
            btnPressed = true;
        }
        else if(btnPressed && !isPressed()) {
            getLabel().moveBy(0, 3);
            btnPressed = false;
        }
    }

}
