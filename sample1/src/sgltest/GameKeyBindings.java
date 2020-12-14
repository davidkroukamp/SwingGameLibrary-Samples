/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sgltest;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import za.co.swinggamelibrary.AnimationCache;
import za.co.swinggamelibrary.KeyBinder;
import za.co.swinggamelibrary.Scene;

/**
 *
 * @author dkrou
 */
public class GameKeyBindings {

    public GameKeyBindings(final Scene gp, final GameObject player1GameObject, final GameObject player2GameObject) {
        setupPlayer1KeyBindings(gp, player1GameObject);
        setupPlayer2KeyBindings(gp, player2GameObject);
    }

    private void setupPlayer1KeyBindings(Scene gp, GameObject player1GameObject) {
        KeyBinder.putKeyBindingOnPressAndRelease(gp, KeyBinder.WHEN_IN_FOCUSED_WINDOW, KeyEvent.VK_D,
                (ActionEvent ae) -> {
                    player1GameObject.RIGHT = true;
                }, "D pressed",
                (ActionEvent ae) -> {
                    player1GameObject.RIGHT = false;
                }, "D released");

        KeyBinder.putKeyBindingOnPressAndRelease(gp, KeyBinder.WHEN_IN_FOCUSED_WINDOW, KeyEvent.VK_A,
                (ActionEvent ae) -> {
                    player1GameObject.LEFT = true;
                }, "A pressed",
                (ActionEvent ae) -> {
                    player1GameObject.LEFT = false;
                }, "A released");

        KeyBinder.putKeyBindingOnPressAndRelease(gp, KeyBinder.WHEN_IN_FOCUSED_WINDOW, KeyEvent.VK_W,
                (ActionEvent ae) -> {
                    player1GameObject.UP = true;
                }, "W pressed",
                (ActionEvent ae) -> {
                    player1GameObject.UP = false;
                }, "W released");
        KeyBinder.putKeyBindingOnPressAndRelease(gp, KeyBinder.WHEN_IN_FOCUSED_WINDOW, KeyEvent.VK_S,
                (ActionEvent ae) -> {
                    player1GameObject.DOWN = true;
                }, "S pressed",
                (ActionEvent ae) -> {
                    player1GameObject.DOWN = false;
                }, "S released");

        KeyBinder.putKeyBindingOnPress(gp, KeyBinder.WHEN_IN_FOCUSED_WINDOW, KeyEvent.VK_SPACE,
                (ActionEvent ae) -> {
                    Bullet bullet = new Bullet((int) (player1GameObject.getX() + player1GameObject.getHeight() / 2), (int) (player1GameObject.getY() + player1GameObject.getWidth() / 2), AnimationCache.getInstance().getAnimation("bullet1Animation"), Bullet.RIGHT_FACING, gp.getWidth(), gp.getHeight(), player1GameObject, gp);
                    gp.addSprite(bullet);//add the bullet to the List of GameObjects that will be drawn on next screen paint
                }, "Space pressed");
    }

    public void setupPlayer2KeyBindings(final Scene gp, final GameObject player2GameObject) {
        KeyBinder.putKeyBindingOnPressAndRelease(gp, KeyBinder.WHEN_IN_FOCUSED_WINDOW, KeyEvent.VK_RIGHT,
                (ActionEvent ae) -> {
                    player2GameObject.RIGHT = true;
                }, "right pressed",
                (ActionEvent ae) -> {
                    player2GameObject.RIGHT = false;
                }, "right released");

        KeyBinder.putKeyBindingOnPressAndRelease(gp, KeyBinder.WHEN_IN_FOCUSED_WINDOW, KeyEvent.VK_LEFT,
                (ActionEvent ae) -> {
                    player2GameObject.LEFT = true;
                }, "left pressed",
                (ActionEvent ae) -> {
                    player2GameObject.LEFT = false;
                }, "left released");

        KeyBinder.putKeyBindingOnPressAndRelease(gp, KeyBinder.WHEN_IN_FOCUSED_WINDOW, KeyEvent.VK_UP,
                (ActionEvent ae) -> {
                    player2GameObject.UP = true;
                }, "up pressed",
                (ActionEvent ae) -> {
                    player2GameObject.UP = false;
                }, "up released");
        KeyBinder.putKeyBindingOnPressAndRelease(gp, KeyBinder.WHEN_IN_FOCUSED_WINDOW, KeyEvent.VK_DOWN,
                (ActionEvent ae) -> {
                    player2GameObject.DOWN = true;
                }, "down pressed",
                (ActionEvent ae) -> {
                    player2GameObject.DOWN = false;
                }, "down released");

        KeyBinder.putKeyBindingOnPress(gp, KeyBinder.WHEN_IN_FOCUSED_WINDOW, KeyEvent.VK_ENTER,
                (ActionEvent ae) -> {
                    Bullet bullet = new Bullet((int) (player2GameObject.getX() + player2GameObject.getHeight() / 2), (int) (player2GameObject.getY() + player2GameObject.getWidth() / 2), AnimationCache.getInstance().getAnimation("bullet2Animation"), Bullet.RIGHT_FACING, gp.getWidth(), gp.getHeight(), player2GameObject, gp);
                    gp.addSprite(bullet);//add the bullet to the List of GameObjects that will be drawn on next screen paint
                }, "Enter pressed");
    }

}
