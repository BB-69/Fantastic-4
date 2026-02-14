package game.nodes.coin;

import java.awt.Graphics2D;

import game.core.graphics.Sprite;
import game.core.node.Entity;
import game.util.Time;

public class Coin extends Entity {

  private Sprite sprite;

  public static final float COIN_SIZE = 52f;

  private int player;

  private boolean initPosition = false;

  public Coin(int player) {
    super();

    this.player = player;

    layer = -2;
    initSprite();
  }

  private void initSprite() {
    sprite = new Sprite("coin.png");
    sprite.setSize(COIN_SIZE, COIN_SIZE);
  }

  @Override
  public void update() {
    sprite.update(Time.deltaTime);
  }

  @Override
  public void render(Graphics2D g, float alpha) {
    drawSprite(g, alpha);
  }

  private void drawSprite(Graphics2D g, float alpha) {
    int renderX = (int) lerp(getPrevWorldX(), getWorldX(), initPosition ? alpha : 1);
    int renderY = (int) lerp(getPrevWorldY(), getWorldY(), initPosition ? alpha : 1);

    if (!initPosition)
      initPosition = true;

    sprite.setPosition(renderX + 2, renderY);

    sprite.draw(g);
  }

  public void setPlayer(int player) {
    this.player = player;
  }
}
