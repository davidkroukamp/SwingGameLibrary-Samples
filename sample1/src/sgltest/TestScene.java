/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sgltest;

import za.co.swinggamelibrary.Scene;
import za.co.swinggamelibrary.Sprite;

/**
 *
 * @author dkrou
 */
public class TestScene extends Scene {

    public TestScene(int fps, int width, int height) {
        super(fps, width, height);
    }

    @Override
    public void update(long elapsedTime) {
        super.update(elapsedTime);
        checkCollisions();
    }

    public void checkCollisions() { //do Sprite collision checks here
        for (Sprite sprite : sprites) {//iterate through all game objects
            if (sprite instanceof Bullet) {//check if its a bullet
                //get Bullet instance and its owner
                Bullet bullet = (Bullet) sprite;
                Sprite bulletOwner = bullet.getOwner();

                for (Sprite sprite2 : sprites) {//go trhough each object again
                    if (!sprite2.equals(bullet)) {//make sure we are not checking the bullet against itself
                        if (bullet.intersects(sprite2) && !sprite2.equals(bulletOwner)) {//check if the bullets intersects any other object besides its owner
                            System.out.println("A bullet hit a Sprite other than its owner");
                            //bullet has hit get rid of it
                            bullet.removeFromParent();
                        }
                    }
                }
            } else {//its not a Nullte thus must be a Sprite
                for (Sprite sprite2 : sprites) {//go through each Sprite again
                    if (!sprite.equals(sprite2)) {//make sure its not checking itself
                        if (!(sprite2 instanceof Bullet)) {//make sure we dont chec bulltes as this has been done above
                            if (sprite.intersects(sprite2)) {//check if the 2 gameobjects are intersecting each other
                                System.out.println("A Sprite intersected another");
                            }
                        }
                    }
                }
            }
        }
    }

}
