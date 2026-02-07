package game.states;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;

import game.core.GameState;
import game.GameCanvas;
import game.core.EntityManager;

public class PlayState extends GameState {

  private EntityManager entityManager;

  public PlayState() {
    stateName = "play";

    entityManager = new EntityManager();
  }

  public void fixedUpdate() {
    entityManager.fixedUpdate();
  }

  public void update() {
    entityManager.update();
  }

  public void render(Graphics2D g, float alpha) {
    entityManager.render(g, alpha);

    g.setFont(new Font("Arial", Font.BOLD, 24));
    g.setColor(Color.BLACK);

    FontMetrics fm = g.getFontMetrics();
    String text = "Connect 4";

    g.drawString(text,
        (GameCanvas.WIDTH - fm.stringWidth(text)) / 2,
        fm.getHeight() / 2 + fm.getAscent());
  }
}
