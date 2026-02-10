package game.input;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import game.GameCanvas;
import game.GameCanvas.RenderSize;

public final class MouseInput implements MouseListener, MouseMotionListener {
  private static final int MOUSE_BUTTON_COUNT = 3;
  private static final boolean[] buttons = new boolean[MOUSE_BUTTON_COUNT];
  private static final boolean[] lastButtons = new boolean[MOUSE_BUTTON_COUNT];
  private static boolean isAnyDown = false;
  private static boolean lastIsAnyDown = false;
  private static boolean isIn = false;
  private static boolean lastIsIn = false;
  private static final Point position = new Point();
  private static final Point lastPosition = new Point();

  /* ===================== INIT ===================== */

  public static void init(java.awt.Component component) {
    MouseInput listener = new MouseInput();
    component.addMouseListener(listener);
    component.addMouseMotionListener(listener);
  }

  /* ===================== FRAME UPDATE ===================== */

  public static void fixedUpdate() {
    System.arraycopy(buttons, 0, lastButtons, 0, MOUSE_BUTTON_COUNT);
    lastIsAnyDown = isAnyDown;
    lastIsIn = isIn;
    lastPosition.x = position.x;
    lastPosition.y = position.y;
  }

  /* ===================== QUERY API ===================== */

  public static boolean isAnyDown() {
    return isAnyDown;
  }

  public static boolean isAnyPressed() {
    return isAnyDown && !lastIsAnyDown;
  }

  public static boolean isAnyReleased() {
    return !isAnyDown && lastIsAnyDown;
  }

  public static boolean isDown(int button) {
    return button < MOUSE_BUTTON_COUNT && buttons[button];
  }

  public static boolean isPressed(int button) {
    return button < MOUSE_BUTTON_COUNT && buttons[button] && !lastButtons[button];
  }

  public static boolean isReleased(int button) {
    return button < MOUSE_BUTTON_COUNT && !buttons[button] && lastButtons[button];
  }

  public static boolean isIn() {
    return isIn;
  }

  public static boolean isEntered() {
    return isIn && !lastIsIn;
  }

  public static boolean isExited() {
    return !isIn && lastIsIn;
  }

  public static Point getRawPosition() {
    return position;
  }

  public static Point getLastRawPosition() {
    return lastPosition;
  }

  private static Point calculateVirtualPosition(Point pos) {
    Point offset = GameCanvas.getVirtualOffset();
    float renderScale = GameCanvas.getRenderScale();
    int vx = (int) ((pos.x - offset.x) / renderScale);
    int vy = (int) ((pos.y - offset.y) / renderScale);
    return new Point(vx, vy);
  }

  public static Point getPosition() {
    return calculateVirtualPosition(position);
  }

  public static Point getLastPosition() {
    return calculateVirtualPosition(lastPosition);
  }

  public static boolean isInsideGame() {
    Point offset = GameCanvas.getVirtualOffset();
    RenderSize render = GameCanvas.getRenderSize();
    return position.x >= offset.x &&
        position.y >= offset.y &&
        position.x < offset.x + render.w &&
        position.y < offset.y + render.h;
  }

  /* ===================== EVENTS ===================== */

  @Override
  public void mouseClicked(MouseEvent e) {
  }

  @Override
  public void mousePressed(MouseEvent e) {
    switch (e.getButton()) {
      case MouseEvent.BUTTON1:
        buttons[0] = true;
        break;
      case MouseEvent.BUTTON2:
        buttons[1] = true;
        break;
      case MouseEvent.BUTTON3:
        buttons[2] = true;
        break;
      default:
        break;
    }

    isAnyDown = buttons[0] || buttons[1] || buttons[2];
  }

  @Override
  public void mouseReleased(MouseEvent e) {
    switch (e.getButton()) {
      case MouseEvent.BUTTON1:
        buttons[0] = false;
        break;
      case MouseEvent.BUTTON2:
        buttons[1] = false;
        break;
      case MouseEvent.BUTTON3:
        buttons[2] = false;
        break;
      default:
        break;
    }

    isAnyDown = buttons[0] || buttons[1] || buttons[2];
  }

  @Override
  public void mouseEntered(MouseEvent e) {
    isIn = true;
  }

  @Override
  public void mouseExited(MouseEvent e) {
    isIn = false;
  }

  // isDown = true;
  @Override
  public void mouseDragged(MouseEvent e) {
    if (isAnyDown) {
      position.x = e.getX();
      position.y = e.getY();
    }
  }

  // isDown = false;
  @Override
  public void mouseMoved(MouseEvent e) {
    if (!isAnyDown) {
      position.x = e.getX();
      position.y = e.getY();
    }
  }
}
