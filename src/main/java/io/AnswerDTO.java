package io;

import lombok.AllArgsConstructor;
import lombok.Data;
import math.Equation;

@Data
@AllArgsConstructor
public class AnswerDTO {
    boolean isSystem;
    double[] solutions;
    double[] inaccuracies;
    int iterationsCount;
    Equation[] equations;
}
