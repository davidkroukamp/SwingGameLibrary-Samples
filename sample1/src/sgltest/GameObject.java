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
public class GameObject extends Sprite {

    protected int speed, position;
    public boolean LEFT, RIGHT, UP, DOWN;
    //used to keep track of the actual image direction 
    public final static int LEFT_FACING = -1, RIGHT_FACING = 1, DEFAULT_SPEED = 5;
    protected final int conW, conH;

    public GameObject(int x, int y, Animation animation, int pos, int conW, int conH) {
        super(x, y, animation);
        setVisible(true);
        UP = false;
        DOWN = false;
        LEFT = false;
        RIGHT = false;
        speed = DEFAULT_SPEED;
        position = pos;
        this.conW = conW;
        this.conH = conH;
    }

    public void setSpeed(int s) {
        speed = s;
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
        if (LEFT && (rectangle.x - speed) >= 0) {
            position = LEFT_FACING;
            rectangle.x -= speed;
        }
        
        if (RIGHT && (rectangle.x + speed) + getWidth() <= conW) {
            position = RIGHT_FACING;
            rectangle.x += speed;
        }
        
        if (UP && (rectangle.y - speed) >= 0) {
            rectangle.y -= speed;
        }
        
        if (DOWN && (rectangle.y + speed) + getHeight() <= conH) {
            rectangle.y += speed;
        }
    }

}
