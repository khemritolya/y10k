package com.kti.y10k.universe;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.kti.y10k.utilities.Logger;
import com.kti.y10k.utilities.managers.GalaxyConstManager;

import java.util.*;

public class Galaxy {
    private List<Star> stars = new ArrayList<>();
    private List<Sector> sectors = new ArrayList<>();
    private Random r;
    private float galacticRadius;

    public Galaxy() {
        Logger.log(Logger.LogLevel.INFO, "Generating a new galaxy...");
        long begin = System.currentTimeMillis();

        stars.clear();
        if (0 != GalaxyConstManager.requestConstant("use_seed"))
            r = new Random((int)GalaxyConstManager.requestConstant("seed"));
        else
            r = new Random();

        float lim = GalaxyConstManager.requestConstant("spiral_length");
        float err = GalaxyConstManager.requestConstant("spiral_radius");
        float pow = GalaxyConstManager.requestConstant("expansion_exponent");
        float fac = GalaxyConstManager.requestConstant("expansion_factor");
        float spiralDensityConstant = GalaxyConstManager.requestConstant("spiral_adj_density");
        float peter = GalaxyConstManager.requestConstant("spiral_peter");

        for (float i = 0; i < lim; i += randF() * Math.PI / spiralDensityConstant * (1 + peter * i / lim)) {
            float raderr = err * (randF()-0.5f);
            float rad = fac*(float)Math.pow(i, pow) + raderr;
            float x = -(float) (Math.cos(i) * rad);
            float y = (float) Math.sqrt(err*err - 4*raderr*raderr) * (randF() - 0.5f) *
                    (raderr < 0 ? -1 : 1) * (randF() - 0.5f < 0 ? -1 : 1);
            float z = -(float) (Math.sin(i) * rad);

            if (Math.sqrt(x*x + z*z) - 1f > 0)
                stars.add(new Star(x - err / 4, y, z,1 + randF()));
        }

        for (float i = 0; i < lim; i += randF() * Math.PI / spiralDensityConstant * (1 + peter * i / lim)) {
            float raderr = err * (randF()-0.5f);
            float rad = fac*(float)Math.pow(i, pow) + raderr;
            float x = (float) (Math.cos(i) * rad);
            float y = (float) Math.sqrt(err*err - 4*raderr*raderr) * (randF() - 0.5f) *
                    (raderr < 0 ? -1 : 1) * (randF() - 0.5f < 0 ? -1 : 1);
            float z = (float) (Math.sin(i) * rad);

            if (Math.sqrt(x*x + z*z) - 1f > 0)
                stars.add(new Star(x + err / 4, y, z,1 + randF()));
        }


        float coreDensityConstant = GalaxyConstManager.requestConstant("core_adj_density");
        for (float i = 0; i < Math.PI * 2; i+= randF() * Math.PI / coreDensityConstant) {
            float rad = err * (randF() + 0.2f);
            float x = (float) (Math.cos(i) * rad);
            float y = (float) Math.sqrt(err*err - 3*rad*rad) * (randF() - 0.5f) *
                    (rad < 0 ? -1 : 1) * (randF() - 0.5f < 0 ? -1 : 1);
            float z = (float) (Math.sin(i) * rad);

            stars.add(new Star(2*x, 2*y, 2*z,1 + randF()));
        }

        float regularDensityConstant = GalaxyConstManager.requestConstant("regular_adj_density");
        galacticRadius = GalaxyConstManager.requestConstant("galaxy_radius");
        galacticRadius *= Math.sqrt(
                GalaxyConstManager.requestConstant("x_squish") *
                GalaxyConstManager.requestConstant("z_squish")
        );

        for (float i = 0; i < lim; i += randF() * Math.PI / regularDensityConstant) {
            float rad = galacticRadius / 2 * (randF2() + 0.1f);
            float x = (float) (Math.cos(i) * rad);
            float y = 0;
            float z = (float) (Math.sin(i) * rad);

            stars.add(new Star(x, y + err * 6 * (randF() - 0.5f) * (randF() - 0.5f),
                    z, 1 + randF()));
        }

        List<Star> toRemove = new ArrayList<>();
        for (Star s:stars) if (!s.verify()) toRemove.add(s);
        for (Star s:toRemove) stars.remove(s);

        sectors.clear();

        List<Vector2> pos = new ArrayList<>();
        float sq3 = (float)Math.sqrt(3);

        pos.add(new Vector2(0,0));

        float iter = GalaxyConstManager.requestConstant("sector_loops");
        for (int i = 1; i < iter; i++) {
            float x = 0;
            float y = i * sq3;

            pos.add(new Vector2(x,y));

            for (int j = 0; j < i; j++) {
                x -= 1.5;
                y -= sq3 / 2;
                pos.add(new Vector2(x,y));
            }

            for (int j = 0; j < i; j++) {
                y -= sq3;
                pos.add(new Vector2(x,y));
            }

            for (int j = 0; j < i; j++) {
                x += 1.5;
                y -= sq3 / 2;
                pos.add(new Vector2(x,y));
            }

            for (int j = 0; j < i; j++) {
                x += 1.5;
                y += sq3 / 2;
                pos.add(new Vector2(x,y));
            }

            for (int j = 0; j < i; j++) {
                y += sq3;
                pos.add(new Vector2(x,y));
            }

            for (int j = 0; j < i - 1; j++) {
                y += sq3 / 2;
                x -= 1.5;
                pos.add(new Vector2(x,y));
            }
        }

        float conv_fac = GalaxyConstManager.requestConstant("sector_to_galrad");

        for (Vector2 v:pos) {
            float[] vertices = {
                    v.x - 0.5f, v.y + sq3 /2,
                    v.x - 1, v.y,
                    v.x - 0.5f, v.y - sq3 / 2,
                    v.x + 0.5f, v.y - sq3 / 2,
                    v.x + 1, v.y,
                    v.x + 0.5f, v.y + sq3 / 2
            };

            for (int i = 0; i < vertices.length; i++) vertices[i] *= galacticRadius / conv_fac;

            sectors.add(new Sector(v.x * galacticRadius / conv_fac,
                    v.y * galacticRadius / conv_fac, vertices));
        }

        fit();

        Logger.log(Logger.LogLevel.DEBUG, "Done Generating in " +
                (System.currentTimeMillis() - begin) + "ms");
    }

