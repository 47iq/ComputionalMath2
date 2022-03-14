package math;

import io.AnswerDTO;

public interface EquationSystemSolver {
    AnswerDTO solve(Equation[] equationSystem, double left, double right, double inaccuracy) throws Exception;
}
