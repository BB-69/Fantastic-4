package game.core.node;

public abstract class Entity extends Node {

  protected float vx = 0f, vy = 0f;

  public Entity() {
  }

  public Entity(Node parent) {
    super(parent);
  }
}
