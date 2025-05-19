/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author vladshuvaev
 */
public class Model {
    private final List<List<Double>> data;
    private final List<String> sampleNames;
    
    public Model() {
        this.data = new ArrayList<>();
        this.sampleNames = new ArrayList<>();
    }
    
    public List<List<Double>> getData() {
        return data;
    }

    public List<String> getSampleNames() {
        return sampleNames;
    }

    public void clearData() {
        data.clear();
        sampleNames.clear();
    }

    public void addSample(List<Double> sample, String sampleName) {
        data.add(sample);
        sampleNames.add(sampleName);
    }
}
