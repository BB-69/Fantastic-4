package game.nodes.ui.transition;

import java.awt.Graphics2D;

import game.core.node.Node;

public class TransitionManager extends Node {

  private static boolean isTransitioning = false;

  @Override
  public void update() {
  }

  @Override
  public void render(Graphics2D g, float alpha) {
  }

  public static void enterTransition() {
    isTransitioning = true;

    isTransitioning = false;
  }

  public static void exitTransition() {
    isTransitioning = true;

    isTransitioning = false;
  }

  public static boolean isTransitioning() {
    return isTransitioning;
  }
}
