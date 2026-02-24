package game.nodes.ui.play;

import java.awt.Graphics2D;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import game.GameCanvas;
import game.core.node.Node;
import game.core.signal.CanConnectSignal;
import game.core.signal.Signal;
import game.core.signal.SignedSignal;
import game.nodes.ui.play.text.StatusText;
import game.nodes.ui.play.text.TotalCoinText;
import game.util.Log;

public class PlayUIManager extends Node implements CanConnectSignal {

  private final PlayUIManager Instance = this;

  private StatusText statusText = new StatusText();
  private StatusTurnBG statusBG = new StatusTurnBG();
  private TotalCoinText totalCoinText = new TotalCoinText();

  private TopMenu topMenu = new TopMenu();
  private StatusTurn statusTurn = new StatusTurn();

  private SignedSignal globalSignal;

  private Signal signalCurP = new Signal();
  private Signal signalTotalCoin = new Signal();
  private Signal signalGameOver = new Signal();

  private boolean uiInit = false;
  ScheduledExecutorService uiScheduler = Executors.newSingleThreadScheduledExecutor();

  public PlayUIManager(SignedSignal globalSignal) {
    super();

    this.globalSignal = globalSignal;
    globalSignal.connect(Instance::onGlobalSignal);

    x = GameCanvas.WIDTH / 2;
    y = GameCanvas.HEIGHT / 2;

    addChildren(statusText, totalCoinText, topMenu, statusBG);
    topMenu.addChild(statusTurn);

    statusText.setWorldY(-statusText.getTextHeight() * 2);
    totalCoinText.setWorldPosition(-totalCoinText.getTextWidth() * 2, 200);
    topMenu.setWorldY(0);
    statusTurn.setWorldY(0);

    statusText.setTargetY(-statusText.getTextHeight() * 2);
    totalCoinText.setTargetX(-totalCoinText.getTextWidth() * 2);

    signalCurP.connect(topMenu::onCurP); // signalCurP
    signalCurP.connect(statusText::onCurP);
    signalCurP.connect(statusTurn::onCurP);
    signalCurP.connect(statusBG::onCurP);
    signalTotalCoin.connect(totalCoinText::onTotalCoin); // signalTotalCoin
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

  private void initUI() {
    if (!uiInit) {
      uiInit = true;

      statusText.slideIn();

      uiScheduler.schedule(() -> {
        globalSignal.emit("startGameAction");
        Log.logInfo("Game Action Started!");

        uiScheduler.close();
      }, 1, TimeUnit.SECONDS);
    }
  }

  private void onRestartReload(Object... args) {
    uiInit = false;
  }

  private void onTransitionDone(Object... args) {
    boolean isEnter = ((String) args[0]).equals("enter");

    if (!isEnter)
      initUI();
  }

  private void onGlobalSignal(String signalName, Object... args) {
    switch (signalName) {
      case "currentPlayer":
        signalCurP.emit(args);
        break;
      case "totalCoin":
        signalTotalCoin.emit(args);
        break;
      case "gameOver":
        signalGameOver.emit(args);
        break;
      case "restart":
        break;
      case "restartReload":
        onRestartReload(args);
        break;
      case "transitionDone":
        onTransitionDone(args);
        break;
      default:
    }
  }

  @Override
  public void disconnectSignals() {
    globalSignal.disconnect(Instance::onGlobalSignal);

    signalCurP.disconnect(topMenu::onCurP); // signalCurP
    signalCurP.disconnect(statusText::onCurP);
    signalCurP.disconnect(statusTurn::onCurP);
    signalCurP.disconnect(statusBG::onCurP);
    signalTotalCoin.disconnect(totalCoinText::onTotalCoin); // signalTotalCoin
    signalGameOver.disconnect(topMenu::onGameOver); // signalGameOver
    signalGameOver.disconnect(statusText::onGameOver);
    signalGameOver.disconnect(statusTurn::onGameOver);
  }

  public void reset() {
    uiInit = false;
    uiScheduler.shutdownNow();
    uiScheduler = Executors.newSingleThreadScheduledExecutor();
    // Force UI components to reset their state when needed
    statusText.reset();
    totalCoinText.reset();
    statusTurn.reset();
    topMenu.reset();
    statusBG.reset();
  }

  @Override
  public void destroy() {
    super.destroy();
    disconnectSignals();
  }
}
