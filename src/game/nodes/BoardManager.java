package game.nodes;

import java.awt.Graphics2D;

import game.core.node.Node;
import game.core.signal.Signal;
import game.util.Log;

public class BoardManager extends Node {

  private BoardLogic boardl;
  private Board board;
  private int currentPlayer;
  private boolean gameOver = false;

  private Signal signalRCVal = new Signal();
  private Signal signalCurP = new Signal();
  private Signal signalGameOver = new Signal();

  public BoardManager() {
    signalRCVal.connect(board::onRCVal);
    signalCurP.connect(board::onCurP);
    signalGameOver.connect(board::onGameOver);

    boardl = new BoardLogic();
    board = new Board();
    currentPlayer = 1;

    signalCurP.emit(currentPlayer);
    signalGameOver.emit(false);
  }

  @Override
  public void update() {
  }

  @Override
  public void fixedUpdate() {
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

    int[] pos = boardl.getlastDroppedPos();
    signalRCVal.emit(pos[0], pos[1], currentPlayer);

    printState(String.format("Dropped at R%dC%d", BoardLogic.ROWS - pos[0], pos[1] + 1));

    if (boardl.checkWin(pos[0], pos[1], currentPlayer)) {
      gameOver = true;
      signalGameOver.emit();
      printState("Wins!");
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
}
