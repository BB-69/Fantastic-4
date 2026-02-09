package game.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public final class KeyInput implements KeyListener {

  private static final int KEY_COUNT = 256;
  private static final boolean[] keys = new boolean[KEY_COUNT];
  private static final boolean[] lastKeys = new boolean[KEY_COUNT];

  /* ===================== INIT ===================== */

  public static void init(java.awt.Component component) {
    KeyInput listener = new KeyInput();
    component.addKeyListener(listener);
    component.setFocusable(true);
    component.requestFocusInWindow();
  }

  /* ===================== FRAME UPDATE ===================== */

  public static void fixedUpdate() {
    System.arraycopy(keys, 0, lastKeys, 0, KEY_COUNT);
  }

  /* ===================== QUERY API ===================== */

  public static boolean isDown(int key) {
    return key < KEY_COUNT && keys[key];
  }

  public static boolean isPressed(int key) {
    return key < KEY_COUNT && keys[key] && !lastKeys[key];
  }

  public static boolean isReleased(int key) {
    return key < KEY_COUNT && !keys[key] && lastKeys[key];
  }

  /* ===================== EVENTS ===================== */

  @Override
  public void keyPressed(KeyEvent e) {
    int code = e.getKeyCode();
    if (code < KEY_COUNT)
      keys[code] = true;
  }

  @Override
  public void keyReleased(KeyEvent e) {
    int code = e.getKeyCode();
    if (code < KEY_COUNT)
      keys[code] = false;
  }

  @Override
  public void keyTyped(KeyEvent e) {
  }
}
