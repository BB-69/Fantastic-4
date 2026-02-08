package game.nodes.board;

import java.awt.Graphics2D;

import game.GameCanvas;
import game.core.node.Node;

public class ColumnBoard extends Node {

  private boolean haveSelected = false;

  private final ColumnArea[] caList = java.util.stream.IntStream
      .range(0, BoardLogic.COLS)
      .mapToObj(i -> new ColumnArea(this, x + (i - (BoardLogic.COLS - 1) / 2f) * Board.PIECE_WIDTH, Board.PIECE_WIDTH))
      .toArray(ColumnArea[]::new);

  public ColumnBoard() {
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

  public void onBoardPos(Object[] args) {
    this.x = (float) args[0];
  }
}
