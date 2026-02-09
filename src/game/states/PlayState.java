package game.states;

import game.core.GameState;
import game.core.signal.SignedSignal;
import game.nodes.board.BoardManager;
import game.nodes.ui.play.PlayUIManager;

public class PlayState extends GameState {

  public PlayState() {
    super();

    stateName = "play";

    SignedSignal globalSignal = new SignedSignal();

    PlayUIManager ui = new PlayUIManager(globalSignal);
    BoardManager bmn = new BoardManager(globalSignal);

    nodeManager.addNode(bmn, ui);
  }
}
