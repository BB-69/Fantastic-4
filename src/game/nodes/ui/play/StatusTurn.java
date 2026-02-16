package game.nodes.ui.play;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import game.core.node.Node;
import game.core.node.ui.Text;
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

  private float width = 280f;
  private float height = 75f;
  private float scale = 1f;
  private float scaleTo = 1f;
  private final float scaleMin = 0.6f;

  private float scaledWidth = width * scale;
  private float scaledHeight = height * scale;

  private boolean isLeft;
  private boolean expand = false;

  private final Color expandColor = Color.getHSBColor(0.12f, 0.8f, 0.9f);
  private final Color expandColorFlipped = Color.getHSBColor(0f, 0.8f, 0.78f);

  private final Text pfText = new Text();

  public StatusPlayer(boolean isLeft) {
    super();

    this.isLeft = isLeft;
    expandPlayer(false);

    addChild(pfText);

    pfText.layer = 106;
    pfText.content = isLeft ? "1" : "2";

    layer = 105;
  }

  @Override
  public void update() {
    scaleTo = expand ? 1f : scaleMin;
    scale = MathUtil.lerp(scale, scaleTo, 15 * Time.deltaTime);
  }

  public void fixedUpdate() {
    scaledWidth = width * scale;
    scaledHeight = height * scale;

    pfText.setPosition(scaledWidth * (isLeft ? -1 : 1), scaledHeight * 0.4f);
    pfText.size = (int) (scaledHeight * 0.5f);
    pfText.updateTextMetrics();
  }

  @Override
  public void render(Graphics2D g, float alpha) {
    AffineTransform old = g.getTransform();
    g.translate(getWorldX(), getWorldY());
    g.scale((isLeft ? -1 : 1), 1);

    g.setColor(expand ? (isLeft ? expandColorFlipped : expandColor) : Color.BLACK);

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
    pfText.color = expand ? (isLeft ? expandColorFlipped : expandColor) : Color.BLACK;
  }
}
