package game.nodes.board;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import game.GameCanvas;
import game.core.node.Area;
import game.core.node.Node;
import game.core.signal.Signal;
import game.input.MouseInput;
import game.nodes.coin.Coin;

public class ColumnArea extends Area {

  private int index = 0;
  public boolean selected = false;

  private int currentPlayer = 0;
  private boolean gameOver = false;
  private Coin coin;

  private Signal signalColClick;

  public ColumnArea(Node parent, int index, float x, float w) {
    super(parent);

    this.index = index;
    setPosition(x, 0);
    setSize(w, GameCanvas.HEIGHT);

    layer = -5;
  }

  @Override
  public void update() {
  }

  @Override
  public void fixedUpdate() {
    super.fixedUpdate();

    if (!gameOver && selected) {
      if (currentPlayer < 3 && currentPlayer > 0) {
        if (coin == null) {
          coin = new Coin(currentPlayer - 1);
          coin.setParent(this);
          coin.x = 0;
          coin.setWorldY(150);
        } else
          coin.setPlayer(currentPlayer - 1);

        if (MouseInput.isAnyPressed()) {
          if (signalColClick != null)
            signalColClick.emit(index);
        }
      }
    } else {
      if (coin != null) {
        coin.destroy();
        coin = null;
      }
    }
  }

  @Override
  public void render(Graphics2D g, float alpha) {
    if (selected) {
      AffineTransform old = g.getTransform();

      g.translate(getWorldX(), getWorldY());
      g.rotate(rotation);
      g.setColor(Color.cyan);
      g.fillRect(
          (int) (-w / 2),
          (int) (-h / 2),
          (int) w,
          (int) h);

      g.setTransform(old);
    }
  }

  private void setCurrentPlayer(int cur) {
    this.currentPlayer = cur;
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

  public void onCurP(Object... args) {
    setCurrentPlayer((int) args[0]);
  }
}
