package game.core.graphics;

import java.awt.image.BufferedImage;

public class Animation {

  private BufferedImage[] frames;
  private float frameDuration; // seconds per frame
  private boolean loop;

  public Animation(BufferedImage[] frames, float frameDuration, boolean loop) {
    this.frames = frames;
    this.frameDuration = frameDuration;
    this.loop = loop;
  }

  public BufferedImage getFrame(int index) {
    return frames[index];
  }

  public int getFrameCount() {
    return frames.length;
  }

  public float getFrameDuration() {
    return frameDuration;
  }

  public boolean isLooping() {
    return loop;
  }
}
