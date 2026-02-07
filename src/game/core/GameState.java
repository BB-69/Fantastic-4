package game.core;

import java.awt.Graphics2D;

public abstract class GameState {

  protected String stateName;
  protected NodeManager nodeManager;

  public GameState() {
    nodeManager = new NodeManager();
  }

  public String getStateName() {
    return stateName;
  }

  public void fixedUpdate() {
    nodeManager.fixedUpdate();
  }

  public void update() {
    nodeManager.update();
  }

  public void render(Graphics2D g, float alpha) {
    nodeManager.render(g, alpha);
  }
}
