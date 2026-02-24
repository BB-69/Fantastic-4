package game.states;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import game.core.GameState;
import game.core.StateManager;
import game.core.signal.CanConnectSignal;
import game.nodes.ui.transition.TransitionManager;
import game.util.Time;

public class TransitionState extends GameState implements CanConnectSignal {

  private final TransitionState Instance = this;

  private TransitionManager tra;

  private static final Map<String, Supplier<GameState>> stateRegistry = new HashMap<>();

  static {
    stateRegistry.put("play", PlayState::new);
    stateRegistry.put("menu", MenuState::new);
  }

  private static enum TransitionIntent {
    Restart, Change
  }

  private TransitionIntent intent = TransitionIntent.Change;

  private String pendingStateChange = null;

  private boolean isTransitioning = false;
  private boolean isLoading = false;
  private float loadTimer = 0f;
  private float loadDuration = 0.3f;

  public TransitionState() {
    super();

    stateName = "transition";
    stateOrder = 10;
    init();

    StateManager.getGlobalSignal().connect(Instance, Instance::onGlobalSignal);
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

  public static GameState createState(String key) {
    Supplier<GameState> supplier = stateRegistry.get(key);
    if (supplier == null) {
      throw new IllegalArgumentException("Unknown key: " + key);
    }
    return supplier.get();
  }

  private void handleTransition() {
    if (!tra.isTransitioning() && loadTimer == 0f) {
      if (intent == TransitionIntent.Restart)
        StateManager.getGlobalSignal().emit("restartReload");
      else {

        if (pendingStateChange != null)
          StateManager.setState(createState(pendingStateChange));
        pendingStateChange = null;
      }
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

  private void setPendingStateChange(String stateName) {
    pendingStateChange = stateName;
  }

  private void onGlobalSignal(String signalName, Object... args) {
    switch (signalName) {
      case "enterMenu":
        onEnterMenu(args);
        break;
      case "enterGame":
        onEnterGame(args);
        break;
      case "transitionToState":
        onTransitionToState(args);
        break;
      case "restart":
        onRestart(args);
        break;
      case "quit":
        onQuit(args);
      default:
    }
  }

  private void onEnterMenu(Object... args) {
    tra.transitionEnterGame();
  }

  private void onEnterGame(Object... args) {
    tra.transitionEnter();
  }

  private void onTransitionToState(Object... args) {
    intent = TransitionIntent.Change;
    setPendingStateChange((String) args[0]);
    tra.transitionEnter();
    loadTimer = 0f;
    isTransitioning = true;
  }

  private void onRestart(Object... args) {
    intent = TransitionIntent.Restart;
    tra.transitionEnter();
    loadTimer = 0f;
    isTransitioning = true;
  }

  private void onQuit(Object... args) {
    tra.transitionExitGame();
  }

  @Override
  public void disconnectSignals() {
    StateManager.getGlobalSignal().disconnect(Instance);
  }
}
