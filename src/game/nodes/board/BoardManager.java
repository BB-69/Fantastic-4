package game.nodes.board;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import game.core.node.Node;
import game.core.signal.Signal;
import game.core.signal.SignedSignal;
import game.util.Log;

public class BoardManager extends Node {

  private final BoardManager Instance = this;

  private BoardLogic boardl = new BoardLogic();
  private Board board = new Board();
  private ColumnBoard colBoard = new ColumnBoard();

  private int currentPlayer = 0;
  private int totalDropped = 0;
  private boolean gameOver = false;

  // 0 = none, 1 = P1 win, 2 = P2 win, 3 = tie
  private int pendingResult = 0;

  private SignedSignal globalSignal;

  private Signal signalRCVal = new Signal();
  private Signal signalCurP = new Signal();
  private Signal signalTotalCoin = new Signal();
  private Signal signalGameOver = new Signal();

  private Signal signalBoardPos = new Signal();
  private Signal signalPendingSpecial = new Signal();

  private Signal signalCoinDropFinish = new Signal();
  private Signal signalColClick = new Signal();

  public BoardManager(SignedSignal globalSignal) {
    super();

    this.globalSignal = globalSignal;
    globalSignal.connect(Instance::onGlobalSignal);

    boardl.setParent(this);
    board.setParent(this);
    colBoard.setParent(this);

    board.attachLogic(boardl);

    signalRCVal.connect(board::onRCVal); // signalRCVal
    signalCurP.connect(board::onCurP); // signalCurP
    signalCurP.connect(Instance::onCurP);
    signalCurP.connect(colBoard::onCurP);
    signalTotalCoin.connect(Instance::onTotalCoin); // signalTotalCoin
    signalTotalCoin.connect(board::onTotalCoin);
    signalGameOver.connect(board::onGameOver); // signalGameOver
    signalGameOver.connect(Instance::onGameOver);
    signalGameOver.connect(colBoard::onGameOver);
    colBoard.attachGameOverSignal(signalGameOver);

    signalBoardPos.connect(colBoard::onBoardPos); // signalBoardPos
    board.attachPosSignal(signalBoardPos);
    signalPendingSpecial.connect(board::onPendingSpecial);
    signalPendingSpecial.connect(colBoard::onPendingSpecial);

    signalCoinDropFinish.connect(colBoard::onCoinDropFinish); // signalCoinDropFinish
    signalCoinDropFinish.connect(Instance::onCoinDropFinish);
    colBoard.attachCoinDropFinishSignal(signalCoinDropFinish);
    board.attachCoinDropFinishSignal(signalCoinDropFinish);
    signalColClick.connect(Instance::onColClick); // signalColClick
    colBoard.passColClickSignaller(signalColClick);

    signalCurP.emit(currentPlayer);
  }

  @Override
  public void update() {
  }

  @Override
  public void fixedUpdate() {
    super.fixedUpdate();

    board.fixedUpdate();
    colBoard.fixedUpdate();
  }

  @Override
  public void render(Graphics2D g, float alpha) {
  }

  public boolean handleMove(int column) {
    if (currentPlayer == 0 || gameOver || pendingResult != 0)
      return false;

    if (!boardl.dropPiece(column, currentPlayer)) {
      printState("Column full!");
      return false;
    }
    setTotalDropped(totalDropped + 1);

    // boardl.printGrid();

    int[] pos = boardl.getlastDroppedPos();
    signalRCVal.emit(pos[0], pos[1], currentPlayer);

    printState(String.format("Dropped at R%dC%d", BoardLogic.ROWS - pos[0], pos[1] + 1));

    pendingResult = checkWin(pos[0], pos[1]);
    return true;
  }

  private int checkWin(int row, int col) {
    int cellPlayer = boardl.getCell(row, col);
    if (cellPlayer != 0 && boardl.checkWin(row, col, cellPlayer))
      return cellPlayer;
    else if (totalDropped == BoardLogic.TOTAL_CELL)
      return 3;
    return 0;
  }

  private void checkMultipleWins(int col) {
    int[] results = new int[BoardLogic.ROWS];

    for (int row = 0; row < BoardLogic.ROWS; row++) {
      results[row] = checkWin(row, col);
    }

    // [0] -> P1
    // [1] -> P2
    // [2] -> tie
    long[] playerPoints = {
        Arrays.stream(results).filter(x -> x == 1).count(),
        Arrays.stream(results).filter(x -> x == 2).count(),
        Arrays.stream(results).filter(x -> x == 3).count()
    };

    int result = 0;

    if (playerPoints[0] + playerPoints[1] == 0)
      return;

    if (playerPoints[2] > 0
        || (playerPoints[0] == playerPoints[1])) {
      result = 3; // tie
    } else if (playerPoints[0] > playerPoints[1]) {
      result = 1;
    } else if (playerPoints[1] > playerPoints[0]) {
      result = 2;
    }

    pendingResult = result;
  }

  private void resolveResult(int result) {
    if (result == 0)
      return;

    gameOver = true;

    if (result == 3) {
      signalCurP.emit(3);
      Log.logInfo("Tie!");
    } else {
      signalCurP.emit(result);
      printState("Wins!");
    }

    board.invokeGlow(boardl.getWinChains());

    signalGameOver.emit();
  }

  private void switchTurn() {
    if (currentPlayer == 0) {
      currentPlayer = 1; // first real turn
    } else {
      currentPlayer = (currentPlayer == 1) ? 2 : 1;
    }

    signalCurP.emit(currentPlayer);
    Log.logInfo("Next -> P" + currentPlayer);
  }

  private void printState(String s) {
    Log.logInfo(String.format("P%d - %s", currentPlayer, s));
  }

  private void setTotalDropped(int total) {
    totalDropped = total;
    signalTotalCoin.emit(total);
  }

  private void resetGameState() {
    currentPlayer = 0;
    pendingResult = 0;
    setTotalDropped(0);
    gameOver = false;
  }

  private void onGlobalSignal(String signalName, Object... args) {
    switch (signalName) {
      case "startGameAction":
        resetGameState();
        signalCurP.emit(currentPlayer);
        signalCoinDropFinish.emit();
        break;
      case "specialCoinPending":
        signalPendingSpecial.emit(args);
        break;
      default:
    }
  }

  private void onCurP(Object... args) {
    globalSignal.emit("currentPlayer", args);
  }

  private void onTotalCoin(Object... args) {
    totalDropped = (int) args[0];
    globalSignal.emit("totalCoin", totalDropped);
  }

  private void onGameOver(Object... args) {
    globalSignal.emit("gameOver", args);
  }

  private void onColClick(Object... args) {
    handleMove((int) args[0]);
  }

  public void onBulkCoinsRemoved(List<int[]> batch) {
    List<Integer> existingCols = new ArrayList<>();

    for (int[] pos : batch) {
      int col = pos[1];
      if (!existingCols.contains(col)) {
        existingCols.add(col);
        checkMultipleWins(col);
      }
      setTotalDropped(totalDropped - 1);
    }
  }

  private void onCoinDropFinish(Object... args) {
    if (pendingResult != 0) {
      resolveResult(pendingResult);
      pendingResult = 0;
      return;
    }

    if (!gameOver)
      switchTurn();
    else
      board.invokeGlow(boardl.getWinChains());
  }
}
