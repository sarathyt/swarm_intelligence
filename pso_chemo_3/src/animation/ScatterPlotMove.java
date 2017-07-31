/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package animation;

/**
 *
 * @author tarangini
 */
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Shape;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.util.*;
import javafx.animation.TranslateTransition;
import java.awt.geom.AffineTransform;
import javafx.util.Duration;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import org.jfree.chart.*;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.xy.XYDataItem;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import pso_basic_functions.Particle;
import org.jfree.util.ShapeUtilities;
import org.jfree.ui.ApplicationFrame;


public class ScatterPlotMove extends ApplicationFrame{

    //private static final int N = 16;
    private static final String title = "Chemotherapy Delivered using NanoBots to tumor cells";
    private static final Random rand = new Random();
    private XYSeries moved = new XYSeries("Nano Bots");
    private XYSeries moved_1= new XYSeries("Tumor Cell");

    public ScatterPlotMove(String title, Vector<Particle> swarm, double i, double d) {
        super(title);
        update(swarm, i, d);
        final ChartPanel chartPanel = createDemoPanel();
        this.add(chartPanel, BorderLayout.CENTER);
        JPanel control = new JPanel();
//        control.add(new JButton(new AbstractAction("Move") {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                moved.clear();
//                update();
//            }
//        }));
        this.add(control, BorderLayout.SOUTH);
    }

    public void action(Vector<Particle> swarm, double x, double y) {
        moved.clear();
        update(swarm, x, y);
    }

    private void update(Vector<Particle> swarm, double x, double y) {
        for (int i = 0; i < swarm.size(); i++) {
            moved.add(new XYDataItem(swarm.get(i).getLocation().getLoc()[0], swarm.get(i).getLocation().getLoc()[1]));
        }
        moved_1.add(new XYDataItem(x,y));
    }

    private ChartPanel createDemoPanel() {
        JFreeChart jfreechart = ChartFactory.createScatterPlot(
                title, "X", "Y", createSampleData(),
                PlotOrientation.VERTICAL, true, true, false);
        Shape cross = ShapeUtilities.createDiamond(2);
         Shape square = ShapeUtilities.createDownTriangle(3);
        XYPlot xyPlot = (XYPlot) jfreechart.getPlot();
        xyPlot.setBackgroundPaint(Color.BLACK);
        XYItemRenderer renderer = xyPlot.getRenderer();
            renderer.setSeriesShape(0, cross);
        renderer.setSeriesPaint(0, Color.green);
        NumberAxis domain = (NumberAxis) xyPlot.getDomainAxis();
        domain.setRange(-5.0, 5.0);
        domain.setTickUnit(new NumberTickUnit(1));
        NumberAxis range = (NumberAxis) xyPlot.getRangeAxis();
        range.setRange(-5.0, 5.0);
        range.setTickUnit(new NumberTickUnit(1));
        
        return new ChartPanel(jfreechart) {

            @Override
            public Dimension getPreferredSize() {
                return new Dimension(640, 480);
            }
        };
    }

    private XYDataset createSampleData() {
        XYSeriesCollection xySeriesCollection = new XYSeriesCollection();
        xySeriesCollection.addSeries(moved);
          xySeriesCollection.addSeries(moved_1);
        return xySeriesCollection;
    }

//    public static void main(String args[]) {
//        EventQueue.invokeLater(new Runnable() {
//            @Override
//            public void run() {
//                ScatterPlotMove demo = new ScatterPlotMove(title);
//                demo.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//                demo.pack();
//                demo.setLocationRelativeTo(null);
//                demo.setVisible(true);
//            }
//        });
//    }
}
