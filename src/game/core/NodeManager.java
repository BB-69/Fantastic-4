package game.core;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import game.core.node.Entity;
import game.core.node.Node;

public class NodeManager {
  private List<Node> nodes = new ArrayList<>();
  private List<Node> toAdd = new ArrayList<>();
  private List<Node> toRemove = new ArrayList<>();

  public NodeManager() {
  }

  public NodeManager(List<Node> nList) {
    nodes.addAll(nList);
  }

  public void fixedUpdate() {
    for (Node n : nodes) {
      if (n.isActive() && n instanceof Entity) {
        Entity e = (Entity) n;
        e.fixedUpdate();
      }
    }
  }

  public void update() {
    for (Node n : nodes)
      if (n.isActive())
        n.update();
    nodes.addAll(toAdd);
    nodes.removeAll(toRemove);
    toAdd.clear();
    toRemove.clear();
  }

  public void render(Graphics2D g, float alpha) {
    for (Node n : nodes) {
      if (n.isActive() && n instanceof Entity) {
        Entity e = (Entity) n;
        e.render(g, alpha);
      }
    }
  }

  public void addNode(Node n) {
    toAdd.add(n);
  }

  public void removeNode(Node n) {
    toRemove.add(n);
  }
}
