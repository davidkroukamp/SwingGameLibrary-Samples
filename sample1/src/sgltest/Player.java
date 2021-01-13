/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sgltest;

import za.co.swinggamelibrary.Animation;
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
    private final String bulletSpriteFrameName;
    private final String bloodSplatterSpriteFrameName;

    public Player(int x, int y, Animation idleAnimation, Direction direction, String bulletSpriteFrameName, String bloodSplatterSpriteFrameName, boolean isFlippedX) {
        super(idleAnimation);
        this.setPosition(x, y);
        this.setFlippedX(isFlippedX);
        this.direction = direction;
        this.bulletSpriteFrameName = bulletSpriteFrameName;
        this.bloodSplatterSpriteFrameName = bloodSplatterSpriteFrameName;
        this.speed = DEFAULT_SPEED;
    }

    @Override
    public void update(long elapsedTime) {
        super.update(elapsedTime);
        this.move();
    }

    @Override
    public void onCollision(INode node) {
        if (node instanceof Player) {
            System.out.println("Players intersected");
        } else if (node instanceof Shuriken && ((Shuriken) node).getOwner() != this) { // a bullet which is not our own struck us
            System.out.println("Bullet struck player");
            BloodSplatter bloodSplatter = new BloodSplatter(node.getX(), node.getY(), this.bloodSplatterSpriteFrameName);
            this.getParent().add(bloodSplatter);
        }
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getSpeed() {
        return this.speed;
    }

    public Direction getDirection() {
        return this.direction;
    }

    private void move() {
        if (LEFT && (this.getScreenX() - this.speed) >= 0) {
            this.direction = Direction.LEFT_FACING;
            this.setFlippedX(true);
            this.setX(this.getX() - this.speed);
        }

        if (RIGHT && (this.getScreenX() + speed) + getWidth() <= this.getParent().getWidth()) {
            this.direction = Direction.RIGHT_FACING;
            this.setFlippedX(false);
            this.setX(this.getX() + this.speed);
        }

        if (UP && (this.getScreenY() - this.speed) >= 0) {
            this.setY(this.getY() - this.speed);
        }

        if (DOWN && (this.getScreenY() + this.speed) + this.getHeight() <= this.getParent().getHeight()) {
            this.setY(this.getY() + this.speed);
        }
    }

    public void shoot() {
        int shurikenX = (int) (this.getX() + this.getWidth() / 2);
        int shurikenY = (int) (this.getY() + this.getHeight() / 2);
        Shuriken shuriken = new Shuriken(shurikenX, shurikenY, this.bulletSpriteFrameName, this);
        this.getParent().add(shuriken);
        AudioEngine.getInstance().playSound(getClass().getResource("assets/sounds/shot.wav"), 0.5f);
    }
}
