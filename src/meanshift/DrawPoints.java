package meanshift;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JFrame;
import javax.swing.JPanel;

class DrawPoints extends JPanel {

	Point[] points;

    public DrawPoints(Point[] points) {
    	this.points = points;
    	
        final JFrame frame = new JFrame();
        frame.add(this);
        frame.setTitle("Points");
        frame.setSize(700, 700);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }



    private void doDrawing(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setPaint(Color.blue);

        int w = getWidth();
        int h = getHeight();
        
    	for (int i=0; i< points.length; i++) {
    		
            int x = points[i].getPosition(0);//% w;
            int y = points[i].getPosition(1);//% h;
            g2d.drawLine(x, y, x, y);
    	}
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        doDrawing(g);
    }

}
