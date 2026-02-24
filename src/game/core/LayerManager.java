package game.core;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import game.core.graphics.Layer;
import game.core.node.Node;
import game.core.signal.Signal;

public class LayerManager {

  private final LayerManager Instance = this;

  private final Map<Integer, Layer> layers = new TreeMap<>();

  private Signal signalSendNodeTo = new Signal();

  public LayerManager() {
    signalSendNodeTo.connect(Instance, Instance::onSendNodeTo);
  }

  public Layer getOrCreateLayer(int index) {
    return layers.computeIfAbsent(index, i -> {
      Layer newLayer = new Layer(i, signalSendNodeTo);
      return newLayer;
    });
  }

  public void fixedUpdate() {
    for (Layer l : layers.values()) {
      l.fixedUpdate();
    }
  }

  public void update() {
    for (Layer l : layers.values()) {
      l.update();
    }
  }

  public void render(Graphics2D g, float alpha) {
    for (Layer l : layers.values()) {
      l.render(g, alpha);
    }
  }

  public void destroyAllNodes() {
    for (Layer l : new ArrayList<>(layers.values())) {
      l.destroyAllNodes();
    }
    layers.clear();
  }

  private void receiveNodeAtLayer(Node n, int layer) {
    getOrCreateLayer(layer).add(n);
  }

  private void onSendNodeTo(Object... args) {
    receiveNodeAtLayer((Node) args[0], (int) args[1]);
  }
}
