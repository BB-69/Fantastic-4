package game.nodes.board;

import java.awt.Color;
import java.awt.Graphics2D;

import game.GameCanvas;
import game.core.node.Node;
import game.core.signal.Signal;
import game.nodes.coin.Coin;
import game.nodes.coin.CoinTrailChain;

public class ColumnBoard extends Node {

  private static final int offsetTop = 110;
  public static final int topSpawnY = 150;
  private float moveX = 0f;

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

    y = GameCanvas.HEIGHT / 2 + offsetTop;
  }

  @Override
  public void update() {
    hoveredIndex = -1;

    for (int i = 0; i < BoardLogic.COLS; i++) {
      caList[i].selected = false;

      if (hoveredIndex == -1 && caList[i].isHovered()) {
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

    updateCoin();
  }

  @Override
  public void render(Graphics2D g, float alpha) {
  }

  private void updateCoin() {
    if (gameOver || currentPlayer <= 0 || currentPlayer >= 3) {
      destroyPreviewCoin();
      return;
    }

    moveX = (hoveredIndex - (BoardLogic.COLS - 1) / 2f)
        * Board.PIECE_WIDTH;

    if (coin == null) {
      coin = new Coin(currentPlayer - 1);
      coin.layer = -7;
      coin.spawn();
      coin.setParent(this);
      coin.setWorldY(topSpawnY);
      if (hoveredIndex != -1)
        coin.x = moveX;
    }

    if (hoveredIndex != -1)
      coin.moveToX((int) (getWorldX() + moveX));
  }

  private void destroyPreviewCoin() {
    if (coin != null) {
      coin.destroy();
      coin = null;
    }
  }

  private void setCurrentPlayer(int cur) {
    checkIfTrail();

    this.currentPlayer = cur;
    destroyPreviewCoin();
  }

  private void checkIfTrail() {
    if (coin != null)
      getNodeManagerInstance().addNode(
          new CoinTrailChain((int) coin.getWorldX(),
              (int) coin.getWorldY(),
              (int) (getWorldX() + moveX),
              (int) coin.getWorldY(),
              switch (currentPlayer) {
                case 1 -> Color.RED;
                case 2 -> Color.YELLOW;
                default -> Color.DARK_GRAY;
              }));
  }

  private void gameOver() {
    checkIfTrail();

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
