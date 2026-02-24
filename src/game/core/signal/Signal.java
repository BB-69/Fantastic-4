package game.core.signal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class Signal {
  private final List<Consumer<Object[]>> listeners = new ArrayList<>();
  private final Map<Object, Consumer<Object[]>> ownerListeners = new HashMap<>();

  public void connect(Consumer<Object[]> listener) {
    listeners.add(listener);
  }

  public void connect(Object owner, Consumer<Object[]> listener) {
    ownerListeners.put(owner, listener);
    listeners.add(listener);
  }

  public void disconnect(Consumer<Object[]> listener) {
    listeners.remove(listener);
  }

  public void disconnect(Object owner) {
    Consumer<Object[]> c = ownerListeners.remove(owner);
    if (c != null)
      listeners.remove(c);
  }

  public void emit(Object... args) {
    for (Consumer<Object[]> c : listeners) {
      c.accept(args);
    }
  }
}
