package com.kti.y10k.utilities.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.kti.y10k.MainLoop;
import com.kti.y10k.gui.*;
import com.kti.y10k.gui.helpers.TextButtonHelper;
import com.kti.y10k.gui.helpers.TextFieldHelper;
import com.kti.y10k.utilities.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class WindowManager {
    public static MainMenuWindow mainMenuWindow;
    public static CreditWindow creditWindow;
    public static SectorInfoWindow sectorInfoWindow;
    private static HashMap<Integer, TextInputWindow> tiwin;
    private static HashMap<TextInputWindow, Integer> tiwinRev;
    private static HashMap<Integer, Boolean> hasReturned;
    private static HashMap<Integer, String> output;
    private static int cmax = 0;
    private static List<PopupWindow> popwin;

    public static Stage s;
    public static Group windows;
    public static Group components;

    public static void init() {
        s = new Stage();
        Gdx.input.setInputProcessor(s);

        windows = new Group();
        components = new Group();

        s.addActor(windows);
        s.addActor(components);

        TextButtonHelper.init();
        TextFieldHelper.init();

        WindowWrapper.init();

        mainMenuWindow = new MainMenuWindow();
        creditWindow = new CreditWindow();
        sectorInfoWindow = new SectorInfoWindow();

        MainLoop.instance.inMenu = true;
        mainMenuWindow.setVisible(true);
        creditWindow.setVisible(true);

        popwin = new ArrayList<>();
        tiwin = new HashMap<>();
        tiwinRev = new HashMap<>();
        hasReturned = new HashMap<>();
        output = new HashMap<>();
    }

    public static void newPopup(String text, float x, float y) {
        popwin.add(new PopupWindow(text, x, y));
    }

    public static void removePopup(PopupWindow p) {
        popwin.remove(p);
    }

    public static void removeLastPopup() {
        try {
        popwin.remove(popwin.size() - 1);
        } catch (Exception e) {
            Logger.log(e.getStackTrace());
        }
    }

    public static Integer newTextInput(String prompt, String defaultRes, float x, float y) {
        Integer id = cmax;
        TextInputWindow t = new TextInputWindow(prompt, defaultRes, x, y);
        cmax++;
        tiwin.put(id, t);
        tiwinRev.put(t, id);
        return id;
    }

    public static void removeTextInput(TextInputWindow t) {
        Integer id = tiwinRev.get(t);
        output.put(id, t.getOutput());
        hasReturned.put(id, true);
        tiwin.remove(id, t);
        tiwinRev.remove(t, id);
    }

    public static boolean hasOutput(Integer id) {
        return (hasReturned.get(id) != null);
    }

    public static String requestOutput(Integer id) {
        return output.get(id);
    }

    public static void submitTextInput() {
        tiwin.get(tiwin.keySet().toArray()[tiwin.keySet().size() - 1]).submit();
    }

    public static void render(SpriteBatch uiBatch, BitmapFont font) {
        if (MainLoop.instance.inMenu) {
            mainMenuWindow.setVisible(true);
            sectorInfoWindow.setVisible(false);
        } else {
            sectorInfoWindow.setVisible(true);
            mainMenuWindow.setVisible(false);
            creditWindow.setVisible(false);
        }

        sectorInfoWindow.render(uiBatch, font);
        mainMenuWindow.render(uiBatch, font);
        creditWindow.render(uiBatch, font);

        for (PopupWindow p: popwin) {
            p.render(uiBatch, font);
        }

        if (tiwin.size() != 0) MainLoop.instance.inPrompt = true;
        else MainLoop.instance.inPrompt = false;

        for (Integer w: tiwin.keySet()) {
            tiwin.get(w).render(uiBatch, font);
        }
    }
}
