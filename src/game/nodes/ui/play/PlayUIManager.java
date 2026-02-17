package game.nodes.ui.play;

import java.awt.Graphics2D;

import game.GameCanvas;
import game.core.node.Node;
import game.core.signal.Signal;
import game.core.signal.SignedSignal;
import game.nodes.ui.play.text.StatusText;

public class PlayUIManager extends Node {

  private final PlayUIManager Instance = this;

  private StatusText statusText = new StatusText();

  private TopMenu topMenu = new TopMenu();
  private StatusTurn statusTurn = new StatusTurn();

  private SignedSignal globalSignal;

  private Signal signalCurP = new Signal();
  private Signal signalGameOver = new Signal();

  public PlayUIManager(SignedSignal globalSignal) {
    super();

    this.globalSignal = globalSignal;
    globalSignal.connect(Instance::onGlobalSignal);

    x = GameCanvas.WIDTH / 2;
    y = GameCanvas.HEIGHT / 2;

    StatusTurnBG statusBG = new StatusTurnBG();

    addChildren(statusText, topMenu, statusBG);
    topMenu.addChild(statusTurn);

    statusText.setWorldY(-statusText.getTextHeight() * 2);
    topMenu.setWorldY(0);
    statusTurn.setWorldY(0);

    signalCurP.connect(topMenu::onCurP); // signalCurP
    signalCurP.connect(statusText::onCurP);
    signalCurP.connect(statusTurn::onCurP);
    signalCurP.connect(statusBG::onCurP);
    signalGameOver.connect(topMenu::onGameOver); // signalGameOver
    signalGameOver.connect(statusText::onGameOver);
    signalGameOver.connect(statusTurn::onGameOver);
  }

  @Override
  public void update() {
  }

  @Override
  public void render(Graphics2D g, float alpha) {
  }

  private void onTransitionDone(Object... args) {
    statusText.slideIn();
  }

  private void onGlobalSignal(String signalName, Object... args) {
    switch (signalName) {
      case "currentPlayer":
        signalCurP.emit(args);
        break;
      case "gameOver":
        signalGameOver.emit(args);
        break;
      case "restart":
        break;
      case "transitionDone":
        onTransitionDone(args);
        break;
      default:
    }
  }
}
