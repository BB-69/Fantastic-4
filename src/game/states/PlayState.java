package game.states;

import game.core.GameState;
import game.core.StateManager;
import game.core.signal.SignedSignal;
import game.nodes.PlayTextureManager;
import game.nodes.board.BoardManager;
import game.nodes.specialCoin.SpecialCoinManager;
import game.nodes.ui.play.PlayUIManager;

public class PlayState extends GameState {

  private final PlayState Instance = this;

  private boolean pendingRestart = false;

  private PlayTextureManager tex;
  private PlayUIManager ui;
  private BoardManager bmn;
  private SpecialCoinManager smn;

  public PlayState() {
    super();

    stateName = "play";
    stateOrder = 0;
    init();

    StateManager.getGlobalSignal().connect(Instance::onGlobalSignal);
  }

  private void init() {
    SignedSignal globalSignal = StateManager.getGlobalSignal();

    tex = new PlayTextureManager(globalSignal);
    ui = new PlayUIManager(globalSignal);
    bmn = new BoardManager(globalSignal);
    smn = new SpecialCoinManager(globalSignal);
    nodeManager.addNode(tex, ui, bmn, smn);
  }

  @Override
  public void update() {
    super.update();

    if (pendingRestart) {
      pendingRestart = false;
      init();
    }
  }

  private void restartReload() {
    nodeManager.removeNode(smn, bmn, ui, tex);
    tex.destroyRecursive();
    ui.destroyRecursive();
    bmn.destroyRecursive();
    smn.destroyRecursive();
    pendingRestart = true;
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
}
