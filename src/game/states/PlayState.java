package game.states;

import game.core.GameState;
import game.core.signal.SignedSignal;
import game.nodes.board.BoardManager;
import game.nodes.ui.play.PlayUIManager;

public class PlayState extends GameState {

  private final PlayState Instance = this;

  private boolean pendingRestart = false;

  private SignedSignal globalSignal = new SignedSignal();

  private PlayUIManager ui;
  private BoardManager bmn;

  public PlayState() {
    super();

    stateName = "play";
    init();

    globalSignal.connect(Instance::onGlobalSignal);
  }

  private void init() {
    ui = new PlayUIManager(globalSignal);
    bmn = new BoardManager(globalSignal);
    nodeManager.addNode(bmn, ui);
  }

  @Override
  public void update() {
    super.update();

    if (pendingRestart) {
      pendingRestart = false;
      init();
    }
  }

  private void restart() {
    nodeManager.removeNode(bmn, ui);
    ui.destroyRecursive();
    bmn.destroyRecursive();
    pendingRestart = true;
  }

  private void onGlobalSignal(String signalName, Object... args) {
    switch (signalName) {
      case "restart":
        onRestart(args);
        break;
      default:
    }
  }

  private void onRestart(Object... args) {
    restart();
  }
}
