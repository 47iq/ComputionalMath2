package io;

import math.Equation;
import math.SolvingMethod;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Map;

public class IOServiceImpl implements IOService {

    private IOMode mode;
    private BufferedReader reader;
    private final Map<String, Equation> equationMap;
    private final Map<String, Equation[]> equationSystemMap;

    public IOServiceImpl(Map<String, Equation> equationMap, Map<String, Equation[]> equationSystemMap) {
        this.equationMap = equationMap;
        this.equationSystemMap = equationSystemMap;
    }


    @Override
    public InputDTO inputData() throws IOException {
        reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Введите 1 для ввода с консоли или 2 для ввода из файла: ");
        try {
            mode = IOMode.getMode(Integer.parseInt(reader.readLine()));
        } catch (Exception e) {
            throw new IOException("Ошибка. Введено некорректное число.");
        }
        if (mode == IOMode.FILE) {
            System.out.println("Введите название файла: ");
            try {
                reader = new BufferedReader(new FileReader(reader.readLine()));
            } catch (Exception e) {
                throw new IOException("Ошибка. Заданный файл недоступен.");
            }
        }
        if (mode == IOMode.CONSOLE) {
            System.out.println("Введите 1 для решения уравнения или 2 для решения системы уравнений :");
        }
        try {
            int mode = Integer.parseInt(reader.readLine());
            if (mode == 1)
                return inputEquation();
            else
                return inputSystemOfEquations();
        } catch (Exception e) {
            throw new IOException("Ошибка. Введено некорректное число.");
        }
    }

    private InputDTO inputSystemOfEquations() throws IOException {
        if (mode == IOMode.CONSOLE) {
            System.out.println("Доступные системы уравнений:");
            equationSystemMap.keySet().stream().sorted().forEach(x -> {
                        System.out.print(x + ") ");
                        Arrays.stream(equationSystemMap.get(x)).forEach(System.out::println);
                        System.out.println();
                    }
            );
            System.out.println("Введите номер уравнения для решения:");
        }
        Equation[] equations;
        double left, right;
        double inaccuracy;
        try {
            equations = equationSystemMap.get(reader.readLine());
            if (equations == null)
                throw new Exception();
        } catch (Exception e) {
            throw new IOException("Ошибка. Неверный номер системы уравнений.");
        }
        try {
            if (mode == IOMode.CONSOLE)
                System.out.println("Введите левую границу интервала x:");
            left = Double.parseDouble(reader.readLine());
            if (mode == IOMode.CONSOLE)
                System.out.println("Введите правую границу интервала x:");
            right = Double.parseDouble(reader.readLine());
        } catch (Exception e) {
            throw new IOException("Ошибка. Неверная граница.");
        }
        try {
            if (mode == IOMode.CONSOLE)
                System.out.println("Введите точность:");
            inaccuracy = Double.parseDouble(reader.readLine());
        } catch (Exception e) {
            throw new IOException("Ошибка. Неверная точность.");
        }
        return new InputDTO(true, equations, left, right, inaccuracy);
    }

    private InputDTO inputEquation() throws IOException {
        if (mode == IOMode.CONSOLE) {
            System.out.println("Доступные уравнения:");
            equationMap.keySet().stream().sorted().forEach(x -> System.out.println(x + ") " + equationMap.get(x)));
            System.out.println("Введите номер уравнения для решения:");
        }
        Equation equation = null;
        double left, right, inaccuracy;
        try {
            equation = equationMap.get(reader.readLine());
            if (equation == null)
                throw new Exception();
        } catch (Exception e) {
            throw new IOException("Ошибка. Неверный номер уравнения.");
        }
        try {
            if (mode == IOMode.CONSOLE)
                System.out.println("Введите левую границу интервала:");
            left = Double.parseDouble(reader.readLine());
            if (mode == IOMode.CONSOLE)
                System.out.println("Введите правую границу интервала:");
            right = Double.parseDouble(reader.readLine());
        } catch (Exception e) {
            throw new IOException("Ошибка. Неверная граница.");
        }
        try {
            if (mode == IOMode.CONSOLE)
                System.out.println("Введите точность:");
            inaccuracy = Double.parseDouble(reader.readLine());
        } catch (Exception e) {
            throw new IOException("Ошибка. Неверная точность.");
        }
        if (mode == IOMode.CONSOLE)
            System.out.println("Введите 1 для метода половинного деления или 2 для метода простых итераций:");
        SolvingMethod method = SolvingMethod.getMethod(Integer.parseInt(reader.readLine()));
        return new InputDTO(false, equation, method, left, right, inaccuracy);
    }

    @Override
    public void printData(AnswerDTO answerDTO) {
        System.out.println("Решение:");
        char var = 'x';
        for (int i = 0; i < answerDTO.getSolutions().length; i++)
            System.out.println(Character.toString(var++) + ": " + answerDTO.getSolutions()[i] + " | погрешность: " + answerDTO.getInaccuracies()[i]);
        if(!answerDTO.isSystem) {
            System.out.println("Значение функции " + answerDTO.getEquations()[0].getInitialExpression() + " в точке: " + answerDTO.getEquations()[0].getValueInitial(answerDTO.getSolutions()[0]));
        } else {
            for (int i = 0; i < answerDTO.getSolutions().length; i++) {
                System.out.println("Значение функции + " + answerDTO.getEquations()[i].getInitialExpression() + " + в точке: " + answerDTO.getEquations()[i].getValueInitial(answerDTO.getSolutions()[0], answerDTO.getSolutions()[1]));
            }
        }
        System.out.println("Количество итераций: " + answerDTO.getIterationsCount());
    }
}
