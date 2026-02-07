package game;

import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.image.BufferStrategy;

import game.util.Log;
import game.util.Time;

public class GameLoop implements Runnable {

  private Thread thread;
  private boolean running;

  private GameCanvas canvas;

  private float accumulator = 0f;

  public GameLoop(GameCanvas canvas) {
    this.canvas = canvas;
  }

  public void start() {
    if (running)
      return;
    running = true;
    Time.init();

    // warm-up (no rendering)
    for (int i = 0; i < 60; i++) {
      Time.update();
      Engine.fixedUpdate();
      Engine.update();
    }
    System.gc();

    canvas.createBufferStrategy(3);
    thread = new Thread(this);
    thread.start();
  }

  @Override
  public void run() {
    BufferStrategy bs = canvas.getBufferStrategy();

    while (running) {

      float frameTime = Time.update();
      accumulator += frameTime;

      if (accumulator > 0.25) {
        Log.logInfo(String.format("Stutter with Accumulator: %.2f", accumulator));
      }
      while (accumulator >= Time.FIXED_DELTA) {
        Engine.fixedUpdate();
        accumulator -= Time.FIXED_DELTA;
      }

      Engine.update();

      float alpha = accumulator / Time.FIXED_DELTA;
      alpha = Math.min(alpha, 1f);

      do {
        do {
          Graphics2D g = (Graphics2D) bs.getDrawGraphics();

          g.clearRect(0, 0, GameCanvas.WIDTH, GameCanvas.HEIGHT);
          Engine.render(g, alpha);

          g.dispose();
        } while (bs.contentsRestored());

        bs.show();
      } while (bs.contentsLost());

      Toolkit.getDefaultToolkit().sync(); // Linux fix
    }
  }
}
