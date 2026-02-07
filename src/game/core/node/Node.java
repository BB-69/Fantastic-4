package game.core.node;

public abstract class Node {
  private boolean isActive = true;

  protected void setIsActive(boolean isActive) {
    this.isActive = isActive;
  }

  public boolean isActive() {
    return isActive;
  }

  public abstract void update();
}
