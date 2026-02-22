package game.core.node.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

import game.core.AssetManager;
import game.core.node.Node;

public class Text extends Node {

  public String content = "newText";
  public Color color = Color.BLACK;
  public int style = Font.BOLD;
  public int size = 24;
  private Font font;
  private Rectangle2D bounds;

  public int lastSize = size;

  public Text() {
    super();

    updateTextMetrics();
  }

  public Text(String content) {
    super();

    this.content = content;
    updateTextMetrics();
  }

  @Override
  public void update() {
    lastSize = size;
  }

  @Override
  public void render(Graphics2D g, float alpha) {
    AffineTransform old = g.getTransform();
    g.translate(getWorldX(), getWorldY());

    g.setFont(font);
    g.setColor(color);

    g.drawString(
        content,
        -(int) bounds.getWidth() / 2,
        (int) bounds.getHeight() / 2);

    g.setTransform(old);
  }

  public void updateTextMetrics() {
    font = AssetManager.getFont("ZillaSlab-SemiBold.ttf", size);

    FontRenderContext frc = new FontRenderContext(
        new AffineTransform(), true, true);

    bounds = font.getStringBounds(content, frc);
  }

  public int getTextWidth() {
    return (int) bounds.getWidth();
  }

  public int getTextHeight() {
    return (int) bounds.getHeight();
  }
}
