package game.nodes.board;

import java.awt.Graphics2D;

import game.core.node.Node;
import game.core.signal.Signal;
import game.core.signal.SignedSignal;
import game.util.Log;

public class BoardManager extends Node {

  private final BoardManager Instance = this;

  private BoardLogic boardl = new BoardLogic();
  private Board board = new Board();
  private ColumnBoard colBoard = new ColumnBoard();

  private int currentPlayer = 1;
  private int totalDropped = 0;
  private boolean gameOver = false;

  private SignedSignal globalSignal;

  private Signal signalRCVal = new Signal();
  private Signal signalCurP = new Signal();
  private Signal signalGameOver = new Signal();

  private Signal signalBoardPos = new Signal();

  private Signal signalColClick = new Signal();

  public BoardManager(SignedSignal globalSignal) {
    super();

    this.globalSignal = globalSignal;
    globalSignal.connect(Instance::onGlobalSignal);

    boardl.setParent(this);
    board.setParent(this);
    colBoard.setParent(this);

    signalRCVal.connect(board::onRCVal); // signalRCVal
    signalCurP.connect(board::onCurP); // signalCurP
    signalCurP.connect(Instance::onCurP);
    signalCurP.connect(colBoard::onCurP);
    signalGameOver.connect(board::onGameOver); // signalGameOver
    signalGameOver.connect(Instance::onGameOver);
    signalGameOver.connect(colBoard::onGameOver);
    colBoard.attachGameOverSignal(signalGameOver);

    signalBoardPos.connect(colBoard::onBoardPos); // signalBoardPos
    board.attachPosSignal(signalBoardPos);

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
    if (gameOver)
      return false;

    if (!boardl.dropPiece(column, currentPlayer)) {
      printState("Column full!");
      return false;
    }
    totalDropped += 1;

    int[] pos = boardl.getlastDroppedPos();
    signalRCVal.emit(pos[0], pos[1], currentPlayer);

    printState(String.format("Dropped at R%dC%d", BoardLogic.ROWS - pos[0], pos[1] + 1));

    if (boardl.checkWin(pos[0], pos[1], currentPlayer)) {
      gameOver = true;
      signalGameOver.emit();
      printState("Wins!");
      return true;
    } else if (totalDropped == BoardLogic.TOTAL_CELL) {
      signalCurP.emit(3);
      gameOver = true;
      signalGameOver.emit();
      Log.logInfo("Tie!");
      return true;
    }

    switchTurn();
    return true;
  }

  private void switchTurn() {
    currentPlayer = (currentPlayer == 1) ? 2 : 1;
    signalCurP.emit(currentPlayer);
    Log.logInfo("Next -> P" + currentPlayer);
  }

  private void printState(String s) {
    Log.logInfo(String.format("P%d - %s", currentPlayer, s));
  }

  private void onGlobalSignal(String signalName, Object... args) {
    switch (signalName) {
      default:
    }
  }

  private void onCurP(Object... args) {
    globalSignal.emit("currentPlayer", args);
  }

  private void onGameOver(Object... args) {
    globalSignal.emit("gameOver", args);
  }

  private void onColClick(Object... args) {
    handleMove((int) args[0]);
  }
}
