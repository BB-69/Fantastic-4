package game.nodes.board;

import java.awt.Graphics2D;

import game.GameCanvas;
import game.core.node.Node;
import game.core.signal.Signal;
import game.nodes.coin.Coin;

public class ColumnBoard extends Node {

  // private boolean haveSelected = false;
  private int hoveredIndex = -1;
  private int currentPlayer = 0;
  private boolean gameOver = false;
  private Coin coin;

  private final ColumnArea[] caList = java.util.stream.IntStream
      .range(0, BoardLogic.COLS)
      .mapToObj(
          i -> new ColumnArea(this, i, x + (i - (BoardLogic.COLS - 1) / 2f) * Board.PIECE_WIDTH, Board.PIECE_WIDTH))
      .toArray(ColumnArea[]::new);

  public ColumnBoard() {
    super();

    y = GameCanvas.HEIGHT / 2;
  }

  @Override
  public void update() {
    hoveredIndex = -1;

    for (int i = 0; i < BoardLogic.COLS; i++) {
      caList[i].selected = false;

      if (hoveredIndex == -1 && caList[i].isMouseInside()) {
        hoveredIndex = i;
        caList[i].selected = true;
      }

      caList[i].setPosition(
          (i - (BoardLogic.COLS - 1) / 2f) * Board.PIECE_WIDTH,
          0);
    }
  }

  @Override
  public void fixedUpdate() {
    super.fixedUpdate();

    updatePreviewCoin();
  }

  @Override
  public void render(Graphics2D g, float alpha) {
  }

  private void updatePreviewCoin() {
    if (gameOver || currentPlayer <= 0 || currentPlayer >= 3) {
      destroyPreviewCoin();
      return;
    }

    float moveX = (hoveredIndex - (BoardLogic.COLS - 1) / 2f)
        * Board.PIECE_WIDTH;

    if (coin == null) {
      coin = new Coin(currentPlayer - 1);
      coin.spawn();
      coin.setParent(this);
      coin.setWorldY(150);
      if (hoveredIndex != -1)
        coin.x = moveX;
    }

    if (hoveredIndex != -1)
      coin.moveToX((int) moveX);
  }

  private void destroyPreviewCoin() {
    if (coin != null) {
      coin.destroy();
      coin = null;
    }
  }

  private void setCurrentPlayer(int cur) {
    this.currentPlayer = cur;
    destroyPreviewCoin();
  }

  private void gameOver() {
    this.gameOver = true;
    destroyPreviewCoin();
  }

  public void attachGameOverSignal(Signal signalGameOver) {
    for (int i = 0; i < BoardLogic.COLS; i++)
      signalGameOver.connect(caList[i]::onGameOver);
  }

  public void passColClickSignaller(Signal signalColClick) {
    for (int i = 0; i < BoardLogic.COLS; i++)
      caList[i].setColClickSignal(signalColClick);
  }

  public void onCurP(Object... args) {
    setCurrentPlayer((int) args[0]);
  }

  public void onGameOver(Object... args) {
    gameOver();
  }

  public void onBoardPos(Object... args) {
    this.x = (float) args[0];
  }
}
