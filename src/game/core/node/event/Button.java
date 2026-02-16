package game.core.node.event;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import game.core.node.Area;
import game.core.node.Node;
import game.core.signal.Signal;
import game.input.MouseInput;

public class Button extends Area {

  protected Signal signalButtonClicked = new Signal();

  public Color color = Color.LIGHT_GRAY;

  public Button() {
    super();

    init();
  }

  public Button(Node parent) {
    super(parent);

    init();
  }

  private void init() {
    w = 50f;
    h = 50f;
  }

  @Override
  public void update() {
  }

  @Override
  public void fixedUpdate() {
    super.fixedUpdate();

    if (MouseInput.isAnyPressed() && isHovered())
      signalButtonClicked.emit();
  }

  @Override
  public void render(Graphics2D g, float alpha) {
    AffineTransform old = g.getTransform();
    g.translate(getWorldX(), getWorldY());

    g.rotate(rotation);
    g.setColor(MouseInput.isAnyDown() && isHovered()
        ? getClickColor(color)
        : color);
    g.fillRect(
        (int) (-w / 2),
        (int) (-h / 2),
        (int) w,
        (int) h);

    g.setTransform(old);
  }

  protected Color getClickColor(Color c) {
    float[] hsb = Color.RGBtoHSB(
        c.getRed(),
        c.getGreen(),
        c.getBlue(),
        null);
    hsb[2] = Math.max(0f, hsb[2] - 0.2f);
    return Color.getHSBColor(hsb[0], hsb[1], hsb[2]);
  }

  public Signal getClickSignal() {
    return signalButtonClicked;
  }
}
