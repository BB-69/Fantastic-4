package game.nodes.board;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import game.GameCanvas;
import game.core.node.Node;
import game.core.signal.Signal;
import game.nodes.coin.Coin;
import game.nodes.specialCoin.SpecialCoin;

public class Board extends Node {

  private BoardLogic boardLogic;

  public static final float PIECE_WIDTH = 54f;
  public static final float PIECE_HEIGHT = 54f;

  private final BoardPiece[][] pieces = new BoardPiece[BoardLogic.ROWS][BoardLogic.COLS];
  private int currentPlayer = 1;
  private int totalCoin = 0;
  private boolean gameOver = false;

  private final int[] lastDroppedPos = new int[2];
  private Signal signalBoardPos;

  private SpecialCoin specialCoin = null;

  private Signal signalCoinDropFinish;

  private final List<DroppingCoin> droppingCoins = new ArrayList<>();
  private int pendingDespawnAnimations = 0;
  private final List<int[]> removalBatch = new ArrayList<>();

  private enum PassivePhase {
    IDLE,
    PASSIVE_1,
    PASSIVE_2
  }

  private PassivePhase passivePhase = PassivePhase.IDLE;

  public Board() {
    super();

    x = GameCanvas.WIDTH / 2;
    y = GameCanvas.HEIGHT / 2 + 50;

    for (int row = 0; row < BoardLogic.ROWS; row++) {
      for (int col = 0; col < BoardLogic.COLS; col++) {
        BoardPiece p = new BoardPiece(
            0, row, col);

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
    lastDroppedPos[0] = row;
    lastDroppedPos[1] = col;
    // pieces[row][col].setValue(val);
  }

  private void startDrop(int row, int col, int val) {
    startDrop(row, col, val, -1);
  }

  private void startDrop(int row, int col, int val, int fromRow) {
    Coin coin;
    if (specialCoin != null) {
      coin = new SpecialCoin(specialCoin.getPlayer(), specialCoin.getAttribute());
      specialCoin = null;
    } else
      coin = new Coin(val - 1);
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
    boolean landedSomething = false;

    for (int i = droppingCoins.size() - 1; i >= 0; i--) {
      DroppingCoin drop = droppingCoins.get(i);

      if (drop.coin.getWorldY() >= drop.targetY) {
        landCoin(drop);
        droppingCoins.remove(i);
        landedSomething = true;

        /*
         * === PASSIVE 1 ===
         * If column full, destroy bottom coin
         */
        if (drop.row == 0) {
          removalBatch.add(
              new int[] { BoardLogic.ROWS - 1, drop.col });
        }
      }
    }
    if (landedSomething && droppingCoins.isEmpty()) {
      passivePhase = PassivePhase.PASSIVE_1;
      processPassives();
    }
  }

  private void processPassives() {

    switch (passivePhase) {

      case PASSIVE_1:
        runPassive1();
        break;

      case PASSIVE_2:
        runPassive2();
        break;

      case IDLE:
      default:
        if (signalCoinDropFinish != null)
          signalCoinDropFinish.emit();
        break;
    }

    executeRemovalBatch();
  }

  private void runPassive1() {

    // removalBatch already contains column overflow removals
    // collected during landing

    passivePhase = PassivePhase.PASSIVE_2;
  }

  private void runPassive2() {

    if (totalCoin >= 24) {
      for (int col = 0; col < BoardLogic.COLS; col++) {
        if (boardLogic.getCell(0, col) != 0)
          removalBatch.add(new int[] { 0, col });

        if (boardLogic.getCell(BoardLogic.ROWS - 1, col) != 0)
          removalBatch.add(new int[] { BoardLogic.ROWS - 1, col });
      }
    }

    passivePhase = PassivePhase.IDLE;
  }

  private void executeRemovalBatch() {

    if (removalBatch.isEmpty()) {

      if (passivePhase != PassivePhase.IDLE) {
        processPassives();
      } else if (signalCoinDropFinish != null) {
        signalCoinDropFinish.emit();
      }

      return;
    }

    pendingDespawnAnimations = 0;

    for (int[] pos : removalBatch) {
      Coin c = pieces[pos[0]][pos[1]].getCoin();
      if (c != null) {
        pendingDespawnAnimations++;
        BoardPiece p = pieces[pos[0]][pos[1]];
        p.despawnCoin();
        p.flash();
      }
    }

    if (pendingDespawnAnimations == 0)
      finishRemovalBatch();
  }

  public void notifyCoinDespawnFinished(int row, int col) {
    pendingDespawnAnimations--;

    if (pendingDespawnAnimations <= 0) {
      finishRemovalBatch();
    }
  }

  private void finishRemovalBatch() {

    for (int[] pos : removalBatch) {
      boardLogic.onBoardCoinRemoved(pos[0], pos[1]);
    }

    for (int[] pos : removalBatch) {
      rebuildColumnFromLogic(pos[0], pos[1]);
    }

    {
      Node n = getParent();
      if (n instanceof BoardManager) {
        BoardManager bmn = (BoardManager) n;
        bmn.onBulkCoinsRemoved(removalBatch);
      }
    }

    removalBatch.clear();

    if (droppingCoins.isEmpty())
      processPassives();
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

  public void invokeGlow(List<List<int[]>> wins) {
    if (!wins.isEmpty()) {
      for (int i = 0; i < wins.size(); i++) {
        for (int[] pos : wins.get(i)) {
          Coin coin = pieces[pos[0]][pos[1]].getCoin();
          if (coin != null)
            coin.setGlow(true);
        }
      }
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

  public void onTotalCoin(Object... args) {
    this.totalCoin = (int) args[0];
  }

  public void onGameOver(Object... args) {
    this.gameOver = true;
  }

  public void onBoardCoinRemoved(Object... args) {
    int removedRow = (int) args[0];
    int col = (int) args[1];

    rebuildColumnFromLogic(removedRow, col);
  }

  public void onPendingSpecial(Object... args) {
    this.specialCoin = (SpecialCoin) args[0];
  }

  public void attachPosSignal(Signal signalBoardPos) {
    this.signalBoardPos = signalBoardPos;
  }

  public void attachCoinDropFinishSignal(Signal signalCoinDropFinish) {
    this.signalCoinDropFinish = signalCoinDropFinish;
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
