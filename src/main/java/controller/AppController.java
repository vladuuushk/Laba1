/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import model.Model;
import model.Statistics;
import model.Result;
import view.App;
import view.Error;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import org.apache.poi.ooxml.POIXMLException;
import org.apache.poi.openxml4j.exceptions.NotOfficeXmlFileException;

/**
 *
 * @author vladshuvaev
 */

public class AppController {
    private final App view;
    private final Model model;
    private final Statistics statistics;
    private double confidenceLevel = 0.95;

    public AppController(App view, Model model) {
        this.view = view;
        this.model = model;
        this.statistics = new Statistics(model);
        attachEvents();
    }

    private void attachEvents() {
        view.getImportButton().addActionListener(e -> importData());
        view.getCalculationButton().addActionListener(e -> calculationData());
        view.getExportButton().addActionListener(e -> exportData());
        view.getExitButton().addActionListener(e -> System.exit(0));
    }
    
    private void importData() {
        JFileChooser fileChooser = new JFileChooser();
        int option = fileChooser.showOpenDialog(view);
        
        if (option == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            String fileName = file.getName();
        
            try (FileInputStream fis = new FileInputStream(file);
            Workbook workbook = new XSSFWorkbook(fis)) {
                List<String> sheetNames = new ArrayList<>();
                for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
                    sheetNames.add(workbook.getSheetName(i));
                }
                String selectedSheet = (String) JOptionPane.showInputDialog(view, "Выберите страницу для импорта:", "Выбор страницы", JOptionPane.QUESTION_MESSAGE, null, sheetNames.toArray(), sheetNames.get(0));
                if (selectedSheet != null) {
                    Sheet sheet = workbook.getSheet(selectedSheet);
                    model.clearData();
                    int numColumns = sheet.getRow(0).getPhysicalNumberOfCells();

                    for (int i = 0; i < numColumns; i++) {
                        model.addSample(new ArrayList<>(), "Выборка " + (i + 1));
                    }
                    
                    FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
                    
                    for (Row row : sheet) {
                        for (int i = 0; i < numColumns; i++) {
                            Cell cell = row.getCell(i, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                            double value = 0;
                            if (cell.getCellType() == CellType.NUMERIC) {
                                value = cell.getNumericCellValue();
                            }
                            else if (cell.getCellType() == CellType.FORMULA) {
                                CellValue cellValue = evaluator.evaluate(cell);
                                value = cellValue.getNumberValue();
                            }
                            else{
                                continue;
                            }
                            model.getData().get(i).add(value);
                        }
                    }
                    view.getOutputArea().setText("Данные успешно импортированы");
                }
            } catch (POIXMLException | NotOfficeXmlFileException e) {
                Error.showError("Файл не является корректным Excel-документом (.xlsx)");
            } catch (IOException e) {
                Error.showError("Ошибка чтения файла: " + e.getMessage());
            } catch (IllegalArgumentException e) {
                Error.showError("Ошибка чтения файла: " + e.getMessage());
            }
        }
    }
    
    private void calculationData() {
        view.getOutputArea().setText("");
        
        if (model.getData() == null || model.getData().isEmpty()) {
            Error.showError("Нет данных для обработки");
            return;
        }
        
        boolean[] selectedOptions = showCalculationOptionsDialog();
        if (selectedOptions == null) {
            return; 
        }
    
        if (selectedOptions[9]) {
            String input = JOptionPane.showInputDialog(view, "Введите уровень доверия (например, 0.95 для 95%):", "Ввод уровня доверия", JOptionPane.QUESTION_MESSAGE);
            
            if (input == null || input.trim().isEmpty()) {
                Error.showError("Уровень доверия не введен.");
                return;
            }

            try {
                confidenceLevel = Double.parseDouble(input);
                if (confidenceLevel <= 0 || confidenceLevel >= 1) {
                    throw new IllegalArgumentException("Уровень доверия должен быть между 0 и 1.");
                }
            } catch (NumberFormatException e) {
                Error.showError("Некорректный уровень доверия. Введите число между 0 и 1.");
                return;
            } catch (IllegalArgumentException e) {
                Error.showError(e.getMessage());
                return;
            }
        }
        
            Statistics stats = new Statistics(model);
            List<Result> results = stats.calculateAll();
            List<Double> covariances = stats.calculateCovariance();
            StringBuilder resultText = new StringBuilder();

            for (int i = 0; i < results.size(); i++) {
                Result result = results.get(i);
                double[] confidenceInterval = statistics.calculateConfidenceInterval(confidenceLevel, i);
                resultText.append("Статистические показатели для выборки '")
                          .append(result.getSampleName())
                          .append("':\n");
                if (selectedOptions[0]) {
                resultText.append("Среднее геометрическое: ")
                          .append(result.getGeometricMean())
                          .append("\n");
                }
                if (selectedOptions[1]) {
                resultText.append("Среднее арифметическое: ")
                          .append(result.getMean())
                          .append("\n");
                }
                if (selectedOptions[2]) {
                resultText.append("Стандартное отклонение: ")
                          .append(result.getStdDev())
                          .append("\n");
                }
                if (selectedOptions[3]) {
                resultText.append("Размах: ")
                          .append(result.getRange())
                          .append("\n");
                }
                if (selectedOptions[4]) {
                resultText.append("Количество элементов: ")
                          .append(result.getCount())
                          .append("\n");
                }
                if (selectedOptions[5]) {
                resultText.append("Коэффициент вариации: ")
                          .append(result.getCoefficientOfVariation())
                          .append("\n");
                }
                if (selectedOptions[6]) {
                resultText.append("Минимум: ")
                          .append(result.getMin())
                          .append("\n");
                }
                if (selectedOptions[7]) {
                resultText.append("Максимум: ")
                          .append(result.getMax())
                          .append("\n");
                }
                if (selectedOptions[8]) {
                 resultText.append("Дисперсия: ")
                          .append(result.getVariance())
                          .append("\n");
                }
                if (selectedOptions[9]) {
                resultText.append("Доверительный интвервал: ")
                          .append(confidenceInterval[0])
                          .append(", ")
                          .append(confidenceInterval[1])
                          .append("\n\n");
                }
            }
        
            if (selectedOptions[10]) {
                resultText.append("Ковариации:\n");
                int covIndex = 0;
                for (int i = 0; i < model.getData().size(); i++) {
                    for (int j = 0; j < model.getData().size(); j++) {
                        resultText.append("Ковариация между '")
                                  .append(model.getSampleNames().get(i))
                                  .append("' и '")
                                  .append(model.getSampleNames().get(j))
                                  .append("': ")
                                  .append(covariances.get(covIndex++))
                                  .append("\n");
                    }   
                }
            }
            view.getOutputArea().setText(resultText.toString()); 
    }

