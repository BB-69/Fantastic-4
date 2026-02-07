package game.core.audio;

import javax.sound.sampled.*;

import game.core.AssetManager;
import game.core.SoundData;

public class Sound {

  private Clip clip;

  public Sound(String name) {
    try {
      SoundData data = AssetManager.getSoundData(name);

      clip = AudioSystem.getClip();
      clip.open(
          data.format,
          data.data,
          0,
          data.data.length);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  // === BASIC PLAY ===
  public void play() {
    playAt(0f);
  }

  // === PLAY AT TIME (seconds) ===
  public void playAt(float seconds) {
    if (seconds < 0f)
      seconds = 0f;

    clip.stop();
    clip.setFramePosition(
        Math.min(
            (int) (seconds * clip.getFormat().getFrameRate()),
            clip.getFrameLength()));
    clip.start();
  }

  // === CONTROL ===
  public void stop() {
    clip.stop();
    clip.setFramePosition(0);
  }

  public void pause() {
    clip.stop();
  }

  public void resume() {
    clip.start();
  }

  public void loop() {
    clip.loop(Clip.LOOP_CONTINUOUSLY);
  }

  public void setVolume(float db) {
    FloatControl vc = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
    vc.setValue(db);
  }

  public void dispose() {
    clip.close();
  }
}
