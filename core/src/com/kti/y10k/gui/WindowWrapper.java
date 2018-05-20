package com.kti.y10k.gui;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.kti.y10k.MainLoop;
import com.kti.y10k.utilities.Logger;
import com.kti.y10k.utilities.managers.AssetManager;
import com.kti.y10k.utilities.managers.WindowManager;

import static com.kti.y10k.MainLoop.WIN_HEIGHT;
import static com.kti.y10k.MainLoop.WIN_WIDTH;

public abstract class WindowWrapper {
    private static Window.WindowStyle windowStyle;

    public static void init() {
        windowStyle = new Window.WindowStyle();
        windowStyle.background = new TextureRegionDrawable(new TextureRegion(AssetManager.requestTexture("win")));
        windowStyle.titleFont = MainLoop.instance.font;
    }

    protected Window w;
    private String title;
    private boolean isVisible;

    public WindowWrapper(String title, float x, float y, float width, float height) {
        Logger.log(Logger.LogLevel.INFO, "Creating a window called \"" + title + "\"");
        w = new Window("", windowStyle);
        this.title = title;
        w.setResizable(false);
        w.setMovable(true);
        w.setKeepWithinStage(true);
        w.setPosition(x * WIN_WIDTH, y * WIN_HEIGHT);
        w.setSize(width * WIN_WIDTH, height * WIN_HEIGHT);
        w.addListener((new DragListener() {
            public void touchDragged (InputEvent event, float x, float y, int pointer) {
                w.setPosition(w.getX() + x - w.getWidth() / 2, w.getY() + y - w.getHeight() / 2);
            }

        }));
        WindowManager.windows.addActor(w);
        isVisible = true;
    }

    public WindowWrapper(String title, float x, float y, float width, float height, boolean isVisible) {
        this(title, x, y, width, height);
        this.isVisible = isVisible;
    }

    public void render(SpriteBatch b, BitmapFont f) {
        if (isVisible) {
            w.draw(b, 0.8f);
            f.draw(b, title, w.getX() + 5, w.getY() + w.getHeight() - 5);
            draw(b, f, w.getX(), w.getY(), w.getWidth(), w.getHeight());
        }
    }

    public abstract void draw(SpriteBatch b, BitmapFont f, float x, float y, float width, float height);

    public void setVisible(boolean visible) {
        this.isVisible = visible;
    }
}
