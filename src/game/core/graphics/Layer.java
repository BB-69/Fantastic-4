package game.core.graphics;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import game.core.node.Node;
import game.core.signal.Signal;

public class Layer {
  public final int index;
  private final List<Node> nodes = new ArrayList<>();
  private final List<Node> toRemove = new ArrayList<>();

  private Signal signalSendNodeTo;

  public Layer(int index, Signal signalSendNodeTo) {
    this.index = index;
    this.signalSendNodeTo = signalSendNodeTo;
  }

  public void add(Node n) {
    nodes.add(n);
  }

  public void add(List<Node> nList) {
    nodes.addAll(nList);
  }

  public void remove(Node n) {
    nodes.remove(n);
  }

  public void remove(List<Node> nList) {
    nodes.removeAll(nList);
  }

  public void fixedUpdate() {
    for (Node n : nodes) {
      if (n.isActive())
        n.fixedUpdate();
    }
  }

  public void update() {
    for (Node n : nodes) {
      if (n.isActive())
        n.update();
      if (n.getLayer() != index) {
        toRemove.add(n);
        signalSendNodeTo.emit(n, index);
      }
    }
    nodes.removeAll(toRemove);
  }

  public void render(Graphics2D g, float alpha) {
    for (Node n : nodes) {
      if (n.isActive())
        n.render(g, alpha);
    }
  }

  public void onSendNodeTo(Object... args) {
    if ((int) args[1] == index)
      add((Node) args[0]);
  }
}
