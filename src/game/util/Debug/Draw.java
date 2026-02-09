package game.util.Debug;

import java.awt.Color;
import java.awt.Graphics2D;

public class Draw {
  public static void dot(Graphics2D g, float alpha) {
    g.setColor(Color.RED);
    g.fillOval(-2, -2, 4, 4);
  }
}
