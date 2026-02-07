package game.states;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;

import game.core.GameState;
import game.nodes.BoardManager;
import game.nodes.TestArea;
import game.GameCanvas;

public class PlayState extends GameState {

  public PlayState() {
    super();

    stateName = "play";

    BoardManager bmn = new BoardManager();
    nodeManager.addNode(bmn);

    bmn.handleMove(1);
    bmn.handleMove(1);
    bmn.handleMove(2);
    bmn.handleMove(2);
    bmn.handleMove(3);
    bmn.handleMove(3);
    bmn.handleMove(4);

    TestArea a = new TestArea();
    // a.setRotation((float) Math.PI / 4);
    nodeManager.addNode(a);
  }

  @Override
  public void render(Graphics2D g, float alpha) {
    super.render(g, alpha);

    g.setFont(new Font("Arial", Font.BOLD, 24));
    g.setColor(Color.BLACK);

    FontMetrics fm = g.getFontMetrics();
    String text = "Connect 4";

    g.drawString(text,
        (GameCanvas.WIDTH - fm.stringWidth(text)) / 2,
        fm.getHeight() / 2 + fm.getAscent());
  }
}
