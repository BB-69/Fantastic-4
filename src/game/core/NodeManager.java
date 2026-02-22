package game.core;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import game.core.node.Node;

public class NodeManager {
  private LayerManager layerManager = new LayerManager();
  private HashMap<Integer, List<Node>> toAdd = new HashMap<>();
  private HashMap<Integer, List<Node>> toRemove = new HashMap<>();

  public NodeManager() {
  }

  public NodeManager(HashMap<Integer, List<Node>> entries) {
    for (Map.Entry<Integer, List<Node>> e : entries.entrySet()) {
      layerManager.getOrCreateLayer(e.getKey()).add(e.getValue());
    }
  }

  public void fixedUpdate() {
    layerManager.fixedUpdate();
  }

  public void update() {
    layerManager.update();
    for (Map.Entry<Integer, List<Node>> e : toRemove.entrySet()) {
      layerManager.getOrCreateLayer(e.getKey()).remove(e.getValue());
    }
    for (Map.Entry<Integer, List<Node>> e : toAdd.entrySet()) {
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
    for (Node node : getNodeRecursive(n, new ArrayList<>())) {
      int layer = node.getLayer();
      toAdd.computeIfAbsent(layer, k -> new ArrayList<>()).add(node);
    }
  }

  public void addNode(Node... nodes) {
    for (Node n : nodes)
      addNode(n);
  }

  public void removeNode(Node n) {
    int layer = n.getLayer();
    toRemove.computeIfAbsent(layer, k -> new ArrayList<>()).add(n);
  }

  public void removeNode(Node... nodes) {
    for (Node n : nodes)
      removeNode(n);
  }

  private List<Node> getNodeRecursive(Node n, List<Node> children) {
    children.add(n);

    for (Node child : n.getChildren()) {
      getNodeRecursive(child, children);
    }

    return children;
  }
}
