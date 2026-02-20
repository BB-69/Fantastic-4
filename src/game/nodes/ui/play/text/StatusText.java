package game.nodes.ui.play.text;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.RadialGradientPaint;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;

import game.core.node.ui.Text;
import game.nodes.ui.play.TopMenu;
import game.util.Time;
import game.util.calc.MathUtil;
import game.util.graphics.ColorUtil;

public class StatusText extends Text {

  private int currentPlayer = 0;
  private boolean gameOver = false;

  private int textSize = 18;
  private int textPadding = 10;
  private int borderPadding = 4;

  private float alphaWidth;
  private float alphaHeight;
  private float targetY;
  private Color targetColor;

  private float scale = 1f;
  private float targetScale = scale;

  private final Color p1Color = Color.getHSBColor(0f, 1f, 0.95f);
  private final Color p2Color = Color.getHSBColor(0.12f, 0.9f, 0.9f);

  public StatusText() {
    super();

    size = textSize;
    color = TopMenu.c1.darker().darker();
    targetColor = color;
    content = "No Player Active!";
    updateTextMetrics();

    alphaWidth = getTextWidth();
    alphaHeight = getTextHeight();
    targetY = getWorldY();

    layer = 104;
  }

  @Override
  public void update() {
    super.update();

    alphaWidth = MathUtil.lerp(alphaWidth, getTextWidth(), 12 * Time.deltaTime);
    alphaHeight = MathUtil.lerp(alphaHeight, getTextHeight(), 12 * Time.deltaTime);
    color = ColorUtil.lerp(color, targetColor, 2 * Time.deltaTime);
    scale = MathUtil.lerp(scale, targetScale, 12 * Time.deltaTime);
    size = (int) (textSize * Math.pow(scale, 2.2f));
    if (lastSize != size)
      updateTextMetrics();
  }

  @Override
  public void fixedUpdate() {
    super.fixedUpdate();

    setWorldY(MathUtil.lerp(getWorldY(), targetY, 6 * Time.FIXED_DELTA));
  }

  @Override
  public void render(Graphics2D g, float alpha) {
    drawBanner(g, alpha);

    super.render(g, alpha);
  }

  private void drawBanner(Graphics2D g, float alpha) {
    AffineTransform old = g.getTransform();
    g.translate(getWorldX(), getWorldY() + alphaHeight / 4);
    g.scale(scale, scale);

    { // shadow
      float scaledWidth = alphaWidth * 1.4f;
      float scaledHeight = alphaHeight * 2.8f;

      AffineTransform old2 = g.getTransform();
      Paint oldPaint = g.getPaint();

      g.translate(0, scaledHeight * 0.05f);
      g.scale(scaledWidth / scaledHeight, 1);

      RadialGradientPaint paint = new RadialGradientPaint(new Point2D.Float(),
          scaledHeight / 2f,
          new float[] { 0, 1 },
          new Color[] { Color.BLACK, ColorUtil.TRANSPARENT });

      g.setPaint(paint);
      g.fill(new Ellipse2D.Float(-scaledHeight, -scaledHeight, scaledHeight * 2, scaledHeight * 2));

      g.setPaint(oldPaint);
      g.setTransform(old2);
    }

    g.setColor(TopMenu.c1);
    drawBannerWithPad(
        g,
        (int) alphaWidth,
        (int) alphaHeight,
        textPadding + borderPadding + 5,
        textPadding + borderPadding);
    g.setColor(TopMenu.c2);
    drawBannerWithPad(
        g,
        (int) alphaWidth,
        (int) alphaHeight,
        textPadding + 5,
        textPadding);

    g.setTransform(old);
  }

  private void drawBannerWithPad(Graphics2D g, int width, int height, int padW, int padH) {
    g.fillPolygon(
        new int[] {
            -(width + height) / 2 - padW,
            (width + height) / 2 + padW,
            width / 2 + padW,
            (width + height) / 2 + padW,
            -(width + height) / 2 - padW,
            -width / 2 - padW
        },
        new int[] {
            height / 2 + padH,
            height / 2 + padH,
            0,
            -height / 2 - padH,
            -height / 2 - padH,
            0
        },
        6);
  }

  public void setTargetY(float targetY) {
    this.targetY = targetY;
  }

  public void slideIn() {
    targetY = 85;
  }

  private void setPlayerText(int currentPlayer) {
    if (gameOver)
      return;

    this.currentPlayer = currentPlayer;

    content = switch (currentPlayer) {
      case 1, 2 -> "Current Player: " + currentPlayer;
      default -> "No Player Active!";
    };
    updateTextMetrics();

    color = switch (currentPlayer) {
      case 1 -> p1Color;
      case 2 -> p2Color;
      default -> Color.WHITE;
    };
  }

  private void setGameOver(boolean gameOver) {
    this.gameOver = gameOver;

    content = switch (currentPlayer) {
      case 1, 2 -> "Player " + currentPlayer + " Wins!";
      case 3 -> "Game Tie!";
      default -> "Game Over!";
    };
    color = switch (currentPlayer) {
      case 1 -> p1Color;
      case 2 -> p2Color;
      default -> Color.WHITE;
    };
    targetColor = color;
    updateTextMetrics();

    targetScale = 1.3f;
    targetY = 110;
    // layer = 110;
  }

  public void onCurP(Object... args) {
    setPlayerText((int) args[0]);
  }

  public void onGameOver(Object... args) {
    setGameOver(true);
  }
}
