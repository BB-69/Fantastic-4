package game.nodes.board;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.LinearGradientPaint;
import java.awt.Paint;
import java.awt.geom.AffineTransform;

import game.GameCanvas;
import game.core.node.Area;
import game.core.node.Node;
import game.core.signal.Signal;
import game.input.MouseInput;
import game.util.graphics.ColorUtil;

public class ColumnArea extends Area {

  private final Color hoverColor = Color.getHSBColor(0f, 0f, 0.75f);

  private int index = 0;
  public boolean selected = false;

  private boolean gameOver = false;

  private Signal signalColClick;

  public ColumnArea(Node parent, int index, float x, float w) {
    super(parent);

    this.index = index;

    setPosition(x, 0);
    setSize(w, GameCanvas.HEIGHT);

    layer = -10;
  }

  @Override
  public void update() {
  }

  @Override
  public void fixedUpdate() {
    super.fixedUpdate();

    if (!gameOver && selected) {
      if (MouseInput.isAnyPressed()) {
        if (signalColClick != null)
          signalColClick.emit(index);
      }
    }
  }

  @Override
  public void render(Graphics2D g, float alpha) {
    if (selected) {
      AffineTransform old = g.getTransform();
      g.translate(getWorldX(), getWorldY());
      g.rotate(rotation);

      g.setColor(hoverColor);
      g.fillRect(
          (int) (-w / 2),
          (int) (-h / 2),
          (int) w,
          (int) h);

      {
        Paint oldPaint = g.getPaint();
        g.translate(-w / 2, -(w + h / 2));

        LinearGradientPaint topGradient = new LinearGradientPaint(
            0f, 0f, 0f, w,
            new float[] { 0f, 1f },
            new Color[] { ColorUtil.TRANSPARENT, hoverColor });
        g.setPaint(topGradient);
        g.fillRect(0, 0, (int) w, (int) w);

        g.setPaint(oldPaint);
      }

      g.setTransform(old);
    }
  }

  private void setGameOver(boolean gameOver) {
    this.gameOver = gameOver;
  }

  public void setColClickSignal(Signal signalColClick) {
    this.signalColClick = signalColClick;
  }

  public void onGameOver(Object... args) {
    setGameOver(true);
  }
}
