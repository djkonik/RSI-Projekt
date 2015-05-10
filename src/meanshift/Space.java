package meanshift;

import java.io.Serializable;

public class Space implements Serializable {

	private int dimention;
	private int maxVal;
	private Point[] points;
	
	public Space(int dimention, int size, int maxVal) {
		this.dimention = dimention;
		this.maxVal = maxVal;
		points = new Point[size];
	}
	
	public void setPoint(int pos, Point point) {
		if (point.getDimention() == dimention) {
			points[pos] = point;
		}
	}
	
	public int getMaxVal() {
		return maxVal;
	}

	public Point getPoint(int pos) {
		return points[pos];
	}
	
	public Point[] getPoints() {
		return points;
	}
	
	public int getSize() {
		return points.length;
	}
	
	public void fillRandom() {
		for(int i=0; i<points.length; i++) {
			points[i] = Point.getRandom(dimention, maxVal);
		}
	}
	
	public void fillRandomGaussian(Point seed, int radius) {
		for(int i=0; i<points.length; i++) {
			points[i] = Point.getRandomGaussian(seed, maxVal, radius);
		}
	}
	
	public void visualize() {
		if(dimention == 2) {
			new DrawPoints(this);
		}
	}
	
	public Space join(Space other) {
		if (dimention == other.dimention) {
			Space newSpace = new Space(dimention, points.length + other.points.length, Math.max(maxVal, other.maxVal));
			for (int i=0; i<points.length; i++) {
				newSpace.points[i] = points[i];
			}
			for (int i=0; i<other.points.length; i++) {
				newSpace.points[points.length + i] = other.points[i];
			}
			return newSpace;
		}
		return null;
	}
}
