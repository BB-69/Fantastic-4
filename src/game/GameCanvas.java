package game;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;

public class GameCanvas extends Canvas {

  public static final int WIDTH = 800;
  public static final int HEIGHT = 600;

  public GameCanvas() {
    setPreferredSize(new Dimension(WIDTH, HEIGHT));
    setFocusable(true);
    setBackground(Color.WHITE);
  }
}
