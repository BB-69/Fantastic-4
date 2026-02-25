package game.states;

import game.core.GameState;
import game.core.StateManager;
import game.core.signal.CanConnectSignal;
import game.core.signal.SignedSignal;
import game.nodes.PlayTextureManager;
import game.nodes.board.BoardManager;
import game.nodes.specialCoin.SpecialCoinManager;
import game.nodes.ui.play.PlayUIManager;
import game.nodes.ui.play.PlayBackground;

public class PlayState extends GameState implements CanConnectSignal {

  private final PlayState Instance = this;

  private PlayTextureManager tex;
  private PlayUIManager ui;
  private BoardManager bmn;
  private SpecialCoinManager smn;

  public PlayState() {
    super();

    stateName = "play";
    PlayBackground bg = new PlayBackground();
    nodeManager.addNode(bg);
    stateOrder = 0;
    init();

    StateManager.getGlobalSignal().connect(Instance, Instance::onGlobalSignal);
  }

  private void init() {
    SignedSignal globalSignal = StateManager.getGlobalSignal();

    tex = new PlayTextureManager(globalSignal);
    ui = new PlayUIManager(globalSignal);
    bmn = new BoardManager(globalSignal);
    smn = new SpecialCoinManager(globalSignal);
    nodeManager.addNode(tex, ui, bmn, smn);
  }

  private void restartReload() {
    // Reset all managers to factory state
    bmn.reset();
    smn.reset();
    ui.reset();
    // PlayTextureManager doesn't need reset as it's stateless
  }

  private void onGlobalSignal(String signalName, Object... args) {
    switch (signalName) {
      case "restartReload":
        onRestartReload(args);
        break;
      default:
    }
  }

  private void onRestartReload(Object... args) {
    restartReload();
  }

  @Override
  public void disconnectSignals() {
    StateManager.getGlobalSignal().disconnect(Instance);
  }
}
