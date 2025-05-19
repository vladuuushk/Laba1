/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

/**
 *
 * @author vladshuvaev
 */

public class App extends JFrame{
    private final JButton importButton, calculationButton, exportButton, exitButton;
    private final JTextArea outputArea;

    public App() {        
        setTitle("Программа расчета статичстических данных");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        importButton = new JButton("Импорт данных");
        calculationButton = new JButton("Расчет данных");
        exportButton = new JButton("Экспорт данных");
        exitButton = new JButton("Выход");
        outputArea = new JTextArea();

        // Настройка кнопок для правильного отображения цветов
        configureButton(importButton, Color.YELLOW);
        configureButton(exportButton, Color.YELLOW);
        configureButton(calculationButton, Color.GREEN);
        configureButton(exitButton, Color.RED);

        // Устанавливаем цвет текста для лучшей читаемости
        exitButton.setForeground(Color.WHITE);
        
        JPanel panel = new JPanel();
        panel.add(importButton);
        panel.add(calculationButton);
        panel.add(exportButton);
        panel.add(exitButton);
        panel.add(new JLabel("Введите надежность для расчета доверительного интервала:"));

        add(panel, BorderLayout.NORTH);
        add(new JScrollPane(outputArea), BorderLayout.CENTER);

        setVisible(true);
    }

    private void configureButton(JButton button, Color color) {
        button.setBackground(color);
        button.setOpaque(true);
        button.setBorderPainted(false);
        button.setContentAreaFilled(true);
    }

    public JButton getImportButton() {return importButton;}
    public JButton getCalculationButton() {return calculationButton;}
    public JButton getExportButton() {return exportButton;}
    public JButton getExitButton() {return exitButton;}
    public JTextArea getOutputArea() {return outputArea;}
}