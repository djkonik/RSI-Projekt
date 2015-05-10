import meanshift.Point;
import remote.RmiServer;


public class StartServer {
	
	private static String PORT = "3357";
	private static int NODES = 1;
	
	private static int DIMENTIONS = 2;
	private static int POINTS_PER_CENTER = 5000;
	private static int SPACE_MAX_VALUE = 700;
	private static Point[] CENTERS = new Point[] {
		new Point(new int[] {500, 500}),
		new Point(new int[] {200, 200})
	};
	private static int GAUSIAN_SPREAD = 100;

	static public void main(String args[]) {
		try {
			RmiServer server = new RmiServer(PORT, NODES);
			server.setDimentions(DIMENTIONS);
			server.setPointsPerCenter(POINTS_PER_CENTER);
			server.setSpaceMaxValue(SPACE_MAX_VALUE);
			server.setCenters(CENTERS);
			server.setGausianSpread(GAUSIAN_SPREAD);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
