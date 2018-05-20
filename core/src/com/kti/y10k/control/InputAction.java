package com.kti.y10k.control;

import com.kti.y10k.MainLoop;
import com.kti.y10k.utilities.managers.WindowManager;

public abstract class InputAction {
    public abstract void exec();
    public abstract boolean isLongDown();
}

class Forward extends InputAction {
    @Override
    public void exec() {
        if (!MainLoop.instance.inMenu && MainLoop.instance.getCamera().position.dst(
                MainLoop.instance.listener.rotateAround) > 1f) {
            MainLoop.instance.getCamera().position.add(
                    (MainLoop.instance.getCamera().position.x - MainLoop.instance.listener.rotateAround.x) /
                            -0.66f * MainLoop.instance.dT,
                    (MainLoop.instance.getCamera().position.y - MainLoop.instance.listener.rotateAround.y) /
                            -0.66f * MainLoop.instance.dT,
                    (MainLoop.instance.getCamera().position.z - MainLoop.instance.listener.rotateAround.z) /
                            -0.66f * MainLoop.instance.dT
            );
        }
    }

    @Override
    public boolean isLongDown() {return false;}
}

class Backward extends InputAction {
    @Override
    public void exec() {
        if (!MainLoop.instance.inMenu) {
            MainLoop.instance.getCamera().position.add(
                    (MainLoop.instance.getCamera().position.x - MainLoop.instance.listener.rotateAround.x) /
                            0.66f * MainLoop.instance.dT,
                    (MainLoop.instance.getCamera().position.y - MainLoop.instance.listener.rotateAround.y) /
                            0.66f * MainLoop.instance.dT,
                    (MainLoop.instance.getCamera().position.z - MainLoop.instance.listener.rotateAround.z) /
                            0.66f * MainLoop.instance.dT
            );
        }
    }

    @Override
    public boolean isLongDown() {return false;}
}

class MainMenu extends InputAction {
    @Override
    public void exec() {
        if (MainLoop.instance.inMenu) {
            if (MainLoop.instance.started) {
                MainLoop.instance.inMenu = false;
            }
        } else {
            MainLoop.instance.inMenu = true;
        }
    }

    @Override
    public boolean isLongDown() {return true;}
}

class Close extends InputAction {
    @Override
    public void exec() {
        MainLoop.instance.dispose();
        System.exit(0);
    }

    @Override
    public boolean isLongDown() {return false;}
}

class CloseDialog extends InputAction {
    @Override
    public void exec() {
        WindowManager.removeLastPopup();
    }

    @Override
    public boolean isLongDown() {return true;}
}