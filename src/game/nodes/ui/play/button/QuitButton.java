package game.nodes.ui.play.button;

import java.awt.Graphics2D;

import game.core.AssetManager;
import game.core.StateManager;
import game.core.audio.Sound;
import game.core.graphics.Sprite;
import game.core.node.event.Button;
import game.core.signal.CanConnectSignal;
import game.nodes.ui.play.TopMenu;
import game.util.Log;
import game.util.calc.MathUtil;

public class QuitButton extends Button implements CanConnectSignal {

  private Sound quitSound = new Sound("notification.wav");

  private Sprite sprite;
  private float spriteScale = 0.7f;
  private boolean hoveredSprite = false;

  public QuitButton() {
    super();

    quitSound.setVolume(0);

    setSize(36, 36);
    color = TopMenu.c2;
    hoverColor = TopMenu.c1;

    sprite = new Sprite("x_brown.png");
    sprite.setSize(w * spriteScale, h * spriteScale);

    QuitButton Instance = this;
    signalButtonClicked.connect(Instance::onQuit);

    layer = 111;
  }

  @Override
  public void update() {
    super.update();

    if (isHovered() && !hoveredSprite) {
      sprite.setSprite("x_inverted.png", AssetManager.getTexture("x_inverted.png"));
      sprite.setSize(w * spriteScale, h * spriteScale);
      hoveredSprite = true;
    } else if (!isHovered() && hoveredSprite) {
      sprite.setSprite("x_brown.png", AssetManager.getTexture("x_brown.png"));
      sprite.setSize(w * spriteScale, h * spriteScale);
      hoveredSprite = false;
    }
  }

  @Override
  public void render(Graphics2D g, float alpha) {
    super.render(g, alpha);

    int renderX = (int) MathUtil.lerp(getPrevWorldX(), getWorldX(), alpha);
    int renderY = (int) MathUtil.lerp(getPrevWorldY(), getWorldY(), alpha);
    sprite.setPosition(renderX, renderY);

    sprite.draw(g);
  }

  private void onQuit(Object... args) {
    Log.logInfo("Game Quitted!");
    StateManager.getGlobalSignal().emit("quit");
    quitSound.play();
  }

  @Override
  public void disconnectSignals() {
    QuitButton Instance = this;
    signalButtonClicked.disconnect(Instance::onQuit);
  }

  @Override
  public void destroy() {
    super.destroy();
    disconnectSignals();
    quitSound.dispose();
  }
}
