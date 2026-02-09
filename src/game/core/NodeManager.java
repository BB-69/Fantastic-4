package game.core;

import java.awt.Graphics2D;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import game.core.node.Node;

public class NodeManager {
  private LayerManager layerManager = new LayerManager();
  private HashMap<Integer, HashSet<Node>> toAdd = new HashMap<>();
  private HashMap<Integer, HashSet<Node>> toRemove = new HashMap<>();

  public NodeManager() {
  }

  public NodeManager(HashMap<Integer, HashSet<Node>> entries) {
    for (Map.Entry<Integer, HashSet<Node>> e : entries.entrySet()) {
      layerManager.getOrCreateLayer(e.getKey()).add(e.getValue());
    }
  }

  public void fixedUpdate() {
    layerManager.fixedUpdate();
  }

  public void update() {
    layerManager.update();
    for (Map.Entry<Integer, HashSet<Node>> e : toRemove.entrySet()) {
      layerManager.getOrCreateLayer(e.getKey()).remove(e.getValue());
    }
    for (Map.Entry<Integer, HashSet<Node>> e : toAdd.entrySet()) {
      layerManager.getOrCreateLayer(e.getKey()).add(e.getValue());
    }
    toRemove.clear();
    toAdd.clear();
  }

  public void render(Graphics2D g, float alpha) {
    layerManager.render(g, alpha);
  }

  public void addNode(Node n) {
    n.setNodeManagerInstance(this);
    for (Node node : getNodeRecursive(n, new HashSet<>())) {
      int layer = node.getLayer();
      toAdd.computeIfAbsent(layer, k -> new HashSet<>()).add(node);
    }
  }

  public void addNode(Node... nodes) {
    for (Node n : nodes)
      addNode(n);
  }

  public void removeNode(Node n) {
    int layer = n.getLayer();
    toRemove.computeIfAbsent(layer, k -> new HashSet<>()).add(n);
  }

  public void removeNode(Node... nodes) {
    for (Node n : nodes)
      removeNode(n);
  }

  private HashSet<Node> getNodeRecursive(Node n, HashSet<Node> children) {
    children.add(n);

    for (Node child : n.getChildren()) {
      getNodeRecursive(child, children);
    }

    return children;
  }
}
