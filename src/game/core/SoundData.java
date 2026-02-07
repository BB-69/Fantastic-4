package game.core;

import javax.sound.sampled.AudioFormat;

public class SoundData {
  public final byte[] data;
  public final AudioFormat format;

  public SoundData(byte[] data, AudioFormat format) {
    this.data = data;
    this.format = format;
  }
}
