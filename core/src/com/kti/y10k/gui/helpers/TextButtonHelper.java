package com.kti.y10k.gui.helpers;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.kti.y10k.MainLoop;

public class TextButtonHelper {
    public static TextButton.TextButtonStyle common;
    public static TextButton.TextButtonStyle alternate;

    public static void init() {
        common = new TextButton.TextButtonStyle();
        common.font = MainLoop.instance.font;
        common.fontColor = Color.WHITE;
        common.downFontColor = Color.LIGHT_GRAY;

        alternate = new TextButton.TextButtonStyle();
        alternate.font = MainLoop.instance.font;
        alternate.fontColor = Color.GOLD;
        alternate.downFontColor = Color.GOLDENROD;
    }
}
