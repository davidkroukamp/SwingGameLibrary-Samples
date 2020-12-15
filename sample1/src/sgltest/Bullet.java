/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sgltest;

import za.co.swinggamelibrary.Animation;
import za.co.swinggamelibrary.Sprite;

/**
 *
 * @author dkrou
 */
public class Bullet extends Sprite {

    private final Sprite owner;
    private boolean left, right;
    protected int speed;
    protected final int containerWidth;
    public final static int DEFAULT_SPEED = 5;

    public Bullet(int x, int y, Animation animation, int containerWidth, final Player owner) {
        super(x, y, animation);
        this.containerWidth = containerWidth;
        this.owner = owner;
        speed = DEFAULT_SPEED * 2;
        setVisible(true);
        // check which way the player is facing and make bullet face and travel in the correct direction
        if (owner.getDirection() == Direction.LEFT_FACING) {
            left = true;
        } else if (owner.getDirection() == Direction.RIGHT_FACING) {
            right = true;
        }
    }

    @Override
    public void update(long elapsedTime) {
        super.update(elapsedTime);
        move();
    }

    public Sprite getOwner() {
        return owner;
    }

    private void move() {
        if ((rectangle.x - speed) <= 0) {
            removeFromParent();
        } else if (left) {
            rectangle.x -= speed;
        }

        if ((rectangle.x + speed) + getWidth() >= containerWidth) {
            removeFromParent();
        } else if (right) {
            rectangle.x += speed;
        }
    }
}
