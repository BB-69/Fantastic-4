package game;

import game.core.StateManager;
import game.states.PlayState;
import game.input.KeyInput;
import game.input.MouseInput;

public final class Game {

  public static void init(GameCanvas canvas) {
    KeyInput.init(canvas);
    MouseInput.init(canvas);

    StateManager.setState(new PlayState());
  }
}
