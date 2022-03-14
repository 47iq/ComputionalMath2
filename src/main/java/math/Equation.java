package math;

public interface Equation {
    double getValue(double x);
    double getValue(double x, double y);
    double getValueInitial(double x, double y);
    double getDerivative(double x, double eps);
    String getInitialExpression();
    void modify(String var);
    double getValueInitial(double v);
}
