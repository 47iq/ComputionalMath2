package math;

import io.AnswerDTO;

import java.util.Arrays;

import static java.lang.Double.NaN;

public class IterationalSolver implements EquationSolver, EquationSystemSolver{

    private static final int MAX_ITERATIONS = 1000000;

    private static final double EPS = 1e-9;

    @Override
    public AnswerDTO solve(Equation equation, SolvingMethod method, double left, double right, double maxInaccuracy) throws Exception {
        double maxDerivative = 0;
        if(right < left) {
            double temp = right;
            right = left;
            left = temp;
        }
        for(double i = left; i <= right; i += Math.abs(right - left) / 10000) {
            maxDerivative = Math.max(maxDerivative, Math.abs(equation.getDerivative(i, EPS)));
        }
        double currentInaccuracy = Double.MAX_VALUE, lambda = -1 / maxDerivative;
       /* double solutionX = (left + right) / 2;*/
        double solutionX = right;
        double prevSolution = solutionX;
        int iterationsCount = 0;
        double leftDerivative = 1 + lambda * equation.getDerivative(left, EPS), rightDerivative = 1 + lambda * equation.getDerivative(right, EPS);
        double q = 0, coeff = 1;
        for(double i = left; i <= right; i += Math.abs(right - left) / 10000) {
            q = Math.max(q, Math.abs(1 + lambda * equation.getDerivative(i, EPS)));
        }
        if(q > 0.5)
            coeff = (1 - q) / q;
         System.out.println(leftDerivative);
        System.out.println(rightDerivative);
        if(leftDerivative >= 1 || rightDerivative >= 1)
            throw new Exception("Ошибка. Сходимость не достигается");
        while (currentInaccuracy > maxInaccuracy && iterationsCount < MAX_ITERATIONS) {
            prevSolution = solutionX;
            solutionX = prevSolution + lambda * equation.getValue(solutionX);
            currentInaccuracy = Math.min(Math.abs(equation.getValue(solutionX)), Math.abs(prevSolution - solutionX) / coeff);
            iterationsCount++;
        }
        if (iterationsCount == MAX_ITERATIONS) {
            throw new RuntimeException("Ошибка. Не удалось достигнуть необходимой точности за заданное число итераций "
                    + MAX_ITERATIONS);
        }
        double[] inaccuracies = new double[1];
        inaccuracies[0] = currentInaccuracy;
        double[] solutions = new double[1];
        solutions[0] = solutionX;
        Equation[] equations = new Equation[1];
        equations[0] = equation;
        return new AnswerDTO(false, solutions, inaccuracies, iterationsCount, equations);
    }

    @Override
    public AnswerDTO solve(Equation[] equationSystem, double left, double right, double inaccuracy) throws Exception {
        int x = 0;
        equationSystem[0].modify("x");
        equationSystem[1].modify("y");
        if(equationSystem.length != 2)
            throw new Exception("Ошибка. В данный момент поддерживается только решение систем из двух уравнений.");
        double[] prevSolution, currentInaccuracies = new double[2];
        double[] solution = new double[2];
        int iterationsCount = 0;
        solution[0] = right;
        solution[1] = right;
        currentInaccuracies[0] = Double.MAX_VALUE;
        currentInaccuracies[1] = Double.MAX_VALUE;
        while (Math.max(currentInaccuracies[0], currentInaccuracies[1]) > inaccuracy && iterationsCount < MAX_ITERATIONS) {
            prevSolution = solution.clone();
            solution[0] = equationSystem[0].getValue(prevSolution[0], prevSolution[1]);
            solution[1] = equationSystem[1].getValue(prevSolution[0], prevSolution[1]);
            currentInaccuracies[0] = Math.abs(solution[0] - prevSolution[0]);
            currentInaccuracies[1] = Math.abs(solution[1] - prevSolution[1]);
            if(Double.isNaN(currentInaccuracies[0]) || Double.isNaN(currentInaccuracies[1])
                    || Double.isInfinite(currentInaccuracies[0]) || Double.isInfinite(currentInaccuracies[1]))
                throw new RuntimeException("Ошибка. Сходимость не достигается.");
            iterationsCount++;
        }
        if (iterationsCount == MAX_ITERATIONS) {
            throw new RuntimeException("Ошибка. Не удалось достигнуть необходимой точности за заданное число итераций " + MAX_ITERATIONS);
        }
        return new AnswerDTO(true, solution, currentInaccuracies, iterationsCount, equationSystem);
    }
}
