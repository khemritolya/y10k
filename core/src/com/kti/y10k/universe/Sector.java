package com.kti.y10k.universe;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.kti.y10k.MainLoop;
import com.kti.y10k.gui.helpers.TextButtonHelper;
import com.kti.y10k.utilities.Logger;
import com.kti.y10k.utilities.managers.WindowManager;

import java.util.ArrayList;
import java.util.List;


public class Sector {
    private Sector self;

    private Vector3 position;
    private TextButton t;
    private String name;
    private float[] vertices;
    private List<Star> stars;

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

        stars = new ArrayList<>();
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

        stars = new ArrayList<>();
    }

    public void render(SpriteBatch s, Camera camera) {
        if (MainLoop.instance.listener.selected == self && Star.DEBUG_STARS_POSITIONS) {
            for (Star x:stars) x.renderColor(s, camera, Color.BLUE);
        }

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

    public void render(ShapeRenderer r) {
        float dst = position.dst(MainLoop.instance.getCamera().position);

        r.setColor(Color.GRAY.r, Color.GRAY.g, Color.GRAY.b, 1.0f < 600 / dst ?
                1.0f : Star.DEBUG_STARS_POSITIONS ? 1.0f : 600 / dst);
        r.polygon(vertices);
    }

    public void addStar(Star s) {
        stars.add(s);
    }

    public boolean atPoint(float x, float z) {
        return  (x - position.x < 0.001 && z - position.z < 0.001);
    }

    @Override
    public String toString() {
        return "Sector " + name + " " + position.x + " " + position.z;
    }

    public Vector2[] getVertices() {
        Vector2[] out = new Vector2[6];

        for (int i = 0; i < 6; i++) {
            out[i] = new Vector2(vertices[2*i], vertices[2 * i + 1]);
        }

        return out;
    }

    public String getName() {
        return name;
    }

    public int getStarCount() {
        return stars.size();
    }
}
