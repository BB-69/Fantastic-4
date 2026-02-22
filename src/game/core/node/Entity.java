package game.core.node;

import game.Engine;
import game.util.Time;

public abstract class Entity extends Node {

  public float vx = 0f, vy = 0f;
  public boolean gravityOn = false;

  public Entity() {
  }

  public Entity(Node parent) {
    super(parent);
  }

  @Override
  public void fixedUpdate() {
    super.fixedUpdate();

    if (gravityOn)
      vy += Engine.GRAVITY;

    x += vx * Time.FIXED_DELTA;
    y += vy * Time.FIXED_DELTA;
  }
}
