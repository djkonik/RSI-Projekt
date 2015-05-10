package meanshift;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Semaphore;

class MeanShift extends Thread {

    private final double tolerance;
    private Semaphore wait_for_all;
    private Space points;
    private  ArrayList<Point> concentrationPoints;

    private int[] pointIndexes;
    private int max_iter;
    private double radius;

    public MeanShift(Semaphore wait_for_all, Space points, ArrayList<Point> result,
                     double radius, int max_iter, double tolerance, int[] pointIndexes) throws InterruptedException {

        this.wait_for_all=wait_for_all;

        wait_for_all.acquire();
        this.points = points;
        this.concentrationPoints = result;
        this.max_iter=max_iter;
        this.pointIndexes=pointIndexes;
        this.radius = radius;
        this.tolerance = tolerance;
    }

    boolean equalsWithTolerance(Point a, Point b, double tolerance) {
        if (a.getDimention() != b.getDimention()) return false;

        for (int i = 0; i < a.getDimention(); i++) {
            if (!areEqualsWithTolerance(a.getPosition(i), b.getPosition(i), tolerance)) {
            	return false;
            }
        }
        return true;
    }

    boolean areEqualsWithTolerance(double x, double y, double tolerance){
        return (Math.abs(x-y))<=tolerance;
    }

    boolean alreadyExistIn(Point element, ArrayList<Point> points) {
        for(Point point : points){
            if(equalsWithTolerance(element, point, 0.01)) {
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

            addNewConcentrationPoint(localMaxima);
        }
        wait_for_all.release();
    }

    private Point computeLocalMaxima(Point newCenter) {
        Point oldCenter = null;

        int iterations=0;
        do {
            iterations++;
            oldCenter= newCenter;
            newCenter = computeWeightCenter(oldCenter);

        } while (!equalsWithTolerance(oldCenter, newCenter,tolerance) && iterations<=max_iter);

        return newCenter;
    }

    private void addNewConcentrationPoint(Point newCenter) {
        synchronized (concentrationPoints) {
            if (!alreadyExistIn(newCenter, concentrationPoints)){
                concentrationPoints.add(newCenter);
            }
        }
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

