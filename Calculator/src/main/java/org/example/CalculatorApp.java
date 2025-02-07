package org.example;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class CalculatorApp extends JFrame {
    // Оголошення всіх необхідних змінних
    private JTextField display, display2;
    private double currentValue = 0.0;
    private String currentOperator = "";
    private double initialNumber = 0.0;

    private CalculatorApp() {
        // Встановлення іконки
        setIconImage(new ImageIcon("src/main/resources/Calculator.jpg").getImage());
        setTitle("Calculator");// Встановлення назви вікна
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);// Встановлення функції закриття вікна
        setSize(300, 600);// Задання розміру вікна
        setLocationRelativeTo(null); // Початкова позиція вікна "Центр"
        Font font = new Font("Forte", Font.PLAIN, 30);// Встановлення шрифту

        JPanel text = new JPanel(new GridLayout(2, 1));// Створення панелі з текстовими полями
        display = new JTextField();
        display2 = new JTextField();
        display2.setBorder(new LineBorder(Color.gray, 2));// Створення рамки з кольром та товщиною
        display.setBorder(new LineBorder(Color.gray, 2));

        display.setBackground(Color.LIGHT_GRAY); // Встановлюємо колір фону поля
        display.setFont(font);
        display.setEditable(false);
        display.setHorizontalAlignment(JTextField.RIGHT);

        display2.setBackground(Color.LIGHT_GRAY);
        display2.setFont(font);
        display2.setEditable(false);
        display2.setHorizontalAlignment(JTextField.RIGHT);
        text.add(display2); // Додаємо до панелі
        text.add(display);

        add(text, BorderLayout.NORTH); // Додаєм панель до вікна з розміщенням NORTH

        JPanel buttonPanel = new JPanel(new GridLayout(6, 5));// Створення панелі для кнопок

        String[] buttonLabels = { // Масив з надписами для кнопок
                "%", "!", "π", "<<",
                "1/x", "x²", "√x", "/",
                "7", "8", "9", "*",
                "4", "5", "6", "-",
                "1", "2", "3", "+",
                "C", "0", ".", "="
        };

        for (String label : buttonLabels) {
            JButton button = new JButton(label); // Створення кнопок з відповідними назвами
            button.setFont(font);
            button.setBorder(BorderFactory.createBevelBorder(1)); // Створення згладження країв кнопки
            button.setBackground(Color.LIGHT_GRAY);
            button.setFocusPainted(false); // Прибирання ефекту виділення кнопок
            button.setPreferredSize(new Dimension(80, 70));// Встановлення розміру кнопок
            button.addActionListener(new ActionListener() {
                // Створення методу який виконується у разі виконання натиснення кнопки
                @Override
                public void actionPerformed(ActionEvent e) {

                // переввірка наявності крапки
                boolean dotAlreadyExists = display.getText().contains(".");

                String buttonText = button.getText(); // Зчитуємо текст з кнопки
                if (buttonText.equals("-") && display.getText().endsWith("-")) {
                    return; // Якщо в кінці вже є мінус, нічого не робити
                }
                if (buttonText.equals(".") && display.getText().endsWith("-")) {
                    return;
                }

                if (buttonText.matches("[0-9]") && display.getText().equals("0")) {
                    display.setText(buttonText);
                    display2.setText(buttonText);
                    return; // Якщо введено 0 і далі натискається 0, нічого не робити
                }
                // Заборона вводити крапку першою
                if ((buttonText.equals(".") || buttonText.equals("%")) && display.getText().isEmpty()) {
                    return;
                }

                if (buttonText.equals("-")) {
                    // Можливість зробити перше число від'ємним
                    if (display.getText().isEmpty() || currentOperator.matches("[+\\-*/]")) {
                        display.setText("-");
                        display2.setText(display2.getText() + buttonText);

                        return;
                    }
                }

                // Перевірка на те чи введено число
                if (buttonText.matches("[π]")) {
                    double PI = 3.14;
                    display.setText(display.getText() + PI);
                    display2.setText(display2.getText() + PI);
                } else if (buttonText.matches("[0-9]") || (buttonText.matches("[.]") && !dotAlreadyExists)) {
                    display.setText(display.getText() + buttonText);
                    display2.setText(display2.getText() + buttonText);

                } else if (buttonText.equals("!")) {// Обрахування факторіала
                    factorial();
                } else if (buttonText.equals("%")) {// Обрахування відсотку від числа
                    currentOperator = buttonText;
                    percentage(currentOperator);
                } else if (buttonText.equals("C")) { // Якщо введено "С" робимо очистку полів та значень
                    clear();

                } else if (buttonText.matches("[+\\-*/]")) { // Перевірка на введення оператора
                    if (!currentOperator.isEmpty()) {
                        double value = Double.parseDouble(display.getText());
                        calculate(value);
                    }
                    currentOperator = buttonText;
                    currentValue = Double.parseDouble(display.getText());
                    display2.setText(" " + display2.getText() + currentOperator);
                    display.setText("");

                } else if (buttonText.equals("1/x")) {
                    if (!display.getText().isEmpty()) {
                        double value = Double.parseDouble(display.getText());
                        if (value != 0) {
                            double result = 1 / value;
                            for (int j = 0; j < display.getText().length(); j++) {
                                display2.setText(display2.getText().substring(0, display2.getText().length() - 1));
                            }
                            display.setText(String.valueOf(result));

                            display2.setText(display2.getText() + "1/(" + value + ")");
                        } else {
                            display.setText("Error");
                            display2.setText("Cannot divide by zero");
                        }
                    }
                } else if (buttonText.equals("x²")) { // Перевірка натиснення відповідної кнопки
                    if (!display.getText().isEmpty()) {
                        double value = Double.parseDouble(display.getText());
                        double result = Math.pow(value, 2);
                        display.setText(Double.toString(result));
                        display2.setText(display2.getText() + "²");
                    }
                } else if (buttonText.matches("[0-9]") && currentOperator.equals("")) {
                    display.setText(buttonText);
                    display2.setText(buttonText);

                } else if (buttonText.equals("√x")) { // Перевірка натиснення відповідної кнопки
                    if (!display.getText().isEmpty()) {
                        double value = Double.parseDouble(display.getText());
                        if (value >= 0) {
                            double result = Math.sqrt(value);
                            for (int j = 0; j < display.getText().length(); j++) {// Стирання певного значення для
                                                                                  // коректного виводу
                                display2.setText(display2.getText().substring(0, display2.getText().length() - 1));
                            }
                            display.setText(Double.toString(result));

                            display2.setText(display2.getText() + "√(" + value + ")");
                        } else {
                            display.setText("Error");
                            display2.setText("Invalid input");
                        }
                    }
                } else if (buttonText.equals("=")) { // Перевірка натиснення відповідної кнопки
                    double value = Double.parseDouble(display.getText());
                    calculate(value);
                    currentOperator = "";
                } else if (buttonText.equals("<<")) {
                    if (!display.getText().isEmpty()) {
                        display.setText(display.getText().substring(0, display.getText().length() - 1));
                        display2.setText(display2.getText().substring(0, display2.getText().length() - 1));
                    }
                }
            }

        });

            buttonPanel.add(button);// Додаємо до панелі кнопки

        }
        // Створення методу який виконується у разі виконання натиснення клавіши
        display.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                boolean dotAlreadyExists = display.getText().contains(".");

                char keyChar = e.getKeyChar();
                if (keyChar == '.' && (display.getText().isEmpty()|| display.getText().endsWith("-"))) {
                    return;
                }
                if (keyChar == '-' && display.getText().endsWith("-")) {
                    return;
                }
                if (keyChar == '-') {
                    if (display.getText().isEmpty() || currentOperator.matches("[+\\-*/]")) {
                        display.setText("-");
                        display2.setText(display2.getText() + keyChar);
                        return;
                    }
                }
                if (Character.isDigit(keyChar) && display.getText().equals("0")) {
                    display.setText(String.valueOf(keyChar));
                    display2.setText(String.valueOf(keyChar));
                    return; // Якщо введено 0 і далі натискається 0, нічого не робить
                }
                if (keyChar=='P' || keyChar=='p') {
                    double PI = 3.14;
                    display.setText(display.getText() + PI);
                    display2.setText(display2.getText() + PI);
                }else if (Character.isDigit(keyChar) || (keyChar == '.' && !dotAlreadyExists)) {
                    display.setText(display.getText() + keyChar);
                    display2.setText(display2.getText() + keyChar);

                } else if (keyChar == 'C' || keyChar == 'c') {
                    clear();

                } else if (keyChar == '+' || keyChar == '-' || keyChar == '*' || keyChar == '/') {
                    if (!currentOperator.isEmpty()) {
                        double value = Double.parseDouble(display.getText());
                        calculate(value);
                    }
                    currentOperator = String.valueOf(keyChar);
                    currentValue = Double.parseDouble(display.getText());
                    display2.setText(" " + display2.getText() + currentOperator);
                    display.setText("");

                } else if (keyChar == '=' || keyChar == KeyEvent.VK_ENTER) {
                    double value = Double.parseDouble(display.getText());
                    calculate(value);
                    currentOperator = "";

                } else if (keyChar == '%') {
                    currentOperator = String.valueOf(keyChar);
                    percentage(currentOperator);
                } else if (keyChar == '!') {
                    factorial();
                } else if (keyChar == KeyEvent.VK_BACK_SPACE) {
                    if (!display.getText().isEmpty()) {
                        display.setText(display.getText().substring(0, display.getText().length() - 1));
                        display2.setText(display2.getText().substring(0, display2.getText().length() - 1));
                    }
                }
            }
        });
        add(buttonPanel, BorderLayout.CENTER); // Додання панелі з кнопками до вікна
        pack(); // Встановлення розміру вікна до мінімально допустимого внутрішніми елементами
        setVisible(true);
        display.requestFocusInWindow();
    }

    private void clear() {
        display2.setText("");
        display.setText("");
        currentValue = 0.0;
        currentOperator = "";
    }

    private void factorial() {
        if (!display.getText().isEmpty()) {
            double value = Double.parseDouble(display.getText());
            for (int j = 0; j < display.getText().length(); j++) {
                display2.setText(display2.getText().substring(0, display2.getText().length() - 1));
            }
            if (value >= 0) {
                double result = 1;
                for (int i = 1; i <= value; i++) {
                    result *= i;
                }
                display.setText(String.valueOf(result));
                display2.setText(display2.getText() + value + "!");
            } else {
                display.setText("Error");
                display2.setText("Invalid input for factorial");
            }
        }
    }

    private void percentage(String currentOperator) {
        if (!currentOperator.isEmpty()) {
            initialNumber = currentValue; // Зберігаємо початкове число

            double value = Double.parseDouble(display.getText());
            display2.setText(" " + display2.getText() + currentOperator);
            display.setText("");
            calculate(value);
        }
    }

    private void calculate(double value) { // Метод який відповідає за деякі обчислення
        switch (currentOperator) {
            case "+":
                currentValue += value;
                break;
            case "-":
                currentValue -= value;

                break;
            case "*":
                currentValue *= value;
                break;
            case "/":
                if (value != 0) { // Перевірка ділення на 0
                    currentValue /= value;
                } else {
                    currentValue = 0.0;
                    display.setText("Error");
                    display2.setText("Cannot divide by zero");
                    return;
                }
                break;
            case "%":
                double percentage = initialNumber * (value / 100.0); // Обчисліть відсоток від початкового числа
                currentValue = initialNumber - percentage; // Зменште відсоток від початкового числа
                display2.setText(" " + display2.getText() + "(" + percentage + ")");
                break;

        }

        display2.setText(display2.getText() + "= " + currentValue);
        display.setText(String.valueOf(currentValue));
    }

    public static void main(String[] args) {
        JFrame.setDefaultLookAndFeelDecorated(true);// Зміна оформлення ввікна
        CalculatorApp calculator = new CalculatorApp();
    }
} 
