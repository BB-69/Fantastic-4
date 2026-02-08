package game.nodes;

import java.awt.Graphics2D;

import game.core.node.Node;

public class Board extends Node {

  private final int[][] gridState = new int[BoardLogic.ROWS][BoardLogic.COLS];
  private int currentPlayer = 1;
  private boolean gameOver = false;

  @Override
  public void update() {
  }

  @Override
  public void render(Graphics2D g, float alpha) {
  }

  public void onRCVal(Object... args) {
    this.gridState[(int) args[0]][(int) args[1]] = (int) args[2];
  }

  public void onCurP(Object... args) {
    this.currentPlayer = (int) args[0];
  }

  public void onGameOver(Object... args) {
    this.gameOver = true;
  }
}
