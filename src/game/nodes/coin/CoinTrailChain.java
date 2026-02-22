package game.nodes.coin;

import java.awt.Color;
import java.awt.Graphics2D;

import game.core.node.Node;

public class CoinTrailChain extends Node {

  private static final float DISTANCE_INTERVAL = 15f;

  public CoinTrailChain(int startX, int startY, int endX, int endY, Color color) {
    this.setWorldPosition(startX, startY);

    endX -= startX;
    endY -= startY;
    float currentX = 0f, currentY = 0f;
    final float hypotenuse = (float) Math.sqrt(endX * endX + endY * endY);
    final float widthInterval = DISTANCE_INTERVAL * endX / hypotenuse;
    final float heightInterval = DISTANCE_INTERVAL * endY / hypotenuse;

    float lifeSpan = 0.25f;
    while (Math.abs(currentX) <= Math.abs(endX)
        && Math.abs(currentY) <= Math.abs(endY)) {
      addChild(new CoinTrail((int) currentX, (int) currentY, lifeSpan, color));

      currentX += widthInterval;
      currentY += heightInterval;
      lifeSpan += 0.01f;
    }
  }

  @Override
  public void update() {
    if (children.isEmpty())
      destroy();
  }

  @Override
  public void render(Graphics2D g, float alpha) {
  }
}
