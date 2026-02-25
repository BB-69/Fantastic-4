package game.nodes.specialCoin;

import java.awt.Graphics2D;

import game.core.StateManager;
import game.core.audio.Sound;
import game.core.node.Node;
import game.core.signal.CanConnectSignal;
import game.core.signal.SignedSignal;

public class SpecialCoinManager extends Node implements CanConnectSignal {

  private final SpecialCoinManager Instance = this;

  private SpecialCoinLogic logic = new SpecialCoinLogic();
  private SpecialCoinLister lister = new SpecialCoinLister();

  private Sound newCoinSound = new Sound("studio-ding.wav");

  private int currentPlayer = 0;

  private SignedSignal globalSignal;

  public SpecialCoinManager(SignedSignal globalSignal) {
    super();

    newCoinSound.setVolume(5.5f);

    this.globalSignal = globalSignal;
    globalSignal.connect(Instance, Instance::onGlobalSignal);

    addChildren(lister);

    lister.attachLogic(logic);
  }

  @Override
  public void update() {
  }

  @Override
  public void render(Graphics2D g, float alpha) {
  }

  public void reset() {
    currentPlayer = 0;
    logic.reset();
    lister.reset();
  }

  private void setCurrentPlayer(int cur) {
    this.currentPlayer = cur;
  }

  private void handleTurnUpdate() {
    if (currentPlayer < 1 || currentPlayer > 2)
      return;

    SpecialCoin coin = logic.advanceTurn();
    if (logic.tryCoin(currentPlayer - 1 == 0 ? 1 : 0))
      newCoinSound.play();
    lister.updateListState();

    if (coin != null) {
      SpecialCoin newCoin = new SpecialCoin(coin.getPlayer(), coin.getAttribute());
      newCoin.setGlow(true);
      StateManager.getGlobalSignal().emit("specialCoinPending",
          newCoin);
    }
  }

  private void onGlobalSignal(String signalName, Object... args) {
    switch (signalName) {
      case "currentPlayer":
        setCurrentPlayer((int) args[0]);
        handleTurnUpdate();
        break;
      default:
    }
  }

  @Override
  public void disconnectSignals() {
    globalSignal.disconnect(Instance);
  }

  @Override
  public void destroy() {
    super.destroy();
    disconnectSignals();
    newCoinSound.dispose();
  }
}
