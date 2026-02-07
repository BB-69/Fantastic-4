package game.nodes;

import java.awt.Graphics2D;

import game.core.node.Node;
import game.util.Log;

public class BoardManager extends Node {

  private Board board;
  private int currentPlayer;
  private boolean gameOver;

  public BoardManager() {
    board = new Board();
    currentPlayer = 1;
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

    if (!board.dropPiece(column, currentPlayer)) {
      printState("Column full!");
      return false;
    }

    int[] pos = board.getlastDroppedPos();

    printState(String.format("Dropped at R%dC%d", Board.ROWS - pos[0], pos[1] + 1));

    if (board.checkWin(pos[0], pos[1], currentPlayer)) {
      gameOver = true;
      printState("Wins!");
      return true;
    }

    switchTurn();
    return true;
  }

  private void switchTurn() {
    currentPlayer = (currentPlayer == 1) ? 2 : 1;
    Log.logInfo("Next -> P" + currentPlayer);
  }

  private void printState(String s) {
    Log.logInfo(String.format("P%d - %s", currentPlayer, s));
  }
}
