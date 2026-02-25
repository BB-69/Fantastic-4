package game.nodes.ui.transition;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import game.GameCanvas;
import game.core.StateManager;
import game.core.node.Node;
import game.util.Time;

public class TransitionManager extends Node {

  private static enum TransitionMode {
    Enter, Exit
  };

  private TransitionMode currentMode = TransitionMode.Enter;

  private boolean isTransitioning = false;
  private boolean pendingFinish = false;
  private boolean pendingFrameDone = false;
  private float progress = 0f;
  private float speed = 2.4f;

  private boolean quittingGame = false;

  @Override
  public void update() {
    if (!isTransitioning)
      return;

    float dir = (currentMode == TransitionMode.Enter ? 1f : -1f);
    progress += speed * dir * Time.deltaTime;

    // Clamp progress
    if (currentMode == TransitionMode.Enter && progress >= 1f) {
      progress = 1f;
      pendingFinish = true;
    } else if (currentMode == TransitionMode.Exit && progress <= 0f) {
      progress = 0f;
      pendingFinish = true;
    }
  }

  private void finishTransition() {
    isTransitioning = false;

    StateManager.getGlobalSignal().emit(
        "transitionDone",
        currentMode == TransitionMode.Enter ? "enter" : "exit");

    if (currentMode == TransitionMode.Exit) {
      game.input.KeyInput.setListenerActive(true);
      game.input.MouseInput.setListenerActive(true);
    }

    if (quittingGame) {
      try {
        Thread.sleep(100);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      System.exit(0);
    }
  }

  @Override
  public void render(Graphics2D g, float alpha) {
    AffineTransform old = g.getTransform();
    g.translate(-GameCanvas.WIDTH, 0);
    g.scale(2f, 1f);

    g.setColor(Color.BLACK);

    float slope = (float) Math.sin((Math.PI / 2) * progress);

    if (currentMode == TransitionMode.Enter && !isTransitioning) {
      g.fillRect(0, 0, GameCanvas.WIDTH, GameCanvas.HEIGHT);
    } else {
      g.fillRect(0,
          0,
          GameCanvas.WIDTH,
          (int) (GameCanvas.HEIGHT / 2f * slope) + 1);
      g.fillRect(0,
          (int) (GameCanvas.HEIGHT * (0.5f + 0.5f * (1 - slope))),
          GameCanvas.WIDTH,
          (int) (GameCanvas.HEIGHT / 2f * slope) + 1);
    }

    g.setTransform(old);

    if (pendingFrameDone) {
      pendingFrameDone = false;
      pendingFinish = false;
      finishTransition();
    }
    if (pendingFinish)
      pendingFrameDone = true;
  }

  public void transitionEnter() {
    if (isTransitioning)
      return;
    isTransitioning = true;
    progress = 0;
    speed = 2.4f;
    currentMode = TransitionMode.Enter;

    game.input.KeyInput.setListenerActive(false);
    game.input.MouseInput.setListenerActive(false);
  }

  public void transitionExit() {
    if (isTransitioning)
      return;
    isTransitioning = true;
    progress = 1;
    speed = 2.4f;
    currentMode = TransitionMode.Exit;
  }

  public void transitionEnterGame() {
    if (isTransitioning)
      return;
    isTransitioning = true;
    progress = 1;
    speed = 1.6f;
    currentMode = TransitionMode.Exit;

    game.input.KeyInput.setListenerActive(false);
    game.input.MouseInput.setListenerActive(false);
  }

  public void transitionExitGame() {
    if (isTransitioning)
      return;
    isTransitioning = true;
    progress = 0;
    speed = 1.6f;
    currentMode = TransitionMode.Enter;

    game.input.KeyInput.setListenerActive(false);
    game.input.MouseInput.setListenerActive(false);

    quittingGame = true;
  }

  public boolean isTransitioning() {
    return isTransitioning;
  }
}
