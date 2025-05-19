/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.math3.distribution.TDistribution;
import org.apache.commons.math3.stat.correlation.Covariance;
import org.apache.commons.math3.stat.descriptive.*;

/**
 *
 * @author vladshuvaev
 */

public class Statistics {
    private final Model dataModel;
   
    public Statistics(Model dataModel) {
        this.dataModel = dataModel;
    }
    
    public List<Double> calculateCovariance(){
        Covariance cov = new Covariance();
        List<Double> covarianceValues = new ArrayList<>();
        List<List<Double>> data = dataModel.getData();
        for (int i = 0; i < data.size(); i++) {
            for (int j = 0; j < data.size(); j++) {
                double[] dataArray1 = data.get(i).stream().mapToDouble(Double::doubleValue).toArray();
                double[] dataArray2 = data.get(j).stream().mapToDouble(Double::doubleValue).toArray();
                double covarianceValue = cov.covariance(dataArray1, dataArray2);
                covarianceValues.add(covarianceValue);
            }
        }
     return covarianceValues;
    }

    public List<Result> calculateAll() {
        List<Result> results = new ArrayList<>();
        List<List<Double>> data = dataModel.getData();
        List<String> sampleNames = dataModel.getSampleNames();

        for (int i = 0; i < data.size(); i++) {
            DescriptiveStatistics stats = new DescriptiveStatistics();
            for (double value : data.get(i)) {
                stats.addValue(value);
            }

            double geometricMean = stats.getGeometricMean();
            double mean = stats.getMean();
            double stdDev = stats.getStandardDeviation();
            double range = stats.getMax() - stats.getMin();
            double number = stats.getN();
            double coefficientOfVariation = (stdDev / mean) * 100;
            double min = stats.getMin();
            double max = stats.getMax();
            double variance = stats.getVariance();
            
        results.add(new Result(
            sampleNames.get(i), geometricMean, mean, stdDev, range, number, coefficientOfVariation, min, max, variance
        ));
    }

    return results;
}
    
    public double[] calculateConfidenceInterval(double confidenceLevel, int sampleIndex){
        DescriptiveStatistics stats = new DescriptiveStatistics();
        for (double value : dataModel.getData().get(sampleIndex)) {
            stats.addValue(value);
    }
        double mean = stats.getMean(); 
        double variance = stats.getVariance(); 
        double n = stats.getN(); 
        double alpha = 1 - confidenceLevel;
        TDistribution tDistribution = new TDistribution(n - 1);
        double tValue = tDistribution.inverseCumulativeProbability(1 - alpha / 2);
        double marginOfError = tValue * (Math.sqrt(variance/n));
        return new double[]{mean - marginOfError, mean + marginOfError};
    }
}



