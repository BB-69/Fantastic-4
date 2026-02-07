package game.core;

import java.awt.Graphics2D;

public abstract class GameState {
  protected String stateName;

  public String getStateName() {
    return stateName;
  }

  public abstract void fixedUpdate();

  public abstract void update();

  public abstract void render(Graphics2D g, float alpha);
}
