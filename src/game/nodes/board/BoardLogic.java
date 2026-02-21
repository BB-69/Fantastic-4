package game.nodes.board;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import game.core.node.Node;
import game.util.Log;

public class BoardLogic extends Node {

  public static final int ROWS = 6;
  public static final int COLS = 7;
  public static final int TOTAL_CELL = ROWS * COLS;

  private final int[][] grid = new int[ROWS][COLS];
  // 0 = empty, 1 = player1, 2 = player2

  private final int[] lastDroppedPos = new int[2];

  private List<List<int[]>> lastWinChains = new ArrayList<>();

  public List<List<int[]>> getWinChains() {
    return lastWinChains;
  }

  public void clearWinChainsHistory() {
    lastWinChains.clear();
  }

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
    List<List<int[]>> wins = new ArrayList<>();

    collectDir(row, col, 1, 0, player, wins); // vertical
    collectDir(row, col, 0, 1, player, wins); // horizontal
    collectDir(row, col, 1, 1, player, wins); // diag ↘
    collectDir(row, col, 1, -1, player, wins); // diag ↙

    if (!wins.isEmpty()) {
      for (int i = 0; i < wins.size(); i++) {
        String msg = "Win chain " + (i + 1) + ": ";
        for (int[] pos : wins.get(i)) {
          msg += "(" + pos[0] + "," + pos[1] + ") ";
        }
        Log.logInfo(msg);
      }
    }

    lastWinChains.addAll(wins);

    return !wins.isEmpty();
  }

  private void collectDir(int r, int c, int dr, int dc, int p,
      List<List<int[]>> wins) {

    List<int[]> chain = new ArrayList<>();
    chain.add(new int[] { r, c });

    collectOneSide(r, c, dr, dc, p, chain, true);
    collectOneSide(r, c, -dr, -dc, p, chain, false);

    if (chain.size() >= 4) {
      wins.add(chain);
    }
  }

  private void collectOneSide(int r, int c, int dr, int dc, int p,
      List<int[]> chain, boolean appendEnd) {

    r += dr;
    c += dc;

    while (r >= 0 && r < ROWS && c >= 0 && c < COLS && grid[r][c] == p) {
      int[] pos = new int[] { r, c };

      if (appendEnd) {
        chain.add(pos);
      } else {
        chain.add(0, pos);
      }

      r += dr;
      c += dc;
    }
  }

  int getCell(int row, int col) {
    return (row >= 0 && row <= grid.length - 1 &&
        col >= 0 && col <= grid[0].length - 1)
            ? grid[row][col]
            : -1;
  }

  void setCell(int row, int col, int val) {
    if (getCell(row, col) > -1)
      grid[row][col] = val;
  }

  void toggleCoinPlayer(int row, int col) {
    if (getCell(row, col) > 0)
      grid[row][col] = grid[row][col] == 1 ? 2 : 1;
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
