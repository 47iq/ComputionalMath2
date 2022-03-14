package io;

public enum IOMode {
    CONSOLE,
    FILE;

    public static IOMode getMode(int NUM) {
        switch (NUM) {
            case 1:
                return CONSOLE;
            default:
                return FILE;
        }
    }
}
