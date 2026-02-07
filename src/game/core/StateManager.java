package game.core;

import java.awt.Graphics2D;

public class StateManager {

  private static GameState current;

  public static void setState(GameState state) {
    current = state;
  }

  public static String currentState() {
    return current.getStateName();
  }

  public static void fixedUpdate() {
    if (current != null)
      current.fixedUpdate();
  }

  public static void update() {
    if (current != null)
      current.update();
  }

  public static void render(Graphics2D g, float alpha) {
    if (current != null)
      current.render(g, alpha);
  }
}
