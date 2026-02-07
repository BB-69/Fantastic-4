package game.nodes;

import java.awt.Graphics2D;

import game.core.node.Node;
import game.util.Log;

public class BoardManager extends Node {

  private BoardLogic boardl;
  private int currentPlayer;
  private boolean gameOver;

  public BoardManager() {
    boardl = new BoardLogic();
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

    if (!boardl.dropPiece(column, currentPlayer)) {
      printState("Column full!");
      return false;
    }

    int[] pos = boardl.getlastDroppedPos();

    printState(String.format("Dropped at R%dC%d", BoardLogic.ROWS - pos[0], pos[1] + 1));

    if (boardl.checkWin(pos[0], pos[1], currentPlayer)) {
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
