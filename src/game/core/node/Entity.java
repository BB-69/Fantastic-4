package game.core.node;

public abstract class Entity extends Node {

  protected float vx, vy;

  protected float lerp(float a, float b, float t) {
    return a + (b - a) * t;
  }
}
