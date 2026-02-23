package game.nodes.ui.menu;

import java.awt.Color;
import java.awt.Graphics2D;
import game.GameCanvas;
import game.core.StateManager;
import game.core.graphics.Sprite;
import game.core.node.Node;
import game.core.node.event.Button;
public class MenuUIManager extends Node {
    private Button startButton;
    private Button quitButton;
    private Sprite titleImage;
    private Sprite startImage;
    private Sprite quitImage;
    public MenuUIManager() {
        super();
        titleImage = new Sprite("Fantastic-4.png");
        titleImage.setPosition(GameCanvas.WIDTH / 2f, 150);
        startButton = new Button(this);
        startButton.setSize(200, 60);
        startButton.setPosition(GameCanvas.WIDTH / 2f, GameCanvas.HEIGHT / 2f + 100);
        startButton.color = new Color(40, 40, 40, 0); 
        startImage = new Sprite("START GAME.png");
        startImage.setPosition(GameCanvas.WIDTH / 2f, GameCanvas.HEIGHT / 2f + 100);
        quitButton = new Button(this);
        quitButton.setSize(200, 60);
        quitButton.setPosition(GameCanvas.WIDTH / 2f, GameCanvas.HEIGHT / 2f + 180);
        quitButton.color = new Color(40, 40, 40, 0);
        quitImage = new Sprite("QUIT GAME.png");
        quitImage.setPosition(GameCanvas.WIDTH / 2f, GameCanvas.HEIGHT / 2f + 180);
        addChildren(startButton, quitButton);
        quitButton.getClickSignal().connect(args -> {
            StateManager.getGlobalSignal().emit("quit"); 
        });
        startButton.getClickSignal().connect(args -> {
            StateManager.getGlobalSignal().emit("requestStartGame");
        });
    }
    @Override
    public void update() {
    }
    @Override
    public void render(Graphics2D g, float alpha) {
        titleImage.draw(g);
        startImage.draw(g);
        quitImage.draw(g);
    }
}