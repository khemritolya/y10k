package com.kti.y10k.gui;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.kti.y10k.gui.helpers.TextButtonHelper;
import com.kti.y10k.utilities.managers.WindowManager;

public class CreditWindow extends WindowWrapper {
    private TextButton close;

    public CreditWindow() {
        super("Credits",0.13f, 0.4f,0.10f,0.17f);

        close = new TextButton("Close", TextButtonHelper.textButtonStyle);
        close.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                WindowManager.creditWindow.setVisible(false);
            }
        });

        WindowManager.components.addActor(close);
    }

    @Override
    public void draw(SpriteBatch uiBatch, BitmapFont font, float x, float y, float width, float height) {
        font.draw(uiBatch, "Programming: ", x + 5, y + width / 2 + 6 * font.getLineHeight() / 2);
        font.draw(uiBatch, "Khemri Tolya", x  + 5, y + width / 2 + 4 * font.getLineHeight() / 2);
        font.draw(uiBatch, "Mr. Cole", x + 5, y + width / 2 + font.getLineHeight());
        font.draw(uiBatch, "Mr. Sours", x + 5, y + width / 2);
        font.draw(uiBatch, "Audio: ", x + 5, y + width / 2 - 2 * font.getLineHeight());
        font.draw(uiBatch, "Ben Lepper", x + 5, y + width / 2 - 3 * font.getLineHeight());
        close.setPosition(x + 5, y + 5);
        close.draw(uiBatch, 1);
    }
}
