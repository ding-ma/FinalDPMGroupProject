package ca.mcgill.ecse211;

import static ca.mcgill.ecse211.Resources.*;

public class SensorsPoller implements Runnable {
    
    private static double currentLightIntensityRight;
    private static double previousLightIntensityRight;
    private static double currentLightIntensityLeft;
    private static double previousLightIntensityLeft;
    private static boolean isLineHitRight = false;
    
    private static int currentDistance;
    private static boolean isIsLineHitLeft = false;
    private float[] usData = new float[US_SENSOR.sampleSize()];
    private float[] lightDataRight = new float[COLOR_SENSOR_RIGHT.sampleSize()];
    private float[] previousLightRight = new float[COLOR_SENSOR_RIGHT.sampleSize()];
    private float[] lightDataLeft = new float[COLOR_SENSOR_LEFT.sampleSize()];
    private float[] previousLightLeft = new float[COLOR_SENSOR_LEFT.sampleSize()];
    
    public static int getCurrentDistance() {
        return currentDistance;
    }
    
    public static double getCurrentLightIntensityRight() {
        return currentLightIntensityRight;
    }
    
    private void setCurrentLightIntensityRight(double lightIntensity) {
        currentLightIntensityRight = lightIntensity;
    }
    
    public static double getPreviousLightIntensityRight() {
        return previousLightIntensityRight;
    }
    
    private void setPreviousLightIntensityRight(double prevLightIntensity) {
        previousLightIntensityRight = prevLightIntensity;
    }
    
    public static double getCurrentLightIntensityLeft() {
        return currentLightIntensityLeft;
    }
    
    private void setCurrentLightIntensityLeft(double lightIntensity) {
        currentLightIntensityLeft = lightIntensity;
    }
    
    public static double getPreviousLightIntensityLeft() {
        return previousLightIntensityLeft;
    }
    
    private void setPreviousLightIntensityLeft(double previousLightIntensity) {
        previousLightIntensityLeft = previousLightIntensity;
    }
    
    public static boolean getIsLineHitRight() {
        return isLineHitRight;
    }
    
    public static boolean getIsLineHitLeft() {
        return isIsLineHitLeft;
    }
    
    @Override
    public void run() {
        COLOR_SENSOR_RIGHT.getMode("Red").fetchSample(lightDataRight, 0);
        setPreviousLightIntensityRight(lightDataRight[0]);
    
        COLOR_SENSOR_LEFT.getMode("Red").fetchSample(lightDataLeft, 0);
        setPreviousLightIntensityLeft(lightDataLeft[0]);
    
        while (true) {
            US_SENSOR.getDistanceMode().fetchSample(usData, 0);
            setUsData((int) (usData[0] * 100));
        
            COLOR_SENSOR_RIGHT.getMode("Red").fetchSample(previousLightRight, 0);
            setCurrentLightIntensityRight(previousLightRight[0]);
        
            COLOR_SENSOR_LEFT.getMode("Red").fetchSample(previousLightLeft, 0);
            setCurrentLightIntensityLeft(previousLightLeft[0]);
        
            setIsLineHitRight(getCurrentLightIntensityRight(), getPreviousLightIntensityRight());
            setIsLineHitLeft(getCurrentLightIntensityLeft(), getPreviousLightIntensityLeft());
            try {
                Thread.sleep(65);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    
    }
    
    private void setUsData(int distance) {
        currentDistance = distance;
    }
    
    public void setIsLineHitRight(double currentLightIntensity, double previousLightIntensity) {
        double diffValue = currentLightIntensity - previousLightIntensity;
        if (diffValue < 0)
            isLineHitRight = false;
        if (diffValue > 0.1 && !isLineHitRight) {
            isLineHitRight = true;
        }
        setPreviousLightIntensityRight(currentLightIntensity);
        
    }
    
    public void setIsLineHitLeft(double currentLightIntensity, double previousLightIntensity) {
        double diffValue = currentLightIntensity - previousLightIntensity;
        if (diffValue < 0)
            isIsLineHitLeft = false;
        if (diffValue > 0.1 && !isIsLineHitLeft) {
            isIsLineHitLeft = true;
        }
        setPreviousLightIntensityLeft(currentLightIntensity);
    }
}