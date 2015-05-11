package meanshift;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import remote.ReceiveMessageInterface;

public class MeanShift extends Thread {

    private final double tolerance;
    private Space points;
    private  List<Point> concentrationPoints;
    ReceiveMessageInterface server;

    private int[] pointIndexes;
    private int max_iter;
    private double radius;

    public MeanShift(ReceiveMessageInterface server, Space points,
                     double radius, int max_iter, double tolerance, int[] pointIndexes) {

        this.server = server;
        this.points = points;
        this.concentrationPoints = new ArrayList<Point>();
        this.max_iter=max_iter;
        this.pointIndexes=pointIndexes;
        this.radius = radius;
        this.tolerance = tolerance;
    }

    boolean equalsWithTolerance(Point a, Point b) {
        if (a.getDimention() != b.getDimention()) return false;

        for (int i = 0; i < a.getDimention(); i++) {
            if (!areEqualsWithTolerance(a.getPosition(i), b.getPosition(i))) {
            	return false;
            }
        }
        return true;
    }

    boolean areEqualsWithTolerance(double x, double y){
        return (Math.abs(x-y))<tolerance;
    }

    boolean alreadyExistIn(Point element, List<Point> points) {
        for(Point point : points){
            if(equalsWithTolerance(element, point)) {
            	return true;
            }
        }
        return false;
    }

    Point getWeightedCenterFromPoints(List<Point> points){

        // assert all dimension are the same
    	Point sum = new Point(points.get(0).getDimention());

        addAllDimensionToSum(points, sum);

        divideArrayElements(sum, points.size());

        return sum;
    }

    private void addAllDimensionToSum(List<Point> points, Point sum) {
        for (Point point : points){
            addArrays(sum, point);
        }
    }

    void divideArrayElements(Point accumulator, int divider){
        for(int i=0; i<accumulator.getDimention(); i++){
            accumulator.setPosition(i, accumulator.getPosition(i) / divider);
        }
    }

    void addArrays(Point accumulator, Point added){
        for(int i=0; i<added.getDimention(); i++){
            accumulator.setPosition(i, accumulator.getPosition(i) + added.getPosition(i));
        }
    }

    boolean isInRadius(Point a, Point b, double radius)
    {
        double length = euclidanDistance(a,b);
        return length<=radius;
    }


    double euclidanDistance(Point a, Point b){
        long distance = 0;

        for(int i=0; i< a.getDimention(); i++){
            distance += (long)Math.pow(a.getPosition(i)-b.getPosition(i), 2);
        }

        return Math.sqrt(distance);
    }

    public void run() {
        for(int i=0; i< pointIndexes.length; i++){
            int point_index = pointIndexes[i];

            Point selectedPoint = points.getPoint(point_index);

            Point localMaxima = computeLocalMaxima(selectedPoint);

            addNewConcentrationPoint(selectedPoint, localMaxima);
        }
        System.out.println("Client finished evaluation and sends response to server");
        try {
			server.sendResult(concentrationPoints);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
    }

    private Point computeLocalMaxima(Point newCenter) {
        Point oldCenter = null;

        int iterations=0;
        do {
            iterations++;
            oldCenter= newCenter;
            newCenter = computeWeightCenter(oldCenter);

        } while (!equalsWithTolerance(oldCenter, newCenter) && iterations<=max_iter);

        return newCenter;
    }

    private void addNewConcentrationPoint(Point selectedPoint, Point newCenter) {
            /*if (!alreadyExistIn(newCenter, concentrationPoints)){
                concentrationPoints.add(newCenter);
            }*/
    	selectedPoint.setCenter(newCenter);
    	concentrationPoints.add(selectedPoint);
    }

    private Point computeWeightCenter(Point center) {
    	List<Point> pointsInRadius = getPointsInRadius(center);
        return getWeightedCenterFromPoints(pointsInRadius);
    }

    private List<Point> getPointsInRadius(Point center) {

        ArrayList<Point> inRadiusPoints = new ArrayList<Point>();
        for (int i=0; i<points.getSize(); i++) {
        	Point point = points.getPoint(i);
            if(isInRadius(center, point, radius)) {
            	inRadiusPoints.add(point);            
            }
        }

        return inRadiusPoints;
    }
}

