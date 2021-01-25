/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sgltest;

import java.util.EnumSet;
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
    protected SpriteDirection direction;
    public boolean LEFT, RIGHT, UP, DOWN;
    private final String bulletSpriteFrameName;
    private final String bloodSplatterSpriteFrameName;

    public Player(int x, int y, Animation idleAnimation, SpriteDirection direction, String bulletSpriteFrameName, String bloodSplatterSpriteFrameName, boolean isFlippedX) {
        super(idleAnimation);
        this.setPosition(x, y);
        this.setFlippedX(isFlippedX);
        this.setMovementRestrictedToParent(true, EnumSet.of(Direction.ALL));
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

    public SpriteDirection getDirection() {
        return this.direction;
    }

    private void move() {
        if (LEFT) {
            this.direction = SpriteDirection.LEFT_FACING;
            this.setFlippedX(true);
            this.setX(this.getX() - this.speed);
        }

        if (RIGHT) {
            this.direction = SpriteDirection.RIGHT_FACING;
            this.setFlippedX(false);
            this.setX(this.getX() + this.speed);
        }

        if (UP) {
            this.setY(this.getY() - this.speed);
        }

        if (DOWN) {
            this.setY(this.getY() + this.speed);
        }
    }

    @Override
    public void onMovementRestricted() {
        System.out.println("Movement restricted");
    }

    public void shoot() {
        int shurikenX = (int) (this.getX() + this.getWidth()) - 25;
        int shurikenY = (int) (this.getY() + this.getHeight() / 2) - 25;
        Shuriken shuriken = new Shuriken(shurikenX, shurikenY, this.bulletSpriteFrameName, this);
        this.getParent().add(shuriken);
        AudioEngine.getInstance().playSound(getClass().getResource("assets/sounds/shot.wav"), 0.5f);
    }
}
