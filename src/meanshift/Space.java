package meanshift;

public class Space {

	int dimention;
	Point[] points;
	
	Space(int dimention, int size) {
		this.dimention = dimention;
		points = new Point[size];
	}
	
	public void setPoint(int pos, Point point) {
		if (point.getDimention() == dimention) {
			points[pos] = point;
		}
	}
	
	public Point getPoint(int pos) {
		return points[pos];
	}
	
	public Point[] getPoints(int pos) {
		return points;
	}
	
	public int getSize() {
		return points.length;
	}
	
	public void fillRandom() {
		for(int i=0; i<points.length; i++) {
			points[i] = Point.getRandom(dimention);
		}
	}
	
	public void fillRandomGaussian(Point seed, int radius) {
		for(int i=0; i<points.length; i++) {
			points[i] = Point.getRandomGaussian(seed, radius);
		}
	}
	
	public void visualize() {
		if(dimention == 2) {
			new DrawPoints(points);
		}
	}
	
	public Space join(Space other) {
		if (dimention == other.dimention) {
			Space newSpace = new Space(dimention, points.length + other.points.length);
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
