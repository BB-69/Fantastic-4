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

public class TotalCoinText extends Text {

  private int totalCoin = 0;

  private int textSize = 14;
  private int textPadding = 10;
  private int borderPadding = 4;

  private float targetX;

  private float scale = 1f;
  private float targetScale = scale;

  private final Color cNormal = TopMenu.c1.darker().darker();
  private final Color cWarn = Color.getHSBColor(0f, 1f, 0.95f);
  private final Color[] cDanger = { Color.BLACK, Color.RED, Color.WHITE };

  public TotalCoinText() {
    super();

    size = textSize;
    color = cNormal;
    content = "Total 00/24";
    updateTextMetrics();

    targetX = getWorldX();

    layer = 104;
  }

  @Override
  public void fixedUpdate() {
    super.fixedUpdate();

    setWorldX(MathUtil.lerp(getWorldX(), targetX, 6 * Time.FIXED_DELTA));
  }

  @Override
  public void update() {
    super.update();

    targetScale = totalCoin >= 24 ? 1.1f : 1;
    scale = MathUtil.lerp(scale, targetScale, 12 * Time.deltaTime);
    size = (int) (textSize * Math.pow(scale, 2.2f));
    if (lastSize != size)
      updateTextMetrics();
  }

  @Override
  public void render(Graphics2D g, float alpha) {
    drawBanner(g, alpha);

    super.render(g, alpha);
  }

  private void drawBanner(Graphics2D g, float alpha) {
    AffineTransform old = g.getTransform();
    g.translate(getWorldX(), getWorldY() + getTextHeight() / 4);
    g.scale(scale, scale);

    int textWidth = getTextWidth();
    int textHeight = getTextHeight();

    { // shadow
      float scaledWidth = textWidth * 1.4f;
      float scaledHeight = textHeight * 2.8f;

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

    g.setColor(totalCoin >= 24 ? cDanger[0] : TopMenu.c1);
    drawBannerWithPad(
        g,
        textWidth,
        textHeight,
        textPadding + borderPadding + 5,
        textPadding + borderPadding);
    g.setColor(totalCoin >= 24 ? cDanger[1] : TopMenu.c2);
    drawBannerWithPad(
        g,
        textWidth,
        textHeight,
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

  public void setTargetX(float targetX) {
    this.targetX = targetX;
  }

  private void slideIn() {
    targetX = 100;
  }

  private void setTotalCoin(int totalCoin) {
    this.totalCoin = totalCoin;

    if (totalCoin > 0)
      slideIn();
    content = String.format("Total %02d/24", totalCoin);
    color = totalCoin >= 24 ? cDanger[2] : totalCoin >= 20 ? cWarn : cNormal;
  }

  public void onTotalCoin(Object... args) {
    setTotalCoin((int) args[0]);
  }
}
