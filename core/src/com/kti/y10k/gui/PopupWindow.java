package com.kti.y10k.gui;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.kti.y10k.gui.helpers.TextButtonHelper;
import com.kti.y10k.utilities.Logger;
import com.kti.y10k.utilities.managers.WindowManager;

public class PopupWindow extends WindowWrapper {
    private String text;
    private TextButton close;
    private PopupWindow self;

    public PopupWindow(String text, float x, float y) {
        super("<!> Notification <!>", x, y, 0.1f, 0.1f);
        this.text = text;
        Logger.log(Logger.LogLevel.DEBUG, "Notification text: " + text.replace("\n", ""));

        self = this;

        close = new TextButton("Ok", TextButtonHelper.common);
        close.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                WindowManager.removePopup(self);
                self = null;
            }
        });
        WindowManager.components.addActor(close);
    }

    @Override
    public void draw(SpriteBatch b, BitmapFont f, float x, float y, float width, float height) {
        f.draw(b, text, x + 5, y + height / 2 + f.getLineHeight());
        close.setPosition(x + 5, y + 5);
        close.draw(b, 1);
    }
}
