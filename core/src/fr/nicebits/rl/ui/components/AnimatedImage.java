package fr.nicebits.rl.ui.components;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class AnimatedImage extends Image {

    private float stateTime = 0;
    private Animation<TextureRegion> animation;

    public AnimatedImage(Animation<TextureRegion> animation) {
        super(animation.getKeyFrame(0));

        this.animation = animation;
    }

    @Override
    public void act(float delta) {
        stateTime += delta;

        TextureRegionDrawable drawable = (TextureRegionDrawable) getDrawable();
        drawable.setRegion(animation.getKeyFrame(stateTime, true));

        super.act(delta);
    }

}
