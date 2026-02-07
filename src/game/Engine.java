package game;

import java.awt.Graphics2D;
import game.core.StateManager;
import game.input.KeyInput;
import game.input.MouseInput;

public final class Engine {

  public static void fixedUpdate() {
    KeyInput.update();
    MouseInput.update();
    StateManager.fixedUpdate();
  }

  public static void update() {
    StateManager.update();
  }

  public static void render(Graphics2D g, float alpha) {
    StateManager.render(g, alpha);
  }
}
