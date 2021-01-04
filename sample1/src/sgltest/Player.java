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
    public boolean LEFT, RIGHT, UP, DOWN;
    private final String bulletAnimationName;

    public Player(int worldX, int worldY, AnimationFrame animation, Direction direction, String bulletAnimationName) {
        super(worldX, worldY, animation);
        this.direction = direction;
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
        if (LEFT && (getScreenX() - speed) >= 0) {
            direction = Direction.LEFT_FACING;
            setWorldX(getWorldX() - speed);
        }

        if (RIGHT && (getScreenX() + speed) + getWidth() <= getParent().getWidth()) {
            direction = Direction.RIGHT_FACING;
            setWorldX(getWorldX() + speed);
        }

        if (UP && (getScreenY() - speed) >= 0) {
            setWorldY(getWorldY() - speed);
        }

        if (DOWN && (getScreenY() + speed) + getHeight() <= getParent().getHeight()) {
            setWorldY(getWorldY() + speed);
        }
    }

    public void shoot() {
        int bulletX = (int) (getWorldX() + getWidth() / 2);
        int bulletY = (int) (getWorldY() + getHeight() / 2);
        AnimationFrame bulletAnimation = AnimationCache.getInstance().getAnimation(bulletAnimationName);
        Bullet bullet = new Bullet(bulletX, bulletY, bulletAnimation, this, true);
        getParent().add(bullet);
        AudioEngine.getInstance().playSound(getClass().getResource("assets/sounds/shot.wav"), 0.5f);
    }
}
