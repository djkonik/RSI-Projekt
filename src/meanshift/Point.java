package meanshift;

import java.awt.Color;
import java.io.Serializable;
import java.util.Random;

public class Point implements Serializable {

	private int[] cords;
	private Point center;
	private Color color = Color.BLUE;

	public Point(int dimention) {
		this.cords = new int[dimention];
		for (int i=0; i<dimention; i++) {
			cords[i] = 0;
		}
	}
	
	public Point(int[] cords) {
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
	
	public Point getCenter() {
		return center;
	}

	public void setCenter(Point center) {
		this.center = center;
	}
	
	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public static Point getRandom(int dimention, int maxVal) {
		int[] cords = new int[dimention];
		Random rand = new Random();
		for (int i=0; i<dimention; i++) {
			cords[i] = (int)(rand.nextDouble()*maxVal);
		}
		return new Point(cords);
	}
	
	public static Point getRandomGaussian(Point center, int maxVal, int radius) {
		int[] cords = new int[center.getDimention()];
		Random rand = new Random();
		for (int i=0; i<cords.length; i++) {
			int val = (int)(rand.nextGaussian() * radius)  + center.getPosition(i);
			cords[i] = val;
			if (val < 0 || val > maxVal) {
				i--;
			}
		}
		return new Point(cords);
	}
	
	
	
}
