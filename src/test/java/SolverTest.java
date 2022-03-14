import io.AnswerDTO;
import io.IOService;
import io.IOServiceImpl;
import io.InputDTO;
import math.*;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SolverTest {

    IOService ioService;
    EquationSolver solver;
    EquationSystemSolver sysSolver;
    EquationSolver divSolver;

    @Before
    public void init() {
        Map<String, Equation> equationMap = new HashMap<>();
        equationMap.put("1", new EquationImpl("x ^ 3 - x + 4"));
        equationMap.put("2", new EquationImpl("x ^ 3 + x"));
        equationMap.put("3", new EquationImpl("0.1*sin(x) + 0.5*x"));
        Map<String, Equation[]> equationSystemMap= new HashMap<>();
        Equation[] equations = new Equation[2];
        equations[0] = new EquationImpl("0.1 * x ^ 2 + x + 0.2 * y^2 - 0.3");
        equations[1] = new EquationImpl("0.2 * x ^ 2 + y - 0.1 * x * y - 0.7");
        equationSystemMap.put("1", equations);
        ioService = new IOServiceImpl(equationMap, equationSystemMap);
        solver = new IterationalSolver();
        divSolver = new DivisionalSolver();
        sysSolver = new IterationalSolver();
    }

    @Test
    public void testIterations() throws Exception {
        String inputData = "1\n1\n3\n-0.25\n0.55\n0.01\n2";
        /*String inputData = "1\n1\n3\n-3\n-2.5\n0.01\n2";*/
        System.setIn(new java.io.ByteArrayInputStream(inputData.getBytes()));
        InputDTO inputDTO = ioService.inputData();
        AnswerDTO answerDTO = solver.solve(inputDTO.getEquation(), inputDTO.getMethod(), inputDTO.getLeft(), inputDTO.getRight(), inputDTO.getInaccuracy());
        ioService.printData(answerDTO);
    }

    @Test
    public void testDiv() throws Exception {
        String inputData = "1\n1\n1\n-2\n-1\n0.01\n1";
        System.setIn(new java.io.ByteArrayInputStream(inputData.getBytes()));
        InputDTO inputDTO = ioService.inputData();
        AnswerDTO answerDTO = divSolver.solve(inputDTO.getEquation(), inputDTO.getMethod(), inputDTO.getLeft(), inputDTO.getRight(), inputDTO.getInaccuracy());
        ioService.printData(answerDTO);
    }

    @Test
    public void testIterationsSystem() throws Exception {
        String inputData = "1\n2\n1\n0\n1\n0.001\n1";
        System.setIn(new java.io.ByteArrayInputStream(inputData.getBytes()));
        InputDTO inputDTO = ioService.inputData();
        AnswerDTO answerDTO = sysSolver.solve(inputDTO.getEquationSystem(), inputDTO.getLeft(), inputDTO.getRight(), inputDTO.getInaccuracy());
        ioService.printData(answerDTO);
    }

    @Test
    public void test() {
        String s = "-2.7 * (x ^ 3) - 1.48 * (x ^ 2) + 19.23 * x + 6.35";
        Expression expression = new ExpressionBuilder(s).variables("x").build();
        expression.setVariable("x", -2.7949175);
        System.out.println(expression.evaluate());
    }

    @Test
    public void chordes() {
        String s = "-2.7 * (x ^ 3) - 1.48 * (x ^ 2) + 19.23 * x + 6.35";
        Expression expression = new ExpressionBuilder(s).variables("x").build();
        double a0 = 2.573814719208616, b0 = 3;
        expression.setVariable("x", a0);
        System.out.println(expression.setVariable("x", a0).evaluate());
        System.out.println(expression.setVariable("x", b0).evaluate());
        System.out.println(a0 - (b0 - a0) / (expression.setVariable("x", b0).evaluate() - expression.setVariable("x", a0).evaluate()) * expression.setVariable("x", a0).evaluate());
        System.out.println(expression.setVariable("x", a0 - (b0 - a0) / (expression.setVariable("x", b0).evaluate() - expression.setVariable("x", a0).evaluate()) * expression.setVariable("x", a0).evaluate()).evaluate());
        System.out.println(b0 - a0);
    }

    @Test
    public void secants() {
        String s = "-2.7 * (x ^ 3) - 1.48 * (x ^ 2) + 19.23 * x + 6.35";
        Expression expression = new ExpressionBuilder(s).variables("x").build();
        double x0 = 0, x1 = -0.35258189894503045;
        System.out.println(x0);
        System.out.println(expression.setVariable("x", x0).evaluate());
        System.out.println(x1);
        System.out.println(expression.setVariable("x", x1).evaluate());
        double x2 = x1 - (x1 - x0) / (expression.setVariable("x", x1).evaluate() - expression.setVariable("x", x0).evaluate()) * expression.setVariable("x", x1).evaluate();
        System.out.println(x2);
        System.out.println(expression.setVariable("x", x2).evaluate());
        System.out.println(x2 - x1);
    }
}
