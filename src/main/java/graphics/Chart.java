package graphics;

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
import org.jfree.data.general.DatasetUtils;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class Chart extends ApplicationFrame {

    public Chart(String title, String inputData) throws Exception {
        super(title);
        Map<String, Equation> equationMap = new HashMap<>();
        equationMap.put("1", new EquationImpl("x ^ 3 - x + 4"));
        equationMap.put("2", new EquationImpl("0.1*sin(x) + 0.5*x"));
        equationMap.put("3", new EquationImpl("0.1*x ^ 5 + 0.5*x"));
        Map<String, Equation[]> equationSystemMap = new HashMap<>();
        Equation[] equations = new Equation[2];
        equations[0] = new EquationImpl("0.1 * x ^ 2 + x + 0.2 * y^2 - 0.3");
        equations[1] = new EquationImpl("0.2 * x ^ 2 + y - 0.1 * x * y - 0.7");
        equationSystemMap.put("1", equations);
        Equation[] equations2 = new Equation[2];
        equations2[0] = new EquationImpl("x^2-x+0.2-y");
        equations2[1] = new EquationImpl("x^2-0.2*x-y");
        equationSystemMap.put("2", equations2);
        IOService ioService = new IOServiceImpl(equationMap, equationSystemMap);
        EquationSolver solver = new IterationalSolver();
        EquationSolver divSolver = new DivisionalSolver();
        EquationSystemSolver systemSolver = new IterationalSolver();
        if(inputData != null)
            System.setIn(new java.io.ByteArrayInputStream(inputData.getBytes()));
        InputDTO inputDTO = ioService.inputData();
        AnswerDTO answerDTO;
        if (inputDTO.isSystem())
            answerDTO = systemSolver.solve(inputDTO.getEquationSystem(), inputDTO.getLeft(), inputDTO.getRight(), inputDTO.getInaccuracy());
        else if (inputDTO.getMethod() == SolvingMethod.ITERATION_METHOD)
            answerDTO = solver.solve(inputDTO.getEquation(), inputDTO.getMethod(), inputDTO.getLeft(), inputDTO.getRight(), inputDTO.getInaccuracy());
        else
            answerDTO = divSolver.solve(inputDTO.getEquation(), inputDTO.getMethod(), inputDTO.getLeft(), inputDTO.getRight(), inputDTO.getInaccuracy());
        ioService.printData(answerDTO);

        JFreeChart chart = getChart(inputDTO, answerDTO);
        chart.setBackgroundPaint(Color.white);

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(560, 367));
        setContentPane(chartPanel);
    }

    private JFreeChart getChart(InputDTO inputDTO, AnswerDTO answerDTO) {
        JFreeChart chart;
        if (inputDTO.isSystem()) {

            chart = ChartFactory.createXYLineChart("", "X", "Y",
                    createDataset(inputDTO.getEquationSystem()[0], answerDTO.getSolutions(), inputDTO.getInaccuracy()),
                    PlotOrientation.VERTICAL, true, true, false);
            XYPlot plot = (XYPlot) chart.getPlot();
            XYLineAndShapeRenderer renderer2 = new XYLineAndShapeRenderer(true, false);
            plot.setRenderer(1, renderer2);
            for (int i = 1; i < inputDTO.getEquationSystem().length; i++) {
                plot.setDataset(i, createDataset(inputDTO.getEquationSystem()[i], answerDTO.getSolutions(), inputDTO.getInaccuracy()));
            }
        } else {
            chart = ChartFactory.createXYLineChart("", "X", "Y",
                    DatasetUtils.sampleFunction2D(x -> inputDTO.getEquation().getValueInitial(x), inputDTO.getLeft(),
                            inputDTO.getRight(), 50, inputDTO.getEquation().getInitialExpression()),
                    PlotOrientation.VERTICAL, true, true, false);
        }
        return chart;
    }

    private XYDataset createDataset(Equation equation, double[] solutions, double inaccuracy) {

        var series = new XYSeries(equation.getInitialExpression());

        for (double point = solutions[0] - 1; point <= solutions[0] + 1; point += inaccuracy) {
            int cnt = 0;
            for (double value = solutions[1] - 1; value <= solutions[1] + 1; value += inaccuracy) {
                if (Math.abs(equation.getValueInitial(point, value)) < inaccuracy) {
                    series.add(point, value);
                }
            }
        }

        var dataset = new XYSeriesCollection();
        dataset.addSeries(series);

        return dataset;
    }

    public static void main(String[] args) throws Exception {
            Chart gc = new Chart("Уравнения", null);
            gc.pack();
            RefineryUtilities.centerFrameOnScreen(gc);
            gc.setVisible(true);
    }
}
