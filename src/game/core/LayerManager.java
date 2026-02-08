package game.core;

import java.awt.Graphics2D;
import java.util.Map;
import java.util.TreeMap;

import game.core.graphics.Layer;
import game.core.signal.Signal;

public class LayerManager {
  private final Map<Integer, Layer> layers = new TreeMap<>();

  private Signal signalSendNodeTo = new Signal();

  public Layer getOrCreateLayer(int index) {
    return layers.computeIfAbsent(index, i -> {
      Layer newLayer = new Layer(i, signalSendNodeTo);
      signalSendNodeTo.connect(newLayer::onSendNodeTo);
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
}
