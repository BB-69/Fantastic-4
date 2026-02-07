package game;

import javax.swing.JFrame;

public class Main {

  public static void main(String[] args) {
    System.out.println();

    JFrame frame = new JFrame("Java Game Skeleton");
    GameCanvas canvas = new GameCanvas();

    frame.add(canvas);
    frame.pack();
    frame.setLocationRelativeTo(null);
    frame.setResizable(false);
    frame.setVisible(true);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    canvas.requestFocus();

    Game.init(canvas);
    new GameLoop(canvas).start();
  }
}
