package jaredbgreat.dldungeons.util.math;

import java.util.Random;

public class Vec3D {
    double x, y, z;
    
    public Vec3D(double x, double y,double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    public Vec3D(Random rand) {
        x = rand.nextDouble() * 2.0 - 1.0;
        y = rand.nextDouble() * 2.0 - 1.0;
        z = rand.nextDouble() * 2.0 - 1.0;
    }
    
    public Vec3D(SpatialHash random, int px, int py, int pz, int pt) {
        x = random.doubleFor(px, py, pz, pt) * 2.0 - 1.0;
        y = random.doubleFor(px, py, pz, pt + 1) * 2.0 - 1.0;
        z = random.doubleFor(px, py, pz, pt + 2) * 2.0 - 1.0;
    }
    
    public static double dot(Vec3D a, Vec3D b) {
        return (a.x * b.x) + (a.y * b.y) + (a.z * b.z);
    } 
    
}
