package game.nodes.board;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import game.GameCanvas;
import game.core.node.Node;
import game.core.signal.Signal;

public class Board extends Node {

  public static final float PIECE_WIDTH = 50f;
  public static final float PIECE_HEIGHT = 50f;

  private final int[][] gridState = new int[BoardLogic.ROWS][BoardLogic.COLS];
  private int currentPlayer = 1;
  private boolean gameOver = false;

  private Signal signalBoardPos;

  public Board() {
    x = GameCanvas.WIDTH / 2;
    y = GameCanvas.HEIGHT / 2;
  }

  @Override
  public void update() {
  }

  @Override
  public void fixedUpdate() {
    if (signalBoardPos != null)
      signalBoardPos.emit(x, y);
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

    g.translate(x - Board.PIECE_WIDTH * ((BoardLogic.COLS - 1) / 2f),
        y - Board.PIECE_HEIGHT * ((BoardLogic.ROWS - 1) / 2f));

    for (int row = 0; row < BoardLogic.ROWS; row++) {
      for (int col = 0; col < BoardLogic.COLS; col++) {
        BoardPiece p = new BoardPiece(gridState[row][col], Board.PIECE_WIDTH, Board.PIECE_HEIGHT);
        p.setPosition(Board.PIECE_WIDTH * col, Board.PIECE_HEIGHT * row);
        p.render(g, alpha);
      }
    }

    g.setTransform(old);
  }

  public void attachPosSignal(Signal signalBoardPos) {
    this.signalBoardPos = signalBoardPos;
  }
}
