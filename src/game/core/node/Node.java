package game.core.node;

import java.awt.Graphics2D;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import game.util.Id;

public abstract class Node {

  protected Node parent;
  protected HashSet<Node> children = new HashSet<>();

  protected String id = Id.getUniqueId("node");
  protected boolean isActive = true;

  protected int layer = 0;

  protected float x = 0f, y = 0f;
  protected float prevX = 0f, prevY = 0f;

  public Node() {
  }

  public Node(Node parent) {
    setParent(parent);
  }

  public void setParent(Node parent) {
    if (this.parent == parent)
      return;

    this.parent = parent;
    if (!parent.getChildren().contains(this))
      parent.addChild(this);
  }

  public Node getParent() {
    return parent;
  }

  public void addChild(Node child) {
    if (this.children.contains(child))
      return;

    this.children.add(child);
    if (child.getParent() != this)
      child.setParent(this);
  }

  public void addChildren(Node... children) {
    addChildren(Arrays.asList(children));
  }

  public void addChildren(List<Node> children) {
    for (Node c : children)
      addChild(c);
  }

  public HashSet<Node> getChildren() {
    return children;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getId() {
    return id;
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
