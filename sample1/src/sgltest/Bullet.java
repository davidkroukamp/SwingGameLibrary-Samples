/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sgltest;

import za.co.swinggamelibrary.AnimationCache;
import za.co.swinggamelibrary.Animation;
import za.co.swinggamelibrary.ICollidable;
import za.co.swinggamelibrary.INode;
import za.co.swinggamelibrary.Sprite;

/**
 *
 * @author dkrou
 */
public class Bullet extends Sprite implements ICollidable {

    private final Sprite owner;
    private boolean left, right;
    protected int speed;
    public final static int DEFAULT_SPEED = 5;

    public Bullet(int worldX, int worldY, Animation animation, final Player owner) {
        super(worldX, worldY, animation);
        this.owner = owner;
        speed = DEFAULT_SPEED * 2;
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

    @Override
    public void onCollision(INode node) {
        if (node.equals(owner)) { // ensure we are not hitting ourselves as bullet spawns from center of player
            return;
        }

        // assume we collided with some other node so bullet must now be removed
        removeFromParent();
    }

    public Sprite getOwner() {
        return owner;
    }

    private void move() {
        if (left && (getScreenX() - speed) <= 0) {
            removeFromParent();
        } else if (left) {
            setX(getX() - speed);
        }

        if (right && (getScreenX() + speed) + getWidth() >= getParent().getWidth()) {
            removeFromParent();
        } else if (right) {
            setX(getX() + speed);
        }
    }
}
