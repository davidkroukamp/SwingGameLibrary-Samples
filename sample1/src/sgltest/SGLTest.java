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
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;
import za.co.swinggamelibrary.Animation;
import za.co.swinggamelibrary.AnimationCache;
import za.co.swinggamelibrary.Graphics2DHelper;
import za.co.swinggamelibrary.ImageScaler;
import za.co.swinggamelibrary.SpriteFrame;
import za.co.swinggamelibrary.SpriteFrameCache;

/**
 *
 * @author dkrou
 */
public class SGLTest {

    public static final int FPS = 60;
    public static final int WIDTH = 800, HEIGHT = 600;
    // TODO add design resolution
    // TODO add small resolution
    // TODO add medium resolution
    // TODO add large resolution
    public static final Dimension STANDARD_IMAGE_SCREEN_SIZE = new Dimension(800, 600);
    private ImageScaler is = new ImageScaler(STANDARD_IMAGE_SCREEN_SIZE, new Dimension(WIDTH, HEIGHT));//create instance to allow creating of image sizes for current screen size and width

    public SGLTest() {
        loadSpritesAndAnimationsIntoCache();
        createAndShowGui();
    }

    //code starts here
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

            new SGLTest();//create an instance which incudes GUI etc
        });
    }

    private void createAndShowGui() {
        JFrame frame = new JFrame(SGLTest.class.getSimpleName());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);

        final TestScene gamePanel = new TestScene(FPS, WIDTH, HEIGHT);

        JPanel buttonPanel = new JPanel();

        //create buttons to control game loop start pause/resume and stop
        final JButton startButton = new JButton("Start");

        final JButton pauseButton = new JButton("Pause");
        pauseButton.setEnabled(false);

        final JButton stopButton = new JButton("Stop");
        stopButton.setEnabled(false);

        //add listeners to buttons 
        startButton.addActionListener((ActionEvent ae) -> {
            //clear enitites currently in array
            gamePanel.clearSprites();

            //get starting position according to current screen size
            //the position 0f 200,300 is on standrad screen size of 800,600
            int startingXPlayer1 = (int) (200 * is.getWidthScaleFactor());
            int startingYPlayer1 = (int) (300 * is.getHeightScaleFactor());

            //create player 1 game onject which can be controlled by W,S,A,D and SPACE to shoot
            final GameObject player1GameObject = new GameObject(startingXPlayer1, startingYPlayer1, AnimationCache.getInstance().getAnimation("player1Animation"), GameObject.RIGHT_FACING, gamePanel.getWidth(), gamePanel.getHeight());

            //get starting position according to current screen size
            //the position 0f 400,100 is on standrad screen size of 800,600
            int startingXPlayer2 = (int) (400 * is.getWidthScaleFactor());
            int startingYPlayer2 = (int) (100 * is.getHeightScaleFactor());

            final GameObject player2GameObject = new GameObject(startingXPlayer2, startingYPlayer2, AnimationCache.getInstance().getAnimation("player2Animation"), GameObject.LEFT_FACING, gamePanel.getWidth(), gamePanel.getHeight());

            //add gameobjetcs to the gamepanel
            gamePanel.addSprite(player1GameObject);
            gamePanel.addSprite(player2GameObject);

            GameKeyBindings gameKeyBindings = new GameKeyBindings(gamePanel, player1GameObject, player2GameObject);

            gamePanel.start();

            startButton.setEnabled(false);
            pauseButton.setEnabled(true);
            stopButton.setEnabled(true);
        });

        pauseButton.addActionListener((ActionEvent ae) -> {
            //checks if the game is paused or not and reacts by either resuming or pausing the game
            if (gamePanel.isPaused()) {
                gamePanel.resume();
            } else {
                gamePanel.pause();
            }
            if (pauseButton.getText().equals("Pause")) {
                pauseButton.setText("Resume");
            } else {
                pauseButton.setText("Pause");
                gamePanel.requestFocusInWindow();//button might have focus
            }
        });

        stopButton.addActionListener((ActionEvent ae) -> {
            gamePanel.stop();
            /*
            //if we want enitites to be cleared and a blank panel shown
            gp.clearGameObjects();
            gp.repaint();
             */
            if (!pauseButton.getText().equals("Pause")) {
                pauseButton.setText("Pause");
            }
            startButton.setEnabled(true);
            pauseButton.setEnabled(false);
            stopButton.setEnabled(false);
        });

        //add buttons to panel
        buttonPanel.add(startButton);
        buttonPanel.add(pauseButton);
        buttonPanel.add(stopButton);
        //add game panel and button panel to jframe
        frame.add(gamePanel, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        frame.pack();
        frame.setVisible(true);
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

    private void loadPlayer1ImagesToSpriteFrameCache() {
        ArrayList<BufferedImage> images = new ArrayList<>();
        images.add(createColouredImage(Color.RED, 100, 100, false));
        images.add(createColouredImage(Color.BLACK, 100, 100, false));
        images.add(createColouredImage(Color.GREEN, 100, 100, false));
        //create arraylist of images scaled for the current screen size
        ArrayList<BufferedImage> scaledImages = is.scaleImages(images);
        for (int i = 0; i < scaledImages.size(); i++) {
            SpriteFrameCache.getInstance().addSpriteFramesWithKey("player1Animation", new SpriteFrame("player1_" + i + ".png", scaledImages.get(i)));
        }
    }

    private void loadPlayer2ImagesToSpriteFrameCache() {
        ArrayList<BufferedImage> images = new ArrayList<>();
        images.add(createColouredImage(Color.CYAN, 100, 100, false));
        images.add(createColouredImage(Color.YELLOW, 100, 100, false));
        images.add(createColouredImage(Color.MAGENTA, 100, 100, false));
        //create arraylist of images scaled for the current screen size
        ArrayList<BufferedImage> scaledImages = is.scaleImages(images);
        for (int i = 0; i < scaledImages.size(); i++) {
            SpriteFrameCache.getInstance().addSpriteFramesWithKey("player2Animation", new SpriteFrame("player2_" + i + ".png", scaledImages.get(i)));
        }
    }

    private void loadSpritesAndAnimationsIntoCache() {
        // load images for player 1 bullet into SpritFrameCache and AnimationCache
        SpriteFrameCache.getInstance().addSpriteFramesWithKey("bullet1Animation", new SpriteFrame("bullet_1.png", SGLTest.createColouredImage(Color.ORANGE, 10, 10, true)));
        AnimationCache.getInstance().addAnimation("bullet1Animation", new Animation(SpriteFrameCache.getInstance().getSpriteFramesByKey("bullet1Animation"), 0, 0));

        //laod images for player 1 into SpritFrameCache
        loadPlayer1ImagesToSpriteFrameCache();
        // load animation for player1 nto AnimationCache
        AnimationCache.getInstance().addAnimation("player1Animation", new Animation(SpriteFrameCache.getInstance().getSpriteFramesByKey("player1Animation"), 300, 0));

        // load images for player 2 bullet into SpritFrameCache and AnimationCache
        SpriteFrameCache.getInstance().addSpriteFramesWithKey("bullet2Animation", new SpriteFrame("bullet_2.png", SGLTest.createColouredImage(Color.MAGENTA, 10, 10, true)));
        AnimationCache.getInstance().addAnimation("bullet2Animation", new Animation(SpriteFrameCache.getInstance().getSpriteFramesByKey("bullet2Animation"), 200, 0));

        //laod images for player 2 into SpritFrameCache
        loadPlayer2ImagesToSpriteFrameCache();
        // load animation for player 2 into AnimationCache
        AnimationCache.getInstance().addAnimation("player2Animation", new Animation(SpriteFrameCache.getInstance().getSpriteFramesByKey("player2Animation"), 200, 0));
    }

}
