package game.core.node;

import java.awt.Graphics2D;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import game.core.NodeManager;
import game.util.Id;

public abstract class Node {

  private NodeManager nodeManagerInstance;

  protected Node parent;
  protected HashSet<Node> children = new HashSet<>();

  protected String id = Id.getUniqueId("node");
  protected boolean isActive = true;

  protected int layer = 0;

  public float x = 0f, y = 0f;
  protected float prevX = 0f, prevY = 0f;

  public Node() {
  }

  public Node(Node parent) {
    setParent(parent);
    NodeManager instance = parent.getNodeManagerInstance();
    if (this.getNodeManagerInstance() == null && instance != null)
      this.setNodeManagerInstance(instance);
  }

  public NodeManager getNodeManagerInstance() {
    return nodeManagerInstance;
  }

  public void setNodeManagerInstance(NodeManager instance) {
    if (this.nodeManagerInstance != instance) {
      this.nodeManagerInstance = instance;
    }
    for (Node c : children)
      c.setNodeManagerInstance(instance);
  }

  private void tryGetNSetNodeManagerInstance(Node parent) {
    if (parent == null)
      return;

    NodeManager instance = parent.getNodeManagerInstance();
    if (this.nodeManagerInstance == null && instance != null) {
      this.setNodeManagerInstance(instance);
      instance.addNode(this);
    }
  }

  public void setParent(Node parent) {
    if (this.parent == parent)
      return;
    tryGetNSetNodeManagerInstance(parent);

    if (parent == null && this.parent != null) {
      this.parent.removeChild(this);
      this.parent = null;
      return;
    }

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

  public void removeChild(Node child) {
    if (!this.children.contains(child))
      return;

    this.children.remove(child);
    if (child.getParent() != null)
      child.setParent(null);
  }

  public void removeChildren(Node... children) {
    removeChildren(Arrays.asList(children));
  }

  public void removeChildren(List<Node> children) {
    for (Node c : children)
      removeChild(c);
  }

  public void clearChildren() {
    for (Node c : new HashSet<>(children))
      removeChild(c);
    children.clear();
  }

  public HashSet<Node> getChildren() {
    return children;
  }

  public void destroy() {
    setParent(null);
    clearChildren();
    if (nodeManagerInstance != null) {
      nodeManagerInstance.removeNode(this);
      nodeManagerInstance = null;
    }
  }

  public void destroyRecursive() {
    for (Node c : new HashSet<>(children))
      c.destroyRecursive();
    this.destroy();
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

  public float getPrevX() {
    return prevX;
  }

  public float getPrevY() {
    return prevY;
  }

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

  public void setWorldPosition(float worldX, float worldY) {
    setWorldX(worldX);
    setWorldY(worldY);
  }

  public void setWorldX(float worldX) {
    if (parent != null) {
      this.x = worldX - parent.getWorldX();
    } else {
      this.x = worldX;
    }
  }

  public void setWorldY(float worldY) {
    if (parent != null) {
      this.y = worldY - parent.getWorldY();
    } else {
      this.y = worldY;
    }
  }
}
