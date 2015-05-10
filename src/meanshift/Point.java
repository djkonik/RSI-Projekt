package meanshift;

import java.util.Random;

public class Point {

	private int[] cords;
	
	Point(int dimention) {
		this.cords = new int[dimention];
		for (int i=0; i<dimention; i++) {
			cords[i] = 0;
		}
	}
	
	Point(int[] cords) {
		this.cords = cords;
	}
	
	public int getDimention() {
		return cords.length;
	}
	
	public int getPosition(int dimention) {
		return cords[dimention];
	}
	
	public int[] getPositions() {
		return cords;
	}
	
	public void setPosition(int dimention, int value) {
		cords[dimention] = value;
	}
	
	public static Point getRandom(int dimention) {
		int[] cords = new int[dimention];
		Random rand = new Random();
		for (int i=0; i<dimention; i++) {
			cords[i] = (int)(rand.nextDouble()*700);
		}
		return new Point(cords);
	}
	
	public static Point getRandomGaussian(Point center, int radius) {
		int[] cords = new int[center.getDimention()];
		Random rand = new Random();
		for (int i=0; i<cords.length; i++) {
			int val = (int)(rand.nextGaussian() * radius)  + center.getPosition(i);
			cords[i] = val;
			if (val < 0 || val > 700) {
				i--;
			}
		}
		return new Point(cords);
	}
	
	
	
}
