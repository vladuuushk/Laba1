/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author vladshuvaev
 */

public class Result {
    private String sampleName;
    private double geometricMean;
    private double mean;
    private double stdDev;
    private double range;
    private double count;
    private double coefficientOfVariation;
    private double min;
    private double max;
    private double variance;

    public Result(String sampleName, double geometricMean, double mean, double stdDev, double range, double count, double coefficientOfVariation, double min, double max, double variance) {
        this.sampleName = sampleName;
        this.geometricMean = geometricMean;
        this.mean = mean;
        this.stdDev = stdDev;
        this.range = range;
        this.count = count;
        this.coefficientOfVariation = coefficientOfVariation;
        this.min = min;
        this.max = max;
        this.variance = variance;
    }

    public String getSampleName() { return sampleName; }
    public double getGeometricMean() { return geometricMean; }
    public double getMean() { return mean; }
    public double getStdDev() { return stdDev; }
    public double getRange() { return range; }
    public double getCount() { return count; }
    public double getCoefficientOfVariation() { return coefficientOfVariation;}
    public double getMin() {return min;}
    public double getMax() {return max;}
    public double getVariance() {return variance;}
}
