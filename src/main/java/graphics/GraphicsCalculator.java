package graphics;

import io.AnswerDTO;
import io.IOService;
import io.IOServiceImpl;
import io.InputDTO;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;
import math.*;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RectangleInsets;
import org.jfree.ui.RefineryUtilities;

import java.awt.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class GraphicsCalculator extends Application {

    LineChart lineChart;

    final double EPS = 1e-11;

    @Override
    public void start(Stage stage) throws Exception {
        Map<String, Equation> equationMap = new HashMap<>();
        equationMap.put("1", new EquationImpl("x ^ 3 - x + 4"));
        equationMap.put("2", new EquationImpl("sin(x) + x"));
        Map<String, Equation[]> equationSystemMap = new HashMap<>();
        Equation[] equations = new Equation[2];
        equations[0] = new EquationImpl("0.1 * x ^ 2 + x + 0.2 * y^2 - 0.3");
        equations[1] = new EquationImpl("0.2 * x ^ 2 + y - 0.1 * x * y - 0.7");
        equationSystemMap.put("1", equations);
        Equation[] equations2 = new Equation[2];
        equations2[0] = new EquationImpl("x ^ 2 - x + 0.2 - y");
        equations2[1] = new EquationImpl("x ^ 2 - 0.2 * x - y");
        equationSystemMap.put("2", equations2);
        IOService ioService = new IOServiceImpl(equationMap, equationSystemMap);
        EquationSolver solver = new IterationalSolver();
        EquationSolver divSolver = new DivisionalSolver();
        EquationSystemSolver systemSolver = new IterationalSolver();
        /*String inputData = "1\n1\n1\n-2\n-1\n0.01\n2";*/
        String inputData = "1\n1\n2\n-2\n1\n0.01\n1";
       /* String inputData = "1\n2\n1\n0\n1\n0.001\n1";*/
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

        if (answerDTO.getSolutions().length == 1)
            lineChart = new LineChart(
                    new NumberAxis(answerDTO.getSolutions()[0] - 1, answerDTO.getSolutions()[0] + 1, 2),
                    new NumberAxis(-1, 1, 2)
            );
        else
            lineChart = new LineChart(
                    new NumberAxis(answerDTO.getSolutions()[0] - 1, answerDTO.getSolutions()[0] + 1, 2),
                    new NumberAxis(answerDTO.getSolutions()[1] - 1, answerDTO.getSolutions()[1] + 1, 2)
            );
        if (inputDTO.isSystem()) {
            for (int i = 0; i < inputDTO.getEquationSystem().length; i++) {
                XYChart.Series<Double, Double> series = new XYChart.Series<>();

                series.setName(inputDTO.getEquationSystem()[i].getInitialExpression());

                lineChart.setCreateSymbols(false);
                lineChart.getData().add(series);

                for (double point = answerDTO.getSolutions()[0] - 1; point <= answerDTO.getSolutions()[0] + 1; point += inputDTO.getInaccuracy()) {
                    int cnt = 0;
                    for (double value = answerDTO.getSolutions()[1] - 1; value <= answerDTO.getSolutions()[1] + 1; value += inputDTO.getInaccuracy()) {
                        if(Math.abs(inputDTO.getEquationSystem()[i].getValueInitial(point, value)) < inputDTO.getInaccuracy()) {
                            series.getData().add(new XYChart.Data<>(point, value));
                        }
                    }
                }
            }
        } else {
            XYChart.Series<Double, Double> series = new XYChart.Series<>();

            series.setName(inputDTO.getEquation().toString());

            lineChart.setCreateSymbols(false);
            lineChart.getData().add(series);
            System.out.println(inputDTO.getEquation().getValue(1));

            for (double point = answerDTO.getSolutions()[0] - 10; point <= answerDTO.getSolutions()[0] + 10; point += 0.01) {

                series.getData().add(new XYChart.Data<>(point, inputDTO.getEquation().getValue(point)));
            }
        }
        Scene scene = new Scene(lineChart, 900, 600);
        stage.setScene(scene);
        stage.show();
    }
}


