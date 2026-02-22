package game.nodes.ui.play;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.RadialGradientPaint;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;

import game.GameCanvas;
import game.core.node.Node;
import game.nodes.ui.play.button.QuitButton;
import game.nodes.ui.play.button.RestartButton;
import game.util.Time;
import game.util.calc.MathUtil;
import game.util.graphics.ColorUtil;

public class TopMenu extends Node {

  public static final Color c1 = Color.getHSBColor(0.1f, 1f, 0.5f);
  public static final Color c2 = Color.getHSBColor(0.12f, 0.45f, 1f);

  private float width = 90f;
  private float height = 40f;
  private float extensionX = 16f;
  private float extensionY = 8f;
  private float trapezoidOffset = 48f;

  private RestartButton restartButton = new RestartButton();
  private QuitButton quitButton = new QuitButton();

  private int currentPlayer = 0;

  private float targetLocalX = 0f;

  public TopMenu() {
    super();

    addChildren(restartButton, quitButton);

    int padding = 6;
    restartButton.setPosition((restartButton.w + padding) / 2, (height + extensionY) / 2);
    quitButton.setPosition(-(quitButton.w + padding) / 2, (height + extensionY) / 2);

    layer = 110;
  }

  @Override
  public void update() {
    x = MathUtil.lerp(x, targetLocalX, 10 * Time.deltaTime);
  }

  @Override
  public void fixedUpdate() {
    super.fixedUpdate();
  }

  @Override
  public void render(Graphics2D g, float alpha) {
    AffineTransform old = g.getTransform();
    g.translate(getWorldX(), getWorldY());

    { // shadow
      Paint oldPaint = g.getPaint();

      int radius = (int) ((width + height) * 0.6f);
      RadialGradientPaint paint = new RadialGradientPaint(new Point2D.Float(),
          radius,
          new float[] { 0, 1 },
          new Color[] { Color.BLACK, ColorUtil.TRANSPARENT });

      g.setPaint(paint);
      g.fill(new Ellipse2D.Float(-radius, -radius, radius * 2, radius * 2));

      g.setPaint(oldPaint);
    }

    g.setColor(c1);
    g.fillPolygon(
        new int[] { (int) (-(width + trapezoidOffset) / 2 - extensionX),
            (int) ((width + trapezoidOffset) / 2 + extensionX),
            (int) ((width) / 2 + extensionX),
            (int) (-(width) / 2 - extensionX) },
        new int[] { 0, 0, (int) height, (int) height },
        4);
    g.setColor(c2);
    g.fillPolygon(
        new int[] { (int) (-(width + trapezoidOffset) / 2),
            (int) ((width + trapezoidOffset) / 2),
            (int) ((width) / 2),
            (int) (-(width) / 2) },
        new int[] { 0, 0, (int) (height + extensionY), (int) (height + extensionY) },
        4);

    g.setTransform(old);
  }

  private void setCurrentPlayer(int cur) {
    this.currentPlayer = cur;
  }

  private void setGameOver(boolean gameOver) {
    // this.gameOver = gameOver;

    switch (currentPlayer) {
      case 1:
        targetLocalX = GameCanvas.WIDTH / 2f - width;
        break;
      case 2:
        targetLocalX = -(GameCanvas.WIDTH / 2f - width);
        break;
      default:
        targetLocalX = 0;
        break;
    }
  }

  public void onCurP(Object... args) {
    setCurrentPlayer((int) args[0]);
  }

  public void onGameOver(Object... args) {
    setGameOver(true);
  }

  public void reset() {
    currentPlayer = 0;
    targetLocalX = 0f;
    x = 0f;
  }
}
