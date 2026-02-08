package game.nodes.board;

import java.awt.Color;
import java.awt.Graphics2D;

import game.core.node.Entity;

public class BoardPiece extends Entity {

  private float pieceWidth = 0f;
  private float pieceHeight = 0f;

  private int val = 0;

  public BoardPiece(int val, float pieceSize) {
    this.pieceWidth = pieceSize;
    this.pieceHeight = pieceSize;
    this.val = val;
  }

  public BoardPiece(int val, float pieceWidth, float pieceHeight) {
    this.pieceWidth = pieceWidth;
    this.pieceHeight = pieceHeight;
    this.val = val;
  }

  @Override
  public void update() {
  }

  @Override
  public void render(Graphics2D g, float alpha) {
    g.setColor(Color.BLACK);
    g.fillRect(
        (int) (x - pieceWidth / 2),
        (int) (y - pieceHeight / 2),
        (int) pieceWidth,
        (int) pieceHeight);

    g.setColor(Color.BLUE);
    float innerRectWidth = pieceWidth * 0.9f;
    float innerRectHeight = pieceHeight * 0.9f;
    g.fillRect(
        (int) (x - innerRectWidth / 2),
        (int) (y - innerRectHeight / 2),
        (int) innerRectWidth,
        (int) innerRectHeight);

    g.setColor(switch (val) {
      case 0 ->
        Color.WHITE;
      case 1 ->
        Color.RED;
      case 2 ->
        Color.YELLOW;
      default ->
        Color.WHITE;
    });
    float coinRadius = Math.min(pieceWidth, pieceHeight) * 0.8f;
    g.fillOval(
        (int) (x - coinRadius / 2),
        (int) (y - coinRadius / 2),
        (int) coinRadius,
        (int) coinRadius);
  }
}
