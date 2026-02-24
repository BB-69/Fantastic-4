package game.nodes.board;

import java.awt.Graphics2D;

import game.core.node.Node;

import java.util.*;

public class BoardLogic extends Node {
  public static final int ROWS = 6;
  public static final int COLS = 7;
  public static final int TOTAL_CELL = ROWS * COLS;

  private boolean[] isReverseGravity = new boolean[COLS];  //สถานะแรงโน้มถ่วงของแต่ละคอลัมน์            
  private int turnCount = 0;       //นับจำนวนเทิร์นที่เล่นไปแล้ว                                    

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

  boolean dropPiece(int col, int player) {
    if (col < 0 || col >= COLS)
      return false;

    boolean droped = false;
    if (isReverseGravity[col]) {
        //แรงโน้มถ่วงกลับด้าน
        for (int row = 0; row < ROWS; row++) {
            if (grid[row][col] == 0) {
              grid[row][col] = player;
              lastDroppedPos[0] = row;
              lastDroppedPos[1] = col;
              droped = true;
              break;
            }
        }
    } else{for (int row = ROWS - 1; row >= 0; row--) {
      if (grid[row][col] == 0) {
        grid[row][col] = player;
        lastDroppedPos[0] = row;
        lastDroppedPos[1] = col;
        droped = true;
        break;
      }
    }
    }if (droped) { ///จัดการหลังจบเทิร์น
        turnCount++;
        if (turnCount % 4 == 0) {
            applyRandomGravity();
        }
        return true;
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

  private void applyRandomGravity(){ ///สุ่มแรงโน้มถ่วง
    for (int i = 0; i < COLS; i++) { ///รีเซ็ตแรงโน้มถ่วงใหม่
    isReverseGravity[i] = false;
    }

    ArrayList<Integer> columns = new ArrayList<>(); ///สร้างลิสต์หมายเลขคอลัมน์
    for (int i = 0; i < COLS; i++) {
        columns.add(i);
    }

    Collections.shuffle(columns);///สลับตำแหน่งหมายเลขคอลัมน์ในลิสต์แบบสุ่ม

    for (int i = 0; i < 3; i++) {///เลือก 3 คอลัมน์แรกที่สุ่มได้เพื่อเปลี่ยนแรงโน้มถ่วง
    int selectedColumn = columns.get(i);
    isReverseGravity[selectedColumn] = true;
    }
    recoinG();
  }

  private void recoinG() { ///ทำให้เหรียญตกลงมาตอนแรงโน้มถ่วงเปลี่ยน
    for (int c = 0; c < COLS; c++) {
        ArrayList<Integer> pieces = new ArrayList<>();
        for (int r = 0; r < ROWS; r++) {
            if (grid[r][c] != 0) {
                pieces.add(grid[r][c]);
                grid[r][c] = 0;
            }
        }

        if (isReverseGravity[c]) {
            for (int i = 0; i < pieces.size(); i++) {
                grid[i][c] = pieces.get(i);
            }
        } else {
            for (int i = 0; i < pieces.size(); i++) {
                grid[ROWS - 1 - i][c] = pieces.get(i);
            }
        }
    }
 }
 public int[][] getGrid() { //// 3
    return grid;
 }
 
}