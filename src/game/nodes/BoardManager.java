package game.nodes;

public class BoardManager {

  private Board board;
  private int currentPlayer;
  private boolean gameOver;

  public BoardManager() {
    board = new Board();
    currentPlayer = 1;
  }

  public void handleMove(int column) {
    if (gameOver)
      return;

    boolean success = board.dropPiece(column, currentPlayer);
    if (!success)
      return;

    int[] pos = board.getlastDroppedPos();

    if (board.checkWin(pos[0], pos[1], currentPlayer)) {
      gameOver = true;
      System.out.println("Player " + currentPlayer + " wins!");
      return;
    }

    switchTurn();
  }

  private void switchTurn() {
    currentPlayer = (currentPlayer == 1) ? 2 : 1;
  }
}
