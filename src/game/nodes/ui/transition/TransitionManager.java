package game.nodes.ui.transition;

import java.awt.Color;
import java.awt.Graphics2D;

import game.GameCanvas;
import game.core.node.Node;
import game.util.Time;

public class TransitionManager extends Node {

  private static enum TransitionMode {
    Enter, Exit
  };

  private TransitionMode currentMode = TransitionMode.Enter;

  private boolean isTransitioning = false;
  private float progress = 0f;
  private float speed = 2f;

  @Override
  public void update() {
    if (isTransitioning) {
      progress += speed * (currentMode == TransitionMode.Enter ? 1 : -1) * Time.deltaTime;
      if ((currentMode == TransitionMode.Enter && progress > 1)
          || (currentMode == TransitionMode.Exit && progress < 0)) {

        progress = currentMode == TransitionMode.Enter ? 1 : 0;
        isTransitioning = false;

        if (currentMode == TransitionMode.Exit) {
          game.input.KeyInput.setListenerActive(true);
          game.input.MouseInput.setListenerActive(true);
        }
      }
    }
  }

  @Override
  public void render(Graphics2D g, float alpha) {
    g.setColor(Color.BLACK);

    float slope = (float) Math.sin((Math.PI / 2) * progress);

    g.fillRect(0,
        0,
        GameCanvas.WIDTH,
        (int) (GameCanvas.HEIGHT / 2f * slope));
    g.fillRect(0,
        (int) (GameCanvas.HEIGHT * (0.5f + 0.5f * (1 - slope))),
        GameCanvas.WIDTH,
        (int) (GameCanvas.HEIGHT / 2f * slope) + 1);
  }

  public void transitionEnter() {
    if (isTransitioning)
      return;
    isTransitioning = true;
    progress = 0;
    currentMode = TransitionMode.Enter;

    game.input.KeyInput.setListenerActive(false);
    game.input.MouseInput.setListenerActive(false);
  }

  public void transitionExit() {
    if (isTransitioning)
      return;
    isTransitioning = true;
    progress = 1;
    currentMode = TransitionMode.Exit;
  }

  public boolean isTransitioning() {
    return isTransitioning;
  }
}
