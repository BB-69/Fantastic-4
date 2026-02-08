package game.nodes.board;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import game.GameCanvas;
import game.core.node.Node;

public class Board extends Node {

  private static final float pieceWidth = 50f;
  private static final float pieceHeight = 50f;

  private final int[][] gridState = new int[BoardLogic.ROWS][BoardLogic.COLS];
  private int currentPlayer = 1;
  private boolean gameOver = false;

  @Override
  public void update() {
  }

  @Override
  public void render(Graphics2D g, float alpha) {
    drawBoard(g, alpha);
  }

  public void onRCVal(Object... args) {
    this.gridState[(int) args[0]][(int) args[1]] = (int) args[2];
  }

  public void onCurP(Object... args) {
    this.currentPlayer = (int) args[0];
  }

  public void onGameOver(Object... args) {
    this.gameOver = true;
  }

  private void drawBoard(Graphics2D g, float alpha) {
    AffineTransform old = g.getTransform();

    g.translate(GameCanvas.WIDTH / 2 - Board.pieceWidth * ((BoardLogic.COLS - 1) / 2f),
        GameCanvas.HEIGHT / 2 - Board.pieceHeight * ((BoardLogic.ROWS - 1) / 2f));

    for (int row = 0; row < BoardLogic.ROWS; row++) {
      for (int col = 0; col < BoardLogic.COLS; col++) {
        BoardPiece p = new BoardPiece(gridState[row][col], Board.pieceWidth, Board.pieceHeight);
        p.setPosition(Board.pieceWidth * col, Board.pieceHeight * row);
        p.render(g, alpha);
      }
    }

    g.setTransform(old);
  }
}
