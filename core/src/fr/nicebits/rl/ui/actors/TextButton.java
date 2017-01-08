package fr.nicebits.rl.ui.actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class TextButton extends Actor {

    private static final BitmapFont FONT = new BitmapFont();

    private ShapeRenderer renderer;
    private String text;

    public TextButton(String text) {
        this.renderer = new ShapeRenderer();
        this.text = text;

        setX(10);
        setY(10);
        setWidth(200);
        setHeight(50);
        setColor(Color.FIREBRICK);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        Color color = getColor();

        batch.end();

        renderer.begin(ShapeRenderer.ShapeType.Filled);
        renderer.setColor(color.r, color.g, color.b, color.a * parentAlpha);
        renderer.rect(getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(),
                getScaleX(), getScaleY(), getRotation());
        renderer.end();

        batch.begin();

        batch.setColor(Color.WHITE);
        FONT.draw(batch, text, getX(), getY());
    }

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }

}
