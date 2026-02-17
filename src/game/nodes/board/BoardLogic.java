package game.nodes.board;

import java.awt.Graphics2D;

import game.core.node.Node;
import game.util.Log;

public class BoardLogic extends Node {

  public static final int ROWS = 6;
  public static final int COLS = 7;
  public static final int TOTAL_CELL = ROWS * COLS;

  private final int[][] grid = new int[ROWS][COLS];
  // 0 = empty, 1 = player1, 2 = player2

  private final int[] lastDroppedPos = new int[2];

  public BoardLogic() {
    super();
  }

  @Override
  public void update() {
  }

  @Override
  public void fixedUpdate() {
    super.fixedUpdate();
  }

  @Override
  public void render(Graphics2D g, float alpha) {
  }

  public void printGrid() {
    String output = "Current BoardLogic State:";
    for (int[] row : grid) {
      output += "\n";
      for (int val : row) {
        output += "[" + val + "] ";
      }
    }
    Log.logInfo(output);
  }

  boolean dropPiece(int col, int player) {
    if (col < 0 || col >= COLS)
      return false;

    for (int row = ROWS - 1; row >= 0; row--) {
      if (grid[row][col] == 0) {
        grid[row][col] = player;
        lastDroppedPos[0] = row;
        lastDroppedPos[1] = col;
        return true;
      }
    }
    return false; // column full
  }

  int[] getlastDroppedPos() {
    return lastDroppedPos;
  }

  boolean checkWin(int row, int col, int player) {
    return checkDir(row, col, 1, 0, player) // vertical
        || checkDir(row, col, 0, 1, player) // horizontal
        || checkDir(row, col, 1, 1, player) // diag ↘
        || checkDir(row, col, 1, -1, player); // diag ↙
  }

  private boolean checkDir(int r, int c, int dr, int dc, int p) {
    int count = 1;

    count += countPieces(r, c, dr, dc, p);
    count += countPieces(r, c, -dr, -dc, p);

    return count >= 4;
  }

  private int countPieces(int r, int c, int dr, int dc, int p) {
    int count = 0;
    r += dr;
    c += dc;

    while (r >= 0 && r < ROWS && c >= 0 && c < COLS && grid[r][c] == p) {
      count++;
      r += dr;
      c += dc;
    }
    return count;
  }

  int getCell(int row, int col) {
    return grid[row][col];
  }

  private void collapseColumn(int startRow, int col) {
    for (int row = startRow; row > 0; row--) {
      grid[row][col] = grid[row - 1][col];
    }

    grid[0][col] = 0;
  }

  public void onBoardCoinRemoved(Object... args) {
    int removedRow = (int) args[0];
    int col = (int) args[1];

    collapseColumn(removedRow, col);
  }
}
