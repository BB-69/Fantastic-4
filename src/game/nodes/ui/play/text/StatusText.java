package game.nodes.ui.play.text;

import game.core.node.ui.Text;

public class StatusText extends Text {

  private int currentPlayer = 0;
  private boolean gameOver = false;

  public StatusText() {
    super();

    content = "No Player Active!";
    updateTextMetrics();
  }

  private void setPlayerText(int currentPlayer) {
    if (gameOver)
      return;

    this.currentPlayer = currentPlayer;

    content = switch (currentPlayer) {
      case 1, 2 -> "Current Player: " + currentPlayer;
      default -> "No Player Active!";
    };
    updateTextMetrics();
  }

  private void setGameOver(boolean gameOver) {
    this.gameOver = gameOver;

    content = switch (currentPlayer) {
      case 1, 2 -> "Player " + currentPlayer + " Wins!";
      default -> "Game Over!";
    };
    updateTextMetrics();
  }

  public void onCurP(Object... args) {
    setPlayerText((int) args[0]);
  }

  public void onGameOver(Object... args) {
    setGameOver(true);
  }
}