        private void exportData() {
            JFileChooser fileChooser = new JFileChooser();
            int option = fileChooser.showSaveDialog(view);
            if (option == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                try (Workbook workbook = new XSSFWorkbook();
                    FileOutputStream fos = new FileOutputStream(file + ".xlsx")) {
                        Sheet sheet = workbook.createSheet("Результаты статистики");
                        Row headerRow = sheet.createRow(0);
                        headerRow.createCell(0).setCellValue("Выборка");
                        headerRow.createCell(1).setCellValue("Среднее геометрическое");
                        headerRow.createCell(2).setCellValue("Среднее арифметическое");
                        headerRow.createCell(3).setCellValue("Стандартное отклонение");
                        headerRow.createCell(4).setCellValue("Размах");
                        headerRow.createCell(5).setCellValue("Количество элементов");
                        headerRow.createCell(6).setCellValue("Коэффициент вариации");
                        headerRow.createCell(7).setCellValue("Минимум");
                        headerRow.createCell(8).setCellValue("Максимум");
                        headerRow.createCell(9).setCellValue("Дисперсия");
                        headerRow.createCell(10).setCellValue("Доверительный интверал (" + (confidenceLevel * 100) + "%)");

                    List<Result> results = statistics.calculateAll();

                    int rowIndex = 1; 
                    for (int i = 0; i < results.size(); i++) {
                        Result result = results.get(i);
                        double[] confidenceInterval = statistics.calculateConfidenceInterval(confidenceLevel, i);
                        Row row = sheet.createRow(rowIndex++);
                        row.createCell(0).setCellValue(result.getSampleName());
                        row.createCell(1).setCellValue(result.getGeometricMean());
                        row.createCell(2).setCellValue(result.getMean());
                        row.createCell(3).setCellValue(result.getStdDev());
                        row.createCell(4).setCellValue(result.getRange());
                        row.createCell(5).setCellValue(result.getCount());
                        row.createCell(6).setCellValue(result.getCoefficientOfVariation());
                        row.createCell(7).setCellValue(result.getMin());
                        row.createCell(8).setCellValue(result.getMax());
                        row.createCell(9).setCellValue(result.getVariance());
                        row.createCell(10).setCellValue("[" + confidenceInterval[0] + ", " + confidenceInterval[1] + "]");
                    }

                    Row covarianceHeaderRow = sheet.createRow(rowIndex++);
                    covarianceHeaderRow.createCell(0).setCellValue("Ковариация");
                    List<Double> covariances = statistics.calculateCovariance();
                    int covIndex = 0;
                    for (int i = 0; i < model.getData().size(); i++) {
                        for (int j = 0; j < model.getData().size(); j++) {
                            Row row = sheet.createRow(rowIndex++);
                            row.createCell(0).setCellValue("Ковариация между " + model.getSampleNames().get(i) + " и " + model.getSampleNames().get(j));
                            row.createCell(1).setCellValue(covariances.get(covIndex++));
                        }
                    }
                    workbook.write(fos);
                    JOptionPane.showMessageDialog(view, "Результаты успешно экспортированы");
                    } catch (IOException ex) {
                    Error.showError("Ошибка экспорта: " + ex.getMessage());
                }
            }
        }
        private boolean[] showCalculationOptionsDialog() {
            String[] options = {
                "Среднее геометрическое",
                "Среднее арифметическое",
                "Стандартное отклонение",
                "Размах",
                "Количество элементов",
                "Коэффициент вариации",
                "Минимум",
                "Максимум",
                "Дисперсия",
                "Доверительный интервал",
                "Ковариация"
            };

            JCheckBox[] checkBoxes = new JCheckBox[options.length];
            for (int i = 0; i < options.length; i++) {
                checkBoxes[i] = new JCheckBox(options[i]);
                checkBoxes[i].setSelected(true);
            }

            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            for (JCheckBox checkBox : checkBoxes) {
                panel.add(checkBox);
            }

            int result = JOptionPane.showConfirmDialog(
                view,
                panel,
                "Выберите пункты для расчета",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
            );

            if (result == JOptionPane.OK_OPTION) {
                boolean[] selectedOptions = new boolean[options.length];
                for (int i = 0; i < checkBoxes.length; i++) {
                    selectedOptions[i] = checkBoxes[i].isSelected();
                }
                return selectedOptions;
            } else {
                return null;
        }
    }
}
