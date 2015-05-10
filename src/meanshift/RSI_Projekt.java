package meanshift;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.Semaphore;

public class RSI_Projekt {

    public static void main(String args[]) throws InterruptedException {
    	
        int nodes = 5;
        int executiveNodes = nodes -2;

        if(executiveNodes<=0){
            System.out.println("Za ma³a liczba w¹tków do wykonania zadania!");
            System.exit(-1);
        }

		Space space1 = new Space(2, 5000);
		space1.fillRandomGaussian(new Point(new int[] {500, 500}), 100);
		
		Space space2 = new Space(2, 5000);
		space2.fillRandomGaussian(new Point(new int[] {200, 200}), 100);
		
		Space points = space1.join(space2);
		points.visualize();

        int[] tasksLengths = splitNumberEqualy(points.getSize(), nodes);
        int[][] tasks = extractTasks(tasksLengths);

        ArrayList<Point> result = new ArrayList<Point>();

        Semaphore semaphore = new Semaphore(nodes);
        
        for(int i=0; i< tasks.length; i++){

            Thread meanShift = new MeanShift(semaphore,points,result, 150,1000, 1,tasks[i]);
            meanShift.start();
        }

        semaphore.acquire(nodes);

        Space after = new Space(2, result.size());
        for (int i=0; i<result.size(); i++) {
        	after.setPoint(i, result.get(i));
        }
        after.visualize();
        	
        printResult(result);
    }
    
    private static int[][] extractTasks(int[] tasksLength) {
        int[][] tasks = new int[tasksLength.length][];

        int counter = 0;
        int index =0;

        for(int taskLength : tasksLength){
            tasks[index] = new int[taskLength];

            for(int i=0; i<taskLength; i++){
                tasks[index][i] = counter;
                counter++;
            }

            index++;
        }

        return tasks;
    }


    static int[] splitNumberEqualy(int number, int divider){
        int rest = number % divider;
        int indexes = number / divider;
        int[] result = new int[divider];

        for(int i=0; i< divider; i++){
            result[i] = indexes;
            if(rest>0){
                result[i]++;
                rest--;
            }
        }

        return result;
    }

    static void printResult(ArrayList<Point> result){
        for(Point maxima : result){
            System.out.println(Arrays.toString(maxima.getPositions()));
        }
    }
    
}