    private boolean contains(Star test, Sector x) {
        Vector2[] vertx = x.getVertices();
        int i, j;
        boolean result = false;
        for (i = 0, j = vertx.length - 1; i < vertx.length; j = i++) {
            if ((vertx[i].y > test.pos().z) != (vertx[j].y > test.pos().z) &&
                    (test.pos().x < (vertx[j].x - vertx[i].x) * (test.pos().z - vertx[i].y) /
                            (vertx[j].y -vertx[i].y) + vertx[i].x)) {
                result = !result;
            }
        }
        return result;
    }

    public int size() {
        return stars.size();
    }

    private float randF() {
        return r.nextFloat();
    }

    private float randF2() { return (float) (r.nextGaussian() + 1) / 2; }

    public void renderStars(SpriteBatch batch, Camera c) {
        for (Star s:stars) s.render(batch, c);
        for (Sector s:sectors) s.render(batch, c);
    }

    public void renderAux(ShapeRenderer r) {
        for (Sector s:sectors) s.render(r);
    }

    public Sector getAt(float x, float z) {
        for (Sector s:sectors) {
            if (s.atPoint(x, z)) return s;
        }

        return null;
    }

    private void fit() {
        for (Star s:stars) {
            for (Sector x:sectors) {
                if (contains(s, x)) {
                    x.addStar(s);
                }
            }
        }
    }

    public List<String> asString() {
        List<String> out = new ArrayList<>();
        for (Star s:stars) out.add(s.toString());
        for (Sector s:sectors) out.add(s.toString());
        return out;
    }

    public void set(List<Star> stars, List<Sector> sectors) {
        this.stars = stars;
        this.sectors = sectors;

        fit();
    }

    public void release() {
        stars.clear();
    }
}
