package game;

import java.awt.Graphics2D;
import game.core.StateManager;
import game.input.KeyInput;
import game.input.MouseInput;

public final class Engine {

  public static final float GRAVITY = 19.6f;

  public static void fixedUpdate() {
    StateManager.fixedUpdate();
    KeyInput.fixedUpdate();
    MouseInput.fixedUpdate();
  }

  public static void update() {
    StateManager.update();
  }

  public static void render(Graphics2D g, float alpha) {
    StateManager.render(g, alpha);
  }
}
