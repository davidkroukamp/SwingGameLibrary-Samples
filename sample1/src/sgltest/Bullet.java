/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sgltest;

import za.co.swinggamelibrary.Animation;
import za.co.swinggamelibrary.Scene;
import za.co.swinggamelibrary.Sprite;

/**
 *
 * @author dkrou
 */
public class Bullet extends Sprite {

    private final Sprite owner;
    private final Scene gp;
    protected int speed, position;
    public boolean LEFT, RIGHT, UP, DOWN;
    //used to keep track of the actual image direction 
    public final static int LEFT_FACING = -1, RIGHT_FACING = 1, DEFAULT_SPEED = 5;
    protected final int conW, conH;

    public Bullet(int x, int y, Animation animation, int pos, int conW, int conH, final GameObject owner, Scene gp) {
        super(x, y, animation);
        UP = false;
        DOWN = false;
        LEFT = false;
        RIGHT = false;
        position = pos;
        this.conW = conW;
        this.conH = conH;
        this.owner = owner;
        setVisible(true);
        setSpeed(DEFAULT_SPEED * 2); // setSpeed(getSpeed() * 2);
        //check which way the game object is facing and make bullet go accrodingly
        if (owner.getPosition() == LEFT_FACING) {
            LEFT = true;
        } else if (owner.getPosition() == RIGHT_FACING) {
            RIGHT = true;
        }
        this.gp = gp;
    }

    public Sprite getOwner() {
        return owner;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getSpeed() {
        return speed;
    }

    public int getPosition() {
        return position;
    }

    @Override
    public void update(long elapsedTime) {
        super.update(elapsedTime);
        move();
    }

    private void move() {
        if ((rectangle.x - speed) <= 0) {
            setVisible(false);
            gp.removeSprite(this);
        } else if (LEFT) {
            position = LEFT_FACING;
            rectangle.x -= speed;
        }

        if ((rectangle.x + speed) + getWidth() >= conW) {
            setVisible(false);
            gp.removeSprite(this);
        } else if (RIGHT) {
            position = RIGHT_FACING;
            rectangle.x += speed;
        }

        if ((rectangle.y - speed) <= 0) {
            setVisible(false);
            gp.removeSprite(this);
        } else if (UP) {
            rectangle.y -= speed;
        }

        if ((rectangle.y + speed) + getHeight() >= conH) {
            setVisible(false);
            gp.removeSprite(this);
        } else if (DOWN) {
            rectangle.y += speed;
        }
    }
}
