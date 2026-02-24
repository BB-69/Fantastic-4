package game.nodes.ui.menu;

import java.awt.Color;
import java.awt.Graphics2D;

import game.GameCanvas;
import game.core.StateManager;
import game.core.graphics.Sprite;
import game.core.node.Node;
import game.core.node.event.Button;
import game.core.signal.CanConnectSignal;
import game.core.signal.SignedSignal;
import game.util.Log;
import game.util.Time;
import game.util.calc.MathUtil;

public class MenuUIManager extends Node implements CanConnectSignal {

    private Button startButton;
    private Button quitButton;
    private Sprite titleImage;
    private Sprite startImage;
    private Sprite quitImage;

    private final int btnWidth = 200;
    private final int btnHeight = 60;
    private float startScale = 1f;
    private float startTargetScale = 1f;
    private float quitScale = 1f;
    private float quitTargetScale = 1f;

    private SignedSignal globalSignal;
    private final MenuUIManager Instance = this;
    private boolean uiInit = false;

    public MenuUIManager() {
        super();
        this.globalSignal = StateManager.getGlobalSignal();
        globalSignal.connect(Instance, Instance::onGlobalSignal);
        titleImage = new Sprite("Fantastic-4.png");
        titleImage.setPosition(GameCanvas.WIDTH / 2f, 150);
        startButton = new Button(this);
        startButton.setSize(btnWidth, btnHeight);
        startButton.setPosition(GameCanvas.WIDTH / 2f, GameCanvas.HEIGHT / 2f + 40);
        startButton.color = new Color(40, 40, 40, 0);
        startImage = new Sprite("START GAME.png");
        startImage.setPosition(GameCanvas.WIDTH / 2f, GameCanvas.HEIGHT / 2f + 40);
        quitButton = new Button(this);
        quitButton.setSize(btnWidth, btnHeight);
        quitButton.setPosition(GameCanvas.WIDTH / 2f, GameCanvas.HEIGHT / 2f + 180);
        quitButton.color = new Color(40, 40, 40, 0);
        quitImage = new Sprite("QUIT GAME.png");
        quitImage.setPosition(GameCanvas.WIDTH / 2f, GameCanvas.HEIGHT / 2f + 180);
        addChildren(startButton, quitButton);
        quitButton.getClickSignal().connect(this::onQuitButtonClicked);
        startButton.getClickSignal().connect(this::onStartButtonClicked);
    }

    @Override
    public void update() {
        startTargetScale = startButton.isHovered() ? 1.2f : 1;
        quitTargetScale = quitButton.isHovered() ? 1.2f : 1;

        startScale = MathUtil.lerp(startScale, startTargetScale, 15f * Time.deltaTime);
        quitScale = MathUtil.lerp(quitScale, quitTargetScale, 15f * Time.deltaTime);

        startImage.setSize(btnWidth * startScale, btnHeight * startScale);
        quitImage.setSize(btnWidth * quitScale, btnHeight * quitScale);
    }

    @Override
    public void render(Graphics2D g, float alpha) {
        titleImage.draw(g);
        startImage.draw(g);
        quitImage.draw(g);
    }

    private void initUI() {
        if (!uiInit) {
            uiInit = true;

            // basic init behaviour: reset scales and optionally run intro animations
            startScale = 1f;
            quitScale = 1f;
        }
    }

    private void onRestartReload(Object... args) {
        uiInit = false;
        startScale = 1f;
        quitScale = 1f;
    }

    private void onTransitionDone(Object... args) {
        boolean isEnter = ((String) args[0]).equals("enter");

        if (!isEnter)
            initUI();
    }

    private void onGlobalSignal(String signalName, Object... args) {
        switch (signalName) {
            case "restartReload":
                onRestartReload(args);
                break;
            case "transitionDone":
                onTransitionDone(args);
                break;
            default:
        }
    }

    private void onStartButtonClicked(Object... args) {
        Log.logInfo("Starting Gameplay...");
        StateManager.getGlobalSignal().emit("requestStartGame");
    }

    private void onQuitButtonClicked(Object... args) {
        Log.logInfo("Quitting Game...");
        StateManager.getGlobalSignal().emit("quit");
    }

    @Override
    public void disconnectSignals() {
        globalSignal.disconnect(Instance);
    }

    public void reset() {
        uiInit = false;
        startScale = 1f;
        startTargetScale = 1f;
        quitScale = 1f;
        quitTargetScale = 1f;
    }

    @Override
    public void destroy() {
        super.destroy();
        disconnectSignals();
    }
}