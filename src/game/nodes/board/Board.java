package game.nodes.board;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import game.GameCanvas;
import game.core.audio.Sound;
import game.core.node.Node;
import game.core.signal.Signal;
import game.nodes.coin.Coin;
import game.nodes.specialCoin.SpecialCoin;

public class Board extends Node {

  private BoardLogic boardLogic;

  private Sound coinLandSound = new Sound("wood-block.wav");
  private Sound scPassiveSound = new Sound("coin-up.wav");

  public static final float PIECE_WIDTH = 54f;
  public static final float PIECE_HEIGHT = 54f;

  private final BoardPiece[][] pieces = new BoardPiece[BoardLogic.ROWS][BoardLogic.COLS];
  private int currentPlayer = 1;
  private int totalCoin = 0;
  private boolean gameOver = false;

  private Coin lastLandedCoin;
  private final int[] lastDroppedPos = new int[2];
  private Signal signalBoardPos;

  private SpecialCoin specialCoin = null;

  private Signal signalCoinDropFinish;

  private final List<DroppingCoin> droppingCoins = new ArrayList<>();
  private int pendingDespawnAnimations = 0;
  private final List<int[]> removalBatch = new ArrayList<>();

  private final List<Integer> passive1ToRemove = new ArrayList<>();
  private final java.util.Set<Integer> columnsAffectedBySpecial = new java.util.HashSet<>();

  // Gates phase progression: prevents advancing past current passive phase until
  // animations settle
  private boolean awaitingPassiveAnimations = false;

  private enum PassivePhase {
    IDLE,
    PASSIVE_COIN,
    PASSIVE_1,
    PASSIVE_2
  }

  private PassivePhase passivePhase = PassivePhase.IDLE;

