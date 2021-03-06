package pcd.pinballs.mvc;

import pcd.pinballs.Body;
import pcd.pinballs.SimulatorViewer;
import pcd.pinballs.components.Position;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

public class MVCSimulatorViewer extends JFrame implements ActionListener, SimulatorViewer {

    private MVCController controller;
    private VisualiserPanel panel;

    /**
     * Creates a view of the specified size (in pixels)
     * @param w Width
     * @param h Height
     */
    public MVCSimulatorViewer(MVCController controller, int w, int h){
        this.controller = controller;

        /* Bottoni per start e stop */
        JButton btnStart = new JButton("Start");
        btnStart.addActionListener(this);

        JButton btnStop = new JButton("Stop");
        btnStop.addActionListener(this);

        setTitle("Pinballs: Bodies Simulation");
        setSize(w, h + 40);
        setResizable(false);

        JPanel panel = new JPanel();
        panel.setSize(40, 40);
        panel.add(btnStart);
        panel.add(btnStop);

        setLayout(new BorderLayout());
        getContentPane().add(panel, BorderLayout.NORTH);

        this.panel = new VisualiserPanel(w,h);
        getContentPane().add(this.panel, BorderLayout.CENTER);

        addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent ev){
                System.exit(-1);
            }
            public void windowClosed(WindowEvent ev){
                System.exit(-1);
            }
        });
        setVisible(true);
    }

    @Override
    public void display(ArrayList<Body> bodies, double vt, long iter) {
        try {
            SwingUtilities.invokeAndWait(() -> panel.display(bodies, vt, iter));
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Prendere l'iterazione corrente.
        controller.processEvent(e.getActionCommand());
    }

    public static class VisualiserPanel extends JPanel {

        private ArrayList<Body> bodies = new ArrayList<>();
        private long nIter;
        private double vt;

        private long dx;
        private long dy;

        public VisualiserPanel(int w, int h){
            setSize(w,h);
            dx = w/2 - 20;
            dy = h/2 - 20;
        }

        public long getIter() {
            return this.nIter;
        }

        public void paint(Graphics g){
            Graphics2D g2 = (Graphics2D) g;

            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_RENDERING,
                    RenderingHints.VALUE_RENDER_QUALITY);
            g2.clearRect(0,0,this.getWidth(),this.getHeight());

            bodies.forEach( b -> {
                Position p = b.getPos();
                double rad = b.getRadius();
                int x0 = (int)(dx + p.getX()*dx);
                int y0 = (int)(dy - p.getY()*dy);
                g2.drawOval(x0,y0, (int)(rad*dx*2), (int)(rad*dy*2));
            });
            String time = String.format("%.2f", vt);
            g2.drawString("Bodies: " + bodies.size() + " - vt: " + time + " - nIter: " + nIter, 2, 20);
        }

        public void display(ArrayList<Body> bodies, double vt, long iter){
            this.bodies = bodies;
            this.vt = vt;
            this.nIter = iter;
            repaint();
        }
    }
}
