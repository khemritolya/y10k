package com.kti.y10k.gui.helpers;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.kti.y10k.MainLoop;
import com.kti.y10k.utilities.managers.AssetManager;

public class TextFieldHelper {
    public static TextField.TextFieldStyle textFieldStyle;

    public static void init() {
        textFieldStyle = new TextField.TextFieldStyle();
        textFieldStyle.font = MainLoop.instance.font;
        textFieldStyle.fontColor = Color.WHITE;
        textFieldStyle.cursor = new TextureRegionDrawable(
                new TextureRegion(AssetManager.requestTexture("cursor")));
        textFieldStyle.cursor.setMinWidth(MainLoop.instance.font.getSpaceWidth() / 1.2f);
        textFieldStyle.background = new TextureRegionDrawable(new TextureRegion(AssetManager.requestTexture("win")));
    }
}
