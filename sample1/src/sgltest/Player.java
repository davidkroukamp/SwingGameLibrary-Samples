/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sgltest;

import za.co.swinggamelibrary.AnimationCache;
import za.co.swinggamelibrary.AnimationFrame;
import za.co.swinggamelibrary.AudioEngine;
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
    private final String bulletAnimationName;

    public Player(int x, int y, AnimationFrame animation, Direction direction, int containerWidth, int containerHeight, String bulletAnimationName) {
        super(x, y, animation);
        this.setVisible(true);
        this.direction = direction;
        this.containerWidth = containerWidth;
        this.containerHeight = containerHeight;
        this.bulletAnimationName = bulletAnimationName;
        this.speed = DEFAULT_SPEED;
    }

    @Override
    public void update(long elapsedTime) {
        super.update(elapsedTime);
        move();
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
        if (LEFT && (getX() - speed) >= 0) {
            direction = Direction.LEFT_FACING;
            setX(getX() - speed);
        }

        if (RIGHT && (getX() + speed) + getWidth() <= containerWidth) {
            direction = Direction.RIGHT_FACING;
            setX(getX() + speed);
        }

        if (UP && (getY() - speed) >= 0) {
            setY(getY() - speed);
        }

        if (DOWN && (getY() + speed) + getHeight() <= containerHeight) {
            setY(getY() + speed);
        }
    }

    public void shoot() {
        int bulletX = (int) (getX() + getHeight() / 2);
        int bulletY = (int) (getY() + getWidth() / 2);
        AnimationFrame bulletAnimation = AnimationCache.getInstance().getAnimation(bulletAnimationName);
        int parentWidth = (int) getParent().getWidth();
        Bullet bullet = new Bullet(bulletX, bulletY, bulletAnimation, parentWidth, this);
        getParent().add(bullet);
        AudioEngine.getInstance().playSound(getClass().getResource("assets/sounds/shot.wav"), 0.5f);
    }
}
