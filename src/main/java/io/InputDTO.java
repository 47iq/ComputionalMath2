package io;

import lombok.Data;
import math.Equation;
import math.SolvingMethod;

@Data
public class InputDTO {
    final boolean isSystem;
    Equation equation;
    Equation[] equationSystem;
    SolvingMethod method;
    double left;
    double right;
    final double inaccuracy;

    public InputDTO(boolean isSystem, Equation equation, SolvingMethod method, double left, double right, double inaccuracy) {
        this.isSystem = isSystem;
        this.equation = equation;
        this.method = method;
        this.left = left;
        this.right = right;
        this.inaccuracy = inaccuracy;
    }

    public InputDTO(boolean isSystem, Equation[] equationSystem, double left, double right, double inaccuracy) {
        this.isSystem = isSystem;
        this.equationSystem = equationSystem;
        this.left = left;
        this.right = right;
        this.inaccuracy = inaccuracy;
        this.method = SolvingMethod.ITERATION_METHOD;
    }
}
