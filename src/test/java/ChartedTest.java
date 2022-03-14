import graphics.Chart;
import io.AnswerDTO;
import io.IOService;
import io.IOServiceImpl;
import io.InputDTO;
import math.*;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.general.DatasetUtils;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;
import org.junit.Test;

import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;


public class ChartedTest {

    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

    @Test
    public void testDivisional() throws Exception {
        Chart gc = new Chart("Уравнения", "1\n1\n1\n-2\n-1\n0.01\n1");
        gc.pack();
        RefineryUtilities.centerFrameOnScreen(gc);
        gc.setVisible(true);
        bufferedReader.readLine();
    }

    @Test
    public void testIterations() throws Exception {
        Chart gc = new Chart("Уравнения", "1\n1\n2\n-0.25\n0.55\n0.01\n2");
        gc.pack();
        RefineryUtilities.centerFrameOnScreen(gc);
        gc.setVisible(true);
        bufferedReader.readLine();
    }
    @Test
    public void testIterationsSystem() throws Exception {
        Chart gc = new Chart("Уравнения", "1\n2\n1\n0\n1\n0.001\n1");
        gc.pack();
        RefineryUtilities.centerFrameOnScreen(gc);
        gc.setVisible(true);
        bufferedReader.readLine();
    }

}
