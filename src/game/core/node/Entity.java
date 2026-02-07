package game.core.node;

import java.awt.Graphics2D;

public abstract class Entity extends Node {

  protected float x, y;
  protected float prevX, prevY;
  protected float vx, vy;

  public void fixedUpdate() {
    prevX = x;
    prevY = y;
  }

  public abstract void render(Graphics2D g, float alpha);

  protected float lerp(float a, float b, float t) {
    return a + (b - a) * t;
  }
}
