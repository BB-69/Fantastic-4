package game.core;

import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.HashMap;
import javax.imageio.ImageIO;
import javax.sound.sampled.*;

public class AssetManager {

  // ===== STORAGE =====
  private static final HashMap<String, BufferedImage> textures = new HashMap<>();
  private static final HashMap<String, SoundData> sounds = new HashMap<>();
  private static final HashMap<String, Font> fonts = new HashMap<>();

  // ===== TEXTURES =====
  public static BufferedImage getTexture(String name) {
    if (textures.containsKey(name))
      return textures.get(name);

    String path = "/assets/textures/" + name;
    try (InputStream is = AssetManager.class.getResourceAsStream(path)) {
      if (is == null)
        throw new RuntimeException("Missing texture: " + name);
      BufferedImage img = ImageIO.read(is);
      textures.put(name, img);
      return img;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  // ===== SOUNDS =====
  public static SoundData getSoundData(String name) {
    if (sounds.containsKey(name))
      return sounds.get(name);

    String path = "/assets/sounds/" + name;
    try (InputStream is = AssetManager.class.getResourceAsStream(path)) {
      if (is == null)
        throw new RuntimeException("Missing sound: " + name);

      AudioInputStream ais = AudioSystem.getAudioInputStream(is);
      AudioFormat format = ais.getFormat();
      byte[] data = ais.readAllBytes();

      SoundData sd = new SoundData(data, format);
      sounds.put(name, sd);
      return sd;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  // ===== FONTS =====
  public static Font getFont(String name, float size) {
    String key = name + "_" + size;
    if (fonts.containsKey(key))
      return fonts.get(key);

    String path = "/assets/fonts/" + name;
    try (InputStream is = AssetManager.class.getResourceAsStream(path)) {
      if (is == null)
        throw new RuntimeException("Missing font: " + name);

      Font font = Font.createFont(Font.TRUETYPE_FONT, is)
          .deriveFont(size);
      fonts.put(key, font);
      return font;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  // ===== CLEANUP =====
  public static void clear() {
    textures.clear();
    sounds.clear();
    sounds.clear();
    fonts.clear();
  }
}
