package game.states;

import game.core.GameState;
import game.core.StateManager;
import game.nodes.ui.transition.TransitionManager;
import game.util.Time;

public class TransitionState extends GameState {

  private final TransitionState Instance = this;

  private TransitionManager tra;

  private boolean isTransitioning = false;
  private boolean isLoading = false;
  private float loadTimer = 0f;
  private float loadDuration = 0.5f;

  public TransitionState() {
    super();

    stateName = "transition";
    stateOrder = 10;
    init();

    StateManager.getGlobalSignal().connect(Instance::onGlobalSignal);
  }

  private void init() {
    tra = new TransitionManager();
    nodeManager.addNode(tra);
  }

  @Override
  public void update() {
    super.update();

    if (isTransitioning)
      handleTransition();
  }

  private void handleTransition() {
    if (!tra.isTransitioning() && loadTimer == 0f) {
      StateManager.getGlobalSignal().emit("restartReload");
      isLoading = true;
      loadTimer = 0.0001f;
    }

    if (isLoading) {
      loadTimer += Time.deltaTime;

      if (loadTimer >= loadDuration) {
        isLoading = false;
        isTransitioning = false;
        tra.transitionExit();
      }
    }
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
    tra.transitionEnter();
    loadTimer = 0f;
    isTransitioning = true;
  }
}
