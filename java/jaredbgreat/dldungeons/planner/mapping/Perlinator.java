package jaredbgreat.dldungeons.planner.mapping;

import java.util.Random;

public class Perlinator {
	private float[][] one;
	private float[][] two;
	private float[][] three;
	private float[][] four;
	private float[][] five;
	
	private int w1, w2, w3, w4, w5;
	
	
	public Perlinator(int width, Random random) {
		w1 = (width / 16) + 1;
		w2 = (width /  8) + 1;
		w3 = (width /  4) + 1;
		w4 = (width /  2) + 1;
		w5 = width;
		
		one   = makeTable(w1, random);
		two   = makeTable(w2, random);
		three = makeTable(w3, random);
		four  = makeTable(w4, random);
		five  = makeTable(w5, random);
	}
	
	
	private float[][] makeTable(int w, Random random) {
		float[][] out = new float[w][w];
		for(int i = 0; i < w; i++)
			for(int j = 0; j < w; j++) {
				out[i][j] = random.nextFloat();
			}
		return out;
	}
	
	
	public byte[][] getIntTable(int scale) {
		byte[][] out = new byte[w5][w5];
		for(int i = 0; i < w5; i++)
			for(int j = 0; j < w5; j++) {
				float tmp = 0;
				tmp += (one[i/16][j/16]             / (1  + (i % 16) + (j % 16))) 
					+  (one[(i/16) + 1][(i/16) + 1] / (31 - (i % 16) - (j % 16)))
					+  (one[(i/16) + 1][j/16]       / (16 + (i % 16) - (j % 16)))
					+  (one[i/16][(j/16) + 1]       / (16 - (i % 16) + (j % 16)));
				tmp += ((two[i/8][j/8]              / (1  + (i % 8) + (j % 8))) 
					+  (two[(i/8) + 1][(i/8) + 1]  / (15 - (i % 8) - (j % 8)))
					+  (two[(i/8) + 1][j/8]        / (8 +  (i % 8) - (j % 8)))
					+  (two[i/8][(j/8) + 1]        / (8 -  (i % 8) + (j % 8))) / 2);
				tmp += ((three[i/4][j/4]             / (1  + (i % 4) + (j % 4))) 
					+  (three[(i/4) + 1][(i/4) + 1]  / (7 - (i % 4) - (j % 4)))
					+  (three[(i/4) + 1][j/4]        / (4 +  (i % 4) - (j % 4)))
					+  (three[i/4][(j/4) + 1]        / (4 -  (i % 4) + (j % 4))) / 4);
				tmp += ((four[i/2][j/2]             / (1  + (i % 2) + (j % 2))) 
					+  (four[(i/2) + 1][(i/2) + 1]  / (3 - (i % 2) - (j % 2)))
					+  (four[(i/2) + 1][j/2]        / (2 +  (i % 2) - (j % 2)))
					+  (four[i/2][(j/2) + 1]        / (2 -  (i % 2) + (j % 2))) / 8);
				tmp += five[i][j] / 16;
				tmp /= 1.5;
				out[i][j] = (byte)((float)tmp * scale);
			}
		return out;
	}
	
}
