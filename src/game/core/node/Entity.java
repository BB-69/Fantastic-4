package game.core.node;

import java.awt.Graphics2D;

public abstract class Entity {

  private boolean isActive = true;
  protected float x, y;
  protected float prevX, prevY;
  protected float vx, vy;

  public abstract void update(); // input, animation

  public void fixedUpdate() {
    prevX = x;
    prevY = y;
  } // physics

  public abstract void render(Graphics2D g, float alpha);

  protected float lerp(float a, float b, float t) {
    return a + (b - a) * t;
  }

  protected void setIsActive(boolean isActive) {
    this.isActive = isActive;
  }

  public boolean isActive() {
    return isActive;
  }
}
