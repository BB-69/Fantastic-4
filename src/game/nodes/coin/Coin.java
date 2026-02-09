package game.nodes.coin;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import game.core.node.Entity;

public class Coin extends Entity {

  public static final float COIN_SIZE = 50f;

  private int player;

  public Coin(int player) {
    super();

    this.player = player;

    layer = -2;
  }

  @Override
  public void update() {
  }

  @Override
  public void render(Graphics2D g, float alpha) {
    AffineTransform old = g.getTransform();
    g.translate(getWorldX(), getWorldY());

    g.setColor(player == 0 ? Color.RED : Color.YELLOW);
    g.fillOval((int) (-COIN_SIZE / 2f),
        (int) (-COIN_SIZE / 2f),
        (int) COIN_SIZE,
        (int) COIN_SIZE);

    g.setTransform(old);
  }

  public void setPlayer(int player) {
    this.player = player;
  }
}
