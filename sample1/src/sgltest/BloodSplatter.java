/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sgltest;

import za.co.swinggamelibrary.Sprite;


/**
 *
 * @author dkrou
 */
public class BloodSplatter extends Sprite {
    
    public BloodSplatter(int x, int y, String spriteFrameName) {
        super(spriteFrameName);
        this.setPosition(x, y);
    }
}