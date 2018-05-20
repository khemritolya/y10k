package com.kti.y10k.gui.helpers;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.kti.y10k.MainLoop;

public class TextButtonHelper {
    public static TextButton.TextButtonStyle textButtonStyle;

    public static void init() {
        textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = MainLoop.instance.font;
        textButtonStyle.fontColor = Color.WHITE;
        textButtonStyle.downFontColor = Color.LIGHT_GRAY;
    }
}
