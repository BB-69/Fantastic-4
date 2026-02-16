package game.nodes.ui.play;

import java.awt.Graphics2D;

import game.GameCanvas;
import game.core.node.Node;
import game.core.node.ui.Text;
import game.core.signal.Signal;
import game.core.signal.SignedSignal;
import game.nodes.ui.play.text._StatusText;

public class PlayUIManager extends Node {

  private final PlayUIManager Instance = this;

  private Text titleText = new Text("Connect 4");
  private _StatusText statusText = new _StatusText();

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

    addChildren(titleText, statusText, topMenu, statusTurn);

    titleText.y = -GameCanvas.HEIGHT / 2 + titleText.getTextHeight();
    statusText.y = -GameCanvas.HEIGHT / 2 + 3 * titleText.getTextHeight();
    topMenu.setWorldY(0);
    statusTurn.setWorldY(0);

    signalCurP.connect(statusText::onCurP); // signalCurP
    signalCurP.connect(statusTurn::onCurP);
    signalGameOver.connect(statusText::onGameOver); // signalGameOver
  }

  @Override
  public void update() {
  }

  @Override
  public void render(Graphics2D g, float alpha) {
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
      default:
    }
  }
}
