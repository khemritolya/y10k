package com.kti.y10k.control;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector3;
import com.kti.y10k.utilities.Logger;
import com.kti.y10k.MainLoop;
import com.kti.y10k.utilities.managers.WindowManager;


import java.io.File;
import java.io.IOException;
import java.util.*;

import static com.badlogic.gdx.Input.Keys.*;

public class Listener {
    private boolean rightDown = false;
    private boolean leftDown = false;

    private HashMap<String, Integer> keyMap;
    private HashMap<String, InputAction> actionMap;
    private HashMap<Integer, InputAction> callbackMap;
    private HashMap<String, Integer> actionKeycodeMap;
    private Integer[] callbacks;

    public Vector3 rotateAround;

    public Listener() {
        keyMap = new HashMap<>();
        initKeyMap();

        actionMap = new HashMap<>();
        actionMap.put("mainmenu", new MainMenu());
        actionMap.put("zoom-in", new ZoomIn());
        actionMap.put("zoom-out", new ZoomOut());
        actionMap.put("close", new Close());
        actionMap.put("close_dialog", new CloseDialog());
        actionMap.put("forward", new Forward());
        actionMap.put("backward", new Backward());
        actionMap.put("right", new Right());
        actionMap.put("left", new Left());


        callbackMap = new HashMap<>();
        actionKeycodeMap = new HashMap<>();

        rotateAround = new Vector3(0,0,0);

        try {
            File f = Gdx.files.absolute("keybinds.txt").file();
            if (!f.exists())
                throw new IOException();

            Scanner scanner = new Scanner(f);
            String line;

            while (scanner.hasNextLine()) {
                line = scanner.nextLine();

                if (line.length() != 0 && line.charAt(0) != '#') {
                    StringTokenizer st = new StringTokenizer(line);
                    Integer keycode = keyMap.get(st.nextToken().toLowerCase());
                    String s = st.nextToken().toLowerCase();
                    InputAction action = actionMap.get(s);

                    if (keycode != null && action != null) {
                        callbackMap.put(keycode, action);
                        actionKeycodeMap.put(s, keycode);
                        Logger.log(Logger.LogLevel.DEBUG, "Added Keybind line: " + line);
                    } else {
                        Logger.log(Logger.LogLevel.WARN, "Unknown Keybind line: " + line);
                    }
                }
            }

            callbacks = callbackMap.keySet().toArray(new Integer[0]);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Unable to parse keybinds");
        }
        Logger.log(Logger.LogLevel.INFO, "Done Loading Keybinds.");
    }

    public void listen() {
        Camera camera = MainLoop.instance.getCamera();

        if (MainLoop.instance.inMenu) {
            camera.rotateAround(rotateAround, Vector3.Y, -2.5f * MainLoop.instance.dT);
        }

        if (!MainLoop.instance.inPrompt) {
            for (int i = 0; i < callbacks.length; i++) {
                if (Gdx.input.isKeyPressed(callbacks[i]) && !callbackMap.get(callbacks[i]).isLongDown()) {
                    callbackMap.get(callbacks[i]).exec();
                } else if (Gdx.input.isKeyJustPressed(callbacks[i]) && callbackMap.get(callbacks[i]).isLongDown()) {
                    callbackMap.get(callbacks[i]).exec();
                }
            }
        } else {
            if (Gdx.input.isKeyJustPressed(actionKeycodeMap.get("close_dialog"))) {
                WindowManager.submitTextInput();
            }
        }

        if (!MainLoop.instance.inMenu && Gdx.input.isButtonPressed(Input.Buttons.RIGHT)) {
            Gdx.input.setCursorCatched(true);

            if (rightDown) {
                int y = Gdx.input.getY() - Gdx.graphics.getHeight() / 2;
                int x = Gdx.input.getX() - Gdx.graphics.getWidth() / 2;

                Vector3 right = new Vector3(camera.direction).crs(camera.up).nor();
                camera.rotateAround(rotateAround, right, y / 5);
                camera.rotateAround(rotateAround, camera.up, x / 5);
            }

            Gdx.input.setCursorPosition(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
            rightDown = true;
        }

        if (!MainLoop.instance.inMenu && rightDown && !Gdx.input.isButtonPressed(Input.Buttons.RIGHT)) {
            Gdx.input.setCursorCatched(false);
            rightDown = false;
        }
    }

    private void initKeyMap() {
        keyMap.put("esc", ESCAPE);
        keyMap.put("enter", ENTER);
        keyMap.put("delete", DEL);
        keyMap.put("backspace", BACKSPACE);

        keyMap.put("right-ctrl", CONTROL_RIGHT);
        keyMap.put("left-ctrl", CONTROL_LEFT);
        keyMap.put("ctrl", CONTROL_LEFT);

        keyMap.put("left-shift", SHIFT_LEFT);
        keyMap.put("right-shift", SHIFT_RIGHT);
        keyMap.put("shift", SHIFT_LEFT);

        keyMap.put("tab", TAB);

        keyMap.put("arrow-up", UP);
        keyMap.put("arrow-down", DOWN);
        keyMap.put("arrow-left", LEFT);
        keyMap.put("arrow-right", RIGHT);

        keyMap.put("0", NUM_0);
        keyMap.put("1", NUM_1);
        keyMap.put("2", NUM_2);
        keyMap.put("3", NUM_3);
        keyMap.put("4", NUM_4);
        keyMap.put("5", NUM_5);
        keyMap.put("6", NUM_6);
        keyMap.put("7", NUM_7);
        keyMap.put("8", NUM_8);
        keyMap.put("9", NUM_9);

        keyMap.put("a", A);
        keyMap.put("b", B);
        keyMap.put("c", C);
        keyMap.put("d", D);
        keyMap.put("e", E);
        keyMap.put("f", F);
        keyMap.put("g", G);
        keyMap.put("h", H);
        keyMap.put("i", I);
        keyMap.put("j", J);
        keyMap.put("k", K);
        keyMap.put("l", L);
        keyMap.put("m", M);
        keyMap.put("n", N);
        keyMap.put("o", O);
        keyMap.put("p", P);
        keyMap.put("q", Q);
        keyMap.put("r", R);
        keyMap.put("s", S);
        keyMap.put("t", T);
        keyMap.put("u", U);
        keyMap.put("v", V);
        keyMap.put("w", W);
        keyMap.put("x", X);
        keyMap.put("y", Y);
        keyMap.put("z", Z);
    }
}
