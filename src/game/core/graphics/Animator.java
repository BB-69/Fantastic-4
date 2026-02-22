package game.core.graphics;

import java.awt.image.BufferedImage;
import java.util.HashMap;

public class Animator {

  private HashMap<String, Animation> animations = new HashMap<>();

  private Animation current;
  private String currentName;

  private int frameIndex = 0;
  private float timer = 0f;
  private boolean finished = false;

  public void add(String name, Animation animation) {
    animations.put(name, animation);
  }

  public void play(String name) {
    if (name.equals(currentName))
      return;

    current = animations.get(name);
    currentName = name;
    frameIndex = 0;
    timer = current.getFrameDuration();
    finished = false;
  }

  public void update(float deltaTime) {
    if (current == null || finished)
      return;

    timer += deltaTime;

    if (timer >= current.getFrameDuration()) {
      timer -= current.getFrameDuration();
      frameIndex++;

      if (frameIndex >= current.getFrameCount()) {
        if (current.isLooping()) {
          frameIndex = 0;
        } else {
          frameIndex = current.getFrameCount() - 1;
          finished = true;
        }
      }
    }
  }

  public BufferedImage getCurrentFrame() {
    if (current == null)
      return null;
    return current.getFrame(frameIndex);
  }

  public boolean isFinished() {
    return finished;
  }
}