  public Board() {
    super();

    coinLandSound.setVolume(-6.5f);

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
      coin.setGlow(true);
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
    if (passivePhase == PassivePhase.PASSIVE_COIN || !fromExistingRow)
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
          passive1ToRemove.add(drop.col);
        }
      }
    }
    if (landedSomething && droppingCoins.isEmpty()) {
      if (passivePhase == PassivePhase.IDLE)
        passivePhase = PassivePhase.PASSIVE_COIN;
      processPassives();
    }
  }

  private void processPassives() {

    if (!droppingCoins.isEmpty())
      return;

    if (awaitingPassiveAnimations)
      return;

    switch (passivePhase) {

      case PASSIVE_COIN:
        runPassiveCoin();
        break;

      case PASSIVE_1:
        runPassive1();
        break;

      case PASSIVE_2:
        runPassive2();
        break;

      case IDLE:
      default:
        checkWinsFromSpecialPassives();
        if (signalCoinDropFinish != null)
          signalCoinDropFinish.emit();
        break;
    }

    executeRemovalBatch();
  }

  private void runPassiveCoin() {
    if (lastLandedCoin instanceof SpecialCoin sc) {
      executeSpecialPassive(sc);

      // Convert SpecialCoin to normal Coin after passive is processed
      int row = lastDroppedPos[0];
      int col = lastDroppedPos[1];
      pieces[row][col].destroyCoin();
      Coin normalCoin = new Coin(sc.getPlayer());
      normalCoin.setParent(this);
      normalCoin.setWorldPosition(sc.getWorldX(), sc.getWorldY());
      normalCoin.spawn();
      lastLandedCoin = normalCoin;
      pieces[row][col].receiveCoin(normalCoin);

      scPassiveSound.play();
    }

    passivePhase = PassivePhase.PASSIVE_1;
  }

  private void checkWinsFromSpecialPassives() {
    if (columnsAffectedBySpecial.isEmpty())
      return;

    Node n = getParent();
    if (n instanceof BoardManager) {
      BoardManager bmn = (BoardManager) n;
      for (int col : columnsAffectedBySpecial) {
        bmn.checkWinsAfterSpecialPassive(col);
      }
    }

    columnsAffectedBySpecial.clear();
  }

  private void executeSpecialPassive(SpecialCoin sc) {
    switch (sc.getAttribute()) {
      case SpecialCoin.CoinAttribute.Splitter:
        handleSplitter(sc);
        break;
      case SpecialCoin.CoinAttribute.Bomb:
        handleBomb(sc);
        break;
      case SpecialCoin.CoinAttribute.Swapper:
        handleSwapper(sc);
        break;
      default:
    }

    sc.setAttribute(null);
  }

  private void handleSplitter(SpecialCoin sc) {
    int row = lastDroppedPos[0];
    int col = lastDroppedPos[1];
    columnsAffectedBySpecial.add(col);

    int bottomVal = boardLogic.getCell(row + 1, col);
    if (bottomVal <= 0)
      return;

    int spawned = 0;

    for (int dc : new int[] { -1, 1 }) {
      int targetCol = col + dc;
      int dropRow = getDropRow(targetCol);

      if (dropRow > row) {
        boardLogic.setCell(dropRow, targetCol, bottomVal);
        startDrop(dropRow, targetCol, bottomVal, row + 1);
        if (!pieces[0][targetCol].isRevealed())
          revealBack(targetCol);
        columnsAffectedBySpecial.add(targetCol);
        spawned++;
      }
    }

    if (spawned == 2)
      addRemoval(row + 1, col);
  }

  private void handleBomb(SpecialCoin sc) {
    int row = lastDroppedPos[0];
    int col = lastDroppedPos[1];
    columnsAffectedBySpecial.add(col);

    for (int r = row - 1; r <= row + 1; r++) {
      for (int c = col - 1; c <= col + 1; c++) {
        if (!inBounds(r, c))
          continue;
        if (r == row && c == col)
          continue;

        if (boardLogic.getCell(r, c) > 0) {
          addRemoval(r, c);
          columnsAffectedBySpecial.add(c);
        }
      }
    }
  }

  private void handleSwapper(SpecialCoin sc) {
    int row = lastDroppedPos[0];
    int col = lastDroppedPos[1];
    columnsAffectedBySpecial.add(col);

    for (int c = 0; c < BoardLogic.COLS; c++) {
      if (c == col)
        continue;
      if (boardLogic.getCell(row, c) <= 0)
        continue;

      boardLogic.toggleCoinPlayer(row, c);
      columnsAffectedBySpecial.add(c);

      Coin coin = pieces[row][c].extractCoin();
      if (coin != null) {
        coin.setPlayer(coin.getPlayer() == 0 ? 1 : 0);
        coin.flash(0.5f);
        pieces[row][c].receiveCoin(coin);
      }
    }

    // boardLogic.printGrid();
  }

  private void runPassive1() {
    for (int col : passive1ToRemove)
      addRemoval(BoardLogic.ROWS - 1, col);

    passive1ToRemove.clear();

    if (!removalBatch.isEmpty()) {
      awaitingPassiveAnimations = true;
    }

    passivePhase = PassivePhase.PASSIVE_2;
  }

  private void runPassive2() {
    if (totalCoin >= 24) {
      for (int col = 0; col < BoardLogic.COLS; col++) {
        if (boardLogic.getCell(0, col) != 0)
          addRemoval(0, col);

        if (boardLogic.getCell(BoardLogic.ROWS - 1, col) != 0)
          addRemoval(BoardLogic.ROWS - 1, col);
      }
    }

    if (!removalBatch.isEmpty()) {
      awaitingPassiveAnimations = true;
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
      awaitingPassiveAnimations = false;
      finishRemovalBatch();
    }
  }

  private void finishRemovalBatch() {

    for (int[] pos : removalBatch) {
      boardLogic.onBoardCoinRemoved(pos[0], pos[1]);
    }

    {
      Map<Integer, List<Integer>> groupedCol = new HashMap<>();

      for (int[] pos : removalBatch)
        groupedCol.computeIfAbsent(pos[1], ArrayList::new).add(pos[0]);

      for (Map.Entry<Integer, List<Integer>> e : groupedCol.entrySet())
        rebuildColumnFromLogic(
            e.getValue().stream().mapToInt(Integer::intValue).toArray(),
            e.getKey());
    }

    {
      Node n = getParent();
      if (n instanceof BoardManager) {
        BoardManager bmn = (BoardManager) n;
        bmn.onBulkCoinsRemoved(removalBatch);
      }
    }
    // boardLogic.printGrid();

    removalBatch.clear();

    if (droppingCoins.isEmpty())
      processPassives();
  }

  private void landCoin(DroppingCoin drop) {
    drop.coin.setGlow(false);
    drop.coin.gravityOn = false;
    drop.coin.vy = 0;

    lastLandedCoin = drop.coin;

    BoardPiece p = pieces[drop.row][drop.col];
    p.receiveCoin(drop.coin);

    coinLandSound.playAt(0.05f);
  }

  private void revealBack(int col) {
    for (BoardPiece[] row : pieces)
      row[col].revealBack();
  }

  private boolean inBounds(int r, int c) {
    return r >= 0 && r < BoardLogic.ROWS && c >= 0 && c < BoardLogic.COLS;
  }

  private void addRemoval(int r, int c) {
    if (!inBounds(r, c))
      return;

    for (int[] p : removalBatch)
      if (p[0] == r && p[1] == c)
        return;

    removalBatch.add(new int[] { r, c });
  }

  private int getDropRow(int col) {
    for (int r = BoardLogic.ROWS - 1; r >= 0; r--)
      if (boardLogic.getCell(r, col) == 0)
        return r;
    return -1;
  }

  private void rebuildColumnFromLogic(int[] removedRows, int col) {

    Arrays.sort(removedRows);

    for (int row = removedRows[removedRows.length - 1]; row >= 0; row--) {

      int targetVal = boardLogic.getCell(row, col);
      BoardPiece piece = pieces[row][col];

      piece.destroyCoin();

      if (targetVal == 0) {
        piece.setValue(0);
        continue;
      }

      startDrop(row, col, targetVal, row - removedRows.length);
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

    for (int col = 0; col < BoardLogic.COLS; col++)
      revealBack(col);
  }

  public void onBoardCoinRemoved(Object... args) {
    int removedRow = (int) args[0];
    int col = (int) args[1];

    rebuildColumnFromLogic(new int[] { removedRow }, col);
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

  public void reset() {
    currentPlayer = 1;
    totalCoin = 0;
    gameOver = false;
    lastDroppedPos[0] = -1;
    lastDroppedPos[1] = -1;
    specialCoin = null;
    droppingCoins.clear();
    pendingDespawnAnimations = 0;
    removalBatch.clear();
    passive1ToRemove.clear();
    columnsAffectedBySpecial.clear();
    passivePhase = PassivePhase.IDLE;
    awaitingPassiveAnimations = false;

    // Destroy all existing board pieces
    for (int row = 0; row < BoardLogic.ROWS; row++) {
      for (int col = 0; col < BoardLogic.COLS; col++) {
        BoardPiece piece = pieces[row][col];
        piece.destroyRecursive();
      }
    }

    // Recreate all board pieces (same as constructor)
    for (int row = 0; row < BoardLogic.ROWS; row++) {
      for (int col = 0; col < BoardLogic.COLS; col++) {
        BoardPiece p = new BoardPiece(0, row, col);

        p.setPosition(
            Board.PIECE_WIDTH * (col - ((BoardLogic.COLS - 1) / 2f)),
            Board.PIECE_HEIGHT * (row - ((BoardLogic.ROWS - 1) / 2f)));

        addChild(p);
        pieces[row][col] = p;
      }
    }

    lastLandedCoin = null;
  }

  @Override
  public void destroy() {
    super.destroy();
    coinLandSound.dispose();
    scPassiveSound.dispose();
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
