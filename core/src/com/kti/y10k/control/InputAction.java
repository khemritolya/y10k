package com.kti.y10k.control;

import com.badlogic.gdx.math.Vector3;
import com.kti.y10k.MainLoop;
import com.kti.y10k.utilities.managers.WindowManager;

import java.util.ArrayList;
import java.util.List;

public abstract class InputAction {
    public abstract void exec();
    public abstract boolean isLongDown();
}

class ZoomIn extends InputAction {
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

class ZoomOut extends InputAction {
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

                MainLoop.instance.getCamera().position.set(400, 651, 0);
                MainLoop.instance.getCamera().lookAt(Vector3.Zero);
                MainLoop.instance.getCamera().position.add(-200, 0,0);
                MainLoop.instance.getCamera().up.set(Vector3.Y);
                MainLoop.instance.listener.rotateAround = Vector3.Zero;
                MainLoop.instance.listener.selected = MainLoop.instance.c.getAt(0,0);
            }
        } else {
            MainLoop.instance.inMenu = true;
            List<Vector3> posStart = new ArrayList<>();
            posStart.add(new Vector3(-30, 651, 30));
            posStart.add(new Vector3(-467, 84, 464));
            posStart.add(new Vector3(0, 0, 1));
            posStart.add(new Vector3(1102, 0, -1592));
            posStart.add(new Vector3(-6094, 4356, 415));

            MainLoop.instance.getCamera().position.set(
                    posStart.get((int) (Math.random() * posStart.size())));
            MainLoop.instance.getCamera().lookAt(Vector3.Zero);
            MainLoop.instance.listener.rotateAround = Vector3.Zero;
            MainLoop.instance.listener.selected = null;
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

class Forward extends InputAction {
    @Override
    public void exec() {
        if (!MainLoop.instance.inMenu) {
            MainLoop.instance.getCamera().position.add(
                    -400 * MainLoop.instance.dT,0,0);
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
                    400 * MainLoop.instance.dT,0,0);
        }
    }

    @Override
    public boolean isLongDown() {return false;}
}

class Right extends InputAction {
    @Override
    public void exec() {
        if (!MainLoop.instance.inMenu) {
            MainLoop.instance.getCamera().position.add(
                    0,0, -400 * MainLoop.instance.dT);
        }
    }

    @Override
    public boolean isLongDown() {return false;}
}

class Left extends InputAction {
    @Override
    public void exec() {
        if (!MainLoop.instance.inMenu) {
            MainLoop.instance.getCamera().position.add(
                    0,0, 400 * MainLoop.instance.dT);
        }
    }

    @Override
    public boolean isLongDown() {return false;}
}