package com.Ferdyano.frontend.core;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Player {

    private static final float FRAME_DURATION = 0.15f;

    private final Animation<TextureRegion> runDownAnimation;
    private final Animation<TextureRegion> runUpAnimation;
    private final Animation<TextureRegion> runLeftAnimation;
    private final Animation<TextureRegion> runRightAnimation;
    private final Animation<TextureRegion> idleAnimation;

    private float stateTime;
    private Vector2 position;
    private Vector2 velocity;
    private Rectangle bounds;
    private boolean isMoving = false;

    private enum Direction { DOWN, UP, LEFT, RIGHT }
    private Direction currentDir = Direction.DOWN;

    public Player(Texture runDownSheet, Texture runUpSheet, Texture runLeftSheet, Texture runRightSheet, Texture idleSheet) {
        this.position = new Vector2(500, 300);
        this.velocity = new Vector2(0, 0);
        this.stateTime = 0f;
        this.bounds = new Rectangle(position.x, position.y, 100, 100);

        // Gambar Anda sekarang 1 Frame semua.
        this.runDownAnimation = createAnimation(runDownSheet, 1);
        this.runUpAnimation = createAnimation(runUpSheet, 1);
        this.runLeftAnimation = createAnimation(runLeftSheet, 1);
        this.runRightAnimation = createAnimation(runRightSheet, 1);
        this.idleAnimation = createAnimation(idleSheet, 1);
    }

    private Animation<TextureRegion> createAnimation(Texture sheet, int frameCols) {
        int frameWidth = sheet.getWidth() / frameCols;
        int frameHeight = sheet.getHeight();

        TextureRegion[][] tmp = TextureRegion.split(sheet, frameWidth, frameHeight);
        TextureRegion[] frames = new TextureRegion[frameCols];

        for (int i = 0; i < frameCols; i++) {
            if (tmp.length > 0 && i < tmp[0].length) {
                frames[i] = tmp[0][i];
            }
        }

        // Perbaikan Constructor agar tidak error tipe data
        Animation<TextureRegion> animation = new Animation<TextureRegion>(FRAME_DURATION, frames);
        animation.setPlayMode(Animation.PlayMode.LOOP);
        return animation;
    }

    public void update(float delta) {
        position.x += velocity.x * delta;
        position.y += velocity.y * delta;
        stateTime += delta;

        isMoving = (Math.abs(velocity.x) > 0.1f || Math.abs(velocity.y) > 0.1f);

        if (isMoving) {
            if (Math.abs(velocity.x) > Math.abs(velocity.y)) {
                currentDir = (velocity.x > 0) ? Direction.RIGHT : Direction.LEFT;
            } else {
                currentDir = (velocity.y > 0) ? Direction.UP : Direction.DOWN;
            }
        }
        bounds.setPosition(position.x, position.y);
    }

    public TextureRegion getCurrentFrame() {
        if (isMoving) {
            switch (currentDir) {
                case UP:    return runUpAnimation.getKeyFrame(stateTime, true);
                case LEFT:  return runLeftAnimation.getKeyFrame(stateTime, true);
                case RIGHT: return runRightAnimation.getKeyFrame(stateTime, true);
                default:    return runDownAnimation.getKeyFrame(stateTime, true);
            }
        } else {
            return idleAnimation.getKeyFrames()[0];
        }
    }

    public Rectangle getBounds() { return bounds; }
    public void setPosition(float x, float y) { this.position.set(x, y); this.bounds.setPosition(x, y); }
    public void setVelocity(float dx, float dy) { this.velocity.set(dx, dy); }
    public float getPositionX() { return position.x; }
    public float getPositionY() { return position.y; }
    public void setBoundsSize(float width, float height) { this.bounds.setSize(width, height); }
}
