package game.nodes;

import game.core.node.Node;

public class Board extends Node {
  public static final int ROWS = 6;
  public static final int COLS = 7;

  private int[][] grid = new int[ROWS][COLS];
  // 0 = empty, 1 = player1, 2 = player2

  private int[] lastDroppedPos = new int[2];

  @Override
  public void update() {
  }

  boolean dropPiece(int col, int player) {
    if (col < 0 || col >= COLS)
      return false;

    for (int row = ROWS - 1; row >= 0; row--) {
      if (grid[row][col] == 0) {
        grid[row][col] = player;
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
}
