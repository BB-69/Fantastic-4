package game.nodes.board;

import java.awt.Graphics2D;

import game.GameCanvas;
import game.core.node.Node;
import game.core.signal.Signal;
import game.nodes.coin.Coin;

public class Board extends Node {

  public static final float PIECE_WIDTH = 60f;
  public static final float PIECE_HEIGHT = 60f;

  private final int[][] gridState = new int[BoardLogic.ROWS][BoardLogic.COLS];
  private final BoardPiece[][] pieces = new BoardPiece[BoardLogic.ROWS][BoardLogic.COLS];
  private int currentPlayer = 1;
  private boolean gameOver = false;

  private final int[] lastDroppedPos = new int[2];

  private Signal signalBoardPos;

  private Coin activeDropCoin;
  private float targetY;
  private final int[] droppingTo = new int[2];
  private boolean isDropping = false;

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
    if (isDropping && activeDropCoin.getWorldY() >= targetY) {
      isDropping = false;

      BoardPiece p = pieces[droppingTo[0]][droppingTo[1]];
      activeDropCoin.gravityOn = false;
      activeDropCoin.vy = 0;
      p.receiveCoin(activeDropCoin);

      activeDropCoin = null;
    }
  }

  @Override
  public void render(Graphics2D g, float alpha) {
  }

  private void setRCVal(int row, int col, int val) {
    gridState[row][col] = val;
    lastDroppedPos[0] = row;
    lastDroppedPos[1] = col;
    // pieces[row][col].setValue(val);
  }

  private void startDrop(int row, int col, int val) {
    activeDropCoin = new Coin(val - 1);
    activeDropCoin.setParent(this);

    activeDropCoin.setWorldPosition(pieces[row][col].getWorldX(), ColumnBoard.topSpawnY);
    targetY = pieces[row][col].getWorldY();
    droppingTo[0] = row;
    droppingTo[1] = col;
    isDropping = true;
    activeDropCoin.gravityOn = true;
  }

  public void onRCVal(Object... args) {
    setRCVal((int) args[0], (int) args[1], (int) args[2]);
    startDrop((int) args[0], (int) args[1], (int) args[2]);
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
}
