package game.nodes.specialCoin;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import game.GameCanvas;
import game.core.node.Node;
import game.core.node.ui.Text;
import game.nodes.coin.Coin;
import game.nodes.ui.play.TopMenu;

public class SpecialCoinLister extends Node {

  private SpecialCoinLogic logic;

  private List<CoinTask> coinTurns = new ArrayList<>();
  private Text[] turnTexts = new Text[3];

  private final float targetX = GameCanvas.WIDTH - 100;
  private final float targetY = 200;

  public SpecialCoinLister() {
    super();

    for (int i = 0; i < turnTexts.length; i++) {
      turnTexts[i] = new Text();
      turnTexts[i].setWorldPosition(
          -turnTexts[i].getTextWidth(), -turnTexts[i].getTextHeight());
      turnTexts[i].size = 14;
      turnTexts[i].color = TopMenu.c1.darker().darker();
      turnTexts[i].content = "%s Turns left";
      turnTexts[i].updateTextMetrics();
      addChild(turnTexts[i]);
    }
  }

  @Override
  public void update() {
  }

  @Override
  public void fixedUpdate() {
    super.fixedUpdate();

    for (int i = 0; i < coinTurns.size(); i++) {
      coinTurns.get(i).coin.setWorldPosition(
          targetX - Coin.COIN_SIZE * 1.5f,
          targetY + Coin.COIN_SIZE * 1.5f * i);
      turnTexts[i].setWorldPosition(
          targetX + turnTexts[i].getTextWidth() / 4,
          targetY + Coin.COIN_SIZE * 1.5f * i);
      turnTexts[i].content = (coinTurns.get(i).remaining < 100 && coinTurns.get(i).remaining > 0
          ? "" + coinTurns.get(i).remaining
          : "?") + " Turns left";
    }
  }

  @Override
  public void render(Graphics2D g, float alpha) {
  }

  public void attachLogic(SpecialCoinLogic logic) {
    this.logic = logic;
  }

  void updateListState() {
    List<CoinTask> newState = logic.getCoinState();
    List<SpecialCoin> newCoins = newState.stream().map(ct -> ct.coin).toList();

    // Remove old coins
    for (Node n : new ArrayList<>(children)) {
      if (n instanceof SpecialCoin && !newCoins.contains(n)) {
        n.destroy();
      }
    }

    // Add new coins
    for (SpecialCoin c : newCoins) {
      if (!children.contains(c))
        addChild(c);
    }

    coinTurns = newState;
  }
}
