package math;

import lombok.Data;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

@Data
public class EquationImpl implements Equation{

    private Expression expression;

    private Expression initExpression;

    private String stringExpression;

    private String initialExpression;

    public EquationImpl(String expression) {
        this.expression = new ExpressionBuilder(expression).variables("x", "y").build();
        this.stringExpression = expression;
        this.initialExpression = expression;
        this.initExpression = this.expression;
    }

    @Override
    public double getValue(double x) {
        return expression.setVariable("x", x).evaluate();
    }

    @Override
    public double getValue(double x, double y) {
        return expression.setVariable("x", x).setVariable("y", y).evaluate();
    }

    @Override
    public double getValueInitial(double x, double y) {
        return initExpression.setVariable("x", x).setVariable("y", y).evaluate();
    }

    @Override
    public double getValueInitial(double v) {
        return initExpression.setVariable("x", v).evaluate();
    }

    @Override
    public double getDerivative(double x, double eps) {
        return (getValue(x + eps) - getValue(x - eps)) / (2 * eps);
    }

    @Override
    public void modify(String var) {
        this.stringExpression = "(" + stringExpression + " - " + var + ") * -1";
        this.expression = new ExpressionBuilder(stringExpression).variables("x", "y").build();
    }

    @Override
    public String toString() {
        return stringExpression;
    }
}
