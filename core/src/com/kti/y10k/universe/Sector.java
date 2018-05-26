package com.kti.y10k.universe;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.kti.y10k.MainLoop;


public class Sector {
    private Vector3 position;
    private float[] vertices;

    public Sector(float x, float z, float[] vertices) {
        position = new Vector3(x, 0, z);
        this.vertices = vertices;
    }


    public void draw(SpriteBatch s, Camera c) {

    }

    public void draw(ShapeRenderer r) {
        float dst = position.dst(MainLoop.instance.getCamera().position);

        r.setColor(Color.GRAY.r, Color.GRAY.g, Color.GRAY.b, 1.0f < 600 / dst ?
                1.0f : Star.DEBUG_STARS_POSITIONS ? 1.0f : 600 / dst);
        r.polygon(vertices);
    }
}
