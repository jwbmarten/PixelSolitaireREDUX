package com.pixel.game;


import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

/**
 * Created by Jake on 2/9/2024
 */
public class Animation {
    private Array<TextureRegion> frames;
    float maxFrameTime;
    float currentFrameTime;
    int frameCount;
    int frame;
    String name;

    boolean isCycle;

    Rectangle bounds;


    public Animation(TextureRegion region, int frameCount, float cycleTime, boolean isCycle, String name) {
        frames = new Array<TextureRegion>();
        TextureRegion temp;
        int frameWidth = region.getRegionWidth() / frameCount;
        for (int i = 0; i < frameCount; i++) {
            temp = new TextureRegion(region, i * frameWidth, 0, frameWidth, region.getRegionHeight());
            frames.add(temp);
        }
        this.frameCount = frameCount;
        maxFrameTime = cycleTime / frameCount;
        frame = 0;
        this.isCycle = isCycle;
        this.name = name;

    }


    public void update(float dt) {
        if (!isComplete() || isCycle) { // Only update if the animation is not complete or if it cycles
            currentFrameTime += dt;
            if (currentFrameTime > maxFrameTime) {
                frame++;
                if (frame >= frameCount) {
                    if (isCycle) {
                        frame = 0; // Loop to start since it should be a cycle animation
                    } else {
                        frame = frameCount - 1; // keep the frame at the last frame for non cycle animations
                    }
                }
                currentFrameTime -= maxFrameTime;
            }
            if (frame >= frameCount) {
                if (isCycle) {
                    frame = 0; // Loop to the start for cycling animations
                } else {
                    frame = frameCount - 1; // Stay on the last frame for non-cycling animations
                }
            }
        }
    }

    public void reset() {
        this.frame = 0;
        this.currentFrameTime = 0;
    }

//    public void flip(){
//        for(TextureRegion region : frames)
//            region.flip(true, false);
//    }


    public TextureRegion getFrame() {

//        System.out.println("Current frame count is: " + frame + " of " + frameCount );

        return frames.get(frame);
    }

    public float getFrameWidth() {

        return this.frames.get(0).getRegionWidth() / 2f;
    }

    public float getFrameHeight() {

        return this.frames.get(0).getRegionHeight();
    }

    public int getCurrFrameNum() {

        return this.frame;
    }


    public boolean isComplete() {
        return frame == frameCount - 2;
    }

    public float animationPercentComplete() {

        return ((float) frame / frameCount);
    }
}
