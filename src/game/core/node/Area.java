package game.core.node;

import java.awt.Point;

import game.core.graphics.CanRotate;
import game.input.MouseInput;

public abstract class Area extends Entity implements CanRotate {

  public float w = 0f, h = 0f;
  protected float prevW = 0f, prevH = 0f;
  public float rotation = 0f;
  protected float rotationVelocity = 0f;

  public Area() {
    super();
  }

  public Area(Node parent) {
    super(parent);
  }

  @Override
  public void fixedUpdate() {
    super.fixedUpdate();

    prevW = w;
    prevH = w;

    rotation += rotationVelocity;
  }

  public void setSize(float w, float h) {
    this.w = w;
    this.h = h;
  }

  @Override
  public void setRotation(float r) {
    this.rotation = r;
  }

  @Override
  public void setRotationVelocity(float v) {
    this.rotationVelocity = v;
  }

  public boolean isMouseInside() {
    // Translate mouse to area center
    Point mousePos = MouseInput.getPosition();
    float dx = mousePos.x - getWorldX();
    float dy = mousePos.y - getWorldY();

    // Un-rotate the point
    float cos = (float) Math.cos(-rotation);
    float sin = (float) Math.sin(-rotation);

    float localX = dx * cos - dy * sin;
    float localY = dx * sin + dy * cos;

    // AABB check
    return Math.abs(localX) <= w * 0.5f &&
        Math.abs(localY) <= h * 0.5f;
  }
}
