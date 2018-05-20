package com.kti.y10k.gui;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.kti.y10k.MainLoop;
import com.kti.y10k.gui.helpers.TextButtonHelper;
import com.kti.y10k.gui.helpers.TextFieldHelper;
import com.kti.y10k.utilities.Logger;
import com.kti.y10k.utilities.managers.WindowManager;

import static com.kti.y10k.MainLoop.WIN_WIDTH;

public class TextInputWindow extends WindowWrapper {
    private String prompt;
    private TextButton close;
    private TextInputWindow self;
    private TextField t;

    public TextInputWindow(String prompt, String defaultRes, float x, float y) {
        super(prompt, x, y, 0.1f, 0.1f);
        this.prompt = prompt;

        self = this;

        close = new TextButton("Ok", TextButtonHelper.textButtonStyle);
        close.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                submit();
            }
        });
        WindowManager.components.addActor(close);

        t = new TextField(defaultRes, TextFieldHelper.textFieldStyle);
        t.setMaxLength(15);
        t.setSize(0.08f * WIN_WIDTH, MainLoop.instance.font.getLineHeight());
        WindowManager.components.addActor(t);
    }

    @Override
    public void draw(SpriteBatch b, BitmapFont f, float x, float y, float width, float height) {
        t.setPosition(x + 5, y + height / 2);
        t.draw(b, 1f);
        close.setPosition(x + 5, y + 5);
        close.draw(b, 1);
    }

    public String getOutput() {
        return t.getText();
    }

    public void submit() {
        Logger.log(Logger.LogLevel.DEBUG, "Submit output text: " + t.getText());
        WindowManager.removeTextInput(self);
        self = null;
    }
}
