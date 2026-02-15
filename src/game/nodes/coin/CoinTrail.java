package game.nodes.coin;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import game.core.node.Node;
import game.util.Time;

public class CoinTrail extends Node {

  private float lifeSpanLeft = 0f;
  private static final float fadeTime = 0.18f;

  private Color color;

  public CoinTrail(int x, int y, float lifeSpan, Color color) {
    this.setWorldPosition(x, y);
    this.lifeSpanLeft = lifeSpan;
    this.color = color;

    layer = -6;
  }

  @Override
  public void update() {
    lifeSpanLeft -= Time.deltaTime;

    if (lifeSpanLeft <= 0) {
      destroy();
    }
  }

  @Override
  public void render(Graphics2D g, float alpha) {
    AffineTransform old = g.getTransform();
    Composite oldComp = g.getComposite();
    g.translate(getWorldX(), getWorldY());

    if (lifeSpanLeft <= fadeTime)
      g.setComposite(AlphaComposite.getInstance(
          AlphaComposite.SRC_OVER, lifeSpanLeft / fadeTime));

    float coinSize = Coin.COIN_SIZE * ((lifeSpanLeft <= fadeTime)
        ? lifeSpanLeft / fadeTime
        : 1);

    { // outer fill
      g.setColor(color.darker());
      g.fillOval((int) (-coinSize / 2f),
          (int) (-coinSize / 2f),
          (int) coinSize,
          (int) coinSize);
    }
    { // inner fill
      g.setColor(color);

      float innerCoin = coinSize * 0.87f;
      g.fillOval((int) (-innerCoin / 2f),
          (int) (-innerCoin / 2f),
          (int) innerCoin,
          (int) innerCoin);
    }

    g.setComposite(oldComp);
    g.setTransform(old);
  }
}
