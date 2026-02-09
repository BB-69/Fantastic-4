package game.nodes.ui.play.button;

import java.awt.Color;
import java.awt.Graphics2D;

import game.core.graphics.Sprite;
import game.core.node.event.Button;
import game.util.Log;

public class RestartButton extends Button {

  private static final Sprite sprite = new Sprite("rotate-left.png");

  public RestartButton() {
    super();

    setSize(55, 55);
    color = Color.getHSBColor(0.7f, 0.6f, 0.85f);

    sprite.setSize(w * 0.7f, h * 0.7f);
    if (!sprite.isColorInverted())
      sprite.invertColor();

    RestartButton instance = this;
    signalButtonClicked.connect(instance::onRestart);
  }

  @Override
  public void render(Graphics2D g, float alpha) {
    super.render(g, alpha);

    int renderX = (int) lerp(getPrevWorldX(), getWorldX(), alpha);
    int renderY = (int) lerp(getPrevWorldY(), getWorldY(), alpha);
    sprite.setPosition(renderX, renderY + 1);

    sprite.draw(g);
  }

  private void onRestart(Object... args) {
    Log.logInfo("Game Restarted!");
  }
}
