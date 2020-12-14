/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sgltest;

import java.util.ArrayList;
import za.co.swinggamelibrary.Scene;
import za.co.swinggamelibrary.Sprite;

/**
 *
 * @author dkrou
 */
public class TestScene extends Scene {

    public TestScene(int fps, int w, int h) {
        super(fps, w, h);
    }

    @Override
    public void update(long elapsedTime) {
        super.update(elapsedTime);
        checkCollisions();
    }

    public void checkCollisions() { //do Sprite collision checks here
        ArrayList<Sprite> spritess = new ArrayList<>(this.getSprites());
        for (Sprite go : spritess) {//iterate through all game objects
            if (go instanceof Bullet) {//check if its a bullet
                //get Bullet instance and its owner
                Bullet bullet = (Bullet) go;
                Sprite bulletOwner = bullet.getOwner();

                for (Sprite go2 : spritess) {//go trhough each object again
                    if (!go2.equals(bullet)) {//make sure we are not checking the bullet against itself
                        if (bullet.intersects(go2) && !go2.equals(bulletOwner)) {//check if the bullets intersects any other object besides its owner
                            System.out.println("A bullet hit a Sprite other than its owner");
                            //bullet has hit get rid of it
                            bullet.setVisible(false);
                            this.removeSprite(bullet);
                        }
                    }
                }
            } else {//its not a Nullte thus must be a Sprite
                for (Sprite go2 : spritess) {//go through each Sprite again
                    if (!go.equals(go2)) {//make sure its not checking itself
                        if (!(go2 instanceof Bullet)) {//make sure we dont chec bulltes as this has been done above
                            if (go.intersects(go2)) {//check if the 2 gameobjects are intersecting each other
                                System.out.println("A Sprite intersected another");
                            }
                        }
                    }
                }
            }
        }
    }

}
