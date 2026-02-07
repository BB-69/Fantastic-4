package game.states;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;

import game.core.GameState;
import game.GameCanvas;

public class PlayState extends GameState {

  public PlayState() {
    super();

    stateName = "play";
  }

  public void fixedUpdate() {
    nodeManager.fixedUpdate();
  }

  public void update() {
    nodeManager.update();
  }

  public void render(Graphics2D g, float alpha) {
    nodeManager.render(g, alpha);

    g.setFont(new Font("Arial", Font.BOLD, 24));
    g.setColor(Color.BLACK);

    FontMetrics fm = g.getFontMetrics();
    String text = "Connect 4";

    g.drawString(text,
        (GameCanvas.WIDTH - fm.stringWidth(text)) / 2,
        fm.getHeight() / 2 + fm.getAscent());
  }
}
