/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sgltest;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import za.co.swinggamelibrary.Animation;
import za.co.swinggamelibrary.AnimationCache;
import za.co.swinggamelibrary.AudioEngine;
import za.co.swinggamelibrary.DesignMetrics;
import za.co.swinggamelibrary.Director;
import za.co.swinggamelibrary.Graphics2DHelper;
import za.co.swinggamelibrary.KeyBinder;
import za.co.swinggamelibrary.Scene;
import za.co.swinggamelibrary.SpriteFrame;
import za.co.swinggamelibrary.SpriteFrameCache;

/**
 *
 * @author dkrou
 */
public class SGLTest {

    private static final Dimension SCREEN_SIZE = new Dimension(1440, 900);
    private static final Dimension DESIGN_SCREEN_SIZE = new Dimension(800, 600);
    private String backgroundMusicId;

    public SGLTest() {
        DesignMetrics.initialise(DESIGN_SCREEN_SIZE, SCREEN_SIZE);
        loadSpritesAndAnimations();
        loadSounds();
        createAndShowGui();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                    if ("Nimbus".equals(info.getName())) {
                        UIManager.setLookAndFeel(info.getClassName());
                        break;
                    }
                }
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
                // If Nimbus is not available, you can set the GUI to another look and feel.
            }

            new SGLTest();
        });
    }

    private void createAndShowGui() {
        JFrame frame = new JFrame("Sample 1");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);

        // Designmetrics should be initialised before creating an instance of the director
        Director director = new Director();
        // show FPS and objects rendered counter
        director.setRenderDebugInfo(true);
        // draw red rectangles around nodes for debugging purposes (helps check collisions etc)
        director.setDrawDebugMasks(true);

        // create the Scene which will hold the player sprites
        final Scene scene = new Scene();
        director.setScene(scene);

        JPanel buttonPanel = new JPanel();
        // create buttons to control game loop start pause/resume and stop
        final JButton startButton = new JButton("Start");
        final JButton pauseButton = new JButton("Pause");
        pauseButton.setEnabled(false);
        final JButton stopButton = new JButton("Stop");
        stopButton.setEnabled(false);

        //add listeners to buttons 
        startButton.addActionListener((ActionEvent ae) -> {
            //clear enitites currently in arrayd
            scene.removeAll();

            //create player 1 which can be controlled by W,S,A,D and SPACE to shoot
            final Player player1 = new Player(0, 0,
                    AnimationCache.getInstance().getAnimation("player1IdleAnimation"), SpriteDirection.RIGHT_FACING, "shuriken_standard.png", "blood_splatter_1.png", false);

            //create player 2 which can be controlled by UP,DOWN,LEFT,RIGHT and Numpad ENTER to shoot
            final Player player2 = new Player(700, 500,
                    AnimationCache.getInstance().getAnimation("player2IdleAnimation"), SpriteDirection.LEFT_FACING, "shuriken_standard.png", "blood_splatter_2.png", true);

            // add gameobjetcs to the gamepanel
            scene.add(player1);
            scene.add(player2);

            // setup key bidnings for each player
            setupPlayer1KeyBindings(director, player1);
            setupPlayer2KeyBindings(director, player2);

            director.start();

            startButton.setEnabled(false);
            pauseButton.setEnabled(true);
            stopButton.setEnabled(true);
            AudioEngine.getInstance().pauseMusic(backgroundMusicId);
        });

        pauseButton.addActionListener((ActionEvent ae) -> {
            // checks if the game is paused or not and reacts by either resuming or pausing the game
            if (director.isPaused()) {
                director.resume();
                AudioEngine.getInstance().pauseMusic(backgroundMusicId);
            } else {
                director.pause();
                AudioEngine.getInstance().resumeMusic(backgroundMusicId);
            }
            if (pauseButton.getText().equals("Pause")) {
                pauseButton.setText("Resume");
            } else {
                pauseButton.setText("Pause");
                director.requestFocusInWindow();//button might have focus
            }
        });

        stopButton.addActionListener((ActionEvent ae) -> {
            // if we want enitites to be cleared and a blank panel shown
            // scene.removeAll();    
            director.stop();

            pauseButton.setText("Pause");
            startButton.setEnabled(true);
            pauseButton.setEnabled(false);
            stopButton.setEnabled(false);
            AudioEngine.getInstance().resumeMusic(backgroundMusicId);
        });

        // add buttons to panel
        buttonPanel.add(startButton);
        buttonPanel.add(pauseButton);
        buttonPanel.add(stopButton);
        // add game panel and button panel to jframe
        frame.add(director, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);
        frame.pack();
        frame.setVisible(true);

        backgroundMusicId = AudioEngine.getInstance().playMusic(getClass().getResource("assets/sounds/mystical_theme.wav"), true, 0.7f);
    }

    private void loadSpritesAndAnimations() {
        try {
            // player 1 idle blinking
            File player1IdlePlist = new File(getClass().getResource("assets/characters/player/player_1_idle_blinking.plist").toURI());
            BufferedImage player1IdleSpriteSheet = ImageIO.read(getClass().getResourceAsStream("assets/characters/player/player_1_idle_blinking.png"));
            SpriteFrameCache.getInstance().addSpriteFramesWithFile("player1IdleFrames", player1IdlePlist, player1IdleSpriteSheet);

            // player 1 blood splatter
            SpriteFrameCache.getInstance().addSpriteFrameWithKey("player1BloodSplatterSprite", new SpriteFrame("blood_splatter_1.png", SGLTest.createColouredImage(Color.RED, 50, 50, false)));

            // player 2 idle blinking
            File player2IdlePlist = new File(getClass().getResource("assets/characters/player/player_2_idle_blinking.plist").toURI());
            BufferedImage player2IdleSpriteSheet = ImageIO.read(getClass().getResourceAsStream("assets/characters/player/player_2_idle_blinking.png"));
            SpriteFrameCache.getInstance().addSpriteFramesWithFile("player2IdleFrames", player2IdlePlist, player2IdleSpriteSheet);

            // player 2 blood splatter
            SpriteFrameCache.getInstance().addSpriteFrameWithKey("player2BloodSplatterSprite", new SpriteFrame("blood_splatter_2.png", SGLTest.createColouredImage(Color.PINK, 50, 50, false)));

            // shuriken
            SpriteFrameCache.getInstance().addSpriteFrameWithKey("shurikenSprite", new SpriteFrame("shuriken_standard.png", ImageIO.read(getClass().getResourceAsStream("assets/weapons/shuriken_standard.png"))));
        } catch (URISyntaxException | IOException | ParserConfigurationException | SAXException ex) {
            Logger.getLogger(SGLTest.class.getName()).log(Level.SEVERE, null, ex);
        }

        // load animation for player1 into AnimationCache
        AnimationCache.getInstance().addAnimation("player1IdleAnimation",
                Animation.createWithSpriteFrames(SpriteFrameCache.getInstance().getSpriteFramesByKey("player1IdleFrames"), 0.06f, 0));

        // load animation for player 2 into AnimationCache
        AnimationCache.getInstance().addAnimation("player2IdleAnimation",
                Animation.createWithSpriteFrames(SpriteFrameCache.getInstance().getSpriteFramesByKey("player2IdleFrames"), 0.06f, 0));
    }

    private void loadSounds() {
        AudioEngine.getInstance().cacheSound(getClass().getResource("assets/sounds/shot.wav"));
        AudioEngine.getInstance().cacheMusic(getClass().getResource("assets/sounds/mystical_theme.wav"));
    }

    private void setupPlayer1KeyBindings(Director director, Player player1) {
        KeyBinder.putKeyBindingOnPressAndRelease(director, KeyBinder.WHEN_IN_FOCUSED_WINDOW,
                KeyEvent.VK_D,
                (ActionEvent ae) -> {
                    player1.RIGHT = true;
                }, "D pressed",
                (ActionEvent ae) -> {
                    player1.RIGHT = false;
                }, "D released");

        KeyBinder.putKeyBindingOnPressAndRelease(director, KeyBinder.WHEN_IN_FOCUSED_WINDOW,
                KeyEvent.VK_A,
                (ActionEvent ae) -> {
                    player1.LEFT = true;
                }, "A pressed",
                (ActionEvent ae) -> {
                    player1.LEFT = false;
                }, "A released");

        KeyBinder.putKeyBindingOnPressAndRelease(director, KeyBinder.WHEN_IN_FOCUSED_WINDOW,
                KeyEvent.VK_W,
                (ActionEvent ae) -> {
                    player1.UP = true;
                }, "W pressed",
                (ActionEvent ae) -> {
                    player1.UP = false;
                }, "W released");
        KeyBinder.putKeyBindingOnPressAndRelease(director, KeyBinder.WHEN_IN_FOCUSED_WINDOW,
                KeyEvent.VK_S,
                (ActionEvent ae) -> {
                    player1.DOWN = true;
                }, "S pressed",
                (ActionEvent ae) -> {
                    player1.DOWN = false;
                }, "S released");

        KeyBinder.putKeyBindingOnPress(director, KeyBinder.WHEN_IN_FOCUSED_WINDOW,
                KeyEvent.VK_SPACE,
                (ActionEvent ae) -> {
                    player1.shoot();
                }, "Space pressed");
    }

    public void setupPlayer2KeyBindings(final Director director, final Player player2) {
        KeyBinder.putKeyBindingOnPressAndRelease(director, KeyBinder.WHEN_IN_FOCUSED_WINDOW,
                KeyEvent.VK_RIGHT,
                (ActionEvent ae) -> {
                    player2.RIGHT = true;
                }, "right pressed",
                (ActionEvent ae) -> {
                    player2.RIGHT = false;
                }, "right released");

        KeyBinder.putKeyBindingOnPressAndRelease(director, KeyBinder.WHEN_IN_FOCUSED_WINDOW,
                KeyEvent.VK_LEFT,
                (ActionEvent ae) -> {
                    player2.LEFT = true;
                }, "left pressed",
                (ActionEvent ae) -> {
                    player2.LEFT = false;
                }, "left released");

        KeyBinder.putKeyBindingOnPressAndRelease(director, KeyBinder.WHEN_IN_FOCUSED_WINDOW,
                KeyEvent.VK_UP,
                (ActionEvent ae) -> {
                    player2.UP = true;
                }, "up pressed",
                (ActionEvent ae) -> {
                    player2.UP = false;
                }, "up released");
        KeyBinder.putKeyBindingOnPressAndRelease(director, KeyBinder.WHEN_IN_FOCUSED_WINDOW,
                KeyEvent.VK_DOWN,
                (ActionEvent ae) -> {
                    player2.DOWN = true;
                }, "down pressed",
                (ActionEvent ae) -> {
                    player2.DOWN = false;
                }, "down released");

        KeyBinder.putKeyBindingOnPress(director, KeyBinder.WHEN_IN_FOCUSED_WINDOW,
                KeyEvent.VK_ENTER,
                (ActionEvent ae) -> {
                    player2.shoot();
                }, "Enter pressed");
    }

    public static BufferedImage createColouredImage(Color color, int w, int h, boolean circular) {
        BufferedImage img = new BufferedImage(w, h, BufferedImage.TRANSLUCENT);
        Graphics2D g2 = img.createGraphics();
        Graphics2DHelper.applyRenderHints(g2);
        g2.setColor(color);
        if (!circular) {
            g2.fillRect(0, 0, img.getWidth(), img.getHeight());
        } else {
            g2.fillOval(0, 0, w, h);
        }
        g2.dispose();
        return img;
    }

}