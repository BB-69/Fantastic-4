package game;

import game.core.StateManager;
import game.states.MenuState;
import game.states.TransitionState;
import game.util.Log;
import game.input.KeyInput;
import game.input.MouseInput;

public final class Game {

  public static void init(GameCanvas canvas) {
    KeyInput.init(canvas);
    MouseInput.init(canvas);

    game.input.KeyInput.setListenerActive(false);
    game.input.MouseInput.setListenerActive(false);

    TransitionState ts = new TransitionState();

    StateManager.addGlobalState(ts);

    MenuState ms = new MenuState();
    StateManager.setState(ms);

    Log.logInfo("Entering Game -> '" + ms.getStateName() + "' State");
    StateManager.getGlobalSignal().emit("enterMenu");
  }
}
