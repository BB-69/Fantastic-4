package game.nodes.board;

import java.awt.Color;
import java.awt.Graphics2D;

import game.GameCanvas;
import game.core.audio.Sound;
import game.core.node.Node;
import game.core.signal.CanConnectSignal;
import game.core.signal.Signal;
import game.nodes.coin.Coin;
import game.nodes.coin.CoinTrailChain;
import game.nodes.specialCoin.SpecialCoin;

public class ColumnBoard extends Node implements CanConnectSignal {

  private Sound colHoverSound = new Sound("hit.wav");
  private Sound startDrop = new Sound("hit-table.wav");
  private Sound specialCoinSound = new Sound("bell-ding.wav");
  private Sound whooshSound = new Sound("quick-swhooshing-noise.wav");

  private int toPlayHoverSound = 0;

  private static final int offsetTop = 110;
  public static final int topSpawnY = 150;
  private float moveX = 0f;

  // private boolean haveSelected = false;
  private int hoveredIndex = -1;
  private int prevHoverIndex = hoveredIndex;
  private int currentPlayer = 0;
  private boolean gameOver = false;
  private Coin coin;

  private Signal signalGameOver;
  private Signal signalCoinDropFinish;
  private Signal signalColClick;

  private SpecialCoin specialCoin = null;
  private boolean canApplyPending = false;

  private boolean canSpawnNewCoin = false;

  private final ColumnArea[] caList = java.util.stream.IntStream
      .range(0, BoardLogic.COLS)
      .mapToObj(
          i -> new ColumnArea(this, i, x + (i - (BoardLogic.COLS - 1) / 2f) * Board.PIECE_WIDTH, Board.PIECE_WIDTH))
      .toArray(ColumnArea[]::new);

  public ColumnBoard() {
    super();

    colHoverSound.setVolume(-8);
    startDrop.setVolume(-1);
    specialCoinSound.setVolume(-3);

    y = GameCanvas.HEIGHT / 2 + offsetTop;
  }

  @Override
  public void update() {
  }

  @Override
  public void fixedUpdate() {
    super.fixedUpdate();

    updateColumnHover();
    updateCoin();
  }

  @Override
  public void render(Graphics2D g, float alpha) {
  }

  private void updateColumnHover() {
    hoveredIndex = -1;

    for (int i = 0; i < BoardLogic.COLS; i++) {
      caList[i].selected = false;

      if (hoveredIndex == -1 && caList[i].isHovered()) {
        hoveredIndex = i;
        caList[i].selected = true;

        if (prevHoverIndex != hoveredIndex)
          toPlayHoverSound++;
      }

      caList[i].setPosition(
          (i - (BoardLogic.COLS - 1) / 2f) * Board.PIECE_WIDTH,
          0);
    }

    prevHoverIndex = hoveredIndex;

    if (toPlayHoverSound > 0) {
      toPlayHoverSound--;
      colHoverSound.playAt(0.03f);
    }
  }

  private void updateCoin() {
    if (gameOver || currentPlayer <= 0 || currentPlayer >= 3) {
      destroyPreviewCoin();
      return;
    }

    moveX = (hoveredIndex - (BoardLogic.COLS - 1) / 2f)
        * Board.PIECE_WIDTH;

    if (coin == null && canSpawnNewCoin) {
      canSpawnNewCoin = false;
      if (specialCoin != null && canApplyPending) {
        coin = specialCoin;
        specialCoin = null;
        canApplyPending = false;

        specialCoinSound.playAt(0.02f);
      } else
        coin = new Coin(currentPlayer - 1);
      coin.layer = -7;
      coin.spawn();
      coin.setParent(this);
      coin.setWorldY(topSpawnY);
      if (hoveredIndex != -1)
        coin.x = moveX;
    }

    if (coin != null && hoveredIndex != -1)
      coin.moveToX((int) (getWorldX() + moveX));
  }

  private void destroyPreviewCoin() {
    if (coin != null) {
      coin.destroy();
      coin = null;
    }
  }

  private void setCurrentPlayer(int cur) {
    this.currentPlayer = cur;
  }

  private void checkIfTrail() {
    if (coin != null) {
      whooshSound.setVolume(
          Math.min(3,
              -20 + 0.15f * Math.abs(coin.getWorldX() - (getWorldX() + moveX))));
      whooshSound.play();
      startDrop.play();

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
  }

  private void gameOver() {
    checkIfTrail();

    this.gameOver = true;
    destroyPreviewCoin();
  }

  private void setSpawnNewCoin(boolean canSpawnNewCoin) {
    this.canSpawnNewCoin = canSpawnNewCoin;
  }

  public void attachGameOverSignal(Signal signalGameOver) {
    this.signalGameOver = signalGameOver;
    for (int i = 0; i < BoardLogic.COLS; i++)
      signalGameOver.connect(caList[i], caList[i]::onGameOver);
  }

  public void attachCoinDropFinishSignal(Signal signalCoinDropFinish) {
    this.signalCoinDropFinish = signalCoinDropFinish;
    for (int i = 0; i < BoardLogic.COLS; i++)
      signalCoinDropFinish.connect(caList[i], caList[i]::onCoinDropFinish);
  }

  public void passColClickSignaller(Signal signalColClick) {
    this.signalColClick = signalColClick;
    for (int i = 0; i < BoardLogic.COLS; i++)
      caList[i].setColClickSignal(signalColClick);

    ColumnBoard colb = this;
    signalColClick.connect(colb, colb::onColClick);
  }

  public void onCurP(Object... args) {
    setCurrentPlayer((int) args[0]);

    if (specialCoin != null)
      canApplyPending = true;
  }

  public void onGameOver(Object... args) {
    gameOver();
  }

  public void onBoardPos(Object... args) {
    this.x = (float) args[0];
  }

  public void onPendingSpecial(Object... args) {
    this.specialCoin = (SpecialCoin) args[0];
  }

  public void onCoinDropFinish(Object... args) {
    setSpawnNewCoin(true);
  }

  private void onColClick(Object... args) {
    for (int i = 0; i < BoardLogic.COLS; i++)
      caList[i].informCoinDropStart();

    setSpawnNewCoin(false);
    checkIfTrail();
    destroyPreviewCoin();
  }

  @Override
  public void disconnectSignals() {
    for (int i = 0; i < BoardLogic.COLS; i++)
      signalGameOver.disconnect(caList[i]);
    for (int i = 0; i < BoardLogic.COLS; i++)
      signalCoinDropFinish.disconnect(caList[i]);
    ColumnBoard colb = this;
    signalColClick.disconnect(colb);
  }

  public void reset() {
    hoveredIndex = -1;
    currentPlayer = 0;
    gameOver = false;
    specialCoin = null;
    canApplyPending = false;
    canSpawnNewCoin = false;
    moveX = 0f;
    destroyPreviewCoin();
    coin = null;

    // Reset all column areas
    for (int i = 0; i < BoardLogic.COLS; i++) {
      caList[i].reset();
    }
  }

  @Override
  public void destroy() {
    super.destroy();
    disconnectSignals();
    colHoverSound.dispose();
    startDrop.dispose();
    specialCoinSound.dispose();
    whooshSound.dispose();
  }
}
