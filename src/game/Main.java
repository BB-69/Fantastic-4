package game;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

import javax.swing.JFrame;

public class Main {

  public static void main(String[] args) {
    System.out.println();

    JFrame frame = new JFrame("Java Game Skeleton");
    GameCanvas canvas = new GameCanvas();

    frame.add(canvas);
    frame.pack();
    // frame.setLocationRelativeTo(null);
    // frame.setResizable(false);
    // frame.setVisible(true);
    GraphicsDevice gd = GraphicsEnvironment
        .getLocalGraphicsEnvironment()
        .getDefaultScreenDevice();

    if (gd.isFullScreenSupported()) {
      gd.setFullScreenWindow(frame);
    } else {
      frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
      frame.setVisible(true);
    }

    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    canvas.requestFocus();

    Game.init(canvas);
    new GameLoop(canvas).start();
  }
}
