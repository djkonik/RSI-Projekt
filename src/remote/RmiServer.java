package remote;
import java.awt.Color;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import meanshift.Point;
import meanshift.Space;

public class RmiServer extends java.rmi.server.UnicastRemoteObject implements ReceiveMessageInterface {
	
	private int dimentions;
	private int pointsPerCenter;
	private int spaceMaxValue;
	private Point[] centers;
	private int gausianSpread;
	
	private int radius = 175;
	private int maxIter = 1000;
	private int precision = 1;
	
	private Color colors[] = new Color[] {
			Color.BLUE, Color.RED, Color.BLACK,
			Color.GREEN, Color.CYAN, Color.MAGENTA
	};
	private long timer;
	
	String thisAddress;
	int thisPort;
	int nodes;
	Registry registry; // rmi registry for lookup the remote objects.
	List<CallbackMessageInterface> clients = new ArrayList<CallbackMessageInterface>();
	List<Point> concentrationPoints = new ArrayList<Point>();
	
	public void setDimentions(int dimentions) {
		this.dimentions = dimentions;
	}

	public void setPointsPerCenter(int pointsPerCenter) {
		this.pointsPerCenter = pointsPerCenter;
	}

	public void setSpaceMaxValue(int spaceMaxValue) {
		this.spaceMaxValue = spaceMaxValue;
	}

	public void setCenters(Point[] centers) {
		this.centers = centers;
	}

	public void setGausianSpread(int gausianSpread) {
		this.gausianSpread = gausianSpread;
	}


	// This method is called from the remote client by the RMI.
	// This is the implementation of the �gReceiveMessageInterface�h.
	@Override
	public void receiveMessage(String x) throws RemoteException {
		System.out.println(x);
	}
	

	@Override
	public void login(CallbackMessageInterface client) throws RemoteException {
		System.out.println("Client logged in");
		clients.add(client);
		if (clients.size() == nodes) {
			System.out.println("Starting MeanShift");
			new StartMeanShift().start();
		} else {
			System.out.println("Waiting for " + (nodes - clients.size()) + " nodes");
		}
	}
	
	class StartMeanShift extends Thread {
		@Override
		public void run() {
			
			Space points = new Space(dimentions, 0, 0);
			for (Point center : centers) {
				Space space = new Space(dimentions, pointsPerCenter, spaceMaxValue);
				space.fillRandomGaussian(center, gausianSpread);
				points = points.join(space);
			}
			points.visualize();

	        int[] tasksLengths = splitNumberEqualy(points.getSize(), nodes);
	        int[][] tasks = extractTasks(tasksLengths);

	        //List<Point> result = new ArrayList<Point>();
	        //result.addAll(Arrays.asList(points.getPoints()));

	        
			//System.out.println(result.size());
			
	        timer = System.currentTimeMillis();
	        
	        for(int i=0; i<tasks.length; i++) {
	        	try {
	        		clients.get(i).sheduleTask(points, radius, maxIter, precision, tasks[i]);
	        	} catch (RemoteException e) {
	        		e.printStackTrace();
	        	};
	        }

	         /*   Thread meanShift = null;
				try {
					meanShift = new MeanShift(semaphore,points,result, 150, 1000, 1,tasks[i]);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	            meanShift.start();*/
	        /*}

	        /*Semaphore semaphore = new Semaphore(nodes);
	        
	        for(int i=0; i< tasks.length; i++) {

	            Thread meanShift = null;
				try {
					meanShift = new MeanShift(semaphore,points,result, 150, 1000, 1,tasks[i]);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	            meanShift.start();*/
	        /*}

	        Space after = new Space(2, result.size(), 700);
	        for (int i=0; i<result.size(); i++) {
	        	after.setPoint(i, result.get(i));
	        }
	        after.visualize();
	        	
	        printResult(result);*/
	    }
	    
	    private int[][] extractTasks(int[] tasksLength) {
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


	    private int[] splitNumberEqualy(int number, int divider){
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

	   /* private void printResult(ArrayList<Point> result){
	        for(Point maxima : result){
	            System.out.println(Arrays.toString(maxima.getPositions()));
	        }
	    }*/

	}

	public RmiServer(String thisPort, int nodes) throws RemoteException, UnknownHostException {
		this.nodes = nodes;
		thisAddress = (InetAddress.getLocalHost()).toString();
		this.thisPort = Integer.parseInt(thisPort);
		System.out.println("Starting server at " + thisAddress + " : " + this.thisPort);
		System.out.println("Waiting for " + nodes + " nodes");
		
		// create the registry and bind the name and object.
		registry = LocateRegistry.createRegistry(this.thisPort);
		registry.rebind("rmiServer", this);
	}

	private boolean isInConcentrationCenter(Point a, Point b) {
		for (int i=0; i<a.getDimention(); i++) {
			if (Math.abs(a.getPosition(i) - b.getPosition(i)) > precision*2) {
				return false;
			}
		}
		return true;
	}

	private List<Point> filterResult() {
		List<Point> result = new ArrayList<Point>();
        
        List<List<Point>> concentrations = new ArrayList<List<Point>>();
        for (Point point : concentrationPoints) {
        	Point maxima = point.getCenter();
        	boolean centerFound = false;
        	for (List<Point> concentration : concentrations) {
        		Point sample = concentration.get(0).getCenter();
        		if (isInConcentrationCenter(maxima, sample)) {
        			centerFound = true;
        			concentration.add(point);
        			break;
        		}
        	}
        	if (!centerFound) {
        		List<Point> newConcentration = new ArrayList<Point>();
        		newConcentration.add(point);
        		concentrations.add(newConcentration);
        	}
        }
    	for (int j=0; j<concentrations.size(); j++) {
    		List<Point> concentration = concentrations.get(j);
    		Point center = new Point(dimentions);
    		for(Point point : concentration) {
    			for(int i=0; i<dimentions; i++) {
    				center.setPosition(i, center.getPosition(i) + point.getCenter().getPosition(i));
    			}
    			point.setCenter(center);
    		}
			for(int i=0; i<dimentions; i++) {
				center.setPosition(i, center.getPosition(i) / concentration.size());
			}
			center.setColor(colors[j]);
			result.add(center);
    	}
        
        return result;
	}

	@Override
	public void sendResult(List<Point> result) throws RemoteException {
		nodes--;
		concentrationPoints.addAll(result);
 
		if (nodes == 0) {
			result = filterResult();
			System.out.println("Server received response from client. Evaluation took " + (System.currentTimeMillis() - timer) + " milisecons");
			
	        Space after = new Space(dimentions, result.size(), spaceMaxValue);
	        for (int i=0; i<result.size(); i++) {
	        	after.setPoint(i, result.get(i));
	        }
	        after.visualize();
	        
	        after = new Space(dimentions, concentrationPoints.size(), spaceMaxValue);
	        for (int i=0; i<concentrationPoints.size(); i++) {
	        	after.setPoint(i, concentrationPoints.get(i));
	        }
	        after.visualize();
	        
	        
		} else {
			System.out.println("Server received response from client. Waits for " + nodes + " clients more");
		}
	}

}