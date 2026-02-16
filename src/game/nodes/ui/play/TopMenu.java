package game.nodes.ui.play;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import game.core.node.Node;
import game.nodes.ui.play.button.QuitButton;
import game.nodes.ui.play.button.RestartButton;

public class TopMenu extends Node {

  public static final Color c1 = Color.getHSBColor(0.1f, 1f, 0.5f);
  public static final Color c2 = Color.getHSBColor(0.12f, 0.45f, 1f);

  private float width = 90f;
  private float height = 40f;
  private float extensionX = 16f;
  private float extensionY = 8f;
  private float trapezoidOffset = 48f;

  private RestartButton restartButton = new RestartButton();
  private QuitButton quitButton = new QuitButton();

  public TopMenu() {
    addChildren(restartButton, quitButton);

    int padding = 6;
    restartButton.setPosition((restartButton.w + padding) / 2, (height + extensionY) / 2);
    quitButton.setPosition(-(quitButton.w + padding) / 2, (height + extensionY) / 2);
  }

  @Override
  public void update() {
  }

  @Override
  public void render(Graphics2D g, float alpha) {
    AffineTransform old = g.getTransform();
    g.translate(getWorldX(), getWorldY());

    // g.fillRect((int) (-width / 2), 0, (int) width, (int) height);
    g.setColor(c1);
    g.fillPolygon(
        new int[] { (int) (-(width + trapezoidOffset) / 2 - extensionX),
            (int) ((width + trapezoidOffset) / 2 + extensionX),
            (int) ((width) / 2 + extensionX),
            (int) (-(width) / 2 - extensionX) },
        new int[] { 0, 0, (int) height, (int) height },
        4);
    g.setColor(c2);
    g.fillPolygon(
        new int[] { (int) (-(width + trapezoidOffset) / 2),
            (int) ((width + trapezoidOffset) / 2),
            (int) ((width) / 2),
            (int) (-(width) / 2) },
        new int[] { 0, 0, (int) (height + extensionY), (int) (height + extensionY) },
        4);

    g.setTransform(old);
  }
}
