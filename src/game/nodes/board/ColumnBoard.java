package game.nodes.board;

import java.awt.Graphics2D;

import game.GameCanvas;
import game.core.node.Node;
import game.core.signal.Signal;

public class ColumnBoard extends Node {

  private boolean haveSelected = false;

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
    for (int i = 0; i < BoardLogic.COLS; i++) {
      if (!haveSelected && caList[i].isMouseInside()) {
        caList[i].selected = true;
        haveSelected = true;
      } else
        caList[i].selected = false;
      caList[i].setPosition((i - (BoardLogic.COLS - 1) / 2f) * Board.PIECE_WIDTH, 0);
    }
    haveSelected = false;
  }

  @Override
  public void render(Graphics2D g, float alpha) {
  }

  public void attachGameOverSignal(Signal signalGameOver) {
    for (int i = 0; i < BoardLogic.COLS; i++)
      signalGameOver.connect(caList[i]::onGameOver);
  }

  public void passColClickSignaller(Signal signalColClick) {
    for (int i = 0; i < BoardLogic.COLS; i++)
      caList[i].setColClickSignal(signalColClick);
  }

  public void attachCurPSignal(Signal signalCurP) {
    for (int i = 0; i < BoardLogic.COLS; i++)
      signalCurP.connect(caList[i]::onCurP);
  }

  public void onBoardPos(Object... args) {
    this.x = (float) args[0];
  }
}
