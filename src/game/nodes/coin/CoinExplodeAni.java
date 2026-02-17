package game.nodes.coin;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;

import game.core.node.Node;
import game.util.Time;

public class CoinExplodeAni extends Node {

  private final float fullSize = Coin.COIN_SIZE * 2f;

  private float progress = 0f;
  private float speed = 4f;

  public CoinExplodeAni(float worldX, float worldY) {
    setWorldPosition(worldX, worldY);
  }

  @Override
  public void update() {
    progress += speed * Time.deltaTime;

    if (progress >= 1f) {
      destroy();
    }
  }

  @Override
  public void render(Graphics2D g, float alpha) {
    AffineTransform old = g.getTransform();
    g.translate(getWorldX(), getWorldY());

    g.setColor(Color.WHITE);

    Area white = new Area(
        new Ellipse2D.Float(-(fullSize * progress) / 2f, -(fullSize * progress) / 2f,
            fullSize * progress, fullSize * progress));

    if (progress >= 0.5f) {
      float innerProgress = (progress - 0.5f) * 2f;
      white.subtract(
          new Area(new Ellipse2D.Float(-(fullSize * innerProgress) / 2f, -(fullSize * innerProgress) / 2f,
              fullSize * innerProgress, fullSize * innerProgress)));
    }

    g.fill(white);

    g.setTransform(old);
  }
}
