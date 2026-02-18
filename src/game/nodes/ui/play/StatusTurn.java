package game.nodes.ui.play;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import game.core.node.Node;
import game.core.node.ui.Text;
import game.util.Time;
import game.util.calc.MathUtil;

public class StatusTurn extends Node {

  private int currentPlayer = 0;

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
    this.currentPlayer = currentPlayer;

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

  private void setGameOver(boolean gameOver) {
    sta1.collapsePlayer(true);
    sta2.collapsePlayer(true);
    switch (currentPlayer) {
      case 1:
        sta1.expandPlayer(true);
        sta1.expandPlayerMore();
        if (currentPlayer != 3)
          break;
      case 2:
        sta2.expandPlayer(true);
        sta2.expandPlayerMore();
        break;
      case 3:
        sta1.expandPlayer(true);
        sta2.expandPlayer(true);
        break;
      default:
    }
  }

  public void onCurP(Object... args) {
    setPlayerStatus((int) args[0]);
  }

  public void onGameOver(Object... args) {
    setGameOver(true);
  }
}

class StatusPlayer extends Node {

  private float width = 280f;
  private float height = 75f;
  private float scale = 1f;
  private float scaleTo = scale;
  private final float scaleMin = 0.6f;

  private float midScalerWidth = 1f;
  private float midScalerWidthTo = midScalerWidth;
  private float midScalerWidthSpd = 0f;
  private final float midScalerWidthAcc = 50f;

  private float scaledWidth = width * midScalerWidth * scale;
  private float scaledHeight = height * scale;

  private boolean isLeft;
  private boolean expand = false;
  private boolean collapse = false;

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
    scaleTo = expand ? 1f : collapse ? 0f : scaleMin;
    scale = MathUtil.lerp(scale, scaleTo, 12 * Time.deltaTime);

    if (midScalerWidthSpd != 0
        && Math.abs(midScalerWidth) < Math.abs(midScalerWidthTo)) {
      midScalerWidth += midScalerWidthSpd / 10f * Time.deltaTime;
      if (Math.abs(midScalerWidth) >= Math.abs(midScalerWidthTo))
        midScalerWidth -= midScalerWidthSpd / 10f * Time.deltaTime;
      midScalerWidthSpd += midScalerWidthAcc * Time.deltaTime;
    } else {
      midScalerWidth = midScalerWidthTo;
      midScalerWidthSpd = 0;
    }

    scaledWidth = width * midScalerWidth * scale;
    scaledHeight = height * scale;

    pfText.setPosition(scaledWidth * (isLeft ? -1 : 1), scaledHeight * 0.4f);
    pfText.size = (int) (scaledHeight * 0.5f);
    if (pfText.lastSize != pfText.size)
      pfText.updateTextMetrics();

  }

  public void fixedUpdate() {
  }

  @Override
  public void render(Graphics2D g, float alpha) {
    AffineTransform old = g.getTransform();
    g.translate(getWorldX(), getWorldY());
    g.scale((isLeft ? -1 : 1), 1);

    g.setColor(expand && !collapse ? (isLeft ? expandColorFlipped : expandColor) : Color.BLACK);

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
    if (expand)
      this.collapse = false;
    pfText.color = expand ? (isLeft ? expandColorFlipped : expandColor) : Color.BLACK;

    midScalerWidthTo = 1f;
  }

  public void collapsePlayer(boolean collapse) {
    this.collapse = collapse;
    if (collapse)
      this.expand = false;
    pfText.color = Color.BLACK;

    midScalerWidthTo = 1f;
  }

  public void expandPlayerMore() {
    if (!expand)
      return;

    ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    scheduler.schedule(() -> {
      midScalerWidthSpd = 1.1f;
      midScalerWidthTo = 2.3f;

      scheduler.close();
    }, 300, TimeUnit.MILLISECONDS);
  }
}
