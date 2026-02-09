package game.core.signal;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class Signal {
  private final List<Consumer<Object[]>> listeners = new ArrayList<>();

  public void connect(Consumer<Object[]> listener) {
    listeners.add(listener);
  }

  public void emit(Object... args) {
    for (Consumer<Object[]> c : listeners) {
      c.accept(args);
    }
  }
}
