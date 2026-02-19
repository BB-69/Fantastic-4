package game.nodes.ui.play.text;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import game.GameCanvas;
import game.core.node.ui.Text;
import game.nodes.ui.play.TopMenu;
import game.util.Time;
import game.util.calc.MathUtil;
import game.util.graphics.ColorUtil;

public class TotalCoinText extends Text {

  private int totalCoin = 0;

  private float targetX;
  private Color targetColor;

  private final Color cNormal = TopMenu.c1;
  private final Color cDanger = Color.getHSBColor(0f, 1f, 0.95f);

  public TotalCoinText() {
    super();

    size = 18;
    color = TopMenu.c1.darker().darker();
    targetColor = color;
    content = "No Player Active!";
    updateTextMetrics();

    targetX = getWorldX();

    layer = 104;
  }

  @Override
  public void update() {
    super.update();

    color = ColorUtil.lerp(color, targetColor, 2 * Time.deltaTime);
  }

  @Override
  public void fixedUpdate() {
    super.fixedUpdate();

    setWorldX(MathUtil.lerp(getWorldX(), targetX, 6 * Time.FIXED_DELTA));
  }

  @Override
  public void render(Graphics2D g, float alpha) {
    drawBanner(g, alpha);

    super.render(g, alpha);
  }

  private void drawBanner(Graphics2D g, float alpha) {
    AffineTransform old = g.getTransform();
    g.translate(getWorldX(), getWorldY() + getTextHeight() / 4);

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

  public void slideIn() {
    targetX = GameCanvas.WIDTH - 85;
  }
}
