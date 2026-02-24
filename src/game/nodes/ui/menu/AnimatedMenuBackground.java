package game.nodes.ui.menu;

import java.awt.Graphics2D;
import game.GameCanvas;
import game.core.graphics.Sprite;
import game.core.node.Node;
import game.util.Time;

public class AnimatedMenuBackground extends Node {
    private Sprite sky, sea1, sea2, offsetImage1, offsetImage2, car, bird;
    private float timer = 0f;
    private float carBaseX = GameCanvas.WIDTH / 2f;
    private float carBaseY = GameCanvas.HEIGHT / 2f;
    private float birdBaseX = GameCanvas.WIDTH / 2f + 200;
    private float birdBaseY = GameCanvas.HEIGHT / 2f - 50;

    public AnimatedMenuBackground() {
        super();
        this.layer = -100;
        sky = new Sprite("sky.png");
        sky.setPosition(GameCanvas.WIDTH / 2f, GameCanvas.HEIGHT / 2f);
        sea1 = new Sprite("sea tree.png");
        sea2 = new Sprite("sea tree.png");
        sea1.setPosition(GameCanvas.WIDTH / 2f, GameCanvas.HEIGHT / 2f);
        sea2.setPosition((GameCanvas.WIDTH / 2f) + GameCanvas.WIDTH, GameCanvas.HEIGHT / 2f);
        offsetImage1 = new Sprite("loop around.png");
        offsetImage2 = new Sprite("loop around.png");
        offsetImage1.setPosition(GameCanvas.WIDTH / 2f, GameCanvas.HEIGHT / 2f);
        offsetImage2.setPosition((GameCanvas.WIDTH / 2f) + GameCanvas.WIDTH, GameCanvas.HEIGHT / 2f);
        // set รถและนกให้อยู่กึ่งกลางจอ
        car = new Sprite("car.png");
        car.setPosition(carBaseX, carBaseY);
        bird = new Sprite("bird.png");
        bird.setPosition(birdBaseX, birdBaseY);
    }

    @Override
    public void update() {
        float dt = (float) Time.deltaTime;
        timer += dt;
        // เลื่อนฉากหลัง (ทะเล)
        float seaSpeed = 50f * dt;
        sea1.x -= seaSpeed;
        sea2.x -= seaSpeed;
        if (sea1.x <= -GameCanvas.WIDTH / 2f)
            sea1.x = sea2.x + GameCanvas.WIDTH;
        if (sea2.x <= -GameCanvas.WIDTH / 2f)
            sea2.x = sea1.x + GameCanvas.WIDTH;
        // เลื่อนฉากหน้า (ถนน/หาดทราย)
        float offsetSpeed = 40f * dt;
        offsetImage1.x -= offsetSpeed;
        offsetImage2.x -= offsetSpeed;
        if (offsetImage1.x <= -GameCanvas.WIDTH / 2f)
            offsetImage1.x = offsetImage2.x + GameCanvas.WIDTH;
        if (offsetImage2.x <= -GameCanvas.WIDTH / 2f)
            offsetImage2.x = offsetImage1.x + GameCanvas.WIDTH;
        // ทำให้รถสั่น (ขยับเฉพาะแกน Y ขึ้นลงตามเวลา)
        car.y = carBaseY + (float) (Math.sin(timer * 4) * 4);
        // ทำให้นกบินอยู่ที่เดิม (ขยับขึ้นลงช้ากว่ารถ
        // เพื่อให้ดูเหมือนกำลังกระพือปีกร่อนอยู่)
        bird.x = birdBaseX + (float) (-Math.cos(timer * 0.2f) * 240);
        bird.y = birdBaseY + (float) (Math.sin(timer * 1.2f) * 20);
    }

    @Override
    public void render(Graphics2D g, float alpha) {
        sky.draw(g);
        sea1.draw(g);
        sea2.draw(g);
        offsetImage1.draw(g);
        offsetImage2.draw(g);
        car.draw(g);
        bird.draw(g);
    }
}