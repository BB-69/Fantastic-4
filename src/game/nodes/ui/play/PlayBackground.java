package game.nodes.ui.play;

import java.awt.Graphics2D;
import game.GameCanvas;
import game.core.graphics.Sprite;
import game.core.node.Node;
import game.util.Time;

public class PlayBackground extends Node {

    private Sprite bgImage;
    private Sprite mikuImage;

    private float timer = 0f;

    private float mikuBaseX, mikuBaseY;
    private int baseWidth, baseHeight;

    public PlayBackground() {
        super();
        this.layer = -100;

        bgImage = new Sprite("BackGround_PlayState.png");
        bgImage.setPosition(GameCanvas.WIDTH / 2f, GameCanvas.HEIGHT / 2f);
        mikuImage = new Sprite("MikuSinging.png");

        mikuBaseX = GameCanvas.WIDTH / 2f;
        mikuBaseY = GameCanvas.HEIGHT / 2f;
        mikuImage.setPosition(mikuBaseX, mikuBaseY);

        baseWidth = GameCanvas.WIDTH;
        baseHeight = GameCanvas.HEIGHT;
    }

    @Override
    public void update() {
        timer += (float) Time.deltaTime;
        float danceSpeed = 4f;

        // คำนวณระยะขยับ
        float offsetX = (float) (Math.cos(timer * danceSpeed) * 5); // ส่ายซ้ายขวา 5px
        float offsetY = (float) (Math.sin(timer * danceSpeed) * 8); // โยกขึ้นลง 8px

        // อัปเดตตำแหน่งใหม่
        mikuImage.setPosition(mikuBaseX + offsetX, mikuBaseY + offsetY);

        // คำนวณอัตราส่วนยืดหด (0.05 = 5%)
        float stretchFactor = 1.0f + (float) (Math.sin(timer * danceSpeed * 2) * 0.05f);

        // คำนวณขนาดใหม่ (ถ้าแกนนึงยืดอีกแกนก็ต้องหดตามเพื่อให้มวลเท่าเดิม)
        int newWidth = (int) (baseWidth * (1.0f / stretchFactor)); // แกน X หด
        int newHeight = (int) (baseHeight * stretchFactor); // แกน Y ยืด

        // ขนาดใหม่
        mikuImage.setSize(newWidth, newHeight);

        // ทำให้เท้าติดพื้น
        mikuImage.y += (baseHeight - newHeight) / 2f;
    }

    @Override
    public void render(Graphics2D g, float alpha) {
        bgImage.draw(g);
        mikuImage.draw(g);
    }
}