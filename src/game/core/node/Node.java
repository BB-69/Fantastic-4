package game.core.node;

import java.awt.Graphics2D;

public abstract class Node {

  private boolean isActive = true;

  protected float x, y;
  protected float prevX, prevY;

  protected void setIsActive(boolean isActive) {
    this.isActive = isActive;
  }

  public boolean isActive() {
    return isActive;
  }

  public abstract void update();

  public void fixedUpdate() {
    prevX = x;
    prevY = y;
  }

  public abstract void render(Graphics2D g, float alpha);
}
