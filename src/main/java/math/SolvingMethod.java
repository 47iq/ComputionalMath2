package math;

public enum SolvingMethod {
    DIVISION_METHOD,
    ITERATION_METHOD;


    public static SolvingMethod getMethod(int parseInt) {
        switch (parseInt) {
            case 1:
                return DIVISION_METHOD;
            default:
                return ITERATION_METHOD;
        }
    }
}
