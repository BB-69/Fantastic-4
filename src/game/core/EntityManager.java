package game.core;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import game.core.node.Entity;

public class EntityManager {
  private List<Entity> entities = new ArrayList<>();
  private List<Entity> toAdd = new ArrayList<>();
  private List<Entity> toRemove = new ArrayList<>();

  public EntityManager() {
  }

  public EntityManager(List<Entity> eList) {
    entities.addAll(eList);
  }

  public void fixedUpdate() {
    for (Entity e : entities) {
      if (e.isActive())
        e.fixedUpdate();
    }
  }

  public void update() {
    for (Entity e : entities)
      if (e.isActive())
        e.update();
    entities.addAll(toAdd);
    entities.removeAll(toRemove);
    toAdd.clear();
    toRemove.clear();
  }

  public void render(Graphics2D g, float alpha) {
    for (Entity e : entities) {
      if (e.isActive())
        e.render(g, alpha);
    }
  }

  public void addEntity(Entity e) {
    toAdd.add(e);
  }

  public void removeEntity(Entity e) {
    toRemove.add(e);
  }
}
