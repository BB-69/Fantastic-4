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

  private final Color hoverColor = new Color(0, 0, 0, 0.3f);

  private int index = 0;
  public boolean selected = false;

  private boolean gameOver = false;

  private boolean coinDropFinished = false;

  private Signal signalColClick;

  public ColumnArea(Node parent, int index, float x, float w) {
    super(parent);

    this.index = index;

    setPosition(x, 0);
    setSize(w, GameCanvas.HEIGHT);

    layer = -10;
  }

  public void reset() {
    selected = false;
    gameOver = false;
    coinDropFinished = false;
  }

  @Override
  public void update() {
  }

  @Override
  public void fixedUpdate() {
    super.fixedUpdate();

    if (!gameOver && coinDropFinished && selected) {
      if (MouseInput.isAnyPressed()) {
        if (signalColClick != null) {
          signalColClick.emit(index);
          coinDropFinished = false;
        }
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

  public void informCoinDropStart() {
    coinDropFinished = false;
  }

  private void setGameOver(boolean gameOver) {
    this.gameOver = gameOver;
  }

  private void informCoinDropFinished() {
    coinDropFinished = true;
  }

  public void setColClickSignal(Signal signalColClick) {
    this.signalColClick = signalColClick;
  }

  public void onGameOver(Object... args) {
    setGameOver(true);
  }

  public void onCoinDropFinish(Object... args) {
    informCoinDropFinished();
  }
}
