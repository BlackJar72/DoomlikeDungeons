package jaredbgreat.dldungeons.util.math;

import java.util.Random;


/**
 * A very simple, scaled down 2D vector class, used by HeightNoise to 
 * represent gradients.
 * 
 * @author Jared Blackburn
 */
public class Vec2D {
    private static final double P2 = Math.PI * 2.0;
    double x, y;
    
    public Vec2D(double x, double y) {
        this.x = x;
        this.y = y;
    }
    
    public Vec2D(Random rand) {
        x = rand.nextDouble() * 2.0 - 1.0;
        y = rand.nextDouble() * 2.0 - 1.0;
    }
    
    public Vec2D(SpatialHash random, int px, int py, int pz) {
        x = random.doubleFor(px, py, pz) * 2.0 - 1.0;
        y = random.doubleFor(px, py, pz + 1) * 2.0 - 1.0;
    }
    
    public static double dot(Vec2D a, Vec2D b) {
        return (a.x * b.x) + (a.y * b.y);
    }
    
}
