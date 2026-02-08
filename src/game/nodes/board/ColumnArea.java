package game.nodes.board;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import game.GameCanvas;
import game.core.node.Area;
import game.core.node.Node;

public class ColumnArea extends Area {

  public boolean selected = false;

  public ColumnArea(Node parent, float x, float w) {
    super(parent);

    setPosition(x, 0);
    setSize(w, GameCanvas.HEIGHT);

    layer = -5;
  }

  @Override
  public void update() {
  }

  @Override
  public void render(Graphics2D g, float alpha) {
    if (selected) {
      AffineTransform old = g.getTransform();

      g.translate(getWorldX(), getWorldY());
      g.rotate(rotation);
      g.setColor(Color.cyan);
      g.fillRect(
          (int) (-w / 2),
          (int) (-h / 2),
          (int) w,
          (int) h);

      g.setTransform(old);
    }
  }
}
