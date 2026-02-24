package game.nodes.board;

import java.awt.Graphics2D;

import game.GameCanvas;
import game.core.node.Node;
import game.core.signal.Signal;

public class Board extends Node {

  public static final float PIECE_WIDTH = 60f;
  public static final float PIECE_HEIGHT = 60f;

  private final int[][] gridState = new int[BoardLogic.ROWS][BoardLogic.COLS];
  private final BoardPiece[][] pieces = new BoardPiece[BoardLogic.ROWS][BoardLogic.COLS];
  private int currentPlayer = 1;
  private boolean gameOver = false;

  private Signal signalBoardPos;

  public Board() {
    super();

    x = GameCanvas.WIDTH / 2;
    y = GameCanvas.HEIGHT / 2 + 60;

    for (int row = 0; row < BoardLogic.ROWS; row++) {
      for (int col = 0; col < BoardLogic.COLS; col++) {
        BoardPiece p = new BoardPiece(
            gridState[row][col],
            Board.PIECE_WIDTH,
            Board.PIECE_HEIGHT);

        p.setPosition(
            Board.PIECE_WIDTH * (col - ((BoardLogic.COLS - 1) / 2f)),
            Board.PIECE_HEIGHT * (row - ((BoardLogic.ROWS - 1) / 2f)));

        addChild(p);
        pieces[row][col] = p;
      }
    }
  }

  @Override
  public void update() {
  }

  @Override
  public void fixedUpdate() {
    super.fixedUpdate();

    if (signalBoardPos != null)
      signalBoardPos.emit(x, y);
  }

  @Override
  public void render(Graphics2D g, float alpha) {
  }

  public void onRCVal(Object... args) {
    int row = (int) args[0];
    int col = (int) args[1];
    int val = (int) args[2];

    gridState[row][col] = val;
    pieces[row][col].setValue(val);
  }

  public void onCurP(Object... args) {
    this.currentPlayer = (int) args[0];
  }

  public void onGameOver(Object... args) {
    this.gameOver = true;
  }

  public void attachPosSignal(Signal signalBoardPos) {
    this.signalBoardPos = signalBoardPos;
  }

  public void refreshFullBoard(int[][] newGrid) { /// 1 รีหน้าบอร์ด
    for (int r = 0; r < BoardLogic.ROWS; r++) {
        for (int c = 0; c < BoardLogic.COLS; c++) {
            gridState[r][c] = newGrid[r][c];
            pieces[r][c].setValue(newGrid[r][c]);
        }
    }
  }
}
