package game.core.node;

import java.awt.Graphics2D;

public abstract class Node {

  protected Node parent;

  private boolean isActive = true;

  protected int layer = 0;

  protected float x = 0f, y = 0f;
  protected float prevX = 0f, prevY = 0f;

  public Node() {
  }

  public Node(Node parent) {
    this.parent = parent;
  }

  protected void setIsActive(boolean isActive) {
    this.isActive = isActive;
  }

  public boolean isActive() {
    return isActive;
  }

  public int getLayer() {
    return layer;
  }

  public abstract void update();

  public void fixedUpdate() {
    prevX = x;
    prevY = y;
  }

  public abstract void render(Graphics2D g, float alpha);

  public void setPosition(float x, float y) {
    this.x = x;
    this.y = y;
  }

  public float getWorldX() {
    return parent != null ? parent.getWorldX() + x : x;
  }

  public float getWorldY() {
    return parent != null ? parent.getWorldY() + y : y;
  }
}
