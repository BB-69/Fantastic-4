package game;

import game.core.StateManager;
import game.states.MenuState;
import game.states.TransitionState;
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
    StateManager.setState(new MenuState());

    StateManager.getGlobalSignal().emit("enterMenu");
  }
}
