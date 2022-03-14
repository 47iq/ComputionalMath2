package math;

import io.AnswerDTO;

public class DivisionalSolver implements EquationSolver{

    private static final int MAX_ITERATIONS = 1000000;

    @Override
    public AnswerDTO solve(Equation equation, SolvingMethod method, double left, double right, double inaccuracy) throws Exception {
        if(right < left) {
            double temp = right;
            right = left;
            left = temp;
        }
        double initLeft = left, initRight = right;
        double middle = Math.abs(right - left) / 2 + left;
        double currentInaccuracy = Math.abs(left - right);
        if(equation.getValue(left) * equation.getValue(right) > 0)
            throw new Exception("На отрезке нет корней/ существует несколько корней. Введите другой отрезок.");
        int iterationsCount = 0;
        while(currentInaccuracy >= inaccuracy && iterationsCount < MAX_ITERATIONS) {
            if(equation.getValue(middle) * equation.getValue(left) > 0)
                left = middle;
            else
                right = middle;
            middle = Math.abs(right - left) / 2 + left;
            currentInaccuracy = Math.min(Math.abs(left - right), Math.abs(equation.getValue(middle)));
            iterationsCount++;
        }
        if (iterationsCount == MAX_ITERATIONS) {
            throw new RuntimeException("Ошибка. Не удалось достигнуть необходимой точности за заданное число итераций " + MAX_ITERATIONS);
        }
        double[] inaccuracies = new double[1];
        inaccuracies[0] = currentInaccuracy;
        double[] solutions = new double[1];
        solutions[0] = middle;
        Equation[] equations = new Equation[1];
        equations[0] = equation;
        return new AnswerDTO(false, solutions, inaccuracies, iterationsCount, equations);
    }
}
