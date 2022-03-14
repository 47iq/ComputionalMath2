package math;

import io.AnswerDTO;

public interface EquationSolver {
    AnswerDTO solve(Equation equation, SolvingMethod method, double left, double right, double inaccuracy) throws Exception;
}
