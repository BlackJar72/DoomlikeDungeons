package jaredbgreat.dldungeons.util.math;

public class ModMath {
    
	/**
	 * This is will produce an always positive modulus, 
	 * that is, a remainder from the next lower number
	 * even when negative.  Many situations require this, 
	 * such as when locating a value in a 2D grid stored 
	 * as a 1D array.
	 * 
	 * @param a the dividend
	 * @param b the divisor
	 * @return the positive modulus
	 */
    public static int modRight(int a, int b) {
    	return (a & 0x7fffffff) % b;
    }
    
    
    /**
     * A convenience method, but one probably better coded locally in 
     * most situations for efficiency (at least in intended uses).  In 
     * some ways this is a reminder, but could be handy in non-performance 
     * critical code.
     * 
     * n is the number being converted to an asymptopic form.
     * start is the place where the output should start to curve.
     * rate is the reciprical of the value it should approach minus the start.
     * 
     * @param n
     * @param start
     * @param rate 
     * @return
     */
    public static float asymptote(float n, float start, float rate) {
    	if(n > start)
    	return start + (rate / (n - start + 1));
    	return n;
    }
    
    
    /**
     * A convenience method, but one probably better coded locally in 
     * most situations for efficiency (at least in intended uses).  In 
     * some ways this is a reminder, but could be handy in non-performance 
     * critical code.
     * 
     * n is the number being converted to an asymptopic form.
     * start is the place where the output should start to curve.
     * rate is the reciprical of the value it should approach minus the start.
     * 
     * @param n
     * @param start
     * @param rate 
     * @return
     */
    public static double asymptote(double n, double start, double rate) {
    	if(n > start)
    	return start + (rate / (n - start + 1));
    	return n;
    }

}
