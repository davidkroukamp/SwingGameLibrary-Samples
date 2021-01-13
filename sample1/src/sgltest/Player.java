/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sgltest;

import za.co.swinggamelibrary.Animation;
import za.co.swinggamelibrary.AnimationCache;
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
    private final String bloodSplatterAnimationName;

    public Player(int worldX, int worldY, Animation idleAnimation, Direction direction, String bulletAnimationName, String bloodSplatterAnimationName, boolean isFlippedX) {
        super(worldX, worldY, idleAnimation);
        this.direction = direction;
        this.bulletAnimationName = bulletAnimationName;
        this.speed = DEFAULT_SPEED;
        this.bloodSplatterAnimationName = bloodSplatterAnimationName;
        this.setFlippedX(isFlippedX);
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
        } else if (node instanceof Bullet && ((Bullet) node).getOwner() != this) { // a bullet which is not our own struck us
            System.out.println("Bullet struck player");
            BloodSplatter bs = new BloodSplatter(node.getX(), node.getY(), AnimationCache.getInstance().getAnimation(bloodSplatterAnimationName));
            getParent().add(bs);
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
            this.setFlippedX(true);
            setX(getX() - speed);
        }

        if (RIGHT && (getScreenX() + speed) + getWidth() <= getParent().getWidth()) {
            direction = Direction.RIGHT_FACING;
            this.setFlippedX(false);
            setX(getX() + speed);
        }

        if (UP && (getScreenY() - speed) >= 0) {
            setY(getY() - speed);
        }

        if (DOWN && (getScreenY() + speed) + getHeight() <= getParent().getHeight()) {
            setY(getY() + speed);
        }
    }

    public void shoot() {
        int bulletX = (int) (getX() + getWidth() / 2);
        int bulletY = (int) (getY() + getHeight() / 2);
        Animation bulletAnimation = AnimationCache.getInstance().getAnimation(bulletAnimationName);
        Bullet bullet = new Bullet(bulletX, bulletY, bulletAnimation, this);
        getParent().add(bullet);
        AudioEngine.getInstance().playSound(getClass().getResource("assets/sounds/shot.wav"), 0.5f);
    }
}
