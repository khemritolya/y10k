package com.kti.y10k.universe;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.kti.y10k.MainLoop;
import com.kti.y10k.gui.helpers.TextButtonHelper;
import com.kti.y10k.utilities.Logger;
import com.kti.y10k.utilities.managers.WindowManager;


public class Sector {
    private Sector self;

    private Vector3 position;
    private TextButton t;
    private String name;
    private float[] vertices;

    private static Vector3 use0 = new Vector3(0,0,0);
    private static Vector3 use1 = new Vector3(0,0,0);

    public Sector(float x, float z, float[] vertices) {
        self = this;

        position = new Vector3(x, 0, z);
        this.vertices = vertices;

        name = MainLoop.instance.nm.generateNew();

        t = new TextButton(name, TextButtonHelper.alternate);
        t.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                MainLoop.instance.listener.rotateAround = position;
                MainLoop.instance.getCamera().position.set(400 + position.x, 651, position.z);
                MainLoop.instance.getCamera().lookAt(position);
                MainLoop.instance.getCamera().position.add(-200, 0,0);
                MainLoop.instance.getCamera().up.set(Vector3.Y);
                MainLoop.instance.listener.selected = self;

                Logger.log(Logger.LogLevel.DEBUG, "Clicked on Sector " + name);
            }
        });

        WindowManager.components.addActor(t);
    }

    public Sector(String name, float x, float z, float[] vertices) {
        self = this;

        position = new Vector3(x, 0, z);
        this.vertices = vertices;
        this.name = name;

        t = new TextButton(name, TextButtonHelper.alternate);
        t.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                MainLoop.instance.listener.rotateAround = position;
                MainLoop.instance.getCamera().position.set(400 + position.x, 651, position.z);
                MainLoop.instance.getCamera().lookAt(position);
                MainLoop.instance.getCamera().position.add(-200, 0,0);
                MainLoop.instance.getCamera().up.set(Vector3.Y);
                MainLoop.instance.listener.selected = self;
            }
        });

        WindowManager.components.addActor(t);
    }


    public void draw(SpriteBatch s, Camera camera) {
        if (!MainLoop.instance.inMenu) {
            use0.x = camera.position.x + camera.direction.x;
            use0.y = camera.position.y + camera.direction.y;
            use0.z = camera.position.z + camera.direction.z;

            if (camera.position.dst2(position) < 3000000 &&
                    use0.dst2(position) < camera.position.dst2(position)) {
                use1.x = position.x;
                use1.y = position.y;
                use1.z = position.z;
                camera.project(use1);

                t.setPosition(use1.x - t.getWidth() / 2, use1.y - t.getHeight() / 2);
                t.draw(s, 1f);
            }
        }
    }

    public void draw(ShapeRenderer r) {
        float dst = position.dst(MainLoop.instance.getCamera().position);

        r.setColor(Color.GRAY.r, Color.GRAY.g, Color.GRAY.b, 1.0f < 600 / dst ?
                1.0f : Star.DEBUG_STARS_POSITIONS ? 1.0f : 600 / dst);
        r.polygon(vertices);
    }

    @Override
    public String toString() {
        return "Sector " + name + " " + position.x + " " + position.z;
    }
}
