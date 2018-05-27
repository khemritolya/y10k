package com.kti.y10k.gui;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.kti.y10k.MainLoop;
import com.kti.y10k.universe.Sector;

public class SectorInfoWindow extends WindowWrapper {
    public SectorInfoWindow() {
        super("Sector Info", 0.75f, 0.65f, 0.2f, 0.3f);
    }

    @Override
    public void draw(SpriteBatch b, BitmapFont f, float x, float y, float width, float height) {
        if (isVisible()) {
            Sector s = MainLoop.instance.listener.selected;
            setTitle("Sector Info: " + s.getName());
            f.draw(b, "Stars: " + s.getStarCount(), x + 5, y + height - f.getLineHeight() - 5);
            f.draw(b, "[Sector info here]", x + 5, y + height / 2);
        }
    }
}
