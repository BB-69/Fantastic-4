package game.nodes;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import game.GameCanvas;
import game.core.node.Area;

public class _TestArea extends Area {

  public _TestArea() {
    setPosition(GameCanvas.WIDTH / 2, GameCanvas.HEIGHT / 2);
    setSize(GameCanvas.WIDTH / 2, GameCanvas.HEIGHT / 2);
  }

  @Override
  public void update() {
  }

  @Override
  public void render(Graphics2D g, float alpha) {
    AffineTransform old = g.getTransform();

    g.translate(x, y);
    g.rotate(rotation);
    g.setColor(isHovered() ? Color.GREEN : Color.RED);
    g.fillRect(
        (int) (-w / 2),
        (int) (-h / 2),
        (int) w,
        (int) h);

    g.setTransform(old);
  }
}
