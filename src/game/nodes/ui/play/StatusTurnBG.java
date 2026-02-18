package game.nodes.ui.play;

import java.awt.Color;
import java.awt.Graphics2D;

import game.GameCanvas;
import game.core.node.Node;

public class StatusTurnBG extends Node {

  private int currentPlayer = 0;

  public StatusTurnBG() {
    super();

    layer = -500;
  }

  @Override
  public void update() {
  }

  @Override
  public void render(Graphics2D g, float alpha) {
    g.setColor(switch (currentPlayer) {
      case 1 -> Color.getHSBColor(0f, 0.2f, 1f);
      case 2 -> Color.getHSBColor(0.12f, 0.2f, 1f);
      default -> Color.WHITE;
    });

    g.fillRect(0, 0, GameCanvas.WIDTH, GameCanvas.HEIGHT);
  }

  private void setCurrentPlayer(int cur) {
    this.currentPlayer = cur;
  }

  public void onCurP(Object... args) {
    setCurrentPlayer((int) args[0]);
  }
}
