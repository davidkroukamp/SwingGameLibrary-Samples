/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sgltest;

import za.co.swinggamelibrary.ICollidable;
import za.co.swinggamelibrary.INode;
import za.co.swinggamelibrary.Sprite;

/**
 *
 * @author dkrou
 */
public class Shuriken extends Sprite implements ICollidable {

    private final Player owner;
    private boolean left, right;
    protected int speed;
    public final static int DEFAULT_SPEED = 5;

    public Shuriken(int x, int y, String spriteFrameName, final Player owner) {
        super(spriteFrameName);
        this.setPosition(x, y);
        this.owner = owner;
        this.speed = DEFAULT_SPEED * 2;
        // check which way the player is facing and make bullet face and travel in the correct direction
        if (this.owner.getDirection() == Direction.LEFT_FACING) {
            this.left = true;
        } else if (this.owner.getDirection() == Direction.RIGHT_FACING) {
            this.right = true;
        }
    }

    @Override
    public void update(long elapsedTime) {
        super.update(elapsedTime);
        this.move();
    }

    @Override
    public void onCollision(INode node) {
        if (node.equals(this.owner)) { // ensure we are not hitting ourselves as bullet spawns from center of player
            return;
        }

        // assume we collided with some other node so bullet must now be removed
        this.removeFromParent();
    }

    public Sprite getOwner() {
        return this.owner;
    }

    private void move() {
        if (this.left && (this.getScreenX() - this.speed) <= 0) {
            this.removeFromParent();
        } else if (this.left) {
            this.setX(this.getX() - speed);
        }

        if (this.right && (this.getScreenX() + this.speed) + this.getWidth() >= this.getParent().getWidth()) {
            this.removeFromParent();
        } else if (this.right) {
            this.setX(this.getX() + this.speed);
        }
    }
}
