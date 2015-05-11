package meanshift;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JFrame;
import javax.swing.JPanel;

class DrawPoints extends JPanel {

	private static double WIDTH = 700;
	private static double HEIGHT =700;
	
	private Space space;

    public DrawPoints(Space space) {
    	this.space = space;
    	
        final JFrame frame = new JFrame();
        frame.add(this);
        frame.setTitle("Points");
        frame.setSize((int)WIDTH, (int)HEIGHT);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }


    private void doDrawing(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setPaint(Color.blue);

        double w = WIDTH / space.getMaxVal();
        double h = HEIGHT / space.getMaxVal();
        
        Point[] points = space.getPoints();
    	for (int i=0; i< points.length; i++) {
    		
            int x = (int)(points[i].getPosition(0) * w);//% w;
            int y = (int)(points[i].getPosition(1) * h);//% h;
            if (points[i].getCenter() != null) {
            	g2d.setColor(points[i].getCenter().getColor());
            } else {
            	g2d.setColor(points[i].getColor());
            }
            
            g2d.drawLine(x, y, x, y);
    	}
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        doDrawing(g);
    }

}
