/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sgltest;

import java.awt.geom.Rectangle2D;
import za.co.swinggamelibrary.Animation;
import za.co.swinggamelibrary.ICollidable;
import za.co.swinggamelibrary.INode;
import za.co.swinggamelibrary.Sprite;

/**
 *
 * @author dkrou
 */
public class Player extends Sprite implements ICollidable {

    private final static int DEFAULT_SPEED = 5;
    protected int speed;
    protected Direction direction;
    protected final int containerWidth, containerHeight;
    public boolean LEFT, RIGHT, UP, DOWN;

    public Player(int x, int y, Animation animation, Direction direction, int containerWidth, int containerHeight) {
        super(x, y, animation);
        setVisible(true);
        this.direction = direction;
        this.containerWidth = containerWidth;
        this.containerHeight = containerHeight;
        speed = DEFAULT_SPEED;
    }

    @Override
    public void update(long elapsedTime) {
        super.update(elapsedTime);
        move();
    }

    @Override
    public Rectangle2D getBounds2D() {
        return rectangle.getBounds2D();
    }

    @Override
    public boolean intersects(ICollidable collidable) {
        return rectangle.intersects(collidable.getBounds2D());
    }

    @Override
    public void onCollision(INode node) {
        if (node instanceof Player) {
            System.out.println("Players intersected");
        }
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getSpeed() {
        return speed;
    }

    public Direction getDirection() {
        return direction;
    }

    private void move() {
        if (LEFT && (rectangle.x - speed) >= 0) {
            direction = Direction.LEFT_FACING;
            rectangle.x -= speed;
        }

        if (RIGHT && (rectangle.x + speed) + getWidth() <= containerWidth) {
            direction = Direction.RIGHT_FACING;
            rectangle.x += speed;
        }

        if (UP && (rectangle.y - speed) >= 0) {
            rectangle.y -= speed;
        }

        if (DOWN && (rectangle.y + speed) + getHeight() <= containerHeight) {
            rectangle.y += speed;
        }
    }

}
