package com.kti.y10k.universe;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.kti.y10k.utilities.Logger;
import com.kti.y10k.utilities.managers.GalaxyConstManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Galaxy {
    private List<Star> stars = new ArrayList<>();
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

        // TODO generate the point plane
        // is a point inside a polygon code:
        //https://www.codeproject.com/tips/84226/is-a-point-inside-a-polygon



        Logger.log(Logger.LogLevel.DEBUG, "Done Generating in " +
                (System.currentTimeMillis() - begin) + "ms");
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
    }

    public void renderAux(ShapeRenderer r) {

    }

    public List<String> asString() {
        List<String> out = new ArrayList<>();
        for (Star s:stars) out.add(s.toString());
        return out;
    }

    public void set(List<Star> stars) {
        this.stars = stars;
    }

    public void release() {
        stars.clear();
    }
}
