package game.states;

import game.core.GameState;
import game.core.StateManager;
import game.nodes.ui.transition.TransitionManager;

public class TransitionState extends GameState {

  private final TransitionState Instance = this;

  private TransitionManager tra;

  public TransitionState() {
    super();

    stateName = "transition";
    init();

    StateManager.getGlobalSignal().connect(Instance::onGlobalSignal);
  }

  private void init() {
    tra = new TransitionManager();
    nodeManager.addNode(tra);
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
    /* enter tra -> PlayState reset -> exit tra */
  }
}
