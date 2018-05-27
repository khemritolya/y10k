package com.kti.y10k.gui;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.kti.y10k.MainLoop;

public class SectorInfoWindow extends WindowWrapper {
    public SectorInfoWindow() {
        super("Sector Info", 0.75f, 0.65f, 0.2f, 0.3f);
    }

    @Override
    public void draw(SpriteBatch b, BitmapFont f, float x, float y, float width, float height) {
        if (isVisible()) {
            setTitle("Sector Info: " + MainLoop.instance.listener.selected.getName());
            f.draw(b, "[Sector info here]", x + 5, y + height / 2);
        }
    }
}
