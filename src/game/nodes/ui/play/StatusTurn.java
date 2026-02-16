package game.nodes.ui.play;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import game.core.node.Node;
import game.util.Time;
import game.util.calc.MathUtil;

public class StatusTurn extends Node {

  private StatusPlayer sta1 = new StatusPlayer(true);
  private StatusPlayer sta2 = new StatusPlayer(false);

  public StatusTurn() {
    super();

    addChildren(sta1, sta2);
  }

  @Override
  public void update() {
  }

  @Override
  public void render(Graphics2D g, float alpha) {
  }

  private void setPlayerStatus(int currentPlayer) {
    sta1.expandPlayer(false);
    sta2.expandPlayer(false);
    switch (currentPlayer) {
      case 3:
      case 1:
        sta1.expandPlayer(true);
        if (currentPlayer != 3)
          break;
      case 2:
        sta2.expandPlayer(true);
        break;
      default:
    }
  }

  public void onCurP(Object... args) {
    setPlayerStatus((int) args[0]);
  }

  public void onGameOver(Object... args) {

  }
}

class StatusPlayer extends Node {

  private float width = 300f;
  private float height = 75f;
  private float scale = 1f;
  private float scaleTo = 1f;
  private final float scaleMin = 0.6f;

  private boolean flipped;
  private boolean expand = false;

  private final Color expandColor = Color.getHSBColor(0.12f, 0.8f, 0.9f);
  private final Color expandColorFlipped = Color.getHSBColor(0f, 0.8f, 0.78f);

  public StatusPlayer(boolean flipped) {
    super();

    this.flipped = flipped;
    expandPlayer(false);

    layer = 105;
  }

  @Override
  public void update() {
    scaleTo = expand ? 1f : scaleMin;
    scale = MathUtil.lerp(scale, scaleTo, 15 * Time.deltaTime);
  }

  @Override
  public void render(Graphics2D g, float alpha) {
    AffineTransform old = g.getTransform();
    g.translate(getWorldX(), getWorldY());
    g.scale((flipped ? -1 : 1), 1);

    g.setColor(expand ? (flipped ? expandColorFlipped : expandColor) : Color.BLACK);

    float scaledWidth = width * scale;
    float scaledHeight = height * scale;

    g.fillRect(0, 0, (int) scaledWidth, (int) (scaledHeight / 4));
    g.fillPolygon(new int[] { 0, (int) scaledWidth, (int) scaledWidth, 0 },
        new int[] { (int) (scaledHeight / 8 * 3),
            (int) (scaledHeight / 8 * 3),
            (int) (scaledHeight / 4 * 3 + (scaledHeight / 4 * (scale - scaleMin) * (1 / (1 - scaleMin)))),
            (int) (scaledHeight / 2) },
        4);
    g.fillOval((int) (scaledWidth - scaledHeight / 2), 0, (int) scaledHeight, (int) scaledHeight);

    { // player pfp
      g.setColor(Color.WHITE);

      float padding = 4f;
      g.fillOval((int) (scaledWidth - scaledHeight / 2 + padding),
          (int) padding,
          (int) (scaledHeight - padding * 2),
          (int) (scaledHeight - padding * 2));
    }

    g.setTransform(old);
  }

  public void expandPlayer(boolean expand) {
    this.expand = expand;
  }
}
