package game.states;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Arrays;

import game.core.GameState;
import game.entities.Player;
import game.core.EntityManager;

public class PlayState extends GameState {

  private EntityManager entityManager;

  public PlayState() {
    stateName = "play";

    entityManager = new EntityManager(new ArrayList<>(Arrays.asList(
        new Player(0, 0))));
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
    g.setColor(Color.WHITE);

    g.drawString("Hello World!", 10, 35);
  }
}
