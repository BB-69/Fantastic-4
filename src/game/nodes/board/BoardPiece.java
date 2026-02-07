package game.nodes.board;

import java.awt.Color;
import java.awt.Graphics2D;

import game.core.node.Entity;

public class BoardPiece extends Entity {

  private float pieceSize = 0f;

  public BoardPiece(int boardWidth, int boardHeight, float pieceSize) {
    this.pieceSize = pieceSize;
  }

  @Override
  public void update() {
  }

  @Override
  public void render(Graphics2D g, float alpha) {
    g.setColor(Color.BLACK);
    g.fillRect(
        (int) (x - pieceSize / 2),
        (int) (y - pieceSize / 2),
        (int) pieceSize,
        (int) pieceSize);

    g.setColor(Color.BLUE);
    float innerRectSize = pieceSize * 0.9f;
    g.fillRect(
        (int) (x - innerRectSize / 2),
        (int) (y - innerRectSize / 2),
        (int) innerRectSize,
        (int) innerRectSize);

    g.setColor(Color.WHITE);
    float coinRadius = pieceSize * 0.8f;
    g.fillOval(
        (int) (x - coinRadius / 2),
        (int) (y - coinRadius / 2),
        (int) coinRadius,
        (int) coinRadius);
  }
}
