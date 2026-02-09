package game.nodes.ui.play;

import java.awt.Graphics2D;

import game.GameCanvas;
import game.core.node.Node;
import game.core.node.ui.Text;
import game.core.signal.Signal;
import game.core.signal.SignedSignal;
import game.nodes.ui.play.button.RestartButton;
import game.nodes.ui.play.text.StatusText;

public class PlayUIManager extends Node {

  private final PlayUIManager Instance = this;

  private Text titleText = new Text("Connect 4");
  private StatusText statusText = new StatusText();

  private RestartButton restartButton = new RestartButton();

  private SignedSignal globalSignal;

  private Signal signalCurP = new Signal();
  private Signal signalGameOver = new Signal();
  private Signal signalRestart;

  public PlayUIManager(SignedSignal globalSignal) {
    super();

    this.globalSignal = globalSignal;
    globalSignal.connect(Instance::onGlobalSignal);

    x = GameCanvas.WIDTH / 2;
    y = GameCanvas.HEIGHT / 2;

    addChildren(titleText, statusText, restartButton);

    titleText.y = -GameCanvas.HEIGHT / 2 + titleText.getTextHeight();
    statusText.y = -GameCanvas.HEIGHT / 2 + 3 * titleText.getTextHeight();
    restartButton.setPosition(
        GameCanvas.WIDTH / 2 - restartButton.w,
        -GameCanvas.HEIGHT / 2 + restartButton.h);

    signalRestart = restartButton.getClickSignal();

    signalCurP.connect(statusText::onCurP); // signalCurP
    signalGameOver.connect(statusText::onGameOver); // signalGameOver
    signalRestart.connect(Instance::onRestart); // signalRestart
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

  private void onRestart(Object... args) {
    globalSignal.emit("restart", args);
  }
}
