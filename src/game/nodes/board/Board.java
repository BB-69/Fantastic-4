package game.nodes.board;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import game.GameCanvas;
import game.core.node.Node;
import game.core.signal.Signal;
import game.nodes.coin.Coin;

public class Board extends Node {

  private BoardLogic boardLogic;

  public static final float PIECE_WIDTH = 60f;
  public static final float PIECE_HEIGHT = 60f;

  private final int[][] gridState = new int[BoardLogic.ROWS][BoardLogic.COLS];
  private final BoardPiece[][] pieces = new BoardPiece[BoardLogic.ROWS][BoardLogic.COLS];
  private int currentPlayer = 1;
  private boolean gameOver = false;

  private final int[] lastDroppedPos = new int[2];
  private Signal signalBoardPos;

  private final List<DroppingCoin> droppingCoins = new ArrayList<>();

  public Board() {
    super();

    x = GameCanvas.WIDTH / 2;
    y = GameCanvas.HEIGHT / 2 + 70;

    for (int row = 0; row < BoardLogic.ROWS; row++) {
      for (int col = 0; col < BoardLogic.COLS; col++) {
        BoardPiece p = new BoardPiece(
            gridState[row][col], row, col);

        p.setPosition(
            Board.PIECE_WIDTH * (col - ((BoardLogic.COLS - 1) / 2f)),
            Board.PIECE_HEIGHT * (row - ((BoardLogic.ROWS - 1) / 2f)));

        addChild(p);
        pieces[row][col] = p;
      }
    }
  }

  public void attachLogic(BoardLogic logic) {
    this.boardLogic = logic;
  }

  @Override
  public void update() {
  }

  @Override
  public void fixedUpdate() {
    super.fixedUpdate();

    if (signalBoardPos != null)
      signalBoardPos.emit(x, y);

    updateDroppingCoins();
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
    startDrop(row, col, val, -1);
  }

  private void startDrop(int row, int col, int val, int fromRow) {
    Coin coin = new Coin(val - 1);
    coin.setParent(this);

    boolean fromExistingRow = fromRow > -1 && fromRow < BoardLogic.ROWS;

    float spawnX = pieces[row][col].getWorldX();
    float spawnY = (fromExistingRow)
        ? pieces[fromRow][col].getWorldY()
        : ColumnBoard.topSpawnY;
    float targetY = pieces[row][col].getWorldY();

    coin.setWorldPosition(spawnX, spawnY);
    coin.gravityOn = true;
    if (!fromExistingRow)
      coin.flash(0.25f);

    droppingCoins.add(new DroppingCoin(coin, row, col, targetY));
  }

  private void updateDroppingCoins() {
    for (int i = droppingCoins.size() - 1; i >= 0; i--) {
      DroppingCoin drop = droppingCoins.get(i);

      if (drop.coin.getWorldY() >= drop.targetY) {
        landCoin(drop);
        droppingCoins.remove(i);

        if (drop.row == 0) {
          pieces[BoardLogic.ROWS - 1][drop.col].despawnCoin();
        }
      }
    }
  }

  private void landCoin(DroppingCoin drop) {
    drop.coin.gravityOn = false;
    drop.coin.vy = 0;

    BoardPiece p = pieces[drop.row][drop.col];
    p.receiveCoin(drop.coin);
  }

  private void revealBack(int col) {
    for (BoardPiece[] row : pieces)
      row[col].revealBack();
  }

  private void rebuildColumnFromLogic(int startRow, int col) {

    for (int row = startRow; row >= 0; row--) {

      int targetVal = boardLogic.getCell(row, col);
      BoardPiece piece = pieces[row][col];

      piece.destroyCoin();

      if (targetVal == 0) {
        piece.setValue(0);
        continue;
      }

      startDrop(row, col, targetVal, row - 1);
    }
  }

  public void onRCVal(Object... args) {
    setRCVal((int) args[0], (int) args[1], (int) args[2]);
    startDrop((int) args[0], (int) args[1], (int) args[2]);
    revealBack((int) args[1]);
  }

  public void onCurP(Object... args) {
    this.currentPlayer = (int) args[0];
  }

  public void onGameOver(Object... args) {
    this.gameOver = true;
  }

  public void onBoardCoinRemoved(Object... args) {
    int removedRow = (int) args[0];
    int col = (int) args[1];

    rebuildColumnFromLogic(removedRow, col);
  }

  public void attachPosSignal(Signal signalBoardPos) {
    this.signalBoardPos = signalBoardPos;
  }

  private static class DroppingCoin {
    Coin coin;
    int row;
    int col;
    float targetY;

    DroppingCoin(Coin coin, int row, int col, float targetY) {
      this.coin = coin;
      this.row = row;
      this.col = col;
      this.targetY = targetY;
    }
  }
}
