package game.core.node;

public abstract class Entity extends Node {

  protected float vx = 0f, vy = 0f;

  protected float lerp(float a, float b, float t) {
    return a + (b - a) * t;
  }
}
